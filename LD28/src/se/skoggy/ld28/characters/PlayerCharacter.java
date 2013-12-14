package se.skoggy.ld28.characters;

import java.util.HashMap;

import se.skoggy.animation.Animation;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerCharacter extends GameCharacter{

	public PlayerCharacter(TextureRegion texture, HashMap<String, Animation> animations, int tileSize, int sheetDimension) {
		super(texture, animations, tileSize, sheetDimension);
	}

}
