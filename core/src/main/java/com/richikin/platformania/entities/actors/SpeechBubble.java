package com.richikin.platformania.entities.actors;

import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.maths.SimpleVec2F;
import com.richikin.platformania.physics.Movement;
import com.richikin.platformania.entities.objects.FixedPathEntity;
import com.richikin.platformania.entities.objects.SpriteDescriptor;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.physics.PhysicsBodyType;

public class SpeechBubble extends FixedPathEntity
{
    public SpeechBubble( GraphicID graphicID )
    {
        super( graphicID );
    }

    @Override
    public void initialise( SpriteDescriptor descriptor )
    {
        create( descriptor );

        bodyCategory = Gfx.CAT_NOTHING;
        collidesWith = Gfx.CAT_NOTHING;

        createBody( PhysicsBodyType._KINEMATIC_SENSOR );

        direction.set( Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN );
        distance.set( 0, 12 );
        distanceReset = new SimpleVec2F( distance );
        speed.set( 0, 0.5f );

        setMovementBounds();

        isAnimating = false;
        isDrawable = true;

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

        animate();

        updateCommon();
    }

    /**
     * Defines a box to use for Box2D Physics body creation,
     * based on this sprite's co-ordinates and size.
     */
    @Override
    public void definePhysicsBodyBox( boolean useBodyPos )
    {
        getPhysicsBody().bodyBox.set( sprite.getX(), sprite.getY(), frameWidth, frameHeight );
    }
}
