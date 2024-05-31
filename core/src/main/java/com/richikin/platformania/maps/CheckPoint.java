package com.richikin.platformania.maps;

import com.richikin.platformania.maths.SimpleVec2;

public final class CheckPoint
{
    private static final SimpleVec2 position = new SimpleVec2();

    // ----- Getters -----

    public static SimpleVec2 get()
    {
        return position;
    }

    public static int getX()
    {
        return position.getX();
    }

    public static int getY()
    {
        return position.getY();
    }

    // ----- Setters -----

    public static void set( SimpleVec2 pos )
    {
        position.set( pos );
    }

    public static void set( float x, float y )
    {
        position.set( x, y );
    }
}
