package com.richikin.platformania.physics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.platformania.enums.GraphicID;

public class PhysicsBody implements Disposable
{
    public static final short _TOP    = 0x01;
    public static final short _BOTTOM = 0x02;
    public static final short _LEFT   = 0x04;
    public static final short _RIGHT  = 0x08;

    public Body      body;
    public int       index;
    public boolean   isAlive;
    public int       contactCount;
    public short     contactMask;
    public Rectangle bodyBox;
    public GraphicID type;

    public PhysicsBody()
    {
        this( null, 0, false );
    }

    public PhysicsBody( Body body, int index, boolean isAlive )
    {
        this.body         = body;
        this.index        = index;
        this.isAlive      = isAlive;
        this.contactCount = 0;
        this.contactMask  = 0;
        this.bodyBox      = new Rectangle();
        this.type         = GraphicID._ENTITY;
    }

    @Override
    public void dispose()
    {
        body    = null;
        bodyBox = null;
        type    = null;
    }

    @Override
    public String toString()
    {
        return "PhysicsBody["
            + "body=" + body
            + ", index=" + index
            + ", isAlive=" + isAlive
            + ", contactCount=" + contactCount
            + ", [bodyBox=" + bodyBox.toString() + "]]"
            + ", type=" + type;
    }
}
