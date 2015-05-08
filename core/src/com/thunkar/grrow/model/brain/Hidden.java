package com.thunkar.grrow.model.brain;



import com.badlogic.gdx.utils.Array;

public class Hidden implements Neuron{

	public Array<Connection> Connections;
	private float value;

	public Hidden()
	{
		Connections = new Array<Connection>();
	}

	public void Propagate()
	{
		double z = 0;
		for (Connection c: this.Connections)
		{
			z += c.getInput().getValue()*c.getWeight();
		}
		this.value = 1 / (1 + (float)(Math.pow(((float)Math.E), -z+1)));
	}



	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

}
