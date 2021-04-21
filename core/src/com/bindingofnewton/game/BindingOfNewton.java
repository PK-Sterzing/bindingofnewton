package com.bindingofnewton.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class BindingOfNewton extends Game {

	private final String MAP_FILE_NAME = "mapStart.tmx";

	private SpriteBatch batch;

	private int x;
	private int y;
	private Orientation orientation;
	private OrthographicCamera camera;
	private MapBodyBuilder mapBuilder;

	private InputHandler inputHandler;
	private Player player;
	private World world;
	private Box2DDebugRenderer renderer;

	private Level level;
	private int levelCount = 0;

	public static final float SCALE = 10;

	@Override
	public void create () {
		setScreen(new MainMenuScreen());
		batch = new SpriteBatch();

		x = 0;
		y = 0;

		orientation = Orientation.DOWN;

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		world = new World(new Vector2(0,0), true);
		player = new Player(world, 150, 150, AssetsHandler.getInstance().getPlayerSprite("isaac-newton"));
		inputHandler = new InputHandler(player);

		camera = new OrthographicCamera();
		camera.zoom = 0.5f;
		camera.setToOrtho(false, w, h);
		camera.update();

		/*
		Level.Builder levelBuilder = new Level.Builder();
		levelBuilder
			.setWorldWidthHeight(4, 4)
			.setMinRooms(6)
			.setAmountRandomRooms(0, 0);
		Level level = levelBuilder.build();*/

		mapBuilder = new MapBodyBuilder(MAP_FILE_NAME);
		mapBuilder.buildBodies(world);



		//Gdx.input.setInputProcessor(inputHandler);
		renderer = new Box2DDebugRenderer();
	}

	@Override
	public void render () {

		world.step(Gdx.graphics.getDeltaTime(), 6, 2);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		camera.update();
		mapBuilder.setViewAndRender(camera);

		int x = 0;
		int y = 0;
		if(Gdx.input.isKeyPressed(Input.Keys.W)){
			 y += player.getSpeed();
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)){
			y -= player.getSpeed();
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)){
			x += player.getSpeed();
		}
		if(Gdx.input.isKeyPressed(Input.Keys.A)){
			x -= player.getSpeed();
		}

		player.move(new Vector2(x, y));

		batch.begin();


		player.getSprite().draw(batch);
		batch.end();
		renderer.render(world, camera.combined);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	/*
	public float scale(float valueToBeScaled) {
		return valueToBeScaled/SCALE;
	}

	 */


}
