package com.thunkar.grrow.model.brain;




public class Input implements Neuron{
	

    private float value;



	public float getValue() {
		return value;
	}


	public void setValue(float value) {
		this.value = value;
	}


	public Input()
    {
		this.value = 0;
    }


	@Override
	public void Propagate() {
	}


}
