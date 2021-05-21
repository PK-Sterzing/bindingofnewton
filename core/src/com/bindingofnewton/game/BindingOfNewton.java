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
import com.bindingofnewton.game.items.Item;
import com.bindingofnewton.game.map.*;
import java.util.ArrayList;
import java.util.List;

public class BindingOfNewton implements Screen{
	private static BindingOfNewton instance;

	private SpriteBatch batch;

	private OrthographicCamera camera;
	private MapBodyBuilder mapBuilder;

	private InputHandler inputHandler;
	private World world;

	protected Game game;

	protected int levelNumber = 0;
	public Level level;
	private Minimap minimap;

	private long lastPathChange = 0;

	private Box2DDebugRenderer renderer;
	public boolean showDebugInfo = false;
	private ContactHandler contactHandler;

	private boolean isPaused = false;

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
			if (level.getCurrentRoom() == level.getRooms().get(level.getRooms().size()-1)){
				System.out.println("HIER EBENE WECHSELN");
				levelNumber++;
				makeNewLevel();
			}else
				makeNewRoom(orientation);
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
			bullet.render(batch);
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
	private void makeNewLevel(){
		Array<Body> bodies = new Array<>();
		world.getBodies(bodies);

		//Destroying all bodies of the map and of the player
		for (Body body : bodies){
			world.destroyBody(body);
		}

		// Set current level so to the right folder, so that the asset handler is loading the right rooms
		// Level number counts from 0
		AssetsHandler.MAP_CURRENT_LEVEL	= "level" + (levelNumber+1) + "/";
		System.out.println(AssetsHandler.MAP_CURRENT_LEVEL);

		// Make new level
		level = new LevelBuilder()
				.setWorld(world)
				.setLevelWidthHeight(8, 8)
				.setMinRooms(4)
				.setAmountRandomRooms(0, 0)
				.build();


		// TODO: Set player spawn in the middle
        AssetsHandler.PlayerName[] players = AssetsHandler.PlayerName.values();
		Player player = new Player(world, players[levelNumber], 100, 100);

		level.getCurrentRoom().setPlayer(player);

		//Creating a new minimap
		minimap = new Minimap(level);

		// Create new inputHandler to supply new level
		// otherwise the old level is going to get used
		inputHandler = new InputHandler(level);
		Gdx.input.setInputProcessor(inputHandler);


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
		if (level.getCurrentRoom() == level.getRooms().get(level.getRooms().size()-1)){
			makeBossEnemy();
		}else if (!level.getCurrentRoom().isCleared()){
			int minX = 40;
			int maxX = 400;
			int minY = 40;
			int maxY = 250;
			ArrayList<Enemy> enemies = new ArrayList<>();
			for(int i = 0; i < 5; i++){
				double startX = Math.floor(Math.random()*(maxX-minX+1)+minX);
				double startY = Math.floor(Math.random()*(maxY-minY+1)+minY);

				if (i%2 == 0){
					enemies.add(new Enemy(world, Enemy.Properties.MOUSE, (int) startX, (int) startY, 80));
				}else{
					Enemy bat = new Enemy(world, Enemy.Properties.BAT, (int)startX, (int)startY, 50);
					enemies.add(bat);
				}
			}
			level.getCurrentRoom().addEnemies(enemies);
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


}
