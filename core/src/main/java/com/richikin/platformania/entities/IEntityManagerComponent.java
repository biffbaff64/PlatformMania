package com.richikin.platformania.entities;

import com.badlogic.gdx.utils.Disposable;
import com.richikin.platformania.enums.GraphicID;

public interface IEntityManagerComponent extends Disposable
{
    void init();

    void update();

    void create();

    void free();

    void reset();

    int getActiveCount();

    void setActiveCount( int numActive );

    void addMaxCount( int add );

    void setMaxCount( int max );

    GraphicID getGID();
}
