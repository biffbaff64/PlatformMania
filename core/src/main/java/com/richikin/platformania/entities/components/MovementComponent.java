package com.richikin.platformania.entities.components;

import com.badlogic.ashley.core.Component;
import com.richikin.platformania.maths.SimpleVec2F;
import com.richikin.platformania.physics.Direction;

public class MovementComponent implements Component
{
    public Direction   direction;
    public Direction   lookingAt;
    public SimpleVec2F distance;
    public SimpleVec2F speed;
}
