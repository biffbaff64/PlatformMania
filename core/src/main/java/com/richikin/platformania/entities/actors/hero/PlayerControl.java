package com.richikin.platformania.entities.actors.hero;

import com.richikin.platformania.physics.Dir;

public interface PlayerControl
{
    void checkButtons();

    void setDirection(Dir _direction);
}
