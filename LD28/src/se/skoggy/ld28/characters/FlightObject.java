package se.skoggy.ld28.characters;

import java.util.HashMap;

import se.skoggy.animation.Animation;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FlightObject extends GameCharacter {

	public FlightObject(TextureRegion texture,
			HashMap<String, Animation> animations, int tileSize,
			int sheetDimension) {
		super(texture, animations, tileSize, sheetDimension);
	}

}
