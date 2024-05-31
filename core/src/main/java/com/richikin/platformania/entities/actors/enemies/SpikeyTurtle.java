package com.richikin.platformania.entities.actors.enemies;

import com.badlogic.gdx.math.MathUtils;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.physics.Movement;
import com.richikin.platformania.entities.objects.FixedPathEntity;
import com.richikin.platformania.graphics.Gfx;

public class SpikeyTurtle extends FixedPathEntity
{
    public SpikeyTurtle()
    {
        super( GraphicID.G_SPIKEY_TURTLE );
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
        getPhysicsBody()
            .bodyBox
            .set
                (
                    ( sprite.getX() + 10 ),
                    sprite.getY(),
                    34,
                    ( ( frameHeight / 4f ) * 3 )
                );
    }

    @Override
    public void setPositionFromBody()
    {
        if ( ( getPhysicsBody().body != null ) && ( sprite != null ) )
        {
            int modifier = ( lookingAt.getX() == Movement._DIRECTION_LEFT ) ? 4 : 12;

            sprite.setPosition
                  (
                      ( ( getPhysicsBody().body.getPosition().x * Gfx._PPM ) - ( getPhysicsBody().bodyBox.width / 2f ) ) - modifier,
                      ( getPhysicsBody().body.getPosition().y * Gfx._PPM ) - ( getPhysicsBody().bodyBox.height / 2f )
                  );
        }
    }

    @Override
    public void onMovementBoundsTurn( int edgeSide )
    {
        speed.setX( 4.0f + ( MathUtils.randomBoolean() ? 2.0f : 0f ) );
    }
}

