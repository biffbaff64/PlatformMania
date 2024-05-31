package com.richikin.platformania.input;

import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.core.App;

public class AButtonActions implements ABXYButton
{
    public static final float _JUMP_HEIGHT = ( 16 * 2.5f );

    @Override
    public void update()
    {
        if ( App.getHud().buttonA != null )
        {
            if ( App.getHud().buttonA.isPressed() )
            {
                process();
                App.getHud().buttonA.release();
            }
        }
    }

    @Override
    public void process()
    {
        if ( App.getPlayer().getActionState() != ActionStates._CROUCHING )
        {
            if ( App.getPlayer().getPhysicsBody().contactCount > 0 )
            {
                App.getPlayer()
                    .getPhysicsBody()
                    .body
                    .setLinearVelocity
                        (
                            App.getPlayer().getPhysicsBody().body.getLinearVelocity().x,
                            _JUMP_HEIGHT
                        );

                App.getPlayer().isMovingY = true;
                App.getPlayer().setActionState( ActionStates._JUMPING );

                App.getHud().buttonDown.release();

                App.getPlayer().isOnPlatform = false;
                App.getPlayer().setLink( 0 );
            }
        }
    }
}
