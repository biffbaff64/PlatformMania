package com.richikin.platformania.input;

import com.richikin.platformania.core.App;
import com.richikin.platformania.enums.ActionStates;

public class BButtonActions implements ABXYButton
{
    @Override
    public void update()
    {
        if (App.getHud().buttonB != null)
        {
            if (App.getHud().buttonB.isPressed())
            {
                process();
            }
        }
    }

    @Override
    public void process()
    {
        App.getPlayer().setActionState(ActionStates._FIGHTING);
    }
}
