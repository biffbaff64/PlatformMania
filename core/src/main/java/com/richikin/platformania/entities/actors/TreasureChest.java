package com.richikin.platformania.entities.actors;

import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.logging.StopWatch;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.core.App;
import com.richikin.platformania.entities.objects.GdxSprite;
import com.richikin.platformania.entities.objects.SpriteDescriptor;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.physics.PhysicsBodyType;

import java.util.concurrent.TimeUnit;

public class TreasureChest extends GdxSprite
{
    private static final int _COINS_FROM_CHEST = 25;

    private StopWatch stopWatch;
    private float     restingTime;

    public TreasureChest()
    {
        super( GraphicID.G_TREASURE_CHEST );
    }

    @Override
    public void initialise( SpriteDescriptor descriptor )
    {
        create( descriptor );

        bodyCategory = Gfx.CAT_COLLECTIBLE;
        collidesWith = Gfx.CAT_PLAYER;

        createBody( PhysicsBodyType._STATIC );

        setActionState( ActionStates._STANDING );

        isAnimating = false;
        isDrawable  = true;
    }

    @Override
    public void update()
    {
        switch ( getActionState() )
        {
            case _STANDING:
                if ( isTouchingPlayer )
                {
                    SpriteDescriptor descriptor = App.getEntities().getDescriptor( GraphicID.G_TREASURE_CHEST_OPENING );
                    descriptor._ANIM_RATE = 1.2f;
                    setAnimation( descriptor );

                    setActionState( ActionStates._COLLECTING );
                    isAnimating = true;
                }
                break;
            case _COLLECTING:
                if ( animation.isAnimationFinished( elapsedAnimTime ) )
                {
                    collect();
                    setActionState( ActionStates._INACTIVE );
                    isAnimating = false;

                    stopWatch   = new StopWatch();
                    restingTime = 2500;
                }
                break;
            case _INACTIVE:
                if ( stopWatch.time( TimeUnit.MILLISECONDS ) > restingTime )
                {
                    setActionState( ActionStates._DEAD );
                }
                break;
            default:
                Trace.err( "Unsupported Sprite Action: ", getActionState() );
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
        getPhysicsBody().bodyBox.set( sprite.getX(), sprite.getY(), 30, 20 );
    }

    /**
     * Award _COINS_FROM_CHEST amount of coins if
     * the treasure chest is opened.
     */
    private void collect()
    {
        for ( int i = 0; i < _COINS_FROM_CHEST; i++ )
        {
            SpriteDescriptor descriptor = App.getEntities().getDescriptor( GraphicID.G_COIN );
            descriptor._POSITION.setX( (int) ( this.sprite.getX() / App.getMapData().tileWidth ) );
            descriptor._POSITION.setY( (int) ( ( this.sprite.getY() /*+ this.frameHeight*/ ) / App.getMapData().tileHeight ) );

            Pickup pickup = new Pickup( GraphicID.G_COIN );
            pickup.initialise( descriptor );

            pickup.canAutoCollect   = true;
            pickup.autoCollectTimer = new StopWatch();
            pickup.autoCollectDelay = ( i * 100 );

            App.getEntityData().addEntity( pickup );
        }
    }
}
