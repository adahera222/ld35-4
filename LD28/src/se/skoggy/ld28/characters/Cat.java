package se.skoggy.ld28.characters;

import java.util.HashMap;

import se.skoggy.animation.Animation;
import se.skoggy.entity.EntityBehavior;
import se.skoggy.ld28.behaviors.FollowCharacterBehavior;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Cat extends GameCharacter{

	FollowCharacterBehavior follow;

	public Cat(TextureRegion texture, HashMap<String, Animation> animations) {
		super(texture, animations, 16, 4);
	}

	public void setTarget(Vector2 target){
		follow.setTarget(target);
	}

	@Override
	public void addBehavior(EntityBehavior behavior) {
		if(behavior instanceof FollowCharacterBehavior){
			follow = (FollowCharacterBehavior)behavior;
		}
		super.addBehavior(behavior);
	}
}
