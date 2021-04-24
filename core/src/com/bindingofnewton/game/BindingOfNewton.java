package com.bindingofnewton.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.World;

public class BindingOfNewton extends Game{

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
		world.setContactListener(new ContactHandler());

		player = new Player(world, 150, 150, AssetsHandler.getInstance().getPlayerSprite("isaac-newton"));
		inputHandler = new InputHandler(player);



		Level.Builder levelBuilder = new Level.Builder();
		levelBuilder
			.setWorld(world)
			.setWorldWidthHeight(4, 4)
			.setMinRooms(6)
			.setAmountRandomRooms(0, 0);
		level = levelBuilder.build();

		mapBuilder = new MapBodyBuilder(level.getRooms().get(0).getMap());
		mapBuilder.buildBodies(world);

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

		batch.end();
		renderer.render(world, camera.combined);
	}

	private void checkDoorCollision() {
		String layerName = "doors";
		TiledMap map = level.getRooms().get(0).getMap();
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(layerName);
		for (MapObject mapObject : layer.getObjects()){
			RectangleMapObject rectangleMapObject = (RectangleMapObject) mapObject;
			Rectangle rectangle = rectangleMapObject.getRectangle();

			Polygon polygon = player.getPolygon();

			System.out.println("Player: " + polygon.getX() + ",  " + polygon.getY());

			Polygon rPoly = new Polygon(new float[] { 0, 0, rectangle.width, 0, rectangle.width,
					rectangle.height, 0, rectangle.height });

			System.out.println("Player: " + polygon.getX() + ",  " + polygon.getY());
			System.out.println("rPoly: " + rPoly.getX() + ",  " + rPoly.getY());

			rPoly.setPosition(rectangle.x, rectangle.y);
			if (Intersector.overlapConvexPolygons(rPoly, polygon))
				System.out.println("COLLISION");
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
