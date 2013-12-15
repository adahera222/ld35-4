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
import se.skoggy.audio.AudioManager;
import se.skoggy.content.ContentManager;
import se.skoggy.entity.Entity;
import se.skoggy.entity.Transform;
import se.skoggy.ld28.GameSettings;
import se.skoggy.ld28.behaviors.CharacterBoundaryBehavior;
import se.skoggy.ld28.behaviors.CharacterKeyboardController;
import se.skoggy.ld28.behaviors.GoInOneDirectionBehavior;
import se.skoggy.ld28.behaviors.ParticleSpawnWhenFallingBehavior;
import se.skoggy.ld28.behaviors.RotationBehavior;
import se.skoggy.ld28.characters.FlightObject;
import se.skoggy.ld28.characters.PlayerCharacter;
import se.skoggy.ld28.particles.ParticleManager;
import se.skoggy.scenes.Scene;
import se.skoggy.utils.Helpers;
import se.skoggy.utils.Rand;
import se.skoggy.utils.TimerTrig;

public class GameScene extends Scene{

	private Matrix4 uiProjectionMatrix;

	PlayerCharacter player;
	ParticleManager particleManager;
	ContentManager content;

	List<FlightObject> flightObjects;
	Entity chute;
	Entity deployedChute;
	Entity cloud;
	Entity mountains;
	Entity ground;
	List<Transform> clouds;
	TimerTrig chuteAirTrig = new TimerTrig(80f);
	AudioManager audio;

	final static int STATE_FALLING = 0;
	final static int STATE_DROPPED_CHUTE = 1;
	final static int STATE_DEPLOYED = 2;
	final static int STATE_LANDED = 3;
	final static int STATE_CRASHED = 4;
	final static int STATE_GAME_OVER_LOST = 5;
	final static int STATE_GAME_OVER_WIN = 6;

	int state = STATE_FALLING;

	float time;
	final float totalTime = (1000f * 60f) * 1f;
	final int altitude = 3000;

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
		return 3000;
	}

	@Override
	public float transitionOutDuration() {
		return 6000;
	}

	@Override
	public boolean isPopup() {
		return false;
	}

	@Override
	public void load(ContentManager content) {
		this.content = content;

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
		animations.put("deployed", new Animation(new int[]{ 5 }, 400f));
		animations.put("crashed", new Animation(new int[]{ 7 }, 400f));
		player = new PlayerCharacter(content.loadTexture("gfx/player"), animations, 32, 4);
		player.transform.setScale(4f);
		player.addBehavior(new CharacterKeyboardController());
		player.addBehavior(new ParticleSpawnWhenFallingBehavior(particleManager));
		player.addBehavior(new CharacterBoundaryBehavior(new Rectangle(0,0, width, height)));
		player.teleport(width * 0.5f, height * 0.2f);

		flightObjects = new ArrayList<FlightObject>();

		chute = new Entity(content.loadTexture("gfx/parachute"));
		chute.transform.setScale(4f);
		deployedChute = new Entity(content.loadTexture("gfx/chute"));
		deployedChute.transform.setScale(4f);

		// BACKGROUND
		clouds = new ArrayList<Transform>();
		cloud = createCloud(content);
		mountains = new Entity(content.loadTexture("gfx/mountains_1"));
		mountains.teleport(width * 0.5f, 4000);
		ground = new Entity(content.loadTexture("gfx/ground"));
		ground.transform.setScale(6f);
		ground.teleport(width * 0.5f, 4000);

		for (int i = 0; i < 8; i++) {
			Transform t = new Transform();
			t.position.x = Rand.rand() * width;
			t.position.y = Rand.rand() * height;
			t.setScale(2f + Rand.rand() * 3);
			t.velocity.y = -(0.1f + Rand.rand()) * (t.scale.x * 0.01f);
			clouds.add(t);
		}

		time = 0;

		audio = new AudioManager();
		audio.registerSong("audio/song1.ogg");
		audio.load(content);
		audio.playSong("song1",  true);

		super.update(16f);

	}

	private FlightObject createBird(){
		HashMap<String, Animation> animations = new HashMap<String, Animation>();
		animations.put("idle", new Animation(new int[]{ 0 }, 500f));
		animations.put("walk", new Animation(new int[]{ 0, 1 }, 120f));
		boolean direction = Rand.rand() > 0.5f;
		FlightObject obj = new FlightObject(content.loadTexture("gfx/bird"), animations, 16, 2);
		obj.transform.setScale(2f);
		obj.addBehavior(new GoInOneDirectionBehavior(direction));
		obj.teleport(direction ? - 100 : width + 100, Rand.rand() * 720f);
		obj.setAnim("walk");
		obj.multiplySpeed(0.3f + Rand.rand() * 0.7f);
		if(!direction)
			obj.setFlip(true, true);
		return obj;
	}

	private FlightObject createPig(){
		HashMap<String, Animation> animations = new HashMap<String, Animation>();
		animations.put("idle", new Animation(new int[]{ 0 }, 500f));
		animations.put("walk", new Animation(new int[]{ 0 }, 120f));
		boolean direction = Rand.rand() > 0.5f;
		FlightObject obj = new FlightObject(content.loadTexture("gfx/pig"), animations, 32, 1);
		obj.transform.setScale(2f);
		obj.addBehavior(new GoInOneDirectionBehavior(direction));
		obj.addBehavior(new RotationBehavior(Rand.rand() * 0.01f));
		obj.teleport(direction ? - 100 : width + 100, Rand.rand() * 720f);
		obj.setAnim("walk");
		obj.multiplySpeed(0.3f + Rand.rand() * 0.7f);
		if(!direction)
			obj.setFlip(true, true);
		return obj;
	}

	private FlightObject createPlane(float y){
		HashMap<String, Animation> animations = new HashMap<String, Animation>();
		animations.put("idle", new Animation(new int[]{ 0 }, 500f));
		boolean direction = Rand.rand() > 0.5f;
		FlightObject obj = new FlightObject(content.loadTexture("gfx/plane"), animations, 64, 1);
		obj.transform.setScale(4f);
		obj.addBehavior(new GoInOneDirectionBehavior(direction));
		obj.teleport(direction ? - 200 : width + 200, y);
		obj.multiplySpeed(1f);
		if(!direction)
			obj.setFlip(true, true);
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
		boolean hasParachute = state == STATE_FALLING;
		state = hasParachute ? STATE_DEPLOYED : STATE_CRASHED;
	}

	boolean gameEnded;
	private void endGame(){
		gameEnded = true;
		manager.popScene();
		MenuScene menuScene = new MenuScene(width, height);
		manager.pushScene(menuScene);
		menuScene.load(new ContentManager(GameSettings.CONTENT_ROOT,true));
	}

	boolean step1 = false;
	boolean step2 = false;
	boolean step3 = false;
	boolean step4 = false;
	boolean step5 = false;
	boolean step6 = false;
	boolean step7 = false;
	boolean step8 = false;
	boolean step9 = false;
	boolean step10 = false;
	boolean step11 = false;
	boolean step12 = false;

	private void updateSpawning(float dt) {
		if(currentProgress() > 0.1f && !step1){
			step1 = true;
			for (int i = 0; i < 6; i++) {
				flightObjects.add(createBird());
			}
		}
		if(currentProgress() > 0.15f && !step2){
			step2 = true;
			flightObjects.add(createPlane(height * 0.4f));
		}
		if(currentProgress() > 0.2f && !step3){
			step3 = true;
			for (int i = 0; i < 6; i++) {
				flightObjects.add(createBird());
			}
		}
		if(currentProgress() > 0.25f && !step4){
			step4 = true;
			for (int i = 0; i < 3; i++) {
				flightObjects.add(createBird());
			}
		}
		if(currentProgress() > 0.35f && !step5){
			step5 = true;
			flightObjects.add(createPlane(height * 0.2f));
			flightObjects.add(createPlane(height * 0.7f));
		}
		if(currentProgress() > 0.4f && !step6){
			step6 = true;
			flightObjects.add(createPlane(height * 0.2f));
			flightObjects.add(createPlane(height * 0.3f));
			flightObjects.add(createPlane(height * 0.4f));
		}
		if(currentProgress() > 0.5f && !step7){
			step7 = true;
			for (int i = 0; i < 3; i++) {
				flightObjects.add(createBird());
			}
			flightObjects.add(createPlane(height * 0.7f));
			flightObjects.add(createPlane(height * 0.6f));
		}

		if(currentProgress() > 0.6f && !step8){
			step8 = true;
			flightObjects.add(createPig());
		}

		if(currentProgress() > 0.68f && !step9){
			step9 = true;
			flightObjects.add(createPig());
			flightObjects.add(createPig());
			flightObjects.add(createPig());
			flightObjects.add(createPlane(height * 0.8f));
			flightObjects.add(createBird());
		}

		if(currentProgress() > 0.75f && !step10){
			step10 = true;
			flightObjects.add(createBird());
			flightObjects.add(createBird());
			flightObjects.add(createBird());
			flightObjects.add(createBird());
			flightObjects.add(createPig());
			flightObjects.add(createPig());
		}

		if(currentProgress() > 0.85f && !step11){
			step11 = true;
			flightObjects.add(createPlane(height * 0.1f));
			flightObjects.add(createPlane(height * 0.15f));
			flightObjects.add(createPlane(height * 0.20f));
			flightObjects.add(createPlane(height * 0.25f));
			flightObjects.add(createPlane(height * 0.30f));
			flightObjects.add(createBird());
			flightObjects.add(createBird());
		}
		if(currentProgress() > 0.9f && !step12){
			step12 = true;
			flightObjects.add(createBird());
			flightObjects.add(createBird());
		}
	}

	@Override
	public void updateTransitionOut(float dt, float progress) {
		super.updateTransitionOut(dt, progress);
		audio.stopSong("song1");
	}

	@Override
	public void update(float dt) {

		if(!gameEnded){
			boolean stillFalling = (state == STATE_FALLING || state == STATE_DROPPED_CHUTE || state == STATE_DROPPED_CHUTE);

			if(stillFalling){
				player.update(dt);

				updateSpawning(dt);
				time += dt;
				if(time > totalTime){
					time = totalTime;
					gameOver();
				}
			}else{
				if(state != STATE_CRASHED && state != STATE_GAME_OVER_LOST && state != STATE_GAME_OVER_WIN){
					player.setAnim("deployed");
				}
				player.transform.position.y = Helpers.lerp(player.transform.position.y, 600, 0.1f);
				if(player.transform.position.y > 599){
					if(state == STATE_CRASHED){
						player.setAnim("crashed");
						state = STATE_GAME_OVER_LOST;
					}else{
						player.setAnim("deployed");
						state = STATE_GAME_OVER_WIN;
					}
					endGame();
				}
				player.updateWithoutBehaviors(dt);
			}

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


			if(stillFalling){
				for (int i = 0; i < clouds.size(); i++) {
					Transform c = clouds.get(i);
					c.position.y += c.velocity.y * dt;
					if(c.position.y < -100){
						c.position.y = height + 200;
					}
				}
			}

			particleManager.update(dt);

			mountains.transform.position.x = width * 0.5f;
			mountains.transform.position.y = Helpers.lerp(4000, 600 - mountains.origin.y * mountains.transform.scale.y, currentProgress());
			mountains.transform.setScale(10f);

			ground.transform.position.y =  Helpers.lerp(4000, 720 - ground.origin.y * ground.transform.scale.y, currentProgress());
			ground.transform.setScale(10f);
		}

		super.update(dt);
	}

	public float currentProgress(){
		return time / totalTime;
	}

	@Override
	public void draw(SpriteBatch sb) {
		sb.setProjectionMatrix(cam.combined);
		sb.begin();

		mountains.draw(sb);
		ground.draw(sb);

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
		if(state == STATE_DEPLOYED){
			deployedChute.teleport(player.transform.position.x, player.transform.position.y - deployedChute.origin.y * deployedChute.transform.scale.y);
			deployedChute.draw(sb);
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
		font.draw(sb, "Altitude: " + (int)(altitude - (currentProgress() * altitude)) + " m", 16, 32);

		blackOverlay.color.a = 1f - brightness;
		blackOverlay.draw(sb);

		if(state == STATE_GAME_OVER_LOST){
			font.draw(sb, "You only had one parachute, and you died...", width * 0.4f, height * 0.5f);
		}else if(state == STATE_GAME_OVER_WIN){
			font.draw(sb, "You only had one parachute, and you survived!", width * 0.35f, height * 0.5f);
		}


		sb.end();
	}

	@Override
	public void drawTransitionIn(SpriteBatch sb, float progress) {
		brightness = progress;
		draw(sb);
	}

	@Override
	public void drawTransitionOut(SpriteBatch sb, float progress) {
		brightness = 1f - progress;
		draw(sb);

	}
}
