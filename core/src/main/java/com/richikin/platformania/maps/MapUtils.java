package com.richikin.platformania.maps;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.enums.TileID;
import com.richikin.platformania.maths.SimpleVec2;
import com.richikin.platformania.maths.SimpleVec2F;
import com.richikin.platformania.physics.Direction;
import com.richikin.platformania.physics.Movement;
import com.richikin.platformania.core.App;
import com.richikin.platformania.entities.Entities;
import com.richikin.platformania.entities.objects.SpriteDescriptor;

public class MapUtils implements Disposable
{
    public Array< Body > mapBodiesList;
    public SimpleVec2    demoPosition;

    private Direction   demoDir;
    private SimpleVec2F demoSpeed;

    public MapUtils()
    {
        mapBodiesList = new Array<>();
    }

    public Array< SpriteDescriptor > findMultiTiles( final GraphicID targetGID )
    {
        Array< SpriteDescriptor > tiles = new Array<>();

        Array.ArrayIterator< SpriteDescriptor > iter = getPlacementTilesIterator();

        while ( iter.hasNext() )
        {
            SpriteDescriptor marker = iter.next();

            if ( marker._GID == targetGID )
            {
                tiles.add( marker );
            }
        }

        return tiles;
    }

    public void destroyBodies()
    {
        for ( Body body : mapBodiesList )
        {
            App.getWorldModel().box2DWorld.destroyBody( body );
        }
    }

    /**
     * Scans through {@link Entities#entityList} to find the
     * {@link GraphicID} which matches the supplied {@link TileID}.
     */
    public GraphicID tileToGID( TileID tileID )
    {
        GraphicID gid   = GraphicID.G_NO_ID;
        int       index = 0;

        while ( ( index < App.getEntities().entityList.length ) && ( gid == GraphicID.G_NO_ID ) )
        {
            if ( App.getEntities().entityList[ index ]._TILE == tileID )
            {
                gid = App.getEntities().entityList[ index ]._GID;
            }

            index++;
        }

        return gid;
    }

    /**
     * Gets the iterator for the placement tiles array.
     */
    public Array.ArrayIterator< SpriteDescriptor > getPlacementTilesIterator()
    {
        return new Array.ArrayIterator<>( App.getMapData().placementTiles );
    }

    /**
     * For Demo use only.
     * Bounce scrolls the viewport around the map.
     */
    public void demoScroll()
    {
        if ( demoDir == null )
        {
            demoDir      = new Direction( Movement._DIRECTION_RIGHT, Movement._DIRECTION_UP );
            demoSpeed    = new SimpleVec2F( 1, 1 );
            demoPosition = new SimpleVec2( App.getMapData().mapPosition );
        }

        if ( demoDir.getX() == Movement._DIRECTION_LEFT )
        {
            if ( demoPosition.getX() <= App.getMapData().minScroll.x )
            {
                demoDir.toggleX();
            }
        }
        else if ( demoDir.getX() == Movement._DIRECTION_RIGHT )
        {
            if ( demoPosition.getX() >= App.getMapData().maxScroll.x )
            {
                demoDir.toggleX();
            }
        }

        if ( demoDir.getY() == Movement._DIRECTION_DOWN )
        {
            if ( demoPosition.getY() <= App.getMapData().minScroll.y )
            {
                demoDir.toggleY();
            }
        }
        else if ( demoDir.getY() == Movement._DIRECTION_UP )
        {
            if ( demoPosition.getY() >= App.getMapData().maxScroll.y )
            {
                demoDir.toggleY();
            }
        }

        demoPosition.addX( ( int ) ( demoSpeed.getX() * demoDir.getX() ) );
        demoPosition.addY( ( int ) ( demoSpeed.getY() * demoDir.getY() ) );

        App.getMapData().positionAt( demoPosition.getX(), demoPosition.getY() );
    }

    @Override
    public void dispose()
    {
        if ( mapBodiesList != null )
        {
            mapBodiesList.clear();
            mapBodiesList = null;
        }

        demoDir      = null;
        demoSpeed    = null;
        demoPosition = null;
    }
}
