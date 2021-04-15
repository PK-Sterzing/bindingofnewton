package com.bindingofnewton.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class BindingOfNewton extends ApplicationAdapter {
	private SpriteBatch batch;
	/*
	private Texture imgCharacterUp;
	private Texture imgCharacterDown;
	private Texture imgCharacterLeft;
	private Texture imgCharacterRight;

	 */
	private Sprite imgCharacterUp;
	private Sprite imgCharacterDown;
	private Sprite imgCharacterLeft;
	private Sprite imgCharacterRight;
	private int x;
	private int y;
	private Orientation orientation;
	private OrthographicCamera camera;
	TextureAtlas textureAtlas;
	private TiledMapRenderer tiledMapRenderer;
	private MapBodyBuilder mapBuilder;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		/*
		imgCharacterUp = new Texture("isaac-back.png");
		imgCharacterDown = new Texture("isaac-front.png");
		imgCharacterLeft = new Texture("isaac-left.png");
		imgCharacterRight = new Texture("isaac-right.png");
		 */

		textureAtlas = new TextureAtlas("data.txt");

		imgCharacterUp = textureAtlas.createSprite("isaac3");
		imgCharacterDown = textureAtlas.createSprite("isaac2");
		imgCharacterRight = textureAtlas.createSprite("isaac5");
		imgCharacterLeft = textureAtlas.createSprite("isaac4");

		x = 0;
		y = 0;
		orientation = Orientation.DOWN;


		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);
		camera.update();
		mapBuilder = new MapBodyBuilder();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		mapBuilder.setViewAndRender(camera);

		batch.begin();
		int range = 3;
		if(Gdx.input.isKeyPressed(Input.Keys.W)){
			y = y + range;
			this.orientation = Orientation.UP;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)){
			y = y - range;
			this.orientation = Orientation.DOWN;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.A)){
			x = x - range;
			this.orientation = Orientation.LEFT;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)){
			x = x + range;
			this.orientation = Orientation.RIGHT;
		}

		if(this.orientation == Orientation.DOWN){
			batch.draw(imgCharacterDown, x, y, 60, 70);
		}else if(this.orientation == Orientation.UP){
			batch.draw(imgCharacterUp, x, y, 60, 70);
		}else if(this.orientation == Orientation.LEFT){
			batch.draw(imgCharacterLeft, x, y, 60, 70);
		}else if(this.orientation == Orientation.RIGHT){
			batch.draw(imgCharacterRight, x, y, 60, 70);
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		/*
		imgCharacterUp.dispose();
		imgCharacterDown.dispose();

		 */
		textureAtlas.dispose();
	}

}
