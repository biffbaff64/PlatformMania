package com.richikin.platformania.entities.managers;

import com.badlogic.gdx.utils.Array;
import com.richikin.platformania.core.App;
import com.richikin.platformania.entities.objects.SpriteDescriptor;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.maths.Vec3;

public class EntityManagerUtils
{
    public Array<SpriteDescriptor> getDescriptorListForGID( final GraphicID targetGID )
    {
        Array<SpriteDescriptor> tiles = new Array<>();

        Array.ArrayIterator<SpriteDescriptor> iter = App.getMapUtils().getPlacementTilesIterator();

        while ( iter.hasNext() )
        {
            SpriteDescriptor descriptor = iter.next();

            if ( descriptor._GID == targetGID )
            {
                tiles.add( descriptor );
            }
        }

        return tiles;
    }

    public Array<SpriteDescriptor> getDescriptorListForType( final GraphicID targetType )
    {
        Array<SpriteDescriptor> tiles = new Array<>();

        Array.ArrayIterator<SpriteDescriptor> iter = App.getMapUtils().getPlacementTilesIterator();

        while ( iter.hasNext() )
        {
            SpriteDescriptor descriptor = iter.next();

            if ( descriptor._TYPE == targetType )
            {
                tiles.add( descriptor );
            }
        }

        return tiles;
    }

    public Array<Vec3> findMultiCoordinates( final GraphicID targetGID )
    {
        Array<Vec3> coords = new Array<>();

        Array.ArrayIterator<SpriteDescriptor> iter = App.getMapUtils().getPlacementTilesIterator();

        while ( iter.hasNext() )
        {
            SpriteDescriptor descriptor = iter.next();

            if ( descriptor._GID == targetGID )
            {
                coords.add( new Vec3( descriptor._POSITION.x, descriptor._POSITION.y, 0 ) );
            }
        }

        return coords;
    }

    public Vec3 findCoordinate( final GraphicID targetGID )
    {
        Vec3 coords = new Vec3();

        Array.ArrayIterator<SpriteDescriptor> iter = App.getMapUtils().getPlacementTilesIterator();

        while ( iter.hasNext() )
        {
            SpriteDescriptor descriptor = iter.next();

            if ( descriptor._GID == targetGID )
            {
                coords.x = descriptor._POSITION.x;
                coords.y = descriptor._POSITION.y;
                coords.z = 0;
            }
        }

        return coords;
    }
}

