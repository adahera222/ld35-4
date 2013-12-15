package se.skoggy.ld28.characters;

import java.util.HashMap;

import se.skoggy.animation.Animation;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerCharacter extends GameCharacter{

	public PlayerCharacter(TextureRegion texture, HashMap<String, Animation> animations, int tileSize, int sheetDimension) {
		super(texture, animations, tileSize, sheetDimension);

		setAnim("walk");
	}

	public void updateWithoutBehaviors(float dt) {

		animations.get(currentAnimation).update(dt);
		int frame = animations.get(currentAnimation).getFrame();
		int col = (frame % sheetDimension);
		int row = (frame / sheetDimension);
		setSource(col * tileSize, row * tileSize, tileSize, tileSize);
	}

}
