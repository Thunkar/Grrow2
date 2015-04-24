package com.thunkar.grrow.controller;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.utils.Array;
//penecito rico
import com.thunkar.grrow.model.*;
import com.thunkar.grrow.view.CheckPointView;
import com.thunkar.grrow.view.CreatureView;

public class MainController {
	
    public static MainController Current;
    public Tank Tank;
	public static Texture creatureTexture;
	public static Texture foodTexture;



    public MainController()
    {
        Current = this;
        Tank = new Tank(1366, 768, 7);
		creatureTexture = new Texture(Gdx.files.internal("Creature.png"));
		foodTexture = new Texture(Gdx.files.internal("Food.png"));
		creatureTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		foodTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
    }
    
    public static void setTank(Array<Creature> creatures, Array<CheckPoint> checkpoints) {
    	for(Creature c : creatures){
    		CreatureView cv = new CreatureView(c.getId(), c.getX(), c.getY(), 
	        		c.getOrientation(), c.collisionRadius, c.Brain.dna, creatureTexture);
    		MainController.Current.Tank.Creatures.add(cv);
    	}
    	for(CheckPoint c : checkpoints){
    		CheckPointView cv = new CheckPointView(c.getX(), 
					 c.getY(), 10, foodTexture);
    		MainController.Current.Tank.CheckPoints.add(cv);
    	}
    }
    	
    public static void setCheckPoint(CheckPoint c){
    	CheckPointView cv = new CheckPointView(c.getX(), 
				 c.getY(), c.collisionRadius, foodTexture);
		MainController.Current.Tank.CheckPoints.add(cv);
	}



}
