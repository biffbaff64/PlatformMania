package com.richikin.platformania.input.controllers;

public class ShanwanPS3PCWiredPad implements ControlMapData
{
    public static final String _NAME = "ShanWan PS3/PC Wired GamePad";

    public static final float _MIN_RANGE          = -1.0f;
    public static final float _MAX_RANGE          = 1.0f;
    public static final float _DEAD_ZONE          = 0.5f;
    public static final int   _BUTTON_X           = 2;
    public static final int   _BUTTON_Y           = 3;
    public static final int   _BUTTON_A           = 0;
    public static final int   _BUTTON_B           = 1;
    public static final int   _BUTTON_BACK        = 4;
    public static final int   _BUTTON_START       = 6;
    public static final int   _BUTTON_LB          = 9;
    public static final int   _BUTTON_RB          = 10;
    public static final int   _BUTTON_L3          = 7;
    public static final int   _BUTTON_R3          = 8;
    public static final int   _AXIS_LEFT_X        = 1; // -1 is left | +1 is right
    public static final int   _AXIS_LEFT_Y        = 0; // -1 is up | +1 is down
    public static final int   _AXIS_RIGHT_X       = 3; // -1 is left | +1 is right
    public static final int   _AXIS_RIGHT_Y       = 2; // -1 is up | +1 is down
    public static final int   _AXIS_LEFT_TRIGGER  = 4; // value 0 to 1f
    public static final int   _AXIS_RIGHT_TRIGGER = 4; // value 0 to -1f
    public static final int   _DPAD_UP            = 11;
    public static final int   _DPAD_DOWN          = 12;
    public static final int   _DPAD_LEFT          = 13;
    public static final int   _DPAD_RIGHT         = 14;
    public static final int   _LEFT_TRIGGER       = 4;
    public static final int   _RIGHT_TRIGGER      = 4;
}
