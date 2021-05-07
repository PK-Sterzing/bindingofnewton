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
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.bindingofnewton.game.assets.AssetsHandler;
import com.bindingofnewton.game.character.Enemy;
import com.bindingofnewton.game.character.Player;
import com.bindingofnewton.game.map.Level;
import com.bindingofnewton.game.map.LevelBuilder;
import com.bindingofnewton.game.map.MapBodyBuilder;
import com.bindingofnewton.game.map.Room;

import java.util.ArrayList;
import java.util.Random;


public class BindingOfNewton implements Screen{
	private SpriteBatch batch;

	private OrthographicCamera camera;
	private MapBodyBuilder mapBuilder;

	private InputHandler inputHandler;
	private Player player;
	private World world;

	protected static Game game;


	protected static Level level;

	private long lastShot = 0;

	private Box2DDebugRenderer renderer;
	public static boolean showDebugInfo = false;

	public BindingOfNewton(Game game) {
		this.game = game;
	}


	@Override
	public void show() {
		batch = new SpriteBatch();


		world = new World(new Vector2(0,0), true);
		world.setContactListener(new ContactHandler());

		makeNewLevel();

		inputHandler = new InputHandler(player);
		Gdx.input.setInputProcessor(inputHandler);

		// Create debug renderer to make collisions visible
		renderer = new Box2DDebugRenderer();

		// Create Camera
		camera = new OrthographicCamera();
		//camera.zoom = 0.5f;
		TiledMapTileLayer layer = (TiledMapTileLayer) level.getCurrentRoom().getMap().getLayers().get("ground");
		float w = layer.getTileWidth() * layer.getWidth();
		float h = layer.getTileHeight() * layer.getHeight();
		camera.setToOrtho(false, w, h);


		camera.update();
	}

	@Override
	public void render(float delta) {
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
				level.getCurrentRoom().addBullet(bullet);

				lastShot = System.currentTimeMillis();
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			if(System.currentTimeMillis() - lastShot >= Bullet.fireRate) {
				System.out.println("Bullet down");
				Bullet bullet = new Bullet(world,
						(int)(player.getSprite().getX() + player.getSprite().getWidth()/2 - Bullet.WIDTH/2),
						(int)player.getSprite().getY() - 20);
				bullet.setMovement(new Vector2(0, -bullet.getSpeed()));
				level.getCurrentRoom().addBullet(bullet);

				lastShot = System.currentTimeMillis();
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			if(System.currentTimeMillis() - lastShot >= Bullet.fireRate) {
				Bullet bullet = new Bullet(world,
						(int)player.getSprite().getX() - 20,
						(int) (player.getSprite().getY() + player.getSprite().getHeight()/2));
				bullet.setMovement(new Vector2(-bullet.getSpeed(), 0));
				level.getCurrentRoom().addBullet(bullet);

				lastShot = System.currentTimeMillis();
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			if(System.currentTimeMillis() - lastShot >= Bullet.fireRate) {
				Bullet bullet = new Bullet(world,
						(int)(player.getSprite().getX() + 20 + (player.getSprite().getWidth()/2)),
						(int)(player.getSprite().getY() + player.getSprite().getHeight()/2));
				bullet.setMovement(new Vector2(bullet.getSpeed(), 0));
				level.getCurrentRoom().addBullet(bullet);

				lastShot = System.currentTimeMillis();
			}
		}

		// Update all bullets in current room
		for(int i = 0; i < level.getCurrentRoom().getBullets().size(); i++){
			if(level.getCurrentRoom().getBullets().get(i).isRemove()){
			    // Destroy body, remove bullet
				System.out.println("Removed bullet");
				world.destroyBody(level.getCurrentRoom().getBullets().get(i).getBody());
				level.getCurrentRoom().getBullets().remove(i);
			}else{
				System.out.println("Updated bullet");
				level.getCurrentRoom().getBullets().get(i).update();
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

		// Move player
		player.move(new Vector2(x, y));

		// Move enemies
		for(int i = 0; i < level.getCurrentRoom().getEnemies().size(); i++){
			// Get random number
			Random rand = new Random();
			int randx = rand.nextInt(40) - 20;
			int randy = rand.nextInt(40) - 20;
			level.getCurrentRoom().getEnemies().get(i).move(new Vector2(randx, randy));
		}

		// Render enemies
		for(int i = 0; i < level.getCurrentRoom().getEnemies().size(); i++){
			level.getCurrentRoom().getEnemies().get(i).getSprite().draw(batch);
		}

		// Render all bullets
		for(int i = 0; i < level.getCurrentRoom().getBullets().size(); i++){
			level.getCurrentRoom().getBullets().get(i).getSprite().draw(batch);
		}

		checkDoorCollision();

		//renders the players health
		Sprite[] sprites = player.getHealthSprites();
		for (int i=0; i< sprites.length; i++){
			batch.draw(sprites[i], 15*i+20, level.getCurrentRoom().getMap().getProperties().get("height", Integer.class) * 32 - 20, 15, 15);
		}

		batch.end();

		// Show debug info when the switch is on (SHIFT)
		if(showDebugInfo){
			renderer.render(world, camera.combined);
		}

	}

	/**
	 * Creates new Level, removes all bodies, creates new Player, loads starting Room
	 */
	private void makeNewLevel(){
		Array<Body> bodies = new Array<>();
		world.getBodies(bodies);

		//Destroying all bodies of the map and of the player
		for (Body body : bodies){
			if (!(body.getUserData() instanceof Player))
				world.destroyBody(body);
		}

		// Make new level
		level = new LevelBuilder()
				.setWorld(world)
				.setLevelWidthHeight(4, 4)
				.setMinRooms(6)
				.setAmountRandomRooms(0, 0)
				.build();


		// TODO: Set player spawn in the middle
		player = new Player(world, 100, 100, AssetsHandler.getInstance().getPlayerSprite(AssetsHandler.NEWTON, "newton"));

		makeNewRoom(Orientation.UP);
	}

	/**
	 * Creates new Room, stores the coordinates and the orientation when entering the door
	 * @param orientation
	 */
	private void makeNewRoom( Orientation orientation){
		// Remove all bodies in current Room
		Array<Body> bodies = new Array<>();
		world.getBodies(bodies);

		//Destroying all bodies of the map and of the player
		for (Body body : bodies){
		    if(!(body.getUserData() instanceof Player)){
		    	world.destroyBody(body);
			}
		}

		// Load next Room
		Room room = level.getNextRoom(orientation);

		// Create Enemies
		ArrayList<Enemy> enemies = new ArrayList<>();
		for(int i = 0; i < 5; i++){
			enemies.add(new Enemy(world, 100, 100, AssetsHandler.getInstance().getSingleSprite(
					"./character/bat_run/run-front1.png")));
		}
		room.addEnemies(enemies);

		room.setDoorBodies();
		TiledMap map = room.getMap();

		int width = (int) map.getProperties().get("width")*32;
		int height = (int) map.getProperties().get("height")*32;

		int playerX=0, playerY=0;
		ArrayList<Sprite> playerSprite = AssetsHandler.getInstance().getPlayerSprite(AssetsHandler.NEWTON, "newton");

		switch (orientation.getOpposite()){
			case UP:
				playerY = (int) (height-playerSprite.get(0).getHeight()-32);
				playerX = (int) (width/2 - playerSprite.get(0).getWidth()/2);
				break;
			case DOWN:
				playerY = (int) (playerSprite.get(0).getHeight()/2);
				playerX = (int) (width/2 - playerSprite.get(0).getWidth()/2);
				break;
			case LEFT:
				playerY = (int) (height/2 - playerSprite.get(0).getHeight()/2);
				playerX = (int) (32 + playerSprite.get(0).getWidth()/2);
				break;
			case RIGHT:
				playerY = (int) (height/2 - playerSprite.get(0).getHeight()/2);
				playerX = (int) (width-32-playerSprite.get(0).getWidth());
				break;
		}
		// TODO: Don't create new player, move player to startx starty
		player = new Player(world, playerX, playerY, AssetsHandler.getInstance().getPlayerSprite(AssetsHandler.NEWTON,"newton"));

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
						makeNewRoom(player.getOrientation());
					}
				}
			}

		}


	}


	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose () {
		batch.dispose();
	}



}
