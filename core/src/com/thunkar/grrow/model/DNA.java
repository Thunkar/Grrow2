package com.thunkar.grrow.model;


import java.util.Random;



public class DNA {

	 private static Random random = new Random();
     public float[] chain;
     private int size;

     
     public DNA(){
    	 this.size = 130;
         this.chain = new float[size];
         for(int i = 0; i<this.size; i++){
        	 this.chain[i] = DNA.nextFloatWithNegatives(10);
         }
     }
     
  

	public static Random getRandom() {
		return random;
	}

	public static void setRandom(Random random) {
		DNA.random = random;
	}
	
	public static int nextIntWithNegatives(int n){
		if(DNA.random.nextBoolean()){
			return DNA.random.nextInt(n);
		}
		else return -DNA.random.nextInt(n);
	}
	
	public static float nextFloatWithNegatives(float n){
		if(DNA.random.nextBoolean()){
			return n*DNA.random.nextFloat();
		}
		else return -n*DNA.random.nextFloat();
	}
	
	
     
	public DNA hybridate(DNA dna){
		DNA result = new DNA();
		int cutpoint = random.nextInt(size);
		for(int i = 0; i < cutpoint; i++){
			result.chain[i] = this.chain[i];
		}
		for(int i = cutpoint; i<size; i++){
			result.chain[i] = dna.chain[i];
		}
		if(DNA.getRandom().nextDouble()<0.003){
			result.chain[cutpoint] = DNA.nextFloatWithNegatives(10);
			System.out.println("Mutation!");
		}
		return result;
	}
	
}
