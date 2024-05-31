package com.richikin.platformania.entities.managers;

import com.badlogic.gdx.utils.Array;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.core.App;
import com.richikin.platformania.entities.EntityStats;
import com.richikin.platformania.entities.actors.Pickup;
import com.richikin.platformania.entities.actors.TreasureChest;
import com.richikin.platformania.entities.objects.SpriteDescriptor;

public class PickupManager extends BasicEntityManager
{
    public PickupManager()
    {
        super( GraphicID._PICKUPS_MANAGER );
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
        EntityManagerUtils                    managerUtils = new EntityManagerUtils();
        Array<SpriteDescriptor>               descriptors  = managerUtils.getDescriptorListForType( GraphicID._PICKUP );
        Array.ArrayIterator<SpriteDescriptor> iter         = new Array.ArrayIterator<>( descriptors );

        while ( iter.hasNext() )
        {
            SpriteDescriptor descriptor = iter.next();

            if ( descriptor._GID == GraphicID.G_TREASURE_CHEST )
            {
                TreasureChest chest = new TreasureChest();
                chest.initialise( descriptor );

                App.getEntityData().addEntity( chest );
            }
            else
            {
                Pickup pickup = new Pickup( descriptor._GID );
                pickup.initialise( descriptor );
                pickup.canAutoCollect = false;

                App.getEntityData().addEntity( pickup );
            }

            EntityStats.log( descriptor._GID );
        }
    }
}
