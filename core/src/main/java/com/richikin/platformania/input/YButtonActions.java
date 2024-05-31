package com.richikin.platformania.input;

import com.richikin.platformania.enums.StateID;
import com.richikin.platformania.core.App;
import com.richikin.platformania.ui.ItemBar;

public class YButtonActions implements ABXYButton
{
    @Override
    public void update()
    {
        if ( App.getHud().buttonY != null )
        {
            if ( App.getHud().buttonY.isPressed() )
            {
                process();
                App.getHud().buttonY.release();
            }
        }
    }

    @Override
    public void process()
    {
        if ( App.getAppState().peek() == StateID._STATE_GAME )
        {
            App.getHud().setItemPanelIndex( ( App.getHud().getItemPanelIndex() + 1 ) % ItemBar._NUM_ITEM_PANELS );
        }
    }
}
