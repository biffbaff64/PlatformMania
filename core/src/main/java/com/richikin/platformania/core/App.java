package com.richikin.platformania.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.richikin.platformania.assets.Assets;
import com.richikin.platformania.audio.AudioInterface;
import com.richikin.platformania.config.AppConfig;
import com.richikin.platformania.config.Settings;
import com.richikin.platformania.entities.EntityData;
import com.richikin.platformania.enums.StateID;
import com.richikin.platformania.google.AdsController;
import com.richikin.platformania.google.PlayServices;
import com.richikin.platformania.google.PlayServicesData;
import com.richikin.platformania.graphics.GameAssets;
import com.richikin.platformania.input.InputInterface;
import com.richikin.platformania.logging.StateManager;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.audio.GameAudio;
import com.richikin.platformania.config.Developer;
import com.richikin.platformania.entities.AnimationUtils;
import com.richikin.platformania.entities.Entities;
import com.richikin.platformania.entities.EntityManager;
import com.richikin.platformania.entities.EntityUtils;
import com.richikin.platformania.entities.actors.hero.MainPlayer;
import com.richikin.platformania.entities.paths.PathUtils;
import com.richikin.platformania.graphics.renderers.BaseRenderer;
import com.richikin.platformania.input.InputManager;
import com.richikin.platformania.maps.MapData;
import com.richikin.platformania.maps.MapParser;
import com.richikin.platformania.maps.MapUtils;
import com.richikin.platformania.maps.RoomManager;
import com.richikin.platformania.maps.TMXMapParser;
import com.richikin.platformania.physics.aabb.AABBRenderer;
import com.richikin.platformania.scenes.MainScene;
import com.richikin.platformania.scenes.TitleScene;
import com.richikin.platformania.ui.HeadsUpDisplay;

@SuppressWarnings( { "GDXJavaStaticResource", "AssignmentToNull" } )
public class App
{
    private static AdsController    adsController;
    private static PlayServices     googleServices;
    private static PlayServicesData playServicesData;
    private static Settings         settings;
    private static Assets           assets;
    private static AppConfig        appConfig;
    private static Developer        developer;
    private static SpriteBatch      spriteBatch;
    private static Stage            stage;
    private static AABBRenderer     aabbRenderer;
    private static HighScoreUtils   highScoreUtils;
    private static StateManager     appState;
    private static AudioInterface   audio;
    private static InputInterface   inputManager;
    private static EntityData       entityData;
    private static MapData          mapData;
    private static BaseRenderer     baseRenderer;
    private static MainGame         mainGame;
    private static Box2DHelper      box2DHelper;
    private static TitleScene       titleScene;
    private static MainScene        mainScene;
    private static GameProgress     gameProgress;
    private static Entities         entities;
    private static LevelManager     levelManager;
    private static HeadsUpDisplay   hud;
    private static EntityManager    entityManager;
    private static EntityUtils      entityUtils;
    private static AnimationUtils   animationUtils;
    private static MapParser        tmxMapParser;
    private static MapUtils         mapUtils;
    private static RoomManager      roomManager;
    private static PathUtils        pathUtils;

    // ------------------------------------------------------------------------
    // CODE
    // ------------------------------------------------------------------------

    /**
     * Creates essential global objects that need to be accessible from app startup. These will only
     * be destroyed when the app is closed. Some data held within these objects may be cleared
     * elsewhere, but the objects can only be destroyed in {@link App#deleteEssentialObjects()}
     */
    public static void createEssentialObjects()
    {
        Trace.checkPoint();

        createObjects();

        // -----------------------
        // These are not initialised in App as these are local
        // classes implementing GdxUtils interfaces, or
        // extending GdxUtils classes..
        setSettings( new Settings() );
        setAssets( new GameAssets() );
        setAudio( new GameAudio() );
        setInputManager( new InputManager() );
        setDeveloper( new Developer() );

        // -----------------------
        baseRenderer = new BaseRenderer();
        box2DHelper  = new Box2DHelper();
        gameProgress = new GameProgress();
    }

    /**
     * Creates global objects that are only needed when {@link MainScene} is active. These objects
     * will only be destroyed when control is returned to {@link TitleScene}
     */
    public static void createMainSceneObjects()
    {
        Trace.checkPoint();

        // -----------------------
        entities       = new Entities();
        levelManager   = new LevelManager();
        hud            = new HeadsUpDisplay();
        entityManager  = new EntityManager();
        entityUtils    = new EntityUtils();
        animationUtils = new AnimationUtils();
        tmxMapParser   = new TMXMapParser();
        mapUtils       = new MapUtils();
        roomManager    = new RoomManager();
        pathUtils      = new PathUtils();
    }

    public static void createObjects()
    {
        appState     = new StateManager( StateID._STATE_POWER_UP );
        spriteBatch  = new SpriteBatch();
        aabbRenderer = new AABBRenderer();
        mapData      = new MapData();
        entityData   = new EntityData();
    }

    public static void deleteEssentialObjects()
    {
        Trace.checkPoint();

        tidy();

        // -----------------------
        if ( baseRenderer != null )
        {
            baseRenderer.dispose();
            baseRenderer = null;
        }

        if ( box2DHelper != null )
        {
            box2DHelper.dispose();
            box2DHelper = null;
        }

        if ( gameProgress != null )
        {
            gameProgress.dispose();
            gameProgress = null;
        }
        // -----------------------

        App.getAudio().tidy();
    }

    public static void deleteMainsceneObjects()
    {
        Trace.checkPoint();

        // -----------------------
        entities.dispose();
        hud.dispose();
        entityManager.dispose();
        tmxMapParser.dispose();
        mapUtils.dispose();
        pathUtils.dispose();

        // -----------------------
        mainScene.dispose();
        mainScene = null;

        // -----------------------
        entities       = null;
        levelManager   = null;
        hud            = null;
        entityManager  = null;
        entityUtils    = null;
        animationUtils = null;
        tmxMapParser   = null;
        mapUtils       = null;
        roomManager    = null;
    }

    // ------------------------------------------------------------------------
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    //@formatter:off
    public static BaseRenderer          getBaseRenderer()           {   return baseRenderer;        }
    public static MainGame              getMainGame()               {   return mainGame;            }
    public static Box2DHelper           getWorldModel()             {   return box2DHelper;         }
    public static TitleScene            getTitleScene()             {   return titleScene;          }
    public static MainScene             getMainScene()              {   return mainScene;           }
    public static GameProgress          getGameProgress()           {  return gameProgress;         }
    public static LevelManager          getLevelManager()           {  return levelManager;         }
    public static HeadsUpDisplay        getHud()                    {  return hud;                  }
    public static Entities              getEntities()               {  return entities;             }
    public static EntityManager         getEntityManager()          {  return entityManager;        }
    public static EntityUtils           getEntityUtils()            {  return entityUtils;          }
    public static AnimationUtils        getAnimationUtils()         {  return animationUtils;       }
    public static MapParser             getMapParser()              {  return tmxMapParser;         }
    public static MapUtils              getMapUtils()               {  return mapUtils;             }
    public static RoomManager           getRoomManager()            {  return roomManager;          }
    public static PathUtils             getPathUtils()              {  return pathUtils;            }
    public static AdsController         getAdsController()          {   return adsController;       }
    public static PlayServices          getPlayServices()           {   return googleServices;      }
    public static PlayServicesData      getPlayServicesData()       {   return playServicesData;    }
    public static Settings              getSettings()               {   return settings;            }
    public static Assets                getAssets()                 {   return assets;              }
    public static AppConfig             getAppConfig()              {   return appConfig;           }
    public static AudioInterface        getAudio()                  {   return audio;               }
    public static InputInterface        getInputManager()           {   return inputManager;        }
    public static HighScoreUtils        getHighScoreUtils()         {   return highScoreUtils;      }
    public static StateManager          getAppState()               {   return appState;            }
    public static EntityData            getEntityData()             {   return entityData;          }
    public static MapData               getMapData()                {   return mapData;             }
    public static Developer             getDev()                    {   return developer;           }
    public static Stage                 getStage()                  {   return stage;               }
    public static SpriteBatch           getSpriteBatch()            {   return spriteBatch;         }
    public static AABBRenderer          getAABBRenderer()           {   return aabbRenderer;        }

    public static void  setAdsController( AdsController controller )    {   adsController = controller; }
    public static void  setPlayServices( PlayServices services )        {   googleServices = services;  }
    public static void  setPlayServicesData( PlayServicesData psData )  {   playServicesData = psData;  }
    public static void  setSettings( Settings _settings )               {   settings = _settings;       }
    public static void  setAppConfig( AppConfig config )                {   appConfig = config;         }
    public static void  setAssets( Assets _assets )                     {   assets = _assets;           }
    public static void  setAudio( AudioInterface _audio )               {   audio = _audio;             }
    public static void  setInputManager( InputInterface manager )       {   inputManager = manager;     }
    public static void  setDeveloper( Developer dev )                   {   developer = dev;            }
    //@formatter:on
    // ------------------------------------------------------------------------

    /**
     * Sets the global access reference to MainGame, which extends LibGDX's Game class. This allows
     * for access to setScreen() in Config classes etc.
     */
    public static void setMainGame( MainGame game )
    {
        mainGame = game;
    }

    public static void setTitleScene( TitleScene scene )
    {
        titleScene = scene;
    }

    public static void setMainScene( MainScene scene )
    {
        mainScene = scene;
    }

    public static MainPlayer getPlayer()
    {
        return entities.mainPlayer;
    }

    public static int getLevel()
    {
        return gameProgress.gameLevel;
    }

    public static void createStage( Viewport viewport )
    {
        stage = new Stage( viewport, getSpriteBatch() );
    }

    public static void tidy()
    {
        stage.dispose();
        spriteBatch.dispose();
        assets.dispose();
        settings.dispose();
        mapData.dispose();
        entityData.dispose();

        if ( appConfig.isAndroidApp() )
        {
            googleServices.signOut();
        }

        adsController    = null;
        googleServices   = null;
        playServicesData = null;
        mapData          = null;
        inputManager     = null;
        appState         = null;
        settings         = null;
        appConfig        = null;
        assets           = null;
        audio            = null;
        highScoreUtils   = null;
        entityData       = null;
        spriteBatch      = null;
        stage            = null;
        aabbRenderer     = null;
    }
}
