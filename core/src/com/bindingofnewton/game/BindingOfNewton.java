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
import com.bindingofnewton.game.assets.AssetsHandler;
import com.bindingofnewton.game.character.Player;
import com.bindingofnewton.game.map.Level;
import com.bindingofnewton.game.map.LevelBuilder;
import com.bindingofnewton.game.map.MapBodyBuilder;
import com.bindingofnewton.game.map.Room;

import java.util.ArrayList;

public class BindingOfNewton extends Game{
	private SpriteBatch batch;

	private OrthographicCamera camera;
	private MapBodyBuilder mapBuilder;

	private InputHandler inputHandler;
	private Player player;
	private World world;
	private Box2DDebugRenderer renderer;

	private Level level;

	public static ArrayList<Bullet> bullets;
	private long lastShot = 0;

	@Override
	public void create () {
		setScreen(new MainMenuScreen());
		batch = new SpriteBatch();


		Orientation orientation = Orientation.DOWN;

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();


		world = new World(new Vector2(0,0), true);
		world.setContactListener(new ContactHandler());

		level = new LevelBuilder()
				.setWorld(world)
				.setLevelWidthHeight(4, 4)
				.setMinRooms(6)
				.setAmountRandomRooms(0, 0)
				.build();

		makeNewLevel(orientation);

		inputHandler = new InputHandler(player);
		Gdx.input.setInputProcessor(inputHandler);

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


		// Create Bullet array
		bullets = new ArrayList<>();

		camera.update();
	}

	@Override
	public void render() {
		world.step(Gdx.graphics.getDeltaTime(), 6, 2);


		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		camera.update();
		mapBuilder.setViewAndRender(camera);

		int x = 0;
		int y = 0;

		// Create Bullet on arrow click
		if(Gdx.input.isKeyPressed(Input.Keys.UP)){
			if(System.currentTimeMillis() - lastShot >= Bullet.fireRate){
				Bullet bullet = new Bullet(world,
						(int) (player.getSprite().getX() + player.getSprite().getWidth() / 2 - Bullet.WIDTH/2),
						(int) (player.getSprite().getY()+ 20 + (player.getSprite().getHeight()/2)));
				bullet.setMovement(new Vector2(0, bullet.getSpeed()));
				bullets.add(bullet);

				lastShot = System.currentTimeMillis();
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			if(System.currentTimeMillis() - lastShot >= Bullet.fireRate) {
				Bullet bullet = new Bullet(world,
						(int)(player.getSprite().getX() + player.getSprite().getWidth()/2 - Bullet.WIDTH/2),
						(int)player.getSprite().getY() - 20);
				bullet.setMovement(new Vector2(0, -bullet.getSpeed()));
				bullets.add(bullet);

				lastShot = System.currentTimeMillis();
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			if(System.currentTimeMillis() - lastShot >= Bullet.fireRate) {
				Bullet bullet = new Bullet(world,
						(int)player.getSprite().getX() - 20,
						(int) (player.getSprite().getY() + player.getSprite().getHeight()/2));
				bullet.setMovement(new Vector2(-bullet.getSpeed(), 0));
				bullets.add(bullet);

				lastShot = System.currentTimeMillis();
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			if(System.currentTimeMillis() - lastShot >= Bullet.fireRate) {
				Bullet bullet = new Bullet(world,
						(int)(player.getSprite().getX() + 20 + (player.getSprite().getWidth()/2)),
						(int)(player.getSprite().getY() + player.getSprite().getHeight()/2));
				bullet.setMovement(new Vector2(bullet.getSpeed(), 0));
				bullets.add(bullet);

				lastShot = System.currentTimeMillis();
			}
		}

		// Update all bullets
		for(int i = 0; i < bullets.size(); i++){
			if(bullets.get(i).isRemove()){
			    // Destroy body, remove bullet
				world.destroyBody(bullets.get(i).getBody());
				bullets.remove(i);
			}else{
				bullets.get(i).update();
			}

		}

		batch.begin();
		if (inputHandler.isMoving) {
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
			batch.draw(player.getTextureRegion(), player.getBody().getPosition().x, player.getBody().getPosition().y, player.getSprite().getWidth(), player.getSprite().getHeight());
		} else {
			batch.draw(player.getSprite(), player.getBody().getPosition().x, player.getBody().getPosition().y, player.getSprite().getWidth(), player.getSprite().getHeight());
		}

		player.move(new Vector2(x, y));

		// Render all bullets
		for(int i = 0; i < bullets.size(); i++){
			bullets.get(i).getSprite().draw(batch);
		}

		checkDoorCollision();

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
		player = new Player(world, 100, 100, AssetsHandler.getInstance().getPlayerSprite("newton"));
		Room room = level.getNextRoom(orientation);


		room.setBodies();
		TiledMap map = room.getMap();


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
