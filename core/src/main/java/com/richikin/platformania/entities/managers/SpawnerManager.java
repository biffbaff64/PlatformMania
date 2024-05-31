package com.richikin.platformania.entities.managers;

import com.badlogic.gdx.math.MathUtils;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.maths.Vec2;
import com.richikin.platformania.core.App;
import com.richikin.platformania.entities.EntityStats;
import com.richikin.platformania.entities.actors.enemies.Spawner;
import com.richikin.platformania.entities.objects.SpriteDescriptor;

public class SpawnerManager extends BasicEntityManager
{
    public SpawnerManager()
    {
        super( GraphicID._SPAWNER_MANAGER );
    }

    @Override
    public void init()
    {
        Trace.checkPoint();

        super.init();

        maxCount = App.getMapData().spawnerPositions.size * 2;
    }

    @Override
    public void update()
    {
        if ( activeCount < maxCount )
        {
            create();
        }
    }

    @Override
    public void create()
    {
        SpriteDescriptor descriptor = App.getEntities().getDescriptor( GraphicID.G_SPAWNER );

        Vec2 vec2 = App.getMapData().spawnerPositions.get
            (
                MathUtils.random( App.getMapData().spawnerPositions.size - 1 )
            );

        descriptor._POSITION.setX( vec2.x );
        descriptor._POSITION.setY( vec2.y );

        Spawner spawner = new Spawner();
        spawner.initialise( descriptor );

        App.getEntityData().addEntity( spawner );

        EntityStats.log( GraphicID.G_SPAWNER );

        activeCount++;
    }
}
