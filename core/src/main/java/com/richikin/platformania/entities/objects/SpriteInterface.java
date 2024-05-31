package com.richikin.platformania.entities.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.platformania.maths.SimpleVec3;

public interface SpriteInterface
{
    /**
     * Initialise this Sprite.
     * Override in any entity classes and add any
     * other relevant initialisation code AFTER the
     * call to create().
     *
     * @param descriptor The {@link SpriteDescriptor} holding
     *                   all setup information.
     */
    void initialise( SpriteDescriptor descriptor );

    /**
     * Performs the actual setting up of the GdxSprite,
     * according to the information provided in the
     * supplied {@link SpriteDescriptor}.
     */
    void create( SpriteDescriptor descriptor );

    /**
     * Sets the initial starting position for this sprite.
     * NOTE: It is important that frameWidth & frameHeight
     * are initialised before this method is called.
     */
    void initPosition( SimpleVec3 vec3F );

    /**
     * Provides an init position modifier value.
     * GdxSprites are placed on TiledMap boundaries and
     * some may need that position adjusting.
     */
    SimpleVec3 getPositionModifier();

    /**
     * Provides the facility for some sprites to perform certain
     * actions before the main update method.
     * Some sprites may not need to do this, or may need to do extra
     * tasks, in which case this can be overridden.
     */
    void preUpdate();

    void update();

    void postUpdate();

    void updateCommon();

    void preDraw();

    void draw( SpriteBatch spriteBatch );

    void animate();

    void setAnimation( SpriteDescriptor descriptor );

    void definePhysicsBodyBox( boolean useBodyPos );
}
