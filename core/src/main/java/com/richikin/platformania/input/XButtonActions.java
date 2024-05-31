package com.richikin.platformania.input;

import com.richikin.platformania.core.App;

public class XButtonActions implements ABXYButton
{
    @Override
    public void update()
    {
        if ( App.getHud().buttonX != null )
        {
            if ( App.getHud().buttonX.isPressed() )
            {
                process();
            }
            else
            {
                App.getPlayer().isHolding  = false;
                App.getPlayer().heldEntity = null;
            }
        }
    }

    @Override
    public void process()
    {
        App.getPlayer().isHolding = true;

        if ( App.getPlayer().heldEntity != null )
        {
            App.getPlayer().heldEntity.isHeld = App.getPlayer().heldEntity.isTouchingPlayer;
        }
    }
}
