package com.muscovy.game;

		import com.badlogic.gdx.Input;
		import com.badlogic.gdx.ApplicationListener;
		import com.badlogic.gdx.InputProcessor;
		import com.badlogic.gdx.ApplicationAdapter;
		import com.badlogic.gdx.Gdx;
        import com.badlogic.gdx.graphics.Color;
        import com.badlogic.gdx.graphics.GL20;
		import com.badlogic.gdx.graphics.OrthographicCamera;
		import com.badlogic.gdx.graphics.Texture;
		import com.badlogic.gdx.graphics.g2d.Sprite;
		import com.badlogic.gdx.graphics.g2d.SpriteBatch;
        import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class MuscovyGame extends ApplicationAdapter implements ApplicationListener, InputProcessor {
	private OrthographicCamera camera;
	SpriteBatch batch;
	RoomRenderer room;
	Room drawRoom;
	Enemy enemy1, enemy2;
	PlayerCharacter playerCharacter;
	Sprite roomSprite, testSprite1, testSprite2;
	GUI dungeonGUI, overworldGUI, pauseGUI;
	boolean keyflagW,keyflagD,keyflagA,keyflagS;
    private BitmapFont xVal, yVal;
	float w = 1280;
	float h = 720;
	int gameState; // 0 = Main Menu, 1 = Overworld, 2 = Dungeon, 3 = Pause

	@Override
	public void create () {
		gameState = 0;
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);
        createTestingSprites();
        Gdx.input.setInputProcessor(this);
	}
	private void createTestingSprites(){
        gameState = 2;
		roomSprite = new Sprite();
		roomSprite.setTexture(new Texture("core/assets/Untitled.jpg"));
		roomSprite.setX(0);
		roomSprite.setY(0);
		drawRoom = new Room();
		drawRoom.setSprite(roomSprite);
		playerCharacter = new PlayerCharacter();
        testSprite1 = new Sprite();
        testSprite1.setTexture(new Texture("core/assets/thing.gif"));
        enemy1 = new Enemy();
        enemy1.setSprite(testSprite1);
        enemy1.setX(150);
        enemy1.setY(150);
		testSprite2 = new Sprite();
		testSprite2.setTexture(new Texture("core/assets/thing2.gif"));
		enemy2 = new Enemy();
		enemy2.setSprite(testSprite2);
		enemy2.setX(100);
		enemy2.setY(100);
        dungeonGUI = new GUI();
        Sprite guiSprite = new Sprite();
        guiSprite.setTexture(new Texture("core/assets/guiFrame.png"));
        guiSprite.setX(0);
        guiSprite.setY(0);
        dungeonGUI.addElement(guiSprite);
        xVal = new BitmapFont();
        xVal.setColor(Color.BLACK);
        yVal = new BitmapFont();
        yVal.setColor(Color.BLACK);
        dungeonGUI.addData("xVal", "X Position: " + String.valueOf(playerCharacter.getX()), xVal, 500, 700);
        dungeonGUI.addData("yVal", "Y Position: " + String.valueOf(playerCharacter.getY()), yVal, 650, 700);
		room = new RoomRenderer(drawRoom);
		room.addNewDrawable(enemy1);
		room.addNewDrawable(enemy2);
		room.addNewDrawable(playerCharacter);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		switch (gameState){
			case 0:
				//thing;
				break;
            case 1:
				break;
            case 2:
                room.render(batch);
                dungeonGUI.editData("xVal","X Position: " + String.valueOf(playerCharacter.getX()));
                dungeonGUI.editData("yVal","Y Position: " + String.valueOf(playerCharacter.getY()));
                dungeonGUI.render(batch);
                playerMovement();
                break;
            case 3:
                break;
		}
        batch.end();
	}

	public void playerMovement(){
		if(keyflagW) playerCharacter.Up();
        if(!keyflagW && keyflagS) playerCharacter.Down();
        if(keyflagD) playerCharacter.Left();
		if(!keyflagD && keyflagA) playerCharacter.Right();
		if(!keyflagD && !keyflagA) playerCharacter.decelXToStop();
		if(!keyflagW && !keyflagS) playerCharacter.decelYToStop();
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
