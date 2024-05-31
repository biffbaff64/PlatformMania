package com.richikin.platformania.input;

public class TouchScreen
{
    public boolean titleScreenTouchDown(int screenX, int screenY)
    {
        return false;
    }

    public boolean titleScreenTouchUp(int screenX, int screenY)
    {
        return false;
    }

    public boolean gameScreenTouchDown(int screenX, int screenY, int pointer)
    {

//        if ( App.getAppConfig().availableInputs.contains( ControllerType._VIRTUAL, true))
//        {
//            if (((GameButton) App.getHud().buttonA).contains(screenX, screenY))
//            {
//                Trace.info( "BUTTON A" );
//
//                App.getHud().buttonA.press();
//                returnFlag = true;
//            }
//
//            if (((GameButton) App.getHud().buttonB).contains(screenX, screenY))
//            {
//                Trace.info( "BUTTON B" );
//
//                App.getHud().buttonB.press();
//                returnFlag = true;
//            }
//
//            if (((GameButton) App.getHud().buttonX).contains(screenX, screenY))
//            {
//                Trace.info( "BUTTON X" );
//
//                App.getHud().buttonX.press();
//                returnFlag = true;
//            }
//
//            if (((GameButton) App.getHud().buttonY).contains(screenX, screenY))
//            {
//                Trace.info( "BUTTON Y" );
//
//                App.getHud().buttonY.press();
//                returnFlag = true;
//            }
//        }

        return false;
    }

    public boolean gameScreenTouchUp(int screenX, int screenY)
    {

//        if ( App.getAppConfig().availableInputs.contains( ControllerType._VIRTUAL, true))
//        {
//            if (((GameButton) App.getHud().buttonA).contains(screenX, screenY))
//            {
//                App.getHud().buttonA.release();
//                returnFlag = true;
//            }
//
//            if (((GameButton) App.getHud().buttonB).contains(screenX, screenY))
//            {
//                App.getHud().buttonB.release();
//                returnFlag = true;
//            }
//
//            if (((GameButton) App.getHud().buttonX).contains(screenX, screenY))
//            {
//                App.getHud().buttonX.release();
//                returnFlag = true;
//            }
//
//            if (((GameButton) App.getHud().buttonY).contains(screenX, screenY))
//            {
//                App.getHud().buttonY.release();
//                returnFlag = true;
//            }
//        }

        return false;
    }
}
