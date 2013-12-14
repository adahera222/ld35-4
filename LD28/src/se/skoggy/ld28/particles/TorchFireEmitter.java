package se.skoggy.ld28.particles;

import se.skoggy.utils.TimerTrig;

public class TorchFireEmitter extends ParticleEmitter{

	TimerTrig timer;

	public TorchFireEmitter(float interval) {
		timer = new TimerTrig(interval);
	}


	@Override
	public void update(float dt, ParticleManager particleManager) {
		if(timer.isTrigged(dt)){
			particleManager.spawnTorchFire(this);
		}
	}
}
