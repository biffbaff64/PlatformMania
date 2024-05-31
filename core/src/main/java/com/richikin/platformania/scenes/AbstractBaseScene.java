package com.richikin.platformania.scenes;

import com.badlogic.gdx.ScreenAdapter;
import com.richikin.platformania.core.App;

public abstract class AbstractBaseScene extends ScreenAdapter implements BaseSceneInterface
{
    public AbstractBaseScene()
    {
        super();
    }

    @Override
    public void update()
    {
    }

    @Override
    public void resize( int _width, int _height )
    {
        App.getBaseRenderer().resizeCameras( _width, _height );
    }

    @Override
    public void pause()
    {
        App.getSettings().getPrefs().flush();
    }

    @Override
    public void show()
    {
        loadImages();
    }

    @Override
    public void render( float delta )
    {
        App.getBaseRenderer().render();
    }

    @Override
    public void loadImages()
    {
    }
}
