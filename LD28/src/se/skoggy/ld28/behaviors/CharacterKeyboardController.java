package se.skoggy.ld28.behaviors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import se.skoggy.entity.Entity;
import se.skoggy.entity.EntityBehavior;
import se.skoggy.ld28.characters.GameCharacter;

public class CharacterKeyboardController extends EntityBehavior{

	@Override
	public void Update(float dt, Entity owner) {
		GameCharacter c = (GameCharacter)owner;

		if(c.isGrounded()){
			if(Gdx.input.isKeyPressed(Keys.LEFT)){
				c.walkLeft(dt);
			}else if(Gdx.input.isKeyPressed(Keys.RIGHT)){
				c.walkRight(dt);
			}else{
				c.setAnim("idle");
			}
			if(Gdx.input.isKeyPressed(Keys.UP)){
				c.jump();
			}
		}else{
			if(Gdx.input.isKeyPressed(Keys.LEFT)){
				c.walkLeft(dt);
			}else if(Gdx.input.isKeyPressed(Keys.RIGHT)){
				c.walkRight(dt);
			}
		}
	}

	@Override
	public void BeforeDraw(SpriteBatch spriteBatch, Entity owner) {

	}

	@Override
	public void AfterDraw(SpriteBatch spriteBatch, Entity owner) {

	}

}
