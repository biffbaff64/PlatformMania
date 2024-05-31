package com.richikin.platformania.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.enums.TileID;
import com.richikin.platformania.config.GdxSystem;
import com.richikin.platformania.graphics.camera.OrthogonalTiledMapRendererBleeding;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.maths.SimpleVec2F;
import com.richikin.platformania.maths.shapes.Box;
import com.richikin.platformania.physics.Dir;
import com.richikin.platformania.physics.Direction;
import com.richikin.platformania.physics.Movement;
import com.richikin.platformania.core.App;
import com.richikin.platformania.entities.objects.SpriteDescriptor;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.physics.PhysicsBodyType;
import org.jetbrains.annotations.NotNull;

public class TMXMapParser implements MapParser
{
    public static final int _LAYER1          = 0;
    public static final int _LAYER2          = 1;
    public static final int _LAYER3          = 2;
    public static final int _LAYER4          = 3;
    public static final int _OVERLAYS        = 4;
    public static final int _OBJECT_MARKERS  = 5;
    public static final int _COLLISION_LAYER = 6;

    public TmxMapLoader               tmxMapLoader;
    public TiledMapTileLayer          gameTilesLayer1;
    public TiledMapTileLayer          gameTilesLayer2;
    public TiledMapTileLayer          gameTilesLayer3;
    public TiledMapTileLayer          gameTilesLayer4;
    public TiledMapTileLayer          overlaysTilesLayer;
    public MapObjects                 objectMarkerTilesLayer;
    public MapObjects                 aabbMapObjects;
    public TiledMap                   currentMap;
    public OrthogonalTiledMapRenderer mapRenderer;
    public String                     currentMapName;

    // -----------------------------------------------------
    // Code
    // -----------------------------------------------------

    public TMXMapParser()
    {
        tmxMapLoader = new TmxMapLoader();
    }

    /**
     * Set up the Map and related data for the current level.
     */
    @Override
    public void initialiseLevelMap()
    {
        currentMapName = App.getRoomManager().getCurrentMapNameWithPath();

        Trace.dbg( "Loading map: ", currentMapName, " [ Level = ", App.getLevel(), "]" );

        currentMap = tmxMapLoader.load( currentMapName );

        if ( mapRenderer == null )
        {
            mapRenderer = new OrthogonalTiledMapRendererBleeding( currentMap, App.getSpriteBatch() );
        }

        setGameLevelMap();

        mapRenderer.setMap( currentMap );

        App.getMapData().scrollDirection.set( Movement._DIRECTION_STILL, Movement._DIRECTION_STILL );
    }

    /**
     * Creates the positioning data for all sprites, AABB boxes and other game
     * entities, by reading this information from TiledMap .tmx files.
     */
    @Override
    public void createPositioningData()
    {
        Trace.checkPoint();

        App.getMapData().placementTiles.clear();
        App.getMapData().spawnerPositions.clear();
        App.getMapData().waterList.clear();
        App.getMapData().doorList.clear();
        App.getMapData().checkPointList.clear();

        parseObjectBasedMarkerTiles();      // Marker tiles with associated properties
        parseTiledCollision();              // Tiles that have associated collision box defined
        parsePolygonCollision();            // Non-rectangular collision 'boxes'.
        parseAABBObjects();                 // Collision boxes for any solid boxes that may be present.
    }

    /**
     * Parse any Marker Tiles present in the marker tiles object layer.
     * Fills the placementTiles array ready for processing.
     */
    @Override
    public void parseObjectBasedMarkerTiles()
    {
        Trace.checkPoint();

        for ( MapObject mapObject : objectMarkerTilesLayer )
        {
            if ( mapObject instanceof TiledMapTileMapObject )
            {
                if ( null != mapObject.getName() )
                {
                    for ( SpriteDescriptor item : App.getEntities().entityList )
                    {
                        SpriteDescriptor descriptor = new SpriteDescriptor( item );

                        if ( mapObject.getName().equals( descriptor._NAME ) )
                        {
                            if ( descriptor._GID != GraphicID.G_NO_ID )
                            {
                                // 'mapObject' will contain positioning, distance speed, and direction information.
                                // 'descriptor' will contain identity information.
                                descriptor._POSITION.x = ( int ) ( ( ( TiledMapTileMapObject ) mapObject ).getX() / App.getMapData().tileWidth );
                                descriptor._POSITION.y = ( int ) ( ( ( TiledMapTileMapObject ) mapObject ).getY() / App.getMapData().tileHeight );
                                descriptor._DIST       = new SimpleVec2F();
                                descriptor._DIR        = new Direction();
                                descriptor._SPEED      = new SimpleVec2F();

                                ObjectTileProperties properties = setObjectTileProperties( descriptor );

                                if ( properties.hasDistance )
                                {
                                    descriptor._DIST.set
                                        (
                                            ( ( int ) mapObject.getProperties().get( "xdistance" ) ),
                                            ( ( int ) mapObject.getProperties().get( "ydistance" ) )
                                        );
                                }

                                if ( properties.hasDirection )
                                {
                                    descriptor._DIR.set
                                        (
                                            mapObject.getProperties().get( "xdirection" )
                                                .equals( "right" ) ? Movement._DIRECTION_RIGHT :
                                            mapObject.getProperties().get( "xdirection" )
                                                .equals( "left" ) ? Movement._DIRECTION_LEFT :
                                            Movement._DIRECTION_STILL,

                                            mapObject.getProperties().get( "ydirection" )
                                                .equals( "up" ) ? Movement._DIRECTION_UP :
                                            mapObject.getProperties().get( "ydirection" )
                                                .equals( "down" ) ? Movement._DIRECTION_DOWN :
                                            Movement._DIRECTION_STILL
                                        );
                                }

                                // A bounds box here is a box which defines the area
                                // in which an entity can move.
                                // I use these for entities which extend BoundedEntity
                                // such as moving platforms etc.
                                if ( properties.hasBoundsBox )
                                {
                                    descriptor._BOX = new Box
                                        (
                                            ( int ) mapObject.getProperties().get( "width" ),
                                            ( int ) mapObject.getProperties().get( "height" )
                                        );
                                }

                                if ( properties.hasSpeed )
                                {
                                    descriptor._SPEED.set
                                        (
                                            ( ( float ) mapObject.getProperties().get( "xspeed" ) ),
                                            ( ( float ) mapObject.getProperties().get( "yspeed" ) )
                                        );
                                }

                                // Linked entities are those which directly affect
                                // each other, such as switches which open doors.
                                if ( properties.isLinked )
                                {
                                    //
                                    // Fetch the link ID of the attached entity
                                    if ( mapObject.getProperties().get( "connection" ) != null )
                                    {
                                        descriptor._LINK = ( int ) mapObject.getProperties().get( "connection" );
                                    }
                                }

                                descriptor._INDEX = App.getMapData().placementTiles.size;

                                App.getMapData().placementTiles.add( descriptor );

                                if ( descriptor._TILE == TileID._SPAWNER_TILE )
                                {
                                    App.getMapData().spawnerPositions.add( descriptor._POSITION );
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // TODO: 02/05/2022 - For future use, parse collision areas from tiles.
    @Override
    public void parseTiledCollision()
    {
    }

    @Override
    public void parsePolygonCollision()
    {
        Trace.checkPoint();

        if ( aabbMapObjects != null )
        {
            PhysicsBodyType bodyType;
            short           bodyCategory;
            short           collidesWith;

            for ( MapObject mapObject : aabbMapObjects )
            {
                bodyCategory = Gfx.CAT_NOTHING;
                collidesWith = Gfx.CAT_NOTHING;
                bodyType     = PhysicsBodyType._NONE;

                if ( mapObject instanceof PolygonMapObject )
                {
                    if ( null != mapObject.getName() )
                    {
                    }
                }
            }
        }
    }

    /**
     * Parse the list of RectangleMapObjects which will be used to create
     * collision boxes for Walls, stationary decorations, etc.
     */
    @Override
    public void parseAABBObjects()
    {
        Trace.checkPoint();

        if ( aabbMapObjects != null )
        {
            PhysicsBodyType bodyType;
            short           bodyCategory;
            short           collidesWith;

            for ( MapObject mapObject : aabbMapObjects )
            {
                bodyCategory = Gfx.CAT_NOTHING;
                collidesWith = Gfx.CAT_NOTHING;
                bodyType     = PhysicsBodyType._NONE;

                if ( mapObject instanceof RectangleMapObject )
                {
                    if ( null != mapObject.getName() )
                    {
                        switch ( mapObject.getName() )
                        {
                            case "Ground":
                            {
                                bodyType     = PhysicsBodyType._STATIC;
                                bodyCategory = Gfx.CAT_GROUND;
                                collidesWith = Gfx.CAT_PLAYER
                                    | Gfx.CAT_WEAPON
                                    | Gfx.CAT_VILLAGER
                                    | Gfx.CAT_USABLE
                                    | Gfx.CAT_MOBILE_ENEMY
                                    | Gfx.CAT_STATIC_ENEMY;
                            }
                            break;

                            // Used as turning sensors for wandering enemies.
                            case "Wall":
                            {
                                bodyType     = PhysicsBodyType._STATIC;
                                bodyCategory = Gfx.CAT_WALL;
                                collidesWith = Gfx.CAT_USABLE | Gfx.CAT_MOBILE_ENEMY;
                            }
                            break;

                            case "Jail":
                            {
                                bodyType     = PhysicsBodyType._STATIC;
                                bodyCategory = Gfx.CAT_JAIL;
                                collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_GROUND | Gfx.CAT_MOBILE_ENEMY | Gfx.CAT_WEAPON;
                            }
                            break;

                            case "Door":
                            {
                                bodyType = PhysicsBodyType._STATIC_SENSOR;
                                bodyCategory = Gfx.CAT_INTERACTIVE;
                                collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_GROUND;
                            }
                            break;

                            case "Water":
                            {
                                bodyType     = PhysicsBodyType._STATIC_SENSOR;
                                bodyCategory = Gfx.CAT_INTERACTIVE;
                                collidesWith = Gfx.CAT_PLAYER
                                    | Gfx.CAT_GROUND
                                    | Gfx.CAT_USABLE
                                    | Gfx.CAT_MOBILE_ENEMY;
                            }
                            break;

                            default:
                            {
                                Trace.err( "Unrecognised mapObject name: ", mapObject.getName() );
                            }
                            break;
                        }

                        if ( bodyCategory != Gfx.CAT_NOTHING )
                        {
                            Rectangle rectangle = new Rectangle
                                (
                                    ( ( float ) mapObject.getProperties().get( "x" ) ),
                                    ( ( float ) mapObject.getProperties().get( "y" ) ),
                                    ( ( float ) mapObject.getProperties().get( "width" ) ),
                                    ( ( float ) mapObject.getProperties().get( "height" ) )
                                );

                            if ( mapObject.getName().equals( "Door" ) )
                            {
                                rectangle.y -= 2;
                                rectangle.height += 2;

                                App.getMapData().doorList.add
                                    (
                                        new Doorway
                                            (
                                                rectangle,
                                                ( rectangle.x > ( App.getMapData().mapWidth / 2f ) )
                                                ? Dir._UP
                                                : Dir._DOWN
                                            )
                                    );
                            }
                            else if ( mapObject.getName().equals( "Water" ) )
                            {
                                rectangle.y -= 2;
                                rectangle.height += 2;

                                App.getMapData().waterList.add( rectangle );
                            }
                            else
                            {
                                App.getMapUtils().mapBodiesList.add
                                    (
                                        App.getWorldModel().bodyBuilder.newBody
                                            (
                                                rectangle,
                                                bodyCategory,
                                                collidesWith,
                                                bodyType
                                            )
                                    );
                            }
                        }
                    }
                }
            }

            Trace.info( "doorList      : ", App.getMapData().doorList.size );
            Trace.info( "waterList     : ", App.getMapData().waterList.size );
            Trace.info( "checkPointList: ", App.getMapData().checkPointList.size );
            Trace.info( "ladderList    : ", App.getMapData().ladderList.size );
        }
    }

    private static PolygonShape getPolygon( PolygonMapObject polygonObject )
    {
        PolygonShape polygon  = new PolygonShape();
        float[]      vertices = polygonObject.getPolygon().getTransformedVertices();

        float[] worldVertices = new float[ vertices.length ];

        for ( int i = 0; i < vertices.length; ++i )
        {
            worldVertices[ i ] = vertices[ i ] / Gfx._PPM;
        }

        polygon.set( worldVertices );

        return polygon;
    }

    @Override
    public SpriteDescriptor createPlacementTile( MapObject mapObject, @NotNull SpriteDescriptor descriptor )
    {
        descriptor._POSITION.x = ( int ) ( ( ( TiledMapTileMapObject ) mapObject ).getX() / App.getMapData().tileWidth );
        descriptor._POSITION.y = ( int ) ( ( ( TiledMapTileMapObject ) mapObject ).getY() / App.getMapData().tileHeight );
        descriptor._DIST       = new SimpleVec2F();
        descriptor._DIR        = new Direction();
        descriptor._SPEED      = new SimpleVec2F();

        ObjectTileProperties properties = setObjectTileProperties( descriptor );

        if ( properties.hasDistance )
        {
            descriptor._DIST.set(
                ( ( int ) mapObject.getProperties().get( "xdistance" ) ),
                ( ( int ) mapObject.getProperties().get( "ydistance" ) )
            );
        }

        if ( properties.hasDirection )
        {
            descriptor._DIR.set(
                mapObject.getProperties().get( "xdirection" )
                    .equals( "right" ) ? Movement._DIRECTION_RIGHT :
                mapObject.getProperties().get( "xdirection" )
                    .equals( "left" ) ? Movement._DIRECTION_LEFT : Movement._DIRECTION_STILL,

                mapObject.getProperties().get( "ydirection" )
                    .equals( "up" ) ? Movement._DIRECTION_UP :
                mapObject.getProperties().get( "ydirection" )
                    .equals( "down" ) ? Movement._DIRECTION_DOWN : Movement._DIRECTION_STILL
            );
        }

        if ( properties.hasBoundsBox )
        {
            descriptor._BOX = new Box
                (
                    ( int ) mapObject.getProperties().get( "width" ),
                    ( int ) mapObject.getProperties().get( "height" )
                );
        }

        if ( properties.hasSpeed )
        {
            descriptor._SPEED.set(
                ( ( float ) mapObject.getProperties().get( "xspeed" ) ),
                ( ( float ) mapObject.getProperties().get( "yspeed" ) )
            );
        }

        if ( properties.isLinked )
        {
            //
            // Fetch the link ID of the attached entity
            if ( mapObject.getProperties().get( "connection" ) != null )
            {
                descriptor._LINK = ( int ) mapObject.getProperties().get( "connection" );
            }
        }

        descriptor._INDEX = App.getMapData().placementTiles.size;

        return descriptor;
    }

    /**
     * Sets the {@link ObjectTileProperties} for the tile described
     * in the supplied SpriteDescriptor.
     */
    @Override
    public ObjectTileProperties setObjectTileProperties( @NotNull SpriteDescriptor descriptor )
    {
        ObjectTileProperties properties = new ObjectTileProperties();

        switch ( descriptor._TILE )
        {
            case _BAT_TILE:
            case _BEAST_TILE:
            case _FROG_TILE:
            case _RABBIT_TILE:
            case _FISH_TILE:
            case _MUSHROOM_TILE:
            case _MOVING_PLATFORM_TILE:
            case _SMALL_MOVING_PLATFORM_TILE:
            case _BLOCK_6X6_TILE:
            case _CROW_MAN_TILE:
            case _WORM_MAN_TILE:
            case _GOBLIN_TILE:
            case _MUSHROOM_MONSTER_TILE:
            case _SPIKEY_TURTLE_TILE:
            case _SPIKES_TILE:
            case _DOWN_SPIKES_TILE:
            case _LEFT_SPIKES_TILE:
            case _RIGHT_SPIKES_TILE:
            {
                properties.hasDirection = true;
                properties.hasDistance  = true;
                properties.hasSpeed     = true;
            }
            break;

            case _DROP_BLOCK_TILE:
            {
                properties.hasDistance = true;
            }
            break;

            case _SPAWNER_TILE:
            default:
            {
            }
            break;
        }

        return properties;
    }

    /**
     * For the currently loaded TMX map file:-
     * - create the tile layer references.
     * - set map properties ( tile width/height, map width/height etc. ).
     */
    @Override
    public void setGameLevelMap()
    {
        gameTilesLayer1    = ( TiledMapTileLayer ) currentMap.getLayers().get( App.getMapData().mapLayerNames[ _LAYER1 ] );
        gameTilesLayer2    = ( TiledMapTileLayer ) currentMap.getLayers().get( App.getMapData().mapLayerNames[ _LAYER2 ] );
        gameTilesLayer3    = ( TiledMapTileLayer ) currentMap.getLayers().get( App.getMapData().mapLayerNames[ _LAYER3 ] );
        gameTilesLayer4    = ( TiledMapTileLayer ) currentMap.getLayers().get( App.getMapData().mapLayerNames[ _LAYER4 ] );
        overlaysTilesLayer = ( TiledMapTileLayer ) currentMap.getLayers().get( App.getMapData().mapLayerNames[ _OVERLAYS ] );

        objectMarkerTilesLayer = currentMap.getLayers().get( App.getMapData().mapLayerNames[ _OBJECT_MARKERS ] ).getObjects();
        aabbMapObjects         = currentMap.getLayers().get( App.getMapData().mapLayerNames[ _COLLISION_LAYER ] ).getObjects();

        App.getMapData().tileWidth  = currentMap.getProperties().get( "tilewidth", Integer.class );
        App.getMapData().tileHeight = currentMap.getProperties().get( "tileheight", Integer.class );
        App.getMapData().mapWidth   = ( currentMap.getProperties().get( "width", Integer.class ) * App.getMapData().tileWidth );
        App.getMapData().mapHeight  = ( currentMap.getProperties().get( "height", Integer.class ) * App.getMapData().tileHeight );

        App.getMapData().maxScroll.x = App.getMapData().mapWidth - Gfx._VIEW_WIDTH;
        App.getMapData().maxScroll.y = App.getMapData().mapHeight - Gfx._VIEW_HEIGHT;
        App.getMapData().minScroll.x = 0;
        App.getMapData().minScroll.y = 0;

        App.getMapData().previousMapPosition.set
            (
                App.getMapData().mapPosition.getX(),
                App.getMapData().mapPosition.getY()
            );

        App.getMapData().mapBox.set
            (
                0,
                0,
                App.getMapData().mapWidth,
                App.getMapData().mapHeight
            );
    }

    /**
     * Draws the GameTilesLayers.
     *
     * @param camera The OrthoGameCamera to use, usually
     *               the TiledMapCamera.
     */
    @Override
    public void render( OrthographicCamera camera )
    {
        if ( gameTilesLayer1 != null )
        {
            mapRenderer.setView( camera );
            mapRenderer.renderTileLayer( gameTilesLayer1 );
            mapRenderer.renderTileLayer( gameTilesLayer2 );
            mapRenderer.renderTileLayer( gameTilesLayer3 );
            mapRenderer.renderTileLayer( gameTilesLayer4 );
        }
    }

    /**
     * Draws an 'overlay tile layer'.
     * This layer is used to draw tiles which will appear
     * on top of sprites, allowing for sprites passing behind
     * objects.
     *
     * @param camera The OrthoGameCamera to use, usually
     *               the TiledMapCamera.
     */
    @Override
    public void drawOverlayLayer( OrthographicCamera camera )
    {
        if ( overlaysTilesLayer != null )
        {
            mapRenderer.setView( camera );
            mapRenderer.renderTileLayer( overlaysTilesLayer );

            if ( !App.getAppConfig().gamePaused )
            {
                AnimatedTiledMapTile.updateAnimationBaseTime();
            }
        }
    }

    @Override
    public TiledMap getCurrentMap()
    {
        return currentMap;
    }

    @Override
    public void dispose()
    {
        mapRenderer.dispose();

        tmxMapLoader           = null;
        gameTilesLayer1        = null;
        gameTilesLayer2        = null;
        gameTilesLayer3        = null;
        gameTilesLayer4        = null;
        overlaysTilesLayer     = null;
        objectMarkerTilesLayer = null;
        aabbMapObjects         = null;
        currentMap             = null;
        currentMapName         = null;
        mapRenderer            = null;
    }
}
