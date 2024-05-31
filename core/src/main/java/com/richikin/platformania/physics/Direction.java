package com.richikin.platformania.physics;

import com.richikin.platformania.maths.SimpleVec2;

public class Direction extends SimpleVec2
{
    public Direction()
    {
        super();
    }

    public Direction( int x, int y )
    {
        super( x, y );
    }

    public Direction( Direction direction )
    {
        this.x = direction.x;
        this.y = direction.y;
    }

    public void set( Direction direction )
    {
        this.x = direction.x;
        this.y = direction.y;
    }

    /**
     * Clears X and Y directions by setting
     * them to _DIRECTION_STILL
     */
    public void standStill()
    {
        this.x = Movement._DIRECTION_STILL;
        this.y = Movement._DIRECTION_STILL;
    }

    public boolean hasDirection()
    {
        return ( this.x != Movement._DIRECTION_STILL ) || ( this.y != Movement._DIRECTION_STILL );
    }

    public boolean hasXDirection()
    {
        return ( this.x != Movement._DIRECTION_STILL );
    }

    public boolean hasYDirection()
    {
        return ( this.y != Movement._DIRECTION_STILL );
    }

    /**
     * Returns the X direction, but flipped.
     * i.e If current X direction is _DIRECTION_LEFT
     * this will return _DIRECTION_RIGHT
     */
    public int getFlippedX()
    {
        return this.x * -1;
    }

    /**
     * Returns the Y direction, but flipped.
     * i.e If current Y direction is _DIRECTION_UP
     * this will return _DIRECTION_DOWN.
     */
    public int getFlippedY()
    {
        return this.y * -1;
    }

    /**
     * Toggle both X and Y directions.
     */
    public void toggle()
    {
        if ( this.x != Movement._DIRECTION_STILL )
        {
            toggleX();
        }

        if ( this.y != Movement._DIRECTION_STILL )
        {
            toggleY();
        }
    }

    /**
     * Toggle X direction
     */
    public void toggleX()
    {
        this.x *= -1;
    }

    /**
     * Toggle Y direction
     */
    public void toggleY()
    {
        this.y *= -1;
    }

    @Override
    public String toString()
    {
        return Movement.getAliasX( x ) + ", " + Movement.getAliasY( y );
    }
}
