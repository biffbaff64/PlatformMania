package com.richikin.platformania.graphics;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.richikin.platformania.core.App;

public class TextureSplitter
{
    public TextureSplitter()
    {
    }

    public void split( String textureName, String outputPath, int tileWidth, int tileHeight )
    {
        Texture texture = App.getAssets().loadSingleAsset( textureName, Texture.class );

        assert (( texture.getWidth() % tileWidth ) == 0 );
        assert (( texture.getHeight() % tileHeight ) == 0 );

        int widthInTiles  = texture.getWidth() / tileWidth;
        int heightInTiles = texture.getHeight() / tileHeight;

        FileHandle fileHandle;
        int tileNumber = 0;

        for ( int i = 0; i < heightInTiles; i++ )
        {
            for ( int j = 0; j < widthInTiles; j++ )
            {
                TextureRegion region = new TextureRegion
                    (
                        texture,
                        j * tileWidth, i * tileHeight,
                        tileWidth, tileHeight
                    );

                fileHandle = new FileHandle( outputPath + "\\tile" + tileNumber++ + ".png" );

                PixmapIO.writePNG( fileHandle, createPixmapFromRegion( region ) );
            }
        }
    }

    private Pixmap createPixmapFromRegion( TextureRegion region )
    {
        TextureData textureData = region.getTexture().getTextureData();

        if ( !textureData.isPrepared() )
        {
            textureData.prepare();
        }

        Pixmap pixmap = new Pixmap
            (
                region.getRegionWidth(),
                region.getRegionHeight(),
                textureData.getFormat()
            );

        pixmap.drawPixmap
                  (
                      textureData.consumePixmap(),
                      0, 0,
                      region.getRegionX(), region.getRegionY(),
                      region.getRegionWidth(), region.getRegionHeight()
                  );

        return pixmap;
    }
}
