package com.richikin.platformania.audio;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.richikin.platformania.config.Settings;
import com.richikin.platformania.core.App;
import com.richikin.platformania.config.GdxSystem;
import com.richikin.platformania.logging.Trace;

public class GameAudio implements AudioInterface
{
    /**
     * Standard Audio properties.
     */
    public static final int _SILENT               = 0;
    public static final int _MIN_VOLUME           = 0;
    public static final int _MAX_VOLUME           = 10;
    public static final int _VOLUME_INCREMENT     = 1;
    public static final int _VOLUME_MULTIPLIER    = 10;
    public static final int _DEFAULT_MUSIC_VOLUME = 4;
    public static final int _DEFAULT_FX_VOLUME    = 6;

    public static final int SFX_LASER          = 0;
    public static final int SFX_PLAZMA         = 1;
    public static final int SFX_EXPLOSION_1    = 2;
    public static final int SFX_EXPLOSION_2    = 3;
    public static final int SFX_EXPLOSION_3    = 4;
    public static final int SFX_EXPLOSION_4    = 5;
    public static final int SFX_EXPLOSION_5    = 6;
    public static final int SFX_THRUST         = 7;
    public static final int SFX_PICKUP         = 8;
    public static final int SFX_TELEPORT       = 9;
    public static final int SFX_EXTRA_LIFE     = 10;
    public static final int SFX_LAUNCH_WARNING = 11;
    public static final int SFX_TEST_SOUND     = 12;
    public static final int SFX_BEEP           = 13;
    public static final int SFX_LOST           = 14;
    public static final int MAX_SOUND          = 15;

    public static final int MUS_TITLE   = 0;
    public static final int MUS_HISCORE = 1;
    public static final int MUS_GAME    = 2;
    public static final int MAX_TUNES   = 3;

    private Sound[] sounds;
    private Music[] music;

    private int     currentTune;
    private int     musicVolumeSave;
    private int     fxVolumeSave;
    private boolean soundsLoaded;
    private boolean musicLoaded;
    private boolean isTunePaused;

    public GameAudio()
    {
    }

    @Override
    public void setup()
    {
        soundsLoaded = false;
        musicLoaded  = false;
        isTunePaused = false;

        loadSounds();

        musicVolumeSave = Math.max( 0, _DEFAULT_MUSIC_VOLUME );
        fxVolumeSave    = Math.max( 0, _DEFAULT_FX_VOLUME );
    }

    @Override
    public void update()
    {
        if ( musicLoaded )
        {
            if ( App.getAppConfig().gamePaused )
            {
                if ( ( getMusic()[ currentTune ] != null )
                    && getMusic()[ currentTune ].isPlaying() )
                {
                    getMusic()[ currentTune ].pause();
                    isTunePaused = true;
                }
            }
            else
            {
                if ( ( getMusic()[ currentTune ] != null )
                    && !getMusic()[ currentTune ].isPlaying()
                    && isTunePaused )
                {
                    getMusic()[ currentTune ].play();
                    isTunePaused = false;
                }
            }
        }
    }

    private void loadSounds()
    {
        Trace.checkPoint();

        try
        {
//            sounds[SFX_HIT]       = App.getAssets().loadSingleAsset("data/sounds/hit.wav", Sound.class);
//            sounds[SFX_LOST]      = App.getAssets().loadSingleAsset("data/sounds/lost.wav", Sound.class);
//            sounds[SFX_PICKUP]    = App.getAssets().loadSingleAsset("data/sounds/pickup.wav", Sound.class);
//            sounds[SFX_EXTRALIFE] = App.getAssets().loadSingleAsset("data/sounds/extra_life.mp3", Sound.class);
//
//            music[MUS_TITLE] = App.getAssets().loadSingleAsset("data/sounds/Bouncy.mp3", Music.class);
//            music[MUS_HISCORE] = App.getAssets().loadSingleAsset("data/sounds/breath.mp3", Music.class);
//            music[MUS_GAME]    = App.getAssets().loadSingleAsset("data/sounds/fear_mon.mp3", Music.class);

            App.getSettings().getPrefs().putInteger( Settings._MUSIC_VOLUME, _DEFAULT_MUSIC_VOLUME );
            App.getSettings().getPrefs().putInteger( Settings._FX_VOLUME, _DEFAULT_FX_VOLUME );
            App.getSettings().getPrefs().flush();

            soundsLoaded = getSounds().length > 0;
            musicLoaded  = getMusic().length > 0;
        }
        catch ( Exception e )
        {
            Trace.err( "SOUNDS NOT LOADED!" );

            soundsLoaded = false;
            musicLoaded  = false;
        }
    }

    @Override
    public void playTune( boolean play )
    {
        if ( currentTune >= 0 )
        {
            if ( play && musicLoaded )
            {
                startTune( currentTune, getMusicVolume(), true );
            }
            else
            {
                tuneStop();
            }
        }
    }

    /**
     * Play or Stop the Main Game tune.
     *
     * @param playTune TRUE to play, FALSE to stop playing.
     */
    @Override
    public void playGameTune( boolean playTune )
    {
        if ( MUS_GAME >= 0 )
        {
            if ( playTune && musicLoaded )
            {
                startTune( MUS_GAME, getMusicVolume(), true );
            }
            else
            {
                tuneStop();
            }
        }
    }

    /**
     * Play or Stop the Main Title tune.
     *
     * @param playTune TRUE to play, FALSE to stop playing.
     */
    @Override
    public void playTitleTune( boolean playTune )
    {
        if ( MUS_TITLE >= 0 )
        {
            if ( playTune && musicLoaded )
            {
                startTune( MUS_TITLE, getMusicVolume(), true );
            }
            else
            {
                tuneStop();
            }
        }
    }

    /**
     * Play or Stop the HiScore name entry tune. This tune is played on the name
     * entry screen only, NOT when the hiscore table is displayed in the titles
     * screen sequence.
     * @param playTune TRUE to play, FALSE to stop playing.
     */
    @Override
    public void playHiScoreTune( boolean playTune )
    {
        if ( MUS_HISCORE >= 0 )
        {
            if ( playTune & musicLoaded )
            {
                startTune( MUS_HISCORE, getMusicVolume(), true );
            }
            else
            {
                tuneStop();
            }
        }
    }

    @Override
    public void startTune( int musicNumber, int volume, boolean looping )
    {
        if ( musicLoaded && ( musicNumber >= 0 ) )
        {
            if ( getMusicVolume() > 0 )
            {
                if ( App.getSettings().isEnabled( Settings._MUSIC_ENABLED )
                    && ( getMusic() != null )
                    && !getMusic()[ musicNumber ].isPlaying() )
                {
                    getMusic()[ musicNumber ].setLooping( looping );
                    getMusic()[ musicNumber ].setVolume( volume );
                    getMusic()[ musicNumber ].play();

                    currentTune = musicNumber;
                }
            }
        }
    }

    @Override
    public void startSound( int soundNumber )
    {
        if ( App.getSettings().isEnabled( Settings._SOUNDS_ENABLED ) && soundsLoaded && ( soundNumber >= 0 ) )
        {
            if ( getFXVolume() > 0 )
            {
                if ( getSounds()[ soundNumber ] != null )
                {
                    getSounds()[ soundNumber ].play( getFXVolume() );
                }
            }
        }
    }

    @Override
    public void tuneStop()
    {
        if ( musicLoaded && ( currentTune >= 0 ) )
        {
            if ( ( getMusic()[ currentTune ] != null ) && getMusic()[ currentTune ].isPlaying() )
            {
                getMusic()[ currentTune ].stop();
            }
        }
    }

    @Override
    public void setMusicVolume( int volume )
    {
        if ( musicLoaded && ( currentTune >= 0 ) )
        {
            if ( getMusic()[ currentTune ] != null )
            {
                getMusic()[ currentTune ].setVolume( volume );
            }
        }

        App.getSettings().getPrefs().putInteger( Settings._MUSIC_VOLUME, volume );
        App.getSettings().getPrefs().flush();
    }

    @Override
    public void setFXVolume( int volume )
    {
        App.getSettings().getPrefs().putInteger( Settings._FX_VOLUME, volume );
        App.getSettings().getPrefs().flush();
    }

    @Override
    public int getMusicVolume()
    {
        return App.getSettings().getPrefs().getInteger( Settings._MUSIC_VOLUME );
    }

    @Override
    public int getFXVolume()
    {
        return App.getSettings().getPrefs().getInteger( Settings._FX_VOLUME );
    }

    @Override
    public void saveMusicVolume()
    {
        musicVolumeSave = getMusicVolume();
    }

    @Override
    public void saveFXVolume()
    {
        fxVolumeSave = getFXVolume();
    }

    @Override
    public int getMusicVolumeSave()
    {
        return musicVolumeSave;
    }

    @Override
    public int getFXVolumeSave()
    {
        return fxVolumeSave;
    }

    @Override
    public Sound[] getSounds()
    {
        return sounds;
    }

    @Override
    public Music[] getMusic()
    {
        return music;
    }

    @Override
    public boolean isTunePlaying()
    {
        return isTunePlaying( currentTune );
    }

    @Override
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isTunePlaying( int tune )
    {
        if ( tune >= 0 )
        {
            return musicLoaded && getMusic()[ tune ].isPlaying();
        }

        return false;
    }

    @Override
    public void tidy()
    {
        music = null;
        sounds = null;
    }
}
