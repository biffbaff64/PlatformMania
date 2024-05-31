package com.richikin.platformania.entities.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.enums.GraphicID;

public class AnimID
{
    public final ActionStates       state;
    public final GraphicID          gid;
    public final float              animRate;
    public final Animation.PlayMode playMode;

    public AnimID( ActionStates _state, GraphicID _gid, float _rate )
    {
        this.state    = _state;
        this.gid      = _gid;
        this.animRate = _rate;
        this.playMode = null;
    }

    public AnimID( ActionStates _state, GraphicID _gid, float _rate, Animation.PlayMode _playMode )
    {
        this.state    = _state;
        this.gid      = _gid;
        this.animRate = _rate;
        this.playMode = _playMode;
    }
}
