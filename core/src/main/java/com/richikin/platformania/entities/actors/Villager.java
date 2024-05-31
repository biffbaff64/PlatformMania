package com.richikin.platformania.entities.actors;

import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.core.App;
import com.richikin.platformania.entities.objects.GdxSprite;
import com.richikin.platformania.entities.objects.SpriteDescriptor;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.physics.PhysicsBodyType;
import com.richikin.platformania.physics.Proximity;

public class Villager extends GdxSprite
{
    private SpeechBubble speechBubble;
    private Proximity    proximity;

    public Villager()
    {
        super( GraphicID.G_PRISONER );
    }

    @Override
    public void initialise( SpriteDescriptor descriptor )
    {
        create( descriptor );

        bodyCategory = Gfx.CAT_VILLAGER;
        collidesWith = Gfx.CAT_GROUND;

        createBody( PhysicsBodyType._KINEMATIC );

        isAnimating = true;
        isDrawable  = true;

        createSpeechBubble();

        proximity = new Proximity();

        setActionState( ActionStates._STANDING );
    }

    @Override
    public void update()
    {
        if ( getActionState() == ActionStates._STANDING )
        {
            isFlippedX = ( App.getPlayer().getBodyX() < getBodyX() );

            if ( proximity.isNear( this, App.getPlayer() ) )
            {
                positionSpeechBubble();
                setBubbleAnimation();
            }
            else if ( speechBubble != null )
            {
                speechBubble.sprite.setRegion( speechBubble.animFrames[ 0 ] );
            }
        }

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

    private void createSpeechBubble()
    {
        SpriteDescriptor descriptor = App.getEntities().getDescriptor( GraphicID.G_HELP_BUBBLE );

        descriptor._POSITION.set
            (
                sprite.getX() / App.getMapData().tileWidth,
                ( sprite.getY() + 48 ) / App.getMapData().tileHeight
            );

        speechBubble = new SpeechBubble( GraphicID.G_HELP_BUBBLE );
        speechBubble.initialise( descriptor );
        speechBubble.sprite.setRegion( speechBubble.animFrames[ 0 ] );
        speechBubble.isDrawable  = true;
        speechBubble.isAnimating = false;

        App.getEntityData().addEntity( speechBubble );
    }

    private void positionSpeechBubble()
    {
    }

    private void setBubbleAnimation()
    {
        speechBubble.sprite.setRegion( speechBubble.animFrames[ ( App.getGameProgress().keyCount.getTotal() > 0 ) ? 1 : 2 ] );
    }
}
