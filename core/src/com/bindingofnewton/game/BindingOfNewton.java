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
import com.badlogic.gdx.physics.box2d.*;

public class BindingOfNewton extends Game {
	private SpriteBatch batch;
	private Texture imgCharacterUp;
	private Texture imgCharacterDown;
	private Texture imgCharacterLeft;
	private Texture imgCharacterRight;

	private int x;
	private int y;
	private Orientation orientation;
	private OrthographicCamera camera;
	private MapBodyBuilder mapBuilder;

	private Sprite character;
	private World world;
	private Body body;

	
	@Override
	public void create () {
		setScreen(new MainMenuScreen());
		batch = new SpriteBatch();
		imgCharacterUp = new Texture("isaac-2.png");
		imgCharacterDown = new Texture("isaac-1.png");
		imgCharacterLeft = new Texture("isaac-3.png");
		imgCharacterRight = new Texture("isaac-4.png");

		x = 200;
		y = 200;
		orientation = Orientation.DOWN;


		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);
		camera.update();

		character = new Sprite(imgCharacterDown);
		character.setPosition(x, y);
		world = new World(new Vector2(0, 0), true);
		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.DynamicBody;
		def.position.set(character.getX(), character.getY());

		body = world.createBody(def);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(character.getWidth()/2, character.getHeight()/2);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1f;

		Fixture fixture = body.createFixture(fixtureDef);

		shape.dispose();

		mapBuilder = new MapBodyBuilder();
		mapBuilder.buildBodies(world);
		System.out.println("Amount of bodies: " + world.getBodyCount());
	}

	@Override
	public void render () {

		world.step(Gdx.graphics.getDeltaTime(), 6, 2);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		mapBuilder.setViewAndRender(camera);

		batch.begin();
		int range = 100;
		int x = 0;
		int y = 0;
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
		/*
		if(this.orientation == Orientation.DOWN){
			batch.draw(imgCharacterDown, x, y);
		}else if(this.orientation == Orientation.UP){
			batch.draw(imgCharacterUp, x, y);
		}else if(this.orientation == Orientation.LEFT){
			batch.draw(imgCharacterLeft, x, y);
		}else if(this.orientation == Orientation.RIGHT){
			batch.draw(imgCharacterRight, x, y);
		}

		 */
        body.setLinearVelocity(new Vector2(x, y));
		character.setPosition(body.getPosition().x, body.getPosition().y);
		//System.out.println(body.getPosition());
		batch.draw(character, character.getX(), character.getY());
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		imgCharacterUp.dispose();
		imgCharacterDown.dispose();
		imgCharacterRight.dispose();
		imgCharacterLeft.dispose();
	}

}
