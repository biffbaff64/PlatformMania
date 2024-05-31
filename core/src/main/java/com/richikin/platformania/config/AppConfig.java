package com.richikin.platformania.config;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.Array;
import com.richikin.platformania.enums.ScreenID;
import com.richikin.platformania.enums.StateID;
import com.richikin.platformania.google.PlayServicesData;
import com.richikin.platformania.input.Switch;
import com.richikin.platformania.input.controllers.ControllerPos;
import com.richikin.platformania.input.controllers.ControllerType;
import com.richikin.platformania.logging.Stats;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.core.App;
import com.richikin.platformania.graphics.GameAssets;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.scenes.MainScene;
import com.richikin.platformania.scenes.TitleScene;
import com.richikin.platformania.ui.Scene2DUtils;

public class AppConfig
{
    public boolean                 isShuttingMainScene;        // Game over, back to menu screen
    public boolean                 forceQuitToMenu;            // Quit to main menu, forced via pause mode for example.
    public boolean                 gamePaused;                 // TRUE / FALSE Game Paused flag
    public boolean                 camerasReady;               // TRUE when all cameras have been created.
    public boolean                 shutDownActive;             // TRUE if game is currently processing EXIT request.
    public boolean                 entitiesExist;              // Set true when all entities have been created
    public boolean                 controllersFitted;          // TRUE if external controllers are fitted/connected.
    public boolean                 gameButtonsReady;           // TRUE When all game buttons have been defined
    public ScreenID                currentScreenID;            // ID of the currently active screeen
    public String                  currentController;          // The name of the controller being used
    public ControllerPos           virtualControllerPos;       // Virtual (on-screen) joystick position (LEFT or RIGHT)
    public Array< ControllerType > availableInputs;            // ...
    public Switch                  systemBackButton;           // ...
    public ImageButton             backButton;                 // ...

    private StateID startupState;

    // ---------------------------------------------------
    // ---------------------------------------------------

    public AppConfig()
    {
        Trace.checkPoint();

        startupState = StateID._STATE_BEGIN_STARTUP;
    }

    public void setup()
    {
        Trace.checkPoint();

        App.createEssentialObjects();

        // ------------------------------------------------
        App.getDev().setDeveloperModeState();
        App.getDev().setTempDeveloperSettings();
        // ------------------------------------------------

        Gfx.initialise();

        isShuttingMainScene = false;
        forceQuitToMenu     = false;
        gamePaused          = false;
        camerasReady        = false;
        shutDownActive      = false;
        entitiesExist       = false;
        controllersFitted   = false;
        gameButtonsReady    = false;
        currentController   = "None";
        availableInputs     = new Array<>();

        if ( isDesktopApp() )
        {
            Gdx.graphics.setWindowedMode( Gfx._DESKTOP_WIDTH, Gfx._DESKTOP_HEIGHT );
        }

        virtualControllerPos = ControllerPos._HIDDEN;

        App.getAppConfig().setControllerTypes();

        systemBackButton = new Switch();

        Stats.setup( "com.richikin.platformania.meters" );

        //
        // These essential objects have now been created. Setup/Initialise for any essential
        // objects required before TitleScene can be created is mostly performed in startApp().
    }

    public void exit()
    {
        Trace.checkPoint();

        availableInputs.clear();
        availableInputs = null;

        currentController    = null;
        virtualControllerPos = null;
        systemBackButton     = null;

        backButton.addAction( Actions.removeActor() );
        backButton = null;

        Gdx.app.exit();
    }

    /**
     * @return TRUE if the app is running on Desktop
     */
    public boolean isDesktopApp()
    {
        return ( Gdx.app.getType() == Application.ApplicationType.Desktop );
    }

    /**
     * @return TRUE if the app is running on Android
     */
    public boolean isAndroidApp()
    {
        return ( Gdx.app.getType() == Application.ApplicationType.Android );
    }

    /**
     * @return TRUE If an external controller is fitted
     */
    public boolean isControllerFitted()
    {
        return controllersFitted;
    }

    public void addBackButton( String _default, String _pressed )
    {
        Scene2DUtils scene2DUtils = new Scene2DUtils();

        backButton = scene2DUtils.addButton( _default, _pressed, 0, 0 );
    }

    public void showAndEnableBackButton()
    {
        if ( backButton != null )
        {
            backButton.setVisible( true );
            backButton.setDisabled( false );
        }
    }

    public void hideAndDisableBackButton()
    {
        if ( backButton != null )
        {
            backButton.setVisible( false );
            backButton.setDisabled( true );
        }
    }

    public void startApp()
    {
        Trace.checkPoint();

        App.getWorldModel().createWorld();
        App.getAssets().initialise();
        App.getSettings().freshInstallCheck();
        App.getSettings().debugReport();

        // ------------------------------------------------------------------
        // Google Play Services setup - Android only.
        if ( App.getAppConfig().isAndroidApp() )
        {
            Trace.dbg( "Initialising Google Play Services." );

            App.getPlayServices().setup();
            App.getPlayServices().createApiClient();

            PlayServicesData psd = new PlayServicesData();
            psd.setup();
        }
        // ------------------------------------------------------------------

        App.getBaseRenderer().createCameras();
        App.createStage( App.getBaseRenderer().getHudGameCamera().viewport );
        App.getAABBRenderer().setup( GameAssets._PRO_WINDOWS_FONT );
        App.getWorldModel().createB2DRenderer();
        App.getAudio().setup();
        App.getInputManager().setup();

        startupState = StateID._STATE_END_STARTUP;

        Trace.divider();
    }

    /**
     * Ends the startup process by handing control to the {@link TitleScene} or, if TitleScene is
     * disabled, control is passed to {@link MainScene}
     */
    public void closeStartup()
    {
        App.getDev().configReport();

        App.setTitleScene( new TitleScene() );

        // Development option, to allow skipping of the main menu
        // and moving straight to the game scene.
        if ( App.getDev().isDevMode() && App.getSettings().isDisabled( Settings._MENU_SCENE ) )
        {
            App.createMainSceneObjects();
            App.setMainScene( new MainScene() );
            App.getMainScene().reset();
            App.getMainGame().setScreen( App.getMainScene() );
        }
        else
        {
            App.getMainGame().setScreen( App.getTitleScene() );
        }
    }

    public boolean isStartupDone()
    {
        return ( startupState == StateID._STATE_END_STARTUP );
    }

    /**
     * If set, this game will use the Ashley Entity Component System instead of my standard sprite
     * system.
     */
    public boolean isUsingAshleyECS()
    {
        return App.getSettings().isEnabled( Settings._USING_ASHLEY_ECS );
    }

    public boolean gameScreenActive()
    {
        return App.getAppConfig().currentScreenID == ScreenID._GAME_SCREEN;
    }

    public void setControllerTypes()
    {
        if ( App.getAppConfig().isAndroidApp() || App.getDev().isAndroidOnDesktop() )
        {
            Trace.dbg( "Enabling _VIRTUAL controller." );

            App.getAppConfig().availableInputs.add( ControllerType._BUTTONS );

            if ( App.getDev().isAndroidOnDesktop() )
            {
                App.getAppConfig().availableInputs.add( ControllerType._KEYBOARD );
            }
        }
        else
        {
            Trace.dbg( "Enabling _EXTERNAL controller." );
            Trace.dbg( "Enabling _KEYBOARD controller." );

            App.getAppConfig().availableInputs.add( ControllerType._EXTERNAL );
            App.getAppConfig().availableInputs.add( ControllerType._KEYBOARD );
        }
    }

    public boolean isUsingOnScreenControls()
    {
        return ( App.getAppConfig().availableInputs.contains( ControllerType._JOYSTICK, true )
            || App.getAppConfig().availableInputs.contains( ControllerType._DPAD, true )
            || App.getAppConfig().availableInputs.contains( ControllerType._BUTTONS, true ) );
    }

    public void freshInstallCheck()
    {
        Trace.checkPoint();

        if ( !App.getSettings().isEnabled( Settings._INSTALLED ) )
        {
            Trace.dbg( "FRESH INSTALL." );

            Trace.dbg( "Initialising all App settings to default values." );
            App.getSettings().resetToDefaults();

            Trace.dbg( "Setting all Statistical logging meters to zero." );
            Stats.resetAllMeters();

            App.getSettings().enable( Settings._INSTALLED );
        }
    }

    /**
     * Pause the game
     */
    public void pause()
    {
        App.getAppState().set( StateID._STATE_PAUSED );
        App.getAppConfig().gamePaused = true;

        if ( ( App.getHud().hudStateID != StateID._STATE_SETTINGS_PANEL )
            && ( App.getHud().hudStateID != StateID._STATE_DEVELOPER_PANEL ) )
        {
            App.getHud().hudStateID = StateID._STATE_PAUSED;
        }
    }

    /**
     * Un-pause the game
     */
    public void unPause()
    {
        App.getAppState().set( StateID._STATE_GAME );

        App.getAppConfig().gamePaused = false;
        App.getHud().hudStateID     = StateID._STATE_PANEL_UPDATE;
    }
}
