package com.richikin.platformania.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.richikin.platformania.config.Settings;
import com.richikin.platformania.core.App;
import com.richikin.platformania.physics.Dir;
import com.richikin.platformania.physics.Direction;
import com.richikin.platformania.physics.DirectionValue;
import com.richikin.platformania.physics.Movement;

public class Keyboard extends InputAdapter
{
    // =================================================================
    // DEFAULT Keyboard options.
    //
    public static final int defaultValueUp       = Input.Keys.W;
    public static final int defaultValueDown     = Input.Keys.S;
    public static final int defaultValueLeft     = Input.Keys.A;
    public static final int defaultValueRight    = Input.Keys.D;
    public static final int defaultValueA        = Input.Keys.NUMPAD_2;
    public static final int defaultValueB        = Input.Keys.NUMPAD_6;
    public static final int defaultValueX        = Input.Keys.NUMPAD_1;
    public static final int defaultValueY        = Input.Keys.NUMPAD_5;
    public static final int defaultValueHudInfo  = Input.Keys.F9;
    public static final int defaultValuePause    = Input.Keys.ESCAPE;
    public static final int defaultValueSettings = Input.Keys.F10;

    public boolean ctrlButtonHeld;
    public boolean shiftButtonHeld;

    private final Vector2 lastTouchPoint;

    public Keyboard()
    {
        ctrlButtonHeld  = false;
        shiftButtonHeld = false;
        lastTouchPoint  = new Vector2();
    }

    @Override
    public boolean keyDown( int keycode )
    {
        boolean returnFlag = false;

        if ( App.getAppConfig().isDesktopApp() )
        {
            if ( App.getAppConfig().gameScreenActive() )
            {
                returnFlag = maingameKeyDown( keycode );
            }
        }

        return returnFlag;
    }

    @Override
    public boolean keyUp( int keycode )
    {
        boolean returnFlag = false;

        if ( App.getAppConfig().isDesktopApp() )
        {
            if ( App.getAppConfig().gameScreenActive() )
            {
                returnFlag = maingameKeyUp( keycode );
            }
        }

        return returnFlag;
    }

    public boolean maingameKeyDown( int keycode )
    {
        boolean returnFlag = false;

        if ( keycode == defaultValueLeft )
        {
            App.getInputManager().setHorizontalValue( Movement._DIRECTION_LEFT );
            App.getHud().buttonLeft.press();
            returnFlag = true;
        }
        else if ( keycode == defaultValueRight )
        {
            App.getInputManager().setHorizontalValue( Movement._DIRECTION_RIGHT );
            App.getHud().buttonRight.press();
            returnFlag = true;
        }
        else if ( keycode == defaultValueUp )
        {
            App.getInputManager().setVerticalValue( Movement._DIRECTION_UP );
            App.getHud().buttonUp.press();
            returnFlag = true;
        }
        else if ( keycode == defaultValueDown )
        {
            App.getInputManager().setVerticalValue( Movement._DIRECTION_DOWN );
            App.getHud().buttonDown.press();
            returnFlag = true;
        }
        else if ( keycode == defaultValueA )
        {
            App.getHud().buttonA.press();
            returnFlag = true;
        }
        else if ( keycode == defaultValueB )
        {
            App.getHud().buttonB.press();
            returnFlag = true;
        }
        else if ( keycode == defaultValueX )
        {
            App.getHud().buttonX.press();
            returnFlag = true;
        }
        else if ( keycode == defaultValueY )
        {
            App.getHud().buttonY.press();
            returnFlag = true;
        }

        if ( returnFlag )
        {
            evaluateKeyboardDirection();
        }

        if ( !returnFlag )
        {
            switch ( keycode )
            {
                case Input.Keys.ESCAPE:
                case Input.Keys.BACK:
                {
                    App.getHud().buttonPause.press();

                    returnFlag = true;
                }
                break;

                case Input.Keys.SHIFT_LEFT:
                case Input.Keys.SHIFT_RIGHT:
                {
                    shiftButtonHeld = true;
                    returnFlag      = true;
                }
                break;

                case Input.Keys.CONTROL_LEFT:
                case Input.Keys.CONTROL_RIGHT:
                {
                    ctrlButtonHeld = true;
                    returnFlag     = true;
                }
                break;

                case Input.Keys.F1:
                {
                    if ( App.getDev().isDevMode() )
                    {
                        App.getWorldModel().canDrawDebug = !App.getWorldModel().canDrawDebug;
                    }

                    returnFlag = true;
                }
                break;

                case Input.Keys.F2:
                {
                    if ( App.getDev().isDevMode() )
                    {
                        App.getBaseRenderer().resetCameraZoom();
                    }

                    returnFlag = true;
                }
                break;

                case Input.Keys.F12:
                {
                    App.getSettings().toggleState( Settings._SHOW_DEBUG );

                    returnFlag = true;
                }
                break;

                case Input.Keys.NUMPAD_ADD:
                {
                    if ( shiftButtonHeld )
                    {
                        App.getBaseRenderer().getGameZoom().out( 0.10f );
                    }
                }
                break;

                case Input.Keys.NUMPAD_SUBTRACT:
                {
                    if ( shiftButtonHeld )
                    {
                        App.getBaseRenderer().getGameZoom().in( 0.10f );
                    }
                }
                break;

                case Input.Keys.MENU:
                case Input.Keys.HOME:
                default:
                {
                }
                break;
            }
        }

        return returnFlag;
    }

    public boolean maingameKeyUp( int keycode )
    {
        boolean returnFlag = false;

        if ( keycode == defaultValueLeft )
        {
            App.getInputManager().clearHorizontalValue();
            App.getHud().buttonLeft.release();
            returnFlag = true;
        }
        else if ( keycode == defaultValueRight )
        {
            App.getInputManager().clearHorizontalValue();
            App.getHud().buttonRight.release();
            returnFlag = true;
        }
        else if ( keycode == defaultValueUp )
        {
            App.getInputManager().clearVerticalValue();
            App.getHud().buttonUp.release();
            returnFlag = true;
        }
        else if ( keycode == defaultValueDown )
        {
            App.getInputManager().clearVerticalValue();
            App.getHud().buttonDown.release();
            returnFlag = true;
        }
        else if ( keycode == defaultValueA )
        {
            App.getHud().buttonA.release();
            returnFlag = true;
        }
        else if ( keycode == defaultValueB )
        {
            App.getHud().buttonB.release();
            returnFlag = true;
        }
        else if ( keycode == defaultValueX )
        {
            App.getHud().buttonX.release();
            returnFlag = true;
        }
        else if ( keycode == defaultValueY )
        {
            App.getHud().buttonY.release();
            returnFlag = true;
        }

        if ( returnFlag )
        {
            evaluateKeyboardDirection();
        }

        if ( !returnFlag )
        {
            switch ( keycode )
            {
                case Input.Keys.ESCAPE:
                case Input.Keys.BACK:
                {
                    App.getHud().buttonPause.release();

                    returnFlag = true;
                }
                break;

                case Input.Keys.SHIFT_LEFT:
                case Input.Keys.SHIFT_RIGHT:
                {
                    shiftButtonHeld = false;
                    returnFlag      = true;
                }
                break;

                case Input.Keys.CONTROL_LEFT:
                case Input.Keys.CONTROL_RIGHT:
                {
                    ctrlButtonHeld = false;
                    returnFlag     = true;
                }
                break;

                case Input.Keys.MENU:
                case Input.Keys.HOME:
                default:
                {
                    returnFlag = true;
                }
                break;
            }
        }

        return returnFlag;
    }

    @Override
    public boolean touchDown( int touchX, int touchY, int pointer, int button )
    {
        Vector2 newPoints = new Vector2( touchX, touchY );
        newPoints = App.getBaseRenderer().getHudGameCamera().viewport.unproject( newPoints );

        //        int screenX = (int) ( newPoints.x - App.getMapData().mapPosition.getX() );
        //        int screenY = (int) ( newPoints.y - App.getMapData().mapPosition.getY() );
        //
        //        lastTouchPoint.set( screenX, screenY );

        boolean returnFlag;

        if ( App.getAppConfig().gameScreenActive() )
        {
            returnFlag = App.getInputManager().getTouchScreen().gameScreenTouchDown( (int) newPoints.x, (int) newPoints.y, pointer );
        }
        else
        {
            returnFlag = App.getInputManager().getTouchScreen().titleScreenTouchDown( (int) newPoints.x, (int) newPoints.y );
        }

        return returnFlag;
    }

    @Override
    public boolean touchUp( int touchX, int touchY, int pointer, int button )
    {
        Vector2 newPoints = new Vector2( touchX, touchY );
        newPoints = App.getBaseRenderer().getHudGameCamera().viewport.unproject( newPoints );

        //        int screenX = (int) ( newPoints.x - App.getMapData().mapPosition.getX() );
        //        int screenY = (int) ( newPoints.y - App.getMapData().mapPosition.getY() );

        boolean returnFlag;

        if ( App.getAppConfig().gameScreenActive() )
        {
            returnFlag = App.getInputManager().getTouchScreen().gameScreenTouchUp( (int) newPoints.x, (int) newPoints.y );
        }
        else
        {
            returnFlag = App.getInputManager().getTouchScreen().titleScreenTouchUp( (int) newPoints.x, (int) newPoints.y );
        }

        return returnFlag;
    }

    @Override
    public boolean keyTyped( char character )
    {
        return false;
    }

    @Override
    public boolean touchDragged( int screenX, int screenY, int pointer )
    {
        Vector2 newPoints = new Vector2( screenX, screenY );
        newPoints = App.getBaseRenderer().getSpriteGameCamera().viewport.unproject( newPoints );

        int touchX = (int) ( newPoints.x - App.getMapData().mapPosition.getX() );
        int touchY = (int) ( newPoints.y - App.getMapData().mapPosition.getY() );

        boolean returnFlag = false;

        if ( App.getAppConfig().gameScreenActive() )
        {
            Vector2 newTouch = new Vector2( touchX, touchY );
            Vector2 delta    = newTouch.cpy().sub( lastTouchPoint );

            // delta.x > 0 means the touch moved to the right.
            // delta.x < 0 means the touch moved to the left.
            // delta.x == 0 means no movement.

            lastTouchPoint.set( touchX, touchY );
        }

        return returnFlag;
    }

    /**
     * Process a movement of the mouse pointer.
     * Not called if any mouse button pressed.
     * Not called on iOS builds.
     *
     * @param screenX - new X coordinate.
     * @param screenY - new Y coordinate.
     * @return boolean indicating whether or not the input
     * was processed.
     */
    @Override
    public boolean mouseMoved( int screenX, int screenY )
    {
        Vector2 newPoints = new Vector2( screenX, screenY );
        newPoints = App.getBaseRenderer().getHudGameCamera().viewport.unproject( newPoints );

        App.getInputManager().getMouseWorldPosition().set( newPoints.x, newPoints.y );

        int touchX = (int) ( newPoints.x - App.getMapData().mapPosition.getX() );
        int touchY = (int) ( newPoints.y - App.getMapData().mapPosition.getY() );

        App.getInputManager().getMousePosition().set( touchX, touchY );

        return false;
    }

    /**
     * React to the mouse wheel scrolling
     *
     * @param amountX - scroll amount.
     *                - amount < 0 == scroll left.
     *                - amount > 0 == scroll right.
     * @param amountY - scroll amount.
     *                - amount < 0 == scroll down.
     *                - amount > 0 == scroll up.
     * @return boolean indicating whether or not the input
     * was processed.
     */
    @Override
    public boolean scrolled( float amountX, float amountY )
    {
        if ( App.getAppConfig().gameScreenActive() )
        {
            if ( App.getDev().isDevMode() )
            {
                if ( ctrlButtonHeld )
                {
                    if ( amountY < 0 )
                    {
                        App.getBaseRenderer().getGameZoom().out( 0.10f );
                    }
                    else if ( amountY > 0 )
                    {
                        App.getBaseRenderer().getGameZoom().in( 0.10f );
                    }
                }

                if ( shiftButtonHeld )
                {
                    if ( amountY < 0 )
                    {
                        App.getBaseRenderer().getHudZoom().out( 0.10f );
                    }
                    else if ( amountY > 0 )
                    {
                        App.getBaseRenderer().getHudZoom().in( 0.10f );
                    }
                }
            }
        }

        return false;
    }

    public void evaluateKeyboardDirection()
    {
        Direction direction = new Direction
            (
                (int) App.getInputManager().getHorizontalValue(),
                (int) App.getInputManager().getVerticalValue()
            );

        Dir keyDir = DirectionMap.map[ DirectionMap.map.length - 1 ].translated;

        for ( DirectionValue dv : DirectionMap.map )
        {
            if ( ( dv.dirX == direction.getX() ) && ( dv.dirY == direction.getY() ) )
            {
                keyDir = dv.translated;
            }
        }

        App.getInputManager().setLastRegisteredDirection( keyDir );
    }
}
