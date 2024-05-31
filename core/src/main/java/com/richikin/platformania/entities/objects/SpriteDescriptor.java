package com.richikin.platformania.entities.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.enums.TileID;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.maths.SimpleVec2;
import com.richikin.platformania.maths.SimpleVec2F;
import com.richikin.platformania.maths.shapes.Box;
import com.richikin.platformania.physics.Direction;

/**
 * Used for storing relevant information for
 * creating, placing and initialising sprites.
 */
public class SpriteDescriptor
{
    public String             _NAME;
    public GraphicID          _GID;          // ID. See GraphicID class for options.
    public TileID             _TILE;         // The placement tile associated with this sprite.
    public String             _ASSET;        // The initial image asset.
    public int                _FRAMES;       // Number of frames in the asset above.
    public GraphicID          _TYPE;         // _MAIN, _INTERACTIVE, _PICKUP etc
    public SimpleVec2         _POSITION;     // X, Y Pos of tile, in TileWidth units, Z-Sort value.
    public SimpleVec2         _SIZE;         // Width and Height.
    public int                _INDEX;        // This entity's position in the entity map.
    public Animation.PlayMode _PLAYMODE;     // Animation playmode for the asset frames above.
    public float              _ANIM_RATE;    // Animation speed
    public GdxSprite          _PARENT;       // Parent GDXSprite (if applicable).
    public int                _LINK;         // Linked GDXSprite (if applicable).
    public Direction          _DIR;          // Initial direction of travel. Useful for moving blocks etc.
    public SimpleVec2F        _DIST;         // Initial travel distance. Useful for moving blocks etc.
    public SimpleVec2F        _SPEED;        // Initial speed. Useful for moving blocks etc.
    public Box                _BOX;          //

    public SpriteDescriptor()
    {
        this._NAME      = "";
        this._GID       = GraphicID.G_NO_ID;
        this._TYPE      = GraphicID.G_NO_ID;
        this._POSITION  = new SimpleVec2();
        this._SIZE      = new SimpleVec2();
        this._INDEX     = 0;
        this._FRAMES    = 0;
        this._PLAYMODE  = Animation.PlayMode.NORMAL;
        this._ANIM_RATE = 1.0f;
        this._ASSET     = "";
        this._LINK      = 0;
        this._TILE      = TileID._DEFAULT_TILE;
        this._PARENT    = null;
        this._DIR       = null;
        this._DIST      = null;
        this._SPEED     = null;
        this._BOX       = null;
    }

    public SpriteDescriptor( GraphicID graphicID,
                             GraphicID type,
                             TileID tileID,
                             String asset,
                             int frames )
    {
        this();

        this._GID    = graphicID;
        this._TILE   = tileID;
        this._ASSET  = asset;
        this._FRAMES = frames;
        this._TYPE   = type;
    }

    public SpriteDescriptor( String name,
                             GraphicID graphicID,
                             GraphicID type,
                             TileID tileID,
                             String asset,
                             int frames,
                             SimpleVec2 assetSize )
    {
        this( graphicID, type, tileID, asset, frames );
        this._NAME     = name;
        this._PLAYMODE = Animation.PlayMode.NORMAL;
        this._SIZE     = assetSize;
    }

    public SpriteDescriptor( String name,
                             GraphicID graphicID,
                             GraphicID type,
                             TileID tileID,
                             String asset,
                             int frames,
                             SimpleVec2 assetSize,
                             Animation.PlayMode playMode )
    {
        this( name, graphicID, type, tileID, asset, frames, assetSize );
        this._PLAYMODE = playMode;
    }


    public SpriteDescriptor( String name,
                             GraphicID graphicID,
                             GraphicID type,
                             TileID tileID,
                             String asset,
                             int frames,
                             SimpleVec2 assetSize,
                             Animation.PlayMode playMode,
                             float animRate )
    {
        this( name, graphicID, type, tileID, asset, frames, assetSize, playMode );
        this._ANIM_RATE = animRate;
    }

    public SpriteDescriptor( SpriteDescriptor descriptor )
    {
        set( descriptor );
    }

    public void set( SpriteDescriptor descriptor )
    {
        this._NAME      = descriptor._NAME;
        this._GID       = descriptor._GID;
        this._TYPE      = descriptor._TYPE;
        this._INDEX     = descriptor._INDEX;
        this._FRAMES    = descriptor._FRAMES;
        this._PLAYMODE  = descriptor._PLAYMODE;
        this._ANIM_RATE = descriptor._ANIM_RATE;
        this._LINK      = descriptor._LINK;
        this._TILE      = descriptor._TILE;
        this._ASSET     = descriptor._ASSET;
        this._POSITION  = new SimpleVec2( descriptor._POSITION );
        this._SIZE      = new SimpleVec2( descriptor._SIZE );
        this._PARENT    = new GdxSprite();
        this._DIR       = new Direction();
        this._DIST      = new SimpleVec2F();
        this._SPEED     = new SimpleVec2F();
        this._BOX       = ( descriptor._BOX == null ) ? new Box() : new Box( descriptor._BOX );
    }

    public void debug()
    {
        Trace.divider();
        Trace.dbg( "_NAME      : ", _NAME );
        Trace.dbg( "_GID       : ", _GID );
        Trace.dbg( "_TYPE      : ", _TYPE );
        Trace.dbg( "_POSITION  : ", ( _POSITION != null ? _POSITION.toString() : "NOT SET" ) );
        Trace.dbg( "_SIZE      : ", ( _SIZE != null ? _SIZE.toString() : "NOT SET" ) );
        Trace.dbg( "_INDEX     : ", _INDEX );
        Trace.dbg( "_FRAMES    : ", _FRAMES );
        Trace.dbg( "_PLAYMODE  : ", _PLAYMODE );
        Trace.dbg( "_ANIM_RATE : ", _ANIM_RATE );
        Trace.dbg( "_ASSET     : ", ( _ASSET != null ? _ASSET : "NOT SET" ) );
        Trace.dbg( "_LINK      : ", _LINK );
        Trace.dbg( "_TILE      : ", _TILE );
        Trace.dbg( "_PARENT    : ", _PARENT );
        Trace.dbg( "_DIR       : ", ( _DIR != null ? _DIR.toString() : "NOT SET" ) );
        Trace.dbg( "_DIST      : ", ( _DIST != null ? _DIST.toString() : "NOT SET" ) );
        Trace.dbg( "_SPEED     : ", ( _SPEED != null ? _SPEED.toString() : "NOT SET" ) );
        Trace.dbg( "_BOX       : ", ( _BOX != null ? _BOX.toString() : "NOT SET" ) );
    }
}
