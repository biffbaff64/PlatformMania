package com.richikin.platformania.entities.objects;

import com.richikin.platformania.enums.GraphicID;

public class EntityCounts
{
    public final GraphicID graphicID;
    public final int       currentTotal;
    public final int       maxTotal;

    /**
     * Constructor.
     *
     * @param gid          The {@link GraphicID} of the entity to track.
     * @param currentTotal Initial value for currently active entities.
     * @param maxTotal     Initializer for maximum allowed. This may initially
     *                     be set to zero, and then set correctly later.
     */
    public EntityCounts(GraphicID gid, int currentTotal, int maxTotal)
    {
        this.graphicID    = gid;
        this.currentTotal = currentTotal;
        this.maxTotal     = maxTotal;
    }
}
