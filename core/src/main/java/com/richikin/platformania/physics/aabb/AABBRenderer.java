package com.richikin.platformania.physics.aabb;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.platformania.config.Settings;
import com.richikin.platformania.core.App;
import com.richikin.platformania.graphics.text.FontUtils;
import com.richikin.platformania.logging.Stats;
import com.richikin.platformania.logging.SystemMeters;
import com.richikin.platformania.logging.Trace;

public class AABBRenderer implements Disposable
{
    private TextureRegion debugTextureRegion;
    private BitmapFont    font;

    public AABBRenderer()
    {
    }

    public void setup( String debugFont )
    {
        debugTextureRegion = new TextureRegion();

        FontUtils fontUtils = new FontUtils();
        font = fontUtils.createFont( debugFont, 15, Color.WHITE );
    }

    public void drawBoxes()
    {
        if ( App.getSettings().isEnabled( Settings._TILE_BOXES ) )
        {
            drawTileLayerBoxes();
        }

        if ( App.getSettings().isEnabled( Settings._SPRITE_BOXES ) )
        {
            drawSpriteCollisionBoxes();
        }
    }

    private void drawTileLayerBoxes()
    {
//        Rectangle debugRectangle = new Rectangle();
//
//        for ( int i = 0; i < App.getEntityData().getEntityMap().size; i++ )
//        {
//            CollisionObject collisionObject = App.getEntityData().getEntity( i ).collisionObject;
//
//            if ( collisionObject.type == GraphicID._OBSTACLE )
//            {
//                debugRectangle.set( collisionObject.rectangle );
//
//                if ( collisionObject.action == ActionStates._HITTABLE )
//                {
//                    debugTextureRegion = App.getAssets().getObjectRegion( App.getAssets().getBlueObjectName() );
//                }
//                else if ( collisionObject.action == ActionStates._HITTING )
//                {
//                    debugTextureRegion = App.getAssets().getObjectRegion( App.getAssets().getRedObjectName() );
//                }
//                else
//                {
//                    debugTextureRegion = App.getAssets().getObjectRegion( App.getAssets().getYellowObjectName() );
//                }
//
//                drawRect
//                    (
//                        debugTextureRegion,
//                        (int) debugRectangle.x,
//                        (int) debugRectangle.y,
//                        (int) debugRectangle.width,
//                        (int) debugRectangle.height,
//                        1
//                    );
//            }
//        }
    }

    private void drawSpriteCollisionBoxes()
    {
//        CollisionRect debugRectangle = new CollisionRect( GraphicID.G_NO_ID );
//
//        for ( int i = 0; i < App.getEntityData().getEntityMap().size; i++ )
//        {
//            CollisionObject collisionObject = App.getEntityData().getEntity( i ).collisionObject;
//
//            if ( collisionObject.type == GraphicID._ENTITY )
//            {
//                debugRectangle.set( collisionObject.rectangle );
//
//                if ( collisionObject.rectangle.colour == Color.BLUE )
//                {
//                    debugTextureRegion = App.getAssets().getObjectRegion( App.getAssets().getBlueObjectName() );
//                }
//                else if ( ( collisionObject.rectangle.colour == Color.RED ) || ( collisionObject.action == ActionStates._HITTING ) )
//                {
//                    debugTextureRegion = App.getAssets().getObjectRegion( App.getAssets().getRedObjectName() );
//                }
//                else if ( collisionObject.rectangle.colour == Color.YELLOW )
//                {
//                    debugTextureRegion = App.getAssets().getObjectRegion( App.getAssets().getYellowObjectName() );
//                }
//                else if ( collisionObject.rectangle.colour == Color.GREEN )
//                {
//                    debugTextureRegion = App.getAssets().getObjectRegion( App.getAssets().getGreenObjectName() );
//                }
//                else
//                {
//                    debugTextureRegion = App.getAssets().getObjectRegion( App.getAssets().getYellowObjectName() );
//                }
//
//                drawRect
//                    (
//                        debugTextureRegion,
//                        (int) debugRectangle.x,
//                        (int) debugRectangle.y,
//                        (int) debugRectangle.width,
//                        (int) debugRectangle.height,
//                        1
//                    );
//            }
//        }
    }

    public void drawRect( int x, int y, int width, int height, int thickness )
    {
        debugTextureRegion = App.getAssets().getObjectRegion( "solid_red32x32" );

        drawRect
            (
                debugTextureRegion,
                x,
                y,
                width,
                height,
                thickness
            );
    }

    public void drawRect( int x, int y, int width, int height, int thickness, Color color )
    {
        String asset;

        if ( color == Color.BLUE )
        {
            asset = App.getAssets().getBlueObjectName();
        }
        else if ( color == Color.RED )
        {
            asset = App.getAssets().getRedObjectName();
        }
        else if ( color == Color.YELLOW )
        {
            asset = App.getAssets().getYellowObjectName();
        }
        else if ( color == Color.GREEN )
        {
            asset = App.getAssets().getGreenObjectName();
        }
        else
        {
            asset = App.getAssets().getWhiteObjectName();
        }

        debugTextureRegion = App.getAssets().getObjectRegion( asset );

        drawRect
            (
                debugTextureRegion,
                x,
                y,
                width,
                height,
                thickness
            );
    }

    private void drawRect( TextureRegion textureRegion, int x, int y, int width, int height, int thickness )
    {
        try
        {
            App.getSpriteBatch().draw( textureRegion, x, y, width, thickness );
            App.getSpriteBatch().draw( textureRegion, x, y, thickness, height );
            App.getSpriteBatch().draw( textureRegion, x, ( y + height ) - thickness, width, thickness );
            App.getSpriteBatch().draw( textureRegion, ( x + width ) - thickness, y, thickness, height );
        }
        catch ( NullPointerException exception )
        {
            Trace.dbg( "NullPointerException !" );
            Trace.dbg( "textureRegion: " + textureRegion );
            Trace.dbg( "x: " + x );
            Trace.dbg( "y: " + y );
            Trace.dbg( "width: " + width );
            Trace.dbg( "height: " + height );
            Trace.dbg( "thickness: " + thickness );
            Trace.dbg( "From: " + new Exception().getStackTrace()[ 1 ].getClassName() );

            Stats.incMeter( SystemMeters._NULL_POINTER_EXCEPTION.get() );
        }
    }

    @Override
    public void dispose()
    {
        Trace.checkPoint();

        debugTextureRegion.getTexture().dispose();
        debugTextureRegion = null;

        font.dispose();
        font = null;
    }
}
