package com.richikin.platformania.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.StringBuilder;
import com.richikin.platformania.config.Settings;
import com.richikin.platformania.core.App;
import com.richikin.platformania.graphics.GameAssets;
import com.richikin.platformania.graphics.text.FontUtils;

public class HudDebug
{
    private final BitmapFont smallFont;

    public HudDebug()
    {
        FontUtils fontUtils = new FontUtils();
        smallFont = fontUtils.createFont( GameAssets._ACME_FONT, HeadsUpDisplay._SMALL_FONT_SIZE );
    }

    /**
     * On-Screen debug messages.
     * Displays FPS, DEV_MODE, GOD_MODE, TiledCamera Zoom level.
     * Also displays any further messages added.
     */
    public void drawHudDebug( float hudOriginX, float hudOriginY )
    {
        if ( App.getSettings().isEnabled( Settings._SHOW_DEBUG ) )
        {
            // -------------------------------

            StringBuilder sb = new StringBuilder( "DEV MODE" );

            if ( App.getDev().isGodMode() )
            {
                sb.append( " : GOD MODE" );
            }

            smallFont.draw( App.getSpriteBatch(), sb.toString(), hudOriginX + 50, hudOriginY + 30 );

            sb.clear();
            sb.append( " : FPS: " ).append( Gdx.graphics.getFramesPerSecond() );
            sb.append( " : ZOOM: " ).append( App.getBaseRenderer().getTiledGameCamera().camera.zoom );
            sb.append( "[ " );
            sb.append( App.getBaseRenderer().getHudGameCamera().camera.zoom );
            sb.append( " ]" );
            smallFont.draw( App.getSpriteBatch(), sb.toString(), hudOriginX + 50, hudOriginY + 55 );

            int yOffset = 80;

            // -------------------------------
            if ( App.getPlayer() != null )
            {
                sb.clear();
                sb.append( "ACT: " ).append( App.getPlayer().getActionState().name() );
                smallFont.draw( App.getSpriteBatch(), sb.toString(), hudOriginX + 50, hudOriginY + yOffset );
                yOffset += 25;

                sb.clear();
                sb.append( "CONTACTS: " ).append( App.getPlayer().getPhysicsBody().contactCount );
                smallFont.draw( App.getSpriteBatch(), sb.toString(), hudOriginX + 50, hudOriginY + yOffset );
                yOffset += 25;

                sb.clear();
                sb.append( "PLATFORM: " ).append( App.getPlayer().isOnPlatform );
                sb.append( "    " );
                sb.append( "LINK: " ).append( App.getPlayer().getLink() );
                smallFont.draw( App.getSpriteBatch(), sb.toString(), hudOriginX + 50, hudOriginY + yOffset );
                yOffset += 25;

                sb.clear();
                sb.append( "LOOK: " ).append( App.getPlayer().lookingAt.getX() );
                sb.append( "      " );
                sb.append( "MOVE: " ).append( App.getPlayer().direction.getX() );
                smallFont.draw( App.getSpriteBatch(), sb.toString(), hudOriginX + 50, hudOriginY + yOffset );
                yOffset += 25;
            }
        }
    }
}
