package com.richikin.platformania.entities.managers;

import com.badlogic.gdx.utils.Array;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.physics.box2d.B2DConstants;
import com.richikin.platformania.core.App;
import com.richikin.platformania.entities.EntityStats;
import com.richikin.platformania.entities.actors.Fish;
import com.richikin.platformania.entities.objects.SimpleEntity;
import com.richikin.platformania.entities.objects.SpriteDescriptor;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.physics.PhysicsBodyType;

public class DecorationsManager extends BasicEntityManager
{
    private final GraphicID[] decorationsList =
        {
            GraphicID.G_EYES,
            GraphicID.G_FLAMES,
            GraphicID.G_BEES,
            GraphicID.G_FISH,
        };

    public DecorationsManager()
    {
        super( GraphicID._DECORATIONS_MANAGER );
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

        for ( GraphicID item : decorationsList )
        {
            // Obtain a list of descriptors for the next GraphicID to process
            Array<SpriteDescriptor> tiles = managerUtils.getDescriptorListForGID( item );

            // Obtain the list iterator
            Array.ArrayIterator<SpriteDescriptor> iter = new Array.ArrayIterator<>( tiles );

            if ( tiles.size > 0 )
            {
                while ( iter.hasNext() )
                {
                    SpriteDescriptor descriptor = iter.next();

                    descriptor._ASSET = checkAssetName( descriptor );

                    if ( descriptor._GID == GraphicID.G_FISH )
                    {
                        Fish decoration = new Fish();
                        decoration.isFlying = true;
                        decoration.initialise( descriptor );
                        decoration.getPhysicsBody().body.setGravityScale( B2DConstants.ZERO_GRAVITY );

                        App.getEntityData().addEntity( decoration );
                    }
                    else
                    {
                        SimpleEntity decoration = new SimpleEntity( descriptor._GID );

                        if ( descriptor._GID == GraphicID.G_FLAMES )
                        {
                            decoration.setFilters
                                (
                                    Gfx.CAT_STATIC_ENEMY,
                                    ( short ) (Gfx.CAT_PLAYER | Gfx.CAT_GROUND)
                                );
                            decoration.setRequestedBodyType( PhysicsBodyType._STATIC );
                        }
                        else if ( descriptor._GID == GraphicID.G_FISH )
                        {
                            decoration.setFilters( Gfx.CAT_NOTHING, Gfx.CAT_NOTHING );
                            decoration.setRequestedBodyType( PhysicsBodyType._KINEMATIC_SENSOR );
                        }
                        else
                        {
                            decoration.setFilters( Gfx.CAT_NOTHING, Gfx.CAT_NOTHING );
                            decoration.setRequestedBodyType( PhysicsBodyType._STATIC_SENSOR );
                        }

                        decoration.initialise( descriptor );

                        App.getEntityData().addEntity( decoration );
                    }

                    EntityStats.log( descriptor._GID );
                }
            }
        }
    }

    private String checkAssetName( SpriteDescriptor descriptor )
    {
        // See Dungeons project for example

        return descriptor._ASSET;
    }
}
