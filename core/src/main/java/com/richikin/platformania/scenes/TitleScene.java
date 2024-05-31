package com.richikin.platformania.scenes;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.platformania.audio.GameAudio;
import com.richikin.platformania.enums.CamID;
import com.richikin.platformania.enums.ScreenID;
import com.richikin.platformania.enums.StateID;
import com.richikin.platformania.config.GdxSystem;
import com.richikin.platformania.graphics.camera.OrthoGameCamera;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.core.App;
import com.richikin.platformania.ui.UIPage;
import com.richikin.platformania.ui.YesNoDialog;

import java.util.ArrayList;
import java.util.List;

public class TitleScene extends AbstractBaseScene
{
    private static final int _MENU_PAGE    = 0;
    private static final int _OPTIONS_PAGE = 1;
    private static final int _CREDITS_PAGE = 2;
    private static final int _EXIT_PAGE    = 3;

    private Texture      foreground;
    private Texture      background;
    private OptionsPage  optionsPage;
    private MenuPage     menuPage;
    private List<UIPage> panels;
    private int          currentPage;
    private YesNoDialog  exitDialog;

    public TitleScene()
    {
        super();
    }

    @Override
    public void initialise()
    {
        optionsPage = new OptionsPage();
        menuPage    = new MenuPage();
        panels      = new ArrayList<>();
        currentPage = _MENU_PAGE;

        panels.add( _MENU_PAGE, menuPage );
        panels.add( _OPTIONS_PAGE, optionsPage );
        panels.add( _CREDITS_PAGE, new CreditsPage() );

        menuPage.initialise();
        menuPage.show();
    }

    @Override
    public void update()
    {
        if ( !App.getAudio().isTunePlaying( GameAudio.MUS_TITLE ) )
        {
            App.getAudio().playTitleTune( true );
        }

        if ( App.getAppState().peek() == StateID._STATE_MAIN_MENU )
        {
            switch ( currentPage )
            {
                case _CREDITS_PAGE:
                    panels.get( currentPage ).update();
                    if ( App.getAppConfig().backButton != null )
                    {
                        if ( App.getAppConfig().backButton.isChecked() )
                        {
                            App.getAppConfig().backButton.setChecked( false );
                            changePageTo( _MENU_PAGE );
                        }
                    }
                    break;
                case _MENU_PAGE:
                    panels.get( _MENU_PAGE ).update();
                    break;
                case _OPTIONS_PAGE:
                    if ( panels.get( _OPTIONS_PAGE ).update() )
                    {
                        if ( App.getAppConfig().backButton != null )
                        {
                            App.getAppConfig().backButton.setChecked( false );
                        }

                        changePageTo( _MENU_PAGE );
                    }
                    break;
                case _EXIT_PAGE:
                    if ( App.getAppConfig().shutDownActive )
                    {
                        App.deleteEssentialObjects();
                        App.getAppConfig().exit();
                    }
                    else
                    {
                        if ( exitDialog.hasJustLeftDialog() )
                        {
                            changePageTo( _MENU_PAGE );
                        }
                    }
                    break;
                default:
                    Trace.dbg( "ERROR: Unknown page - " + currentPage );
                    break;
            }

            //
            // Start button check
            if ( ( menuPage.buttonStart != null ) && menuPage.buttonStart.isChecked() )
            {
                Trace.divider( '#', 100 );
                Trace.dbg( " ***** START PRESSED ***** " );
                Trace.divider( '#', 100 );

                App.getAudio().playTitleTune( false );

                menuPage.buttonStart.setChecked( false );

                if ( App.getMainScene() == null )
                {
                    App.setMainScene( new MainScene() );
                }

                App.createMainSceneObjects();
                App.getMainScene().reset();
                App.getMainGame().setScreen( App.getMainScene() );
            }
            else
            {
                if ( App.getAppState() != null )
                {
                    // If we're still on the title screen...
                    if ( App.getAppState().peek() == StateID._STATE_MAIN_MENU )
                    {
                        //
                        // Check OPTIONS button, open settings page if pressed
                        if ( ( menuPage.buttonOptions != null ) && menuPage.buttonOptions.isChecked() )
                        {
                            menuPage.buttonOptions.setChecked( false );
                            changePageTo( _OPTIONS_PAGE );
                        }

                        //
                        // Check CREDITS button, open credits page if pressed
                        if ( ( menuPage.buttonCredits != null ) && menuPage.buttonCredits.isChecked() )
                        {
                            menuPage.buttonCredits.setChecked( false );
                            changePageTo( _CREDITS_PAGE );
                        }

                        //
                        // Check EXIT button, open exit dialog if pressed
                        if ( ( menuPage.buttonExit != null ) && menuPage.buttonExit.isChecked() )
                        {
                            menuPage.buttonExit.setChecked( false );
                            panels.get( currentPage ).hide();

                            changePageTo( _EXIT_PAGE );

                            exitDialog = new YesNoDialog();
                            exitDialog.createDialog( "QUIT GAME" );
                        }
                    }
                }
            }
        }
        else
        {
            Trace.dbg( "Unsupported game state: " + App.getAppState().peek() );
        }
    }

    /**
     * Closes down the current page, and switches to a new one.
     * Copy of LibGdx setScreen() method.
     *
     * @param nextPage The ID of the next page.
     */
    private void changePageTo( int nextPage )
    {
        Trace.checkPoint();
        Trace.info( "currentPage: ", currentPage );
        Trace.info( "nextPage: ", nextPage );

        if ( currentPage == _EXIT_PAGE )
        {
            exitDialog.dispose();
            exitDialog = null;
        }

        if ( currentPage != _EXIT_PAGE )
        {
            if ( panels.get( currentPage ) != null )
            {
                panels.get( currentPage ).hide();
                panels.get( currentPage ).dispose();
            }
        }

        currentPage = nextPage;

        if ( currentPage != _EXIT_PAGE )
        {
            if ( panels.get( currentPage ) != null )
            {
                panels.get( currentPage ).initialise();
                panels.get( currentPage ).show();
            }
        }
    }

    @Override
    public void render( float delta )
    {
        super.update();

        if ( App.getAppState().peek() == StateID._STATE_MAIN_MENU )
        {
            update();

            super.render( delta );
        }
    }

    public void draw( final SpriteBatch spriteBatch, OrthoGameCamera camera )
    {
        if ( App.getAppState().peek() == StateID._STATE_MAIN_MENU )
        {
            spriteBatch.draw( background, 0, 0 );

            switch ( currentPage )
            {
                case _MENU_PAGE:
                    spriteBatch.draw( foreground, 0, 0 );
                    panels.get( currentPage ).draw( spriteBatch );
                    break;
                case _EXIT_PAGE:
                    spriteBatch.draw( foreground, 0, 0 );
                    break;
                case _OPTIONS_PAGE:
                case _CREDITS_PAGE:
                    panels.get( currentPage ).draw( spriteBatch );
                    if ( App.getAppConfig().backButton != null )
                    {
                        if ( App.getAppConfig().backButton.isVisible() )
                        {
                            App.getAppConfig().backButton.setPosition( 20, 20 );
                        }
                    }
                    break;
                default:
                    Trace.err( "Unknown panel: " + currentPage );
                    break;
            }
        }
    }

    @Override
    public void show()
    {
        Trace.checkPoint();

        if ( App.getAppConfig().currentScreenID == ScreenID._GAME_SCREEN )
        {
            //
            // If moving to the TitleScene from MainScene, then all objects
            // that are unnecessary at this point must be destroyed.
            App.deleteMainsceneObjects();
        }

        App.getAppConfig().currentScreenID = ScreenID._MAIN_MENU;
        App.getAppState().set( StateID._STATE_MAIN_MENU );

        super.show();

        App.getBaseRenderer().resetCameraZoom();
        App.getBaseRenderer().enableCamera( CamID._PARALLAX, CamID._HUD, CamID._STAGE );

        initialise();
    }

    @Override
    public void hide()
    {
        Trace.checkPoint();

        super.hide();

        dispose();
    }

    @Override
    public void loadImages()
    {
        background = App.getAssets().loadSingleAsset( "title_background.png", Texture.class );
        foreground = App.getAssets().loadSingleAsset( "title_foreground.png", Texture.class );
    }

    @Override
    public void dispose()
    {
        Trace.checkPoint();

        menuPage.hide();
        menuPage.dispose();

        if ( panels != null )
        {
            panels.clear();
            panels = null;
        }

        optionsPage = null;
        menuPage    = null;
        exitDialog  = null;

        foreground = null;
        background = null;
    }
}
