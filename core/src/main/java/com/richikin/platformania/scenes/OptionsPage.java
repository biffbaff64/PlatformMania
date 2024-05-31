package com.richikin.platformania.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.richikin.platformania.config.Settings;
import com.richikin.platformania.config.Version;
import com.richikin.platformania.core.App;
import com.richikin.platformania.enums.ScreenID;
import com.richikin.platformania.enums.StateID;
import com.richikin.platformania.graphics.GameAssets;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.maths.Vec2;
import com.richikin.platformania.ui.DevPanelData;
import com.richikin.platformania.ui.DeveloperPanel;
import com.richikin.platformania.ui.Scene2DUtils;
import com.richikin.platformania.ui.StatsPanel;
import com.richikin.platformania.ui.UIPage;

public class OptionsPage implements UIPage
{
    public DeveloperPanel developerPanel;

    private ImageButton buttonStats;
    private ImageButton buttonAbout;
    private ImageButton buttonDevOptions;
    private CheckBox    musicCheckBox;
    private CheckBox    fxCheckBox;
    private Texture     foreground;
    private Skin        skin;
    private ScreenID    activePanel;
    private StatsPanel  statsPanel;
    private boolean     justFinishedOptionsPanel;
    private boolean     enteredDeveloperPanel;
    private Vec2        panelPos;
    private Label       versionLabel;

    public OptionsPage()
    {
    }

    @Override
    public void initialise()
    {
        Trace.checkPoint();

        if ( App.getAppConfig().currentScreenID == ScreenID._MAIN_MENU )
        {
            if ( App.getAppConfig().backButton != null )
            {
                App.getAppConfig().showAndEnableBackButton();
                App.getAppConfig().backButton.setChecked( false );
            }
        }

        foreground = App.getAssets().loadSingleAsset( App.getAssets().getOptionsPanelAsset(), Texture.class );
        skin       = new Skin( Gdx.files.internal( App.getAssets().getSkinFilename() ) );

        panelPos = new Vec2
                (
                        ( int ) ( App.getBaseRenderer().getHudGameCamera().camera.viewportWidth - foreground.getWidth() ) / 2,
                        ( int ) ( App.getBaseRenderer().getHudGameCamera().camera.viewportHeight - foreground.getHeight() ) / 2
                );

        Scene2DUtils scene2DUtils = new Scene2DUtils();

        versionLabel = scene2DUtils.addLabel
                (
                        Version.getDisplayVersion(),
                        new Vec2( panelPos.x, panelPos.y - 30 ),
                        Color.WHITE,
                        new Skin( Gdx.files.internal( App.getAssets().getSkinFilename() ) )
                );
        versionLabel.setFontScale( 1.35f, 1.35f );
        versionLabel.setAlignment( Align.left );

        populateTable();
        createButtonListeners();
        createCheckboxListeners();
        updateSettingsOnEntry();

        activePanel    = ScreenID._SETTINGS_SCREEN;
        developerPanel = new DeveloperPanel();
        DevPanelData.initialiseDeveloperMenu( this );
        enteredDeveloperPanel    = false;
        justFinishedOptionsPanel = false;

        App.getDev().setDeveloperPanelState( StateID._STATE_DISABLED );
    }

    @Override
    public boolean update()
    {
        if ( App.getAppConfig().backButton.isChecked() )
        {
            switch ( activePanel )
            {
                case _STATS_SCREEN:
                {
                    if ( statsPanel != null )
                    {
                        statsPanel.dispose();
                    }

                    statsPanel               = null;
                    justFinishedOptionsPanel = false;
                }
                break;

                case _INSTRUCTIONS_SCREEN:
                {
                }
                break;

                case _DEVELOPER_PANEL:
                {
                    developerPanel.update();
                }
                break;

                default:
                {
                    updateSettings();
                    justFinishedOptionsPanel = true;
                }
                break;
            }

            if ( !justFinishedOptionsPanel )
            {
                showActors( true );
                activePanel = ScreenID._SETTINGS_SCREEN;
                App.getAppConfig().backButton.setChecked( false );
            }
        }

        if ( enteredDeveloperPanel && ( App.getDev().getDeveloperPanelState() == StateID._STATE_DISABLED ) )
        {
            enteredDeveloperPanel = false;
            showActors( true );

            if ( App.getAppConfig().currentScreenID == ScreenID._MAIN_MENU )
            {
                App.getAppConfig().backButton.setVisible( true );
                App.getAppConfig().backButton.setDisabled( false );
            }
        }

        return justFinishedOptionsPanel;
    }

    @Override
    public void draw( SpriteBatch spriteBatch )
    {
        switch ( activePanel )
        {
            case _STATS_SCREEN:
                statsPanel.draw( spriteBatch );
                break;

            case _DEVELOPER_PANEL:
                developerPanel.draw( spriteBatch );
                break;

            case _SETTINGS_SCREEN:
            default:
            {
                if ( foreground != null )
                {
                    spriteBatch.draw( foreground, panelPos.x, panelPos.y );
                }
            }
            break;
        }
    }

    @Override
    public void show()
    {
        Trace.checkPoint();

        showActors( true );

        App.getAppConfig().showAndEnableBackButton();
    }

    @Override
    public void hide()
    {
        Trace.checkPoint();

        showActors( false );
    }

    private void showActors( boolean visibility )
    {
        buttonDevOptions.setVisible( visibility );
        buttonAbout.setVisible( visibility );
        buttonStats.setVisible( visibility );

        musicCheckBox.setVisible( visibility );
        fxCheckBox.setVisible( visibility );

        versionLabel.setVisible( visibility );
    }

    private void updateSettings()
    {
        App.getSettings().getPrefs().putBoolean( Settings._MUSIC_ENABLED, musicCheckBox.isChecked() );
        App.getSettings().getPrefs().putBoolean( Settings._SOUNDS_ENABLED, fxCheckBox.isChecked() );
        App.getSettings().getPrefs().flush();
    }

    private void updateSettingsOnEntry()
    {
        musicCheckBox.setChecked( App.getSettings().isEnabled( Settings._MUSIC_ENABLED ) );
        fxCheckBox.setChecked( App.getSettings().isEnabled( Settings._SOUNDS_ENABLED ) );
    }

    private void createCheckboxListeners()
    {
        if ( musicCheckBox != null )
        {
            musicCheckBox.addListener( new ChangeListener()
            {
                /**
                 * @param event the {@link ChangeEvent}
                 * @param actor The event target, which is the actor that emitted the change event.
                 */
                @Override
                public void changed( ChangeEvent event, Actor actor )
                {
                    App.getSettings().getPrefs().putBoolean( Settings._MUSIC_ENABLED, musicCheckBox.isChecked() );
                    App.getSettings().getPrefs().flush();
                }
            } );
        }

        if ( fxCheckBox != null )
        {
            fxCheckBox.addListener( new ChangeListener()
            {
                /**
                 * @param event the {@link ChangeEvent}
                 * @param actor The event target, which is the actor that emitted the change event.
                 */
                @Override
                public void changed( ChangeEvent event, Actor actor )
                {
                    App.getSettings().getPrefs().putBoolean( Settings._SOUNDS_ENABLED, fxCheckBox.isChecked() );
                    App.getSettings().getPrefs().flush();
                }
            } );
        }
    }

    private void createButtonListeners()
    {
        if ( App.getDev().isDevMode() )
        {
            /*
             * Statistics button.
             * Displays the in-game statistics.
             */
            if ( buttonStats != null )
            {
                buttonStats.addListener( new ClickListener()
                {
                    public void clicked( InputEvent event, float x, float y )
                    {
                        if ( statsPanel == null )
                        {
                            showActors( false );

                            statsPanel = new StatsPanel( 0, 0 );
                            statsPanel.setup();

                            activePanel = ScreenID._STATS_SCREEN;
                        }
                    }
                } );
            }

            /*
             * Developer Options Button.
             * Provides a button for accessing DEV MODE ONLY game option settings
             */
            if ( buttonDevOptions != null )
            {
                buttonDevOptions.addListener( new ClickListener()
                {
                    public void clicked( InputEvent event, float x, float y )
                    {
                        if ( App.getDev().getDeveloperPanelState() == StateID._STATE_DISABLED )
                        {
                            App.getDev().setDeveloperPanelState( StateID._STATE_ENABLED );
                            enteredDeveloperPanel = true;

                            showActors( false );

                            App.getAppConfig().backButton.setVisible( false );
                            App.getAppConfig().backButton.setDisabled( true );

                            developerPanel.setup();
                        }
                    }
                } );
            }
        }

        /*
         * Instructions button.
         * Displays the Instructions / Game objectives on
         * screen, for the players reference.
         */
        //        if (buttonAbout != null)
        //        {
        //            buttonAbout.addListener(new ClickListener()
        //            {
        //                public void clicked(InputEvent event, float x, float y)
        //                {
        //                }
        //            });
        //        }
    }

    private void populateTable()
    {
        Trace.checkPoint();

        Scene2DUtils scene2DUtils = new Scene2DUtils();

        // ----------
        musicCheckBox = scene2DUtils.addCheckBox( "toggle_on", "toggle_off", panelPos.x + 800, panelPos.y + 441, Color.WHITE, skin );
        fxCheckBox    = scene2DUtils.addCheckBox( "toggle_on", "toggle_off", panelPos.x + 800, panelPos.y + 390, Color.WHITE, skin );

        // ----------
        if ( App.getAppConfig().currentScreenID == ScreenID._MAIN_MENU )
        {
            buttonAbout = scene2DUtils.addButton
                    (
                            "blank_button",
                            "blank_button_pressed",
                            panelPos.x + 401,
                            panelPos.y + 209
                    );
        }

        // ----------
        buttonDevOptions = scene2DUtils.addButton
                (
                        App.getDev().isDevMode() ? "new_developer_options_button" : "blank_button",
                        App.getDev().isDevMode() ? "new_developer_options_button_pressed" : "blank_button",
                        panelPos.x + 648,
                        panelPos.y + 209
                );

        buttonStats = scene2DUtils.addButton
                (
                        App.getDev().isDevMode() ? "new_stats_button" : "blank_button",
                        App.getDev().isDevMode() ? "new_stats_button_pressed" : "blank_button",
                        panelPos.x + 648,
                        panelPos.y + 162
                );

        showActors( true );
    }

    @Override
    public void dispose()
    {
        Trace.checkPoint();

        App.getAppConfig().backButton.setVisible( false );
        App.getAppConfig().backButton.setDisabled( true );

        if ( buttonStats != null )
        {
            buttonStats.addAction( Actions.removeActor() );
            buttonStats = null;
        }

        if ( buttonAbout != null )
        {
            buttonAbout.addAction( Actions.removeActor() );
            buttonAbout = null;
        }

        if ( buttonDevOptions != null )
        {
            buttonDevOptions.addAction( Actions.removeActor() );
            buttonDevOptions = null;
        }

        musicCheckBox.addAction( Actions.removeActor() );
        musicCheckBox = null;
        fxCheckBox.addAction( Actions.removeActor() );
        fxCheckBox = null;

        versionLabel.addAction( Actions.removeActor() );
        versionLabel = null;

        App.getAssets().unloadAsset( GameAssets._OPTIONS_PANEL_ASSET );
        App.getAssets().unloadAsset( GameAssets._CONTROLLER_TEST_ASSET );

        foreground = null;
        skin       = null;
    }
}
