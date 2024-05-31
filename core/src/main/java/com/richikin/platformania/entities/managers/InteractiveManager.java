package com.richikin.platformania.entities.managers;

import com.badlogic.gdx.utils.Array;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.core.App;
import com.richikin.platformania.entities.EntityStats;
import com.richikin.platformania.entities.actors.Villager;
import com.richikin.platformania.entities.actors.interactive.*;
import com.richikin.platformania.entities.objects.SpriteDescriptor;

public class InteractiveManager extends BasicEntityManager
{
    private final GraphicID[] interactives =
        {
            GraphicID.G_MOVING_PLATFORM,
            GraphicID.G_PUSHABLE_BOULDER,
            GraphicID.G_PUSHABLE_CRATE,
            GraphicID.G_PRISONER,
            GraphicID.G_FLOOR_LEVER,
            GraphicID.G_RED_FLAG,
            GraphicID.G_BIG_BLOCK,
        };

    public InteractiveManager()
    {
        super( GraphicID._INTERACTIVE_MANAGER );
    }

    @Override
    public void init()
    {
        super.init();

        create();
    }

    @Override
    public void update()
    {
    }

    @Override
    public void create()
    {
        EntityManagerUtils managerUtils = new EntityManagerUtils();

        for ( GraphicID item : interactives )
        {
            // Obtain a list of descriptors for the next GraphicID to process
            Array< SpriteDescriptor > descriptors = managerUtils.getDescriptorListForGID( item );

            // Obtain the list iterator
            Array.ArrayIterator< SpriteDescriptor > iter = new Array.ArrayIterator<>( descriptors );

            while ( iter.hasNext() )
            {
                SpriteDescriptor descriptor = iter.next();

                switch ( descriptor._GID )
                {
                    case G_MOVING_PLATFORM:
                    case G_BIG_BLOCK:
                    {
                        MovingPlatform platform = new MovingPlatform();
                        platform.initialise( descriptor );
                        App.getEntityData().addEntity( platform );
                    }
                    break;

                    case G_PUSHABLE_BOULDER:
                    {
                        Boulder boulder = new Boulder();
                        boulder.initialise( descriptor );
                        App.getEntityData().addEntity( boulder );
                    }
                    break;

                    case G_PUSHABLE_CRATE:
                    {
                        Crate crate = new Crate();
                        crate.initialise( descriptor );
                        App.getEntityData().addEntity( crate );
                    }
                    break;

                    case G_PRISONER:
                    {
                        Villager villager = new Villager();
                        villager.initialise( descriptor );
                        App.getEntityData().addEntity( villager );
                    }
                    break;

                    case G_FLOOR_LEVER:
                    {
                        FloorLever lever = new FloorLever();
                        lever.initialise( descriptor );
                        App.getEntityData().addEntity( lever );
                    }
                    break;

                    case G_RED_FLAG:
                    {
                        Flag flag = new Flag();
                        flag.initialise( descriptor );
                        App.getEntityData().addEntity( flag );
                    }
                    break;

                    default:
                    {
                        Trace.dbg( "UNSUPPORTED GID: ", descriptor._GID );
                    }
                    break;
                }

                EntityStats.log( descriptor._GID );
            }
        }
    }
}
