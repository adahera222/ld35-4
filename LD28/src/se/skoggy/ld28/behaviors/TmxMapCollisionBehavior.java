package se.skoggy.ld28.behaviors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import se.skoggy.entity.Entity;
import se.skoggy.entity.EntityBehavior;
import se.skoggy.ld28.GameSettings;
import se.skoggy.ld28.characters.GameCharacter;
import se.skoggy.ld28.maps.Map;

public class TmxMapCollisionBehavior extends EntityBehavior{

	private Map map;

	public TmxMapCollisionBehavior(Map map) {
		this.map = map;
	}

	@Override
	public void Update(float dt, Entity owner) {
		GameCharacter c = (GameCharacter)owner;

		int col = 0;
		int row = 0;

		if(c.isGrounded()){
			// check if no ground
			col = (int)Math.floor(c.transform.position.x / map.tilewidth);
			row = (int)Math.floor(c.bottom() / map.tileheight);
			if(!map.collides(col, row)){
				c.fallOff();
			}
		}else{
			if(c.transform.velocity.y > 0f){
				// check for ground
				col = (int)Math.floor(c.transform.position.x / map.tilewidth);
				row = (int)Math.floor(c.bottom() / map.tileheight);
				if(map.collides(col, row)){
					c.land();
					c.transform.position.y = (row * map.tileheight) - c.halfHeight();
				}
			}else if(c.transform.velocity.y < 0f){
				// check for roof
				col = (int)Math.floor(c.transform.position.x / map.tilewidth);
				row = (int)Math.floor(c.top() / map.tileheight);
				if(map.collides(col, row, GameSettings.TILES_IGNORE_GOING_UP)){
					c.transform.velocity.y = 0f;
					c.transform.position.y = ((row + 1) * map.tileheight) + c.halfHeight();
				}
			}

		}

		// walls
		col = (int)Math.floor(c.left() / map.tilewidth);
		row = (int)Math.floor(c.transform.position.y / map.tileheight);
		// left
		if(map.collides(col, row, GameSettings.TILES_IGNORE_GOING_UP) && c.transform.velocity.x < 0f){
			c.transform.velocity.x = 0f;
			c.transform.position.x = ((col + 1) * map.tilewidth) + c.halfWidth();
		}
		// right
		col = (int)Math.floor(c.right() / map.tilewidth);
		row = (int)Math.floor(c.transform.position.y / map.tileheight);
		if(map.collides(col, row, GameSettings.TILES_IGNORE_GOING_UP) && c.transform.velocity.x > 0f){
			c.transform.velocity.x = 0f;
			c.transform.position.x = (col * map.tilewidth) - c.halfWidth();
		}
	}

	@Override
	public void BeforeDraw(SpriteBatch spriteBatch, Entity owner) {}

	public void AfterDraw(SpriteBatch spriteBatch, Entity owner) {}
}
