package com.richikin.platformania.input;

import com.badlogic.gdx.graphics.Color;
import com.richikin.platformania.config.Settings;
import com.richikin.platformania.core.App;
import com.richikin.platformania.maths.shapes.Box;

public class GameButtonRegion extends Switch
{
    private final Box region;

    public GameButtonRegion( int x, int y, int width, int height )
    {
        super();

        this.region = new Box( x, y, width, height );
    }

    public boolean contains( int x, int y )
    {
        return ( region.contains( x, y ) );
    }

    public void draw()
    {
        if ( App.getDev().isDevMode()
            && App.getSettings().isEnabled( Settings._BUTTON_BOXES ) )
        {
            App.getAABBRenderer().drawRect( region.x, region.y, region.width, region.height, 2, Color.WHITE );
        }
    }

    @Override
    public Type getType()
    {
        return Type._BUTTON_REGION;
    }
}
