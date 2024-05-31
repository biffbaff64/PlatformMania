package com.richikin.platformania.entities.actors.enemies;

import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.physics.Movement;
import com.richikin.platformania.core.App;
import com.richikin.platformania.entities.objects.FixedPathEntity;
import com.richikin.platformania.entities.objects.SpriteDescriptor;
import com.richikin.platformania.graphics.Gfx;

public class Beast extends FixedPathEntity
{
    public Beast()
    {
        super( GraphicID.G_BEAST );

        isAttacker = true;
    }

    /**
     * Defines a box to use for Box2D Physics body creation,
     * based on this sprite's co-ordinates and size.
     * This is the default implementation, and sets the box
     * size to the exact dimensions of an animation frame.
     * Override to create differing sizes.
     */
    @Override
    public void definePhysicsBodyBox( boolean useBodypos )
    {
        getPhysicsBody().bodyBox.set( sprite.getX(), sprite.getY(), 48, 40 );
    }

    @Override
    public void setPositionFromBody()
    {
        if ( ( getPhysicsBody().body != null ) && ( sprite != null ) )
        {
            int modifierX = ( lookingAt.getX() == Movement._DIRECTION_LEFT ) ? 8 : 0;

            sprite.setPosition(
                ( ( getPhysicsBody().body.getPosition().x * Gfx._PPM ) - ( getPhysicsBody().bodyBox.width / 2f ) ) - modifierX,
                ( getPhysicsBody().body.getPosition().y * Gfx._PPM ) - ( getPhysicsBody().bodyBox.height / 2f )
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
                descriptor = App.getEntities().getDescriptor( GraphicID.G_BEAST );
                setAnimation( descriptor );
                break;
            case _RUNNING:
                descriptor = App.getEntities().getDescriptor( GraphicID.G_BEAST_WALK );
                setAnimation( descriptor );
                break;
            case _FIGHTING:
                descriptor = App.getEntities().getDescriptor( GraphicID.G_BEAST_ATTACK );
                setAnimation( descriptor );
                break;
            default:
                break;
        }

        super.setActionState( action );
    }
}
