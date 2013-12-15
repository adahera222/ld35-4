package se.skoggy.ld28.characters;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import se.skoggy.animation.Animation;
import se.skoggy.entity.Entity;
import se.skoggy.entity.EntityBehavior;
import se.skoggy.ld28.GameSettings;

public class GameCharacter  extends Entity {

	protected HashMap<String, Animation> animations;
	protected String currentAnimation;
	protected boolean grounded;

	protected float moveSpeed = 0.0005f;
	protected float maxSpeed = 0.4f;

	protected int tileSize;
	protected int sheetDimension;

	public GameCharacter(TextureRegion texture, HashMap<String, Animation> animations, int tileSize, int sheetDimension) {
		super(texture);
		this.animations = animations;
		this.tileSize = tileSize;
		this.sheetDimension = sheetDimension;
		currentAnimation = "idle";
		grounded = false;
		update(1f);
	}

	public void multiplySpeed(float multiplier){
		moveSpeed *= multiplier;
		maxSpeed *= multiplier;
	}

	/**
	 * Warning, may be slow, please cache response
	 * @param type
	 * @return
	 */
	public EntityBehavior getBehavior(Class<?> type){
		for (int i = 0; i < behaviors.size(); i++) {
			if(behaviors.get(i).getClass() == type){
				return behaviors.get(i);
			}
		}
		return null;
	}

	public boolean collides(Entity other) {
		if(left() > other.transform.position.x + other.origin.x * other.transform.scale.x) return false;
		if(right() < other.transform.position.x - other.origin.x * other.transform.scale.x) return false;
		if(top() > other.transform.position.y + other.origin.y * other.transform.scale.y) return false;
		if(bottom() < other.transform.position.y - other.origin.y * other.transform.scale.y) return false;
		return true;
	}

	public float left(){
		return transform.position.x - origin.x * transform.scale.x;
	}
	public float right(){
		return transform.position.x + origin.x * transform.scale.x;
	}
	public float top(){
		return transform.position.y - (origin.y * 0.1f) * transform.scale.y;
	}
	public float bottom(){
		return transform.position.y + (origin.y * 0.1f) * transform.scale.y;
	}
	public float halfHeight(){
		return (origin.y * transform.scale.y);
	}
	public float halfWidth(){
		return (origin.x * transform.scale.x);
	}

	public boolean isFacingLeft(){
		return flipped();
	}

	public boolean isFacingRight(){
		return !flipped();
	}

	public boolean isMovingDown() {
		return (transform.velocity.y) > 0f;
	}
	public boolean isMovingUp() {
		return (transform.velocity.y) < 0f;
	}
	public boolean isMovingRight() {
		return (transform.velocity.x) > 0f;
	}
	public boolean isMovingLeft() {
		return (transform.velocity.x) < 0f;
	}

	public boolean flipped(){
		return textureRegion.isFlipX();
	}

	public void setAnim(String name){
		if(!currentAnimation.equals(name)){
			currentAnimation = name;
			animations.get(currentAnimation).reset();
		}
	}

	public void goLeft(float dt){
//		setFlip(true, true);
		transform.velocity.x -= moveSpeed * dt;
	}

	public void goRight(float dt){
	//	setFlip(false, true);
		transform.velocity.x += moveSpeed * dt;
	}
	public void goUp(float dt){
		transform.velocity.y -= moveSpeed * dt;
	}

	public void goDown(float dt){
		transform.velocity.y += moveSpeed * dt;
	}

	protected void clampSpeed(){
		if(transform.velocity.x > maxSpeed){
			transform.velocity.x = maxSpeed;
		}
		if(transform.velocity.x < -maxSpeed){
			transform.velocity.x = -maxSpeed;
		}

		if(transform.velocity.y > maxSpeed){
			transform.velocity.y = maxSpeed;
		}
		if(transform.velocity.y < -maxSpeed){
			transform.velocity.y = -maxSpeed;
		}

	}

	@Override
	public void update(float dt) {
		clampSpeed();

		transform.position.x += transform.velocity.x * dt;
		transform.position.y += transform.velocity.y * dt;

		super.update(dt);


		if(grounded){
		}else{
		}

		animations.get(currentAnimation).update(dt);
		int frame = animations.get(currentAnimation).getFrame();
		int col = (frame % sheetDimension);
		int row = (frame / sheetDimension);
		setSource(col * tileSize, row * tileSize, tileSize, tileSize);
	}
}
