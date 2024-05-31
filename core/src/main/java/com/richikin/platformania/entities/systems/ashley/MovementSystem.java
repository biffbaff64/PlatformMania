package com.richikin.platformania.entities.systems.ashley;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.richikin.platformania.entities.components.MovementComponent;
import com.richikin.platformania.entities.components.StateComponent;
import com.richikin.platformania.entities.components.TransformComponent;

public class MovementSystem extends IteratingSystem
{
    final ComponentMapper<MovementComponent>  movementMapper;
    final ComponentMapper<StateComponent>     stateMapper;
    final ComponentMapper<TransformComponent> transformMapper;

    public MovementSystem()
    {
        super
            (
                Family.all
                    (
                        MovementComponent.class,
                        StateComponent.class,
                        TransformComponent.class
                    ).get());

        movementMapper  = ComponentMapper.getFor(MovementComponent.class);
        stateMapper     = ComponentMapper.getFor(StateComponent.class);
        transformMapper = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {
    }
}
