package se.skoggy.ld28.behaviors;

import se.skoggy.entity.Entity;
import se.skoggy.entity.EntityBehavior;
import se.skoggy.ld28.particles.ParticleManager;
import se.skoggy.ld28.particles.TorchFireEmitter;
import se.skoggy.utils.TimerTrig;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class TorchBehavior extends EntityBehavior {

	private ParticleManager particleManager;
	Entity torch;
	Vector2 offset;
	TorchFireEmitter fireEmitter;

	public TorchBehavior(TextureRegion torchTexture, ParticleManager particleManager) {
		this.particleManager = particleManager;
		torch = new Entity(torchTexture);
		offset = new Vector2();
		torch.transform.setScale(0.6f);
		offset.x = 8f * torch.transform.scale.x;
		offset.y = -9f * torch.transform.scale.y;
		fireEmitter = new TorchFireEmitter(40f);
		particleManager.registerEmitter(fireEmitter);
	}

	@Override
	public void Update(float dt, Entity owner) {
	}

	@Override
	public void BeforeDraw(SpriteBatch spriteBatch, Entity owner) {
	}

	@Override
	public void AfterDraw(SpriteBatch spriteBatch, Entity owner) {
		torch.transform.position.x = owner.transform.position.x + (offset.x * (owner.isFlipX() ? -1f : 1));
		torch.transform.position.y = owner.transform.position.y + offset.y;
		torch.draw(spriteBatch);
		fireEmitter.x = torch.transform.position.x;
		fireEmitter.y = torch.transform.position.y - torch.origin.y * torch.transform.scale.y;
	}
}
