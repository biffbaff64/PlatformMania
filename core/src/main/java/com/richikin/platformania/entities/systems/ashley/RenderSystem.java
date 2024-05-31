package com.richikin.platformania.entities.systems.ashley;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.richikin.platformania.core.App;
import com.richikin.platformania.entities.components.IdentityComponent;

import java.util.Comparator;

public class RenderSystem extends SortedIteratingSystem
{
    private final Array<Entity>                      renderQueue;
    private final ComponentMapper<IdentityComponent> spriteMapper;
    private       Comparator<Entity>                 comparator;

    public RenderSystem(SpriteBatch spriteBatch)
    {
        // Fetch all entities with SpriteComponent
        super(Family.all(IdentityComponent.class).get(), new ZComparator());

        // Create component mappers
        spriteMapper = ComponentMapper.getFor(IdentityComponent.class);

        // renderQueue is the array for sorting entities for display.
        renderQueue = new Array<>();
    }

    @Override
    public void update(float deltaTime)
    {
        super.update(deltaTime);

        // Sort the renderQueue based on Z index
        renderQueue.sort(comparator);

        for (Entity entity : renderQueue)
        {
            IdentityComponent identityComponent = spriteMapper.get(entity);

            if ((identityComponent.sprite != null)
                && identityComponent.isDrawable)
            {
                identityComponent.sprite.draw(App.getSpriteBatch());
            }
        }

        renderQueue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime)
    {
        renderQueue.add(entity);
    }
}
