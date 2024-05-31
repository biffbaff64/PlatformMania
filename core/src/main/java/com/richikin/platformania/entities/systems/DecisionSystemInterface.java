package com.richikin.platformania.entities.systems;

import com.badlogic.gdx.math.Vector2;
import com.richikin.platformania.entities.objects.GdxSprite;

public interface DecisionSystemInterface
{
    void update( GdxSprite gdxSprite, GdxSprite target );

    Vector2 getTargetPosition( GdxSprite gdxSprite, GdxSprite target );

    void setAdjustedTarget( GdxSprite gdxSprite, Object... args );

    void faceTarget( float targetX, float targetY, GdxSprite gdxSprite );

    Vector2 getTargetVector( float targetX, float targetY, GdxSprite gdxSprite );

    void calculateMove( Vector2 vector2, GdxSprite gdxSprite );

    float checkXMovement( float _xMove, GdxSprite gdxSprite );

    float checkYMovement( float _yMove, GdxSprite gdxSprite );

    float distanceRemaining( GdxSprite parentSprite, Vector2 destination );
}
