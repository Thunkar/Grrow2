package com.thunkar.grrow.model.brain;


import com.badlogic.gdx.utils.Array;

public class Output implements Neuron{
	
	public Array<Connection> Connections;
    private float activation;
	private float value;
    
    public Output(float activation)
    {
    	this.Connections = new Array<Connection>();
        this.activation = activation;
    }

    public void Propagate()
    {
    	double z = 0;
        for (Connection c: this.Connections)
        {
            z += c.getInput().getValue()*c.getWeight();
        }
        this.value = this.activation / (1 + (float)(Math.pow(((float)Math.E), -z+1)));
    }
    
	public double getActivation() {
		return activation;
	}

	public void setActivation(float activation) {
		this.activation = activation;
	}

	@Override
	public float getValue() {
		return this.value;
	}

	public void setValue(float value) {
		this.value = value;
	}

}
