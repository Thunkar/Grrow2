package com.salt.grrow;



import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.salt.grrow.viewmodel.MainViewModel;

public class GrrowMain extends Game {
	
	Stage stage;
	ShapeRenderer srenderer;
	Skin uiskin;
	FileHandle file;
	int width;
	int height;

	public void create() 
	{
		width = 1366;
		height = 768;
		stage = new Stage();
		srenderer = new ShapeRenderer();
		file = Gdx.files.internal("uiskin.json");
		uiskin = new Skin(file);
		this.setScreen(new MainMenuScreen(this));
		@SuppressWarnings("unused")
		MainViewModel MainViewModel = new MainViewModel();
	}

	public void render() {
		super.render(); //important!
	}
	
	public void dispose() {
		stage.dispose();
		srenderer.dispose();
		uiskin.dispose();
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
}
