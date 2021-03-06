package com.bindingofnewton.game;
import com.bindingofnewton.game.assets.AssetsHandler;
import com.bindingofnewton.game.bullets.Bullet;
import com.bindingofnewton.game.character.BossEnemy;
import com.bindingofnewton.game.character.Enemy;
import com.bindingofnewton.game.character.Player;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.bindingofnewton.game.map.*;
import com.bindingofnewton.game.items.Item;
import com.sun.glass.ui.Window;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The main class for the bindingOfNewton
 */
public class BindingOfNewton implements Screen{
	private static BindingOfNewton instance;

	private SpriteBatch batch;

	private OrthographicCamera camera;
	private MapBodyBuilder mapBuilder;

	private InputHandler inputHandler;
	private ContactHandler contactHandler;
	private World world;

	protected Game game;

	protected int levelNumber = 0;
	public Level level;
	private Minimap minimap;

	private long lastPathChange = 0;

	private Box2DDebugRenderer renderer;
	public boolean showDebugInfo = false;

	private boolean isPaused = false;

	protected boolean shouldMoveToNewLevel = false;

	private BindingOfNewton() { }

	public static BindingOfNewton getInstance(){
		if (instance == null){
			instance = new BindingOfNewton();
		}
		return instance;
	}

	@Override
	public void show() {
		batch = new SpriteBatch();

		world = new World(new Vector2(0,0), true);

		// Create Camera
		camera = new OrthographicCamera();
		levelNumber = 0;

		makeNewLevel();

		contactHandler = new ContactHandler(level);
		world.setContactListener(contactHandler);

		inputHandler = new InputHandler(level);
		Gdx.input.setInputProcessor(inputHandler);

		// Create debug renderer to make collisions visible
		renderer = new Box2DDebugRenderer();

		camera.update();
		isPaused = false;
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

		Orientation orientation = contactHandler.isDoorCollision();
		if (orientation != null ){
			makeNewRoom(orientation);
		}

		// Check if we have to move to next level
		if(shouldMoveToNewLevel == true){
			makeNewLevel();
			shouldMoveToNewLevel = false;
		}

		contactHandler.contactWithFire();

		batch.begin();

		// Render the player
		Vector2 movementPlayer = inputHandler.getPlayerMovement();
		if (movementPlayer.x != 0 || movementPlayer.y != 0){
			player.render(batch, true);
		}else{
			player.render(batch, false);
		}

		// Move player
		player.move(movementPlayer);

		// Render enemies
		for(Enemy enemy : level.getCurrentRoom().getEnemies()){
			enemy.render(batch, true);
		}

		// Render all bullets
		for(Bullet bullet : level.getCurrentRoom().getBullets()){
			if(bullet != null){
				bullet.render(batch);
			}
		}

		// Render all dropped items
		for(Item item : level.getCurrentRoom().getDroppedItems()){
			item.render(batch);
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
		// Show debug info when the switch is on (SHIFT)
		if(showDebugInfo){
			renderer.render(world, camera.combined);
		}
	}

	/**
	 * Creates new Level, removes all bodies, creates new Player, loads starting Room
	 */
	public void makeNewLevel(){
		Array<Body> bodies = new Array<>();
		world.getBodies(bodies);

		//Destroying all bodies of the map and of the player
		for (Body body : bodies){
		    if(body != null){
				world.destroyBody(body);
			}
		}

		// Set current level so to the right folder, so that the asset handler is loading the right rooms
		// Level number counts from 0
		AssetsHandler.MAP_CURRENT_LEVEL	= "level" + (levelNumber+1) + "/";

		// Make new level
		level = new LevelBuilder()
				.setWorld(world)
				.setLevelWidthHeight(10, 10)
				//.setMinRooms(8 + levelNumber*2)
				.setMinRooms(3)
				.setAmountRandomRooms(0, 1)
				.build();

        AssetsHandler.PlayerName[] players = AssetsHandler.PlayerName.values();
		Player player = new Player(world, players[levelNumber], 100, 100);

		level.getCurrentRoom().setPlayer(player);

		//Creating a new minimap
		minimap = new Minimap(level);

		// Create new inputHandler to supply new level
		// otherwise the old level is going to get used
		inputHandler = new InputHandler(level);
		Gdx.input.setInputProcessor(inputHandler);

		// Create new Contact handler so that the level is up to date
		contactHandler = new ContactHandler(level);
		world.setContactListener(contactHandler);

		makeNewRoom(Orientation.DOWN);
	}

	/**
	 * Creates new Room, stores the coordinates and the orientation when entering the door
	 * @param orientation
	 */
	private void makeNewRoom(Orientation orientation){

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
		if (orientation == null && room == level.getRooms().get(0)){
			orientation = Orientation.DOWN;
		}

		TiledMapTileLayer layer = (TiledMapTileLayer) room.getMap().getLayers().get("ground");
		float w = layer.getTileWidth() * layer.getWidth();
		float h = layer.getTileHeight() * layer.getHeight();
		camera.setToOrtho(false, w, h);

		level.getCurrentRoom().setPlayer(playerCached);

		// Create Enemies
		if (!level.getCurrentRoom().isCleared()){
			int enemyCount = (int) (6 + levelNumber);

			if (level.getCurrentRoom() == level.getRooms().get(level.getRooms().size()-1)){
				//TODO: Boss macht problem
				//makeBossEnemy();
				enemyCount += 5;
			}

			ArrayList<Enemy> enemies = new ArrayList<>();

			String roomName = level.getCurrentRoom().getMap().getProperties().get("file").toString();
			roomName = roomName.split("/")[roomName.split("/").length - 2] + "/" + roomName.split("/")[roomName.split("/").length - 1];
			System.out.println(roomName);

			HashMap<String, Vector2[]> spawnPoints = AssetsHandler.getInstance().spawnPoints;
			Vector2[] vectors =  spawnPoints.get(roomName);

			if (vectors == null){
				vectors = new Vector2[2];
				vectors[0] = new Vector2(5, 4);
				vectors[1] = new Vector2(9, 4);
			}

			if (!roomName.endsWith("mapStart.tmx")){
				for(int i = 0; i < enemyCount; i++){
					if (i<3){
						enemies.add(new Enemy(world, Enemy.Properties.MOUSE, (int) vectors[i%2].x*32, (int) vectors[i%2].y*32, 80));
					}else{

						Enemy bat = new Enemy(world, Enemy.Properties.BAT, (int)vectors[i%2].x*32, (int)vectors[i%2].y*32, 40);
						enemies.add(bat);

						if (levelNumber == 1) {
							Enemy fireBat = new Enemy(world, Enemy.Properties.FIREBAT, (int)vectors[i%2].x*32, (int)vectors[i%2].y*32, 60);
							enemies.add(fireBat);
						}
						if (levelNumber == 2){
							Enemy goblin = new Enemy(world, Enemy.Properties.GOBLIN, (int)vectors[i%2].x*32, (int)vectors[i%2].y*32, 50);
							enemies.add(goblin);
						}
					}
				}
				level.getCurrentRoom().addEnemies(enemies);
			}
		}

		room.setDoorBodies();
		TiledMap map = room.getMap();

		int playerX=0, playerY=0;
		ArrayList<Sprite> playerSprite = AssetsHandler.getInstance().getPlayerSprites(level.getCurrentRoom().getPlayer().getPlayerName());

		switch (orientation.getOpposite()){
			case UP:
				playerY = (int) (h-playerSprite.get(0).getHeight()-layer.getTileHeight());
				playerX = (int) (w/2 - playerSprite.get(0).getWidth()/2);
				break;
			case DOWN:
				playerY = (int) (playerSprite.get(0).getHeight()/2);
				playerX = (int) (w/2 - playerSprite.get(0).getWidth()/2);
				break;
			case LEFT:
				playerY = (int) (h/2 - playerSprite.get(0).getHeight()/2);
				playerX = (int) (layer.getTileWidth() + playerSprite.get(0).getWidth()/2);
				break;
			case RIGHT:
				playerY = (int) (h/2 - playerSprite.get(0).getHeight()/2);
				playerX = (int) (w-layer.getTileWidth()-playerSprite.get(0).getWidth());
				break;
		}
		level.getCurrentRoom().getPlayer().transform(new Vector2(playerX, playerY));

		mapBuilder = new MapBodyBuilder(map);
		mapBuilder.buildBodies(world);
	}

	/**
	 * Makes a boss enemy
	 */
	private void makeBossEnemy() {
		Room room = level.getCurrentRoom();

		TiledMapTileLayer layer = (TiledMapTileLayer) level.getCurrentRoom().getMap().getLayers().get("ground");
		int width = layer.getTileWidth() * layer.getWidth();
		int height = layer.getTileHeight() * layer.getHeight();

		int x=0, y=0;

		Sprite sprite = AssetsHandler.getInstance().getSingleSpriteFromAtlas("boss-run-1");

		Orientation orientation = room.getDoors().get(0).getOrientation();

		switch(orientation.getOpposite()){
			case UP:
				x = width/2;
				y = (int) (height-layer.getTileHeight()-sprite.getHeight());
				break;
			case DOWN:
				x = width/2;
				y = layer.getTileHeight()+10;
				break;
			case LEFT:
				x = layer.getTileWidth()+10;
				y = height/2;
				break;
			case RIGHT:
				x = (int) (width-layer.getTileWidth()-sprite.getWidth());
				y = height/2;
				break;
		}
		BossEnemy enemy = new BossEnemy(world, x, y, 40);

		ArrayList<Enemy> enemies = new ArrayList<>();
		enemies.add(enemy);
		room.addEnemies(enemies);
	}

	public void setGame(Game game){
		this.game = game;
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


	public boolean isPaused() {
		return isPaused;
	}

	public void setPaused(boolean paused) {
		isPaused = paused;
	}

	public Game getGame() {
		return game;
	}


	public int getLevelNumber() {
		return levelNumber;
	}

	public boolean isShouldMoveToNewLevel() {
		return shouldMoveToNewLevel;
	}

	public void setShouldMoveToNewLevel(boolean shouldMoveToNewLevel) {
		this.shouldMoveToNewLevel = shouldMoveToNewLevel;
	}

}
