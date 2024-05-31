package com.richikin.platformania.entities.actors.interactive;

import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.core.App;
import com.richikin.platformania.entities.objects.GdxSprite;
import com.richikin.platformania.entities.objects.SpriteDescriptor;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.physics.PhysicsBodyType;

public class Crate extends GdxSprite
{
    public Crate()
    {
        super( GraphicID.G_PUSHABLE_CRATE );
    }

    @Override
    public void initialise( SpriteDescriptor descriptor )
    {
        create( descriptor );

        bodyCategory = Gfx.CAT_USABLE;
        collidesWith = Gfx.CAT_PLAYER
            | Gfx.CAT_WEAPON
            | Gfx.CAT_ENEMY
            | Gfx.CAT_USABLE
            | Gfx.CAT_PLATFORM
            | Gfx.CAT_OBSTACLE;

        createBody( PhysicsBodyType._DYNAMIC_PUSHABLE );

        isAnimating = false;
        isDrawable  = true;
        isHeld      = false;

        setActionState( ActionStates._STANDING );
    }

    @Override
    public void update()
    {
        if ( isHeld && !App.getPlayer().isHolding )
        {
            isHeld = false;
        }

        isAnimating = isHeld;

        if ( isHeld )
        {
            getPhysicsBody().body.setLinearVelocity
                (
                    ( App.getPlayer().speed.getX() * App.getPlayer().direction.getX() ),
                    ( App.getPlayer().speed.getY() * App.getPlayer().direction.getY() )
                );
        }

        animate();

        updateCommon();
    }

    @Override
    public void definePhysicsBodyBox( boolean useBodyPos )
    {
        getPhysicsBody().bodyBox.set
            (
                sprite.getX() + 1,
                sprite.getY(),
                frameWidth - 2,
                frameHeight
            );
    }

    @Override
    public void setPositionFromBody()
    {
        if ( ( getPhysicsBody() != null ) && ( sprite != null ) )
        {
            sprite.setPosition
                (
                    ( getPhysicsBody().body.getPosition().x * Gfx._PPM ) - ( getPhysicsBody().bodyBox.width / 2f ) - 1,
                    ( getPhysicsBody().body.getPosition().y * Gfx._PPM ) - ( getPhysicsBody().bodyBox.height / 2f )
                );
        }
    }
}
