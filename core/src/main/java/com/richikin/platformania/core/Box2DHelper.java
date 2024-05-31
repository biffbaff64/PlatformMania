package com.richikin.platformania.core;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.platformania.config.GdxSystem;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.physics.box2d.Box2DContactListener;
import com.richikin.platformania.physics.BodyBuilder;
import com.richikin.platformania.physics.PhysicsBody;

public class Box2DHelper implements Disposable
{
    public       World                box2DWorld;
    public       Box2DDebugRenderer   b2dr;
    public       Box2DContactListener box2DContactListener;
    public       BodyBuilder          bodyBuilder;
    public       boolean              worldStepEnabled;
    public       boolean              canDrawDebug;
    public final Array< PhysicsBody > bodiesList;

    public Box2DHelper()
    {
        bodiesList = new Array<>();
    }

    public void createWorld()
    {
        Trace.checkPoint();

        box2DWorld = new World
            (
                new Vector2
                    (
                        ( Gfx._WORLD_GRAVITY.x * Gfx._PPM ),
                        ( Gfx._WORLD_GRAVITY.y * Gfx._PPM )
                    ),
                false
            );

        bodyBuilder          = new BodyBuilder();
        box2DContactListener = new Box2DContactListener();

        box2DWorld.setContactListener( box2DContactListener );

        worldStepEnabled = true;
    }

    public void createB2DRenderer()
    {
        b2dr = new Box2DDebugRenderer
            (
                true,
                true,
                true,
                true,
                false,
                true
            );

        canDrawDebug = false;
    }

    public void drawDebugMatrix()
    {
        if ( ( b2dr != null ) && App.getDev().isDevMode() && canDrawDebug )
        {
            //
            // Care needed here if the viewport sizes for SpriteGameCamera
            // and TiledGameCamera are different.
            Matrix4 debugMatrix = App.getBaseRenderer()
                .getSpriteGameCamera()
                .camera
                .combined
                .scale( Gfx._PPM, Gfx._PPM, 0 );

            b2dr.render( box2DWorld, debugMatrix );
        }
    }

    /**
     * Update this games Box2D physics.
     */
    public void worldStep()
    {
        if ( worldStepEnabled && ( box2DWorld != null ) && !App.getAppConfig().gamePaused )
        {
            box2DWorld.step
                (
                    Gfx._STEP_TIME,
                    Gfx._VELOCITY_ITERATIONS,
                    Gfx._POSITION_ITERATIONS
                );
        }

        tidyDeletionList();
    }

    /**
     * If any PhysicsBody#isAlive is FALSE then destroy the associated
     * Box2D Body. The PhysicsBody should remain intact, allowing for
     * a new body to be allocated if required.
     */
    public void tidyDeletionList()
    {
        if ( !App.getWorldModel().box2DWorld.isLocked() && ( bodiesList.size > 0 ) )
        {
            Array.ArrayIterator< PhysicsBody > iter = new Array.ArrayIterator<>( bodiesList );

            while ( iter.hasNext() )
            {
                PhysicsBody physicsBody = iter.next();

                if ( ( physicsBody != null ) && ( physicsBody.body != null ) && !physicsBody.isAlive )
                {
                    App.getWorldModel().box2DWorld.destroyBody( physicsBody.body );

                    physicsBody.body = null;
                }
            }
        }
    }

    public void activate()
    {
        worldStepEnabled = true;
    }

    public void deActivate()
    {
        worldStepEnabled = false;
    }

    @Override
    public void dispose()
    {
        Trace.checkPoint();

        b2dr.dispose();
        b2dr = null;

        box2DContactListener = null;
        bodyBuilder          = null;

        box2DWorld.dispose();
        box2DWorld = null;
    }
}

