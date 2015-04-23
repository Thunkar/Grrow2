package com.salt.grrow.model;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.salt.grrow.model.brain._1LayerBrain;

public class Creature implements Comparable<Creature>{
	
	private float x;
    private float y;
    public _1LayerBrain Brain;
    private float orientation;
    private String id;
    public int health;
    public final int collisionRadius;
    public Array<Vector2> RayEnds;
    private boolean canEat;
    public CheckPoint closerCheckPoint;
    public int itWithOutEating;
    public int itEating;


    public boolean canEat() {
		return canEat;
	}

	public void setCanEat(boolean canEat) {
		this.canEat = canEat;
	}

	public Creature(String id, float x, float y, float orientation,int collisionRadius ,DNA dna)
    {
        this.id = id;
        this.health = 100;
        this.orientation = orientation;
        this.x = x;
        this.y = y;
        this.Brain = new _1LayerBrain(dna);
        this.collisionRadius = collisionRadius;
        this.RayEnds = new Array<Vector2>();
        this.itWithOutEating = 0;
        this.itEating = 0;
    }
	
	public boolean SameSide(Vector2 p1, Vector2 p2, Vector2 a, Vector2 b){
		Vector2 inA = new Vector2(a);
		Vector2 inB = new Vector2(b);
		Vector2 inP1 = new Vector2(p1);
		Vector2 inP2 = new Vector2(p2);
		Vector2 BA = inB.sub(inA);
		Vector2 P1A = inP1.sub(inA);
		float CP1 = BA.crs(P1A);
		Vector2 P2A = inP2.sub(inA);
		float CP2 = BA.crs(P2A);
		if((CP1*CP2)>= 0) return true;
		else return false;
	}
	public boolean PointInTriangle(Vector2 p, Vector2 a, Vector2 b, Vector2 c){
		if(SameSide(p,a,b,c)&&SameSide(p,b,a,c)&&SameSide(p,c,a,b)) return true;
		else return false;
	}

    
	public void Sense(Tank tank)
	{
		this.RayEnds.clear();
		this.canEat = false;
		CheckPoint closer = null;
		this.Brain.central.setValue(0);
		this.Brain.left2.setValue(0);
		this.Brain.right1.setValue(0);
		this.Brain.left1.setValue(0);
		this.Brain.right2.setValue(0);
		this.Brain.lrdist.setValue(0);
		Vector2 vis1 = this.getCreaturePosition().nor().add(500,25).rotate(this.orientation);
		Vector2 vis2 = this.getCreaturePosition().nor().add(500,-25).rotate(this.orientation);
		Vector2 vis3 = this.getCreaturePosition().nor().add(200, -50).rotate(this.orientation);
		Vector2 vis4 = this.getCreaturePosition().nor().add(150,-170).rotate(this.orientation);
		Vector2 vis5 = this.getCreaturePosition().nor().add(200,50).rotate(this.orientation);
		Vector2 vis6 = this.getCreaturePosition().nor().add(150,170).rotate(this.orientation);
		Vector2 A = this.getCreaturePosition().add(vis1);
		Vector2 B = this.getCreaturePosition().add(vis2);
		Vector2 D = this.getCreaturePosition().add(vis3);
		Vector2 E = this.getCreaturePosition().add(vis4);
		Vector2 F = this.getCreaturePosition().add(vis5);
		Vector2 G = this.getCreaturePosition().add(vis6);
//		this.RayEnds.add(A);
//		this.RayEnds.add(B);
//		this.RayEnds.add(D);
//		this.RayEnds.add(E);
//		this.RayEnds.add(F);
//		this.RayEnds.add(G);
		float minimunDistance = 0;
		for(CheckPoint c : tank.CheckPoints){
			float distance = this.getCreaturePosition().dst(c.getCheckPointPosition());
			if(distance < minimunDistance) minimunDistance = distance;
			this.Brain.lrdist.setValue(minimunDistance);
			if(PointInTriangle(c.getCheckPointPosition(), A, B, this.getCreaturePosition())){
				//this.RayEnds.add(new Vector2(c.getX(), c.getY()));
				float newValue = this.Brain.central.getValue()+1; 
				this.Brain.central.setValue(newValue);
				if(distance<32){
					this.canEat = true;
					closer = c;
				}
			}
			if(PointInTriangle(c.getCheckPointPosition(), B, D, this.getCreaturePosition())){
				//this.RayEnds.add(new Vector2(c.getX(), c.getY()));
				float newValue = this.Brain.right1.getValue()+1; 
				this.Brain.right1.setValue(newValue);
				if(distance<32){
					this.canEat = true;
					closer = c;
				}
			}

			if(PointInTriangle(c.getCheckPointPosition(), D, E, this.getCreaturePosition())){
				//this.RayEnds.add(new Vector2(c.getX(), c.getY()));
    			float newValue = this.Brain.right2.getValue()+1; 
				this.Brain.left2.setValue(newValue);
				if(distance<32){
					this.canEat = true;
					closer = c;
				}
    		}
			if(PointInTriangle(c.getCheckPointPosition(), A, F, this.getCreaturePosition())){
				//this.RayEnds.add(new Vector2(c.getX(), c.getY()));
    			float newValue = this.Brain.left1.getValue()+1; 
				this.Brain.left2.setValue(newValue);
				if(distance<32){
					this.canEat = true;
					closer = c;
				}
    		}
			
			if(PointInTriangle(c.getCheckPointPosition(), F, G, this.getCreaturePosition())){
				//this.RayEnds.add(new Vector2(c.getX(), c.getY()));
    			float newValue = this.Brain.left2.getValue()+1; 
				this.Brain.left2.setValue(newValue);
				if(distance<32){
					this.canEat = true;
					closer = c;
				}
    		}
    		
    	}   
    	this.closerCheckPoint = closer;
    }
    
    public boolean  Eat(double outputFromBrain, CheckPoint closer, Tank tank){
    	if(canEat && (outputFromBrain >= 0.4)){
    	tank.CheckPoints.removeValue(closer, true);
    	return true;
    	}
    	else return false;
    }
    
    public File StoreDNA(int generation) throws IOException{
   	 Json json = new Json();
   	 FileWriter file = new FileWriter(System.getProperty("user.dir")+"\\DNA\\" + this.id + "-" + generation + ".json");
		 file.write(json.prettyPrint(this.Brain.dna));
		 file.flush();
		 file.close();
	 File finished = new File(System.getProperty("user.dir")+"\\DNA\\" + this.id + "-" + generation + ".json");
	 return finished;
    }
    
   
	public float getX() {
		return x;
	}


	public void setX(float x) {
		this.x = x;
	}


	public float getY() {
		return y;
	}


	public void setY(float y) {
		this.y = y;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public float getOrientation() {
		while(this.orientation >= 360){
			this.orientation = this.orientation - 360;
		}
		while(this.orientation <= 0){
			this.orientation = this.orientation+360;
		}
		return  this.orientation;
	}


	public void setOrientation(float orientation) {
		float neworientation = orientation;
		while(neworientation >= 360){
			neworientation = neworientation - 360;
		}
		while(neworientation <= 0){
			neworientation = neworientation+360;
		}
		this.orientation = neworientation;
	}


	public int getHealth() {
		return health;
	}


	public void setHealth(int health) {
		this.health = health;
	}


	@Override
	public int compareTo(Creature o) {
		if(this.health < o.getHealth()){
			return 1;
		}
		else if(this.health == o.getHealth())
			return 0;
		else return -1;
	}

	public Vector2 getCreaturePosition() {
		return new Vector2(this.x, this.y);
	}



}
