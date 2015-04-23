package com.salt.grrow.model;



import java.util.Random;

import com.badlogic.gdx.utils.Array;

public class Tank {

	 private int xsize;
     private int ysize;
     public Array<CheckPoint> CheckPoints; 
     public Array<Creature> Creatures;
     private Random random;



     public Tank(int x, int y, int seed)
     {
         this.xsize = x;
         this.ysize = y;
         this.CheckPoints = new Array<CheckPoint>();
         this.Creatures = new Array<Creature>();
         this.setRandom(new Random(seed));
     }



	public int getYsize() {
		return ysize;
	}



	public void setYsize(int ysize) {
		this.ysize = ysize;
	}



	public int getXsize() {
		return xsize;
	}



	public void setXsize(int xsize) {
		this.xsize = xsize;
	}
	
	public  int nextIntWithNegatives(int n){
		if(this.getRandom().nextBoolean()){
			return this.getRandom().nextInt(n);
		}
		else return -this.getRandom().nextInt(n);
	}



	public Random getRandom() {
		return random;
	}



	public void setRandom(Random random) {
		this.random = random;
	}


}
