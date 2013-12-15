package se.skoggy.ld28.behaviors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import se.skoggy.entity.Entity;
import se.skoggy.entity.EntityBehavior;
import se.skoggy.ld28.characters.GameCharacter;

public class GoInOneDirectionBehavior extends EntityBehavior{

	boolean right;

	public GoInOneDirectionBehavior(boolean right) {
		this.right = right;
	}

	@Override
	public void Update(float dt, Entity owner) {
		GameCharacter c = (GameCharacter)owner;
		if(right){
			c.goRight(dt);
		}else{
			c.goLeft(dt);
		}
	}

	@Override
	public void BeforeDraw(SpriteBatch spriteBatch, Entity owner) {

	}

	@Override
	public void AfterDraw(SpriteBatch spriteBatch, Entity owner) {

	}

}
