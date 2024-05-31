package com.richikin.platformania.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.richikin.platformania.config.Settings;
import com.richikin.platformania.core.App;
import com.richikin.platformania.graphics.GameAssets;
import com.richikin.platformania.graphics.Gfx;

public class PausePanel extends DefaultPanel
{
    private static final int _MUSIC          = 0;
    private static final int _SOUNDS         = 1;
    private static final int _NUM_CHECKBOXES = 2;

    private final int[][] displayPos =
        {
            { 424, 147 },
            { 424, 95 },
        };

    private Image       foreground;
    private CheckBox[]  checkBoxes;
    private ImageButton quitButton;
    public  ImageButton buttonPauseExit;

    private int originX;
    private int originY;

    public PausePanel( int x, int y )
    {
        super();

        setNameID( "Pause Panel" );

        originX = x;
        originY = y;
    }

    @Override
    public void setup()
    {
        Texture       texture = App.getAssets().loadSingleAsset( GameAssets._PAUSE_PANEL_ASSET, Texture.class );
        TextureRegion region  = new TextureRegion( texture );
        foreground = new Image( region );

        originX += ( ( Gfx._HUD_WIDTH - foreground.getPrefWidth() ) / 2 );
        originY += ( ( Gfx._HUD_HEIGHT - foreground.getPrefHeight() ) / 2 );

        foreground.setPosition( originX, originY );
        App.getStage().addActor( foreground );

        String skinFilename = App.getAssets().getSkinFilename();

        if ( skinFilename.equals( "" ) )
        {
            skin = new Skin();
        }
        else
        {
            skin = new Skin( Gdx.files.internal( App.getAssets().getSkinFilename() ) );
        }

        createButtons();
    }

    public ImageButton getQuitButton()
    {
        return quitButton;
    }

    public ImageButton getExitButton()
    {
        return buttonPauseExit;
    }

    private void createButtons()
    {
        checkBoxes = new CheckBox[ _NUM_CHECKBOXES ];

        CheckBox.CheckBoxStyle style = new CheckBox.CheckBoxStyle();
        style.checkboxOn  = new TextureRegionDrawable( App.getAssets().getButtonRegion( "toggle_on" ) );
        style.checkboxOff = new TextureRegionDrawable( App.getAssets().getButtonRegion( "toggle_off" ) );
        style.font        = new BitmapFont();

        for ( int i = 0; i < _NUM_CHECKBOXES; i++ )
        {
            checkBoxes[ i ] = new CheckBox( "", skin );
            checkBoxes[ i ].setStyle( style );
            checkBoxes[ i ].setPosition( ( originX + displayPos[ i ][ 0 ] ), ( originY + displayPos[ i ][ 1 ] ) );
            App.getStage().addActor( checkBoxes[ i ] );
        }

        checkBoxes[ _MUSIC ].setChecked( App.getSettings().isEnabled( Settings._MUSIC_ENABLED ) );
        checkBoxes[ _SOUNDS ].setChecked( App.getSettings().isEnabled( Settings._SOUNDS_ENABLED ) );

        Scene2DUtils utils = new Scene2DUtils();

        quitButton = utils.addButton
            (
                "button_quit_to_title",
                "button_quit_to_title_pressed",
                ( originX + 530 ),
                ( originY + ( Gfx._HUD_HEIGHT - 482 ) )
            );
        quitButton.setChecked( false );

        buttonPauseExit = utils.addButton
            (
                "pause_exit_button",
                "pause_exit_button_pressed",
                ( originX + 870 ),
                ( originY + ( Gfx._HUD_HEIGHT - 190 ) )
            );
        buttonPauseExit.setChecked( false );
    }

    @Override
    public void dispose()
    {
        if ( foreground != null )
        {
            foreground.addAction( Actions.removeActor() );
        }

        if ( quitButton != null )
        {
            quitButton.addAction( Actions.removeActor() );
        }

        if ( buttonPauseExit != null )
        {
            buttonPauseExit.addAction( Actions.removeActor() );
        }

        if ( checkBoxes != null )
        {
            for ( int i = 0; i < _NUM_CHECKBOXES; i++ )
            {
                checkBoxes[ i ].addAction( Actions.removeActor() );
                checkBoxes[ i ] = null;
            }
        }

        checkBoxes = null;
        quitButton = null;
        buttonPauseExit = null;
        foreground = null;

        super.dispose();
    }
}
