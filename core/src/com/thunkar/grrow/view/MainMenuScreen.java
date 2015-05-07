package com.thunkar.grrow.view;


import java.io.FileNotFoundException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.thunkar.grrow.controller.MainController;
import com.thunkar.grrow.model.CheckPoint;
import com.thunkar.grrow.model.Creature;
import com.thunkar.grrow.model.DNA;
import com.thunkar.grrow.model.OfflineTank;

	public class MainMenuScreen implements Screen {

		final GrrowMain game;

		OrthographicCamera camera;
		Label label;
		TextButton GameScreenButton;
		TextButton GameScreenButton2;
		TextButton OfflineTrainingButton;
		TextButton Exit;

		public MainMenuScreen(final GrrowMain gam) {
			game = gam;
			camera = new OrthographicCamera();
			Gdx.input.setInputProcessor(game.stage);
			camera.setToOrtho(false, game.width, game.height);
			game.stage.setViewport(new FitViewport(game.width, game.height, camera));
			label = new Label("Welcome to Grrow", game.uiskin);
			game.stage.addActor(label);
			Table table=new Table();
			GameScreenButton = new TextButton("Creepy things running all over your screen (Gen0)", game.uiskin);
			table.add(GameScreenButton).width(400).padTop(10).padBottom(3);
			table.row();
			GameScreenButton2 = new TextButton("Show me my last minions!", game.uiskin);
			table.add(GameScreenButton2).width(400).padTop(10).padBottom(3);
			table.row();
			OfflineTrainingButton = new TextButton("Simulate those creepy things", game.uiskin);
			table.add(OfflineTrainingButton).width(400).padTop(10).padBottom(3);
			table.row();
			Exit = new TextButton("Take me out of here!", game.uiskin);
			table.add(Exit).width(400).padTop(10).padBottom(3);
			table.row();
			table.add(label).padTop(10).padBottom(3);
			table.row();
			table.setFillParent(true);
			OfflineTrainingButton.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					try {
						game.stage.clear();
						game.setScreen(new OfflineTrainingScreen(game));
					
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			GameScreenButton.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					game.stage.clear();
			        Array<Creature> generation0 = new Array<Creature>();
			        Array<CheckPoint> random = new Array<CheckPoint>();
			        for(int i = 0; i<15; i++){
			        	CheckPoint checkpoint1 = new CheckPoint(DNA.nextIntWithNegatives(650), 
								 DNA.nextIntWithNegatives(350), 10);
			        	random.add(checkpoint1);
			        }
			        //for(int i = 0; i<10; i++){
			        	DNA dna1 = new DNA();
				        Creature Creature1 = new Creature("Carol", 0, -300, 90, 10, dna1);
				        generation0.add(Creature1);
			       // }
			        MainController.setTank(generation0, random);
					game.setScreen(new GameScreen(game));
				}
			});
			GameScreenButton2.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					game.stage.clear();
			        Array<CheckPoint> random = new Array<CheckPoint>();
			        for(int i = 0; i<15; i++){
			        	CheckPoint checkpoint1 = new CheckPoint(DNA.nextIntWithNegatives(650), 
								 DNA.nextIntWithNegatives(350), 10);
			        	random.add(checkpoint1);
			        }
			        try {
						MainController.setTank(OfflineTank.creaturesFromFiles(), random);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					game.setScreen(new GameScreen(game));
				}
			});
			Exit.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					Gdx.app.exit();
				}
			});
			game.stage.addActor(table);
		}

		@Override
		public void render(float delta) {
			Gdx.gl.glClearColor(0, 0, 0.2f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			game.stage.getViewport().update(game.width, game.height, true);

			game.stage.act();
			game.stage.draw();
	
		}

		@Override
		public void resize(int width, int height) {
			game.stage.getViewport().update(game.width, game.height, true);
		}

		@Override
		public void show() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void hide() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void pause() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void resume() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void dispose() {	
		}

	}


