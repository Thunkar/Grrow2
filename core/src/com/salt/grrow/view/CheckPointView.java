package com.salt.grrow.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.salt.grrow.model.CheckPoint;

public class CheckPointView extends CheckPoint{

	public Texture foodTexture;
	public Image actor;
	public CheckPointView(int x, int y, int collisionRadius, Texture foodTexture) {
		super(x, y, collisionRadius);
		this.actor = new Image(foodTexture);
		this.actor.setName("Food");
		this.actor.setOrigin(foodTexture.getWidth()/2, foodTexture.getHeight()/2);
	}

}
