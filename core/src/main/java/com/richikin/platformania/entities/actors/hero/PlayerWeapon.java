package com.richikin.platformania.entities.actors.hero;

import com.richikin.platformania.enums.ActionStates;
import com.richikin.platformania.enums.GraphicID;
import com.richikin.platformania.core.App;
import com.richikin.platformania.entities.objects.GdxSprite;
import com.richikin.platformania.entities.objects.SpriteDescriptor;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.physics.PhysicsBodyType;

public class PlayerWeapon extends GdxSprite
{
    public PlayerWeapon()
    {
        super( GraphicID.G_PLAYER_KNIFE );
    }

    @Override
    public void initialise( SpriteDescriptor descriptor )
    {
        create( descriptor );

        bodyCategory = Gfx.CAT_WEAPON;
        collidesWith = Gfx.CAT_ENEMY | Gfx.CAT_USABLE | Gfx.CAT_OBSTACLE;

        createBody( PhysicsBodyType._DYNAMIC );

        getPhysicsBody().body.setGravityScale( 0 );

        speed.set( Gfx._PPM * 1.5f, 0 );
        direction.set( App.getPlayer().lookingAt );
        distance.set( speed.getX() * 80, 0 );

        isAnimating = false;
        isDrawable = true;

        setActionState( ActionStates._RUNNING );
    }

    @Override
    public void update()
    {
        if ( getActionState() == ActionStates._RUNNING )
        {
            getPhysicsBody().body.setLinearVelocity
                (
                    speed.getX() * direction.getX(),
                    speed.getY() * direction.getY()
                );

            distance.subX( speed.getX() );

            if ( !App.getEntityUtils().isOnScreen( this )
                || ( distance.getX() <= 0 ) )
            {
                setActionState( ActionStates._DYING );
            }
        }
        else if ( getActionState() == ActionStates._DYING )
        {
            isAnimating = true;
            getPhysicsBody().body.setLinearVelocity( 0, 0 );

            if ( animation.isAnimationFinished( elapsedAnimTime ) )
            {
                setActionState( ActionStates._DEAD );
            }
        }

        super.update();
    }

    @Override
    public void animate()
    {
        if ( !isAnimating )
        {
            sprite.setRegion( animFrames[ 0 ] );
        }

        super.animate();
    }
}
