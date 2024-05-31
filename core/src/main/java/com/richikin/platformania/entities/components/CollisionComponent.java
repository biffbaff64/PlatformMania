package com.richikin.platformania.entities.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Body;

public class CollisionComponent implements Component
{
    public Body   b2dBody;
    public short  bodyCategory;
    public short  collidesWith;
    public Entity collisionEntity;
}
