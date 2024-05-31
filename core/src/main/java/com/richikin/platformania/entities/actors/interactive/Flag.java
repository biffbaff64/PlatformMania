package com.richikin.platformania.entities.actors.interactive;

import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.core.App;
import com.richikin.platformania.entities.objects.GdxSprite;
import com.richikin.platformania.entities.objects.SpriteDescriptor;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.physics.PhysicsBodyType;

public class Flag extends GdxSprite
{
    public boolean isChecked;

    public Flag()
    {
        super( GraphicID.G_FLAG );
    }

    @Override
    public void initialise( SpriteDescriptor descriptor )
    {
        create( descriptor );

        bodyCategory = Gfx.CAT_INTERACTIVE;
        collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_GROUND;

        createBody( PhysicsBodyType._STATIC_SENSOR );

        App.getMapData().checkPointList.add( getBodyBox() );

        isAnimating = true;
        isDrawable = true;
        isChecked = false;

        setActionState( ActionStates._STANDING );
    }

    @Override
    public void update()
    {
        if ( !isChecked )
        {
            if ( getBodyBox().contains( App.getPlayer().getBodyX(), App.getPlayer().getBodyY() ) )
            {
                setAnimation( App.getEntities().getDescriptor( GraphicID.G_GREEN_FLAG ) );
                isChecked = true;
            }
        }

        animate();

        updateCommon();
    }
}
