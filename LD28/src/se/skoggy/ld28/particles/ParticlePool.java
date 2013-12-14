package se.skoggy.ld28.particles;

import se.skoggy.utils.Pool;

public class ParticlePool extends Pool<Particle>{

	Particle[] items;

	public ParticlePool(int capacity) {
		super(capacity);
		items = new Particle[capacity];
		for (int i = 0; i < items.length; i++) {
			items[i] = new Particle();
		}
	}

	@Override
	public Particle pop() {
		return items[count++];
	}

	@Override
	public void push(int index) {
		Particle temp = items[count - 1];
		items[count - 1] = items[index];
		items[index] = temp;
		count--;
	}
}
