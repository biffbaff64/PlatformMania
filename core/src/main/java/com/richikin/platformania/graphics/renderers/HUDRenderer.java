package com.richikin.platformania.graphics.renderers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.platformania.core.App;
import com.richikin.platformania.config.GdxSystem;
import com.richikin.platformania.graphics.camera.OrthoGameCamera;
import com.richikin.platformania.input.controllers.ControllerType;

public class HUDRenderer
{
    public HUDRenderer()
    {
    }

    public void render(SpriteBatch spriteBatch, OrthoGameCamera hudCamera)
    {
        if (!App.getAppConfig().shutDownActive)
        {
            switch (App.getAppState().peek())
            {
                case _STATE_MAIN_MENU:
                {
                    if (App.getTitleScene() != null)
                    {
                        App.getTitleScene().draw(spriteBatch, hudCamera);
                    }
                }
                break;

                case _STATE_SETUP:
                case _STATE_GET_READY:
                case _STATE_PAUSED:
                case _STATE_PREPARE_LEVEL_FINISHED:
                case _STATE_LEVEL_FINISHED:
                case _STATE_LEVEL_RETRY:
                case _STATE_GAME:
                case _STATE_MESSAGE_PANEL:
                case _STATE_WELCOME_PANEL:
                case _STATE_DEBUG_HANG:
                case _STATE_GAME_OVER:
                {
                    if (App.getHud() != null)
                    {
                        App.getHud().render(hudCamera, (App.getAppConfig().availableInputs.contains(ControllerType._JOYSTICK, true)));
                    }
                }
                break;

                case _STATE_GAME_FINISHED:
                {
                    if (App.getHud() != null)
                    {
                        App.getHud().render(hudCamera, false);
                    }
                }
                break;

                case _STATE_CLOSING:
                default:
                {
                }
                break;
            }
        }
    }
}
