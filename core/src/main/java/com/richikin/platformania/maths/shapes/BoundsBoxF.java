package com.richikin.platformania.maths.shapes;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class BoundsBoxF
{
    private float x;
    private float y;
    private float width;
    private float height;

    public BoundsBoxF( float x, float y, float width, float height )
    {
        this.x      = x;
        this.y      = y;
        this.width  = width;
        this.height = height;
    }

    public void set( float x, float y, float width, float height )
    {
        this.x      = x;
        this.y      = y;
        this.width  = width;
        this.height = height;
    }

    public boolean contains( float _x, float _y )
    {
        return ( this.getX() <= _x ) && ( ( this.getX() + this.getWidth() ) >= _x )
            && ( this.getY() <= _y ) && ( ( this.getY() + this.getHeight() ) >= _y );
    }

    public boolean contains( Vector2 point )
    {
        return contains( point.x, point.y );
    }

    public boolean contains( Rectangle rectangle )
    {
        float xmin = rectangle.x;
        float xmax = ( xmin + rectangle.width );

        float ymin = rectangle.y;
        float ymax = ( ymin + rectangle.height );

        return ( ( ( xmin > left() ) && ( xmin < right() ) ) && ( ( xmax > left() ) && ( xmax < right() ) ) )
            && ( ( ( ymin > bottom() ) && ( ymin < top() ) ) && ( ( ymax > bottom() ) && ( ymax < top() ) ) );
    }

    public float left()
    {
        return x;
    }

    public float right()
    {
        return x + width;
    }

    public float top()
    {
        return y + height;
    }

    public float bottom()
    {
        return y;
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public float getWidth()
    {
        return width;
    }

    public float getHeight()
    {
        return height;
    }

    @Override
    public String toString()
    {
        return "[" + x + ", " + y + "," + width + ", " + height + "]";
    }
}
