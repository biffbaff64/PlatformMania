package com.richikin.platformania.physics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.entities.IEntityComponent;
import com.richikin.platformania.core.App;
import com.richikin.platformania.entities.objects.GdxSprite;

public class Proximity
{
    public Proximity()
    {
    }

    public boolean isNear( final GdxSprite parent, final GdxSprite target )
    {
        boolean near = false;

        Rectangle proximityArea = new Rectangle( parent.sprite.getBoundingRectangle() );

        proximityArea.x -= proximityArea.getWidth();
        // proximityArea.y stays the same

        proximityArea.setWidth( proximityArea.getWidth() * 3 );
        proximityArea.setHeight( proximityArea.getHeight() * 2 );

        if ( proximityArea.overlaps( target.sprite.getBoundingRectangle() ) )
        {
            near = true;
        }

        return near;
    }

    public boolean isNearX( final GdxSprite parent, final GdxSprite target )
    {
        boolean near = false;

        Rectangle proximityArea = new Rectangle( parent.sprite.getBoundingRectangle() );

        proximityArea.x -= proximityArea.getWidth();
        // proximityArea.y stays the same

        proximityArea.setWidth( proximityArea.getWidth() * 3 );
        // proximityArea.height stays the same

        if ( proximityArea.overlaps( target.sprite.getBoundingRectangle() ) )
        {
            near = true;
        }

        return near;
    }

    public boolean isNearY( final GdxSprite parent, final GdxSprite target )
    {
        boolean near = false;

        Rectangle proximityArea = new Rectangle( parent.sprite.getBoundingRectangle() );

        proximityArea.x -= proximityArea.getWidth();
        // proximityArea.y stays the same

        // proximityArea.width stays the same
        proximityArea.setHeight( proximityArea.getHeight() * 2 );

        if ( proximityArea.overlaps( target.sprite.getBoundingRectangle() ) )
        {
            near = true;
        }

        return near;
    }

    public boolean isRoughlyAbove( final GdxSprite parent, final GdxSprite target )
    {
        boolean above = false;

        Rectangle proximityArea = new Rectangle( parent.sprite.getBoundingRectangle() );

        proximityArea.x -= proximityArea.getWidth();
        // proximityArea.y stays the same

        // proximityArea.width stays the same
        proximityArea.setHeight( proximityArea.getHeight() * 2 );

        Rectangle targetRectangle = target.sprite.getBoundingRectangle();

        targetRectangle.y = proximityArea.y;

        if ( proximityArea.overlaps( targetRectangle ) )
        {
            above = true;
        }

        return above;
    }

    public boolean isVeryClose( final GdxSprite parent, final GdxSprite target )
    {
        boolean near = false;

        Rectangle proximityArea = new Rectangle( parent.sprite.getBoundingRectangle() );

        proximityArea.x -= ( proximityArea.getWidth() / 2 );
        proximityArea.y += ( proximityArea.getHeight() / 2 );
        proximityArea.setWidth( proximityArea.getWidth() * 2 );
        proximityArea.setHeight( proximityArea.getHeight() * 2 );

        if ( proximityArea.overlaps( target.sprite.getBoundingRectangle() ) )
        {
            near = true;
        }

        return near;
    }

    /**
     * Checks to see if the supplied sprite is next to an entity
     * of the supplied GraphicID Identity.
     *
     * @param gdxSprite The sprite to check.
     * @param graphicID The GraphicID to check for.
     *
     * @return boolean holding the result.
     */
    public boolean isNextTo( GdxSprite gdxSprite, GraphicID graphicID )
    {
        boolean isNextTo = false;

        Array.ArrayIterator<IEntityComponent> iter = new Array.ArrayIterator<>( App.getEntityData().getEntityMap() );

        while ( iter.hasNext() )
        {
            IEntityComponent sprite = iter.next();

            if ( sprite.getGID() == graphicID )
            {
                isNextTo = ( ((GdxSprite) sprite).sprite.getBoundingRectangle()
                                                        .overlaps( gdxSprite.sprite.getBoundingRectangle() ) );
            }
        }

        return isNextTo;
    }

    public boolean isInLineOfSight( final GdxSprite ent1, final GdxSprite ent2, final int step )
    {
        return isInLineOfSight( ent1, ent2, step, 1000 );
    }

    /**
     *
     * @param ent1
     * @param ent2
     * @param step
     * @param maxDist
     * @return
     */
    public boolean isInLineOfSight( final GdxSprite ent1, final GdxSprite ent2, final int step, final float maxDist )
    {
        Vector2 tmp1  = new Vector2();
        Vector2 tmp2  = new Vector2();
        Vector2 start = new Vector2( ent1.sprite.getX(), ent1.sprite.getY() );
        Vector2 stop  = new Vector2( ent2.sprite.getX(), ent2.sprite.getY() );

        float dist = 0;

        tmp1.set( start );

        tmp2.x = stop.x - start.x;
        tmp2.y = stop.y - start.y;

        tmp2.nor().scl( step );

        boolean processing = true;
        boolean retFlag    = false;

        while ( processing )
        {
            tmp1.x += tmp2.x;
            tmp1.y += tmp2.y;

            if ( Vector2.dst2( tmp1.x, tmp1.y, stop.x, stop.y ) < ( step * step * 1.2f ) )
            {
                retFlag    = true;
                processing = false;
            }

            if ( dist > maxDist )
            {
                retFlag    = false;
                processing = false;
            }

            dist += step;
        }

        return retFlag;
    }
}
