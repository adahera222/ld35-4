package se.skoggy.ld28.behaviors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import se.skoggy.entity.Entity;
import se.skoggy.entity.EntityBehavior;

public class RotationBehavior extends EntityBehavior{

	float speed;

	public RotationBehavior(float speed) {
		this.speed = speed;
	}

	@Override
	public void Update(float dt, Entity owner) {

		owner.transform.rotation += speed * dt;
	}

	@Override
	public void BeforeDraw(SpriteBatch spriteBatch, Entity owner) {
		// TODO Auto-generated method stub

	}

	@Override
	public void AfterDraw(SpriteBatch spriteBatch, Entity owner) {
		// TODO Auto-generated method stub

	}

}
