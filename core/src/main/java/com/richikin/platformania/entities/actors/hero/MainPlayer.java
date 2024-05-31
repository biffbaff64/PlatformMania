package com.richikin.platformania.entities.actors.hero;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.enums.StateID;
import com.richikin.platformania.logging.Stats;
import com.richikin.platformania.logging.StopWatch;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.maths.shapes.Box;
import com.richikin.platformania.physics.Dir;
import com.richikin.platformania.physics.Direction;
import com.richikin.platformania.physics.Movement;
import com.richikin.platformania.config.Meters;
import com.richikin.platformania.core.App;
import com.richikin.platformania.core.GameConstants;
import com.richikin.platformania.entities.objects.AnimID;
import com.richikin.platformania.entities.objects.GdxSprite;
import com.richikin.platformania.entities.objects.SpriteDescriptor;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.maps.CheckPoint;
import com.richikin.platformania.maps.Doorway;
import com.richikin.platformania.physics.PhysicsBodyType;

import java.util.concurrent.TimeUnit;

public class MainPlayer extends GdxSprite
{
    //@formatter:off
    private static final AnimID[] animIDs =
        {
            new AnimID( ActionStates._SPAWNING,     GraphicID.G_PLAYER_SPAWNING,    0.6f ),
            new AnimID( ActionStates._STANDING,     GraphicID.G_PLAYER,             0.6f ),
            new AnimID( ActionStates._CROUCHING,    GraphicID.G_PLAYER_CROUCH,      0.6f ),
            new AnimID( ActionStates._RUNNING,      GraphicID.G_PLAYER_RUN,         0.4f ),
            new AnimID( ActionStates._FIGHTING,     GraphicID.G_PLAYER_FIGHT,       0.2f ),
            new AnimID( ActionStates._DYING,        GraphicID.G_PLAYER_DYING,       0.8f ),
            new AnimID( ActionStates._HURT,         GraphicID.G_PLAYER_HURT,        0.4f ),
            new AnimID( ActionStates._JUMPING,      GraphicID.G_PLAYER,             0.4f ),
            new AnimID( ActionStates._HOLDING,      GraphicID.G_PLAYER,             0.4f ),
            new AnimID( ActionStates._RESETTING,    GraphicID.G_PLAYER,             0.4f ),
            new AnimID( ActionStates._CLIMBING,     GraphicID.G_PLAYER_CLIMB,       0.4f ),
        };
    //@formatter:on

    private static final float _PLAYER_X_SPEED   = 12;
    private static final int   _BODYBOX_WIDTH    = 15;
    private static final int   _DEAD_PAUSE_DELAY = 5000;

    public ButtonInputHandler buttons;
    public Box                viewBox;
    public GdxSprite          heldEntity;

    public boolean isHurting;
    public boolean isMovingX;
    public boolean isMovingY;
    public boolean isInWater;
    public boolean isHolding;
    public boolean hasFallenTooFar;

    private StopWatch strengthTimer;
    private int       strengthLossInterval;
    private Direction previousDirection;
    private float     fallStartY;

    // ---------------------------------------------------
    //      CODE
    // ---------------------------------------------------

    public MainPlayer()
    {
        super( GraphicID.G_PLAYER );
    }

    @Override
    public void initialise( SpriteDescriptor descriptor )
    {
        Trace.checkPoint();

        create( descriptor );

        bodyCategory = Gfx.CAT_PLAYER;
        collidesWith = Gfx.CAT_COLLECTIBLE
            | Gfx.CAT_ENEMY
            | Gfx.CAT_USABLE
            | Gfx.CAT_OBSTACLE
            | Gfx.CAT_JAIL
            | Gfx.CAT_INTERACTIVE;

        createBody( PhysicsBodyType._DYNAMIC );

        previousDirection    = new Direction();
        buttons              = new ButtonInputHandler();
        viewBox              = new Box();
        strengthTimer        = new StopWatch();
        strengthLossInterval = 400;

        setup( true );
    }

    /**
     * Completes entity initialisation WITHOUT allocating any
     * new objects.
     * This is the rentry point for restarting an entity, after
     * losing a life for instance.
     */
    public void setup( boolean isSpawning )
    {
        Trace.checkPoint();

        direction.set( Movement._DIRECTION_STILL, Movement._DIRECTION_STILL );
        lookingAt.set( Movement._DIRECTION_RIGHT, Movement._DIRECTION_STILL );
        previousDirection.set( lookingAt );

        strength   = GameConstants._MAX_STRENGTH;
        fallStartY = 0;

        isMovingX       = false;
        isMovingY       = false;
        isRotating      = false;
        isFlippedX      = false;
        isFlippedY      = false;
        canFlip         = true;
        isDrawable      = true;
        isHurting       = false;
        isOnPlatform    = false;
        isInWater       = false;
        isTouchingEnemy = false;
        isHolding       = false;
        hasFallenTooFar = false;

        App.getRoomManager().setPlayerStart();

        sprite.setRotation( 0 );

//        if ( getActionState() == ActionStates._RESETTING )
//        {
//            positionAtCheckpoint();
//        }

        isSetupCompleted = true;

        App.getHud().enableHUDButtons();

        if ( isSpawning )
        {
            setActionState( ActionStates._SPAWNING );
            CheckPoint.set( getBodyX(), getBodyY() );
        }
        else
        {
            setActionState( ActionStates._STANDING );
        }
    }

    /**
     * Set back to start positions, and reset relevant properties
     * such as movement, collision state etc.
     */
    public void restartPlayer()
    {
        App.getPlayer().isSetupCompleted = false;
        App.getPlayer().setup( true );
    }

    @Override
    public void preUpdate()
    {
        // If the physics body was destroyed by a previous action state
        // change, create a new body and update the PhysicsBody object.
        // This requires the bodyBox to have been set up correctly for the
        // new body, which must be done in setActionState.
        if ( getPhysicsBody().body == null )
        {
            createBody( PhysicsBodyType._DYNAMIC );
        }
        // else...
        // Update the bodybox details ready for when the physics body
        // needs to be recreated.
        else
        {
            updateBodyBox();
        }

        super.preUpdate();
    }

    @Override
    public void update()
    {
        if ( App.getAppState().peek() == StateID._STATE_PAUSED )
        {
            setActionState( ActionStates._PAUSED );
        }

        updateMainPlayer();

        animate();

        updateCommon();
    }

    @Override
    public void postUpdate()
    {
        super.postUpdate();

        updateHealth();

        // If any of the previously handled action states have ended with
        // the player being in midair, and Y velocity is negative, then
        // set the action state to _FALLING. This is so that falling excessive
        // distances can be checked for.
        if ( ( getActionState() != ActionStates._FALLING )
            && ( getActionState() != ActionStates._DYING ) )
        {
            if ( ( getPhysicsBody().contactCount == 0 )
                && ( getPhysicsBody().body.getLinearVelocity().y < 0 ) )
            {
                setActionState( ActionStates._FALLING );
                fallStartY      = sprite.getY();
                hasFallenTooFar = false;

                App.getPlayer().isOnPlatform = false;
                App.getPlayer().setLink( 0 );
            }
        }

        // Quick check to see if the player entity has left the map window.
        if ( !App.getMapData().mapBox.contains( sprite.getBoundingRectangle() ) )
        {
            setActionState( ActionStates._DYING );
            handleDying();
        }
    }

    @Override
    public void setActionState( ActionStates newAction )
    {
        if ( newAction != getActionState() )
        {
            SpriteDescriptor descriptor;
            GraphicID        graphicID = GraphicID.G_NO_ID;
            float            animRate  = 0.0f;

            if ( direction.hasDirection() )
            {
                lookingAt.set( direction );
            }

            for ( AnimID animID : animIDs )
            {
                if ( animID.state == newAction )
                {
                    graphicID = animID.gid;
                    animRate  = animID.animRate;
                }
            }

            if ( graphicID != GraphicID.G_NO_ID )
            {
                descriptor            = App.getEntities().getDescriptor( graphicID );
                descriptor._ANIM_RATE = animRate;
                elapsedAnimTime       = 0;

                int modifier = ( ( lookingAt.getX() == Movement._DIRECTION_LEFT ) ? 23 : 26 );

                if ( newAction == ActionStates._CROUCHING )
                {
                    getPhysicsBody().isAlive        = false;
                    getPhysicsBody().bodyBox.x      = sprite.getX() + modifier;
                    getPhysicsBody().bodyBox.y      = sprite.getY();
                    getPhysicsBody().bodyBox.width  = _BODYBOX_WIDTH;
                    getPhysicsBody().bodyBox.height = frameHeight - 5;
                }
                else if ( getActionState() == ActionStates._CROUCHING )
                {
                    getPhysicsBody().isAlive        = false;
                    getPhysicsBody().bodyBox.x      = sprite.getX() + modifier;
                    getPhysicsBody().bodyBox.y      = sprite.getY();
                    getPhysicsBody().bodyBox.width  = _BODYBOX_WIDTH;
                    getPhysicsBody().bodyBox.height = frameHeight;
                }
                else if ( newAction == ActionStates._HURT )
                {
                    lookingAt.set( direction );
                }

                setAnimation( descriptor );
            }
        }

        super.setActionState( newAction );
    }

    /**
     * Sets the sprite position from the physics body coordinates
     */
    @Override
    public void setPositionFromBody()
    {
        if ( ( getPhysicsBody().body != null ) && ( sprite != null ) )
        {
            int modifier = ( ( lookingAt.getX() == Movement._DIRECTION_LEFT ) ? 23 : 26 );

            float newX = getBodyX();
            float newY;

            newX -= ( getPhysicsBody().bodyBox.width / 2f );
            newY = getBodyY() - ( getPhysicsBody().bodyBox.height / 2f );

            newX -= modifier;

            sprite.setPosition( newX, newY );
        }
    }

    @Override
    public void definePhysicsBodyBox( boolean useBodyPos )
    {
        if ( !useBodyPos )
        {
            int modifier = ( ( lookingAt.getX() == Movement._DIRECTION_LEFT ) ? 23 : 26 );

            getPhysicsBody().bodyBox.set
                (
                    sprite.getX() + modifier,
                    sprite.getY(),
                    _BODYBOX_WIDTH,
                    ( getActionState() == ActionStates._CROUCHING ) ? frameHeight - 5 : frameHeight
                );
        }
        else
        {
            updateBodyBox();
        }
    }

    /**
     * The body box holds the size, shape, and position data for use
     * when creating a new body.
     * Useful for when an existing body is destroyed when changing
     * to an animation which is a different size than the current one.
     */
    private void updateBodyBox()
    {
        if ( sprite != null )
        {
            getPhysicsBody().bodyBox.width  = _BODYBOX_WIDTH;
            getPhysicsBody().bodyBox.height = ( getActionState() == ActionStates._CROUCHING )
                                              ? frameHeight - 5
                                              : frameHeight;
            getPhysicsBody().bodyBox.x      = sprite.getX() + 26;
            getPhysicsBody().bodyBox.y      = sprite.getY();
        }
    }

    /**
     * Utility method for updating the players X Direction.
     * Updates the X Direction after first copying the current
     * direction to the copy.
     * This makes it easier to update lookingAt when direction
     * has no direction value.
     *
     * @param newXDir int holding the new X direction.
     */
    public void setXDirection( int newXDir )
    {
        previousDirection.set( direction );
        direction.setX( newXDir );
    }

    /**
     * Adds a specified amount to the player strength.
     *
     * @param amount The amount to add.
     */
    public void addStrength( float amount )
    {
        strength = Math.min( strength + amount, GameConstants._MAX_STRENGTH );
    }

    /**
     * Flag the players physics body for destruction.
     */
    public void killBody()
    {
        getPhysicsBody().isAlive = false;
        App.getWorldModel().bodiesList.add( App.getPlayer().getPhysicsBody() );
    }

    public void handleDying()
    {
        App.getGameProgress().lives.subtract( 1 );

        if ( ( App.getGameProgress().lives.getTotal() > 0 ) || App.getDev().isGodMode() )
        {
            Trace.dbg( "RESETTING - Lives left: ", App.getGameProgress().lives.getTotal() );

            if ( App.getGameProgress().lives.getTotal() <= 0 )
            {
                App.getGameProgress().lives.refill();
            }

            setActionState( ActionStates._RESETTING );

            App.getGameProgress().isRestarting   = false;
            App.getGameProgress().gameOver       = false;
        }
        else
        {
            Trace.dbg( "DEAD" );

            setActionState( ActionStates._DEAD );

            App.getGameProgress().isRestarting = false;
            App.getGameProgress().gameOver     = true;
        }
    }

    public void positionAtCheckpoint()
    {
        getPhysicsBody().body.setTransform
            (
                ( float ) ( CheckPoint.getX() / App.getMapData().tileWidth ),
                ( float ) ( CheckPoint.getY() / App.getMapData().tileHeight ),
                getPhysicsBody().body.getAngle()
            );
    }

    private void updateMainPlayer()
    {
        switch ( getActionState() )
        {
            case _SPAWNING:
            {
                if ( animation.isAnimationFinished( elapsedAnimTime ) )
                {
                    setActionState( ActionStates._STANDING );
                }
            }
            break;

            case _EXPLODING:
            case _RESTARTING:
            case _WAITING:
            case _DEAD:
            case _PAUSED:
            case _KILLED:
            case _CHANGING_ROOM:
            {
            }
            break;

            case _STANDING:
            case _CROUCHING:
            {
                buttons.checkButtons();
            }
            break;

            case _RESETTING:
            case _CLIMBING:
            {
                // TODO
            }
            break;

            case _RUNNING:
            {
                if ( direction.hasDirection() )
                {
                    lookingAt.set( direction );
                }

                buttons.checkButtons();
                movePlayer();
            }
            break;

            case _JUMPING:
            {
                buttons.checkButtons();

                movePlayer();

                if ( getPhysicsBody().body.getLinearVelocity().y < 0 )
                {
                    getPhysicsBody().body.setLinearVelocity
                        (
                            getPhysicsBody().body.getLinearVelocity().x,
                            0
                        );

                    App.getHud().buttonA.setDisabled( false );

                    setActionState( ActionStates._STANDING );
                }
            }
            break;

            case _FIGHTING:
            {
                if ( animation.isAnimationFinished( elapsedAnimTime ) )
                {
                    if ( getActionState() == ActionStates._FIGHTING )
                    {
                        createKnife();
                    }

                    setActionState( ActionStates._STANDING );
                }
            }
            break;

            case _FALLING:
            {
                if ( getPhysicsBody().contactCount > 0 )
                {
                    setActionState( ActionStates._STANDING );

                    if ( sprite.getY() < ( fallStartY - ( 15 * App.getMapData().tileHeight ) ) )
                    {
                        hasFallenTooFar = true;
                    }
                }
            }
            break;

            // State may have changed in checkButtons
            case _HURT:
            {
                buttons.checkButtons();

                if ( getActionState() == ActionStates._HURT )
                {
                    if ( animation.isAnimationFinished( elapsedAnimTime ) )
                    {
                        setActionState( ActionStates._STANDING );

                        isTouchingEnemy = false;
                    }
                }
                else
                {
                    isTouchingEnemy = false;

                    movePlayer();
                }
            }
            break;

            case _DYING:
            {
                if ( animation.isAnimationFinished( elapsedAnimTime ) )
                {
                    handleDying();
                }
            }
            break;

            default:
            {
                Trace.dbg( "Unsupported player action: " + getActionState() );
                Stats.incMeter( Meters._BAD_PLAYER_ACTION.get() );
            }
            break;
        }

        checkDoorways();
        checkWaterBodies();
        monitorCheckpoints();
    }

    private void movePlayer()
    {
        speed.setX( isMovingX ? _PLAYER_X_SPEED : 0 );

        if ( isMovingX )
        {
            getPhysicsBody().body.setLinearVelocity
                (
                    ( speed.getX() * direction.getX() ),
                    App.getPlayer().getPhysicsBody().body.getLinearVelocity().y
                );

            if ( !isHolding )
            {
                isFlippedX = ( lookingAt.getX() == Movement._DIRECTION_LEFT );
            }
        }
    }

    /**
     * Creates a knife for the player to throw.
     */
    private void createKnife()
    {
        SpriteDescriptor descriptor = App.getEntities().getDescriptor( GraphicID.G_PLAYER_KNIFE );

        descriptor._POSITION.x = ( int ) ( getBodyX() / Gfx._PPM );
        descriptor._POSITION.y = ( int ) ( getBodyY() / Gfx._PPM );

        PlayerWeapon weapon = new PlayerWeapon();
        weapon.initialise( descriptor );

        App.getEntityData().addEntity( weapon );
    }

    /**
     * Check for the player standing in a doorway.
     */
    private void checkDoorways()
    {
        for ( Doorway doorway : new Array.ArrayIterator<>( App.getMapData().doorList ) )
        {
            if ( doorway.box.contains( getBodyX(), getBodyY() ) )
            {
                if ( doorway.nextDir == Dir._UP )
                {
                    App.getLevelManager().gameLevelUp();
                }
                else
                {
                    App.getLevelManager().gameLevelDown();
                }

                App.getGameProgress().levelCompleted = true;
                App.getGameProgress().isRestarting   = false;
            }
        }
    }

    /**
     * Checks for main player reaching a checkpoint.
     */
    private void monitorCheckpoints()
    {
        int index = 0;

        for ( Rectangle rectangle : new Array.ArrayIterator<>( App.getMapData().checkPointList ) )
        {
            if ( rectangle.contains( getBodyX(), getBodyY() )
                && ( index != App.getMapData().checkPointNumber ) )
            {
                CheckPoint.set( getBodyX(), getBodyY() );

                App.getHud().getTextPanel().setTexture( "checkpoint_text_box" );
                App.getHud().getTextPanel().activate();

                App.getMapData().checkPointNumber = index;
            }

            index++;
        }
    }

    /**
     * Checks for main player standing in water.
     */
    private void checkWaterBodies()
    {
        isInWater = false;

        for ( Rectangle rectangle : new Array.ArrayIterable<>( App.getMapData().waterList ) )
        {
            if ( rectangle.contains( getBodyX(), getBodyY() + 4 ) )
            {
                isInWater = true;
            }
        }
    }

    /**
     * Updates the health bar value. PLayer strength is reduced if touching
     * a lethal enemy, if underwater, or if the player has fallen from a
     * great height.
     * <p>
     * Health is also reduced by a small amount at regular intervals.
     * </p>
     */
    private void updateHealth()
    {
        if ( ( getActionState() != ActionStates._RESETTING )
            && ( getActionState() != ActionStates._DYING )
            && ( getActionState() != ActionStates._FIGHTING ) )
        {
            // ------------------------------------------------------------------
            if ( isInWater || isTouchingEnemy || hasFallenTooFar
                || ( getActionState() == ActionStates._FIGHTING )
                || ( getActionState() == ActionStates._JUMPING ) )
            {
                if ( isTouchingEnemy || hasFallenTooFar )
                {
                    strength -= Math.min( hasFallenTooFar ? 25.0f : 1.0f, strength );

                    setActionState( ActionStates._HURT );

                    hasFallenTooFar = false;
                }
                else
                {
                    if ( strengthTimer.time( TimeUnit.MILLISECONDS ) > ( isInWater ? 200 : strengthLossInterval ) )
                    {
                        if ( strength > 0 )
                        {
                            strength -= ( isInWater ? 1f : 0.2f );

                            if ( isInWater )
                            {
                                setActionState( ActionStates._HURT );
                            }
                        }

                        strengthTimer.reset();
                    }
                }

                if ( strength <= 0 )
                {
                    setActionState( ActionStates._DYING );
                }
            }
            // ------------------------------------------------------------------
        }
    }

    @Override
    public void dispose()
    {
        super.dispose();

        buttons.dispose();

        heldEntity        = null;
        previousDirection = null;
        buttons           = null;
        viewBox           = null;
        strengthTimer     = null;
    }
}
