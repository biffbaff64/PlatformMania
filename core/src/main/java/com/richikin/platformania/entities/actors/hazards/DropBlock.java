package com.richikin.platformania.entities.actors.hazards;

import com.badlogic.gdx.math.MathUtils;
import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.graphics.camera.Shake;
import com.richikin.platformania.logging.StopWatch;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.maths.SimpleVec2F;
import com.richikin.platformania.physics.Movement;
import com.richikin.platformania.core.App;
import com.richikin.platformania.entities.objects.BoundedEntity;
import com.richikin.platformania.entities.objects.SpriteDescriptor;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.physics.PhysicsBodyType;

import java.util.concurrent.TimeUnit;

public class DropBlock extends BoundedEntity
{
    public boolean canShake;

    private final StopWatch stopWatch;

    private int delay;

    public DropBlock()
    {
        super( GraphicID.G_DROP_BLOCK );

        stopWatch = new StopWatch();
    }

    @Override
    public void initialise( SpriteDescriptor descriptor )
    {
        init();

        create( descriptor );

        bodyCategory = Gfx.CAT_MOBILE_ENEMY;
        collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_GROUND;

        createBody( PhysicsBodyType._DYNAMIC );

        getPhysicsBody().body.setGravityScale( 0 );

        direction.set( Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN );
        distance.set( descriptor._DIST );
        distance.mul( App.getMapData().tileWidth, App.getMapData().tileHeight );
        distanceReset = new SimpleVec2F( distance );

        setMovementBounds();

        isAnimating = false;
        isDrawable  = true;
        canShake    = false;

        stopWatch.reset();
        delay = 2000 + ( MathUtils.random( 100 ) * 10 );

        setActionState( ActionStates._STANDING );
    }

    @Override
    public void update()
    {
        switch ( getActionState() )
        {
            case _STANDING:
                getPhysicsBody().body.setLinearVelocity( 0, 0 );
                if ( stopWatch.time( TimeUnit.MILLISECONDS ) > delay )
                {
                    getPhysicsBody().body.setGravityScale( Gfx._FALL_GRAVITY );
                    setActionState( ActionStates._FALLING );

                    isHittable = true;
                }
                break;
            case _FALLING:
                if ( getPhysicsBody().body.getLinearVelocity().y < 0 )
                {
                    stopWatch.reset();
                    delay = 2000 + ( MathUtils.random( 100 ) * 10 );

                    setActionState( ActionStates._WAITING );
                }
                break;
// FYI: 'canShake' is set in Box2DContactContactListener.
            case _WAITING:
                if ( canShake && App.getEntityUtils().isOnScreen( this ) )
                {
                    Shake.start();
                    canShake = false;
                }
                if ( stopWatch.time( TimeUnit.MILLISECONDS ) > delay )
                {
                    getPhysicsBody().body.setGravityScale( 0 );
                    setActionState( ActionStates._CLIMBING );
                }
                break;
            case _CLIMBING:
                checkMovementBounds();
                if ( justTurnedAroundY )
                {
                    getPhysicsBody().body.setGravityScale( 0 );

                    stopWatch.reset();
                    delay = 2000 + ( MathUtils.random( 100 ) * 10 );

                    setActionState( ActionStates._STANDING );
                }
                else
                {
                    direction.setY( Movement._DIRECTION_UP );
                    getPhysicsBody().body.setLinearVelocity( 0, ( direction.getY() * 4 ) );
                }
                break;
            default:
                Trace.err( "Unsupported sprite action: ", getActionState() );
                break;
        }

        animate();

        updateCommon();
    }

    @Override
    public void animate()
    {
        sprite.setRegion( getActionState() == ActionStates._STANDING ? animFrames[ 0 ] : animFrames[ 1 ] );
    }

    @Override
    public void definePhysicsBodyBox( boolean useBodyPos )
    {
        getPhysicsBody().bodyBox.set( sprite.getX(), sprite.getY(), frameWidth, frameHeight );
    }

    @Override
    public void setPositionFromBody()
    {
        if ( ( getPhysicsBody() != null ) && ( sprite != null ) )
        {
            float newX = ( getPhysicsBody().body.getPosition().x * Gfx._PPM );
            float newY = ( getPhysicsBody().body.getPosition().y * Gfx._PPM );

            newX -= ( getPhysicsBody().bodyBox.width / 2f );
            newY -= ( getPhysicsBody().bodyBox.height / 2f );

            sprite.setPosition( newX, newY );
        }
    }
}
