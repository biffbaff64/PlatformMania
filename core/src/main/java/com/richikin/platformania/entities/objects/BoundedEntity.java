package com.richikin.platformania.entities.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.graphics.GraphicsUtils;
import com.richikin.platformania.maths.SimpleVec2F;
import com.richikin.platformania.maths.shapes.BoundsBox;
import com.richikin.platformania.physics.Movement;
import com.richikin.platformania.core.App;

public class BoundedEntity extends GdxSprite
{
    public BoundsBox movementBounds;
    public boolean   justTurnedAroundX;
    public boolean   justTurnedAroundY;

    protected SimpleVec2F distanceReset;

    private final GraphicsUtils gfxUtils;

    public BoundedEntity( GraphicID graphicID )
    {
        super( graphicID );

        gfxUtils = new GraphicsUtils();
    }

    public void init()
    {
        justTurnedAroundX = false;
        justTurnedAroundY = false;
    }

    public void checkMovementBounds()
    {
        if ( movementBounds != null )
        {
            justTurnedAroundX = false;
            justTurnedAroundY = false;

            if ( direction.getX() != Movement._DIRECTION_STILL )
            {
                if ( direction.getX() == Movement._DIRECTION_LEFT )
                {
                    if ( ( ( int ) getBodyX() <= movementBounds.left() )
                        || shouldForceTurnX )
                    {
                        justTurnedAroundX = true;
                    }
                }
                else if ( direction.getX() == Movement._DIRECTION_RIGHT )
                {
                    if ( ( ( int ) getBodyX() >= movementBounds.right() )
                        || shouldForceTurnX )
                    {
                        justTurnedAroundX = true;
                    }
                }

                if ( justTurnedAroundX )
                {
                    onMovementBoundsTurn( direction.getX() );

                    direction.toggleX();
                    shouldForceTurnX = false;
                }
            }

            if ( direction.getY() != Movement._DIRECTION_STILL )
            {
                if ( direction.getY() == Movement._DIRECTION_UP )
                {
                    if ( ( ( int ) getBodyY() >= movementBounds.top() )
                        || shouldForceTurnY )
                    {
                        justTurnedAroundY = true;
                    }
                }
                else if ( direction.getY() == Movement._DIRECTION_DOWN )
                {
                    if ( ( ( int ) getBodyY() <= movementBounds.bottom() )
                        || shouldForceTurnY )
                    {
                        justTurnedAroundY = true;
                    }
                }

                if ( justTurnedAroundY )
                {
                    onMovementBoundsTurn( direction.getY() );

                    direction.toggleY();
                    shouldForceTurnY = false;
                }
            }

            if ( ( ( int ) getBodyX() < ( movementBounds.left() - App.getMapData().tileWidth ) )
                || ( ( int ) getBodyX() > ( movementBounds.right() + App.getMapData().tileWidth ) ) )
            {
                setActionState( ActionStates._DYING );
            }
        }
    }

    public void onMovementBoundsTurn( int edgeSide )
    {
    }

    /**
     * Define the bounding box in which this entity is
     * allowed to move.
     */
    public void setMovementBounds()
    {
        int width  = ( int ) distance.getX(); // Math.max( frameWidth, distance.getX() );
        int height = ( int ) distance.getY(); // Math.max( frameHeight, distance.getY() );

        int x = ( direction.getX() == Movement._DIRECTION_LEFT )
                ? ( int ) ( getBodyX() - distance.getX() )
                : ( int ) getBodyX();

        int y = ( direction.getY() == Movement._DIRECTION_DOWN )
                ? ( int ) ( getBodyY() - distance.getY() )
                : ( int ) getBodyY();

        movementBounds = new BoundsBox( x, y, width, height );
    }

    protected void drawBounds( SpriteBatch spriteBatch )
    {
        gfxUtils.drawRect
            (
                movementBounds.getX(),
                movementBounds.getY(),
                movementBounds.getWidth(),
                movementBounds.getHeight(),
                2
            );
    }
}
