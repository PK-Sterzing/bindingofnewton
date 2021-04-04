package com.bindingofnewton.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sun.org.apache.xpath.internal.operations.Or;

public class BindingOfNewton extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture imgCharacterUp;
	private Texture imgCharacterDown;
	private Texture imgCharacterLeft;
	private Texture imgCharacterRight;
	private int x;
	private int y;
	private Orientation orientation;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		imgCharacterUp = new Texture("isaac-2.png");
		imgCharacterDown = new Texture("isaac-1.png");
		imgCharacterLeft = new Texture("isaac-3.png");
		imgCharacterRight = new Texture("isaac-4.png");
		x = 0;
		y = 0;
		orientation = Orientation.DOWN;
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		int range = 3;
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

		if(this.orientation == Orientation.DOWN){
			batch.draw(imgCharacterDown, x, y, 60, 70);
		}else if(this.orientation == Orientation.UP){
			batch.draw(imgCharacterUp, x, y, 60, 70);
		}else if(this.orientation == Orientation.LEFT){
			batch.draw(imgCharacterLeft, x, y, 60, 70);
		}else if(this.orientation == Orientation.RIGHT){
			batch.draw(imgCharacterRight, x, y, 60, 70);
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		imgCharacterUp.dispose();
		imgCharacterDown.dispose();
	}
}
