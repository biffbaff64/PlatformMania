package com.richikin.platformania.input;

import com.badlogic.gdx.math.Vector2;
import com.richikin.platformania.config.GdxSystem;
import com.richikin.platformania.core.App;
import com.richikin.platformania.input.controllers.ControllerType;

public class InputUtils
{
    public static float getJoystickAngle()
    {
        return getJoystickVector().angleDeg();
    }

    public static Vector2 getJoystickVector()
    {
        float xPerc = App.getInputManager().getVirtualJoystick().getTouchpad().getKnobPercentX();
        float yPerc = App.getInputManager().getVirtualJoystick().getTouchpad().getKnobPercentY();

        Vector2 vector2 = new Vector2( xPerc, yPerc );

        return vector2.rotate90( -1 );
    }

    public static boolean isInputAvailable( ControllerType _inputType )
    {
        return App.getAppConfig().availableInputs.contains( _inputType, true );
    }
}
