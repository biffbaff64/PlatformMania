package com.richikin.platformania.entities.actors.enemies;

import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.physics.Movement;
import com.richikin.platformania.entities.objects.FixedPathEntity;
import com.richikin.platformania.graphics.Gfx;

public class Mushroom extends FixedPathEntity
{
    public Mushroom()
    {
        super( GraphicID.G_MUSHROOM );
    }

    @Override
    public void definePhysicsBodyBox( boolean useBodyPos )
    {
        getPhysicsBody().bodyBox.set( sprite.getX() + 8, sprite.getY(), 22, 25 );
    }

    @Override
    public void setPositionFromBody()
    {
        if ( ( getPhysicsBody().body != null ) && ( sprite != null ) )
        {
            int modifier = ( lookingAt.getX() == Movement._DIRECTION_LEFT ) ? 10 : 11;

            sprite.setPosition
                (
                    ( ( getPhysicsBody().body.getPosition().x * Gfx._PPM ) - ( getPhysicsBody().bodyBox.width / 2f ) ) - modifier,
                    ( getPhysicsBody().body.getPosition().y * Gfx._PPM ) - ( getPhysicsBody().bodyBox.height / 2f )
                );
        }
    }
}
