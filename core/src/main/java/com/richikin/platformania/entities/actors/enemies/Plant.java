package com.richikin.platformania.entities.actors.enemies;

import com.badlogic.gdx.math.MathUtils;
import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.logging.StopWatch;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.core.App;
import com.richikin.platformania.entities.objects.GdxSprite;
import com.richikin.platformania.entities.objects.SpriteDescriptor;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.physics.PhysicsBodyType;
import com.richikin.platformania.physics.Proximity;

import java.util.concurrent.TimeUnit;

public class Plant extends GdxSprite
{
    private StopWatch stopWatch;
    private int       stopWatchTime;
    private Proximity proximity;

    public Plant()
    {
        super( GraphicID.G_PLANT );
    }

    @Override
    public void initialise( SpriteDescriptor descriptor )
    {
        create( descriptor );

        bodyCategory = Gfx.CAT_STATIC_ENEMY;
        collidesWith = Gfx.CAT_PLAYER
            | Gfx.CAT_WEAPON
            | Gfx.CAT_USABLE
            | Gfx.CAT_GROUND
            | Gfx.CAT_MOBILE_ENEMY;

        createBody( PhysicsBodyType._KINEMATIC );

        isAnimating = true;
        isDrawable  = true;
        canFlip     = false;

        proximity = new Proximity();

        stopWatch     = new StopWatch();
        stopWatchTime = 0;

        App.getAnimationUtils().randomiseAnimTime( this );

        setActionState( ActionStates._STANDING );
    }

    @Override
    public void update()
    {
        switch ( getActionState() )
        {
            case _STANDING:
                if ( proximity.isNear( this, App.getPlayer() )
                    && ( stopWatch.time( TimeUnit.MILLISECONDS ) > stopWatchTime ) )
                {
                    setActionState( ActionStates._FIGHTING );
                }
                break;
            case _FIGHTING:
                if ( animation.isAnimationFinished( elapsedAnimTime ) )
                {
                    setActionState( ActionStates._STANDING );
                    stopWatchTime = 100 + ( MathUtils.random( 10 ) * 100 );
                    stopWatch.reset();
                }
                break;
            case _SET_DYING:
                SpriteDescriptor descriptor = App.getEntities().getDescriptor( GraphicID.G_PLANT_HURT );
                setAnimation( descriptor );
                setActionState( ActionStates._DYING );
                break;
            case _DYING:
                if ( animation.isAnimationFinished( elapsedAnimTime ) )
                {
                    setActionState( ActionStates._DEAD );
                }
                break;
            default:
                Trace.err( "Unsupported action state: ", getActionState() );
                break;
        }

        animate();

        updateCommon();
    }

    /**
     * Defines a box to use for Box2D Physics body creation,
     * based on this sprite's co-ordinates and size.
     * This is the default implementation, and sets the box
     * size to the exact dimensions of an animation frame.
     * Override to create differing sizes.
     */
    @Override
    public void definePhysicsBodyBox( boolean useBodyPos )
    {
        getPhysicsBody().bodyBox.set( sprite.getX() + 21, sprite.getY(), 22, 40 );
    }

    /**
     * Sets the sprite position from the physics body coordinates
     */
    @Override
    public void setPositionFromBody()
    {
        if ( ( getPhysicsBody().body != null ) && ( sprite != null ) )
        {
            sprite.setPosition
                (
                    ( ( getPhysicsBody().body.getPosition().x * Gfx._PPM ) - ( getPhysicsBody().bodyBox.width / 2f ) ) - 22,
                    ( getPhysicsBody().body.getPosition().y * Gfx._PPM ) - ( getPhysicsBody().bodyBox.height / 2f )
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
                descriptor = App.getEntities().getDescriptor( GraphicID.G_PLANT );
                setAnimation( descriptor );
                break;
            case _FIGHTING:
                descriptor = App.getEntities().getDescriptor( GraphicID.G_PLANT_FIGHTING );
                setAnimation( descriptor );
                break;
            default:
                break;
        }

        super.setActionState( action );
    }
}
