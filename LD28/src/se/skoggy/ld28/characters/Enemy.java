package se.skoggy.ld28.characters;

import java.util.HashMap;

import se.skoggy.animation.Animation;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Enemy extends GameCharacter{

	boolean dead;

	public Enemy(TextureRegion texture, HashMap<String, Animation> animations,
			int tileSize, int sheetDimension) {
		super(texture, animations, tileSize, sheetDimension);
	}

	public void kill(){
		dead = true;
	}

	public boolean isDead() {
		return dead;
	}
}
