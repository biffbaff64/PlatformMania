package com.richikin.platformania.maps;

public class ObjectTileProperties
{
    public boolean hasDirection;
    public boolean hasDistance;
    public       boolean hasSpeed;
    public final boolean isLinked;
    public final boolean hasBoundsBox;
    public final boolean hasData;

    public ObjectTileProperties()
    {
        hasDirection = false;
        hasDistance  = false;
        hasSpeed     = false;
        isLinked     = false;
        hasBoundsBox = false;
        hasData      = false;
    }
}
