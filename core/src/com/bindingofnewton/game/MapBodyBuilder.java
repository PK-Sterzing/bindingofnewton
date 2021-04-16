package com.bindingofnewton.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class MapBodyBuilder {
    private TiledMap map;
    private TiledMapRenderer renderer;

    public MapBodyBuilder(){
        map = new TmxMapLoader().load("map2.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
    }

    public void setViewAndRender(OrthographicCamera camera){
        renderer.setView(camera);
        renderer.render();
    }

    public void buildBodies(World world){

    }

    public TiledMap getMap() {
        return map;
    }

    public TiledMapRenderer getRenderer() {
        return renderer;
    }

}
