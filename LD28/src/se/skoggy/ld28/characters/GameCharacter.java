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

	protected float moveSpeed = 0.0009f;
	protected float friction = 0.0004f;
	protected float gravity = GameSettings.GRAVITY;
	protected float maxSpeed = 0.1f;
	protected float jumpForce = 0.3f;

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

	public void setMovement(float multiplier){
		moveSpeed *= multiplier;
		friction *= multiplier;
		maxSpeed *= multiplier;
	}

	public void setJumpForce(float jumpForce) {
		this.jumpForce = jumpForce;
	}

	public float left(){
		return transform.position.x - origin.x * transform.scale.x;
	}
	public float right(){
		return transform.position.x + origin.x * transform.scale.x;
	}
	public float top(){
		return transform.position.y - origin.y * transform.scale.y;
	}
	public float bottom(){
		return transform.position.y + origin.y * transform.scale.y;
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

	public boolean isGrounded() {
		return grounded;
	}

	public void walkLeft(float dt){
		if(grounded)
			setAnim("walk");
		setFlip(true, true);
		transform.velocity.x -= moveSpeed * dt;
	}

	public void walkRight(float dt){
		if(grounded)
			setAnim("walk");
		setFlip(false, true);
		transform.velocity.x += moveSpeed * dt;
	}

	public void jump(){
		setAnim("jump");
		grounded = false;
		transform.velocity.y = -jumpForce;
	}

	public void land(){
		setAnim("idle");
		grounded = true;
		transform.velocity.y = 0f;
	}

	public void fallOff(){
		setAnim("jump");
		grounded = false;
		transform.velocity.y = 0f;
	}

	protected void updateFriction(float dt){
		if(transform.velocity.x < 0f){
			transform.velocity.x += friction * dt;
			if(transform.velocity.x > 0f){
				transform.velocity.x = 0f;
			}
		}
		if(transform.velocity.x > 0f){
			transform.velocity.x -= friction * dt;
			if(transform.velocity.x < 0f){
				transform.velocity.x = 0f;
			}
		}
	}

	protected void clampSpeed(){
		if(transform.velocity.x > maxSpeed){
			transform.velocity.x = maxSpeed;
		}
		if(transform.velocity.x < -maxSpeed){
			transform.velocity.x = -maxSpeed;
		}

	}

	@Override
	public void update(float dt) {
		updateFriction(dt);
		clampSpeed();

		transform.position.x += transform.velocity.x * dt;
		transform.position.y += transform.velocity.y * dt;

		super.update(dt);


		if(grounded){
		}else{
			transform.velocity.y += gravity * dt;
		}

		animations.get(currentAnimation).update(dt);
		int frame = animations.get(currentAnimation).getFrame();
		int col = (frame % sheetDimension);
		int row = (frame / sheetDimension);
		setSource(col * tileSize, row * tileSize, tileSize, tileSize);
	}
}
