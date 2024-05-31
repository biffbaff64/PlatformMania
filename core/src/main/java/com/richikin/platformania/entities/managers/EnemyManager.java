package com.richikin.platformania.entities.managers;

import com.badlogic.gdx.utils.Array;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.core.App;
import com.richikin.platformania.entities.EntityStats;
import com.richikin.platformania.entities.actors.enemies.*;
import com.richikin.platformania.entities.objects.FixedPathEntity;
import com.richikin.platformania.entities.objects.SpriteDescriptor;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.physics.PhysicsBodyType;

public class EnemyManager extends BasicEntityManager
{
    //@formatter:off
    private final GraphicID[] enemies =
        {
            GraphicID.G_BAT,
            GraphicID.G_BEAST,
            GraphicID.G_MUSHROOM,
            GraphicID.G_PLANT,
            GraphicID.G_SPIKEY_TURTLE,
            GraphicID.G_FROG,
            GraphicID.G_RABBIT,
            GraphicID.G_GOBLIN,
            GraphicID.G_MUSHROOM_MONSTER,
        };
    //@formatter:on

    public EnemyManager()
    {
        super( GraphicID._ENEMY_MANAGER );
    }

    /**
     * Initialises all enemy tracking objects.
     */
    @Override
    public void init()
    {
        super.init();

        for ( GraphicID item : enemies )
        {
            create( item );
        }
    }

    /**
     * Update all enemy trackers.
     * If the number of active enemies of a type is less than the max allowed,
     * a new enemy will be spawned.
     */
    @Override
    public void update()
    {
    }

    /**
     * Spawn an enemy of the specified type.
     * WIP
     */
    private void create( GraphicID graphicID )
    {
        EntityManagerUtils managerUtils = new EntityManagerUtils();

        // Obtain a list of descriptors for this enemy type
        Array< SpriteDescriptor > itemList = managerUtils.getDescriptorListForGID( graphicID );

        Array.ArrayIterator< SpriteDescriptor > iter = new Array.ArrayIterator<>( itemList );

        while ( iter.hasNext() )
        {
            SpriteDescriptor descriptor = iter.next();

            switch ( graphicID )
            {
                case G_BAT:
                {
                    Bat bat = new Bat();
                    bat.initialise( descriptor );
                    App.getEntityData().addEntity( bat );
                }
                break;

                case G_BEAST:
                {
                    Beast beast = new Beast();
                    beast.initialise( descriptor );
                    App.getEntityData().addEntity( beast );
                }
                break;

                case G_RABBIT:
                {
                    Rabbit rabbit = new Rabbit();
                    rabbit.initialise( descriptor );
                    App.getEntityData().addEntity( rabbit );
                }
                break;

                case G_FROG:
                {
                    FixedPathEntity entity = new FixedPathEntity( graphicID );
                    entity.initialise( descriptor );
                    App.getEntityData().addEntity( entity );
                }
                break;

                case G_PLANT:
                {
                    Plant plant = new Plant();
                    plant.initialise( descriptor );
                    App.getEntityData().addEntity( plant );
                }
                break;

                case G_MUSHROOM:
                {
                    Mushroom mushroom = new Mushroom();
                    mushroom.initialise( descriptor );
                    mushroom.isJumper = true;
                    App.getEntityData().addEntity( mushroom );
                }
                break;

                case G_SPIKEY_TURTLE:
                {
                    SpikeyTurtle turtle = new SpikeyTurtle();
                    turtle.initialise( descriptor );
                    App.getEntityData().addEntity( turtle );
                }
                break;

                case G_GOBLIN:
                case G_MUSHROOM_MONSTER:
                {
                    PatrollingEnemy entity = new PatrollingEnemy( graphicID );
                    entity.setFilters( Gfx.CAT_MOBILE_ENEMY, Gfx.CAT_GROUND );
                    entity.setRequestedBodyType( PhysicsBodyType._KINEMATIC );
                    entity.initialise( descriptor );
                    entity.shouldPauseAtBoundsTurn = true;
                    App.getEntityData().addEntity( entity );
                }
                break;

                default:
                    break;
            }

            EntityStats.log( graphicID );
        }
    }
}
