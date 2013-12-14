package se.skoggy.ld28.particles;

public abstract class ParticleEmitter {
	public float x, y;
	public abstract void update(float dt, ParticleManager particleManager);
}
