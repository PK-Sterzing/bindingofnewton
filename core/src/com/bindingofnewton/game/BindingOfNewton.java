package com.bindingofnewton.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class BindingOfNewton extends Game{
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
		world.setContactListener(new ContactHandler());

		level = new Level.Builder()
				.setWorld(world)
				.setLevelWidthHeight(4, 4)
				.setMinRooms(6)
				.setAmountRandomRooms(0, 0)
				.build();

		makeNewLevel(orientation);

		inputHandler = new InputHandler(player);

		// Create debug renderer to make collisions visible
		renderer = new Box2DDebugRenderer();


		// Create Camera
		camera = new OrthographicCamera();
		camera.zoom = 0.5f;
		camera.setToOrtho(false, w, h);

		// Get map height and width
		int mapWidth = mapBuilder.getMap().getProperties().get("width", Integer.class);
		int mapHeight = mapBuilder.getMap().getProperties().get("height", Integer.class);
		int tileWidth = mapBuilder.getMap().getProperties().get("tilewidth", Integer.class);
		int tileHeight = mapBuilder.getMap().getProperties().get("tileheight", Integer.class);

		int translateX = (Gdx.graphics.getWidth() - (mapWidth * tileWidth)) / 4;
		int translateY = (Gdx.graphics.getHeight() - (mapHeight * tileHeight)) / 4;

		// Move Camera to set map in the middle
		// * 0.5 because the map is zoomed
		camera.translate(-translateX * 0.5f, -translateY * 0.5f);

		camera.update();
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

		checkDoorCollision();

		batch.draw(player.getTextureRegion(), player.getBody().getPosition().x, player.getBody().getPosition().y);
		batch.end();
		renderer.render(world, camera.combined);
	}

	/**
	 * Sets up a new Level. Removes the old bodies of the map, loads and creates the new bodies of the map. A new player gets generated.
	 */
	private void makeNewLevel(Orientation orientation){
		Array<Body> bodies = new Array<>();
		world.getBodies(bodies);

		//Destroying all bodies of the map and of the player
		for (Body body : bodies){
			world.destroyBody(body);
		}

		//Generating a new player and bodies of the new map
		Room room = level.getNextRoom(orientation);

		room.setBodies();
		TiledMap map = room.getMap();

		int width = (int) map.getProperties().get("width")*32;
		int height = (int) map.getProperties().get("height")*32;

		int playerX=0, playerY=0;
		Sprite[] playerSprite = AssetsHandler.getInstance().getPlayerSprite("isaac-newton");

		switch (orientation.getOpposite()){
			case UP:
				playerY = (int) (height-playerSprite[0].getHeight());
				playerX = (int) (width/2 - playerSprite[0].getWidth()/2);
				break;
			case DOWN:
				playerY = (int) (playerSprite[0].getHeight()/2);
				playerX = (int) (width/2 - playerSprite[0].getWidth()/2);
				break;
			case LEFT:
				playerY = (int) (height/2 - playerSprite[0].getHeight()/2);
				playerX = (int) (32 + playerSprite[0].getWidth()/2);
				break;
			case RIGHT:
				playerY = (int) (height/2 - playerSprite[0].getHeight()/2);
				playerX = (int) (width-32-playerSprite[0].getWidth());
				break;
		}

		player = new Player(world, playerX, playerY, AssetsHandler.getInstance().getPlayerSprite("isaac-newton"));

		mapBuilder = new MapBodyBuilder(map);
		mapBuilder.buildBodies(world);
	}

	/**
	 * Checks if a collision with a door happened
	 */
	private void checkDoorCollision() {
		String layerName = "doors-";

		Rectangle playerRectangle = player.getPolygon().getBoundingRectangle();

		for (Orientation orientation : Orientation.values()){
			MapLayer layer = level.getCurrentRoom().getMap().getLayers().get(layerName + orientation.name());
			if (layer == null) return;

			MapObjects objects = layer.getObjects();

			for (MapObject object : objects){
				if (object instanceof RectangleMapObject){
					Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

					Vector2 position, size;
					if (orientation == Orientation.DOWN || orientation == Orientation.UP){
						position = orientation.moveCoord(new Vector2(0,0), 16);

						size = new Vector2(-16, 0);
					}else{
						position = new Vector2(0,0);

						size = new Vector2(0, -16);
					}

					Rectangle rectangle1 = new Rectangle(
							rectangle.x + position.x,
							rectangle.y + position.y,
							rectangle.width + size.x,
							rectangle.height + size.y);

					Vector2 start = orientation.moveCoord(
							new Vector2(rectangle1.x , rectangle1.y ),
							-playerRectangle.getHeight()
					);

					if (playerRectangle.overlaps(rectangle1)){
						//The Player enters a door
						makeNewLevel(orientation);
					}
				}
			}

		}


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
