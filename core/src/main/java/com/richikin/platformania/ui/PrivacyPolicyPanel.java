package com.richikin.platformania.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.richikin.platformania.graphics.text.FontUtils;
import com.richikin.platformania.logging.Stats;
import com.richikin.platformania.logging.SystemMeters;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.core.App;
import com.richikin.platformania.graphics.GameAssets;
import com.richikin.platformania.graphics.Gfx;

import java.io.BufferedReader;
import java.io.IOException;

public class PrivacyPolicyPanel extends DefaultPanel
{
    private static final String _FILE_NAME = "documents/privacy_policy.txt";

    private final int originX;
    private final int originY;

    public PrivacyPolicyPanel(int x, int y)
    {
        nameID  = "Privacy Policy Panel";
        originX = x;
        originY = y;
    }

    public void setup()
    {
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        table = new Table();
        table.top().left();
        table.pad(30, 30, 30, 30);
        table.setDebug(false);

        populateTable();

        Scene2DUtils utils = new Scene2DUtils();

        scrollPane = utils.createScrollPane(table, skin, "");
        scrollPane.setScrollingDisabled(false, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setWidth(Gfx._HUD_WIDTH - 80);
        scrollPane.setHeight((float) (Gfx._HUD_HEIGHT / 8) * 6);
        scrollPane.setPosition((originX + 40), (originY + 120));

        App.getStage().addActor(scrollPane);
    }

    @Override
    public void populateTable()
    {
        try
        {
            FileHandle     file   = Gdx.files.internal(_FILE_NAME);
            BufferedReader reader = new BufferedReader(file.reader());
            String         string;
            Label          label;
            BitmapFont     bitmapFont;

            FontUtils fontUtils = new FontUtils();
            bitmapFont = fontUtils.createFont(GameAssets._PRO_WINDOWS_FONT, 18);

            while ((string = reader.readLine()) != null)
            {
                label = new Label(string, skin);
                label.setAlignment(Align.left);
                label.setWrap(true);

                Label.LabelStyle labelStyle = new Label.LabelStyle();
                labelStyle.font = bitmapFont;
                labelStyle.font.setColor(Color.WHITE);
                label.setStyle(labelStyle);

                table.add(label).align(Align.left);
                table.row();
            }

            table.setVisible(true);
        }
        catch (NullPointerException npe)
        {
            Trace.checkPoint();
            Stats.incMeter(SystemMeters._NULL_POINTER_EXCEPTION.get());
        }
        catch (IOException ioe)
        {
            Trace.checkPoint();
            Stats.incMeter(SystemMeters._IO_EXCEPTION.get());
        }
    }
}
