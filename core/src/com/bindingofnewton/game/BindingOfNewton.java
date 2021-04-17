package com.bindingofnewton.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class BindingOfNewton extends Game {
	private SpriteBatch batch;

	private int x;
	private int y;
	private Orientation orientation;
	private OrthographicCamera camera;
	private MapBodyBuilder mapBuilder;

	private InputHandler inputHandler;
	private Player player;
	private World world;

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
		System.out.println(Atlas.getInstance().getPlayerSprite("isaac-newton"));
		player = new Player(world, 100, 100, Atlas.getInstance().getPlayerSprite("isaac-newton"));
		inputHandler = new InputHandler(player);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);
		camera.update();


		mapBuilder = new MapBodyBuilder();
		mapBuilder.buildBodies(world);
		//Gdx.input.setInputProcessor(inputHandler);
	}

	@Override
	public void render () {

		world.step(Gdx.graphics.getDeltaTime(), 6, 2);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		mapBuilder.setViewAndRender(camera);

		int x = 0;
		int y = 0;
		if(Gdx.input.isKeyPressed(Input.Keys.W)){
			 y += 100;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)){
			y -= 100;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.D)){
			x += 100;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.A)){
			x -= 100;
		}

		player.move(new Vector2(x, y));

		batch.begin();


		batch.draw(player.getSprite(), player.getSprite().getX(), player.getSprite().getY());
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

}
