package com.richikin.platformania.entities;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.logging.Trace;

public class EntityData implements Disposable
{
    private Array< IEntityComponent >        entityMap;
    private Array< IEntityManagerComponent > managerList;

    public EntityData()
    {
        entityMap   = new Array<>();
        managerList = new Array<>();
    }

    /**
     * Adds an IEntityComponent to the entityMap.
     */
    public void addEntity( IEntityComponent entity )
    {
        if ( entity != null )
        {
            entityMap.add( entity );
        }
        else
        {
            throw new NullPointerException
                (
                    "***** Attempt to add NULL Object, EntityMap current size: "
                        + entityMap.size
                );
        }
    }

    /**
     * Add an IEntityManagerComponent to the manager list.
     *
     * @param manager - The entity manager to add.
     */
    public void addManager( IEntityManagerComponent manager )
    {
        if ( manager != null )
        {
            managerList.add( manager );
        }
        else
        {
            throw new NullPointerException
                (
                    "***** Attempt to add NULL Object, ManagerList current size: "
                        + managerList.size
                );
        }
    }

    /**
     * Gets the entity from entityMap at the supplied index.
     */
    public IEntityComponent getEntity( int index )
    {
        return entityMap.get( index );
    }

    /**
     * Gets the entity manager at the specified array index.
     */
    public IEntityManagerComponent getManager( GraphicID managerID )
    {
        int index = 0;

        for ( int i = 0; i < managerList.size; i++ )
        {
            if ( managerList.get( i ).getGID() == managerID )
            {
                index = i;
            }
        }

        return managerList.get( index );
    }

    /**
     * Remove the entity at the supplied index from entityMap.
     */
    public void removeEntityAt( int index )
    {
        entityMap.removeIndex( index );
    }

    /**
     * Removes the specified manager from the manager array.
     * @param manager The manager to remove.
     */
    public void removeManager( IEntityManagerComponent manager )
    {
        if ( managerList.removeValue( manager, false ) )
        {
            Trace.err( "FAILED to remove ", manager.getGID().name() );
        }
    }

    public Array< IEntityComponent > getEntityMap()
    {
        return entityMap;
    }

    public Array< IEntityManagerComponent > getManagerList()
    {
        return managerList;
    }

    @Override
    public void dispose()
    {
        Trace.checkPoint();

        for ( IEntityComponent component : entityMap )
        {
            component.dispose();
        }

        for ( IEntityManagerComponent component : managerList )
        {
            component.dispose();
        }

        entityMap.clear();
        managerList.clear();

        entityMap   = null;
        managerList = null;
    }
}
