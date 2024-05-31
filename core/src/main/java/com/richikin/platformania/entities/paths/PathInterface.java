package com.richikin.platformania.entities.paths;

import com.richikin.platformania.entities.objects.GdxSprite;

public interface PathInterface
{
    void reset();

    void setNextMove(GdxSprite spriteObject);
}
