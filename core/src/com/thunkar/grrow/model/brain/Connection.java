package com.thunkar.grrow.model.brain;

public class Connection {
	
	private Neuron Input;
	private float Weight;

	public Connection(Neuron input, float weight){
		this.Input = input;
		this.Weight = weight;
	}

	public Neuron getInput() {
		return Input;
	}

	public void setInput(Neuron input) {
		Input = input;
	}

	public float getWeight() {
		return Weight;
	}

	public void setWeight(float weight) {
		Weight = weight;
	}
	
}
