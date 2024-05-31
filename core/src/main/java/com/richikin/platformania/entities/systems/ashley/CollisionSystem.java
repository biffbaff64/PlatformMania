package com.richikin.platformania.entities.systems.ashley;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.richikin.platformania.entities.components.CollisionComponent;
import com.richikin.platformania.entities.components.IdentityComponent;
import com.richikin.platformania.enums.GraphicID;

public class CollisionSystem extends IteratingSystem
{
    final ComponentMapper<CollisionComponent> collisionMapper;

    public CollisionSystem()
    {
        super(Family.all(CollisionComponent.class).get());

        collisionMapper = ComponentMapper.getFor(CollisionComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {
        CollisionComponent cc = collisionMapper.get(entity);
        Entity collidedEntity = cc.collisionEntity;

        if (collidedEntity != null)
        {
            GraphicID type = collidedEntity.getComponent(IdentityComponent.class).type;

            switch ( type )
            {
                case _ENEMY:
                case _OBSTACLE:
                    break;
                default:
                    break;
            }
        }
    }
}
