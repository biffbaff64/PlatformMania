package com.richikin.platformania.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.platformania.graphics.text.FontUtils;
import com.richikin.platformania.core.App;
import com.richikin.platformania.graphics.GameAssets;
import com.richikin.platformania.graphics.Gfx;

public class YesNoDialog implements Disposable
{
    private Dialog  dialog;
    private boolean justLeftDialog;
    private boolean dialogResult;

    public YesNoDialog()
    {
    }

    /**
     * Creates the YES/NO dialog and adds it to the Scene2D stage.
     *
     * @param text A message to display in front of " (YES/NO) ?".
     *             This could be "Are You Sure", or "Quit Game" for example.
     */
    public void createDialog( String text )
    {
        FontUtils fontUtils = new FontUtils();

        Skin skin = new Skin( Gdx.files.internal( App.getAssets().getSkinFilename() ) );

        justLeftDialog = false;

        dialog = new Dialog( "", skin )
        {
            protected void result( Object object )
            {
                dialogResult = ( Boolean ) object;
                justLeftDialog = true;
            }
        };

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        Label.LabelStyle           labelStyle  = new Label.LabelStyle();

        buttonStyle.font      = fontUtils.createFont( GameAssets._ACME_FONT, 32 );
        buttonStyle.fontColor = Color.WHITE;
        labelStyle.font       = buttonStyle.font;
        labelStyle.fontColor  = buttonStyle.fontColor;

        Button buttonYes = new TextButton( "YES", buttonStyle );
        Button buttonNo  = new TextButton( "NO", buttonStyle );

        buttonYes.padRight( 30 );
        buttonNo.padLeft( 30 );

        dialog.button( buttonYes, true );
        dialog.button( buttonNo, false );
        dialog.text( text + " (Y/N)", labelStyle );
        dialog.key( Input.Keys.ENTER, true ).key( Input.Keys.ESCAPE, false );
        dialog.setSize( 300.0f, 200.0f );
        dialog.setPosition( Gfx._HUD_WIDTH / 2.0f, Gfx._HUD_HEIGHT / 2.0f, Align.center );

        App.getStage().addActor( dialog );
    }

    public boolean getDialogResult()
    {
        return dialogResult;
    }

    public boolean hasJustLeftDialog()
    {
        return justLeftDialog;
    }

    @Override
    public void dispose()
    {
        dialog.addAction( Actions.removeActor() );
        dialog = null;
    }
}
