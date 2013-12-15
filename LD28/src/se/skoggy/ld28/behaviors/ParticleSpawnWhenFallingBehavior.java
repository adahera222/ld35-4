package se.skoggy.ld28.behaviors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import se.skoggy.entity.Entity;
import se.skoggy.entity.EntityBehavior;
import se.skoggy.ld28.particles.ParticleManager;
import se.skoggy.utils.TimerTrig;

public class ParticleSpawnWhenFallingBehavior extends EntityBehavior{

	ParticleManager particleManager;
	TimerTrig interval;

	public ParticleSpawnWhenFallingBehavior(ParticleManager particleManager) {
		this.particleManager = particleManager;
		interval = new TimerTrig(120f);
	}

	@Override
	public void Update(float dt, Entity owner) {
		if(interval.isTrigged(dt)){
			particleManager.spawnAir(owner.transform.position.x, owner.transform.position.y, (owner.origin.x * 2f) * owner.transform.scale.x);
		}
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
