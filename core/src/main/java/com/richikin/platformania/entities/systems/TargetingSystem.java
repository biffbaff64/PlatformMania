package com.richikin.platformania.entities.systems;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.core.App;
import com.richikin.platformania.entities.objects.GdxSprite;
import org.jetbrains.annotations.NotNull;

public class TargetingSystem implements DecisionSystemInterface
{
    public final float   speedX;
    public final float   speedY;
    public       boolean isAdjustingTarget;
    public final boolean rotationAllowed;
    public final boolean checkMoveAllowed;

    private final Vector2 destination;

    public TargetingSystem()
    {
        speedX = 0;
        speedY = 0;

        destination      = new Vector2();
        rotationAllowed  = false;
        checkMoveAllowed = true;
    }

    @Override
    public void update( GdxSprite parentSprite, GdxSprite target )
    {
        Vector2 vector2 = getTargetPosition( parentSprite, target );

        faceTarget( vector2.x, vector2.y, parentSprite );
    }

    @Override
    public Vector2 getTargetPosition( GdxSprite parentSprite, GdxSprite target )
    {
        float targetX;
        float targetY;

        if ( isAdjustingTarget )
        {
            targetX = destination.x;
            targetY = destination.y;

            isAdjustingTarget = false;
        }
        else
        {
            targetX = ( target.sprite.getX() + ( target.frameWidth / 2f ) );
            targetY = ( target.sprite.getY() + ( target.frameHeight / 2f ) );
        }

        return new Vector2( targetX, targetY );
    }

    @Override
    public void setAdjustedTarget( @NotNull GdxSprite parentSprite, Object... args )
    {
        int trys = 0;

        do
        {
            destination.set
                (
                    MathUtils.random( App.getMapData().mapWidth ),
                    MathUtils.random( App.getMapData().mapHeight )
                );

            trys++;
        }
        while ( ( trys < 1000 ) && ( App.getPlayer().viewBox.contains( destination ) ) );
    }

    @Override
    public void faceTarget( float targetX, float targetY, GdxSprite parentSprite )
    {
        Vector2 vector2 = getTargetVector( targetX, targetY, parentSprite );

        if ( rotationAllowed )
        {
            parentSprite.sprite.setRotation( vector2.angleDeg() - 90 );
        }

        if ( parentSprite.getActionState() == ActionStates._RUNNING )
        {
            calculateMove( vector2.nor(), parentSprite );
        }
    }

    @Override
    public Vector2 getTargetVector( float targetX, float targetY, GdxSprite parentSprite )
    {
        Vector2 targetPos = new Vector2( targetX, targetY );
        Vector2 parentPos = new Vector2( parentSprite.sprite.getX(), parentSprite.sprite.getY() );

        return new Vector2( targetPos.sub( parentPos ) );
    }

    @Override
    public void calculateMove( @NotNull Vector2 vector2, GdxSprite parentSprite )
    {
        float xMove = ( speedX * vector2.x );
        float yMove = ( speedY * vector2.y );

        if ( checkMoveAllowed )
        {
            xMove = checkXMovement( xMove, parentSprite );
            yMove = checkYMovement( yMove, parentSprite );
        }

        parentSprite.speed.set( xMove, yMove );

        if ( parentSprite.speed.isEmpty() )
        {
            parentSprite.setActionState( ActionStates._STANDING );
        }
    }

    @Override
    public float checkXMovement( float _xMove, GdxSprite parentSprite )
    {

        return _xMove;
    }

    @Override
    public float checkYMovement( float _yMove, GdxSprite parentSprite )
    {

        return _yMove;
    }

    @Override
    public float distanceRemaining( GdxSprite parentSprite, Vector2 destination )
    {
        return new Vector2( parentSprite.sprite.getX(), parentSprite.sprite.getY() ).dst( destination );
    }
}
