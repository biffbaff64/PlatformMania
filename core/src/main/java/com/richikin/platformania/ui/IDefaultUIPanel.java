package com.richikin.platformania.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.richikin.platformania.enums.StateID;
import com.richikin.platformania.maths.SimpleVec2F;
import com.richikin.platformania.maths.Vec2F;
import com.richikin.platformania.physics.Direction;

public interface IDefaultUIPanel
{
    void open();

    void close();

    void initialise( TextureRegion _region, String _nameID, Object... args );

    void set( SimpleVec2F xy, SimpleVec2F distance, Direction direction, SimpleVec2F speed );

    void setup();

    void draw( SpriteBatch spriteBatch );

    void activate();

    void deactivate();

    void populateTable();

    void setPosition( float x, float y );

    void forceZoomOut();

    void setPauseTime( int _time );

    boolean update();

    boolean getActiveState();

    boolean nameExists( String _nameID );

    Vec2F getPosition();

    int getWidth();

    int getHeight();

    String getNameID();

    void setNameID( String _name );

    StateID getState();

    void setState( StateID _state );
}
