package com.richikin.platformania.physics.aabb;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.richikin.platformania.core.App;
import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.physics.CollisionObject;

public class AABB implements AABBCollision
{
    private Rectangle topCell;
    private Rectangle middleCell;
    private Rectangle bottomCell;

    public AABB()
    {
        super();

        this.topCell    = new Rectangle();
        this.middleCell = new Rectangle();
        this.bottomCell = new Rectangle();
    }

    @Override
    public boolean checkAABBBoxes( CollisionObject boxA )
    {
        boolean isHitting;
        boolean collisionDetected = false;

        if ( App.getEntityData().getEntityMap().size > 0 )
        {
            float boxHeight = boxA.rectangle.height / 3;

            // The cell at the top third of BoxA's
            // collision box, for detecting collisions
            // at the top.
            topCell.set
                (
                    boxA.rectangle.x + ( boxA.rectangle.width / 4 ),
                    ( boxA.rectangle.y + ( boxHeight * 2 ) ),
                    boxA.rectangle.width / 2,
                    boxHeight
                );

            // The cell at the middle third of BoxA's
            // collision box, for detecting collisions
            // in the middle.
            middleCell.set
                (
                    boxA.rectangle.x,
                    ( boxA.rectangle.y + boxHeight ),
                    boxA.rectangle.width,
                    boxHeight
                );

            // The cell at the bottom third of BoxA's
            // collision box, for detecting collisions
            // at the bottom.
            bottomCell.set
                (
                    boxA.rectangle.x + ( boxA.rectangle.width / 4 ),
                    boxA.rectangle.y,
                    boxA.rectangle.width / 2,
                    boxHeight
                );

            isHitting = false;

            //
            // boxA is the sprite checking for collision
            // boxB is the sprite that is being checked
            // All collisionObjects have parentEntities.
            for ( int i = 0; i < App.getEntityData().getEntityMap().size; i++ )
            {
                CollisionObject boxB = App.getEntityData().getEntity( i ).getCollisionObject();

                if ( ( ( boxA.parentEntity.getCollidesWith() & boxB.parentEntity.getBodyCategory() ) != 0 )
                    && ( ( boxB.parentEntity.getCollidesWith() & boxA.parentEntity.getBodyCategory() ) != 0 )
                    && ( ( boxA.gid != boxB.gid ) || ( boxA.parentEntity.getSpriteNumber() != boxB.parentEntity.getSpriteNumber() ) )
                    && ( ( boxA.type != boxB.type ) || ( boxA.parentEntity.getSpriteNumber() != boxB.parentEntity.getSpriteNumber() ) ) )
                {
                    if ( Intersector.overlaps( boxA.rectangle, boxB.rectangle ) )
                    {
                        if ( Intersector.overlaps( topCell, boxB.rectangle ) )
                        {
                            isHitting          = true;
                            boxA.idTop         = boxB.gid;
                            boxA.boxHittingTop = boxB.parentEntity.getSpriteNumber();
                            boxA.contactMask |= CollisionObject._TOP;
                        }

                        if ( Intersector.overlaps( middleCell, boxB.rectangle ) )
                        {
                            if ( ( middleCell.x >= boxB.rectangle.x )
                                && ( middleCell.x <= ( boxB.rectangle.x + boxB.rectangle.width ) )
                                && ( ( middleCell.x + middleCell.width ) > ( boxB.rectangle.x + boxB.rectangle.width ) ) )
                            {
                                isHitting           = true;
                                boxA.idLeft         = boxB.gid;
                                boxA.boxHittingLeft = boxB.parentEntity.getSpriteNumber();
                                boxA.contactMask |= CollisionObject._LEFT;
                            }

                            if ( ( middleCell.x < boxB.rectangle.x )
                                && ( ( middleCell.x + middleCell.width ) >= boxB.rectangle.x )
                                && ( ( middleCell.x + middleCell.width ) <= ( boxB.rectangle.x + boxB.rectangle.width ) ) )
                            {
                                isHitting            = true;
                                boxA.idRight         = boxB.gid;
                                boxA.boxHittingRight = boxB.parentEntity.getSpriteNumber();
                                boxA.contactMask |= CollisionObject._RIGHT;
                            }
                        }

                        if ( Intersector.overlaps( bottomCell, boxB.rectangle )
                            && ( boxB.rectangle.y <= bottomCell.y ) )
                        {
                            isHitting             = true;
                            boxA.idBottom         = boxB.gid;
                            boxA.boxHittingBottom = boxB.parentEntity.getSpriteNumber();
                            boxA.contactMask |= CollisionObject._BOTTOM;
                        }
                    }
                }

                if ( isHitting )
                {
                    collisionDetected = true;
                    isHitting         = false;

                    boxA.contactEntity     = boxB.parentEntity;
                    boxA.isContactObstacle = boxB.isObstacle;
                    boxA.action            = ActionStates._HITTING;
                    boxB.action            = ActionStates._HITTING;

                    if ( boxB.gid == App.getAssets().getPlayerGID() )
                    {
                        boxA.isHittingPlayer = true;
                    }
                }
            }
        }

        return collisionDetected;
    }

    @Override
    public void dispose()
    {
        topCell    = null;
        middleCell = null;
        bottomCell = null;
    }
}
