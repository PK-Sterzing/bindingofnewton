package com.bindingofnewton.game.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;


public class MapBodyBuilder {
    private TiledMap map;
    private TiledMapRenderer renderer;

    public MapBodyBuilder(TiledMap map) {
        this.map = map;
        renderer = new OrthogonalTiledMapRenderer(map);
    }

    public void setViewAndRender(OrthographicCamera camera) {
        renderer.setView(camera);
        renderer.render();
    }

    /**
     * Creates box2d definition from tiled map
     * Runs trough all objects in map and creates bodies and fixtures from objects and adds them to the world
     *
     * @param world
     */
    public void buildBodies(World world) {
        String layer = "border";
        MapObjects objects = map.getLayers().get(layer).getObjects();

        for (MapObject object : objects) {
            // Make sure the found object is a rectangle
            Shape shape = null;
            if (object instanceof RectangleMapObject) {
                shape = getRectangle((RectangleMapObject)object);
            } else if (object instanceof PolygonMapObject) {
                shape = getPolygon((PolygonMapObject)object);
            } else {
                continue;
            }

            if(shape != null){
                // Create box2d body
                BodyDef def = new BodyDef();
                def.type = BodyDef.BodyType.StaticBody;
                Body body = world.createBody(def);
                body.createFixture(shape, 1);
            }

                //body.setTransform(getTransformedCenterForRectangle(rectangle), 0);
                shape.dispose();

        }
    }

    /**
     * Returns PolygonShape with shape from Rectangle
     * PolygonShape is required to create fixture, but we only get a rectangle from the map
     *
     * @param rectangleMapObject
     * @return shape
     */
    private static PolygonShape getRectangle(RectangleMapObject rectangleMapObject) {
        Rectangle rectangle = rectangleMapObject.getRectangle();
        PolygonShape polygonShape = new PolygonShape();
        Vector2 position = new Vector2((rectangle.x + rectangle.width * 0.5f ),
                (rectangle.y + rectangle.height * 0.5f ));

        polygonShape.setAsBox(rectangle.width * 0.5f ,
                rectangle.height * 0.5f ,
                position,
                0.0f);

        return polygonShape;
    }
    private static PolygonShape getPolygon(PolygonMapObject polygonObject) {
        PolygonShape polygon = new PolygonShape();
        float[] vertices = polygonObject.getPolygon().getTransformedVertices();

        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; i++) {
            worldVertices[i] = vertices[i];
        }

        polygon.set(worldVertices);
        return polygon;
    }

    public TiledMap getMap() {
        return map;
    }


}

