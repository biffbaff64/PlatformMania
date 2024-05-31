package com.richikin.platformania.graphics.parallax;

import com.richikin.platformania.core.App;

public class ParallaxUtils
{
    public ParallaxUtils()
    {
    }

    public void scroll()
    {
        if ((App.getMapData().mapPosition.getX() > App.getMapData().minScroll.x)
            && (App.getMapData().mapPosition.getX() < App.getMapData().maxScroll.x))
        {
            if (App.getMapData().previousMapPosition.getX() > App.getMapData().mapPosition.getX())
            {
                App.getBaseRenderer().getParallaxBackground().scrollLayersLeft();
            }
            else if (App.getMapData().previousMapPosition.getX() < App.getMapData().mapPosition.getX())
            {
                App.getBaseRenderer().getParallaxBackground().scrollLayersRight();
            }
        }

        if ((App.getMapData().mapPosition.getY() > App.getMapData().minScroll.y)
            && (App.getMapData().mapPosition.getY() < App.getMapData().maxScroll.y))
        {
            if (App.getMapData().previousMapPosition.getY() > App.getMapData().mapPosition.getY())
            {
                App.getBaseRenderer().getParallaxBackground().scrollLayersDown();
            }
            else if (App.getMapData().previousMapPosition.getY() < App.getMapData().mapPosition.getY())
            {
                App.getBaseRenderer().getParallaxBackground().scrollLayersUp();
            }
        }
    }
}
