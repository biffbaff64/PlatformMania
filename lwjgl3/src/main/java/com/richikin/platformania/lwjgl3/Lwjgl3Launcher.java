package com.richikin.platformania.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.richikin.platformania.graphics.TextureAtlasBuilder;
import com.richikin.platformania.core.MainGame;
import com.richikin.platformania.config.Version;
import com.richikin.platformania.graphics.Gfx;

/**
 * Launches the desktop (LWJGL3) application.
 */
public class Lwjgl3Launcher
{
    public static void main( String[] args )
    {
        if ( StartupHelper.startNewJvmIfRequired() )
        {
            return; // This handles macOS support and helps on Windows.
        }

        TextureAtlasBuilder.buildTextureAtlases();

        createApplication();
    }

    private static void createApplication()
    {
        new Lwjgl3Application( new MainGame( null ), getDefaultConfiguration() );
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration()
    {
        Gfx.setDesktopDimensions();

        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle( Version.getProjectID() );
        configuration.useVsync( true );

        //// Limits FPS to the refresh rate of the currently active monitor.
        configuration.setForegroundFPS( Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate );

        //// If you remove the above line and set Vsync to false, you can get unlimited FPS, which can be
        //// useful for testing performance, but can also be very stressful to some hardware.
        //// You may also need to configure GPU drivers to fully disable Vsync; this can cause screen tearing.
        configuration.setWindowedMode( Gfx._DESKTOP_WIDTH, Gfx._DESKTOP_HEIGHT );
        configuration.setWindowIcon( "icon32x32.png" );

        return configuration;
    }
}
