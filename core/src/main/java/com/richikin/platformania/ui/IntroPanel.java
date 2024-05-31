package com.richikin.platformania.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.richikin.platformania.core.App;
import com.richikin.platformania.graphics.GameAssets;
import com.richikin.platformania.graphics.Gfx;

public class IntroPanel extends DefaultPanel
{
    private static final int _TICK_X = 1020;
    private static final int _TICK_Y = 562;

    private Image       foreground;
    private ImageButton tickButton;
    private int         originX;
    private int         originY;

    public IntroPanel()
    {
    }

    public void create()
    {
        Texture       texture = App.getAssets().loadSingleAsset( GameAssets._INTRO_PANEL_ASSET, Texture.class );
        TextureRegion region  = new TextureRegion( texture );
        foreground = new Image( region );

        originX += ( ( Gfx._HUD_WIDTH - foreground.getPrefWidth() ) / 2 );
        originY += ( ( Gfx._HUD_HEIGHT - foreground.getPrefHeight() ) / 2 );

        foreground.setPosition( originX, originY );
        App.getStage().addActor( foreground );

        Scene2DUtils utils = new Scene2DUtils();

        tickButton = utils.addButton
            (
                "button_tick",
                "button_tick_pressed",
                _TICK_X,
                ( Gfx._HUD_HEIGHT - _TICK_Y )
            );
    }

    @Override
    public boolean update()
    {
        return tickButton.isPressed();
    }

    @Override
    public void dispose()
    {
        foreground.addAction( Actions.removeActor() );
        tickButton.addAction( Actions.removeActor() );
        foreground = null;
        tickButton = null;
    }
}
