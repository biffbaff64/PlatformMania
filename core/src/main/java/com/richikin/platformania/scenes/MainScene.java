package com.richikin.platformania.scenes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.richikin.platformania.config.Settings;
import com.richikin.platformania.core.App;
import com.richikin.platformania.core.EndgameManager;
import com.richikin.platformania.core.MainGameHandler;
import com.richikin.platformania.enums.ScreenID;
import com.richikin.platformania.enums.StateID;
import com.richikin.platformania.graphics.camera.OrthoGameCamera;
import com.richikin.platformania.graphics.camera.Shake;
import com.richikin.platformania.graphics.parallax.ParallaxLayer;
import com.richikin.platformania.input.controllers.ControllerType;
import com.richikin.platformania.logging.StopWatch;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.physics.Box2DWorldContactListener;
import com.richikin.platformania.ui.GameCompletedPanel;

public class MainScene extends AbstractBaseScene
{
    public GameCompletedPanel gameCompletedPanel;
    public EndgameManager     endgameManager;
    public MainGameHandler    mainGameHandler;
    public StopWatch          retryDelay;
    public ContactListener    collisionListener;

    /*
     * boolean firstTime - TRUE if MainScene has just been entered, i.e. a NEW Game.
     *
     * Setting this to true allows 'initialise()' to be called from show(), one time
     * only. If false, then initialise() will be bypassed but the rest of show()
     * will be processed.
     */
    public boolean       firstTime;
    public ParallaxLayer background;

    public MainScene()
    {
        super();

        firstTime = true;
    }

    @Override
    public void initialise()
    {
        if ( firstTime )
        {
            Trace.divider( '#' );
            Trace.dbg( "NEW GAME:" );
            Trace.dbg( "_DEVMODE: ", App.getDev().isDevMode() );
            Trace.dbg( "_GODMODE: ", App.getDev().isGodMode() );
            Trace.divider( '#' );

            gameCompletedPanel = new GameCompletedPanel();
            endgameManager     = new EndgameManager();
            mainGameHandler    = new MainGameHandler();

            App.getLevelManager().prepareNewGame();

            App.getAppState().set( StateID._STATE_SETUP );

            collisionListener = new Box2DWorldContactListener();

            App.getWorldModel().box2DContactListener.addListener( collisionListener );

            App.getBaseRenderer().getHudGameCamera().setCameraZoom( 0.6f );
        }

        if ( App.getAppConfig().availableInputs.contains( ControllerType._JOYSTICK, true ) )
        {
            App.getInputManager().getVirtualJoystick().show();
        }

        Shake.setAllowed( App.getSettings().isEnabled( Settings._VIBRATIONS ) );
    }

    @Override
    public void update()
    {
        switch ( App.getAppState().peek() )
        {
            // These are here in case there is a lag between appstate
            // being set to these values and control being passed
            // to a different scene.
            case _STATE_MAIN_MENU:
            case _STATE_CLOSING:
            {
            }
            break;

            // All relevant states which apply
            // to this scene.
            case _STATE_SETUP:
            case _STATE_GET_READY:
            case _STATE_WELCOME_PANEL:
            case _STATE_DEVELOPER_PANEL:
            case _STATE_PAUSED:
            case _STATE_GAME:
            case _STATE_MESSAGE_PANEL:
            case _STATE_PREPARE_LEVEL_RETRY:
            case _STATE_LEVEL_RETRY:
            case _STATE_PREPARE_LEVEL_FINISHED:
            case _STATE_LEVEL_FINISHED:
            case _STATE_PREPARE_GAME_OVER_MESSAGE:
            case _STATE_GAME_OVER:
            case _STATE_GAME_FINISHED:
            case _STATE_END_GAME:
            {
                mainGameHandler.update();
            }
            break;

            default:
            {
                Trace.dbg( "Unsupported game state: " + App.getAppState().peek() );
            }
            break;
        }
    }

    @Override
    public void render( float delta )
    {
        super.update();

        App.getMapData().update();
        App.getGameProgress().update();

        if ( App.getAppConfig().gameScreenActive() )
        {
            update();

            super.render( delta );

            App.getWorldModel().worldStep();
        }
    }

    public void draw( final SpriteBatch spriteBatch, OrthoGameCamera camera )
    {
        if ( background != null )
        {
            background.draw();
        }
    }

    @Override
    public void show()
    {
        Trace.checkPoint();

        super.show();

        App.getAppConfig().currentScreenID = ScreenID._GAME_SCREEN;
        App.getBaseRenderer().disableAllCameras();

        App.getWorldModel().activate();

        initialise();

        App.getAppConfig().hideAndDisableBackButton();

        App.getAppState().set( StateID._STATE_SETUP );
    }

    @Override
    public void hide()
    {
        Trace.checkPoint();

        super.hide();

        App.getWorldModel().deActivate();
    }

    /**
     * Resets this scene for re-use.
     */
    public void reset()
    {
        firstTime = true;
    }

    @Override
    public void loadImages()
    {
        App.getBaseRenderer().getParallaxBackground().setupLayers
            (
                App.getMapData().backgroundLayers[ App.getMapData().backgroundIndexes[ App.getLevel() ] ]
            );
    }

    @Override
    public void dispose()
    {
        if ( background != null )
        {
            background.dispose();
        }

        background         = null;
        gameCompletedPanel = null;
        endgameManager     = null;
        mainGameHandler    = null;
        retryDelay         = null;
        collisionListener  = null;
    }
}
