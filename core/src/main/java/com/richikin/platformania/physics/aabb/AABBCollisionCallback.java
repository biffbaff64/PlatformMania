package com.richikin.platformania.physics.aabb;

import com.richikin.platformania.physics.CollisionObject;

public interface AABBCollisionCallback
{
    void onPositiveCollision( CollisionObject cobjHitting );

    void onNegativeCollision();
}
