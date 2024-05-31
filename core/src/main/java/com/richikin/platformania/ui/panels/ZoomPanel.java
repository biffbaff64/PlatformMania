package com.richikin.platformania.ui.panels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.richikin.platformania.core.App;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.enums.StateID;
import com.richikin.platformania.graphics.camera.Zoom;
import com.richikin.platformania.logging.StopWatch;
import com.richikin.platformania.ui.DefaultPanel;

import java.util.concurrent.TimeUnit;

public class ZoomPanel extends DefaultPanel implements IPanel
{
    private final static int   _DEFAULT_PAUSE_TIME = 1500;
    private final static float _DEFAULT_SPEED      = 0.04f;
    private final static float _DEFAULT_SPEED_INC  = 0.01f;
    private final static float _MAXIMUM_ZOOM       = 8.0f;
    private final static float _MINIMUM_ZOOM       = 0.1f;
    private final static float _PAUSED_ZOOM        = 0.75f;

    private StopWatch stopWatch;
    private float     zoomSpeed;
    private boolean   isFinished;
    private boolean   canPause;       // enable / disable pausing
    private int       pauseTime;      // How long to pause for. Default is 1500ms.
    private boolean   bounceBack;     // bounces back out of view after pausing
    private Zoom      zoom;

    public ZoomPanel()
    {
        super();
    }

    @Override
    public void initialise(TextureRegion objectRegion, String imageName, final Object... args)
    {
        textureRegion   = objectRegion;
        nameID          = imageName;
        canPause        = Boolean.parseBoolean(args[0].toString());
        pauseTime       = _DEFAULT_PAUSE_TIME;
        bounceBack      = Boolean.parseBoolean(args[1].toString());
        isFinished      = false;
        stopWatch       = new StopWatch();
        zoom            = new Zoom();
        zoomSpeed       = _DEFAULT_SPEED;

        setState(StateID._STATE_ZOOM_IN);

        setPosition
            (
                (float) ((Gfx._HUD_WIDTH - textureRegion.getRegionWidth()) / 2),
                (float) ((Gfx._HUD_HEIGHT - textureRegion.getRegionHeight()) / 2)
            );
    }

    @Override
    public boolean update()
    {
        if (!isFinished)
        {
            //
            // Zoom the panel into view
            //
            // Pause for a short while before continuing
            //
            // Shrink the panel back down to its original size
            //
            // Alternative ending, panel continues zooming in
            // to fill the screen before closing.
            switch ( getState() )
            {
                case _STATE_ZOOM_IN:
                    zoom.in( zoomSpeed );
                    if ( zoom.getZoomValue() > _PAUSED_ZOOM )
                    {
                        if ( canPause )
                        {
                            setState( StateID._STATE_PAUSED );
                            stopWatch.reset();
                        }
                        else
                        {
                            setState( bounceBack ? StateID._STATE_ZOOM_OUT : StateID._STATE_CLOSING );
                        }
                    }
                    zoomSpeed += _DEFAULT_SPEED_INC;
                    break;
                //
                // Do nothing while until the caller changes
                // the state.
                case _STATE_PANEL_UPDATE:
                    break;
                case _STATE_PAUSED:
                    if ( stopWatch.time( TimeUnit.MILLISECONDS ) >= pauseTime )
                    {
                        setState( bounceBack ? StateID._STATE_ZOOM_OUT : StateID._STATE_CLOSING );

                        zoomSpeed = _DEFAULT_SPEED;
                    }
                    break;
                case _STATE_ZOOM_OUT:
                    zoom.out( zoomSpeed );
                    if ( zoom.getZoomValue() <= _MINIMUM_ZOOM )
                    {
                        dispose();

                        isFinished = true;
                    }
                    zoomSpeed += _DEFAULT_SPEED_INC;
                    break;
                case _STATE_CLOSING:
                    zoom.in( zoomSpeed );
                    if ( zoom.getZoomValue() > _MAXIMUM_ZOOM )
                    {
                        dispose();

                        isFinished = true;
                    }
                    zoomSpeed += _DEFAULT_SPEED_INC;
                    break;
                default:
                    break;
            }
        }

        return isFinished;
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        if (!isFinished)
        {
            App.getSpriteBatch().draw
                (
                    textureRegion,
                    getPosition().x,
                    getPosition().y,
                    App.getHud().hudOriginX + (textureRegion.getRegionWidth() / 2f),
                    App.getHud().hudOriginY + (textureRegion.getRegionHeight() / 2f),
                    textureRegion.getRegionWidth(),
                    textureRegion.getRegionHeight(),
                    zoom.getZoomValue(),
                    zoom.getZoomValue(),
                    0.0f
                );
        }
    }

    @Override
    public void setPauseTime(int pauseTime)
    {
        this.pauseTime = pauseTime;
    }

    public void forceZoomOut()
    {
        zoomSpeed = _DEFAULT_SPEED;

        setState(StateID._STATE_ZOOM_OUT);
    }
}
