package com.richikin.platformania.graphics.parallax;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.platformania.core.App;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.maths.shapes.Box;
import com.richikin.platformania.maths.XYSetF;
import com.richikin.platformania.physics.Direction;
import com.richikin.platformania.physics.Movement;

public class ParallaxLayer implements Disposable
{
    public String        name;
    public boolean       isActive;
    public TextureRegion textureRegion;
    public XYSetF        offset;
    public XYSetF        position;
    public Direction     direction;
    public float         xSpeed;
    public float         ySpeed;

    private Box imageBox;

    public ParallaxLayer( String textureName )
    {
        this.name = textureName;

        Texture texture = App.getAssets().loadSingleAsset( textureName, Texture.class );

        texture.setWrap( Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat );
        texture.setFilter( Texture.TextureFilter.Linear, Texture.TextureFilter.Linear );

        textureRegion = new TextureRegion( texture );
        imageBox      = new Box( texture.getWidth(), texture.getHeight() );
        offset        = new XYSetF( 0f, texture.getHeight() - Gfx._PARALLAX_VIEW_HEIGHT );
        position      = new XYSetF( 0f, 0f );
        direction     = new Direction();
        isActive      = true;
        xSpeed        = 0.0f;
        ySpeed        = 0.0f;

        setTextureRegion();
    }

    public void draw()
    {
        if ( isActive )
        {
            App.getSpriteBatch().draw( textureRegion, position.getX(), position.getY() );
        }
    }

    public void scrollLayer( int xDirection, int yDirection )
    {
        if ( isActive )
        {
            boolean isChanged = false;

            if ( xDirection != Movement._DIRECTION_STILL )
            {
                offset.addXWrapped
                          (
                              xSpeed * xDirection,
                              0.0f,
                              imageBox.width - Gfx._PARALLAX_VIEW_WIDTH
                          );

                isChanged = true;
            }

            if ( yDirection != Movement._DIRECTION_STILL )
            {
                offset.addYWrapped
                          (
                              ySpeed * yDirection,
                              0.0f,
                              imageBox.height - Gfx._PARALLAX_VIEW_HEIGHT
                          );

                isChanged = true;
            }

            if ( isChanged )
            {
                setTextureRegion();
            }
        }
    }

    public void bounceScroll()
    {
        if ( direction.isEmpty() )
        {
            direction.set( Movement._DIRECTION_RIGHT, Movement._DIRECTION_UP );

            xSpeed = 4.0f;
            ySpeed = 4.0f;
        }

        scrollLayer( direction.getX(), direction.getY() );

        if ( ( ( direction.getX() == Movement._DIRECTION_LEFT ) && ( offset.getX() <= 0 ) )
            || ( ( direction.getX() == Movement._DIRECTION_RIGHT )
            && ( offset.getX() >= ( textureRegion.getRegionWidth() - Gfx._PARALLAX_VIEW_WIDTH ) ) ) )
        {
            direction.toggleX();
        }

        if ( ( ( direction.getY() == Movement._DIRECTION_DOWN ) && ( offset.getY() <= 0 ) )
            || ( ( direction.getY() == Movement._DIRECTION_UP )
            && ( offset.getY() >= ( textureRegion.getRegionHeight() - Gfx._PARALLAX_VIEW_HEIGHT ) ) ) )
        {
            direction.toggleY();
        }
    }

    public void setTextureRegion()
    {
        textureRegion.setRegion
                         (
                             (int) offset.getX(),
                             (int) offset.getY(),
                             Gfx._PARALLAX_VIEW_WIDTH,
                             Gfx._PARALLAX_VIEW_HEIGHT
                         );
    }

    public void reset()
    {
        offset.set( 0, 0 );
        position.set( 0, 0 );
        direction.standStill();
    }

    @Override
    public void dispose()
    {
        App.getAssets().unloadAsset( name );
        textureRegion.getTexture().dispose();

        name          = null;
        textureRegion = null;
        offset        = null;
        position      = null;
        direction     = null;
        imageBox      = null;
    }
}
