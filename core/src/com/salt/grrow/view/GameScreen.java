package com.salt.grrow.view;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.salt.grrow.controller.MainController;
import com.salt.grrow.model.CheckPoint;
import com.salt.grrow.model.Creature;



public class GameScreen implements Screen {
	
	final GrrowMain game;
	OrthographicCamera camera;
	Creature selected;
	TextButton exit;
	Table table;


	public GameScreen(final GrrowMain game) {

		this.game = game; 
		camera = new OrthographicCamera();
		camera.setToOrtho(false, game.width, game.height);
		Gdx.input.setInputProcessor(game.stage);
		game.stage.setViewport(new FitViewport(game.width, game.height, camera));
		Table table=new Table();
		exit = new TextButton("Stahp!", game.uiskin);
		table.add(exit).width(300).padTop(10).padBottom(3);
		table.row();
		table.bottom();
		table.center();
		table.setPosition(700, 30);
		exit.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.stage.clear();
				MainController.Current.Tank.Creatures.clear();
				MainController.Current.Tank.CheckPoints.clear();
				game.setScreen(new MainMenuScreen(game));
			}
		});
		game.stage.addActor(table);
		createCheckPointActors();
		createCreatureActors();
	}

	public void createCreatureActors(){
		for(Creature c : MainController.Current.Tank.Creatures)
		{
			game.stage.addActor(((CreatureView)c).actor);
		}
	}
	

	public void createCheckPointActors(){
		for(CheckPoint c : MainController.Current.Tank.CheckPoints)
		{
			game.stage.addActor(((CheckPointView)c).actor);
		}
	}
	
	public void updateCreatureActors() {
		for(Creature c : MainController.Current.Tank.Creatures)
		{
		((CreatureView)c).actor.setRotation(c.getOrientation());
		((CreatureView)c).actor.setX(camera.viewportWidth/2  + c.getX() - ((CreatureView)c).actor.getWidth()/2);
		((CreatureView)c).actor.setY(camera.viewportHeight/2  + c.getY() - ((CreatureView)c).actor.getHeight()/2);
		}
	}

	public void updateCheckpointActors(){
		for(Actor a : game.stage.getActors()){
			if(a instanceof Image && a.getName().equals("Food"))
				game.stage.getRoot().removeActor(a);
		}
		createCheckPointActors();
		for(CheckPoint c : MainController.Current.Tank.CheckPoints)
		{
			((CheckPointView)c).actor.setX(camera.viewportWidth/2 + c.getX() - ((CheckPointView)c).actor.getWidth()/2);
			((CheckPointView)c).actor.setY(camera.viewportHeight/2 + c.getY() - ((CheckPointView)c).actor.getHeight()/2);
		}
	}
	
	public void updateCreatureBrains(){
		for(Creature c : MainController.Current.Tank.Creatures){
			float tempX = c.getX();
			float tempY = c.getY();
			float tempRot = c.getOrientation();
			boolean hasEaten = false;
			c.Sense(MainController.Current.Tank);
			c.Brain.Propagate();
			hasEaten = c.Eat(c.Brain.ImpulseEat(), c.closerCheckPoint, MainController.Current.Tank);
			if(hasEaten) {
				MainController.setCheckPoint(new CheckPoint(MainController.Current.Tank.nextIntWithNegatives(650), 
						MainController.Current.Tank.nextIntWithNegatives(350), 10));
				game.stage.addActor(((CheckPointView)MainController.Current.Tank.CheckPoints.peek()).actor);
			}
			tempRot += (c.Brain.ImpulseRotateSign()*c.Brain.ImpulseRotateAmounth());
			tempX += c.Brain.ImpulsePush()*Math.cos(Math.toRadians(c.getOrientation()));
			tempY += c.Brain.ImpulsePush()*Math.sin(Math.toRadians(c.getOrientation()));
			if(tempX > 650){
				tempX = 650;
			}
			if(tempX < -650){
				tempX = -650;
			}
			if(tempY > 350){
				tempY = 350;
			}
			if(tempY < -350){
				tempY = -350;
			}
			c.setX(tempX);
			c.setY(tempY);
			c.setOrientation(tempRot);
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.stage.getViewport().update(game.width, game.height,true);
		updateCreatureBrains();
		updateCreatureActors();
		updateCheckpointActors();
		//game.stage.setCamera(camera);
		game.srenderer.setProjectionMatrix(camera.combined);
		game.stage.act();
		game.stage.draw();
		game.srenderer.begin(ShapeType.Line);
		for(Creature c : MainController.Current.Tank.Creatures){
			for(Vector2 v : c.RayEnds){
				game.srenderer.line(camera.viewportWidth/2 + c.getX(), 
						camera.viewportHeight/2 +c.getY(), 
						camera.viewportWidth/2 +v.x, camera.viewportHeight/2 +v.y);
		}
		}
		//game.srenderer.line(camera.viewportWidth/2, camera.viewportHeight, camera.viewportWidth/2, -camera.viewportHeight/2);
		//game.srenderer.line(0, camera.viewportHeight/2, camera.viewportWidth, camera.viewportHeight/2);
		game.srenderer.end();
		
	}

	@Override
	public void resize(int width, int height) {
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
