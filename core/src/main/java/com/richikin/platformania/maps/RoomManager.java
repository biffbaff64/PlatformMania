package com.richikin.platformania.maps;

import com.badlogic.gdx.utils.Array;
import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.maths.SimpleVec2;
import com.richikin.platformania.core.App;
import com.richikin.platformania.entities.objects.SpriteDescriptor;

public class RoomManager
{
    private static final String ROOM_001 = "map1.tmx";
    private static final String ROOM_002 = "map2.tmx";
    private static final String ROOM_003 = "map3.tmx";
    private static final String ROOM_004 = "map4.tmx";
    private static final String ROOM_005 = "map5.tmx";

    private static final String ROOM_006 = "map1.tmx";
    private static final String ROOM_007 = "map1.tmx";
    private static final String ROOM_008 = "map1.tmx";
    private static final String ROOM_009 = "map1.tmx";
    private static final String ROOM_010 = "map1.tmx";
    private static final String ROOM_011 = "map1.tmx";
    private static final String ROOM_012 = "map1.tmx";
    private static final String ROOM_013 = "map1.tmx";
    private static final String ROOM_014 = "map1.tmx";
    private static final String ROOM_015 = "map1.tmx";
    private static final String ROOM_016 = "map1.tmx";
    private static final String ROOM_017 = "map1.tmx";
    private static final String ROOM_018 = "map1.tmx";
    private static final String ROOM_019 = "map1.tmx";
    private static final String ROOM_020 = "map1.tmx";

    private static final String TEST_ROOM = "map1.tmx";

    private static final String _MAPS_PATH = "maps/";

    //@formatter:off
    private final Room[][] roomMap =
        {
            // -----------------------------------------
            { null },
            // -----------------------------------------
            // Level testing, comment out when not using.
//            { new Room(TEST_ROOM) },
            // -----------------------------------------
            { new Room(ROOM_001) },
            { new Room(ROOM_002) },
            { new Room(ROOM_003) },
            { new Room(ROOM_004) },
            { new Room(ROOM_005) },
            { new Room(ROOM_006) },
            { new Room(ROOM_007) },
            { new Room(ROOM_008) },
            { new Room(ROOM_009) },
            { new Room(ROOM_010) },
            { new Room(ROOM_011) },
            { new Room(ROOM_012) },
            { new Room(ROOM_013) },
            { new Room(ROOM_014) },
            { new Room(ROOM_015) },
            { new Room(ROOM_016) },
            { new Room(ROOM_017) },
            { new Room(ROOM_018) },
            { new Room(ROOM_019) },
            { new Room(ROOM_020) },

            // -----------------------------------------
            { null },
            // -----------------------------------------
     };
    //@formatter:on

    private final int  worldWidth;
    private final int  worldHeight;
    public        Room activeRoom;

    public RoomManager()
    {
        Trace.checkPoint();

        worldWidth  = roomMap[ 0 ].length;
        worldHeight = roomMap.length;
    }

    public void initialise()
    {
        Trace.checkPoint();

        activeRoom = new Room();

        setRoom( App.getLevel(), 0 );
    }

    public void setRoom( int row, int column )
    {
        if ( roomMap[ row ][ column ] != null )
        {
            activeRoom.set( roomMap[ row ][ column ] );

            activeRoom.row    = row;
            activeRoom.column = column;
        }
    }

    private SimpleVec2 findRoom( String roomName )
    {
        SimpleVec2 roomPosition = null;

        for ( int row = 0; row < worldHeight && ( roomPosition == null ); row++ )
        {
            for ( int column = 0; column < worldWidth && ( roomPosition == null ); column++ )
            {
                if ( roomMap[ row ][ column ] != null )
                {
                    if ( roomName.equals( roomMap[ row ][ column ].roomName ) )
                    {
                        roomPosition = new SimpleVec2( row, column );
                    }
                }
            }
        }

        return roomPosition;
    }

    public String getActiveRoomName()
    {
        String name = "null";

        if ( roomMap[ activeRoom.row ][ activeRoom.column ] != null )
        {
            name = roomMap[ activeRoom.row ][ activeRoom.column ].roomName;
        }

        return name;
    }

    public String getCurrentMapNameWithPath()
    {
        return _MAPS_PATH + roomMap[ activeRoom.row ][ activeRoom.column ].roomName;
    }

    public void setPlayerStart()
    {
        if ( App.getPlayer().getActionState() == ActionStates._RESETTING )
        {
            // Position the player at the most recent checkpoint
            App.getPlayer().initXYZ.setX( CheckPoint.getX() * App.getMapData().tileWidth );
            App.getPlayer().initXYZ.setY( CheckPoint.getY() * App.getMapData().tileHeight );
            App.getPlayer().initXYZ.setZ( App.getEntityUtils().getInitialZPosition( GraphicID.G_PLAYER ) );
        }
        else
        {
            for ( SpriteDescriptor descriptor : new Array.ArrayIterator<>( App.getMapData().placementTiles ) )
            {
                if ( descriptor._GID == GraphicID.G_PLAYER )
                {
                    App.getPlayer().initXYZ.setX( descriptor._POSITION.getX() * App.getMapData().tileWidth );
                    App.getPlayer().initXYZ.setY( descriptor._POSITION.getY() * App.getMapData().tileHeight );
                    App.getPlayer().initXYZ.setZ( App.getEntityUtils().getInitialZPosition( GraphicID.G_PLAYER ) );
                }
            }
        }
    }
}
