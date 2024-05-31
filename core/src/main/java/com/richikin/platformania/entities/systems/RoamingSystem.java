package com.richikin.platformania.entities.systems;

import com.badlogic.gdx.math.Vector2;
import com.richikin.platformania.entities.objects.GdxSprite;
import com.richikin.platformania.physics.Movement;

public class RoamingSystem implements DecisionSystemInterface
{
    public final float   speedX;
    public final float   speedY;
    public final boolean isAdjustingTarget;
    public final boolean checkMoveAllowed;

    public RoamingSystem()
    {
        speedX = 0;
        speedY = 0;

        isAdjustingTarget = false;
        checkMoveAllowed  = false;
    }

    @Override
    public void update( GdxSprite gdxSprite, GdxSprite target )
    {
    }

    @Override
    public Vector2 getTargetPosition( GdxSprite gdxSprite, GdxSprite target )
    {
        return new Vector2();
    }

    @Override
    public void setAdjustedTarget( GdxSprite gdxSprite, Object... args )
    {
    }

    @Override
    public void faceTarget( float targetX, float targetY, GdxSprite gdxSprite )
    {
        if ( targetY < gdxSprite.sprite.getY() )
        {
            gdxSprite.lookingAt.setY( Movement._DIRECTION_DOWN );
        }
        else if ( targetY > ( gdxSprite.sprite.getY() + gdxSprite.frameHeight ) )
        {
            gdxSprite.lookingAt.setY( Movement._DIRECTION_UP );
        }
        else
        {
            if ( targetX < gdxSprite.sprite.getX() )
            {
                gdxSprite.lookingAt.setX( Movement._DIRECTION_LEFT );
            }
            else if ( targetX > ( gdxSprite.sprite.getX() + gdxSprite.frameWidth ) )
            {
                gdxSprite.lookingAt.setX( Movement._DIRECTION_RIGHT );
            }
        }
    }

    @Override
    public Vector2 getTargetVector( float targetX, float targetY, GdxSprite gdxSprite )
    {
        return new Vector2();
    }

    @Override
    public void calculateMove( Vector2 vector2, GdxSprite gdxSprite )
    {
    }

    @Override
    public float checkXMovement( float _xMove, GdxSprite gdxSprite )
    {
        return 0;
    }

    @Override
    public float checkYMovement( float _yMove, GdxSprite gdxSprite )
    {
        return 0;
    }

    @Override
    public float distanceRemaining( GdxSprite parentSprite, Vector2 destination )
    {
        return 0;
    }
}
