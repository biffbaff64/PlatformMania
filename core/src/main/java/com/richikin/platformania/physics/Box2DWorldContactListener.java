package com.richikin.platformania.physics;

import com.badlogic.gdx.physics.box2d.*;
import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.entities.IEntityComponent;
import com.richikin.platformania.core.App;
import com.richikin.platformania.entities.actors.Pickup;
import com.richikin.platformania.entities.actors.hazards.DropBlock;
import com.richikin.platformania.graphics.Gfx;

public class Box2DWorldContactListener implements ContactListener
{
    private static final int _A = 0;
    private static final int _B = 1;

    private final BodyIdentity[] bodyIdentities;

    private int platformIndex;

    public Box2DWorldContactListener()
    {
        bodyIdentities = new BodyIdentity[ 2 ];
    }

    /**
     * Called when two fixtures begin to touch.
     */
    @Override
    public void beginContact( Contact contact )
    {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        bodyIdentities[ _A ] = ( BodyIdentity ) fixtureA.getBody().getUserData();
        bodyIdentities[ _B ] = ( BodyIdentity ) fixtureB.getBody().getUserData();

        for ( BodyIdentity bodyIdentity : bodyIdentities )
        {
            if ( bodyIdentity != null )
            {
                bodyIdentity.entity.getPhysicsBody().contactCount++;
            }
        }

        int catData = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        // Establish the contact edges.
//        checkAABB( fixtureA.getBody(), fixtureB.getBody() );

        checkForSame();

        if ( ( catData & ( Gfx.CAT_PLAYER | Gfx.CAT_PLATFORM ) ) == ( Gfx.CAT_PLAYER | Gfx.CAT_PLATFORM ) )
        {
            if ( playerIsAbovePlatform() )
            {
                App.getPlayer().isOnPlatform = true;
                App.getPlayer().setLink( platformIndex );
            }
            else
            {
                App.getPlayer().isOnPlatform = false;
                App.getPlayer().setLink( 0 );
            }
        }

        checkWeaponContact( catData );
        checkPlayerContactWithPushable( catData );
        checkPlayerContactWithCollectible( catData );
        checkEnemyContactWithOther( catData );

        //
        // Check for contact between Mobile Enemy and the Ground. This is for enemies
        // like Drop Blocks which crash into the ground, causing it to vibrate.
        if ( ( catData & ( Gfx.CAT_MOBILE_ENEMY | Gfx.CAT_GROUND ) ) == ( Gfx.CAT_MOBILE_ENEMY | Gfx.CAT_GROUND ) )
        {
            for ( BodyIdentity bodyIdentity : bodyIdentities )
            {
                if ( ( bodyIdentity != null )
                    && bodyIdentity.entity.isHittable
                    && ( bodyIdentity.entity.getGID() == GraphicID.G_DROP_BLOCK ) )
                {
                    ( ( DropBlock ) bodyIdentity.entity ).canShake = true;
                    bodyIdentity.entity.isHittable                 = false;
                }
            }
        }
    }

    /**
     * Called when two fixtures cease to touch.
     */
    @Override
    public void endContact( Contact contact )
    {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        BodyIdentity bodyIdentityA = ( BodyIdentity ) fixtureA.getBody().getUserData();
        BodyIdentity bodyIdentityB = ( BodyIdentity ) fixtureB.getBody().getUserData();

        if ( bodyIdentityA != null )
        {
            bodyIdentityA.entity.getPhysicsBody().contactCount--;

            if ( ( bodyIdentityA.entity.getBodyCategory() == Gfx.CAT_STATIC_ENEMY )
                || ( bodyIdentityA.entity.getBodyCategory() == Gfx.CAT_MOBILE_ENEMY ) )
            {
                bodyIdentityA.entity.isTouchingPlayer = false;
            }

            if ( ( bodyIdentityA.entity.getBodyCategory() == Gfx.CAT_PLAYER )
                && ( bodyIdentityB != null )
                && ( bodyIdentityB.entity.getBodyCategory() != Gfx.CAT_PLATFORM ) )
            {
                App.getPlayer().isOnPlatform = false;
                App.getPlayer().setLink( 0 );
            }

            bodyIdentityA.entity.isHittingSame = false;
            bodyIdentityA.entity.isHittingWeapon = false;
        }

        if ( bodyIdentityB != null )
        {
            bodyIdentityB.entity.getPhysicsBody().contactCount--;

            if ( ( bodyIdentityB.entity.getBodyCategory() == Gfx.CAT_STATIC_ENEMY )
                || ( bodyIdentityB.entity.getBodyCategory() == Gfx.CAT_MOBILE_ENEMY ) )
            {
                bodyIdentityB.entity.isTouchingPlayer = false;
            }

            if ( ( bodyIdentityB.entity.getBodyCategory() == Gfx.CAT_PLAYER )
                && ( bodyIdentityA != null )
                && ( bodyIdentityA.entity.getBodyCategory() != Gfx.CAT_PLATFORM ) )
            {
                App.getPlayer().isOnPlatform = false;
                App.getPlayer().setLink( 0 );
            }

            bodyIdentityB.entity.isHittingSame = false;
            bodyIdentityB.entity.isHittingWeapon = false;
        }
    }

    /**
     * This is called after a contact is updated. This allows you to
     * inspect a contact before it goes to the solver. If you are careful,
     * you can modify the contact manifold (e.g. disable contact).
     * A copy of the old manifold is provided so that you can detect
     * changes.
     * <p>
     * Note: this is called only for awake bodies.
     * Note: this is called even when the number of contact points is zero.
     * Note: this is not called for sensors.
     * Note: if you set the number of contact points to zero, you will not
     * get an EndContact callback. However, you may get a BeginContact
     * callback the next step.
     */
    @Override
    public void preSolve( Contact contact, Manifold oldManifold )
    {
    }

    /**
     * This lets you inspect a contact after the solver is finished.
     * This is useful for inspecting impulses.
     * <p>
     * Note: the contact manifold does not include time of impact impulses,
     * which can be arbitrarily large if the sub-step is small. Hence the
     * impulse is provided explicitly in a separate data structure.
     * <p>
     * Note: this is only called for contacts that are touching, solid, and awake.
     */
    @Override
    public void postSolve( Contact contact, ContactImpulse impulse )
    {
    }

    private void checkWeaponContact( int catData )
    {
        int contact = catData & ( Gfx.CAT_WEAPON
            | Gfx.CAT_ENEMY
            | Gfx.CAT_USABLE
            | Gfx.CAT_JAIL
            | Gfx.CAT_OBSTACLE );

        if ( (bodyIdentities[0] != null) && (bodyIdentities[1] != null))
        {
            if ( bodyIdentities[0].entity.getGID() == GraphicID.G_PLAYER_KNIFE )
            {
                bodyIdentities[0].entity.setActionState( ActionStates._DYING );
                bodyIdentities[1].entity.isHittingWeapon = true;
            }
            else if ( bodyIdentities[1].entity.getGID() == GraphicID.G_PLAYER_KNIFE )
            {
                bodyIdentities[1].entity.setActionState( ActionStates._DYING );
                bodyIdentities[0].entity.isHittingWeapon = true;
            }
        }
    }

    /**
     * Fixed and Mobile Enemies colliding with pushables or the player.
     * NOTE: For DropBlocks, only contact with the BOTTOM of the block
     * will hurt the player. This allows the player to stand on the block.
     */
    private void checkEnemyContactWithOther( int catData )
    {
        int enemyContact = catData & ( Gfx.CAT_ENEMY | Gfx.CAT_USABLE | Gfx.CAT_PLAYER | Gfx.CAT_JAIL | Gfx.CAT_WALL );

        if ( ( enemyContact == ( Gfx.CAT_MOBILE_ENEMY | Gfx.CAT_USABLE ) )
            || ( enemyContact == ( Gfx.CAT_STATIC_ENEMY | Gfx.CAT_USABLE ) )
            || ( enemyContact == ( Gfx.CAT_MOBILE_ENEMY | Gfx.CAT_PLAYER ) )
            || ( enemyContact == ( Gfx.CAT_MOBILE_ENEMY | Gfx.CAT_JAIL ) )
            || ( enemyContact == ( Gfx.CAT_MOBILE_ENEMY | Gfx.CAT_STATIC_ENEMY ) )
            || ( enemyContact == ( Gfx.CAT_MOBILE_ENEMY | Gfx.CAT_WALL ) )
            || ( enemyContact == ( Gfx.CAT_STATIC_ENEMY | Gfx.CAT_PLAYER ) ) )
        {
            // ixA is bodyIdentityA
            // ixB is bodyIdentityB
            for ( int ixA = 0, ixB = 1; ixA < 2; ixA++, ixB = ( ( ixB + 1 ) & 0x01 ) )
            {
                if ( bodyIdentities[ ixA ] != null )
                {
                    if ( bodyIdentities[ ixA ].entity.getBodyCategory() == Gfx.CAT_MOBILE_ENEMY )
                    {
                        if ( ( bodyIdentities[ ixB ] != null )
                            && ( bodyIdentities[ ixB ].entity.getGID() == GraphicID.G_SPIKES ) )
                        {
                            bodyIdentities[ ixA ].entity.setActionState( ActionStates._DYING );
                        }
                        else
                        {
                            bodyIdentities[ ixA ].entity.shouldForceTurnX = true;
                        }
                    }

                    if ( bodyIdentities[ ixA ].entity.getBodyCategory() == Gfx.CAT_PLAYER )
                    {
                        App.getPlayer().isTouchingEnemy = true;

                        if ( bodyIdentities[ ixB ] != null )
                        {
                            if ( ( bodyIdentities[ ixB ].entity.getBodyCategory() == Gfx.CAT_MOBILE_ENEMY )
                                || ( bodyIdentities[ ixB ].entity.getBodyCategory() == Gfx.CAT_STATIC_ENEMY ) )
                            {
                                bodyIdentities[ ixB ].entity.isTouchingPlayer = true;
                            }

                            if ( bodyIdentities[ ixB ].gid == GraphicID.G_DROP_BLOCK )
                            {
                                // If the player is above the drop block, maintain contact
                                // but make it non-lethal so he can stand on the block
                                if ( App.getPlayer().getBodyY() > bodyIdentities[ ixB ].entity.getBodyY() )
                                {
                                    App.getPlayer().isTouchingEnemy               = false;
                                    bodyIdentities[ ixB ].entity.isTouchingPlayer = false;
                                }
                            }

                            if ( bodyIdentities[ ixB ].gid == GraphicID.G_SPIKES )
                            {
                                App.getPlayer().setActionState( ActionStates._DYING );
                            }
                        }
                    }

                    if ( bodyIdentities[ ixA ].entity.getBodyCategory() == Gfx.CAT_STATIC_ENEMY )
                    {
                        if ( bodyIdentities[ ixB ] != null )
                        {
                            // Pushing a crate or boulder into plants kills them
                            if ( ( bodyIdentities[ ixB ].entity.getBodyCategory() == Gfx.CAT_USABLE )
                                && ( bodyIdentities[ ixA ].gid == GraphicID.G_PLANT ) )
                            {
                                bodyIdentities[ ixA ].entity.setActionState( ActionStates._SET_DYING );
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Check collision between player and pushable items
     */
    private void checkPlayerContactWithPushable( int catData )
    {
        if ( ( catData & ( Gfx.CAT_PLAYER | Gfx.CAT_USABLE ) ) == ( Gfx.CAT_PLAYER | Gfx.CAT_USABLE ) )
        {
            if ( ( bodyIdentities[ _A ] != null ) && ( bodyIdentities[ _B ] != null ) )
            {
                for ( BodyIdentity bodyIdentity : bodyIdentities )
                {
                    if ( ( bodyIdentity.entity.getGID() == GraphicID.G_PUSHABLE_CRATE )
                        || ( bodyIdentity.entity.getGID() == GraphicID.G_PUSHABLE_BOULDER )
                        || ( bodyIdentity.entity.getGID() == GraphicID.G_FLOOR_LEVER ) )
                    {
                        bodyIdentity.entity.isTouchingPlayer = true;

                        App.getPlayer().heldEntity = bodyIdentity.entity;
                    }
                }
            }
        }
    }

    /**
     * Check collision between player and collectible items
     */
    private void checkPlayerContactWithCollectible( int catData )
    {
        if ( ( catData & ( Gfx.CAT_PLAYER | Gfx.CAT_COLLECTIBLE ) ) == ( Gfx.CAT_PLAYER | Gfx.CAT_COLLECTIBLE ) )
        {
            if ( ( bodyIdentities[ _A ] != null ) && ( bodyIdentities[ _B ] != null ) )
            {
                if ( bodyIdentities[ _A ].entity.isHittable && bodyIdentities[ _B ].entity.isHittable )
                {
                    for ( BodyIdentity bodyIdentity : bodyIdentities )
                    {
                        if ( bodyIdentity.entity.getGID() == GraphicID.G_TREASURE_CHEST )
                        {
                            bodyIdentity.entity.isTouchingPlayer = true;
                            bodyIdentity.entity.isHittable       = false;
                        }
                        else
                        {
                            if ( bodyIdentity.entity.getType() == GraphicID._PICKUP )
                            {
                                ( ( Pickup ) bodyIdentity.entity ).setCollecting();
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns TRUE if the player is standing above any platform that he is
     * standing on. Also sets platformIndex to the sprite number of the
     * platform entity, so that the link between player and platform can be set.
     */
    private boolean playerIsAbovePlatform()
    {
        boolean isAbove = false;

        if ( ( bodyIdentities[ _A ] != null ) && ( bodyIdentities[ _B ] != null ) )
        {
            IEntityComponent player   = null;
            IEntityComponent platform = null;

            for ( BodyIdentity bodyIdentity : bodyIdentities )
            {
                if ( bodyIdentity.entity.getGID() == GraphicID.G_PLAYER )
                {
                    player = bodyIdentity.entity;
                }

                if ( bodyIdentity.entity.getGID() == GraphicID.G_MOVING_PLATFORM )
                {
                    platform      = bodyIdentity.entity;
                    platformIndex = bodyIdentity.entity.spriteNumber;
                }
            }

            if ( ( player != null ) && ( platform != null ) )
            {
                if ( player.getBodyY() > platform.getBodyY() )
                {
                    isAbove = true;
                }
            }
        }

        return isAbove;
    }

    private void checkForSame()
    {
        if ( ( bodyIdentities[ _A ] != null ) && ( bodyIdentities[ _B ] != null ) )
        {
            if ( bodyIdentities[ _A ].entity.gid == bodyIdentities[ _B ].entity.gid )
            {
                bodyIdentities[ _A ].entity.isHittingSame = true;
            }

            if ( bodyIdentities[ _B ].entity.gid == bodyIdentities[ _A ].entity.gid )
            {
                bodyIdentities[ _B ].entity.isHittingSame = true;
            }
        }
    }

    private void checkAABB( Body bodyA, Body bodyB )
    {
    }
}
