package com.richikin.platformania.entities.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.logging.StopWatch;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.maths.SimpleVec2F;
import com.richikin.platformania.physics.Movement;
import com.richikin.platformania.core.App;
import com.richikin.platformania.core.GameConstants;
import com.richikin.platformania.core.GameProgress;
import com.richikin.platformania.entities.objects.GdxSprite;
import com.richikin.platformania.entities.objects.SpriteDescriptor;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.physics.PhysicsBodyType;

import java.util.concurrent.TimeUnit;

public class Pickup extends GdxSprite
{
    private static final byte _KEYS  = 0;
    private static final byte _COIN  = 1;
    private static final byte _GEM   = 2;
    private static final byte _HEART = 3;
    private static final byte _APPLE = 4;

    private final byte[] pickupValues =
        {
            1,      // Keys
            1,      // Coin
            1,      // Gem
            25,     // Heart
            5,      // Apple
        };

    public StopWatch autoCollectTimer;
    public float     autoCollectDelay;
    public boolean   canAutoCollect;

    private SimpleVec2F distanceReset;

    // ---------------------------------------------------------
    // Code
    // ---------------------------------------------------------

    public Pickup( GraphicID gid )
    {
        super( gid );
    }

    @Override
    public void initialise( SpriteDescriptor descriptor )
    {
        if ( App.getHud().getItemBar().isItemBarItem( descriptor._GID ) )
        {

        }

        create( descriptor );

        bodyCategory = Gfx.CAT_COLLECTIBLE;
        collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_PLATFORM | Gfx.CAT_MOBILE_ENEMY;

        createBody( PhysicsBodyType._KINEMATIC_SENSOR );

        int limit = MathUtils.random( 5 );

        for ( int i = 0; i < limit; i++ )
        {
            elapsedAnimTime += Gdx.graphics.getDeltaTime();
        }

        if ( descriptor._FRAMES == 1 )
        {
            direction.setX( Movement._DIRECTION_STILL );
            direction.setY( MathUtils.randomBoolean() ? Movement._DIRECTION_UP : Movement._DIRECTION_DOWN );

            distanceReset = new SimpleVec2F();

            distance.set( 0, 4 );
            distanceReset.set( 0, distance.getY() * 2 );
            speed.set( 0, 0.25f );
        }

        isDrawable  = true;
        isAnimating = true;
        isHittable  = true;

        setActionState( ActionStates._STANDING );
    }

    @Override
    public void update()
    {
        switch ( getActionState() )
        {
            case _STANDING:
            {
                if ( canCollectItem() && canAutoCollect )
                {
                    if ( autoCollectTimer.time( TimeUnit.MILLISECONDS ) > autoCollectDelay )
                    {
                        setActionState( ActionStates._SET_HIDING );
                    }
                }
                bounce();
            }
            break;

            case _SET_HIDING:
            {
                speed.set( 24, 24 );
                distance.set( speed.getX() * 10, speed.getY() * 10 );
                direction.set( Movement._DIRECTION_STILL, Movement._DIRECTION_UP );
                setActionState( ActionStates._HIDING );
            }
            break;

            case _HIDING:
            {
                if ( !App.getMapData().viewportBox.contains( sprite.getX(), sprite.getY() ) )
                {
                    collect();

                    setActionState( ActionStates._DYING );
                }
                else
                {
                    getPhysicsBody().body.setLinearVelocity
                        (
                            speed.getX() * direction.getX(),
                            speed.getY() * direction.getY()
                        );
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
                Trace.err( "Unsupported Sprite Action: ", getActionState() );
            }
            break;
        }

        super.update();
    }

    public void setCollecting()
    {
        if ( isActive )
        {
            setActionState( ActionStates._SET_HIDING );
            isHittable = false;
        }
    }

    private void bounce()
    {
        if ( direction.hasDirection() )
        {
            if ( distance.isEmpty() )
            {
                distance.set( distanceReset );

                direction.toggle();
            }

            getPhysicsBody().body.setLinearVelocity
                (
                    speed.getX() * direction.getX(),
                    speed.getY() * direction.getY()
                );

            distance.sub( speed.getX(), speed.getY() );
        }
    }

    private void collect()
    {
        if ( App.getHud().getItemBar().isItemBarItem( this.gid ) )
        {
            App.getHud().getItemBar().markItemCollected( this.gid );
        }
        else
        {
            switch ( this.gid )
            {
                case G_COIN:
                {
                    App.getGameProgress().stackPush( GameProgress.Stack._COIN, pickupValues[ _COIN ] );
                }
                break;

                case G_GEM:
                {
                    App.getGameProgress().stackPush( GameProgress.Stack._GEM, pickupValues[ _GEM ] );
                }
                break;

                case G_KEY:
                {
                    App.getGameProgress().stackPush( GameProgress.Stack._KEY, pickupValues[ _KEYS ] );
                    App.getHud().getTextPanel().setTexture( "key_collected_text_box" );
                    App.getHud().getTextPanel().activate();
                }
                break;

                case G_HEART:
                {
                    App.getPlayer().strength = GameConstants._MAX_STRENGTH;
                    App.getHud().getTextPanel().setTexture( "health_text_box" );
                    App.getHud().getTextPanel().activate();
                }
                break;

                case G_APPLE:
                {
                    App.getPlayer().strength += pickupValues[ _APPLE ];
                }
                break;

                default:
                    break;
            }
        }
    }

    private boolean canCollectItem()
    {
        boolean canCollect;

        switch ( gid )
        {
            case G_HEART:
            case G_APPLE:
                canCollect = App.getHud().getHealthBar().hasRoom();
                break;

            case G_COIN:
                canCollect = App.getGameProgress().coinCount.hasRoom();
                break;

            case G_GEM:
                canCollect = App.getGameProgress().gemCount.hasRoom();
                break;

            case G_KEY:
                canCollect = App.getGameProgress().keyCount.hasRoom();
                break;

            default:
                canCollect = App.getHud().getItemBar().isItemBarItem( this.gid );
                break;
        }

        getPhysicsBody().body.setAwake( canCollect );
        isActive = canCollect;

        return canCollect;
    }
}
