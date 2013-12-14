package se.skoggy.ld28.behaviors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import se.skoggy.entity.Entity;
import se.skoggy.entity.EntityBehavior;
import se.skoggy.ld28.characters.GameCharacter;
import se.skoggy.ld28.maps.Map;

public class WalkBackAndForthBehavior extends EntityBehavior {

	Map map;

	public WalkBackAndForthBehavior(Map map){
		this.map = map;
	}

	@Override
	public void Update(float dt, Entity owner) {
		GameCharacter c = (GameCharacter)owner;

		int col = 0;
		int row = 0;

		int padding = 4;
		if(c.isFacingLeft()){
			col = (int)Math.floor((c.left() - padding) / map.tilewidth);
			row = (int)Math.floor(c.transform.position.y / map.tileheight);
			if(map.collides(col, row)){
				c.walkRight(dt);
			}
		}else{
			col = (int)Math.floor((c.right() + padding) / map.tilewidth);
			row = (int)Math.floor(c.transform.position.y / map.tileheight);
			if(map.collides(col, row)){
				c.walkLeft(dt);
			}
		}

		if(c.isFacingLeft()){
			c.walkLeft(dt);
		}else{
			c.walkRight(dt);
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
