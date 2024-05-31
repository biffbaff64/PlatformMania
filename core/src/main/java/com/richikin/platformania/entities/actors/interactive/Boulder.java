package com.richikin.platformania.entities.actors.interactive;

import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.core.App;
import com.richikin.platformania.entities.objects.GdxSprite;
import com.richikin.platformania.entities.objects.SpriteDescriptor;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.physics.PhysicsBodyType;

public class Boulder extends GdxSprite
{
    public Boulder()
    {
        super( GraphicID.G_PUSHABLE_BOULDER );
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

        createBody( PhysicsBodyType._DYNAMIC_CIRCLE );

        isAnimating = true;
        isDrawable  = true;
        isHeld      = false;

        setActionState( ActionStates._STANDING );
    }

    @Override
    public void update()
    {
        isRotating = ( getPhysicsBody().body.getLinearVelocity().x != 0 );

        if ( isRotating )
        {
            rotateSpeed = ( getPhysicsBody().body.getLinearVelocity().x *= -1 );
        }

        if ( isHeld && !App.getPlayer().isHolding )
        {
            isHeld = false;
        }

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
}
