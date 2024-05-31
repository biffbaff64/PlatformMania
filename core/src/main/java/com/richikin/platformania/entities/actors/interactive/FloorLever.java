package com.richikin.platformania.entities.actors.interactive;

import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.entities.objects.GdxSprite;
import com.richikin.platformania.entities.objects.SpriteDescriptor;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.physics.PhysicsBodyType;

public class FloorLever extends GdxSprite
{
    private boolean hasOpened;

    public FloorLever()
    {
        super( GraphicID.G_FLOOR_LEVER );
    }

    @Override
    public void initialise( SpriteDescriptor descriptor )
    {
        create( descriptor );

        bodyCategory = Gfx.CAT_USABLE;
        collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_GROUND;

        createBody( PhysicsBodyType._KINEMATIC );

        isAnimating = false;
        hasOpened = false;
        isDrawable = true;

        setActionState( ActionStates._STANDING );
    }

    @Override
    public void update()
    {
        if ( !hasOpened && isTouchingPlayer )
        {
            isAnimating = true;
            hasOpened = true;
        }

        animate();

        updateCommon();
    }

    /**
     * Defines a box to use for Box2D Physics body creation,
     * based on this sprites co-ordinates and size.
     * This is the default implementation, and sets the box
     * size to the exact dimensions of an animation frame.
     * Override to create differing sizes.
     */
    @Override
    public void definePhysicsBodyBox( boolean useBodyPos )
    {
        getPhysicsBody().bodyBox.set( sprite.getX() + 20, sprite.getY(), 56, 26 );
    }

    @Override
    public void setPositionFromBody()
    {
        if ( ( getPhysicsBody() != null ) && ( sprite != null ) )
        {
            sprite.setPosition
                (
                    ( getPhysicsBody().body.getPosition().x * Gfx._PPM ) - ( getPhysicsBody().bodyBox.width / 2f ) - 20,
                    ( getPhysicsBody().body.getPosition().y * Gfx._PPM ) - ( getPhysicsBody().bodyBox.height / 2f )
                );
        }
    }
}
