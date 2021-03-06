package com.thunkar.grrow.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.thunkar.grrow.model.brain.Brain;

public class Creature implements Comparable<Creature>
{

	private float x;
	private float y;
	public Brain Brain;
	private float orientation;
	private String id;
	public int health;
	public final int collisionRadius;
	public Array<Vector2> RayEnds;
	private boolean canEat;
	public CheckPoint closerCheckPoint;
	
	private final int EATING_THRESHOLD = 28;
	private final int INITIAL_HEALTH = 100;

	public boolean canEat()
	{
		return canEat;
	}

	public void setCanEat(boolean canEat)
	{
		this.canEat = canEat;
	}

	public Creature(String id, float x, float y, float orientation, int collisionRadius, DNA dna)
	{
		this.id = id;
		this.health = INITIAL_HEALTH;
		this.orientation = orientation;
		this.x = x;
		this.y = y;
		this.Brain = new Brain(dna);
		this.collisionRadius = collisionRadius;
		this.RayEnds = new Array<Vector2>();
	}
	
	public void resetHealth()
	{
		this.health = INITIAL_HEALTH;
	}

	public boolean SameSide(Vector2 p1, Vector2 p2, Vector2 a, Vector2 b)
	{
		Vector2 inA = new Vector2(a);
		Vector2 inB = new Vector2(b);
		Vector2 inP1 = new Vector2(p1);
		Vector2 inP2 = new Vector2(p2);
		Vector2 BA = inB.sub(inA);
		Vector2 P1A = inP1.sub(inA);
		float CP1 = BA.crs(P1A);
		Vector2 P2A = inP2.sub(inA);
		float CP2 = BA.crs(P2A);
		return CP1 * CP2 >= 0;
	}

	public boolean PointInTriangle(Vector2 p, Vector2 a, Vector2 b, Vector2 c)
	{
		return SameSide(p, a, b, c) && SameSide(p, b, a, c) && SameSide(p, c, a, b);
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
		this.Brain.wallsX.setValue(0);
		this.Brain.wallsY.setValue(0);
		this.Brain.partner.setValue(1);
		
		Vector2 vis1 = this.getCreaturePosition().nor().add(-50, 300).rotate(this.orientation);
		Vector2 vis2 = this.getCreaturePosition().nor().add(200, 200).rotate(this.orientation);
		Vector2 vis3 = this.getCreaturePosition().nor().add(400, 50).rotate(this.orientation);
		Vector2 vis4 = this.getCreaturePosition().nor().add(400, -50).rotate(this.orientation);
		Vector2 vis5 = this.getCreaturePosition().nor().add(200, -200).rotate(this.orientation);
		Vector2 vis6 = this.getCreaturePosition().nor().add(-50, -300).rotate(this.orientation);


		
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
		float minimumDistance = 1000000;
		float nose = 0;
 
		
		for (CheckPoint c : tank.CheckPoints)
		{
			float distance = this.getCreaturePosition().dst(c.getCheckPointPosition());
			if (distance < minimumDistance)
			{
				minimumDistance = distance;
				closer = c;
			}
			
			distance = distance < 400 ? distance : 400;
			float normDistance = 1-distance/400;
			if (PointInTriangle(c.getCheckPointPosition(), D, E, this.getCreaturePosition()))
			{
				//this.RayEnds.add(new Vector2(c.getX(), c.getY()));
				this.Brain.central.setValue(normDistance);
				if (distance < EATING_THRESHOLD)
				{
					this.canEat = true;
					closer = c;
				}
			}
			if (PointInTriangle(c.getCheckPointPosition(), B, D, this.getCreaturePosition()))
			{
				//this.RayEnds.add(new Vector2(c.getX(), c.getY()));
				this.Brain.left1.setValue(normDistance);
				if (distance < EATING_THRESHOLD)
				{
					this.canEat = true;
					closer = c;
				}
			}

			if (PointInTriangle(c.getCheckPointPosition(), A, B, this.getCreaturePosition()))
			{
				//this.RayEnds.add(new Vector2(c.getX(), c.getY()));
				this.Brain.left2.setValue(normDistance);
				if (distance < EATING_THRESHOLD)
				{
					this.canEat = true;
					closer = c;
				}
			}
			if (PointInTriangle(c.getCheckPointPosition(), E, F, this.getCreaturePosition()))
			{
				//this.RayEnds.add(new Vector2(c.getX(), c.getY()));
				this.Brain.right1.setValue(normDistance);
				if (distance < EATING_THRESHOLD)
				{
					this.canEat = true;
					closer = c;
				}
			}

			if (PointInTriangle(c.getCheckPointPosition(), F, G, this.getCreaturePosition()))
			{
				//this.RayEnds.add(new Vector2(c.getX(), c.getY()));
				this.Brain.right2.setValue(normDistance);
				if (distance < EATING_THRESHOLD)
				{
					this.canEat = true;
					closer = c;
				}
			}

		}
		
		for(int i = 0; i < tank.Creatures.size; i++)
		{
			Creature another = tank.Creatures.get(i);
			if(!another.equals(this) && another.getCreaturePosition().dst(this.getCreaturePosition()) < EATING_THRESHOLD){
				this.Brain.partner.setValue(0);
			}
		}
		
		
		
		minimumDistance = minimumDistance < 800 ? minimumDistance : 800;
		nose = 1-minimumDistance/800;
		
		float normalizedXToWalls = 2*Math.abs(this.x)/tank.getXsize();
		float normalizedYToWalls = 2*Math.abs(this.y)/tank.getYsize();
		
		this.Brain.wallsX.setValue(normalizedXToWalls);
		this.Brain.wallsY.setValue(normalizedYToWalls);
		this.Brain.lrdist.setValue(nose);
		this.closerCheckPoint = closer;
		
		//System.out.println("L2: " + Brain.left2.getValue() + " L1: " + Brain.left1.getValue() + " C: " + Brain.central.getValue() + " R1: " + Brain.right1.getValue() + " R2: " + Brain.right2.getValue());
		//System.out.println("Partner: " + Brain.partner.getValue() + " X: " + Brain.wallsX.getValue() + " Y: " + Brain.wallsY.getValue() + " Nose: " + Brain.lrdist.getValue());
	}

	public boolean Eat(double outputFromBrain, CheckPoint closer, Tank tank)
	{
		if (canEat && (outputFromBrain >= 0.4))
		{
			tank.CheckPoints.removeValue(closer, true);
			return true;
		}
		else
			return false;
	}

	public File StoreDNA(int generation) throws IOException
	{
		Json json = new Json();
		FileWriter file = new FileWriter(System.getProperty("user.dir") + "\\DNA\\" + this.id + "-"
				+ generation + ".json");
		file.write(json.prettyPrint(this.Brain.dna));
		file.flush();
		file.close();
		File finished = new File(System.getProperty("user.dir") + "\\DNA\\" + this.id + "-"
				+ generation + ".json");
		return finished;
	}

	public float getX()
	{
		return x;
	}

	public void setX(float x)
	{
		this.x = x;
	}

	public float getY()
	{
		return y;
	}

	public void setY(float y)
	{
		this.y = y;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public float getOrientation()
	{
		while (this.orientation >= 360)
		{
			this.orientation = this.orientation - 360;
		}
		while (this.orientation <= 0)
		{
			this.orientation = this.orientation + 360;
		}
		return this.orientation;
	}

	public void setOrientation(float orientation)
	{
		float neworientation = orientation;
		while (neworientation >= 360)
		{
			neworientation = neworientation - 360;
		}
		while (neworientation <= 0)
		{
			neworientation = neworientation + 360;
		}
		this.orientation = neworientation;
	}

	public int getHealth()
	{
		return health;
	}

	public void setHealth(int health)
	{
		this.health = health;
	}

	@Override
	public int compareTo(Creature o)
	{
		if (this.health < o.getHealth())
		{
			return 1;
		}
		else if (this.health == o.getHealth())
			return 0;
		else
			return -1;
	}

	public Vector2 getCreaturePosition()
	{
		return new Vector2(this.x, this.y);
	}

}
