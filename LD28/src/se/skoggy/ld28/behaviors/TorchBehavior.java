package se.skoggy.ld28.behaviors;

import se.skoggy.entity.Entity;
import se.skoggy.entity.EntityBehavior;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class TorchBehavior extends EntityBehavior {

	Entity torch;
	Vector2 offset;

	public TorchBehavior(TextureRegion torchTexture) {
		torch = new Entity(torchTexture);
		offset = new Vector2();
		torch.transform.setScale(0.6f);
		offset.x = 8f * torch.transform.scale.x;
		offset.y = -9f * torch.transform.scale.y;
	}

	@Override
	public void Update(float dt, Entity owner) {
		// TODO: spawn fire particles
	}

	@Override
	public void BeforeDraw(SpriteBatch spriteBatch, Entity owner) {

	}

	@Override
	public void AfterDraw(SpriteBatch spriteBatch, Entity owner) {
		torch.transform.position.x = owner.transform.position.x + (offset.x * (owner.isFlipX() ? -1f : 1));
		torch.transform.position.y = owner.transform.position.y + offset.y;
		torch.draw(spriteBatch);
	}

}
