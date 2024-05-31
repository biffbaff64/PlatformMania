package com.richikin.platformania.config;

import com.richikin.platformania.logging.SystemMeters;

public enum Meters
{
    //@formatter:off
    // -------------------------------------------
    _VALUE              (SystemMeters._MAX_SYSTEM_METERS.get()),
    // -------------------------------------------
    // Add Game specific meters here.
    _BAD_PLAYER_ACTION  (_VALUE.meterNum),

    // -------------------------------------------
    _DUMMY_METER        (_VALUE.meterNum+1);
    //@formatter:on

    private int meterNum;

    Meters(int value)
    {
        setMeterNum(value);
    }

    public static Meters fromValue(int value)
    {
        Meters[] meters = values();

        for (Meters meter : meters)
        {
            if (meter.get() == value)
            {
                return meter;
            }
        }

        return _DUMMY_METER;
    }

    public int get()
    {
        return meterNum;
    }

    private void setMeterNum(int meterNum)
    {
        this.meterNum = meterNum;
    }
}
