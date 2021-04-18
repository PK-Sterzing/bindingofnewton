package com.bindingofnewton.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;



public class MapBodyBuilder {
    private TiledMap map;
    private TiledMapRenderer renderer;
    private static final float TILE_SIZE = 32;



    public MapBodyBuilder(){
        map = new TmxMapLoader().load("map2.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
    }

    public void setViewAndRender(OrthographicCamera camera){
        renderer.setView(camera);
        renderer.render();
    }

    /**
     * Creates box2d definition from tiled map
     * Runs trough all objects in map and creates bodies and fixtures from objects and adds them to the world
     * @param world
     */
    public void buildBodies(World world){
        String layer = "border";
        MapObjects objects = map.getLayers().get(layer).getObjects();

        for(MapObject object: objects) {
            // Make sure the found object is a rectangle
            if(object instanceof RectangleMapObject){
                // Get Rectangle from found object on map
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

                // Create box2d body
                BodyDef def = new BodyDef();
                def.type = BodyDef.BodyType.StaticBody;
                Body body = world.createBody(def);

                Fixture fixture = body.createFixture(getShapeFromRectangle(rectangle), 1f);
                fixture.setFriction(0.5f);

                body.setTransform(getTransformedCenterForRectangle(rectangle), 0);

            }
        }
    }

    /**
     * Returns PolygonShape with shape from Rectangle
     * PolygonShape is required to create fixture, but we only get a rectangle from the map
     * @param rectangle
     * @return shape
     */
    private static Shape getShapeFromRectangle(Rectangle rectangle){
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(rectangle.width / 2, rectangle.height / 2);
        System.out.println("Rectangle size: (" + rectangle.width + ", " + rectangle.height + ")");
        return polygonShape;
    }


    /**
     * Gets center from rectangle
     * Returns coordinates from the found rectangle on the map
     * @param rectangle
     * @return Vector2 coordinates to the origin point of the object
     */
    private static Vector2 getTransformedCenterForRectangle(Rectangle rectangle){
        Vector2 center = new Vector2();
        rectangle.getCenter(center);
        // The center is offset by one tile in every direction
        // Not sure if this is right...
        System.out.println("Rectangle pos: " + center);
        //return center.add(new Vector2(-TILE_SIZE, -TILE_SIZE));
        return center.add(new Vector2(-TILE_SIZE, -TILE_SIZE));
    }

    public TiledMap getMap() {
        return map;
    }


    public TiledMapRenderer getRenderer() {
        return renderer;
    }

}
