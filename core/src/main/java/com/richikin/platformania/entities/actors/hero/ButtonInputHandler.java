package com.richikin.platformania.entities.actors.hero;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.config.GdxSystem;
import com.richikin.platformania.input.DirectionMap;
import com.richikin.platformania.input.controllers.ControllerType;
import com.richikin.platformania.physics.Dir;
import com.richikin.platformania.physics.Movement;
import com.richikin.platformania.core.App;
import com.richikin.platformania.input.*;

public class ButtonInputHandler implements PlayerControl, Disposable
{
    private Array< ABXYButton >               ABXYButtons;
    private Array.ArrayIterator< ABXYButton > abxyButtonArrayIterator;

    /**
     * Handles Player button actions
     */
    public ButtonInputHandler()
    {
        ABXYButtons = new Array<>();

        ABXYButtons.add( new AButtonActions() );    // A - Jump
        ABXYButtons.add( new BButtonActions() );    // B - Fight
        ABXYButtons.add( new XButtonActions() );    // X - Grab / Hold
        ABXYButtons.add( new YButtonActions() );    // Y - Change Item Panel

        abxyButtonArrayIterator = new Array.ArrayIterator<>( ABXYButtons );
    }

    @Override
    public void checkButtons()
    {
        abxyButtonArrayIterator.reset();

        while ( abxyButtonArrayIterator.hasNext() )
        {
            abxyButtonArrayIterator.next().update();
        }

        // PC Keyboard.
        if ( App.getAppConfig().availableInputs.contains( ControllerType._KEYBOARD, true ) )
        {
            setDirection( App.getInputManager().getLastRegisteredDirection() );
        }
        else
        {
            // External XBox/Playstation style controllers.
            if ( App.getAppConfig().availableInputs.contains( ControllerType._EXTERNAL, true ) )
            {
                setDirection( App.getInputManager().getLastRegisteredDirection() );
            }
            else
            {
                // On-Screen virtual controller, usually on phone/tablet builds, but
                // sometimes used on desktop builds for testing.
                if ( App.getAppConfig().availableInputs.contains( ControllerType._JOYSTICK, true ) )
                {
                    if ( App.getInputManager().getVirtualJoystick() != null )
                    {
                        // Updates button presses depending upon joystick knob position
                        App.getInputManager().getVirtualJoystick().update();

                        setDirection( App.getInputManager().getLastRegisteredDirection() );
                    }
                }
            }
        }

        boolean directionButtonPressed = false;

        //
        // Check for moving RIGHT
        if ( App.getPlayer().direction.getX() == Movement._DIRECTION_RIGHT )
        {
            directionButtonPressed = true;

            App.getPlayer().setXDirection( Movement._DIRECTION_RIGHT );
            App.getPlayer().setActionState( ActionStates._RUNNING );
            clearMovingY();
            App.getPlayer().isMovingX  = true;
//            App.getPlayer().isFlippedX = App.getPlayer().isHolding;
        }
        //
        // Check for moving LEFT
        else if ( App.getPlayer().direction.getX() == Movement._DIRECTION_LEFT )
        {
            directionButtonPressed = true;

            App.getPlayer().setXDirection( Movement._DIRECTION_LEFT );
            App.getPlayer().setActionState( ActionStates._RUNNING );
            clearMovingY();
            App.getPlayer().isMovingX  = true;
//            App.getPlayer().isFlippedX = !App.getPlayer().isHolding;
        }
        //
        // Check for CROUCHING
        else if ( App.getPlayer().direction.getY() == Movement._DIRECTION_DOWN )
        {
            directionButtonPressed = true;

            App.getPlayer().setActionState( ActionStates._CROUCHING );
        }

        if ( !directionButtonPressed )
        {
            if ( ( App.getPlayer().getActionState() != ActionStates._HURT )
                && ( App.getPlayer().getActionState() != ActionStates._JUMPING )
                && ( App.getPlayer().getActionState() != ActionStates._FIGHTING ) )
            {
                App.getHud().releaseDirectionButtons();

                clearMovingX();
                clearMovingY();

                App.getPlayer().setActionState( ActionStates._STANDING );
            }
        }
    }

    @Override
    public void setDirection( Dir _direction )
    {
        for ( int i = 0; i < DirectionMap.map.length; i++ )
        {
            if ( DirectionMap.map[ i ].translated == _direction )
            {
                int yDir = DirectionMap.map[ i ].dirY;

                if ( App.getPlayer().isMovingY )
                {
                    yDir = App.getPlayer().direction.getY();
                }

                App.getPlayer().setXDirection( DirectionMap.map[ i ].dirX );
                App.getPlayer().direction.setY( yDir );
            }
        }
    }

    /**
     * Signal that the player is not moving horizontally.
     */
    public void clearMovingX()
    {
        App.getPlayer().isMovingX = false;
        App.getPlayer().setXDirection( Movement._DIRECTION_STILL );
        App.getPlayer().speed.setX( 0 );
    }

    /**
     * Signal that the player is not moving vertically.
     */
    public void clearMovingY()
    {
        App.getPlayer().isMovingY = false;
        App.getPlayer().direction.setY( Movement._DIRECTION_STILL );
        App.getPlayer().speed.setY( 0 );
    }

    @Override
    public void dispose()
    {
        ABXYButtons.clear();
        ABXYButtons             = null;
        abxyButtonArrayIterator = null;
    }
}
