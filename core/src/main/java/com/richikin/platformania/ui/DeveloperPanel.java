package com.richikin.platformania.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.richikin.platformania.core.App;
import com.richikin.platformania.enums.StateID;
import com.richikin.platformania.config.GdxSystem;
import com.richikin.platformania.config.Settings;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.graphics.text.FontUtils;
import com.richikin.platformania.logging.NotImplementedException;
import com.richikin.platformania.logging.Stats;
import com.richikin.platformania.logging.Trace;

import java.util.Locale;

public class DeveloperPanel extends DefaultPanel
{
    public static final int _TABLE_COLUMNS = 3;

    public DMEntry[][] devMenu;

    // ------------------------------------------------------------------------

    private final        int originX;
    private final        int originY;

    private int          disableEnemiesColumn;
    private int          disableEnemiesRow;
    private int          glProfilerRow;
    private int          glProfilerColumn;
    private Texture      foreground;
    private CheckBox[][] buttons;
    private TextField    heading;
    private GLProfiler   glProfiler;
    private TextButton   exitButton;
    private TextButton   buttonResetPrefs;
    private TextButton   buttonResetHiScores;
    private TextButton   buttonResetStats;
    private TextButton   buttonGLProfiler;
    private TextButton   buttonCollisionDump;
    private boolean      previousDisableEnemies;
    private boolean      previousExternalController;
    private boolean      okToResetPrefs;

    // ------------------------------------------------------------------------

    public static final boolean[] columnEnabled =
            {
                    true,
                    false,
                    false,
            };

    // ------------------------------------------------------------
    // Code
    // ------------------------------------------------------------

    public DeveloperPanel()
    {
        this( 0, 0 );
    }

    public DeveloperPanel( int x, int y )
    {
        super();

        nameID  = "Developer Panel";
        originX = x;
        originY = y;

        if ( App.getDev().isDevMode() )
        {
            glProfiler = new GLProfiler( Gdx.graphics );
        }
    }

    @Override
    public void setup()
    {
        Trace.checkPoint();

        App.getAppConfig().gamePaused = true;

        validateRowsAndColumns();

        foreground = App.getAssets().loadSingleAsset( App.getAssets().getDevPanelBackground(), Texture.class );

        okToResetPrefs = false;

        String skinFilename = App.getAssets().getSkinFilename();

        if ( skinFilename.isEmpty() )
        {
            skin = new Skin();
        }
        else
        {
            skin = new Skin( Gdx.files.internal( App.getAssets().getSkinFilename() ) );
        }

        table = createTable();

        createHeading( skin );
        createButtons( skin );

        populateTable( table, skin );

        // Wrap the table in a scrollpane.
        scrollPane = new ScrollPane( table, skin );
        scrollPane.setScrollingDisabled( false, false );
        scrollPane.setFadeScrollBars( false );
        scrollPane.setWidth( ( float ) ( Gfx._HUD_WIDTH - 100 ) );
        scrollPane.setHeight( ( float ) ( Gfx._HUD_HEIGHT - 260 ) );
        scrollPane.setPosition( originX + 50, originY + 120 );
        scrollPane.setScrollbarsOnTop( true );

        App.getStage().addActor( scrollPane );
        App.getStage().addActor( heading );
        App.getStage().addActor( exitButton );
        App.getStage().addActor( buttonResetPrefs );
        App.getStage().addActor( buttonResetHiScores );
        App.getStage().addActor( buttonResetStats );
        App.getStage().addActor( buttonGLProfiler );
        App.getStage().addActor( buttonCollisionDump );

        updatePreferencesOnEntry();

        previousDisableEnemies = buttons[ disableEnemiesRow ][ disableEnemiesColumn ].isChecked();

        if ( App.getDev().isDevMode() )
        {
            glProfilerUpdate();
        }
    }

    private Table createTable()
    {
        Trace.checkPoint();

        Table table = new Table();
        table.top().left();
        table.pad( 60, 10, 10, 10 );

        Texture texture    = App.getAssets().loadSingleAsset( App.getAssets().getDevPanelBackground(), Texture.class );
        Image   background = new Image( new TextureRegion( texture ) );
        table.setBackground( background.getDrawable() );

        return table;
    }

    private void createHeading( Skin _skin )
    {
        Trace.checkPoint();

        heading = new TextField( "DEVELOPER OPTIONS", _skin );
        heading.setSize( 400, 96 );
        heading.setDisabled( true );
        heading.setPosition
                (
                        originX + ( Gfx._HUD_WIDTH / 6f ),
                        originY + ( Gfx._HUD_HEIGHT - 80 ),
                        Align.left
                );

        FontUtils                fontUtils = new FontUtils();
        BitmapFont               font      = fontUtils.createFont( App.getAssets().getDevPanelFont(), 32, Color.WHITE );
        TextField.TextFieldStyle style     = heading.getStyle();
        style.font = font;

        heading.setStyle( style );
    }

    private void createButtons( Skin _skin )
    {
        Trace.checkPoint();

        exitButton          = new TextButton( "Back", _skin );
        buttonResetPrefs    = new TextButton( "Reset Settings To Default", _skin );
        buttonResetHiScores = new TextButton( "Reset HiScore Table", _skin );
        buttonResetStats    = new TextButton( "Reset Stats Meters", _skin );
        buttonGLProfiler    = new TextButton( "GLProfiler Dump", _skin );
        buttonCollisionDump = new TextButton( "CollisionObject Breakdown", _skin );

        int x = 20;

        buttonResetPrefs.setPosition( originX + x, originY + 15 );

        x += ( int ) ( buttonResetPrefs.getWidth() + 20 );

        buttonResetHiScores.setPosition( originX + x, originY + 15 );

        x += ( int ) ( buttonResetHiScores.getWidth() + 20 );

        buttonResetStats.setPosition( originX + x, originY + 15 );

        x += ( int ) ( buttonResetStats.getWidth() + 20 );

        buttonGLProfiler.setPosition( originX + x, originY + 15 );

        x += ( int ) ( buttonGLProfiler.getWidth() + 20 );

        buttonCollisionDump.setPosition( originX + x, originY + 15 );

        exitButton.setPosition( originX + 20, originY + ( Gfx._HUD_HEIGHT - 100 ) );
        exitButton.setSize( 40, 40 );

        createButtonListeners();
    }

    private void populateTable( Table _table, Skin _skin )
    {
        Trace.checkPoint();

        Label[] label = new Label[ _TABLE_COLUMNS ];

        buttons = new CheckBox[ devMenu.length ][ _TABLE_COLUMNS ];

        FontUtils fontUtils = new FontUtils();

        for ( int row = 0; row < devMenu.length; row++ )
        {
            for ( int column = 0; columnEnabled[ column ] && column < _TABLE_COLUMNS; column++ )
            {
                label[ column ] = new Label
                        (
                                ( String.format( Locale.getDefault(), "%-30s", devMenu[ row ][ column ].string ) ).toUpperCase(),
                                _skin
                        );

                Label.LabelStyle labelStyle = label[ column ].getStyle();
                labelStyle.font = fontUtils.createFont( App.getAssets().getDevPanelFont(), 18, Color.WHITE );

                label[ column ].setStyle( labelStyle );
                label[ column ].setAlignment( Align.left );

                buttons[ row ][ column ] = new CheckBox( "", _skin );
                buttons[ row ][ column ].setHeight( label[ column ].getHeight() );

                CheckBox.CheckBoxStyle style = buttons[ row ][ column ].getStyle();
                style.checkboxOn  = new TextureRegionDrawable( App.getAssets().getButtonRegion( "toggle_on" ) );
                style.checkboxOff = new TextureRegionDrawable( App.getAssets().getButtonRegion( "toggle_off" ) );

                buttons[ row ][ column ].setStyle( style );

                if ( "".equals( devMenu[ row ][ column ].string ) )
                {
                    buttons[ row ][ column ].setChecked( false );
                }
                else
                {
                    buttons[ row ][ column ].setChecked( App.getSettings().isEnabled( devMenu[ row ][ column ].prefName ) );
                }
            }

            createCheckBoxListener( row );

            for ( int column = 0; columnEnabled[ column ] && column < _TABLE_COLUMNS; column++ )
            {
                Label num = new Label( row + ": ", _skin );
                _table.add( num ).padLeft( 20 );
                _table.add( label[ column ] );
                _table.add( buttons[ row ][ column ] );
            }

            _table.row();
        }

        _table.setVisible( true );
    }

    private void createButtonListeners()
    {
        exitButton.addListener( new ClickListener()
        {
            public void clicked( InputEvent event, float x, float y )
            {
                clearUp();
            }
        } );

        buttonResetPrefs.addListener( new ClickListener()
        {
            public void clicked( InputEvent event, float x, float y )
            {
                resetPreferencesToDefaults();
            }
        } );

        buttonResetStats.addListener( new ClickListener()
        {
            public void clicked( InputEvent event, float x, float y )
            {
                Stats.resetAllMeters();
            }
        } );

        buttonResetHiScores.addListener( new ClickListener()
        {
            public void clicked( InputEvent event, float x, float y )
            {
                if ( App.getDev().isDevMode() )
                {
                    App.getHighScoreUtils().resetTable();

                    Trace.dbg( "HISCORE Table reset to defaults." );
                }
            }
        } );

        buttonGLProfiler.addListener( new ClickListener()
        {
            public void clicked( InputEvent event, float x, float y )
            {
                if ( App.getDev().isDevMode() )
                {
                    Trace.dbg
                            (
                                    "  Drawcalls: " + glProfiler.getDrawCalls()
                                            + ", Calls: " + glProfiler.getCalls()
                                            + ", TextureBindings: " + glProfiler.getTextureBindings()
                                            + ", ShaderSwitches:  " + glProfiler.getShaderSwitches()
                                            + "vertexCount: " + glProfiler.getVertexCount().value
                            );

                    glProfiler.reset();
                }
            }
        } );

        buttonCollisionDump.addListener( new ClickListener()
        {
            @Override
            public void clicked( final InputEvent event, final float x, final float y )
            {
                if ( App.getDev().isDevMode() )
                {
                    throw new NotImplementedException( "DebugALL from collisionUtils..." );
//                    app.collisionUtils.debugAll();
                }
            }
        } );
    }

    private void createCheckBoxListener( int index )
    {
        for ( int column = 0; columnEnabled[ column ] && column < _TABLE_COLUMNS; column++ )
        {
            buttons[ index ][ column ].addListener( new ChangeListener()
            {
                @Override
                public void changed( ChangeEvent event, Actor actor )
                {
                    if ( !okToResetPrefs )
                    {
                        updatePreferences();
                    }
                }
            } );
        }
    }

    @Override
    public void draw( SpriteBatch spriteBatch )
    {
        if ( foreground != null )
        {
            spriteBatch.draw( foreground, 0, 0, Gfx._HUD_WIDTH, Gfx._HUD_HEIGHT );
        }
    }

    private void updatePreferencesOnEntry()
    {
        Trace.checkPoint();

        if ( !App.getDev().isDevMode() )
        {
            App.getSettings().disable( Settings._MENU_HEAPS );
        }

        updatePreferences();
    }

    private void updatePreferences()
    {
        if ( buttons[ disableEnemiesRow ][ disableEnemiesColumn ].isChecked() != previousDisableEnemies )
        {
            setAllEnemiesEnableStatus( buttons[ disableEnemiesRow ][ disableEnemiesColumn ].isChecked() );
        }

        for ( int row = 0; row < devMenu.length; row++ )
        {
            for ( int column = 0; columnEnabled[ column ] && column < _TABLE_COLUMNS; column++ )
            {
                App.getSettings().getPrefs().putBoolean( devMenu[ row ][ column ].prefName, buttons[ row ][ column ].isChecked() );
            }
        }

        App.getSettings().getPrefs().flush();

        glProfilerUpdate();

        previousDisableEnemies = buttons[ disableEnemiesRow ][ disableEnemiesColumn ].isChecked();
    }

    private void updatePreferencesOnExit()
    {
        Trace.checkPoint();
    }

    private void glProfilerUpdate()
    {
        if ( App.getDev().isDevMode() )
        {
            if ( buttons[ glProfilerRow ][ glProfilerColumn ].isChecked() )
            {
                // Profiling should be disabled on release software, hence
                // the warning suppression. Normally, not a good idea to
                // suppress such warnings but this if..else... is only
                // executed in Developer Mode.

                //noinspection LibGDXProfilingCode
                glProfiler.enable();
            }
            else
            {
                glProfiler.disable();
            }
        }
    }

    /**
     * Either ENABLE or DISABLE all enemy entities.
     *
     * @param _disable boolean TRUE to disable
     *                 boolean FALSE to enable.
     */
    private void setAllEnemiesEnableStatus( boolean _disable )
    {
        for ( int row = 0; row < devMenu.length; row++ )
        {
            for ( int column = 0; columnEnabled[ column ] && column < _TABLE_COLUMNS; column++ )
            {
                if ( devMenu[ row ][ column ].isEnemy )
                {
                    buttons[ row ][ column ].setChecked( !_disable );
                }
            }
        }
    }

    private void resetPreferencesToDefaults()
    {
        okToResetPrefs = true;

        App.getSettings().resetToDefaults();

//        App.getSettings().getPrefs().putBoolean(Settings._DEV_MODE, App.appConfig.isDevMode());
//        App.getSettings().getPrefs().putBoolean(Settings._GOD_MODE, App.appConfig.isGodMode());
        App.getSettings().getPrefs().putBoolean( Settings._SIGN_IN_STATUS, App.getPlayServices().isSignedIn() );

        App.getSettings().getPrefs().flush();

        for ( int row = 0; row < devMenu.length; row++ )
        {
            for ( int column = 0; columnEnabled[ column ] && column < _TABLE_COLUMNS; column++ )
            {
                boolean isChecked = App.getSettings().isEnabled( devMenu[ row ][ column ].prefName );

                buttons[ row ][ column ].setChecked( isChecked );
            }
        }

        okToResetPrefs = false;
    }

    private void validateRowsAndColumns()
    {
        Trace.checkPoint();

        for ( int row = 0; row < devMenu.length; row++ )
        {
            int length = devMenu[ row ].length;

            for ( int column = 0; columnEnabled[ column ] && column < length; column++ )
            {
                String prefName = devMenu[ row ][ column ].prefName;

                switch ( prefName )
                {
                    case Settings._DISABLE_ENEMIES:
                    {
                        disableEnemiesColumn = column;
                        disableEnemiesRow    = row;
                    }
                    break;

                    case Settings._GL_PROFILER:
                    {
                        glProfilerColumn = column;
                        glProfilerRow    = row;
                    }
                    break;

                    default:
                    {
                    }
                    break;
                }
            }
        }
    }

    public void debugReport()
    {
        Trace.checkPoint();

        for ( DMEntry[] entry : devMenu )
        {
            for ( DMEntry dmEntry : entry )
            {
                if ( !dmEntry.string.isEmpty() )
                {
                    Trace.dbg( dmEntry.string + ": " + App.getSettings().isEnabled( dmEntry.prefName ) );
                }
            }
        }
    }

    private void clearActors()
    {
        Trace.checkPoint();

        for ( int row = 0; row < devMenu.length; row++ )
        {
            for ( int column = 0; column < _TABLE_COLUMNS; column++ )
            {
                if ( columnEnabled[ column ] )
                {
                    buttons[ row ][ column ].addAction( Actions.removeActor() );
                }
            }
        }

        exitButton.addAction( Actions.removeActor() );
        buttonResetHiScores.addAction( Actions.removeActor() );
        buttonResetPrefs.addAction( Actions.removeActor() );
        buttonGLProfiler.addAction( Actions.removeActor() );
        buttonResetStats.addAction( Actions.removeActor() );
        buttonCollisionDump.addAction( Actions.removeActor() );
        heading.addAction( Actions.removeActor() );

        table.addAction( Actions.removeActor() );
        scrollPane.addAction( Actions.removeActor() );
    }

    private void clearUp()
    {
        Trace.checkPoint();

        updatePreferencesOnExit();

        App.getDev().setDeveloperPanelState( StateID._STATE_DISABLED );
        App.getAppConfig().gamePaused = false;

        clearActors();
    }

    @Override
    public void dispose()
    {
        super.dispose();

        foreground.dispose();
        exitButton.clear();
        buttonResetPrefs.clear();
        buttonResetHiScores.clear();
        buttonResetStats.clear();
        buttonGLProfiler.clear();
        heading.clear();
        table.clear();
        scrollPane.clear();

        foreground          = null;
        exitButton          = null;
        buttonResetPrefs    = null;
        buttonResetHiScores = null;
        buttonResetStats    = null;
        buttonGLProfiler    = null;
        heading             = null;
        table               = null;
        scrollPane          = null;
    }

    public static class DMEntry
    {
        final String  string;
        final String  prefName;
        final boolean isEnemy;

        public DMEntry( String _string, String pref, boolean _isEnemy )
        {
            this.string   = _string;
            this.prefName = pref;
            this.isEnemy  = _isEnemy;
        }
    }
}
