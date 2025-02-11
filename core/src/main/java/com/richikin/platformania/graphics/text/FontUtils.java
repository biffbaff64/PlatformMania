package com.richikin.platformania.graphics.text;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.richikin.platformania.logging.Stats;
import com.richikin.platformania.logging.SystemMeters;
import com.richikin.platformania.logging.Trace;

public class FontUtils
{
    public BitmapFont createFont( String fontFile, int size, Color color )
    {
        BitmapFont font;

        try
        {
            font = createFont( fontFile, size );
            font.setColor( color );
        }
        catch ( Exception e )
        {
            Trace.dbg( e.getMessage() );
            Stats.incMeter( SystemMeters._FONT_LOAD_FAILURE.get() );

            font = new BitmapFont();
        }

        return font;
    }

    public BitmapFont createFont( String fontFile, int size )
    {
        BitmapFont font;

        try
        {
            FreeTypeFontGenerator                       generator = new FreeTypeFontGenerator( Gdx.files.internal( fontFile ) );
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

            parameter.size = size;

            font = generator.generateFont( parameter );
            font.setColor( Color.WHITE );
        }
        catch ( Exception e )
        {
            Trace.dbg( e.getMessage() );
            Stats.incMeter( SystemMeters._FONT_LOAD_FAILURE.get() );

            font = new BitmapFont();
        }

        return font;
    }

    public BitmapFont createBorderedFont( String fontFile, int size, Color colour, int borderSize, Color borderColor )
    {
        BitmapFont font;

        try
        {
            FreeTypeFontGenerator                       generator = new FreeTypeFontGenerator( Gdx.files.internal( fontFile ) );
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

            parameter.size        = size;
            parameter.borderColor = borderColor;
            parameter.borderWidth = borderSize;

            font = generator.generateFont( parameter );
            font.setColor( colour );

            generator.dispose();
        }
        catch ( Exception e )
        {
            Trace.dbg( e.getMessage() );
            Stats.incMeter( SystemMeters._BORDERED_FONT_LOAD_FAILURE.get() );

            font = new BitmapFont();
        }

        return font;
    }
}
