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
		import com.badlogic.gdx.math.Intersector;
		import java.util.ArrayList;

public class MuscovyGame extends ApplicationAdapter implements ApplicationListener, InputProcessor {
	private OrthographicCamera camera;
	SpriteBatch batch;
	ScreenController room;
	Room drawRoom;
	Obstacle obstacle1, obstacle2;
	PlayerCharacter playerCharacter;
	Sprite roomSprite, testSprite1, testSprite2;
	GUI dungeonGUI, overworldGUI, pauseGUI;
	boolean keyflagW,keyflagD,keyflagA,keyflagS;
    private BitmapFont xVal, yVal;
	float w = 1280;
	float h = 720;
	int gameState; // 0 = Main Menu, 1 = Overworld/Map, 2 = Dungeon/Building, 3 = Pause

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
        Sprite guiMapSprite = new Sprite();
        Sprite guiSprite = new Sprite();
        gameState = 1;
		roomSprite = new Sprite();
		roomSprite.setTexture(new Texture("core/assets/testMap.png"));
		roomSprite.setX(32);
		roomSprite.setY(32);
		drawRoom = new Room();
		drawRoom.setSprite(roomSprite);
		playerCharacter = new PlayerCharacter();
        //test enemies
        testSprite1 = new Sprite();
        testSprite1.setTexture(new Texture("core/assets/thing.gif"));
        obstacle1 = new Obstacle(testSprite1);
        obstacle1.setX(350);
        obstacle1.setY(350);
		testSprite2 = new Sprite();
		testSprite2.setTexture(new Texture("core/assets/thing2.gif"));
		obstacle2 = new Obstacle(testSprite2);
		obstacle2.setX(500);
		obstacle2.setY(100);
        //Dungeon GUI
        dungeonGUI = new GUI();
        guiSprite.setTexture(new Texture("core/assets/guiFrame.png"));
        guiSprite.setX(0);
        guiSprite.setY(0);
        dungeonGUI.addElement(guiSprite);
        xVal = new BitmapFont();
        xVal.setColor(Color.BLACK);
        yVal = new BitmapFont();
        yVal.setColor(Color.BLACK);
        overworldGUI = new GUI();
        dungeonGUI.addData("PlayerXVal", "X Position: " + String.valueOf(playerCharacter.getX()), xVal, 400, 700);
        dungeonGUI.addData("EnemyXVal", "Y Position: " + String.valueOf(playerCharacter.getY()), yVal, 650, 700);
		dungeonGUI.addData("PlayerYVal", "X Position: " + String.valueOf(playerCharacter.getX()), xVal, 400, 650);
		dungeonGUI.addData("EnemyYVal", "Y Position: " + String.valueOf(playerCharacter.getY()), yVal, 650, 650);
		dungeonGUI.addData("Collision", "Collision?: False", yVal, 850, 700);
        guiMapSprite.setTexture(new Texture("core/assets/hesEastMap.png"));
        guiMapSprite.setX(0);
        guiMapSprite.setY(0);
        overworldGUI.addElement(guiMapSprite);
		room = new ScreenController(drawRoom);
		room.addNewObstacle(obstacle1);
		room.addNewObstacle(obstacle2);
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
				break;
            case 1:
				overworldGUI.render(batch);
				break;
            case 2:
				playerMovement();
				playerCollision();
                room.render(batch);
                dungeonGUI.editData("EnemyXVal","obstaclerectangleX: " + String.valueOf(obstacle1.getSprite().getHeight()));
				dungeonGUI.editData("EnemyYVal","obstaclerectangleY: " + String.valueOf(obstacle1.getSprite().getHeight()));
				dungeonGUI.editData("PlayerXVal","playerrectangleX: " + String.valueOf(playerCharacter.getBottomRectangle().getX()));
				dungeonGUI.editData("PlayerYVal","playerrectangleY: " + String.valueOf(playerCharacter.getBottomRectangle().getY()));
                dungeonGUI.render(batch);
                break;
            case 3:
                break;
		}
        batch.end();
	}
    public void playerCollision(){
		 ArrayList<Obstacle> obstacleList = new ArrayList<Obstacle>(room.getObstacles());
		 playerCharacter.resetMaxVelocity();
		 for (Collidable collidable:obstacleList) {
			if (Intersector.overlaps(playerCharacter.getBottomRectangle(),collidable.getTopRectangle())) {
				playerCharacter.setYVelocity(0);
				playerCharacter.setMaxVelocity(200);
				playerCharacter.setY(collidable.getTopRectangle().getY() + collidable.getTopRectangle().getHeight());
			}
			if (Intersector.overlaps(playerCharacter.getLeftRectangle(),collidable.getRightRectangle())){
				playerCharacter.setXVelocity(0);
				playerCharacter.setMaxVelocity(200);
				playerCharacter.setX(collidable.getRightRectangle().getX() + collidable.getRightRectangle().getWidth());
			}
			if (Intersector.overlaps(playerCharacter.getRightRectangle(),collidable.getLeftRectangle())){
				playerCharacter.setXVelocity(0);
				playerCharacter.setMaxVelocity(200);
				playerCharacter.setX(collidable.getLeftRectangle().getX() - playerCharacter.getSprite().getWidth());
			}
			if (Intersector.overlaps(playerCharacter.getTopRectangle(),collidable.getBottomRectangle())){
				playerCharacter.setYVelocity(0);
				playerCharacter.setMaxVelocity(200);
				playerCharacter.setY(collidable.getY() - (playerCharacter.getHeightOffset()+playerCharacter.getRectangleBorder()-2));//not sure why I need this 2 here. It's wonky.
			}
			 if (playerCharacter.collides(collidable.getSprite().getBoundingRectangle())) {
				 dungeonGUI.editData("Collision","collision?: True");
			 }else {
				 dungeonGUI.editData("Collision","collision?: False");
			 }

		}
	}
    
	public void playerMovement(){
		if(keyflagW) {playerCharacter.Up(); playerCharacter.movementAnimation();}
        if(!keyflagW && keyflagS) {playerCharacter.Down(); playerCharacter.movementAnimation();}
        if(keyflagD) {playerCharacter.Right(); playerCharacter.movementAnimation();}
		if(!keyflagD && keyflagA) {playerCharacter.Left(); playerCharacter.movementAnimation();}
		if(!keyflagD && !keyflagA) playerCharacter.decelXToStop();
		if(!keyflagW && !keyflagS) playerCharacter.decelYToStop();
		/**
		 * Animation stuff
		 */
		if (keyflagD) playerCharacter.setDirection(1);
		if (keyflagA) playerCharacter.setDirection(3);
		//if (keyflagA && keyflagD) playerCharacter.setDirection(2);
		if (keyflagW) playerCharacter.setDirection(0);
		if (keyflagS) playerCharacter.setDirection(2);
		playerCharacter.movement();
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (gameState){
			case 0:
				break;
			case 1:
                if(keycode == Input.Keys.ENTER) gameState = 2;
				break;
			case 2:
				if(keycode == Input.Keys.W) keyflagW = true;
				if(keycode == Input.Keys.S) keyflagS = true;
				if(keycode == Input.Keys.D) keyflagD = true;
				if(keycode == Input.Keys.A) keyflagA = true;
				break;
			case 3:
				break;
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (gameState){
			case 0:
				break;
			case 1:
				break;
			case 2:
				if(keycode == Input.Keys.W) keyflagW = false;
				if(keycode == Input.Keys.S) keyflagS = false;
				if(keycode == Input.Keys.D) keyflagD = false;
				if(keycode == Input.Keys.A) keyflagA = false;
				break;
			case 3:
				break;
		}
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
