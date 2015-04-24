package com.salt.grrow.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.salt.grrow.model.Creature;
import com.salt.grrow.model.DNA;

public class CreatureView extends Creature{
	
	public Image actor;

	public CreatureView(String id, float x, float y, float orientation,
			int collisionRadius, DNA dna, Texture creatureTexture) {
		super(id, x, y, orientation, collisionRadius, dna);
		this.actor = new Image(creatureTexture);
		this.actor.setName("Creature");
		this.actor.setOrigin(creatureTexture.getWidth()/2, creatureTexture.getHeight()/2);
	}
	
	public void updateActor(){
		
	}

}
