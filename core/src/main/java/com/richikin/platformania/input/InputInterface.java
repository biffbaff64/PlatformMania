package com.richikin.platformania.input;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.platformania.physics.Dir;

public interface InputInterface extends Disposable
{
    void setup();

    Array< GdxButton > getGameButtons();

    VirtualJoystick getVirtualJoystick();

    TouchScreen getTouchScreen();

    Keyboard getKeyboard();

    Vector2 getMousePosition();

    Vector2 getMouseWorldPosition();

    Dir getCurrentRegisteredDirection();

    void setCurrentRegisteredDirection( Dir dir );

    Dir getLastRegisteredDirection();

    void setLastRegisteredDirection( Dir dir );

    float getHorizontalValue();

    void setHorizontalValue( float vval );

    void clearHorizontalValue();

    float getVerticalValue();

    void setVerticalValue( float vval );

    void clearVerticalValue();

    float getControllerXPercentage();

    float getControllerYPercentage();
}
