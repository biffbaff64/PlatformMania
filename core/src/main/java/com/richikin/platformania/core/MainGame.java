package com.richikin.platformania.core;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.richikin.platformania.google.DummyAdsController;
import com.richikin.platformania.google.PlayServices;
import com.richikin.platformania.google.PlayServicesData;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.ui.SplashScreen;
import com.richikin.platformania.config.AppConfig;
import com.richikin.platformania.graphics.GameAssets;

public class MainGame extends com.badlogic.gdx.Game
{
    private SplashScreen splashScreen;

    /**
     * Main entry point for the actual game.
     */
    public MainGame( PlayServices _services )
    {
        if ( _services != null )
        {
            App.setPlayServices( _services );
            App.setAdsController( new DummyAdsController() );
            App.setPlayServicesData( new PlayServicesData() );
        }
    }

    @Override
    public void create()
    {
        //
        // Initialise DEBUG classes
        //noinspection GDXJavaLogLevel
        Gdx.app.setLogLevel( Application.LOG_DEBUG );
        Trace.enableWriteToFile( true );
        Trace.openDebugFile( "documents/log.txt", true );
        Trace.checkPoint();

        App.setMainGame( this );

        splashScreen = new SplashScreen();
        splashScreen.setup( GameAssets._SPLASH_SCREEN_ASSET );

        //
        // Initialise all essential objects required before
        // the main screen is initialised.
        App.setAppConfig( new AppConfig() );
        App.getAppConfig().setup();
    }

    @Override
    public void render()
    {
        if ( splashScreen.isAvailable )
        {
            if ( !App.getAppConfig().isStartupDone() )
            {
                App.getAppConfig().startApp();
            }

            splashScreen.update();
            splashScreen.render();

            // SplashScreen becomes unavailable in update() if app
            // initialisation has finished.
            if ( !splashScreen.isAvailable )
            {
                App.getAppConfig().closeStartup();
                splashScreen.dispose();
            }
        }
        else
        {
            super.render();
        }
    }
}
