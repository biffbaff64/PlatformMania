package com.richikin.platformania.physics.aabb;

import com.richikin.platformania.physics.CollisionObject;

public interface AABBCollision
{
    boolean checkAABBBoxes( CollisionObject boxA );

    void dispose();
}
