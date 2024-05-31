package com.richikin.platformania.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.platformania.logging.StopWatch;
import com.richikin.platformania.logging.Trace;

import java.util.concurrent.TimeUnit;

public class SplashScreen implements Disposable
{
    private static final SplashScreen _INSTANCE;

    // Instance initialiser block
    static
    {
        try
        {
            _INSTANCE = new SplashScreen();
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e.getMessage() );
        }
    }

    public boolean isAvailable;

    private SpriteBatch batch;
    private Texture     background;
    private StopWatch   stopWatch;

    public static SplashScreen inst()
    {
        return _INSTANCE;
    }

    public void setup( final String imageName )
    {
        Trace.checkPoint();

        batch      = new SpriteBatch();
        background = new Texture( imageName );
        stopWatch  = new StopWatch();

        isAvailable = true;
    }

    public void update()
    {
        if ( stopWatch.time( TimeUnit.MILLISECONDS ) > 1500 )
        {
            isAvailable = false;
        }
    }

    public void render()
    {
        if ( isAvailable )
        {
            Gdx.gl.glClearColor( 0, 0, 0, 1 );
            Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );

            batch.begin();
            batch.draw
                (
                    background,
                    0,
                    0,
                    Gdx.graphics.getWidth(),
                    Gdx.graphics.getHeight()
                );
            batch.end();
        }
    }

    @Override
    public void dispose()
    {
        Trace.checkPoint();

        batch.dispose();
        background.dispose();

        stopWatch  = null;
        batch      = null;
        background = null;
    }
}
