package com.richikin.platformania.graphics;

import com.badlogic.gdx.math.Vector2;
import com.richikin.platformania.maths.SimpleVec2F;

public class Gfx
{
    //
    // Entity collision types. These are checked against an entity's 'collidesWith' property
    // to see if collision is allowed. i.e. If EntityA.bodyCategory is set to CAT_PLAYER,
    // and EntityB.collidesWith has the bit set for CAT_PLAYER, then EntityA and EntityB will
    // register collisions.

    public static final short CAT_NOTHING      = 0x0000;   // - 00 (0     )
    public static final short CAT_PLAYER       = 0x0001;   // - 01 (1     )
    public static final short CAT_MOBILE_ENEMY = 0x0002;   // - 02 (2     )
    public static final short CAT_STATIC_ENEMY = 0x0004;   // - 03 (4     )
    public static final short CAT_WALL         = 0x0008;   // - 04 (8     )
    public static final short CAT_INTERACTIVE  = 0x0010;   // - 05 (16    )
    public static final short CAT_COLLECTIBLE  = 0x0020;   // - 06 (32    )
    public static final short CAT_PLATFORM     = 0x0040;   // - 07 (64    )
    public static final short CAT_GROUND       = 0x0080;   // - 08 (128   )
    public static final short CAT_USABLE       = 0x0100;   // - 09 (256   ) // Usable by the player character
    public static final short CAT_VILLAGER     = 0x0200;   // - 10 (512   )
    public static final short CAT_JAIL         = 0x0400;   // - 11 (1024  )
    public static final short CAT_WEAPON       = 0x0800;   // - 12 (2048  )
    public static final short _UNDEFINED_13    = 0x1000;   // - 13 (4096  )
    public static final short _UNDEFINED_14    = 0x2000;   // - 14 (8192  )
    public static final short _UNDEFINED_15    = 0x4000;   // - 15 (16384 )
    public static final short CAT_ALL          = 0x7fff;   // - 16 (32767 )

    //
    // Combined Categories
    public static final short CAT_ENEMY    = ( CAT_MOBILE_ENEMY | CAT_STATIC_ENEMY );
    public static final short CAT_OBSTACLE = ( CAT_GROUND | CAT_PLATFORM | Gfx.CAT_JAIL );

    // ------------------------------------------------------------------------

    // Maximum Z-sorting depth for sprites
    public static final int _MAXIMUM_Z_DEPTH = 20;

    // The desired Frame Rate
    public static final float   _FPS                 = 60f;
    public static final float   _MIN_FPS             = 30f;

    // Values for Box2D.step()
    public static final float   _STEP_TIME           = ( 1.0f / 60f );
    public static final int     _VELOCITY_ITERATIONS = 8;
    public static final int     _POSITION_ITERATIONS = 3;
    public static final Vector2 _WORLD_GRAVITY       = new Vector2( 0, -9.8f );
    public static final float   _FALL_GRAVITY        = 9.8f;
    public static       float   _DEFAULT_ZOOM        = 1.0f;
    public static final float   _LERP_SPEED          = 0.075f;

    // Data to be initialised in setData().
    //
    public static       int     _TERMINAL_VELOCITY;
    public static       float   _PIXELS_TO_METERS;

    public static float _HUD_SCENE_WIDTH;
    public static float _HUD_SCENE_HEIGHT;
    public static float _GAME_SCENE_WIDTH;
    public static float _GAME_SCENE_HEIGHT;
    public static float _PARALLAX_SCENE_WIDTH;
    public static float _PARALLAX_SCENE_HEIGHT;

    // Pixels Per Meter in the Box2D World, usually the length of a single TiledMap tile.
    public static float _PPM;

    public static int _HUD_WIDTH;                   // Width in pixels of the HUD
    public static int _HUD_HEIGHT;                  // Height in pixels of the HUD
    public static int _DESKTOP_WIDTH;               // Width in pixels of the Desktop window
    public static int _DESKTOP_HEIGHT;              // Height in pixels of the Desktop window
    public static int _VIEW_WIDTH;                  // Width in pixels of the game view
    public static int _VIEW_HEIGHT;                 // Height in pixels of the game view
    public static int _PARALLAX_VIEW_WIDTH;         // Width in pixels of the parallax view
    public static int _PARALLAX_VIEW_HEIGHT;        // Height in pixels of the parallax view

    public static final SimpleVec2F pixelDimensions = new SimpleVec2F();
    public static final SimpleVec2F meterDimensions = new SimpleVec2F();

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    public static void initialise()
    {
        setPPM( 16.0f );

        _TERMINAL_VELOCITY = ( int ) ( _PPM * _FALL_GRAVITY );
        _PIXELS_TO_METERS  = ( 1.0f / _PPM );

        setSceneDimensions();
    }

    /**
     * Sets the viewport and app window dimensions
     * for Android builds.
     */
    public static void setAndroidDimensions()
    {
        _VIEW_WIDTH           = 960;
        _VIEW_HEIGHT          = 540;
        _HUD_WIDTH            = 1280;
        _HUD_HEIGHT           = 720;
        _DESKTOP_WIDTH        = 1152;
        _DESKTOP_HEIGHT       = 650;
        _PARALLAX_VIEW_WIDTH  = 480;
        _PARALLAX_VIEW_HEIGHT = 270;
    }

    /**
     * Sets the viewport and app window dimensions
     * for LWJGL2 and LWJGL3 (Desktop) builds.
     */
    public static void setDesktopDimensions()
    {
        _VIEW_WIDTH           = 960;
        _VIEW_HEIGHT          = 540;
        _HUD_WIDTH            = 1280;
        _HUD_HEIGHT           = 720;
        _DESKTOP_WIDTH        = 1152;
        _DESKTOP_HEIGHT       = 650;
        _PARALLAX_VIEW_WIDTH  = 480;
        _PARALLAX_VIEW_HEIGHT = 270;
    }

    public static void setPPM( final float newPPM )
    {
        if ( newPPM != _PPM )
        {
            _PPM              = newPPM;
            _PIXELS_TO_METERS = ( 1.0f / _PPM );

            setSceneDimensions();
        }
    }

    public static void setSceneDimensions()
    {
        _HUD_SCENE_WIDTH       = ( _HUD_WIDTH / _PPM );
        _HUD_SCENE_HEIGHT      = ( _HUD_HEIGHT / _PPM );
        _GAME_SCENE_WIDTH      = ( _VIEW_WIDTH / _PPM );
        _GAME_SCENE_HEIGHT     = ( _VIEW_HEIGHT / _PPM );
        _PARALLAX_SCENE_WIDTH  = ( _PARALLAX_VIEW_WIDTH / _PPM );
        _PARALLAX_SCENE_HEIGHT = ( _PARALLAX_VIEW_HEIGHT / _PPM );
    }

    public static SimpleVec2F getScreenSizeInMeters()
    {
        meterDimensions.set( _VIEW_WIDTH * _PIXELS_TO_METERS, _VIEW_HEIGHT * _PIXELS_TO_METERS );

        return meterDimensions;
    }

    public static SimpleVec2F getScreenSizeInPixels()
    {
        pixelDimensions.set( _VIEW_WIDTH, _VIEW_HEIGHT );

        return pixelDimensions;
    }

    public static float PixelsToMeters( float pixels )
    {
        return pixels * _PIXELS_TO_METERS;
    }
}
