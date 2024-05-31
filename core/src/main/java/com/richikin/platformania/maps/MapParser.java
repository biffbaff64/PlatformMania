package com.richikin.platformania.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.platformania.entities.objects.SpriteDescriptor;

public interface MapParser extends Disposable
{
    void initialiseLevelMap();

    void createPositioningData();

    void parseObjectBasedMarkerTiles();

    void parseTiledCollision();

    void parsePolygonCollision();

    void parseAABBObjects();

    SpriteDescriptor createPlacementTile( MapObject mapObject, SpriteDescriptor descriptor );

    ObjectTileProperties setObjectTileProperties( SpriteDescriptor descriptor);

    void setGameLevelMap();

    void render( OrthographicCamera camera );

    void drawOverlayLayer( OrthographicCamera camera );

//    LayerImage[][] getBackgroundLayers();
//
//    int[] getBackgroundIndexes();

    TiledMap getCurrentMap();
}
