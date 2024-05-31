package com.richikin.platformania.entities.components;

import com.badlogic.ashley.core.Component;
import com.richikin.platformania.maths.SimpleVec3F;

public class TransformComponent implements Component
{
    public final SimpleVec3F position = new SimpleVec3F();
    public       SimpleVec3F initXYZ  = new SimpleVec3F();

    public boolean isFlippedX = false;
    public boolean isFlippedY = false;
    public boolean canFlip    = false;

    public boolean isRotating  = false;
    public float   rotateSpeed = 0.0f;
    public float   rotation    = 0.0f;
}
