package com.richikin.platformania.entities.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.richikin.platformania.enums.GraphicID;

public class IdentityComponent implements Component
{
    public Sprite    sprite;
    public boolean   isDrawable;
    public GraphicID gid;
    public GraphicID type;
    public int       spriteNumber;
    public boolean   isMainCharacter;
}
