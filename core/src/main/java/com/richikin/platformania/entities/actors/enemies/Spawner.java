package com.richikin.platformania.entities.actors.enemies;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.physics.Movement;
import com.richikin.platformania.core.App;
import com.richikin.platformania.entities.objects.AnimID;
import com.richikin.platformania.entities.objects.GdxSprite;
import com.richikin.platformania.entities.objects.SpriteDescriptor;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.physics.PhysicsBodyType;

public class Spawner extends GdxSprite
{
    //@formatter:off
    private static final AnimID[] animIDs =
        {
            new AnimID( ActionStates._SPAWNING,     GraphicID.G_SPAWNER,        0.6f ),
            new AnimID( ActionStates._RUNNING,      GraphicID.G_SPAWNER_RUN,    0.4f ),
            new AnimID( ActionStates._DYING,        GraphicID.G_SPAWNER_DYING,  0.4f ),
        };
    //@formatter:on

    private static final float _XSPEED = 24.0f;

    private int turnCount;

    public Spawner()
    {
        super( GraphicID.G_SPAWNER );
    }

    @Override
    public void initialise( SpriteDescriptor descriptor )
    {
        create( descriptor );

        bodyCategory = Gfx.CAT_MOBILE_ENEMY;
        collidesWith = Gfx.CAT_PLAYER
            | Gfx.CAT_WEAPON
            | Gfx.CAT_STATIC_ENEMY
            | Gfx.CAT_PLATFORM
            | Gfx.CAT_GROUND
            | Gfx.CAT_WALL;

        createBody( PhysicsBodyType._DYNAMIC );

        isAnimating = true;
        isDrawable  = true;

        setActionState( ActionStates._SPAWNING );
        getPhysicsBody().body.setGravityScale( 0.0f );

        direction.standStill();
        speed.setEmpty();
    }

    @Override
    public void update()
    {
        switch ( getActionState() )
        {
            case _SPAWNING:
            {
                if ( animation.isAnimationFinished( elapsedAnimTime ) )
                {
                    turnCount = 0;

                    setActionState( ActionStates._STANDING );
                }
            }
            break;

            case _STANDING:
            {
                direction.set
                    (
                        MathUtils.randomBoolean() ? Movement._DIRECTION_LEFT : Movement._DIRECTION_RIGHT,
                        Movement._DIRECTION_STILL
                    );

                speed.setX( _XSPEED );

                setActionState( ActionStates._RUNNING );
            }
            break;

            case _RUNNING:
            {
                move();

                if ( shouldForceTurnX
                    || App.getEntityUtils().canRandomlyTurn( this ) )
                {
                    direction.toggleX();
                    shouldForceTurnX = false;
                    turnCount++;

                    if ( ( turnCount > 6 ) || isTouchingPlayer )
                    {
                        setDying();
                    }
                }
            }
            break;

            case _FALLING:
            {
                if ( getPhysicsBody().contactCount > 0 )
                {
                    setActionState( ActionStates._STANDING );
                }
            }
            break;

            case _DYING:
            {
                if ( animation.isAnimationFinished( elapsedAnimTime ) )
                {
                    setActionState( ActionStates._DEAD );
                }
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

    @Override
    public void postUpdate()
    {
        super.postUpdate();

        if ( ( getActionState() != ActionStates._FALLING )
            && ( getActionState() != ActionStates._DYING ) )
        {
            if ( ( getPhysicsBody().contactCount == 0 )
                && ( getPhysicsBody().body.getLinearVelocity().y < 0 ) )
            {
                speed.setX( 0 );
                setActionState( ActionStates._FALLING );
            }
        }

        // Quick check to see if the player entity has left the map window.
        if ( !App.getMapData().mapBox.contains( sprite.getBoundingRectangle() ) )
        {
            setDying();
        }
    }

    @Override
    public void tidy( int index )
    {
        App.getEntityData().getManager( GraphicID._SPAWNER_MANAGER ).free();
    }

    @Override
    public void setActionState( ActionStates newAction )
    {
        if ( newAction != getActionState() )
        {
            SpriteDescriptor   descriptor;
            GraphicID          graphicID = GraphicID.G_NO_ID;
            float              animRate  = 0.0f;

            if ( direction.hasDirection() )
            {
                lookingAt.set( direction );
            }

            for ( AnimID animID : animIDs )
            {
                if ( animID.state == newAction )
                {
                    graphicID = animID.gid;
                    animRate  = animID.animRate;
                }
            }

            if ( graphicID != GraphicID.G_NO_ID )
            {
                descriptor            = App.getEntities().getDescriptor( graphicID );
                descriptor._ANIM_RATE = animRate;
                elapsedAnimTime       = 0;

                if ( newAction == ActionStates._RUNNING )
                {
                    getPhysicsBody().body.setGravityScale( Gfx._FALL_GRAVITY );
                }

                setAnimation( descriptor );
            }
        }

        super.setActionState( newAction );
    }

    private void move()
    {
        sprite.setColor( isHittingSame ? Color.BLUE : Color.WHITE );

        getPhysicsBody().body.setLinearVelocity
            (
                ( speed.getX() * direction.getX() ),
                getPhysicsBody().body.getLinearVelocity().y
            );

        isFlippedX = ( direction.getX() == Movement._DIRECTION_LEFT );
        isFlippedY = false;
    }

    @Override
    public void definePhysicsBodyBox( boolean useBodyPos )
    {
        getPhysicsBody().bodyBox.set
            (
                sprite.getX() + 10,
                sprite.getY(),
                36,
                32
            );
    }

    @Override
    public void setPositionFromBody()
    {
        if ( ( getPhysicsBody() != null ) && ( sprite != null ) )
        {
            sprite.setPosition
                (
                    ( ( getPhysicsBody().body.getPosition().x * Gfx._PPM ) - ( getPhysicsBody().bodyBox.width / 2f ) ) - 10,
                    ( getPhysicsBody().body.getPosition().y * Gfx._PPM ) - ( getPhysicsBody().bodyBox.height / 2f )
                );
        }
    }
}
