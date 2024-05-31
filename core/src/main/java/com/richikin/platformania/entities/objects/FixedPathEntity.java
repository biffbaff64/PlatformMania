package com.richikin.platformania.entities.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.logging.StopWatch;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.maths.SimpleVec2F;
import com.richikin.platformania.physics.Movement;
import com.richikin.platformania.ui.ProgressBar;
import com.richikin.platformania.core.App;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.physics.PhysicsBodyType;
import com.richikin.platformania.physics.Proximity;

import java.util.concurrent.TimeUnit;

public class FixedPathEntity extends BoundedEntity
{
    public boolean isFlying;
    public boolean isAttacker;
    public boolean isJumper;
    public boolean shouldPauseAtBoundsTurn;

    protected int         ratDelay;
    protected int         standDelay;
    protected Proximity   proximity;
    protected ProgressBar strengthBar;
    protected boolean     hasStrengthBar;

    private final StopWatch standTimer;
    private final StopWatch randomAttackTimer;

    private boolean hasJumped;

    public FixedPathEntity( GraphicID gid )
    {
        super( gid );

        standTimer        = new StopWatch();
        randomAttackTimer = new StopWatch();

        isFlying                = false;
        isAttacker              = false;
        isJumper                = false;
        hasJumped               = false;
        hasStrengthBar          = false;
        shouldPauseAtBoundsTurn = false;
    }

    @Override
    public void initialise( SpriteDescriptor descriptor )
    {
        create( descriptor );

        setFilters();

        createBody( PhysicsBodyType._DYNAMIC );

        if ( isFlying )
        {
            getPhysicsBody().body.setGravityScale( 0 );
        }

        direction.set( descriptor._DIR );
        distance.set( descriptor._DIST );
        distance.mul( App.getMapData().tileWidth, App.getMapData().tileHeight );
        distanceReset = new SimpleVec2F( distance );
        speed.set( descriptor._SPEED );

        setMovementBounds();

        if ( hasStrengthBar )
        {
            strengthBar = new ProgressBar( 0.25f, 100, 100, "bar9", true );
            strengthBar.setHeightColorScale( 4.0f, Color.GREEN, 0.25f );
        }

        isAnimating      = true;
        isDrawable       = true;
        shouldForceTurnX = false;
        shouldForceTurnY = false;

        standDelay = 200 + ( MathUtils.random( 20 ) * 100 );
        ratDelay   = 2000 + ( MathUtils.random( 30 ) * 100 );

        proximity = new Proximity();

        setActionState( ActionStates._STANDING );
    }

    @Override
    public void update()
    {
        switch ( getActionState() )
        {
            case _STANDING:
            {
                if ( standTimer.time( TimeUnit.MILLISECONDS ) > standDelay )
                {
                    if ( isAttacker )
                    {
                        randomAttackTimer.reset();
                        ratDelay = 2000 + ( MathUtils.random( 30 ) * 100 );
                    }

                    setActionState( ActionStates._RUNNING );
                }
            }
            break;

            case _RUNNING:
            {
                move();

                if ( isAttacker )
                {
                    if ( isTouchingPlayer )
                    {
                        setActionState( ActionStates._FIGHTING );

                        // Make sure this entity is facing the player when fighting
                        if ( ( lookingAt.getX() == Movement._DIRECTION_LEFT )
                            && ( App.getPlayer().sprite.getX() > sprite.getX() ) )
                        {
                            isFlippedX       = false;
                            shouldForceTurnX = true;
                        }
                        else if ( ( lookingAt.getX() == Movement._DIRECTION_RIGHT )
                            && ( App.getPlayer().sprite.getX() < sprite.getX() ) )
                        {
                            isFlippedX       = true;
                            shouldForceTurnX = true;
                        }

                        lookingAt.set( direction );
                    }
                }
            }
            break;

            case _JUMPING:
            {
                if ( !hasJumped )
                {
                    getPhysicsBody().body.setLinearVelocity( 0, App.getMapData().tileHeight * 1.5f );

                    hasJumped = true;
                }

                if ( getPhysicsBody().body.getLinearVelocity().y < 0 )
                {
                    setActionState( ActionStates._STANDING );

                    standTimer.reset();
                    isAnimating = true;
                }
            }
            break;

            case _FIGHTING:
            {
                if ( animation.isAnimationFinished( elapsedAnimTime ) )
                {
                    setActionState( ActionStates._STANDING );

                    standTimer.reset();
                }
            }
            break;

            case _DYING:
            {
                setActionState( ActionStates._DEAD );
            }
            break;

            default:
            {
                Trace.err( "Unsupported sprite action: ", getActionState() );
            }
            break;
        }

        if ( isTouchingPlayer
            && ( App.getPlayer().getActionState() == ActionStates._FIGHTING ) )
        {
            strength = Math.max( 0, ( strength - 10 ) );

            if ( strengthBar != null )
            {
                strengthBar.setTotal( strength );
            }

            if ( strength <= 0 )
            {
                strength = 0;
                setActionState( ActionStates._DYING );
            }
            else
            {
                shouldForceTurnX = true;
                shouldForceTurnY = true;
            }
        }

        animate();

        updateCommon();
    }

    protected void move()
    {
        getPhysicsBody().body.setLinearVelocity
            (
                ( speed.getX() * direction.getX() ),
                ( speed.getY() * direction.getY() )
            );

        lookingAt.set( direction );

        isFlippedX = ( direction.getX() == Movement._DIRECTION_LEFT );
        isFlippedY = false;

        checkMovementBounds();
    }

    @Override
    public void onMovementBoundsTurn( int edgeSide )
    {
        if ( ( edgeSide == Movement._DIRECTION_LEFT )
            || ( edgeSide == Movement._DIRECTION_RIGHT ) )
        {
            if ( isJumper )
            {
                isAnimating = false;
                hasJumped   = false;

                getPhysicsBody().body.setLinearVelocity( 0, 0 );

                setActionState( ActionStates._JUMPING );
            }
            else if ( shouldPauseAtBoundsTurn )
            {
                standTimer.reset();
                standDelay = 2000;

                getPhysicsBody().body.setLinearVelocity( 0, 0 );

                setActionState( ActionStates._STANDING );
            }
        }
    }

    public void positionStrengthBar()
    {
        if ( ( gid == GraphicID.G_WORM_MAN ) || ( gid == GraphicID.G_CROW_MAN ) )
        {
            int modifier = ( lookingAt.getX() == Movement._DIRECTION_LEFT ) ? 32 : 22;

            strengthBar.setPosition
                (
                    ( int ) ( getPhysicsBody().bodyBox.getX() + modifier ),
                    ( int ) ( getPhysicsBody().bodyBox.getY() + getPhysicsBody().bodyBox.height )
                );
        }
    }

    /**
     * Set the entity category masks used to check for
     * compatible collision types.
     */
    public void setFilters()
    {
        bodyCategory = Gfx.CAT_MOBILE_ENEMY;
        collidesWith = Gfx.CAT_PLAYER
            | Gfx.CAT_WEAPON
            | Gfx.CAT_STATIC_ENEMY
            | Gfx.CAT_USABLE
            | Gfx.CAT_GROUND;
    }
}
