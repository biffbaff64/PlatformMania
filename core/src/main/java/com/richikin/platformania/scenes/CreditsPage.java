package com.richikin.platformania.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.richikin.platformania.config.Version;
import com.richikin.platformania.core.App;
import com.richikin.platformania.graphics.GameAssets;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.config.GdxSystem;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.maths.Vec2;
import com.richikin.platformania.ui.Scene2DUtils;
import com.richikin.platformania.ui.UIPage;

public class CreditsPage implements UIPage
{
    private Texture foreground;
    private Label   versionLabel;

    public CreditsPage()
    {
    }

    @Override
    public void initialise()
    {
        Trace.checkPoint();

        foreground = App.getAssets().loadSingleAsset(GameAssets._CREDITS_PANEL_ASSET, Texture.class);

        Scene2DUtils scene2DUtils = new Scene2DUtils();

        versionLabel = scene2DUtils.addLabel
            (
                Version.getDisplayVersion(),
                new Vec2(80, (Gfx._HUD_HEIGHT - 740)),
                Color.WHITE,
                new Skin(Gdx.files.internal(App.getAssets().getSkinFilename()))
            );
        versionLabel.setFontScale(1.5f, 1.5f);
        versionLabel.setAlignment(Align.left);
    }

    @Override
    public boolean update()
    {
        return false;
    }

    @Override
    public void show()
    {
        Trace.checkPoint();

        if (App.getAppConfig().backButton != null)
        {
            App.getAppConfig().backButton.setVisible(true);
            App.getAppConfig().backButton.setDisabled(false);
            App.getAppConfig().backButton.setChecked(false);
        }

        if (versionLabel != null)
        {
            versionLabel.setVisible(true);
        }

        App.getAppConfig().showAndEnableBackButton();
    }

    @Override
    public void hide()
    {
        Trace.checkPoint();

        if (App.getAppConfig().backButton != null)
        {
            App.getAppConfig().backButton.setVisible(false);
            App.getAppConfig().backButton.setDisabled(true);
        }

        if (versionLabel != null)
        {
            versionLabel.setVisible(false);
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        if (foreground != null)
        {
            spriteBatch.draw
                (
                    foreground,
                    (App.getBaseRenderer().getHudGameCamera().camera.viewportWidth - foreground.getWidth()) / 2,
                    (App.getBaseRenderer().getHudGameCamera().camera.viewportHeight - foreground.getHeight()) / 2
                );
        }
    }

    @Override
    public void dispose()
    {
        Trace.checkPoint();

        App.getAssets().unloadAsset(GameAssets._CREDITS_PANEL_ASSET);

        if (versionLabel != null)
        {
            versionLabel.addAction(Actions.removeActor());
            versionLabel = null;
        }

        foreground = null;
    }
}
