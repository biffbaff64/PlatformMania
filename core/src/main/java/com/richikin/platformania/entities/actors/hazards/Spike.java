package com.richikin.platformania.entities.actors.hazards;

import com.badlogic.gdx.math.MathUtils;
import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.maths.SimpleVec2F;
import com.richikin.platformania.physics.Movement;
import com.richikin.platformania.core.App;
import com.richikin.platformania.entities.objects.BoundedEntity;
import com.richikin.platformania.entities.objects.SpriteDescriptor;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.physics.PhysicsBodyType;

public class Spike extends BoundedEntity
{
    private int turnCount;

    public Spike()
    {
        super( GraphicID.G_SPIKES );
    }

    @Override
    public void initialise( SpriteDescriptor descriptor )
    {
        create( descriptor );

        bodyCategory = Gfx.CAT_STATIC_ENEMY;
        collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_MOBILE_ENEMY;

        createBody( PhysicsBodyType._KINEMATIC );

        getPhysicsBody().body.setGravityScale( 0 );

        direction.set( descriptor._DIR );
        distance.set( descriptor._DIST );
        distance.mul( App.getMapData().tileWidth, App.getMapData().tileHeight );
        distanceReset = new SimpleVec2F( distance );

        setSpeed();

        setMovementBounds();

        isDrawable  = true;
        isAnimating = true;

        turnCount = 0;

        setActionState( ActionStates._STANDING );
    }

    @Override
    public void update()
    {
        checkMovementBounds();

        getPhysicsBody().body.setLinearVelocity
            (
                speed.getX() * direction.getX(),
                speed.getY() * direction.getY()
            );

        lookingAt.set( direction );

        animate();

        updateCommon();
    }

    @Override
    public void onMovementBoundsTurn( int edgeSide )
    {
        if ( edgeSide == Movement._DIRECTION_DOWN )
        {
            turnCount++;

            if ( turnCount >= 5 )
            {
                turnCount = 0;

                setSpeed();
                speed.setY( 4.0f + MathUtils.random( 3.0f ) );
            }
        }
    }

    private void setSpeed()
    {
        int index = 0;

        try
        {
            int[] speeds =
                {
                    4, 4, 6, 4,
                    6, 4, 6, 4,
                    6, 6, 4, 4,
                };

            index = MathUtils.random( speeds.length - 1 );

            speed.setX( direction.hasXDirection() ? speeds[ index ] : 0 );
            speed.setY( direction.hasYDirection() ? speeds[ index ] : 0 );
        }
        catch ( IndexOutOfBoundsException e )
        {
            Trace.dbg( e, " : index = ", index );

            speed.setY( 2 );
        }
    }
}
