package se.skoggy.ld28.particles;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import se.skoggy.content.ContentManager;
import se.skoggy.entity.Entity;
import se.skoggy.ld28.GameSettings;
import se.skoggy.utils.Helpers;
import se.skoggy.utils.Rand;

public class ParticleManager {

	ParticlePool particles;
	Entity particle;
	int dimension = 8;
	int size = 8;
	Color[] fireColors = new Color[]{
			new Color(1f, 0f, 0f, 1f),
			new Color(1f, 1f, 0f, 1f),
			new Color(1f, 1f, 1f, 1f)
	};
	Entity torch;
	List<ParticleEmitter> emitters;

	public ParticleManager() {
		particles = new ParticlePool(GameSettings.MAX_PARTICLES);
		emitters = new ArrayList<ParticleEmitter>();
	}

	public void load(ContentManager content){
		particle = new Entity(content.loadTexture("gfx/particles"));
	}

	public void clear(){
		particles.clear();
		emitters.clear();
	}

	public void registerEmitter(ParticleEmitter emitter){
		emitters.add(emitter);
	}

	public void spawnAir(float x, float y, float width) {
		for (int i = 0; i < 3; i++) {
			Particle p = particles.pop();
			p.owner = null;
			p.type = 0;
			p.current = 0f;
			p.duration = 200f;
			p.startScale = 1f;
			p.endScale = 4f;
			p.rotation = 0f;
			p.x = x + (-0.5f + Rand.rand()) * width;
			p.y = y;
			p.vx = 0f;
			p.vy = (-3f + Rand.rand()) * 0.6f;
			p.startColor.r = 1f;
			p.startColor.g = 1f;
			p.startColor.b = 1f;
			p.startColor.a = 0.4f;
			p.endColor.r = 0f;
			p.endColor.g = 0f;
			p.endColor.b = 0f;
			p.endColor.a = 0f;
			p.rotationSpeed = 0f;
		}
	}

	public void spawnTorchFire(ParticleEmitter emitter){
		for (int i = 0; i < 2; i++) {
			Particle p = particles.pop();
			p.owner = emitter;
			p.type = 0;
			p.current = 0f;
			p.duration = 500f + Rand.rand() * 1000f;
			p.startScale = 0.5f;
			p.endScale = 0f;
			p.rotation = Rand.rand() * 10f;
			// local when using emitter
			p.x = 0f;
			p.y = 0f;
			p.vx = (-0.5f + Rand.rand()) * 0.005f;
			p.vy = (-1f + Rand.rand()) * 0.01f;
			p.startColor = fireColors[(int)(Rand.rand() * fireColors.length)].cpy();
			p.endColor = new Color(0f, 0f, 0f, 0f);
			p.rotationSpeed = (-0.5f + Rand.rand()) * 0.001f;
		}
	}


	public void update(float dt){
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).update(dt, this);
		}

		for (int i = 0; i < particles.count(); i++) {
			Particle p = particles.items[i];

			p.current += dt;
			float progress = p.current / p.duration;
			if(p.current > p.duration){
				particles.push(i--);
			}else{
				if(p.type == 0 || p.type == 1 || p.type == 2){
					p.x += p.vx * dt;
					p.y += p.vy * dt;
					p.rotation += p.rotationSpeed * dt;

					p.scale = Helpers.lerp(p.startScale, p.endScale, progress);
					p.color.a = Helpers.lerp(p.startColor.a, p.endColor.a, progress);
					p.color.r = Helpers.lerp(p.startColor.r, p.endColor.r, progress);
					p.color.g = Helpers.lerp(p.startColor.g, p.endColor.g, progress);
					p.color.b = Helpers.lerp(p.startColor.b, p.endColor.b, progress);

				}else{
					p.x += p.vx * dt;
					p.y += p.vy * dt;
				}
			}
		}
	}

	public void draw(SpriteBatch sb){
		sb.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		for (int i = 0; i < particles.count(); i++) {
			Particle p = particles.items[i];
			if(p.owner != null){
				particle.transform.position.x = p.owner.x + p.x;
				particle.transform.position.y = p.owner.y + p.y;
			}else{
				particle.transform.position.x = p.x;
				particle.transform.position.y = p.y;
			}
			particle.transform.rotation = p.rotation;
			particle.transform.scale.x = p.scale;
			particle.transform.scale.y = p.scale;
			particle.setSource(
					(p.type % dimension) * size,
					(p.type / dimension) * size,
					size, size);
			particle.setColor(p.color);
			particle.draw(sb);
		}
		sb.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
	}

}
