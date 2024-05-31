package com.richikin.platformania.graphics.renderers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.richikin.platformania.config.Settings;
import com.richikin.platformania.enums.CamID;
import com.richikin.platformania.enums.StateID;
import com.richikin.platformania.graphics.camera.OrthoGameCamera;
import com.richikin.platformania.graphics.camera.ViewportType;
import com.richikin.platformania.graphics.camera.Zoom;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.maths.SimpleVec3F;
import com.richikin.platformania.core.App;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.graphics.parallax.ParallaxBackground;
import com.richikin.platformania.graphics.parallax.ParallaxUtils;
import org.jetbrains.annotations.NotNull;

public class BaseRenderer implements Disposable
{
    private static final float _DEFAULT_HUD_ZOOM      = 1.0f;
    private static final float _DEFAULT_PARALLAX_ZOOM = 1.0f;
    private static final float _DEFAULT_MAP_ZOOM      = 1.0f;

    private OrthoGameCamera    hudGameCamera;
    private OrthoGameCamera    overlayCamera;
    private OrthoGameCamera    spriteGameCamera;
    private OrthoGameCamera    tiledGameCamera;
    private OrthoGameCamera    parallaxCamera;
    private Zoom               gameZoom;
    private Zoom               hudZoom;
    private boolean            isDrawingStage;
    private ParallaxBackground parallaxBackground;
    private ParallaxUtils      parallaxUtils;

    private WorldRenderer worldRenderer;
    private HUDRenderer   hudRenderer;
    private SimpleVec3F   cameraPos;

    public BaseRenderer()
    {
    }

    /**
     * Create all game cameras and
     * associated viewports.
     */
    public void createCameras()
    {
        Trace.checkPoint();

        App.getAppConfig().camerasReady = false;

        // --------------------------------------
        // Camera for displaying a parallax background behind the tiles map and sprites.
        // Using a seperate camera to allow camera effects to be applied
        // to mainGameCamera without affecting the background.
        parallaxCamera = new OrthoGameCamera
            (
                Gfx._PARALLAX_SCENE_WIDTH, Gfx._PARALLAX_SCENE_HEIGHT,
                ViewportType._STRETCH,
                "Parallax Cam"
            );

        parallaxBackground = new ParallaxBackground();
        parallaxUtils      = new ParallaxUtils();

        // --------------------------------------
        // Camera for displaying the tiled map data.
        tiledGameCamera = new OrthoGameCamera
            (
                Gfx._GAME_SCENE_WIDTH, Gfx._GAME_SCENE_HEIGHT,
                ViewportType._STRETCH,
                "Tiled Cam"
            );

        // --------------------------------------
        // Camera for displaying game scene, usually just sprites.
        spriteGameCamera = new OrthoGameCamera
            (
                Gfx._GAME_SCENE_WIDTH, Gfx._GAME_SCENE_HEIGHT,
                ViewportType._STRETCH,
                "Sprite Cam"
            );

        // --------------------------------------
        // Camera for displaying the tiled map data which is overlayed
        // on TOP of sprites etc.
        overlayCamera = new OrthoGameCamera
            (
                Gfx._GAME_SCENE_WIDTH, Gfx._GAME_SCENE_HEIGHT,
                ViewportType._STRETCH,
                "Overlay Cam"
            );

        // --------------------------------------
        // Camera for displaying the HUD
        // Using a seperate camera to allow camera effects to be applied
        // to mainGameCamera without affecting the hud.
        hudGameCamera = new OrthoGameCamera
            (
                Gfx._HUD_SCENE_WIDTH, Gfx._HUD_SCENE_HEIGHT,
                ViewportType._STRETCH,
                "Hud Cam"
            );

        gameZoom      = new Zoom();
        hudZoom       = new Zoom();
        cameraPos     = new SimpleVec3F();
        worldRenderer = new WorldRenderer();
        hudRenderer   = new HUDRenderer();

        parallaxCamera.setCameraZoom( _DEFAULT_PARALLAX_ZOOM );
        tiledGameCamera.setCameraZoom( _DEFAULT_MAP_ZOOM );
        spriteGameCamera.setCameraZoom( _DEFAULT_MAP_ZOOM );
        overlayCamera.setCameraZoom( _DEFAULT_MAP_ZOOM );
        hudGameCamera.setCameraZoom( _DEFAULT_HUD_ZOOM );

        isDrawingStage                = false;
        App.getAppConfig().camerasReady = true;
    }

    public void render()
    {
        ScreenUtils.clear( 0, 0, 0, 1, true );

        boolean positionSet = false;

        if ( App.getAppConfig().gameScreenActive() )
        {
            if ( App.getSettings().isEnabled( Settings._SCROLL_DEMO ) )
            {
                App.getMapUtils().demoScroll();

                positionSet = true;
            }
            else
            {
                if ( ( App.getPlayer() != null ) && App.getAppState().after( StateID._STATE_SETUP ) )
                {
                    int x = ( int ) ( App.getPlayer().sprite.getX() );
                    int y = ( int ) ( App.getPlayer().sprite.getY() );

                    App.getMapData().positionAt( x, y );

                    positionSet = true;
                }
            }
        }

        if ( !positionSet && !App.getAppConfig().gameScreenActive() )
        {
            App.getMapData().mapPosition.set( 0, 0 );
        }

        App.getSpriteBatch().enableBlending();

        drawParallaxLayers();
        drawTiledMap();
        drawSprites();
        drawOverlays();
        updateHudCamera();

        // ----- Draw the Stage, if enabled -----
        if ( isDrawingStage && ( App.getStage() != null ) )
        {
            App.getStage().act( Math.min( Gdx.graphics.getDeltaTime(), Gfx._STEP_TIME ) );
            App.getStage().draw();
        }

        gameZoom.stop();
        hudZoom.stop();

        App.getWorldModel().drawDebugMatrix();
    }

    private void drawParallaxLayers()
    {
        if ( parallaxCamera.isInUse )
        {
            parallaxCamera.viewport.apply();
            App.getSpriteBatch().setProjectionMatrix( parallaxCamera.camera.combined );
            App.getSpriteBatch().begin();

            cameraPos.x = ( parallaxCamera.camera.viewportWidth / 2 );
            cameraPos.y = ( parallaxCamera.camera.viewportHeight / 2 );
            cameraPos.z = 0;

            if ( parallaxCamera.isLerpingEnabled )
            {
                parallaxCamera.lerpTo( cameraPos, Gfx._LERP_SPEED, 0, true );
            }
            else
            {
                parallaxCamera.setPosition( cameraPos, 0, true );
            }

            parallaxBackground.render();

            App.getSpriteBatch().end();
        }
        else
        {
            drawGameScreenBackdrop();
        }
    }

    private void drawGameScreenBackdrop()
    {
        if ( App.getAppConfig().gameScreenActive() && ( App.getMainScene().background != null ) )
        {
            parallaxCamera.viewport.apply();
            App.getSpriteBatch().setProjectionMatrix( parallaxCamera.camera.combined );
            App.getSpriteBatch().begin();

            cameraPos.setEmpty();
            parallaxCamera.setPosition( cameraPos );

            App.getMainScene().draw( App.getSpriteBatch(), parallaxCamera );

            App.getSpriteBatch().end();
        }
    }

    private void drawTiledMap()
    {
        if ( tiledGameCamera.isInUse )
        {
            tiledGameCamera.viewport.apply();
            App.getSpriteBatch().setProjectionMatrix( tiledGameCamera.camera.combined );
            App.getSpriteBatch().begin();

            cameraPos.x = ( App.getMapData().mapPosition.getX() + ( tiledGameCamera.camera.viewportWidth / 2 ) );
            cameraPos.y = ( App.getMapData().mapPosition.getY() + ( tiledGameCamera.camera.viewportHeight / 2 ) );
            cameraPos.z = 0;

            if ( tiledGameCamera.isLerpingEnabled )
            {
                tiledGameCamera.lerpTo( cameraPos, Gfx._LERP_SPEED, gameZoom.getZoomValue(), true );
            }
            else
            {
                tiledGameCamera.setPosition( cameraPos, gameZoom.getZoomValue(), true );
            }

            App.getMapParser().render( tiledGameCamera.camera );
            App.getSpriteBatch().end();
        }
    }

    private void drawSprites()
    {
        if ( spriteGameCamera.isInUse )
        {
            spriteGameCamera.viewport.apply();
            App.getSpriteBatch().setProjectionMatrix( spriteGameCamera.camera.combined );
            App.getSpriteBatch().begin();

            cameraPos.x = ( App.getMapData().mapPosition.getX() + ( spriteGameCamera.camera.viewportWidth / 2 ) );
            cameraPos.y = ( App.getMapData().mapPosition.getY() + ( spriteGameCamera.camera.viewportHeight / 2 ) );
            cameraPos.z = 0;

            spriteGameCamera.setPosition( cameraPos, gameZoom.getZoomValue(), true );

            App.getMapParser().render( spriteGameCamera.camera );

            worldRenderer.render();

            App.getSpriteBatch().end();
        }
    }

    private void drawOverlays()
    {
        if ( overlayCamera.isInUse )
        {
            overlayCamera.viewport.apply();
            App.getSpriteBatch().setProjectionMatrix( overlayCamera.camera.combined );
            App.getSpriteBatch().begin();

            cameraPos.x = ( App.getMapData().mapPosition.getX() + ( overlayCamera.camera.viewportWidth / 2 ) );
            cameraPos.y = ( App.getMapData().mapPosition.getY() + ( overlayCamera.camera.viewportHeight / 2 ) );
            cameraPos.z = 0;

            if ( overlayCamera.isLerpingEnabled )
            {
                overlayCamera.lerpTo( cameraPos, Gfx._LERP_SPEED, gameZoom.getZoomValue(), true );
            }
            else
            {
                overlayCamera.setPosition( cameraPos, gameZoom.getZoomValue(), true );
            }

            App.getMapParser().drawOverlayLayer( overlayCamera.camera );
            App.getSpriteBatch().end();
        }
    }

    private void updateHudCamera()
    {
        if ( hudGameCamera.isInUse )
        {
            hudGameCamera.viewport.apply();
            App.getSpriteBatch().setProjectionMatrix( hudGameCamera.camera.combined );
            App.getSpriteBatch().begin();

            cameraPos.x = ( hudGameCamera.camera.viewportWidth / 2 );
            cameraPos.y = ( hudGameCamera.camera.viewportHeight / 2 );
            cameraPos.z = 0;

            hudGameCamera.setPosition( cameraPos, hudZoom.getZoomValue(), false );

            hudRenderer.render( App.getSpriteBatch(), hudGameCamera );

            App.getSpriteBatch().end();
        }
    }

    public void resizeCameras( int width, int height )
    {
        parallaxCamera.resizeViewport( width, height, true );
        tiledGameCamera.resizeViewport( width, height, true );
        spriteGameCamera.resizeViewport( width, height, true );
        overlayCamera.resizeViewport( width, height, true );
        hudGameCamera.resizeViewport( width, height, true );

        App.getStage().getViewport().update( width, height );
    }

    public void resetCameraZoom()
    {
        parallaxCamera.camera.zoom   = _DEFAULT_PARALLAX_ZOOM;
        tiledGameCamera.camera.zoom  = _DEFAULT_MAP_ZOOM;
        spriteGameCamera.camera.zoom = _DEFAULT_MAP_ZOOM;
        overlayCamera.camera.zoom    = _DEFAULT_MAP_ZOOM;
        hudGameCamera.camera.zoom    = _DEFAULT_HUD_ZOOM;

        parallaxCamera.camera.update();
        tiledGameCamera.camera.update();
        spriteGameCamera.camera.update();
        overlayCamera.camera.update();
        hudGameCamera.camera.update();

        gameZoom.stop();
        hudZoom.stop();
    }

    /**
     * Enables only the cameras specified in cameraList.
     * All others will be disabled.
     *
     * @param cameraList The list of {@link CamID} cameras to enable.
     */
    public void enableCamera( @NotNull CamID... cameraList )
    {
        disableAllCameras();

        if ( cameraList.length > 0 )
        {
            for ( CamID id : cameraList )
            {
                if ( id == CamID._PARALLAX )
                {
                    parallaxCamera.isInUse = true;
                }
                else if ( id == CamID._TILED )
                {
                    tiledGameCamera.isInUse = true;
                }
                else if ( id == CamID._SPRITE )
                {
                    spriteGameCamera.isInUse = true;
                }
                else if ( id == CamID._OVERLAY )
                {
                    overlayCamera.isInUse = true;
                }
                else if ( id == CamID._HUD )
                {
                    hudGameCamera.isInUse = true;
                }
                else if ( id == CamID._STAGE )
                {
                    isDrawingStage = true;
                }
            }
        }
    }

    public void enableAllCameras()
    {
        parallaxCamera.isInUse   = true;
        tiledGameCamera.isInUse  = true;
        spriteGameCamera.isInUse = true;
        overlayCamera.isInUse    = true;
        hudGameCamera.isInUse    = true;
        isDrawingStage           = true;
    }

    public void disableAllCameras()
    {
        parallaxCamera.isInUse   = false;
        tiledGameCamera.isInUse  = false;
        spriteGameCamera.isInUse = false;
        overlayCamera.isInUse    = false;
        hudGameCamera.isInUse    = false;
        isDrawingStage           = false;
    }

    public void disableLerping()
    {
        parallaxCamera.isLerpingEnabled   = false;
        tiledGameCamera.isLerpingEnabled  = false;
        spriteGameCamera.isLerpingEnabled = false;
        overlayCamera.isLerpingEnabled    = false;
        hudGameCamera.isLerpingEnabled    = false;
    }

    public OrthoGameCamera getHudGameCamera()
    {
        return hudGameCamera;
    }

    public OrthoGameCamera getOverlayCamera()
    {
        return overlayCamera;
    }

    public OrthoGameCamera getSpriteGameCamera()
    {
        return spriteGameCamera;
    }

    public OrthoGameCamera getTiledGameCamera()
    {
        return tiledGameCamera;
    }

    public OrthoGameCamera getParallaxCamera()
    {
        return parallaxCamera;
    }

    public Zoom getGameZoom()
    {
        return gameZoom;
    }

    public Zoom getHudZoom()
    {
        return hudZoom;
    }

    public boolean isDrawingStage()
    {
        return isDrawingStage;
    }

    public ParallaxBackground getParallaxBackground()
    {
        return parallaxBackground;
    }

    public ParallaxUtils getParallaxUtils()
    {
        return parallaxUtils;
    }

    @Override
    public void dispose()
    {
        parallaxCamera.dispose();
        tiledGameCamera.dispose();
        spriteGameCamera.dispose();
        overlayCamera.dispose();
        hudGameCamera.dispose();

        parallaxCamera   = null;
        tiledGameCamera  = null;
        spriteGameCamera = null;
        overlayCamera    = null;
        hudGameCamera    = null;

        parallaxBackground.dispose();
        parallaxBackground = null;

        cameraPos     = null;
        gameZoom      = null;
        hudZoom       = null;
        worldRenderer = null;
        hudRenderer   = null;
    }
}
