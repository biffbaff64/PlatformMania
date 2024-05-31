package com.richikin.platformania.graphics.text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.platformania.core.App;
import com.richikin.platformania.graphics.GameAssets;

public class TextUtils
{
    private static final TextUtils instance = new TextUtils();

    private BitmapFont font;

    public TextUtils()
    {
        setFont( GameAssets._ACME_FONT, 12, Color.WHITE );
    }

    public static TextUtils inst()
    {
        return instance;
    }

    public void setFont( String fontAsset, int size, Color colour )
    {
        FontUtils fontUtils = new FontUtils();
        font = fontUtils.createFont( fontAsset, size, colour );
    }

    public void drawText( String message, float x, float y, SpriteBatch spriteBatch )
    {
        if ( font == null )
        {
            setFont( App.getAssets().getDevPanelFont(), 20, Color.WHITE );
        }

        font.draw( spriteBatch, message, x, y );
    }
}
