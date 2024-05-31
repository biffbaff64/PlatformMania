package com.richikin.platformania.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.platformania.config.Settings;
import com.richikin.platformania.core.App;
import com.richikin.platformania.core.GameConstants;
import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.enums.StateID;
import com.richikin.platformania.graphics.GameAssets;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.graphics.camera.OrthoGameCamera;
import com.richikin.platformania.graphics.text.FontUtils;
import com.richikin.platformania.input.Switch;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.maths.SimpleVec2F;
import com.richikin.platformania.physics.Direction;
import com.richikin.platformania.physics.Movement;
import com.richikin.platformania.ui.panels.PanelManager;
import com.richikin.platformania.ui.panels.TextPanel;

import java.util.Locale;

public class HeadsUpDisplay implements Disposable
{
    public static final int _VERY_LARGE_FONT_SIZE = 48;
    public static final int _LARGE_FONT_SIZE      = 35;
    public static final int _MID_FONT_SIZE        = 25;
    public static final int _SMALL_FONT_SIZE      = 18;

    private static final int _X      = 0;
    private static final int _Y      = 1;
    public static final  int _WIDTH  = 2;
    public static final  int _HEIGHT = 3;

    public static final int _JOYSTICK    = 0;
    public static final int _BUTTON_X    = 1;
    public static final int _BUTTON_Y    = 2;
    public static final int _BUTTON_B    = 3;
    public static final int _BUTTON_A    = 4;
    public static final int _PAUSE       = 5;
    public static final int _KEYS        = 6;
    public static final int _LIVES       = 7;
    public static final int _HEALTH      = 8;
    public static final int _VILLAGERS   = 9;
    public static final int _LEVEL       = 10;
    public static final int _COINS       = 11;
    public static final int _GEMS        = 12;
    public static final int _DEV_OPTIONS = 13;
    public static final int _DEV_UP      = 14;
    public static final int _DEV_DOWN    = 15;
    public static final int _DEV_LEFT    = 16;
    public static final int _DEV_RIGHT   = 17;
    public static final int _DEV_A       = 18;
    public static final int _DEV_B       = 19;
    public static final int _DEV_X       = 20;
    public static final int _DEV_Y       = 21;

    //@formatter:off
    public final int[][] displayPos =
    {
        {     25,   25,  240,  240  },             // Joystick
        {    959,   97,   96,   96  },             // X
        {   1060,  186,   96,   96  },             // Y
        {   1157,   97,   96,   96  },             // B (Attack)
        {   1060,   14,   96,   96  },             // A (Action)
        {   1179,  630,   66,   66  },             // Pause Button

        // ----------------------------------------
        // Y is distance from the TOP of the screen
        {    180,   70,    0,    0  },             // Keys total
        {    932,  103,   40,    0  },             // Lives
        {    933,   53,    0,    0  },             // Health bar
        {     64,   52,    0,    0  },             // Villagers
        {   1200,   64,    0,    0  },             // Level
        {    308,   30,    0,    0  },             // Coins
        {    308,   70,    0,    0  },             // Gems

        // ----------------------------------------
        //
        {    911,  717,    0,    0  },             // Developer Options
        {   1008,  621,    0,    0  },             // 'UP'
        {   1008,  644,    0,    0  },             // 'DOWN'
        {   1008,  667,    0,    0  },             // 'LEFT'
        {   1008,  690,    0,    0  },             // 'RIGHT'
        {   1170,  681,    0,    0  },             // 'A'
        {   1170,  635,    0,    0  },             // 'B'
        {   1100,  681,    0,    0  },             // 'X'
        {   1100,  635,    0,    0  },             // 'Y'
    };
    //@formatter:on

    public float      hudOriginX;
    public float      hudOriginY;
    public StateID    hudStateID;
    public Switch     buttonUp;
    public Switch     buttonDown;
    public Switch     buttonLeft;
    public Switch     buttonRight;
    public Switch     buttonPause;
    public Switch     buttonA;
    public Switch     buttonB;
    public Switch     buttonX;
    public Switch     buttonY;
    public IntroPanel introPanel;
    public HudDebug   hudDebug;

    private ImageButton sceneButtonA;
    private ImageButton sceneButtonB;
    private ImageButton sceneButtonX;
    private ImageButton sceneButtonY;

    private Image         scorePanel;
    private TextureRegion devButtonPanel;
    private Image[]       smallMan;
    private BitmapFont    smallFont;
    private BitmapFont    midFont;
    private BitmapFont    bigFont;
    private PanelManager  panelManager;
    private PausePanel    pausePanel;
    private ItemBar       itemBar;
    private int           itemPanelIndex;
    private ProgressBar   healthBar;
    private Message       messageHandler;
    private YesNoDialog   yesNoDialog;
    private TextPanel     textPanel;

    // ----------------------------------------------------------------
    // Code
    // ----------------------------------------------------------------

    public HeadsUpDisplay()
    {
    }

    public void createHud()
    {
        Trace.checkPoint();

        createHudFonts();

        Scene2DUtils scene2DUtils = new Scene2DUtils();
        smallMan = new Image[ GameConstants._MAX_LIVES ];

        for ( int i = 0; i < GameConstants._MAX_LIVES; i++ )
        {
            smallMan[ i ] = scene2DUtils.createImage( GameAssets._SMALL_MAN, App.getAssets().getObjectsLoader() );

            smallMan[ i ].setPosition(
                ( displayPos[ _LIVES ][ _X ] + ( i * displayPos[ _LIVES ][ _WIDTH ] ) ),
                ( Gfx._HUD_HEIGHT - displayPos[ _LIVES ][ _Y ] )
            );

            smallMan[ i ].setVisible( true );

            App.getStage().addActor( smallMan[ i ] );
        }

        Texture               texture  = App.getAssets().loadSingleAsset( GameAssets._HUD_PANEL_ASSET, Texture.class );
        TextureRegionDrawable drawable = new TextureRegionDrawable( texture );
        this.scorePanel = new Image( drawable );

        messageHandler = new Message();
        panelManager   = new PanelManager();
        pausePanel     = new PausePanel( 0, 0 );
        itemBar        = new ItemBar();
        itemPanelIndex = 0;

        textPanel = new TextPanel( 486, 695 );
        textPanel.set
            (
                new SimpleVec2F( 356, 0 ),
                new Direction( Movement._DIRECTION_RIGHT, Movement._DIRECTION_STILL ),
                new SimpleVec2F( 32.0f, 0.0f )
            );

        healthBar = new ProgressBar( 1, 0, GameConstants._MAX_PROGRESSBAR_LENGTH, "bar9", false );
        healthBar.setPosition
            (
                ( int ) hudOriginX + displayPos[ _HEALTH ][ _X ],
                ( int ) hudOriginY + ( Gfx._HUD_HEIGHT - displayPos[ _HEALTH ][ _Y ] )
            );
        healthBar.setHeightColorScale( 19f, Color.GREEN, 2.0f );

        hudDebug = new HudDebug();

        createHUDButtons();

        hudStateID = StateID._STATE_PANEL_START;
    }

    public void update()
    {
        switch ( hudStateID )
        {
            case _STATE_PANEL_START:
            {
                if ( App.getBaseRenderer().getHudGameCamera().getCameraZoom() != 1.0f )
                {
                    App.getBaseRenderer().getHudGameCamera().updateZoom( 1.0f, 0.02f );
                }
                else
                {
                    hudStateID = StateID._STATE_PANEL_UPDATE;
                }
            }
            break;

            case _STATE_PANEL_UPDATE:
            {
                if ( buttonPause.isPressed() )
                {
                    pausePanel = new PausePanel( ( int ) hudOriginX, ( int ) hudOriginY );
                    pausePanel.setup();
                    App.getAppConfig().pause();
                    buttonPause.release();
                }
                else
                {
                    updateLives();
                    updateBars();
                    checkButtons();

                    panelManager.update();
                    textPanel.update();
                }
            }
            break;

            case _STATE_WELCOME_PANEL:
            {
                if ( introPanel.update() )
                {
                    hudStateID = StateID._STATE_PANEL_UPDATE;

                    App.getPlayer().setActionState( ActionStates._STANDING );

                    introPanel.dispose();

                    App.getSettings().disable( Settings._INTRO_PANEL );
                }
            }
            break;

            // TODO: 18/11/2021
            // Update the developer options panel here
            case _STATE_DEVELOPER_PANEL:
            {
            }
            break;

            case _STATE_PAUSED:
            {
                if ( pausePanel != null )
                {
                    pausePanel.update();

                    if ( pausePanel.getQuitButton().isChecked() )
                    {
                        yesNoDialog = new YesNoDialog();
                        yesNoDialog.createDialog( "ARE YOU SURE" );

                        hudStateID = StateID._STATE_YES_NO_DIALOG;

                        pausePanel.getQuitButton().setChecked( false );
                    }

                    if ( buttonPause.isPressed() || pausePanel.getExitButton().isPressed() )
                    {
                        removePausePanel();

                        App.getAppState().set( StateID._STATE_GAME );
                        hudStateID = StateID._STATE_PANEL_UPDATE;
                    }
                }
            }
            break;

            case _STATE_YES_NO_DIALOG:
            {
                if ( yesNoDialog.hasJustLeftDialog() )
                {
                    if ( yesNoDialog.getDialogResult() )
                    {
                        App.getAppConfig().forceQuitToMenu = true;
                    }

                    hudStateID = StateID._STATE_PAUSED;

                    yesNoDialog.dispose();
                    yesNoDialog = null;
                }
            }
            break;

            default:
            {
                Trace.err( "Unsupported HUD State: ", hudStateID );
            }
            break;
        }
    }

    private void updateLives()
    {
        if ( App.getPlayer() != null )
        {
            for ( int i = 0; i < GameConstants._MAX_LIVES; i++ )
            {
                smallMan[ i ].setVisible( i < App.getGameProgress().lives.getTotal() );
            }
        }
    }

    private void updateBars()
    {
        if ( App.getPlayer() != null )
        {
            healthBar.setTotal( App.getPlayer().strength );

            if ( healthBar.getTotal() < ( float ) ( GameConstants._MAX_PROGRESSBAR_LENGTH / 3 ) )
            {
                if ( healthBar.getTotal() < ( float ) ( GameConstants._MAX_PROGRESSBAR_LENGTH / 8 ) )
                {
                    healthBar.setColor( Color.RED );
                }
                else
                {
                    healthBar.setColor( Color.ORANGE );
                }
            }
            else
            {
                healthBar.setColor( Color.GREEN );
            }
        }
    }

    /**
     * Check for any presses on the ABXY buttons.
     * Only valid for ControllerType._VIRTUAL mode.
     */
    private void checkButtons()
    {
        if ( App.getAppConfig().isUsingOnScreenControls() )
        {
            if ( sceneButtonA.isChecked() )
            {
                buttonA.press();
                sceneButtonA.setChecked( false );
            }

            if ( sceneButtonB.isChecked() )
            {
                buttonB.press();
                sceneButtonB.setChecked( false );
            }

            if ( sceneButtonX.isChecked() )
            {
                buttonX.press();
                sceneButtonX.setChecked( false );
            }

            // 'Y' button is used for swapping between the item panels
            if ( sceneButtonY.isChecked() )
            {
                buttonY.press();
                sceneButtonY.setChecked( false );
            }
        }
    }

    public void render( OrthoGameCamera camera, boolean canDrawControls )
    {
        hudOriginX = camera.getPosition().x - ( Gfx._HUD_WIDTH / 2f );
        hudOriginY = camera.getPosition().y - ( Gfx._HUD_HEIGHT / 2f );

        drawPanels();
        drawItems();
        drawMessages();

        if ( ( pausePanel != null ) && ( hudStateID == StateID._STATE_PAUSED ) )
        {
            pausePanel.draw( App.getSpriteBatch() );
        }

        if ( ( introPanel != null ) && ( hudStateID == StateID._STATE_WELCOME_PANEL ) )
        {
            introPanel.draw( App.getSpriteBatch() );
        }

        hudDebug.drawHudDebug( hudOriginX, hudOriginY );
    }

    public void refillItems()
    {
        // The player starts each level with full strength, there's
        // no sense in starting a new level with 1% strength!
        App.getPlayer().strength = GameConstants._MAX_STRENGTH;
        healthBar.refill();
    }

    //@formatter:off
    public ProgressBar          getHealthBar()                  {   return healthBar;           }
    public PanelManager         getPanelManager()               {   return panelManager;        }
    public Message              getMessageHandler()             {   return messageHandler;      }
    public int                  getItemPanelIndex()             {   return itemPanelIndex;      }
    public TextPanel            getTextPanel()                  {   return textPanel;           }
    public ItemBar              getItemBar()                    {   return itemBar;             }
    public void                 setItemPanelIndex( int index )  {   itemPanelIndex = index;     }
    public void                 setStateID( StateID state )     {   hudStateID = state;         }
    //@formatter:on

    private void drawPanels()
    {
        textPanel.draw( App.getSpriteBatch() );

        scorePanel.setPosition( hudOriginX, hudOriginY + ( Gfx._HUD_HEIGHT - scorePanel.getHeight() ) );
        scorePanel.draw( App.getSpriteBatch(), 1.0f );

        itemBar.draw( hudOriginX, hudOriginY );
    }

    private void drawItems()
    {
        healthBar.draw( App.getSpriteBatch() );

        midFont.setColor( Color.YELLOW );
        bigFont.setColor( Color.YELLOW );

        // The number of keys collected
        midFont.draw
            (
                App.getSpriteBatch(),
                String.format( Locale.UK, "%d", App.getGameProgress().keyCount.getTotal() ),
                hudOriginX + displayPos[ _KEYS ][ _X ],
                hudOriginY + ( Gfx._HUD_HEIGHT - displayPos[ _KEYS ][ _Y ] )
            );

        // The number of villagers rescued so far
        int offset = App.getGameProgress().rescueCount.getTotal() < 10 ? 10 : 4;

        bigFont.draw
            (
                App.getSpriteBatch(),
                String.format( Locale.UK, "%d", App.getGameProgress().rescueCount.getTotal() ),
                hudOriginX + displayPos[ _VILLAGERS ][ _X ] + offset,
                hudOriginY + ( Gfx._HUD_HEIGHT - displayPos[ _VILLAGERS ][ _Y ] )
            );

        // The current game level
        offset = App.getGameProgress().gameLevel < 10 ? 10 : 4;

        bigFont.draw
            (
                App.getSpriteBatch(),
                String.format( Locale.UK, "%d", App.getGameProgress().gameLevel ),
                hudOriginX + displayPos[ _LEVEL ][ _X ] + offset,
                hudOriginY + ( Gfx._HUD_HEIGHT - displayPos[ _LEVEL ][ _Y ] )
            );

        midFont.draw
            (
                App.getSpriteBatch(),
                String.format( Locale.UK, "%d", App.getGameProgress().gemCount.getTotal() ),
                hudOriginX + displayPos[ _GEMS ][ _X ] + offset,
                hudOriginY + ( Gfx._HUD_HEIGHT - displayPos[ _GEMS ][ _Y ] )
            );

        midFont.draw
            (
                App.getSpriteBatch(),
                String.format( Locale.UK, "%d", App.getGameProgress().coinCount.getTotal() ),
                hudOriginX + displayPos[ _COINS ][ _X ] + offset,
                hudOriginY + ( Gfx._HUD_HEIGHT - displayPos[ _COINS ][ _Y ] )
            );
    }

    /**
     * Draws the in-game 'message panels' that pop up.
     * 'Get Ready', "Game Over' etc.
     */
    private void drawMessages()
    {
        if ( !App.getAppConfig().gamePaused )
        {
            if ( panelManager.isEnabled() )
            {
                panelManager.draw();
            }

            messageHandler.draw();
        }
    }

    public void showControls( boolean visible )
    {
        if ( App.getAppConfig().isUsingOnScreenControls() )
        {
            if ( App.getInputManager().getVirtualJoystick() != null )
            {
                if ( visible )
                {
                    App.getInputManager().getVirtualJoystick().show();
                }
                else
                {
                    App.getInputManager().getVirtualJoystick().hide();
                }
            }
        }
    }

    /**
     * Show or Hide the onscreen pause button.
     * Only valid for ControllerType._VIRTUAL mode.
     */
    @SuppressWarnings("StatementWithEmptyBody")
    public void showPauseButton( boolean visibility )
    {
        if ( App.getAppConfig().isUsingOnScreenControls() )
        {
            // TODO: 22/02/2022
        }
    }

    public void enableHUDButtons()
    {
        buttonA.setDisabled( false );
        buttonB.setDisabled( false );
        buttonX.setDisabled( false );
        buttonY.setDisabled( false );
        buttonLeft.setDisabled( false );
        buttonRight.setDisabled( false );
        buttonUp.setDisabled( false );
        buttonDown.setDisabled( false );

        releaseABXYButtons();
        releaseDirectionButtons();
    }

    public void releaseDirectionButtons()
    {
        buttonLeft.release();
        buttonRight.release();
        buttonUp.release();
        buttonDown.release();
    }

    public void releaseABXYButtons()
    {
        buttonA.release();
        buttonB.release();
        buttonX.release();
        buttonY.release();
    }

    private void removePausePanel()
    {
        if ( pausePanel != null )
        {
            pausePanel.dispose();
            pausePanel = null;
        }

        App.getAppConfig().unPause();

        buttonPause.release();
    }

    private void createHudFonts()
    {
        FontUtils fontUtils = new FontUtils();

        bigFont   = fontUtils.createFont( GameAssets._ACME_FONT, _VERY_LARGE_FONT_SIZE );
        midFont   = fontUtils.createFont( GameAssets._ACME_FONT, _LARGE_FONT_SIZE );
        smallFont = fontUtils.createFont( GameAssets._ACME_FONT, _SMALL_FONT_SIZE );
    }

    /**
     * Creates any buttons used for the HUD.
     * HUD buttons are just switches so that they can be set/unset
     * by keyboard OR on-screen virtual buttons.
     */
    private void createHUDButtons()
    {
        buttonLeft  = new Switch();
        buttonRight = new Switch();
        buttonUp    = new Switch();
        buttonDown  = new Switch();
        buttonPause = new Switch();

        if ( App.getAppConfig().isUsingOnScreenControls() )
        {
            Scene2DUtils utils = new Scene2DUtils();

            sceneButtonA = utils.addButton
                (
                    "button_a",
                    "button_a_pressed",
                    displayPos[ _BUTTON_A ][ _X ],
                    displayPos[ _BUTTON_A ][ _Y ]
                );

            sceneButtonB = utils.addButton
                (
                    "button_b",
                    "button_b_pressed",
                    displayPos[ _BUTTON_B ][ _X ],
                    displayPos[ _BUTTON_B ][ _Y ]
                );

            sceneButtonX = utils.addButton
                (
                    "button_x",
                    "button_x_pressed",
                    displayPos[ _BUTTON_X ][ _X ],
                    displayPos[ _BUTTON_X ][ _Y ]
                );

            sceneButtonY = utils.addButton
                (
                    "button_y",
                    "button_y_pressed",
                    displayPos[ _BUTTON_Y ][ _X ],
                    displayPos[ _BUTTON_Y ][ _Y ]
                );
        }

        buttonA = new Switch();
        buttonB = new Switch();
        buttonX = new Switch();
        buttonY = new Switch();
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose()
    {
        hudStateID  = null;
        hudDebug    = null;
        buttonUp    = null;
        buttonDown  = null;
        buttonLeft  = null;
        buttonRight = null;
        buttonPause = null;
        buttonA     = null;
        buttonB     = null;
        buttonX     = null;
        buttonY     = null;

        sceneButtonA = null;
        sceneButtonB = null;
        sceneButtonX = null;
        sceneButtonY = null;

        App.getAssets().unloadAsset( GameAssets._HUD_PANEL_ASSET );
        scorePanel = null;

        for ( int i = 0; i < GameConstants._MAX_LIVES; i++ )
        {
            smallMan[ i ].addAction( Actions.removeActor() );
            smallMan[ i ] = null;
        }

        smallMan  = null;
        smallFont = null;
        midFont   = null;
        bigFont   = null;

        if ( pausePanel != null )
        {
            pausePanel.dispose();
        }

        pausePanel   = null;
        panelManager = null;

        itemBar.dispose();
        itemBar = null;

        healthBar.dispose();
        healthBar = null;
    }
}
