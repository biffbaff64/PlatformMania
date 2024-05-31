package com.richikin.platformania.maths.shapes;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class BoundsBox
{
    private int x;
    private int y;
    private int width;
    private int height;

    public BoundsBox( int x, int y, int width, int height )
    {
        this.x      = x;
        this.y      = y;
        this.width  = width;
        this.height = height;
    }

    public void set( int x, int y, int width, int height )
    {
        this.x      = x;
        this.y      = y;
        this.width  = width;
        this.height = height;
    }

    public boolean contains( int _x, int _y )
    {
        return ( this.getX() <= _x ) && ( ( this.getX() + this.getWidth() ) >= _x )
            && ( this.getY() <= _y ) && ( ( this.getY() + this.getHeight() ) >= _y );
    }

    public boolean contains( Vector2 point )
    {
        return contains( ( int ) point.x, ( int ) point.y );
    }

    public boolean contains( Rectangle rectangle )
    {
        int xmin = ( int ) rectangle.x;
        int xmax = ( int ) ( xmin + rectangle.width );

        int ymin = ( int ) rectangle.y;
        int ymax = ( int ) ( ymin + rectangle.height );

        return ( ( ( xmin > left() ) && ( xmin < right() ) ) && ( ( xmax > left() ) && ( xmax < right() ) ) )
            && ( ( ( ymin > bottom() ) && ( ymin < top() ) ) && ( ( ymax > bottom() ) && ( ymax < top() ) ) );
    }

    public int left()
    {
        return x;
    }

    public int right()
    {
        return ( x + width );
    }

    public int top()
    {
        return ( y + height );
    }

    public int bottom()
    {
        return y;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    @Override
    public String toString()
    {
        return "[" + x + ", " + y + ", " + width + ", " + height + "]";
    }
}
