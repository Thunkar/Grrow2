package com.thunkar.grrow.model;

import com.badlogic.gdx.math.Vector2;

public class CheckPoint {

	private int x;
    private int y;
    public final int collisionRadius;

    public Vector2 getCheckPointPosition() {
		return new Vector2(this.x, this.y);
	}



	public CheckPoint(int x, int y, int collisionRadius)
    {
        this.x = x;
        this.y = y;
        this.collisionRadius = collisionRadius;
    }

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	
	
}
