package com.richikin.platformania.entities.actors;

import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.physics.Movement;
import com.richikin.platformania.entities.objects.FixedPathEntity;
import com.richikin.platformania.graphics.Gfx;

public class Fish extends FixedPathEntity
{
    private float ticks = 0;

    public Fish()
    {
        super( GraphicID.G_FISH );
    }

    @Override
    protected void move()
    {
        checkMovementBounds();

        ticks++;

        getPhysicsBody().body.setLinearVelocity
            (
                ( speed.getX() * direction.getX() ),
                ( float ) ( Math.sin( ( ticks * 0.5f * Math.PI ) / 16 ) )
            );

        lookingAt.set( direction );

        isFlippedX = ( direction.getX() == Movement._DIRECTION_LEFT );
        isFlippedY = false;
    }

    @Override
    public void definePhysicsBodyBox( boolean useBodyPos )
    {
        getPhysicsBody().bodyBox.set( sprite.getX() + 2, sprite.getY() + 2, 28, 28 );
    }

    @Override
    public void setPositionFromBody()
    {
        if ( ( getPhysicsBody() != null ) && ( sprite != null ) )
        {
            sprite.setPosition
                (
                    ( ( getPhysicsBody().body.getPosition().x * Gfx._PPM ) - ( getPhysicsBody().bodyBox.width / 2f ) ) - 2,
                    ( getPhysicsBody().body.getPosition().y * Gfx._PPM ) - ( getPhysicsBody().bodyBox.height / 2f ) - 2
                );
        }
    }

    /**
     * Set the entity category masks used to check for
     * compatible collision types.
     */
    @Override
    public void setFilters()
    {
        bodyCategory = Gfx.CAT_NOTHING;
        collidesWith = Gfx.CAT_NOTHING;
    }
}
