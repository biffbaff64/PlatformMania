package com.richikin.platformania.entities.objects;

import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.core.App;
import com.richikin.platformania.physics.PhysicsBodyType;

public class SimpleEntity extends BoundedEntity
{
    private boolean         filtersSet;
    private PhysicsBodyType requestedBodyType;

    public SimpleEntity( GraphicID graphicID )
    {
        super( graphicID );

        filtersSet        = false;
        requestedBodyType = PhysicsBodyType._NONE;
    }

    @Override
    public void initialise( SpriteDescriptor descriptor )
    {
        create( descriptor );

        Trace._assert( !filtersSet, "Filters require setting!" );
        Trace._assert( requestedBodyType == PhysicsBodyType._NONE, "PhysicsBodyType not specified!" );

        createBody( requestedBodyType );

        isAnimating = true;
        isDrawable  = true;

        setActionState( ActionStates._STANDING );
    }

    @Override
    public void update()
    {
        animate();

        updateCommon();
    }

    @Override
    public void animate()
    {
        if ( gid == GraphicID.G_EYES )
        {
            if ( App.getPlayer().sprite.getX() < this.sprite.getX() )
            {
                sprite.setRegion( animFrames[ 0 ] );
            }
            else if ( App.getPlayer().sprite.getX() > ( this.sprite.getX() + frameWidth ) )
            {
                sprite.setRegion( animFrames[ 2 ] );
            }
            else
            {
                sprite.setRegion( animFrames[ 1 ] );
            }
        }
        else
        {
            super.animate();
        }
    }

    @Override
    public void definePhysicsBodyBox( boolean useBodyPos )
    {
        if ( getGID() == GraphicID.G_FLAMES )
        {
            getPhysicsBody().bodyBox.set
                (
                    sprite.getX(),
                    sprite.getY(),
                    frameWidth,
                    26
                );
        }
        else
        {
            super.definePhysicsBodyBox( useBodyPos );
        }
    }

    public void setFilters( short category, short collisionMask )
    {
        bodyCategory = category;
        collidesWith = collisionMask;

        filtersSet = true;
    }

    public void setRequestedBodyType( PhysicsBodyType physicsBodyType )
    {
        requestedBodyType = physicsBodyType;
    }
}
