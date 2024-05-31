package com.richikin.platformania.entities.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.entities.IEntityComponent;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.maths.SimpleVec2F;
import com.richikin.platformania.maths.SimpleVec3;
import com.richikin.platformania.maths.SimpleVec3F;
import com.richikin.platformania.physics.CollisionObject;
import com.richikin.platformania.physics.Direction;
import com.richikin.platformania.core.App;
import com.richikin.platformania.core.GameConstants;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.physics.BodyIdentity;
import com.richikin.platformania.physics.PhysicsBody;
import com.richikin.platformania.physics.PhysicsBodyType;

import java.util.Arrays;

public class GdxSprite implements SpriteInterface, IEntityComponent
{
    // -----------------------------------------------
    // Identity etc.
    //
    public GraphicID    gid;
    public GraphicID    type;                           // Entity Type - _ENTITY, _PICKUP, _OBSTACLE, etc.
    public ActionStates entityAction;                   // Current action/state
    public int          spriteNumber;                   // Position in the EntityMap array
    public boolean      isMainCharacter;
    public Sprite       sprite;
    public float        strength;

    protected boolean isLinked;                         // TRUE if this sprite is linked to another.
    protected int     link;                             // The index of the linked sprite.

    // -----------------------------------------------
    // Movement / Transform
    //
    public Direction   direction;
    public Direction   lookingAt;
    public SimpleVec2F distance;
    public SimpleVec2F speed;
    public SimpleVec3F initXYZ;                         // Initial Map position, set on creation.
    public int         zPosition;
    public boolean     isFlippedX;
    public boolean     isFlippedY;
    public boolean     canFlip;
    public boolean     isRotating;
    public float       rotateSpeed;
    public Vector3     position;
    public boolean     shouldForceTurnX;
    public boolean     shouldForceTurnY;
    public boolean     isHeld;

    // -----------------------------------------------
    // Collision/Physics Related
    //
    public int     b2dBodyIndex;                  // Index into the Body array
    public short   bodyCategory;                  // Bit-mask entity collision type (See Gfx()).
    public short   collidesWith;                  // Bit-mask of entity types that can be collided with
    public boolean isHittable;                    // ( Might be losing this flag... )
    public boolean isTouchingPlayer;
    public boolean isTouchingEnemy;
    public boolean isHittingWeapon;
    public boolean isOnPlatform;
    public boolean isHittingSame;

    // -----------------------------------------------
    // Animation related
    //
    public Animation< TextureRegion > animation;
    public TextureRegion[]            animFrames;

    public float   elapsedAnimTime;
    public boolean isAnimating;
    public boolean isLoopingAnim;
    public int     frameWidth;         // Width in pixels, or width of frame for animations
    public int     frameHeight;        // Width in pixels, or width of frame for animations
    public boolean isDrawable;
    public boolean isActive;
    public boolean isSetupCompleted;

    // --------------------------------------------------------------
    // Code
    // --------------------------------------------------------------

    /**
     * Default constructor.
     */
    public GdxSprite()
    {
        this( GraphicID.G_NO_ID );
    }

    /**
     * Constructor.
     * Creates a GdxSprite with the supplied GraphicID.
     */
    public GdxSprite( GraphicID gid )
    {
        this.gid = gid;
    }

    /**
     * Initialise this Sprite.
     * Override in any entity classes and add any
     * other relevant initialisation code AFTER the
     * call to create().
     *
     * @param descriptor The {@link SpriteDescriptor} holding all setup information.
     */
    @Override
    public void initialise( SpriteDescriptor descriptor )
    {
        create( descriptor );
    }

    /**
     * Performs the actual setting up of the GdxSprite, according to the
     * information provided in the supplied {@link SpriteDescriptor}.
     */
    @Override
    public void create( SpriteDescriptor descriptor )
    {
        sprite    = new Sprite();
        direction = new Direction();
        lookingAt = new Direction();
        speed     = new SimpleVec2F();
        distance  = new SimpleVec2F();
        initXYZ   = new SimpleVec3F();
        position  = new Vector3();

        isDrawable      = true;
        isRotating      = false;
        isFlippedX      = false;
        isFlippedY      = false;
        canFlip         = true;
        isMainCharacter = false;
        isHittable      = true;
        isActive        = true;

        spriteNumber = descriptor._INDEX;
        type         = descriptor._TYPE;
        isAnimating  = ( descriptor._FRAMES > 1 );
        entityAction = ActionStates._NO_ACTION;
        b2dBodyIndex = App.getWorldModel().bodiesList.size;
        strength     = GameConstants._MAX_STRENGTH;

        // PhysicsBody data will be initialised later on.
        App.getWorldModel().bodiesList.add( new PhysicsBody() );

        if ( descriptor._ASSET != null )
        {
            setAnimation( descriptor );
        }

        // Determine the initial starting position by
        // multiplying the marker tile position by tile size
        // and then adding on any supplied modifier value.
        SimpleVec3 vec3m = getPositionModifier();

        SimpleVec3 vec3 = new SimpleVec3
            (
                descriptor._POSITION.x + vec3m.getX(),
                descriptor._POSITION.y + vec3m.getY(),
                App.getEntityUtils().getInitialZPosition( gid ) + vec3m.getZ()
            );

        initPosition( vec3 );

        definePhysicsBodyBox( false );

        isLinked = ( descriptor._LINK > 0 );
        link     = descriptor._LINK;

        isSetupCompleted = true;
    }

    public void createBody( PhysicsBodyType bodyType )
    {
        getPhysicsBody().body = App.getWorldModel().bodyBuilder.newBody
            (
                getPhysicsBody().bodyBox,
                bodyCategory,
                collidesWith,
                bodyType
            );

        if ( App.getWorldModel().bodiesList.get( b2dBodyIndex ).body != null )
        {
            getPhysicsBody().body.setUserData( new BodyIdentity( this, gid, type ) );

            App.getWorldModel().bodiesList.get( b2dBodyIndex ).isAlive = true;
        }
    }

    /**
     * Sets the initial starting position for this sprite.
     * NOTE: It is important that frameWidth & frameHeight
     * are initialised before this method is called.
     */
    @Override
    public void initPosition( SimpleVec3 vec3F )
    {
        initXYZ.set
            (
                ( vec3F.getX() * App.getMapData().tileWidth ),
                ( vec3F.getY() * App.getMapData().tileHeight ),
                vec3F.getZ()
            );

        sprite.setPosition( initXYZ.getX(), initXYZ.getY() );
        sprite.setBounds( initXYZ.getX(), initXYZ.getY(), frameWidth, frameHeight );
        sprite.setOriginCenter();

        zPosition = vec3F.getZ();
    }

    /**
     * Provides an init position modifier value.
     * GdxSprites are placed on TiledMap boundaries and
     * some may need that position adjusting.
     */
    @Override
    public SimpleVec3 getPositionModifier()
    {
        return new SimpleVec3( 0, 0, 0 );
    }

    /**
     * Provides the facility for some sprites to perform certain
     * actions before the main update method.
     * Some sprites may not need to do this, or may need to do extra
     * tasks, in which case this can be overridden.
     */
    @Override
    public void preUpdate()
    {
        //
        // Catch-All for NPCs. Kill them if strength is depleted.
        if ( !isMainCharacter
            && ( strength <= 0 )
            && ( entityAction != ActionStates._DEAD )
            && ( entityAction != ActionStates._DEAD_PAUSE )
            && ( entityAction != ActionStates._DYING ) )
        {
            entityAction = ActionStates._DYING;
        }
    }

    /**
     * The main update method.
     * This is the MINIMUM that should be performed for each sprite. Most sprites
     * should override this to perform their various actions, or at least call
     * this method at the end of the overriden method.
     */
    @Override
    public void update()
    {
        animate();

        updateCommon();
    }

    @Override
    public void postUpdate()
    {
    }

    /**
     * Common updates for all entities
     */
    @Override
    public void updateCommon()
    {
        if ( isRotating )
        {
            sprite.rotate( rotateSpeed );
        }

        if ( canFlip )
        {
            sprite.setFlip( isFlippedX, isFlippedY );
        }
    }

    @Override
    public void tidy( int index )
    {
    }

    @Override
    public void preDraw()
    {
    }

    @Override
    public void draw( SpriteBatch spriteBatch )
    {
        setPositionFromBody();

        if ( isDrawable )
        {
            try
            {
                sprite.setAlpha( isActive ? 1.0f : 0.4f );
                sprite.draw( spriteBatch );
            }
            catch ( NullPointerException npe )
            {
                Trace.dbg( gid.name() + " : " + npe.getMessage() );
            }
        }
    }

    @Override
    public void animate()
    {
        if ( isAnimating )
        {
            sprite.setRegion( App.getAnimationUtils().getKeyFrame( animation, elapsedAnimTime, isLoopingAnim ) );
            elapsedAnimTime += Gdx.graphics.getDeltaTime();
        }
        else
        {
            sprite.setRegion( animFrames[ 0 ] );
        }
    }

    /**
     * Creates the animation sequence to be used.
     * Also initialises frameWidth & frameHeight.
     */
    @Override
    public void setAnimation( SpriteDescriptor descriptor )
    {
        try
        {
            animFrames = new TextureRegion[ descriptor._FRAMES ];

            TextureRegion asset = App.getAssets().getAnimationRegion( descriptor._ASSET );

            if ( descriptor._SIZE != null )
            {
                frameWidth  = descriptor._SIZE.x;
                frameHeight = descriptor._SIZE.y;
            }
            else
            {
                frameWidth  = asset.getRegionWidth() / descriptor._FRAMES;
                frameHeight = asset.getRegionHeight();
            }

            TextureRegion[][] tmpFrames = asset.split( frameWidth, frameHeight );

            int i = 0;

            for ( final TextureRegion[] tmpFrame : tmpFrames )
            {
                for ( final TextureRegion textureRegion : tmpFrame )
                {
                    if ( i < descriptor._FRAMES )
                    {
                        animFrames[ i++ ] = textureRegion;
                    }
                }
            }

            animation = new Animation<>( descriptor._ANIM_RATE / 6f, animFrames );
            animation.setPlayMode( descriptor._PLAYMODE );
            elapsedAnimTime = 0;

            isLoopingAnim = ( ( descriptor._PLAYMODE != Animation.PlayMode.NORMAL )
                && ( descriptor._PLAYMODE != Animation.PlayMode.REVERSED ) );

            sprite.setRegion( animFrames[ 0 ] );
            sprite.setSize( frameWidth, frameHeight );
        }
        catch ( NullPointerException npe )
        {
            Trace.divider( '#', 100 );
            Trace.checkPoint();
            descriptor.debug();
            Trace.divider( '#', 100 );
        }
    }

    /**
     * Sets the sprite position from the physics body coordinates
     * so that it is drawn at the correct location.
     */
    public void setPositionFromBody()
    {
        if ( ( getPhysicsBody() != null ) && ( sprite != null ) )
        {
            sprite.setPosition
                (
                    ( getPhysicsBody().body.getPosition().x * Gfx._PPM ) - ( getPhysicsBody().bodyBox.width / 2f ),
                    ( getPhysicsBody().body.getPosition().y * Gfx._PPM ) - ( getPhysicsBody().bodyBox.height / 2f )
                );
        }
    }

    @Override
    public CollisionObject getCollisionObject()
    {
        return null;
    }

    /**
     * Sets up the CollisionObject for this sprite, and sets
     * its position to the supplied x & y coordinates.
     */
    @Override
    public void setCollisionObject( float xPos, float yPos )
    {
    }

    /**
     * Defines a box to use for Box2D Physics body creation,
     * based on this sprite's co-ordinates and size.
     * This is the default implementation, and sets the box
     * size to the exact dimensions of an animation frame.
     * Override to create differing sizes.
     */
    @Override
    public void definePhysicsBodyBox( boolean useBodyPos )
    {
        getPhysicsBody().bodyBox.set( sprite.getX(), sprite.getY(), frameWidth, frameHeight );
    }

    //@formatter:off
    @Override public short          getBodyCategory()   {   return bodyCategory;                }
    @Override public short          getCollidesWith()   {   return collidesWith;                }
    @Override public int            getSpriteNumber()   {   return spriteNumber;                }
    @Override public int            getLink()           {   return link;                        }
    @Override public boolean        isLinked()          {   return isLinked;                    }
    @Override public GraphicID      getGID()            {   return gid;                         }
    @Override public GraphicID      getType()           {   return type;                        }
    @Override public ActionStates   getActionState()    {   return entityAction;                }
    @Override public boolean        isHittingSame()     {   return isHittingSame;               }

    @Override public void   setActionState( ActionStates action )   {   entityAction = action;  }
    //@formatter:on

    @Override
    public void setDying()
    {
        speed.setEmpty();
        getPhysicsBody().body.setLinearVelocity( 0, 0 );
        setActionState( ActionStates._DYING );
    }

    @Override
    public void setLink( int _link )
    {
        link     = _link;
        isLinked = ( _link > 0 );
    }

    @Override
    public PhysicsBody getPhysicsBody()
    {
        return App.getWorldModel().bodiesList.get( b2dBodyIndex );
    }

    @Override
    public Rectangle getBodyBox()
    {
        return App.getWorldModel().bodiesList.get( b2dBodyIndex ).bodyBox;
    }

    /**
     * Gets the current X position of the physics body
     * attached to this sprite.
     */
    @Override
    public float getBodyX()
    {
        return getPhysicsBody().body == null
               ? 0 : ( getPhysicsBody().body.getPosition().x * Gfx._PPM );
    }

    /**
     * Gets the current Y position of the physics body
     * attached to this sprite.
     */
    @Override
    public float getBodyY()
    {
        return getPhysicsBody().body == null
               ? 0 : ( getPhysicsBody().body.getPosition().y * Gfx._PPM );
    }

    @Override
    public void dispose()
    {
        Arrays.fill( animFrames, null );

        gid          = null;
        type         = null;
        entityAction = null;
        sprite       = null;
        direction    = null;
        lookingAt    = null;
        distance     = null;
        speed        = null;
        initXYZ      = null;
        animation    = null;
        animFrames   = null;
    }
}
