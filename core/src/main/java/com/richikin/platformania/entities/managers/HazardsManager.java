package com.richikin.platformania.entities.managers;

import com.badlogic.gdx.utils.Array;
import com.richikin.platformania.core.App;
import com.richikin.platformania.entities.EntityStats;
import com.richikin.platformania.entities.actors.hazards.DropBlock;
import com.richikin.platformania.entities.actors.hazards.Spike;
import com.richikin.platformania.entities.objects.SpriteDescriptor;
import com.richikin.platformania.enums.GraphicID;

public class HazardsManager extends BasicEntityManager
{
    private final GraphicID[] hazards =
        {
            GraphicID.G_SPIKES,
            GraphicID.G_DOWN_SPIKES,
            GraphicID.G_LEFT_SPIKES,
            GraphicID.G_RIGHT_SPIKES,
            GraphicID.G_SMALL_SPIKES,
            GraphicID.G_DROP_BLOCK,
        };

    public HazardsManager()
    {
        super( GraphicID._HAZARDS_MANAGER );
    }

    @Override
    public void init()
    {
        super.init();

        create();
    }

    @Override
    public void create()
    {
        EntityManagerUtils managerUtils = new EntityManagerUtils();

        for ( GraphicID item : hazards )
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
                    case G_SPIKES:
                    case G_SMALL_SPIKES:
                    case G_DOWN_SPIKES:
                    case G_LEFT_SPIKES:
                    case G_RIGHT_SPIKES:
                    {
                        Spike spike = new Spike();
                        spike.initialise( descriptor );
                        App.getEntityData().addEntity( spike );
                    }
                    break;

                    case G_DROP_BLOCK:
                    {
                        DropBlock block = new DropBlock();
                        block.initialise( descriptor );
                        App.getEntityData().addEntity( block );
                    }
                    break;

                    default:
                        break;
                }

                EntityStats.log( descriptor._GID );
            }
        }
    }
}
