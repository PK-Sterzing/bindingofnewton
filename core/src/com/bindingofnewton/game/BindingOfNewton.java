package com.bindingofnewton.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.bindingofnewton.game.assets.AssetsHandler;
import com.bindingofnewton.game.character.BossEnemy;
import com.bindingofnewton.game.character.Enemy;
import com.bindingofnewton.game.character.Player;
import com.bindingofnewton.game.map.*;
import java.util.ArrayList;
import java.util.List;

public class BindingOfNewton implements Screen{
	private static SpriteBatch batch;

	private OrthographicCamera camera;
	private MapBodyBuilder mapBuilder;

	private InputHandler inputHandler;
	private World world;

	protected static Game game;

	protected static Level level;
	private Minimap minimap;

	private long lastPathChange = 0;

	private Box2DDebugRenderer renderer;
	public static boolean showDebugInfo = false;
	private ContactHandler contactHandler;
	protected static boolean isPaused = false;

	public BindingOfNewton(Game game) {
		this.game = game;
	}

	@Override
	public void show() {
		batch = new SpriteBatch();

		world = new World(new Vector2(0,0), true);

		makeNewLevel();
		contactHandler = new ContactHandler(level);

		world.setContactListener(contactHandler);

		inputHandler = new InputHandler(level);
		Gdx.input.setInputProcessor(inputHandler);

		// Create debug renderer to make collisions visible
		renderer = new Box2DDebugRenderer();

		// Create Camera
		camera = new OrthographicCamera();

		TiledMapTileLayer layer = (TiledMapTileLayer) level.getCurrentRoom().getMap().getLayers().get("ground");
		float w = layer.getTileWidth() * layer.getWidth();
		float h = layer.getTileHeight() * layer.getHeight();
		camera.setToOrtho(false, w, h);

		camera.update();
	}

	@Override
	public void render(float delta) {
		if(isPaused){
			delta = 0;
		}
		world.step(delta, 6, 2);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		camera.update();
		mapBuilder.setViewAndRender(camera);

		Player player = level.getCurrentRoom().getPlayer();

		inputHandler.handleBullets();

		level.getCurrentRoom().update();

		if (contactHandler.isDoorCollision())
			makeNewRoom(level.getCurrentRoom().getPlayer().getOrientation());

		batch.begin();

		// Render the player
		Vector2 movementPlayer = inputHandler.getPlayerMovement();
		if (movementPlayer.x != 0 && movementPlayer.y != 0){
			player.render(batch, true);
		}else{
			player.render(batch, false);
		}

		// Move player
		player.move(movementPlayer);

		// Render enemies
		for(Enemy enemy : level.getCurrentRoom().getEnemies()){
			if (enemy instanceof BossEnemy){
				((BossEnemy)enemy).render(batch, true);
			}else{
				enemy.render(batch, true);
			}
		}

		// Render all bullets
		for(Bullet bullet : level.getCurrentRoom().getBullets()){
			bullet.render(batch);
		}


		// Show debug info when the switch is on (SHIFT)
		if(showDebugInfo){
			renderer.render(world, camera.combined);
		}

		//Renders the hearts of the player
		List<Sprite> sprites = player.getHealthSprites();
		for (int i=0; i< sprites.size(); i++){
			batch.draw(sprites.get(i),
					(int) level.getCurrentRoom().getMap().getProperties().get("width")*i+20,
					(int) level.getCurrentRoom().getMap().getProperties().get("height") * 32 - 20,
					15, 15);
		}

		minimap.render(batch);
		batch.end();
	}

	/**
	 * Creates new Level, removes all bodies, creates new Player, loads starting Room
	 */
	private void makeNewLevel(){
		System.out.println("Making new Level");
		Array<Body> bodies = new Array<>();
		world.getBodies(bodies);

		//Destroying all bodies of the map and of the player
		for (Body body : bodies){
			world.destroyBody(body);
		}

		// Make new level
		level = new LevelBuilder()
				.setWorld(world)
				.setLevelWidthHeight(8, 8)
				.setMinRooms(8)
				.setAmountRandomRooms(0, 0)
				.build();


		// TODO: Set player spawn in the middle
		Player player = new Player(world, AssetsHandler.PlayerName.NEWTON, 100, 100);
		level.getCurrentRoom().setPlayer(player);

		//Creating a new minimap
		minimap = new Minimap(level);

		makeNewRoom(Orientation.UP);
	}

	/**
	 * Creates new Room, stores the coordinates and the orientation when entering the door
	 * @param orientation
	 */
	private void makeNewRoom( Orientation orientation){
		Player playerCached = level.getCurrentRoom().getPlayer();
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
		level.getCurrentRoom().setPlayer(playerCached);

		if (level.getCurrentRoom() == level.getRooms().get(level.getRooms().size()-1)){
			makeBossEnemy();
		}

		// Create Enemies
		if (!level.getCurrentRoom().isCleared()){
			int minX = 40;
			int maxX = 400;
			int minY = 40;
			int maxY = 250;
			ArrayList<Enemy> enemies = new ArrayList<>();
			for(int i = 0; i < 5; i++){
				double startX = Math.floor(Math.random()*(maxX-minX+1)+minX);
				double startY = Math.floor(Math.random()*(maxY-minY+1)+minY);
				enemies.add(new Enemy(world, (int)startX, (int)startY, 50, AssetsHandler.getInstance().getSingleSprite(
						"./character/bat_run/run-front1.png")));
			}
			level.getCurrentRoom().addEnemies(enemies);
		}

		room.setDoorBodies();
		TiledMap map = room.getMap();

		int width = (int) map.getProperties().get("width")*32;
		int height = (int) map.getProperties().get("height")*32;

		int playerX=0, playerY=0;
		ArrayList<Sprite> playerSprite = AssetsHandler.getInstance().getPlayerSprites(level.getCurrentRoom().getPlayer().getPlayerName());

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
		level.getCurrentRoom().getPlayer().transform(new Vector2(playerX, playerY));

		mapBuilder = new MapBodyBuilder(map);
		mapBuilder.buildBodies(world);
	}

	private void makeBossEnemy() {
		Room room = level.getCurrentRoom();
		int width = (int) room.getMap().getProperties().get("width");
		int height = (int) room.getMap().getProperties().get("height");

		int x=0, y=0;

		Orientation orientation = room.getDoors().get(0).getOrientation();

		switch(orientation.getOpposite()){
			case UP:
				x = width/2;
				y = height-200;
				break;
			case DOWN:
				x = width/2;
				y = 200;
				break;
			case LEFT:
				x = 200;
				y = height/2;
				break;
			case RIGHT:
				x = width-200;
				y = height/2;
				break;
		}

		BossEnemy enemy = new BossEnemy(world, x, y, 30, AssetsHandler.getInstance().getSingleSprite("./character/boss/boss_run1.png"));
		ArrayList<Enemy> enemies = new ArrayList<>();
		enemies.add(enemy);
		room.addEnemies(enemies);
	}

	/**
	 * Draws dead player and pauses game
	 */
	public static void playerDeath() {
		Sprite deadSprite = AssetsHandler.getInstance().getSingleSprite("./character/newton/newton-dead.png");
		System.out.println("Dead Player drawn");
		// Set Player sprite to dead sprite
        level.getCurrentRoom().getPlayer().setSingleSprites(deadSprite);

        // Pause game
		isPaused = true;
		// Remove Body and player
		level.getWorld().destroyBody(level.getCurrentRoom().getPlayer().getBody());
		level.getCurrentRoom().setPlayer(null);

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
