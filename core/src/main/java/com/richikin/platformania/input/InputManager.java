package com.richikin.platformania.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.richikin.platformania.core.App;
import com.richikin.platformania.input.controllers.ControllerType;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.physics.Dir;

public class InputManager implements InputInterface
{
    public Array< GdxButton > gameButtons;
    public Vector2            mousePosition;
    public Vector2           mouseWorldPosition;
    public Keyboard          keyboard;
    public VirtualJoystick   virtualJoystick;
    public TouchScreen       touchScreen;
    public GameController    gameController;
    public InputMultiplexer  inputMultiplexer;
    public Dir               currentRegisteredDirection;
    public Dir               lastRegisteredDirection;
    public float             horizontalValue;
    public float             verticalValue;

    public InputManager()
    {
    }

    @Override
    public void setup()
    {
        Trace.checkPoint();

        currentRegisteredDirection = Dir._STILL;
        lastRegisteredDirection    = Dir._STILL;

        if ( App.getAppConfig().availableInputs.contains( ControllerType._JOYSTICK, true ) )
        {
            Trace.dbg( "Initialising _VIRTUAL Controller" );

            virtualJoystick = new VirtualJoystick();
            virtualJoystick.create();
        }

        mousePosition      = new Vector2();
        mouseWorldPosition = new Vector2();
        keyboard           = new Keyboard();
        touchScreen        = new TouchScreen();
        gameButtons        = new Array<>();

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor( App.getStage() );
        inputMultiplexer.addProcessor( keyboard );

        Gdx.input.setCatchKey( Input.Keys.BACK, true );
        Gdx.input.setCatchKey( Input.Keys.MENU, true );
        Gdx.input.setInputProcessor( inputMultiplexer );

        App.getAppConfig().addBackButton( "button_backToMenu", "button_backToMenu_pressed" );
    }

    @Override
    public Array< GdxButton > getGameButtons()
    {
        return gameButtons;
    }

    @Override
    public VirtualJoystick getVirtualJoystick()
    {
        return virtualJoystick;
    }

    @Override
    public TouchScreen getTouchScreen()
    {
        return touchScreen;
    }

    @Override
    public Keyboard getKeyboard()
    {
        return keyboard;
    }

    @Override
    public Vector2 getMousePosition()
    {
        return mousePosition;
    }

    @Override
    public Vector2 getMouseWorldPosition()
    {
        return mouseWorldPosition;
    }

    @Override
    public Dir getCurrentRegisteredDirection()
    {
        return currentRegisteredDirection;
    }

    @Override
    public Dir getLastRegisteredDirection()
    {
        return lastRegisteredDirection;
    }

    @Override
    public void setCurrentRegisteredDirection( Dir dir )
    {
        currentRegisteredDirection = dir;
    }

    @Override
    public void setLastRegisteredDirection( Dir dir )
    {
        lastRegisteredDirection = dir;
    }

    @Override
    public float getHorizontalValue()
    {
        return horizontalValue;
    }

    @Override
    public void setHorizontalValue( float hval )
    {
        horizontalValue = hval;
    }

    @Override
    public void clearHorizontalValue()
    {
        horizontalValue = 0;
    }

    @Override
    public float getVerticalValue()
    {
        return verticalValue;
    }

    @Override
    public void setVerticalValue( float vval )
    {
        verticalValue = vval;
    }

    @Override
    public void clearVerticalValue()
    {
        verticalValue = 0;
    }

    @Override
    public float getControllerXPercentage()
    {
        float xPercent = 0.0f;

        if ( App.getHud() != null )
        {
            if ( App.getAppConfig().availableInputs.contains( ControllerType._JOYSTICK, true ) )
            {
                if ( virtualJoystick != null )
                {
                    xPercent = virtualJoystick.getXPercent();
                }
            }
            else
            {
                if ( App.getAppConfig().availableInputs.contains( ControllerType._EXTERNAL, true ) )
                {
                    xPercent = horizontalValue;
                }
                else
                {
                    if ( App.getAppConfig().availableInputs.contains( ControllerType._KEYBOARD, true ) )
                    {
                        xPercent = horizontalValue;
                    }
                }
            }
        }

        return xPercent;
    }

    @Override
    public float getControllerYPercentage()
    {
        float yPercent = 0.0f;

        if ( App.getHud() != null )
        {
            if ( App.getAppConfig().availableInputs.contains( ControllerType._JOYSTICK, true ) )
            {
                if ( virtualJoystick != null )
                {
                    yPercent = virtualJoystick.getYPercent();
                }
            }
            else
            {
                if ( App.getAppConfig().availableInputs.contains( ControllerType._EXTERNAL, true )
                    && gameController != null )
                {
                    yPercent = verticalValue;

                    switch ( gameController.controller.getName() )
                    {
                        case "PC/PS3/Android":
                        case "Controller (Inno GamePad..)":
                            yPercent *= -1;
                            break;
                        default:
                            break;
                    }
                }
                else
                {
                    if ( App.getAppConfig().availableInputs.contains( ControllerType._KEYBOARD, true ) )
                    {
                        yPercent = App.getPlayer().lookingAt.getY();
                    }
                }
            }
        }

        return yPercent;
    }

    @Override
    public void dispose()
    {
        inputMultiplexer.clear();
        inputMultiplexer = null;

        mousePosition      = null;
        mouseWorldPosition = null;
        keyboard           = null;
        touchScreen        = null;
    }
}
