package com.richikin.platformania.entities.actors.interactive;

import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.maths.SimpleVec2F;
import com.richikin.platformania.core.App;
import com.richikin.platformania.entities.objects.FixedPathEntity;
import com.richikin.platformania.entities.objects.SpriteDescriptor;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.physics.PhysicsBodyType;

public class MovingPlatform extends FixedPathEntity
{
    public MovingPlatform()
    {
        super( GraphicID.G_MOVING_PLATFORM );
    }

    @Override
    public void initialise( SpriteDescriptor descriptor )
    {
        create( descriptor );

        setFilters();

        createBody( PhysicsBodyType._KINEMATIC );

        direction.set( descriptor._DIR );
        distance.set( descriptor._DIST );
        distance.mul( App.getMapData().tileWidth, App.getMapData().tileHeight );
        distanceReset = new SimpleVec2F( distance );
        speed.set( descriptor._SPEED );

        setMovementBounds();

        canFlip     = false;
        isAnimating = true;
        isDrawable  = true;

        setActionState( ActionStates._STANDING );
    }

    @Override
    public void setFilters()
    {
        bodyCategory = Gfx.CAT_PLATFORM;
        collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_GROUND | Gfx.CAT_USABLE | Gfx.CAT_MOBILE_ENEMY | Gfx.CAT_WEAPON;
    }
}
