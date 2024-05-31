package com.richikin.platformania.ui.panels;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.enums.StateID;
import com.richikin.platformania.maths.SimpleVec2F;
import com.richikin.platformania.physics.Direction;
import com.richikin.platformania.ui.DefaultPanel;

public class SlidePanel extends DefaultPanel implements IPanel
{
    public SimpleVec2F  speed;
    public Direction    direction;
    public SimpleVec2F  distance;
    public SimpleVec2F  distanceReset;
    public ActionStates action;

    public SlidePanel()
    {
        super();
    }

    @Override
    public void initialise(TextureRegion objectRegion, String imageName, final Object... args)
    {
        this.textureRegion = objectRegion;
        this.nameID        = imageName;

        deactivate();

        this.action = ActionStates._NO_ACTION;

        setState(StateID._STATE_OPENING);
    }

    @Override
    public void set(SimpleVec2F xy, SimpleVec2F distance, Direction direction, SimpleVec2F speed)
    {
        setPosition(xy.getX(), xy.getY());

        this.distance.set(distance);
        this.distanceReset.set(distance);
        this.direction.set(direction);
        this.speed.set(speed);
    }

    @Override
    public boolean update()
    {
        if (getActiveState())
        {
            switch (getState())
            {
                case _STATE_OPENING:
                {
                    if (move())
                    {
                        setState(StateID._UPDATE);
                    }
                }
                break;

                case _STATE_CLOSING:
                {
                    if (move())
                    {
                        deactivate();

                        setState(StateID._STATE_CLOSED);
                    }
                }
                break;

                default:
                {
                }
                break;
            }
        }

        return !getActiveState();
    }

    private boolean updateReveal()
    {
        if (move())
        {
            deactivate();

            return true;
        }

        return false;
    }

    private boolean updateHide()
    {
        if (move())
        {
            deactivate();

            return true;
        }

        return false;
    }

    private boolean move()
    {
        if (distance.getX() > 0)
        {
            setPosition((int) (getPosition().x + (speed.getX() * direction.getX())), getPosition().y);
            distance.subX((int) Math.min(distance.getX(), speed.getX()));
        }

        if (distance.getY() > 0)
        {
            setPosition(getPosition().x, (int) (getPosition().y + (speed.getY() * direction.getY())));
            distance.subY((int) Math.min(distance.getY(), speed.getY()));
        }

        return distance.isEmpty();
    }
}
