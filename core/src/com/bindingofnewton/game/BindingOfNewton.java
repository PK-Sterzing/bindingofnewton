package com.bindingofnewton.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

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

	private Texture white;
	private Texture red;

	@Override
	public void create () {
		setScreen(new MainMenuScreen());
		batch = new SpriteBatch();

		x = 100;
		y = 100;

		orientation = Orientation.DOWN;

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		world = new World(new Vector2(0,0), true);
		world.setContactListener(new CollisionListener());

		player = new Player(world, x, y, Atlas.getInstance().getPlayerSprite("isaac-newton"));
		inputHandler = new InputHandler(player);

		camera = new OrthographicCamera();
		camera.zoom = 0.35f;
		camera.setToOrtho(false, w, h);
		camera.update();



		mapBuilder = new MapBodyBuilder();
		mapBuilder.buildBodies(world);

		white = new Texture("white.jpg");
		red = new Texture("red.jpg");

	}


	@Override
	public void render () {

		world.step(Gdx.graphics.getDeltaTime(), 24, 24);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		mapBuilder.setViewAndRender(camera);
		camera.update();

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

		/*
		for(int i = 0; i < mapBuilder.rectangles.size(); i+=4){
			batch.draw(texture, mapBuilder.rectangles.get(i), mapBuilder.rectangles.get(i+1), mapBuilder.rectangles.get(i+2), mapBuilder.rectangles.get(i+3));
		}




		Array<Body> bodies = new Array<>();
		world.getBodies(bodies);
		int counter = 0;
		for(int i = 0; i < bodies.size; i++){
			int x1 = (int) bodies.get(i).getPosition().x;
			int y1 = (int) bodies.get(i).getPosition().y;
			int width = (int) ((PolygonShape) bodies.get(0).getUserData()).getHeight();
			int height = (int) ((PolygonShape) bodies.get(0).getFixtureList().get(0).getShape()

            int width = 32;
            int height = 50;
			if(counter+3 <= mapBuilder.rectangles.size()){
				width = mapBuilder.rectangles.get(counter+2);
				height = mapBuilder.rectangles.get(counter+3);

			}

			batch.draw(texture, x1, y1, width, height);
			counter += 4;

		}
		*/

		batch.draw(player.getSprite(), player.getSprite().getX(), player.getSprite().getY());

		System.out.println("World:");
		Array<Body> bodies = new Array<>();
		world.getBodies(bodies);
		for(int i = 0; i < bodies.size; i++){
			System.out.println("X, Y: (" + bodies.get(i).getPosition().x + ", " + bodies.get(i).getPosition().y + ")");
			batch.draw(white, bodies.get(i).getPosition().x, bodies.get(i).getPosition().y, 5, 5);
		}
		System.out.println("--------------------");


		batch.end();



	}

	@Override
	public void dispose () {
		batch.dispose();
	}

}
