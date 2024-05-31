package com.richikin.platformania.ui.panels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.platformania.enums.StateID;
import com.richikin.platformania.logging.StopWatch;
import com.richikin.platformania.maths.SimpleVec2F;
import com.richikin.platformania.physics.Direction;
import com.richikin.platformania.physics.Movement;
import com.richikin.platformania.ui.DefaultPanel;
import com.richikin.platformania.core.App;
import com.richikin.platformania.graphics.GameAssets;
import com.richikin.platformania.graphics.Gfx;

import java.util.concurrent.TimeUnit;

public class TextPanel extends DefaultPanel
{
    private SimpleVec2F speed;
    private Direction   direction;
    private Direction   directionReset;
    private SimpleVec2F distance;
    private SimpleVec2F distanceReset;
    private StopWatch   stopWatch;
    private int         delay;

    public TextPanel()
    {
        this( GameAssets._DEFAULT_TEXTPANEL_ASSET, 0, 0 );
    }

    public TextPanel( int x, int y )
    {
        this( GameAssets._DEFAULT_TEXTPANEL_ASSET, x, y );
    }

    public TextPanel( String assetName, int x, int y )
    {
        super();

        textureRegion  = App.getAssets().getTextRegion( assetName );
        nameID         = assetName;
        stopWatch      = new StopWatch();
        distance       = new SimpleVec2F();
        distanceReset  = new SimpleVec2F();
        direction      = new Direction();
        directionReset = new Direction();
        speed          = new SimpleVec2F();
        delay          = 3000;

        setPosition( x, ( Gfx._HUD_HEIGHT - y ) );

        setState( StateID._STATE_SETUP );
    }

    @Override
    public boolean update()
    {
        if ( isActive )
        {
            switch ( getState() )
            {
                case _STATE_SETUP:
                    direction.set( directionReset );
                    distance.set( distanceReset );
                    setState( StateID._STATE_OPENING );
                    break;

                // Move UP
                case _STATE_OPENING:
                    if ( move() )
                    {
                        distance.set( distanceReset );

                        if ( directionReset.getX() != Movement._DIRECTION_STILL )
                        {
                            direction.toggleX();
                        }

                        if ( directionReset.getY() != Movement._DIRECTION_STILL )
                        {
                            direction.toggleY();
                        }

                        stopWatch.reset();

                        setState( StateID._STATE_PAUSED );
                    }
                    break;

                case _STATE_PAUSED:
                    if ( stopWatch.time( TimeUnit.MILLISECONDS ) > delay )
                    {
                        setState( StateID._STATE_CLOSING );
                    }
                    break;

                // Move DOWN
                case _STATE_CLOSING:
                    if ( move() )
                    {
                        setState( StateID._STATE_SETUP );

                        deactivate();
                    }
                    break;
                default:
                    break;
            }
        }

        return false;
    }

    public void set( SimpleVec2F distance, Direction direction, SimpleVec2F speed )
    {
        this.distance.set( distance );
        this.distanceReset.set( distance );
        this.direction.set( direction );
        this.directionReset.set( direction );
        this.speed.set( speed );
    }

    public void setDelay( int delay )
    {
        this.delay = delay;
    }

    public void setTexture( String assetName )
    {
        textureRegion = App.getAssets().getTextRegion( assetName );
    }

    @Override
    public void draw( SpriteBatch spriteBatch )
    {
        if ( isActive )
        {
            spriteBatch.draw( textureRegion, getPosition().x, getPosition().y );
        }
    }

    private boolean move()
    {
        if ( distance.getX() > 0 )
        {
            setPosition( ( int ) ( getPosition().x + ( speed.getX() * direction.getX() ) ), getPosition().y );
            distance.subX( ( int ) Math.min( distance.getX(), speed.getX() ) );
        }

        if ( distance.getY() > 0 )
        {
            setPosition( getPosition().x, ( int ) ( getPosition().y + ( speed.getY() * direction.getY() ) ) );
            distance.subY( ( int ) Math.min( distance.getY(), speed.getY() ) );
        }

        return distance.isEmpty();
    }
}
