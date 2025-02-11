package com.richikin.platformania.entities.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationComponent implements Component
{
    public Animation< TextureRegion > animation;
    public float                      elapsedAnimTime;
    public TextureRegion[]            animFrames;
    public boolean                    isAnimating;
    public boolean                    isLooping;
    public int                        frameWidth;
    public int                        frameHeight;
}
