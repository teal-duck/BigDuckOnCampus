package com.muscovy.game;

		import com.badlogic.gdx.Input;
		import com.badlogic.gdx.ApplicationListener;
		import com.badlogic.gdx.InputProcessor;
		import com.badlogic.gdx.ApplicationAdapter;
		import com.badlogic.gdx.Gdx;
		import com.badlogic.gdx.graphics.GL20;
		import com.badlogic.gdx.graphics.OrthographicCamera;
		import com.badlogic.gdx.graphics.Texture;
		import com.badlogic.gdx.graphics.g2d.Sprite;
		import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MuscovyGame extends ApplicationAdapter implements ApplicationListener, InputProcessor {
	private OrthographicCamera camera;
	SpriteBatch batch;
	Texture img;
	RoomRenderer room;
	Room drawRoom;
	Enemy enemy1, enemy2;
	PlayerCharacter playerCharacter;
	Sprite roomSprite, testSprite1, testSprite2;
	boolean keyflagW,keyflagD,keyflagA,keyflagS;
	float w = 1280;
	float h = 720;

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);
		roomSprite = new Sprite();
		roomSprite.setTexture(new Texture("core/assets/Untitled.jpg"));
		roomSprite.setX(0);
		roomSprite.setY(0);
		drawRoom = new Room();
		drawRoom.setSprite(roomSprite);
		playerCharacter = new PlayerCharacter();
		testSprite2 = new Sprite();
		testSprite2.setTexture(new Texture("core/assets/thing2.jpg"));
		enemy2 = new Enemy();
		enemy2.setSprite(testSprite2);
		enemy2.setX(100);
		enemy2.setY(100);
		testSprite1 = new Sprite();
		testSprite1.setTexture(new Texture("core/assets/thing.jpg"));
		enemy1 = new Enemy();
		enemy1.setSprite(testSprite2);
		enemy1.setX(150);
		enemy1.setY(150);
		room = new RoomRenderer(drawRoom);
		room.addNewDrawable(enemy1);
		room.addNewDrawable(enemy2);
		room.addNewDrawable(playerCharacter);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		playerMovement();
        batch.setProjectionMatrix(camera.combined);
		room.render(batch);
	}

	public void playerMovement(){
		if(keyflagW) playerCharacter.Up();
		if(keyflagA) playerCharacter.Right();
		if(keyflagD) playerCharacter.Left();
		if(keyflagS) playerCharacter.Down();
		if(!keyflagD && !keyflagA){
			playerCharacter.decelXToStop();
		}
		if(!keyflagW && !keyflagS){
			playerCharacter.decelYToStop();
		}
		playerCharacter.movement();
	}

	@Override
	public boolean keyDown(int keycode) {
		if(keycode == Input.Keys.W) keyflagW = true;
		if(keycode == Input.Keys.S) keyflagS = true;
		if(keycode == Input.Keys.D) keyflagD = true;
		if(keycode == Input.Keys.A) keyflagA = true;
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Input.Keys.W) keyflagW = false;
		if(keycode == Input.Keys.S) keyflagS = false;
		if(keycode == Input.Keys.D) keyflagD = false;
		if(keycode == Input.Keys.A) keyflagA = false;
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}


}
