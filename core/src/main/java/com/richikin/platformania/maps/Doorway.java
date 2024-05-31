package com.richikin.platformania.maps;

import com.badlogic.gdx.math.Rectangle;
import com.richikin.platformania.physics.Dir;

public class Doorway
{
    public Rectangle box;
    public Dir nextDir;

    public Doorway( Rectangle box, Dir nextDir )
    {
        this.box     = box;
        this.nextDir = nextDir;
    }
}
