package com.bindingofnewton.game;

import com.badlogic.gdx.*;
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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class BindingOfNewton extends Game {
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
	private InputHandler inputHandler;
	private Player player;
	private World world;
	
	@Override
	public void create () {
		setScreen(new MainMenuScreen());
		batch = new SpriteBatch();
		/*
		imgCharacterUp = new Texture("isaac-back.png");
		imgCharacterDown = new Texture("isaac-front.png");
		imgCharacterLeft = new Texture("isaac-left.png");
		imgCharacterRight = new Texture("isaac-right.png");
		 */
		x = 0;
		y = 0;

		orientation = Orientation.DOWN;

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		world = new World(new Vector2(0,0), true);
		player = new Player(world, 0, 0, Atlas.getInstance().getPlayerSprite("isaac-newton"));
		inputHandler = new InputHandler(player);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);
		camera.update();
		mapBuilder = new MapBodyBuilder();
		Gdx.input.setInputProcessor(inputHandler);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		mapBuilder.setViewAndRender(camera);

		batch.begin();
		/*
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
		}*/

		batch.draw(player.getSprite(), player.getX(), player.getY());

		/*
		if(this.orientation == Orientation.DOWN){
			batch.draw(imgCharacterDown, x, y);
		}else if(this.orientation == Orientation.UP){
			batch.draw(imgCharacterUp, x, y);
		}else if(this.orientation == Orientation.LEFT){
			batch.draw(imgCharacterLeft, x, y);
		}else if(this.orientation == Orientation.RIGHT){
			batch.draw(imgCharacterRight, x, y, (float) (imgCharacterRight.getBoundingRectangle().width * 4), (float) (imgCharacterRight.getBoundingRectangle().height * 4));
		}*/
		//batch.draw(player.getImage);

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		/*
		imgCharacterUp.dispose();
		imgCharacterDown.dispose();

		textureAtlas.dispose();

		 */
	}

}
