package se.skoggy.ld28.scenes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import se.skoggy.animation.Animation;
import se.skoggy.content.ContentManager;
import se.skoggy.entity.Entity;
import se.skoggy.entity.Transform;
import se.skoggy.ld28.GameSettings;
import se.skoggy.ld28.behaviors.CharacterBoundaryBehavior;
import se.skoggy.ld28.behaviors.CharacterKeyboardController;
import se.skoggy.ld28.behaviors.GoInOneDirectionBehavior;
import se.skoggy.ld28.behaviors.ParticleSpawnWhenFallingBehavior;
import se.skoggy.ld28.characters.FlightObject;
import se.skoggy.ld28.characters.PlayerCharacter;
import se.skoggy.ld28.particles.ParticleManager;
import se.skoggy.scenes.Scene;
import se.skoggy.utils.Rand;
import se.skoggy.utils.TimerTrig;

public class GameScene extends Scene{

	private Matrix4 uiProjectionMatrix;

	PlayerCharacter player;
	ParticleManager particleManager;

	List<FlightObject> flightObjects;
	Entity chute;
	Entity cloud;
	List<Transform> clouds;
	TimerTrig chuteAirTrig = new TimerTrig(80f);

	final static int STATE_FALLING = 0;
	final static int STATE_DROPPED_CHUTE = 1;
	final static int STATE_DEPLOYED = 2;

	int state = STATE_FALLING;

	TimerTrig[] sequences = new TimerTrig[]{
				new TimerTrig(20000f),
				new TimerTrig(20000f),
				new TimerTrig(20000f),
				new TimerTrig(20000f)
			};
	int currentSequence = 0;

	// prettiness
	Entity blackOverlay;
	float brightness = 1f;
	BitmapFont font;

	public GameScene(float width, float height) {
		super(width, height);
	}

	@Override
	protected void initCam() {
		createCam(null /* new Rectangle(0, 0, (int)width, (int)height) */);
		uiProjectionMatrix = new Matrix4(cam.combined);
		cam.zoom = 1f;//GameSettings.ZOOM;
	}

	@Override
	public float transitionInDuration() {
		return 200;
	}

	@Override
	public float transitionOutDuration() {
		return 3000;
	}

	@Override
	public boolean isPopup() {
		return false;
	}

	@Override
	public void load(ContentManager content) {

		particleManager = new ParticleManager();
		particleManager.load(content);


		blackOverlay = new Entity(content.loadTexture("gfx/black"));
		blackOverlay.teleport(width * 0.5f, height * 0.5f);

		//ui
		font = content.loadFont("monaco_32");

		// TODO: move to factory
		HashMap<String, Animation> animations = new HashMap<String, Animation>();
		animations.put("idle", new Animation(new int[]{ 0 }, 500f));
		animations.put("walk", new Animation(new int[]{ 0, 1, 4 }, 40f));
		animations.put("walk_no_chute", new Animation(new int[]{ 2, 3, 6 }, 40f));
		player = new PlayerCharacter(content.loadTexture("gfx/player"), animations, 32, 4);
		player.transform.setScale(4f);
		player.addBehavior(new CharacterKeyboardController());
		player.addBehavior(new ParticleSpawnWhenFallingBehavior(particleManager));
		player.addBehavior(new CharacterBoundaryBehavior(new Rectangle(0,0, width, height)));
		player.teleport(width * 0.5f, height * 0.2f);

		flightObjects = new ArrayList<FlightObject>();

		flightObjects.add(createBird(content));
		flightObjects.add(createBird(content));
		flightObjects.add(createBird(content));
		flightObjects.add(createBird(content));
		flightObjects.add(createBird(content));

		chute = new Entity(content.loadTexture("gfx/parachute"));
		chute.transform.setScale(4f);

		clouds = new ArrayList<Transform>();
		cloud = createCloud(content);

		for (int i = 0; i < 8; i++) {
			Transform t = new Transform();
			t.position.x = Rand.rand() * width;
			t.position.y = Rand.rand() * height;
			t.setScale(2f + Rand.rand() * 3);
			t.velocity.y = -(0.1f + Rand.rand()) * (t.scale.x * 0.01f);
			clouds.add(t);
		}

		super.update(16f);

	}

	private FlightObject createBird(ContentManager content){
		HashMap<String, Animation> animations = new HashMap<String, Animation>();
		animations.put("idle", new Animation(new int[]{ 0 }, 500f));
		animations.put("walk", new Animation(new int[]{ 0, 1 }, 120f));
		boolean direction = Rand.rand() > 0f;
		FlightObject obj = new FlightObject(content.loadTexture("gfx/bird"), animations, 16, 2);
		obj.transform.setScale(2f);
		obj.addBehavior(new GoInOneDirectionBehavior(direction));
		obj.teleport(direction ? - 100 : width + 100, Rand.rand() * 720f);
		obj.setAnim("walk");
		obj.multiplySpeed(0.3f + Rand.rand() * 0.7f);
		return obj;
	}

	private Entity createCloud(ContentManager content){
		Entity cloud = new Entity(content.loadTexture("gfx/cloud"));
		return cloud;
	}

	private void parachuteLost(){
		player.setAnim("walk_no_chute");
		chute.teleport(player.transform.position.x, player.transform.position.y);
		chute.transform.velocity.x = (-0.5f + Rand.rand()) * 0.1f;
		chute.transform.velocity.y = (-0.5f + Rand.rand()) * 0.1f;
		state = STATE_DROPPED_CHUTE;
	}

	private void gameOver(){
		// TODO: check if dead or not
		// manager.popScene();
	}

	private void nextSequenceTriggered(){

	}

	@Override
	public void update(float dt) {

		if(currentSequence > sequences.length - 1){
			gameOver();
		}else{
			if(sequences[currentSequence].isTrigged(dt)){
				currentSequence++;
				nextSequenceTriggered();
			}
		}

		player.update(dt);

		for (int i = 0; i < flightObjects.size(); i++) {
			flightObjects.get(i).update(dt);

			if(state == STATE_FALLING){
				if(flightObjects.get(i).collides(player)){
					parachuteLost();
				}
			}
		}


		if(state == STATE_DROPPED_CHUTE){
			chute.transform.rotation += 0.001f * dt;
			chute.transform.position.x += chute.transform.velocity.x * dt;
			chute.transform.position.y += chute.transform.velocity.y * dt;
			chute.update(dt);
			if(chuteAirTrig.isTrigged(dt)){
				particleManager.spawnAir(chute.transform.position.x,  chute.transform.position.y, 8f * 4f);
			}
		}


		for (int i = 0; i < clouds.size(); i++) {
			Transform c = clouds.get(i);
			c.position.y += c.velocity.y * dt;
			if(c.position.y < -100){
				c.position.y = height + 200;
			}
		}

		particleManager.update(dt);

		if(isDead()){
			gameOver();
		}

		super.update(dt);
	}

	public float currentSequenceProgress(){
		return sequences[currentSequence].progress();
	}

	private boolean isDead(){
		return false;
	}

	@Override
	public void draw(SpriteBatch sb) {
		sb.setProjectionMatrix(cam.combined);
		sb.begin();
		for (int i = 0; i < clouds.size(); i++) {
			Transform c = clouds.get(i);
			cloud.transform = c;
			cloud.draw(sb);
		}
		particleManager.draw(sb);
		for (int i = 0; i < flightObjects.size(); i++) {
			flightObjects.get(i).draw(sb);
			if(flightObjects.get(i).collides(player)){
				player.color.r = 1f;
				player.color.g = 0f;
				player.color.b = 0f;
				player.color.r = 1f;
			}
		}
		player.draw(sb);

		player.color.r = 1f;
		player.color.g = 1f;
		player.color.b = 1f;
		player.color.r = 1f;

		if(state == STATE_DROPPED_CHUTE){
			chute.draw(sb);
		}
		sb.end();

		sb.setProjectionMatrix(uiProjectionMatrix);
		sb.begin();
		font.draw(sb, "Sequence: " + currentSequence + " : " + (int)(currentSequenceProgress() * 100) + "%", 16, 16);
		blackOverlay.color.a = 1f - brightness;
		blackOverlay.draw(sb);
		sb.end();
	}

	@Override
	public void drawTransitionIn(SpriteBatch sb, float progress) {
		brightness = progress;
		draw(sb);
	}

	@Override
	public void drawTransitionOut(SpriteBatch sb, float progress) {
		brightness = 0f;

	}
}
