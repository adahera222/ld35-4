package se.skoggy.ld28.behaviors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import se.skoggy.entity.Entity;
import se.skoggy.entity.EntityBehavior;
import se.skoggy.ld28.GameSettings;
import se.skoggy.ld28.characters.GameCharacter;
import se.skoggy.ld28.maps.Map;
import se.skoggy.utils.Rand;

public class FollowCharacterBehavior extends EntityBehavior{

	Vector2 target;
	float minDistance;
	float maxDistance = GameSettings.CAT_MAX_DISTANCE_BEFORE_TELEPORTING;
	Map map;

	public FollowCharacterBehavior(Vector2 target, Map map) {
		this.target = target;
		this.map = map;
		minDistance = Rand.rand() * 32f;
	}

	public void setTarget(Vector2 target) {
		this.target = target;
	}

	@Override
	public void Update(float dt, Entity owner) {
		GameCharacter c = (GameCharacter)owner;

		float distance = Math.abs(target.x - c.transform.position.x);
		if(distance > minDistance){ // should move towards target

			// check if something is in the way
			if(c.transform.velocity.x > 0f){ // right
				if(map.collides((int)Math.floor((c.right() + c.transform.velocity.x * dt) / map.tilewidth),
								(int)Math.floor(c.transform.position.y / map.tileheight))){
					c.jump();
				}
			}else if(c.transform.velocity.x < 0f){ // left
				if(map.collides((int)Math.floor((c.left() + c.transform.velocity.x * dt) / map.tilewidth),
								(int)Math.floor(c.transform.position.y / map.tileheight))){
					c.jump();
				}
			}

			if(target.x < c.transform.position.x){
				c.walkLeft(dt);
			}else{
				c.walkRight(dt);
			}

			if(distance > maxDistance){
				//c.teleport(target.x,  target.y);
			}
		}else{
			c.setAnim("idle");
		}
	}

	@Override
	public void BeforeDraw(SpriteBatch spriteBatch, Entity owner) {
	}

	@Override
	public void AfterDraw(SpriteBatch spriteBatch, Entity owner) {
	}

}
