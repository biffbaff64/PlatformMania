package com.richikin.platformania.entities.managers;

import com.richikin.platformania.maths.Vec3;
import com.richikin.platformania.core.App;
import com.richikin.platformania.entities.actors.hero.MainPlayer;
import com.richikin.platformania.entities.objects.SpriteDescriptor;
import com.richikin.platformania.enums.GraphicID;

public class PlayerManager extends BasicEntityManager
{
    public int playerTileX;
    public int playerTileY;

    private SpriteDescriptor descriptor;

    public PlayerManager()
    {
        super( GraphicID._PLAYER_MANAGER );
    }

    @Override
    public void init()
    {
        super.init();

        setSpawnPoint();
        createPlayer();
    }

    public void createPlayer()
    {
        App.getEntityManager().playerIndex = 0;
        App.getEntityManager().playerReady = false;

        App.getEntities().mainPlayer = new MainPlayer();
        App.getEntities().mainPlayer.initialise( descriptor );
        App.getEntityData().addEntity( App.getEntities().mainPlayer );

        App.getEntityManager().updateEntityMapIndexes();
        App.getEntityManager().playerIndex = descriptor._INDEX;
        App.getEntityManager().playerReady = true;
    }

    public void setSpawnPoint()
    {
        EntityManagerUtils managerUtils = new EntityManagerUtils();

        Vec3 coords = managerUtils.findCoordinate( GraphicID.G_PLAYER );

        playerTileX = coords.x;
        playerTileY = coords.y;

        descriptor             = App.getEntities().getDescriptor( GraphicID.G_PLAYER );
        descriptor._POSITION.x = playerTileX;
        descriptor._POSITION.y = playerTileY;
        descriptor._INDEX      = App.getEntityData().getEntityMap().size;
    }
}
