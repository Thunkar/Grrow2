package com.salt.grrow.model.brain;

import com.badlogic.gdx.utils.Array;
import com.salt.grrow.model.DNA;

public class _1LayerBrain {

    public Input central;
    public Input left1;
    public Input left2;
    public Input right1;
    public Input right2;
    public Input lrdist;
    public Hidden l1;
    public Hidden l2;
    public Hidden l3;
    public Hidden l4;
    public Hidden l5;
    public Hidden l6;
    public Hidden ll1;
    public Hidden ll2;
    public Hidden ll3;
    public Hidden ll4;
    public Hidden lll1;
    public Hidden lll2;
    public Hidden lll3;
    public Hidden lll4;
    public Hidden llll1;
    public Hidden llll2;
    public Hidden llll3;
    public Hidden llll4;
    public Output push;
    public Output rotateAmounth;
    public Output rotateSign;
    public Output eat;
    public Output pushMultiplier;
    public DNA dna;
    public Array<Neuron> neurons;
    public Array<Neuron> inputs;
    public Array<Neuron> firstLayer;
    public Array<Neuron> secondLayer;
    public Array<Neuron> thirdLayer;
    public Array<Neuron> fourthLayer;
    public Array<Neuron> outputs;

    public _1LayerBrain(DNA dna)
    {
        this.dna = dna;
        neurons = new Array<Neuron>();
        inputs = new Array<Neuron>();
        firstLayer = new Array<Neuron>();
        secondLayer = new Array<Neuron>();
        thirdLayer = new Array<Neuron>();
        fourthLayer = new Array<Neuron>();
        outputs = new Array<Neuron>();
        central = new Input();
        left2 = new Input();
        right1 = new Input();
        left1 = new Input();
        right2 = new Input();
        lrdist = new Input();
        l1 = new Hidden();
        l2 = new Hidden();
        l3 = new Hidden();
        l4 = new Hidden();
        l5= new Hidden();
        l6 = new Hidden();
        ll1 = new Hidden();
        ll2 = new Hidden();
        ll3 = new Hidden();
        ll4 = new Hidden();
        lll1 = new Hidden();
        lll2 = new Hidden();
        lll3 = new Hidden();
        lll4 = new Hidden();
        llll1 = new Hidden();
        llll2 = new Hidden();
        llll3 = new Hidden();
        llll4 = new Hidden();
        push = new Output(1);
        rotateAmounth = new Output(5);
        rotateSign = new Output(1);
        eat = new Output(1);
        pushMultiplier = new Output(2);
        inputs.add(central);
        inputs.add(left2);
        inputs.add(right1);
        inputs.add(right2);
        inputs.add(left1);
        inputs.add(lrdist);
        firstLayer.add(l1);
        firstLayer.add(l2);
        firstLayer.add(l3);
        firstLayer.add(l4);
        firstLayer.add(l5);
        firstLayer.add(l6);
        secondLayer.add(ll1);
        secondLayer.add(ll2);
        secondLayer.add(ll3);
        secondLayer.add(ll4);
        thirdLayer.add(lll1);
        thirdLayer.add(lll2);
        thirdLayer.add(lll3);
        thirdLayer.add(lll4);
        fourthLayer.add(llll1);
        fourthLayer.add(llll2);
        fourthLayer.add(llll3);
        fourthLayer.add(llll4);
        outputs.add(push);
        outputs.add(eat);
        outputs.add(rotateSign);
        outputs.add(rotateAmounth);
        outputs.add(pushMultiplier);
        neurons.addAll(inputs);
        neurons.addAll(firstLayer);
        neurons.addAll(secondLayer);
        neurons.addAll(thirdLayer);
        neurons.addAll(fourthLayer);
        neurons.addAll(outputs);
        buildBrain();
    }
    
    public void Propagate(){
    	for(Neuron n : this.neurons){
    		n.Propagate();
    	}
    }
    
    public void buildBrain(){
    	int dnaCounter = 0;
    	for(Neuron fl : this.firstLayer){
    		for(Neuron i : this.inputs){
    			Connection c = new Connection(i, this.dna.chain[dnaCounter]);
    			((Hidden)fl).Connections.add(c);
    			dnaCounter++;
    		}
    	}
    	for(Neuron fll : this.secondLayer){
    		for(Neuron fl : this.firstLayer){
    			Connection c = new Connection(fl, this.dna.chain[dnaCounter]);
    			((Hidden)fll).Connections.add(c);
    			dnaCounter++;
    		}
    	}
    	for(Neuron flll : this.thirdLayer){
    		for(Neuron fll : this.secondLayer){
    			Connection c = new Connection(fll, this.dna.chain[dnaCounter]);
    			((Hidden)flll).Connections.add(c);
    			dnaCounter++;
    		}
    	}
    	for(Neuron fllll : this.fourthLayer){
    		for(Neuron flll : this.thirdLayer){
    			Connection c = new Connection(flll, this.dna.chain[dnaCounter]);
    			((Hidden)fllll).Connections.add(c);
    			dnaCounter++;
    		}
    	}
    	for(Neuron o : this.outputs){
    		for(Neuron fllll : this.fourthLayer){
    			Connection c = new Connection(fllll, this.dna.chain[dnaCounter]);
    			((Output)o).Connections.add(c);
    			dnaCounter++;
    		}
    	}
    	
    }

	public double ImpulseEat() {
		return this.eat.getValue();
	}

	public float ImpulseRotateSign() {
		float sign = this.rotateSign.getValue();
		if(sign < 0.5) return -1;
		else return 1;
	}

	public float ImpulseRotateAmounth() {
		
		return this.rotateAmounth.getValue();
	}

	public double ImpulsePush() {
		return this.push.getValue()*this.pushMultiplier.getValue();
	}

 

}
