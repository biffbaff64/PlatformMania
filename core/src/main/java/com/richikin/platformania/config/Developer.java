package com.richikin.platformania.config;

import com.richikin.platformania.enums.StateID;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.input.controllers.ControllerType;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.ui.DeveloperPanel;
import com.richikin.platformania.core.App;

public class Developer
{
    private StateID developerPanelState = StateID._STATE_DISABLED;
    private boolean isAndroidOnDesktop  = false;
    private boolean isGodMode           = false;
    private boolean isDevMode           = false;

    public void setTempDeveloperSettings()
    {
        if ( isDevMode() )
        {
            Trace.divider();
            Trace.dbg( "Temporary Development Settings" );

            setAndroidOnDesktop( false );
            setGodMode( false );

            final String[] disableList =
                {
                    Settings._SHOW_FPS,
                    Settings._USING_ASHLEY_ECS,
                    Settings._CULL_SPRITES,
                    Settings._BUTTON_BOXES,
                    Settings._DISABLE_PLAYER,
                    Settings._SHADER_PROGRAM,
                    Settings._TILE_BOXES,
                    Settings._SPRITE_BOXES,
                    Settings._LEVEL_SELECT,
                    Settings._SCROLL_DEMO,
                    };

            final String[] enableList =
                {
                    Settings._BOX2D_PHYSICS,
                    Settings._BOX2D_DEBUG,
                    Settings._VIBRATIONS,
                    Settings._INSTALLED,
                    Settings._MENU_SCENE,
                    Settings._SHOW_DEBUG,
                    };

            for ( String str : disableList )
            {
                App.getSettings().disable( str );
            }

            for ( String str : enableList )
            {
                App.getSettings().enable( str );
            }

            Trace.divider();
        }
    }

    /**
     * Reads the environment variable '_DEV_MODE', and sets 'isDevMode' accordingly.
     * Note: Android builds default to _DEV_MODE = false.
     */
    public void setDeveloperModeState()
    {
        if ( App.getAppConfig().isDesktopApp() )
        {
            isDevMode = "TRUE".equalsIgnoreCase( System.getenv( "_DEV_MODE" ) );
        }
        else
        {
            isDevMode = false;
        }

        Trace.dbg( "Developer Mode is ", isDevMode ? "ENABLED." : "DISABLED." );
    }

    /**
     * Sets 'isDevMode' flag to the specified state.
     * @param _state TRUE or FALSE.
     */
    public void setDevMode( boolean _state )
    {
        isDevMode = _state;
    }

    /**
     * Sets 'isGodMode' flag to the specified state.
     * @param _state TRUE or FALSE.
     */
    public void setGodMode( boolean _state )
    {
        isGodMode = _state;
    }

    /**
     * If enabled, this flag allows testing of android related tests on desktop builds.
     */
    public void setAndroidOnDesktop( boolean _state )
    {
        isAndroidOnDesktop = _state;
    }

    public boolean isAndroidOnDesktop()
    {
        return isAndroidOnDesktop;
    }

    /**
     * 'Developer Mode' is only allowed on Desktop builds
     * @return TRUE if enabled.
     */
    public boolean isDevMode()
    {
        return App.getAppConfig().isDesktopApp() && isDevMode;
    }

    /**
     * 'GOD Mode' is only allowed on Desktop builds
     * @return TRUE if enabled.
     */
    public boolean isGodMode()
    {
        return App.getAppConfig().isDesktopApp() && isGodMode;
    }

    /**
     * Enables or disables the Developer Settings Panel. The only valid states are:-
     * StateID._STATE_DISABLED StateID._STATE_ENABLED All other states will default to
     * _STATE_DISABLED.
     * @param state The panel State.
     */
    public void setDeveloperPanelState( StateID state )
    {
        switch ( state )
        {
            case _STATE_ENABLED:
            case _STATE_DISABLED:
            {
                developerPanelState = state;
            }
            break;

            default:
            {
                developerPanelState = StateID._STATE_DISABLED;
            }
            break;
        }
    }

    /**
     * Returns the current state of the {@link DeveloperPanel}
     * @return Either _STATE_ENABLED or _STATE_DISABLED. ALl other states are invalid.
     */
    public StateID getDeveloperPanelState()
    {
        return developerPanelState;
    }

    public void configReport()
    {
        if ( isDevMode() )
        {
            Trace.divider();
            Trace.dbg( "Android App         : " + App.getAppConfig().isAndroidApp() );
            Trace.dbg( "Desktop App         : " + App.getAppConfig().isDesktopApp() );
            Trace.divider();
            Trace.dbg( "isDevMode()         : " + isDevMode() );
            Trace.dbg( "isGodMode()         : " + isGodMode() );
            Trace.divider();
            Trace.dbg( "_DESKTOP_WIDTH      : " + Gfx._DESKTOP_WIDTH );
            Trace.dbg( "_DESKTOP_HEIGHT     : " + Gfx._DESKTOP_HEIGHT );
            Trace.dbg( "_VIEW_WIDTH         : " + Gfx._VIEW_WIDTH );
            Trace.dbg( "_VIEW_HEIGHT        : " + Gfx._VIEW_HEIGHT );
            Trace.dbg( "_HUD_WIDTH          : " + Gfx._HUD_WIDTH );
            Trace.dbg( "_HUD_HEIGHT         : " + Gfx._HUD_HEIGHT );
            Trace.dbg( "_GAME_SCENE_WIDTH   : " + Gfx._GAME_SCENE_WIDTH );
            Trace.dbg( "_GAME_SCENE_HEIGHT  : " + Gfx._GAME_SCENE_HEIGHT );
            Trace.dbg( "_HUD_SCENE_WIDTH    : " + Gfx._HUD_SCENE_WIDTH );
            Trace.dbg( "_HUD_SCENE_HEIGHT   : " + Gfx._HUD_SCENE_HEIGHT );
            Trace.divider();
            Trace.dbg( "_PPM                : " + Gfx._PPM );
            Trace.divider();
            Trace.dbg( "_VIRTUAL?           : " + App.getAppConfig().availableInputs.contains( ControllerType._JOYSTICK, true ) );
            Trace.dbg( "_EXTERNAL?          : " + App.getAppConfig().availableInputs.contains( ControllerType._EXTERNAL, true ) );
            Trace.dbg( "_KEYBOARD?          : " + App.getAppConfig().availableInputs.contains( ControllerType._KEYBOARD, true ) );
            Trace.dbg( "controllerPos       : " + App.getAppConfig().virtualControllerPos );
            Trace.dbg( "controllersFitted   : " + App.getAppConfig().controllersFitted );
            Trace.dbg( "usedController      : " + App.getAppConfig().currentController );
            Trace.divider();
        }
    }
}
