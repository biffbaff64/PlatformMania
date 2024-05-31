package com.richikin.platformania.maps;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.platformania.config.Settings;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.maths.SimpleVec2;
import com.richikin.platformania.maths.Vec2;
import com.richikin.platformania.core.App;
import com.richikin.platformania.entities.objects.SpriteDescriptor;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.graphics.parallax.LayerImage;

public class MapData implements Disposable
{
    // ---------------------------------------------------------------------

    //@formatter:off
    public final String[] mapLayerNames =
        {
            "layer1",
            "layer2",
            "layer3",
            "layer4",
            "overlays",
            "object marker tiles",
            "collision",
        };

    public final LayerImage[][] backgroundLayers =
        {
            {
                new LayerImage("background1.png", 0.0f, 0.0f),
                new LayerImage("background2.png", 0.3f, 0.0f),
                new LayerImage("background3.png", 0.6f, 0.0f),
            },
            {
                new LayerImage("background_layer_1.png", 0.0f, 0.0f),
                new LayerImage("background_layer_2.png", 0.3f, 0.0f),
                new LayerImage("background_layer_3.png", 0.6f, 0.0f),
            },
            {
                new LayerImage("background_1.png", 0.0f, 0.0f),
                new LayerImage("background_2.png", 0.2f, 0.0f),
                new LayerImage("background_3.png", 0.4f, 0.0f),
                new LayerImage("background_4.png", 0.6f, 0.0f),
                new LayerImage("background_5.png", 0.8f, 0.0f),
                new LayerImage("background_6.png", 0.10f, 0.0f),
            },
        };

    public final int[] backgroundIndexes =
        {
            0,
            1, 1, 0, 2, 1, 1, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        };
    //@formatter:on

    // ---------------------------------------------------------------------

    public int mapWidth;
    public int mapHeight;
    public int tileWidth;
    public int tileHeight;

    public       Rectangle  viewportBox;
    public       Rectangle  entityWindow;
    public       SimpleVec2 mapPosition;
    public       Vec2       maxScroll;
    public       Vec2       minScroll;
    public       Vec2       scrollDirection;
    public       SimpleVec2 previousMapPosition;
    public       Rectangle  mapBox;

    public Array< SpriteDescriptor > placementTiles;
    public Array< Vec2 >             spawnerPositions;
    public Array< Rectangle >        waterList;
    public Array< Doorway >          doorList;
    public Array< Rectangle >        checkPointList;
    public Array< Rectangle >        ladderList;
    public int                       checkPointNumber;

    // ---------------------------------------------------------------------

    public MapData()
    {
        Trace.checkPoint();

        viewportBox         = new Rectangle();
        entityWindow        = new Rectangle();
        mapPosition         = new SimpleVec2();
        previousMapPosition = new SimpleVec2();
        mapBox              = new Rectangle();
        maxScroll           = new Vec2();
        minScroll           = new Vec2();
        scrollDirection     = new Vec2();
        placementTiles      = new Array<>();
        spawnerPositions    = new Array<>();
        waterList           = new Array<>();
        doorList            = new Array<>();
        ladderList          = new Array<>();
        checkPointList      = new Array<>();
        checkPointNumber    = -1;
    }

    /**
     * Update the screen virtual window. This box is used for checking
     * that entities are visible on screen.
     * An extended virtual window is also updated, which is larger than
     * the visible screen, and can be used for tracking entities that
     * are nearby.
     * NOTE: These windows will NOT be updated if the MainPlayer has
     * not been initialised, as they use its map position.
     */
    public void update()
    {
        if ( App.getPlayer() != null )
        {
            viewportBox.set
                (
                    ( App.getPlayer().sprite.getX() - ( Gfx._VIEW_WIDTH / 2f ) ),
                    ( App.getPlayer().sprite.getY() - ( Gfx._VIEW_HEIGHT / 2f ) ),
                    Gfx._VIEW_WIDTH,
                    Gfx._VIEW_HEIGHT
                );

            entityWindow.set
                (
                    ( App.getPlayer().sprite.getX() - ( Gfx._VIEW_WIDTH + ( Gfx._VIEW_WIDTH / 2f ) ) ),
                    ( App.getPlayer().sprite.getY() - ( Gfx._VIEW_HEIGHT + ( Gfx._VIEW_HEIGHT / 2f ) ) ),
                    ( Gfx._VIEW_WIDTH * 3 ),
                    ( Gfx._VIEW_HEIGHT * 3 )
                );
        }
    }

    /**
     * Updates the map position with the supplied coordinates.
     */
    public void positionAt( int x, int y )
    {
        previousMapPosition.set( mapPosition );

        try
        {
            float xPosition = x;
            float yPosition = y;

            if ( App.getSettings().isEnabled( Settings._SCROLL_DEMO ) )
            {
                xPosition = x;
                yPosition = y;
            }
            else
            {
                if ( App.getPlayer() != null )
                {
                    xPosition += ( float ) ( App.getPlayer().frameWidth / 2 );
                    yPosition += ( float ) ( App.getPlayer().frameHeight / 2 );
                }

                xPosition -= ( float ) ( Gfx._VIEW_WIDTH / 2 );
                yPosition -= ( float ) ( Gfx._VIEW_HEIGHT / 2 );

                xPosition = Math.max( minScroll.x, xPosition );
                xPosition = Math.min( xPosition, maxScroll.x );

                yPosition = Math.max( minScroll.y, yPosition );
                yPosition = Math.min( yPosition, maxScroll.y );
            }

            mapPosition.set( xPosition, yPosition );

            App.getBaseRenderer().getParallaxUtils().scroll();
        }
        catch ( NullPointerException npe )
        {
            Trace.dbg( npe.getMessage() );

            mapPosition.set( 0, 0 );
        }
    }

    @Override
    public void dispose()
    {
        placementTiles.clear();
        waterList.clear();
        doorList.clear();
        ladderList.clear();

        placementTiles = null;
        waterList      = null;
        doorList       = null;
        ladderList     = null;

        viewportBox         = null;
        entityWindow        = null;
        mapPosition         = null;
        previousMapPosition = null;
        mapBox              = null;
        maxScroll           = null;
        minScroll           = null;
        scrollDirection     = null;
    }
}
