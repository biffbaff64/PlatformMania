package com.richikin.platformania.physics.aabb;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.richikin.platformania.enums.GraphicID;

public class CollisionRect extends Rectangle
{
    public  Color     colour;
    private GraphicID gid;

    public CollisionRect( GraphicID _gid )
    {
        super( new Rectangle( 0, 0, 1, 1 ) );

        this.gid    = _gid;
        this.colour = Color.WHITE;
    }

    public CollisionRect( Rectangle rectangle, GraphicID _gid )
    {
        super( rectangle );

        this.gid    = _gid;
        this.colour = Color.WHITE;
    }

    public CollisionRect( float _x, float _y, float _width, float _height, GraphicID _gid )
    {
        super( new Rectangle( _x, _y, _width, _height ) );

        this.gid    = _gid;
        this.colour = Color.WHITE;
    }

    public CollisionRect set( CollisionRect rectangle, GraphicID _gid )
    {
        this.x      = rectangle.x;
        this.y      = rectangle.y;
        this.width  = rectangle.width;
        this.height = rectangle.height;
        this.gid    = _gid;
        this.colour = Color.WHITE;

        return rectangle;
    }
}
