package com.salt.grrow.viewmodel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.utils.Array;
import com.salt.grrow.CheckPointView;
import com.salt.grrow.CreatureView;
//penecito rico
import com.salt.grrow.model.*;

public class MainViewModel {
	
    public static MainViewModel Current;
    public Tank Tank;
	public static Texture creatureTexture;
	public static Texture foodTexture;



    public MainViewModel()
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
    		MainViewModel.Current.Tank.Creatures.add(cv);
    	}
    	for(CheckPoint c : checkpoints){
    		CheckPointView cv = new CheckPointView(c.getX(), 
					 c.getY(), 10, foodTexture);
    		MainViewModel.Current.Tank.CheckPoints.add(cv);
    	}
    }
    	
    public static void setCheckPoint(CheckPoint c){
    	CheckPointView cv = new CheckPointView(c.getX(), 
				 c.getY(), c.collisionRadius, foodTexture);
		MainViewModel.Current.Tank.CheckPoints.add(cv);
	}



}
