package com.bindingofnewton.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;


public class MapBodyBuilder {
    private TiledMap map;
    private TiledMapRenderer renderer;
    private static final float TILE_SIZE = 16;

    public MapBodyBuilder(){
        map = new TmxMapLoader().load("map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        System.out.println("bodybuilder created");
    }

    public void setViewAndRender(OrthographicCamera camera){
        renderer.setView(camera);
        renderer.render();
    }

    public void buildBodies(World world){
        String layer = "border";
        MapObjects objects = map.getLayers().get(layer).getObjects();
        System.out.println(map.getLayers().get(layer).getObjects().getCount());
        System.out.println(objects);
        for(MapObject object: objects){
            System.out.println("border found");
            System.out.println(object);
            if(object instanceof RectangleMapObject){
                System.out.println("Rectangle border found");
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                BodyDef def = new BodyDef();
                def.type = BodyDef.BodyType.StaticBody;
                Body body = world.createBody(def);

                Fixture fixture = body.createFixture(getShapeFromRectangle(rectangle), 1f);
                fixture.setFriction(0.1f);

                String positionX = (String) object.getName();
                String positionY = (String) object.getName();
                System.out.println("name: " + positionX);
                System.out.println("name: " + positionY);
                System.out.println("X exists: " + object.getProperties().containsKey("X"));

                //body.setTransform(new Vector2(200, 200), 0);
                body.setTransform(getTransformedCenterForRectangle(rectangle), 0);

            }
        }
    }

    private static Shape getShapeFromRectangle(Rectangle rectangle){
        PolygonShape polygonShape = new PolygonShape();
        //polygonShape.setAsBox(rectangle.width * 0.5f / TILE_SIZE, rectangle.height * 0.5f / TILE_SIZE);
        polygonShape.setAsBox(rectangle.width / 2, rectangle.height / 2);
        return polygonShape;
    }

    private static Vector2 getTransformedCenterForRectangle(Rectangle rectangle){
        Vector2 center = new Vector2();
        rectangle.getCenter(center);
        System.out.println("Rectangle :" + rectangle.getCenter(center));
        return center.scl(TILE_SIZE);
    }

    public TiledMap getMap() {
        return map;
    }

    public TiledMapRenderer getRenderer() {
        return renderer;
    }

}
