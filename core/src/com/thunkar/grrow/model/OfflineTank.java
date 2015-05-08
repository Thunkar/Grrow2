package com.thunkar.grrow.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

public class OfflineTank extends Tank
{

	private int CreatureNumber;
	private int CheckPointNumber;
	private int generation;
	private static final int iterationNumber = 1000;
	private Array<Creature> lastGeneration;
	public static PrintWriter printwr = new PrintWriter(System.out);

	public OfflineTank(int x, int y, int creatures, int checkpoints, int seed)
	{
		super(x, y, seed);
		this.CreatureNumber = creatures;
		this.CheckPointNumber = checkpoints;
		generateCheckPoints();
		this.setGeneration(0);
		this.lastGeneration = new Array<Creature>();
		Boolean dir1exists = new File(System.getProperty("user.dir") + "\\DNA\\").exists();
		Boolean dir2exists = new File(System.getProperty("user.dir") + "\\Statistics\\").exists();
		if (!dir1exists)
		{
			@SuppressWarnings("unused")
			Boolean setup1 = new File(System.getProperty("user.dir") + "\\DNA\\").mkdir();
		}
		if (!dir2exists)
		{
			@SuppressWarnings("unused")
			Boolean setup2 = new File(System.getProperty("user.dir") + "\\Statistics\\").mkdir();
		}
	}

	public void evaluateCreatures()
	{
		this.Creatures.sort();
		Iterator<Creature> itr = this.Creatures.iterator();
		for (int i = 0; i < 9; i++)
		{
			Creature dummy = itr.next();
			this.lastGeneration.add(dummy);
		}
		this.Creatures.removeAll(this.lastGeneration, true);
		Creature UglyOneThatFucks = null;
		if (DNA.getRandom().nextDouble() < 0.05)
		{
			UglyOneThatFucks = this.Creatures.random();
			System.out.println("The ugly one fucked!");
		}
		else
			UglyOneThatFucks = itr.next();
		if (UglyOneThatFucks != null)
		{
			this.lastGeneration.add(UglyOneThatFucks);
		}
		this.Creatures.clear();
		this.Creatures.addAll(reproduce(this.lastGeneration));
		this.Creatures.addAll(this.lastGeneration);
	}

	public void clearLastGeneration()
	{
		this.lastGeneration.clear();
	}

	public Array<DNA> extractDNA(Array<Creature> creatures)
	{
		Array<DNA> dnas = new Array<DNA>();
		for (Creature c : creatures)
		{
			dnas.add(c.Brain.dna);
		}
		return dnas;
	}

	public Array<Creature> reproduce(Array<Creature> creatures)
	{
		Array<Creature> newCreatures = new Array<Creature>();
		DNA[] recovered = new DNA[10];
		int dnacounter = 0;
		for (Creature c : creatures)
		{
			recovered[dnacounter] = c.Brain.dna;
			dnacounter++;
		}
		int counter = 0;
		for (int j = 0; j < 10; j++)
		{
			for (int i = 0; i < 10; i++)
			{
				if (!recovered[i].equals(recovered[j]))
				{
					DNA result = recovered[j].hybridate(recovered[i]);
					String name = counter + "";
					Creature creature = new Creature(name, 0, -330, 90, 10, result);
					newCreatures.add(creature);
					counter++;
				}
			}
		}
		return newCreatures;
	}

	public void newGeneration() throws IOException
	{
		this.lastGeneration.clear();
		for (Creature c : this.Creatures)
		{
			c.resetHealth();
		}
		generateCheckPoints();
		for (int i = 0; i < iterationNumber; i++)
		{
			creatureIteration();
		}
		this.generation++;
		evaluateCreatures();
	}

	public void firstGeneration()
	{
		generateCreatures();
		for (int i = 0; i < iterationNumber; i++)
		{
			creatureIteration();
		}
		this.generation++;
		evaluateCreatures();
	}

	public void generateCreatures()
	{
		for (int i = 0; i < this.CreatureNumber; i++)
		{
			DNA dna = new DNA();
			String name = i + "";
			Creature creature = new Creature(name, 0, -330, 90, 10, dna);
			this.Creatures.add(creature);
		}
	}

	public void generateCheckPoints()
	{
		this.CheckPoints.clear();
		for (int i = 0; i < this.CheckPointNumber; i++)
		{
			CheckPoint checkpoint = new CheckPoint(this.nextIntWithNegatives(650),
					this.nextIntWithNegatives(350), 10);
			this.CheckPoints.add(checkpoint);
		}
	}

	public void creatureIteration()
	{
		for (Creature c : this.Creatures)
		{
			float tempX = c.getX();
			boolean hasEaten = false;
			boolean hasCrashed = false;
			float tempY = c.getY();
			float tempRot = c.getOrientation();
			Vector2 previousPos = c.getCreaturePosition();
			c.Sense(this);
			c.Brain.Propagate();
			hasEaten = c.Eat(c.Brain.ImpulseEat(), c.closerCheckPoint, this);
			if (hasEaten)
			{
				this.CheckPoints.add(new CheckPoint(this.nextIntWithNegatives(650), this
						.nextIntWithNegatives(350), 10));
			}
			tempRot += (c.Brain.ImpulseRotateSign() * c.Brain.ImpulseRotateAmounth());
			tempX += c.Brain.ImpulsePush() * Math.cos(Math.toRadians(c.getOrientation()));
			tempY += c.Brain.ImpulsePush() * Math.sin(Math.toRadians(c.getOrientation()));
			if (tempX > 650)
			{
				tempX = 650;
				hasCrashed = true;
			}
			if (tempX < -650)
			{
				tempX = -650;
				hasCrashed = true;
			}
			if (tempY > 350)
			{
				tempY = 350;
				hasCrashed = true;
			}
			if (tempY < -350)
			{
				tempY = -350;
				hasCrashed = true;
			}
			c.setX(tempX);
			c.setY(tempY);
			c.setOrientation(tempRot);
			fitnessFunction(c, hasEaten, hasCrashed, previousPos);
		}
	}

	private void fitnessFunction(Creature c, boolean hasEaten, boolean hasCrashed,
			Vector2 previousPosition)
	{
		c.health -= 1;
		if (hasCrashed)
		{
			c.health -= 10;
		}
		if (hasEaten)
		{
			c.health += 50;
		}
	}

	public static Array<Creature> creaturesFromFiles() throws FileNotFoundException
	{
		File dir = new File(System.getProperty("user.dir") + "\\DNA\\");
		DNA[] recovered = new DNA[10];
		int dnacounter = 0;
		for (File f : dir.listFiles())
		{
			FileReader reader = new FileReader(f);
			Json json = new Json();
			DNA dna = json.fromJson(DNA.class, reader);
			recovered[dnacounter] = dna;
			dnacounter++;
		}
		Array<Creature> newCreatures = new Array<Creature>();
		int counter = 0;
		for (DNA dna : recovered)
		{
			String name = counter + "";
			Creature creature = new Creature(name, 0, -330, 90, 10, dna);
			newCreatures.add(creature);
			counter++;
		}
		return newCreatures;
	}

	public void saveLastGeneration() throws IOException
	{
		File dir = new File(System.getProperty("user.dir") + "\\DNA\\");
		for (File f : dir.listFiles())
		{
			f.delete();
		}
		for (int i = 0; i < 10; i++)
		{
			this.Creatures.get(i).StoreDNA(this.getGeneration());
		}
	}

	public Array<Creature> getLastGeneration()
	{
		return lastGeneration;
	}

	public void setLastGeneration(Array<Creature> lastGeneration)
	{
		this.lastGeneration = lastGeneration;
	}

	public int getGeneration()
	{
		return generation;
	}

	public void setGeneration(int generation)
	{
		this.generation = generation;
	}
}
