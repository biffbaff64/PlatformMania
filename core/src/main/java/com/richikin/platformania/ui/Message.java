package com.richikin.platformania.ui;

import com.badlogic.gdx.graphics.Color;
import com.richikin.platformania.graphics.text.TextUtils;
import com.richikin.platformania.logging.StopWatch;
import com.richikin.platformania.core.App;
import com.richikin.platformania.graphics.GameAssets;

import java.util.concurrent.TimeUnit;

public class Message
{
    private final TextUtils textUtils;
    private       String    message;
    private       StopWatch stopWatch;
    private       int       displayX;
    private       int       displayY;

    public Message()
    {
        textUtils = new TextUtils();
    }

    public void setMessage( String msg, int x, int y )
    {
        textUtils.setFont( GameAssets._ACME_FONT, 20, Color.WHITE );

        message   = msg;
        displayX  = x;
        displayY  = y;
        stopWatch = new StopWatch();
    }

    public void draw()
    {
        if ( message != null )
        {
            if ( stopWatch.time( TimeUnit.MILLISECONDS ) > 3000 )
            {
                message = null;
            }
            else
            {
                textUtils.drawText( message, displayX, displayY, App.getSpriteBatch() );
            }
        }
    }
}
