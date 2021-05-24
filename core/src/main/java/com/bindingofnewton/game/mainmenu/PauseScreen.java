package com.bindingofnewton.game.mainmenu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bindingofnewton.game.BindingOfNewton;
import com.bindingofnewton.game.assets.AssetsHandler;
import com.bindingofnewton.game.assets.SoundHandler;

public class PauseScreen implements Screen {
    private SpriteBatch batch;
    protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private TextureAtlas atlas;
    protected Skin skin;
    protected Game game;
    private Sprite backgroundSprite;

    public PauseScreen(Game game) {
        this.game = game;

        atlas = new TextureAtlas("uiskin.atlas");
        skin = new Skin(Gdx.files.internal("uiskin.json"), atlas);

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        stage = new Stage(viewport, batch);

        backgroundSprite = AssetsHandler.getInstance().getSingleSpriteFromFile("Pause-screen.png");
    }


    @Override
    public void show() {
        //Stage should control input:
        Gdx.input.setInputProcessor(stage);


        //Create buttons
        TextButton continueButton = new TextButton("Continue", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        continueButton.setTransform(true);
        continueButton.setScale(16f, 9.5f);
        continueButton.setPosition(200, 350);

        exitButton.setTransform(true);
        exitButton.setScale(15f, 8f);
        exitButton.setPosition(260, 100);

        //Add listeners to buttons
        continueButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SoundHandler.getInstance().stopMusic(SoundHandler.Music.MAIN_MENU);
                game.setScreen(BindingOfNewton.getInstance());
                BindingOfNewton.getInstance().setGame(game);
                SoundHandler.getInstance().playMusic(SoundHandler.Music.MAIN_MENU, true, 0.2f);
            }
        });
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // Add actors (buttons) to stage
        stage.addActor(continueButton);
        stage.addActor(exitButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw stage with buttons
        stage.act();
        stage.draw();
        // Draw background sprite
        batch.begin();
        backgroundSprite.draw(batch);
        batch.end();


    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
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
    public void dispose() {
        skin.dispose();
        atlas.dispose();
    }

}
