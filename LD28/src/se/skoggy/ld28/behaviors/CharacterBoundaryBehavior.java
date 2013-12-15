package se.skoggy.ld28.behaviors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import se.skoggy.entity.Entity;
import se.skoggy.entity.EntityBehavior;
import se.skoggy.ld28.characters.GameCharacter;

public class CharacterBoundaryBehavior extends EntityBehavior {

	Rectangle area;

	public CharacterBoundaryBehavior(Rectangle area) {
		this.area = area;
	}

	@Override
	public void Update(float dt, Entity owner) {
		GameCharacter c = (GameCharacter)owner;
		if(c.left() < area.x){
			c.transform.position.x = area.x + c.origin.x * c.transform.scale.x;
			c.transform.velocity.x = 0f;
		}
		if(c.right() > area.x + area.width){
			c.transform.position.x = (area.x + area.width) - c.origin.x * c.transform.scale.x;
			c.transform.velocity.x = 0f;
		}
		if(c.top() < area.y){
			c.transform.position.y = area.y + c.origin.y * c.transform.scale.y;
			c.transform.velocity.y = 0f;
		}
		if(c.bottom() > area.x + area.height){
			c.transform.position.y = (area.y + area.height) - c.origin.y * c.transform.scale.y;
			c.transform.velocity.y = 0f;
		}
	}

	@Override
	public void BeforeDraw(SpriteBatch spriteBatch, Entity owner) {

	}

	@Override
	public void AfterDraw(SpriteBatch spriteBatch, Entity owner) {

	}

}
