package com.richikin.platformania.entities.actors.enemies;

import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.physics.Movement;
import com.richikin.platformania.core.App;
import com.richikin.platformania.entities.objects.FixedPathEntity;
import com.richikin.platformania.entities.objects.SpriteDescriptor;
import com.richikin.platformania.graphics.Gfx;

public class Bat extends FixedPathEntity
{
    private float ticks = 0;

    public Bat()
    {
        super( GraphicID.G_BAT );

        isFlying = true;
    }

    @Override
    protected void move()
    {
        checkMovementBounds();

        if ( direction.hasYDirection() )
        {
            getPhysicsBody().body.setLinearVelocity
                (
                    ( speed.getX() * direction.getX() ),
                    ( speed.getY() * direction.getY() )
                );
        }
        else
        {
            ticks++;

            getPhysicsBody().body.setLinearVelocity
                (
                    ( speed.getX() * direction.getX() ),
                    ( float ) ( Math.sin( ( ticks * 0.5f * Math.PI ) / 16 ) )
                );
        }

        lookingAt.set( direction );

        isFlippedX = ( direction.getX() == Movement._DIRECTION_LEFT );
        isFlippedY = false;
    }

    @Override
    public void definePhysicsBodyBox( boolean useBodyPos )
    {
        getPhysicsBody().bodyBox.set( sprite.getX() + 8, sprite.getY() + 8, 16, 16 );
    }

    @Override
    public void setPositionFromBody()
    {
        if ( ( getPhysicsBody() != null ) && ( sprite != null ) )
        {
            sprite.setPosition
                (
                    ( ( getPhysicsBody().body.getPosition().x * Gfx._PPM ) - ( getPhysicsBody().bodyBox.width / 2f ) ) - 8,
                    ( getPhysicsBody().body.getPosition().y * Gfx._PPM ) - ( getPhysicsBody().bodyBox.height / 2f ) - 8
                );
        }
    }

    @Override
    public void setActionState( ActionStates action )
    {
        SpriteDescriptor descriptor;

        switch ( action )
        {
            case _STANDING:
            case _RUNNING:
                descriptor = App.getEntities().getDescriptor( GraphicID.G_BAT );
                setAnimation( descriptor );
                break;
            case _FIGHTING:
                descriptor = App.getEntities().getDescriptor( GraphicID.G_BAT_ATTACK );
                setAnimation( descriptor );
                break;
            default:
                break;
        }

        super.setActionState( action );
    }

    /**
     * Set the entity category masks used to check for
     * compatible collision types.
     */
    @Override
    public void setFilters()
    {
        bodyCategory = Gfx.CAT_MOBILE_ENEMY;
        collidesWith = Gfx.CAT_PLAYER
            | Gfx.CAT_WEAPON
            | Gfx.CAT_VILLAGER
            | Gfx.CAT_STATIC_ENEMY
            | Gfx.CAT_USABLE
            | Gfx.CAT_GROUND;
    }
}
