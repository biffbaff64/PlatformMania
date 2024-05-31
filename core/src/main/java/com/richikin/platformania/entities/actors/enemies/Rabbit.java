package com.richikin.platformania.entities.actors.enemies;

import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.entities.objects.FixedPathEntity;
import com.richikin.platformania.graphics.Gfx;

public class Rabbit extends FixedPathEntity
{
    public Rabbit()
    {
        super( GraphicID.G_RABBIT );
    }

    @Override
    public void definePhysicsBodyBox( boolean useBodyPos )
    {
        getPhysicsBody().bodyBox.set
            (
                sprite.getX() + 6,
                sprite.getY(),
                22,
                36
            );
    }

    @Override
    public void setPositionFromBody()
    {
        if ( ( getPhysicsBody() != null ) && ( sprite != null ) )
        {
            sprite.setPosition
                (
                    ( ( getPhysicsBody().body.getPosition().x * Gfx._PPM ) - ( getPhysicsBody().bodyBox.width / 2f ) ) - 6,
                    ( getPhysicsBody().body.getPosition().y * Gfx._PPM ) - ( getPhysicsBody().bodyBox.height / 2f )
                );
        }
    }
}
