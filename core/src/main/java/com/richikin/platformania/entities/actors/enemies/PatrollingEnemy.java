package com.richikin.platformania.entities.actors.enemies;

import com.badlogic.gdx.graphics.Color;
import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.maths.SimpleVec2F;
import com.richikin.platformania.physics.Movement;
import com.richikin.platformania.ui.ProgressBar;
import com.richikin.platformania.core.App;
import com.richikin.platformania.entities.objects.FixedPathEntity;
import com.richikin.platformania.entities.objects.SpriteDescriptor;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.physics.PhysicsBodyType;
import com.richikin.platformania.physics.Proximity;

public class PatrollingEnemy extends FixedPathEntity
{
    private boolean         filtersSet;
    private PhysicsBodyType requestedBodyType;
    private float           ticks = 0;

    public PatrollingEnemy( GraphicID gid )
    {
        super( gid );

        isFlying                = ( gid == GraphicID.G_BAT );
        filtersSet              = false;
        requestedBodyType       = PhysicsBodyType._NONE;
    }

    @Override
    public void initialise( SpriteDescriptor descriptor )
    {
        create( descriptor );

        Trace._assert( !filtersSet, "Filters require setting!" );
        Trace._assert( requestedBodyType == PhysicsBodyType._NONE, "PhysicsBodyType not specified!" );

        createBody( requestedBodyType );

        if ( isFlying )
        {
            getPhysicsBody().body.setGravityScale( 0.0f );
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

        standDelay = 0;
        ratDelay   = 0;

        proximity = new Proximity();

        setActionState( ActionStates._STANDING );
    }

    @Override
    protected void move()
    {
        if ( getActionState() == ActionStates._RUNNING )
        {
            checkMovementBounds();

            if ( isFlying )
            {
                ticks++;

                getPhysicsBody().body.setLinearVelocity
                    (
                        ( speed.getX() * direction.getX() ),
                        ( float ) ( Math.sin( ( ticks * 0.5f * Math.PI ) / 16 ) )
                    );
            }
            else
            {
                getPhysicsBody().body.setLinearVelocity
                    (
                        ( speed.getX() * direction.getX() ),
                        ( speed.getY() * direction.getY() )
                    );
            }
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
            {
                descriptor = App.getEntities().getDescriptor( getGID() );
                setAnimation( descriptor );
            }
            break;

            default:
                break;
        }

        super.setActionState( action );
    }

    public void setFilters( short category, short collisionMask )
    {
        bodyCategory = category;
        collidesWith = collisionMask;

        filtersSet = true;
    }

    public void setRequestedBodyType( PhysicsBodyType physicsBodyType )
    {
        requestedBodyType = physicsBodyType;
    }
}
