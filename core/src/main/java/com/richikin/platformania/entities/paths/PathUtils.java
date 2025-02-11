package com.richikin.platformania.entities.paths;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.platformania.logging.Trace;

public class PathUtils implements Disposable
{
    private static final String           _PATH_NUMBER = "Path Number";
    private              Array<FixedPath> paths;
    private              int              numberOfPaths;

    public PathUtils()
    {
        Trace.checkPoint();
    }

    public void setup()
    {
        Trace.dbg("To be Completed");
    }

    /**
     * Load the path data from the TiledMap.
     * Nodes are identified by a MarkerTile of nodeName.
     * Node properties will specify parent path and position in the path.
     *
     * @param mapObjects The TiledMap Object Layer
     */
    private void loadFromMap(final MapObjects mapObjects)
    {
        Trace.checkPoint();

        for (MapObject object : mapObjects)
        {
            if (object instanceof PolygonMapObject)
            {
                int pathNum = (int) object.getProperties().get(_PATH_NUMBER);

                Polygon polygon = ((PolygonMapObject) object).getPolygon();

                float[] points = polygon.getTransformedVertices();

                for (int i = 0; i < points.length; i += 2)
                {
                    paths.get(pathNum).data.add(new Vector2(points[i], points[i + 1]));
                }
            }
        }
    }

    /**
     * Establish how many seperate paths there are.
     *
     * @param mapObjects The TiledMap Object Layer
     * @return The number of paths found.
     */
    private int establishNumberOfPaths(final MapObjects mapObjects)
    {
        int pathCount = -1;

        for (MapObject object : mapObjects)
        {
            if (object instanceof PolygonMapObject)
            {
                if (object.getProperties().containsKey(_PATH_NUMBER))
                {
                    if ((int) object.getProperties().get(_PATH_NUMBER) > pathCount)
                    {
                        pathCount++;
                    }
                }
            }
        }

        return pathCount + 1;
    }

    public Vector2 getVector(int pathNum, int index)
    {
        return paths.get(pathNum).data.get(index);
    }

    public Array<FixedPath> getPaths()
    {
        return paths;
    }

    public int getNumberOfPaths()
    {
        return numberOfPaths;
    }

    /**
     * Pick a path at random from the paths array.
     *
     * @return The path number.
     */
    public int getRandomPath()
    {
        int randomPath;

        if (paths.size <= 1)
        {
            randomPath = 0;
        }
        else
        {
            randomPath = MathUtils.random(paths.size - 1);
        }

        return randomPath;
    }

    @Override
    public void dispose()
    {
        if ( paths != null )
        {
            paths.clear();
            paths = null;
        }
    }
}
