package com.richikin.platformania.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.TimeUtils;
import com.richikin.platformania.config.GdxSystem;
import com.richikin.platformania.logging.Trace;
import com.richikin.platformania.ui.Scene2DUtils;
import com.richikin.platformania.core.App;
import com.richikin.platformania.graphics.Gfx;
import com.richikin.platformania.ui.UIPage;

import java.util.Calendar;
import java.util.Date;

public class MenuPage implements UIPage
{
    public ImageButton buttonStart;
    public ImageButton buttonOptions;
    public ImageButton buttonCredits;
    public ImageButton buttonExit;

    private static final int _START            = 0;
    private static final int _OPTIONS          = 1;
    private static final int _CREDITS          = 2;
    private static final int _EXIT             = 3;
    private static final int _POPPY            = 4;
    private static final int _XMAS_TREE        = 5;
    private static final int _INDICATOR_FRAMES = 6;

    //@formatter:off
    private final int[][] displayPos =
        {
            { 470, 435, 339,  35 },       //
            { 526, 495, 228,  35 },       //
            { 526, 556, 228,  35 },       //
            { 578, 615, 123,  35 },       //
            { 512, 120,   0,   0 },       //
            {   0,   0,   0,   0 },       //
        };
    //@formatter:on

    private Scene2DUtils               scene2DUtils;
    private Image                      decoration;
    private Sprite                     leftIndicator;
    private Sprite                     rightIndicator;
    private TextureRegion[]            indicatorFrames;
    private Animation< TextureRegion > indicatorAnim;
    private float                      elapsedAnimTime;
    private boolean                    indicatorDrawable;
    private int                        indicatorIndex;
    private Rectangle[]                buttonBoxes;

    public MenuPage()
    {
    }

    @Override
    public void initialise()
    {
        Trace.checkPoint();

        scene2DUtils = new Scene2DUtils();

        createButtons();

        addDateSpecificItems( false );

        createIndicator();
    }

    @Override
    public boolean update()
    {
        Vector2 vec2 = App.getInputManager().getMouseWorldPosition();

        for ( int i = 0; i < buttonBoxes.length; i++ )
        {
            if ( buttonBoxes[ i ].contains( vec2 ) )
            {
                indicatorIndex = i;
            }
        }


        return false;
    }

    @Override
    public void draw( SpriteBatch spriteBatch )
    {
        if ( indicatorDrawable )
        {
            leftIndicator.setRegion( indicatorAnim.getKeyFrame( elapsedAnimTime, true ) );
            rightIndicator.setRegion( indicatorAnim.getKeyFrame( elapsedAnimTime, true ) );

            leftIndicator.setPosition
                (
                    displayPos[ indicatorIndex ][ 0 ] - 62,
                    Gfx._HUD_HEIGHT - ( displayPos[ indicatorIndex ][ 1 ] ) - 6
                );

            rightIndicator.setPosition
                (
                    ( displayPos[ indicatorIndex ][ 0 ] + displayPos[ indicatorIndex ][ 2 ] ) + 10,
                    Gfx._HUD_HEIGHT - ( displayPos[ indicatorIndex ][ 1 ] ) - 6
                );

            leftIndicator.setFlip( false, false );
            rightIndicator.setFlip( true, false );

            leftIndicator.draw( spriteBatch );
            rightIndicator.draw( spriteBatch );

            elapsedAnimTime += Gdx.graphics.getDeltaTime();
        }
    }

    @Override
    public void show()
    {
        Trace.checkPoint();

        showItems( true );

        App.getAppConfig().hideAndDisableBackButton();
    }

    @Override
    public void hide()
    {
        Trace.checkPoint();

        showItems( false );
    }

    @SuppressWarnings("SameParameterValue")
    private void addDateSpecificItems( boolean force )
    {
        // TODO: 29/12/2020 - Add New Years Day, Mother Goddess Day

        Date     date     = new Date( TimeUtils.millis() );
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( date );

        Scene2DUtils scene2DUtils = new Scene2DUtils();

        if ( ( calendar.get( Calendar.MONTH ) == Calendar.NOVEMBER ) || force )
        {
            if ( ( calendar.get( Calendar.DAY_OF_MONTH ) == 11 ) || force )
            {
                decoration = scene2DUtils.makeObjectsImage( "poppy" );
                decoration.setPosition( displayPos[ _POPPY ][ 0 ], ( Gfx._HUD_HEIGHT - displayPos[ _POPPY ][ 1 ] ) );
                App.getStage().addActor( decoration );
            }
        }

        if ( ( calendar.get( Calendar.MONTH ) == Calendar.DECEMBER ) || force )
        {
            if ( ( calendar.get( Calendar.DAY_OF_MONTH ) == 25 ) || force )
            {
                decoration = scene2DUtils.makeObjectsImage( "xmas_tree" );
                decoration.setPosition( displayPos[ _XMAS_TREE ][ 0 ], ( Gfx._HUD_HEIGHT - displayPos[ _XMAS_TREE ][ 0 ] ) );
                App.getStage().addActor( decoration );
            }
        }
    }

    private void showItems( boolean visible )
    {
        if ( buttonStart != null )
        {
            buttonStart.setVisible( visible );
        }

        if ( buttonOptions != null )
        {
            buttonOptions.setVisible( visible );
        }

        if ( buttonCredits != null )
        {
            buttonCredits.setVisible( visible );
        }

        if ( buttonExit != null )
        {
            buttonExit.setVisible( visible );
        }

        if ( decoration != null )
        {
            decoration.setVisible( visible );
        }

        indicatorDrawable = visible;
    }

    private void createButtons()
    {
        buttonStart = scene2DUtils.addButton
            (
                "buttonStart",
                "buttonStart_pressed",
                displayPos[ _START ][ 0 ],
                ( Gfx._HUD_HEIGHT - displayPos[ _START ][ 1 ] )
            );

        buttonOptions = scene2DUtils.addButton
            (
                "buttonOptions",
                "buttonOptions_pressed",
                displayPos[ _OPTIONS ][ 0 ],
                ( Gfx._HUD_HEIGHT - displayPos[ _OPTIONS ][ 1 ] )
            );

        buttonCredits = scene2DUtils.addButton
            (
                "buttonCredits",
                "buttonCredits_pressed",
                displayPos[ _CREDITS ][ 0 ],
                ( Gfx._HUD_HEIGHT - displayPos[ _CREDITS ][ 1 ] )
            );

        buttonExit = scene2DUtils.addButton
            (
                "buttonExit",
                "buttonExit_pressed",
                displayPos[ _EXIT ][ 0 ],
                ( Gfx._HUD_HEIGHT - displayPos[ _EXIT ][ 1 ] )
            );

        buttonBoxes             = new Rectangle[ 4 ];
        buttonBoxes[ _START ]   = new Rectangle
            (
                displayPos[ _START ][ 0 ], ( Gfx._HUD_HEIGHT - displayPos[ _START ][ 1 ] ),
                displayPos[ _START ][ 2 ], displayPos[ _START ][ 3 ]
            );
        buttonBoxes[ _OPTIONS ] = new Rectangle
            (
                displayPos[ _OPTIONS ][ 0 ], ( Gfx._HUD_HEIGHT - displayPos[ _OPTIONS ][ 1 ] ),
                displayPos[ _OPTIONS ][ 2 ], displayPos[ _OPTIONS ][ 3 ]
            );
        buttonBoxes[ _CREDITS ] = new Rectangle
            (
                displayPos[ _CREDITS ][ 0 ], ( Gfx._HUD_HEIGHT - displayPos[ _CREDITS ][ 1 ] ),
                displayPos[ _CREDITS ][ 2 ], displayPos[ _CREDITS ][ 3 ]
            );
        buttonBoxes[ _EXIT ]    = new Rectangle
            (
                displayPos[ _EXIT ][ 0 ], ( Gfx._HUD_HEIGHT - displayPos[ _EXIT ][ 1 ] ),
                displayPos[ _EXIT ][ 2 ], displayPos[ _EXIT ][ 3 ]
            );
    }

    private void createIndicator()
    {
        indicatorFrames = new TextureRegion[ _INDICATOR_FRAMES ];

        TextureRegion     region    = App.getAssets().getAnimationRegion( "menu_arrows_green" );
        TextureRegion[][] tmpFrames = region.split( 52, 48 );

        int i = 0;

        for ( final TextureRegion[] tmpFrame : tmpFrames )
        {
            for ( final TextureRegion textureRegion : tmpFrame )
            {
                if ( i < _INDICATOR_FRAMES )
                {
                    indicatorFrames[ i++ ] = textureRegion;
                }
            }
        }

        indicatorAnim = new Animation<>( 0.8f / 6f, indicatorFrames );
        indicatorAnim.setPlayMode( Animation.PlayMode.LOOP );
        elapsedAnimTime = 0;

        leftIndicator  = new Sprite();
        rightIndicator = new Sprite();

        leftIndicator.setSize( 52, 48 );
        rightIndicator.setSize( 52, 48 );

        indicatorDrawable = true;
        indicatorIndex    = 0;
    }

    @Override
    public void dispose()
    {
        if ( buttonStart != null )
        {
            buttonStart.addAction( Actions.removeActor() );
            buttonStart = null;
        }

        if ( buttonOptions != null )
        {
            buttonOptions.addAction( Actions.removeActor() );
            buttonOptions = null;
        }

        if ( buttonCredits != null )
        {
            buttonCredits.addAction( Actions.removeActor() );
            buttonCredits = null;
        }

        if ( buttonExit != null )
        {
            buttonExit.addAction( Actions.removeActor() );
            buttonExit = null;
        }

        if ( decoration != null )
        {
            decoration.addAction( Actions.removeActor() );
            decoration = null;
        }

        buttonBoxes     = null;
        leftIndicator   = null;
        rightIndicator  = null;
        indicatorFrames = null;
        indicatorAnim   = null;
    }
}
