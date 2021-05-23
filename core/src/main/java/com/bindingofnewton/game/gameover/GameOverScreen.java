package com.bindingofnewton.game.gameover;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bindingofnewton.game.assets.AssetsHandler;
import com.bindingofnewton.game.mainmenu.MainMenuScreen;

public class GameOverScreen extends ScreenAdapter {

    private SpriteBatch batch;
    private Viewport viewport;
    private OrthographicCamera camera;
    protected Game game;
    private Sprite backgroundSprite;

    public GameOverScreen(Game game) {
        this.game = game;

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        viewport.apply();

        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();

        backgroundSprite = AssetsHandler.getInstance().getSingleSpriteFromFile("game-over.png");
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
            this.game.setScreen(new MainMenuScreen(this.game));
        }

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
    }

}
