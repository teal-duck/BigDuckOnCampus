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
	ScreenController controller;
	Room drawRoom;
	Obstacle obstacle1, obstacle2;
    Enemy enemy1;
	PlayerCharacter playerCharacter;
	Sprite roomSprite, testSprite1, testSprite2;
	GUI dungeonGUI, overworldGUI, pauseGUI, gameOverGUI;
	boolean keyflagW,keyflagD,keyflagA,keyflagS;
    private BitmapFont xVal, yVal, gameOverFont;
	float w = 1280;
	float h = 720;
	int gameState; // 0 = Main Menu, 1 = Overworld/Map, 2 = Dungeon/Building, 3 = Pause, 4 = Game Over

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
		playerCharacter.setY(300);
		playerCharacter.setX(0);
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
        obstacle2.setDamaging(true);
        obstacle2.setTouchDamage(10.0f);
        testSprite1 = new Sprite(new Texture("core/assets/testEnemy.png"));
        enemy1 = new Enemy(testSprite1);
        enemy1.setX(1024);
        enemy1.setY(300);
        enemy1.setAttackType(0);
		enemy1.setMovementType(1);
        enemy1.setTouchDamage(30.0f);
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
        dungeonGUI.addData("Invincible", "Invincibility counter: False", yVal, 850, 650);
        guiMapSprite.setTexture(new Texture("core/assets/hesEastMap.png"));
        guiMapSprite.setX(0);
        guiMapSprite.setY(0);
        overworldGUI.addElement(guiMapSprite);
        gameOverFont = new BitmapFont();
        gameOverFont.setColor(Color.RED);
        gameOverGUI = new GUI();
        gameOverGUI.addData("Gameover", "Game Over",gameOverFont,640,150);
		controller = new ScreenController(drawRoom);
		controller.addNewObstacle(obstacle1);
		controller.addNewObstacle(obstacle2);
        controller.addNewEnemy(enemy1);
		controller.addNewDrawable(playerCharacter);
	}
    public void update(){
        switch (gameState){
            case 0:
                break;
            case 1:
                break;
            case 2:
                playerMovement();
                playerCharacter.update();
                enemy1.update(playerCharacter);
				if (playerCharacter.getHealth() <= 0){this.gameState = 4;}
				collision();
                break;
            case 3:
                break;
        }
    }
	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.update();
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
                controller.render(batch);
                dungeonGUI.editData("EnemyXVal", "enemy X: " + String.valueOf(enemy1.getX()));
				dungeonGUI.editData("EnemyYVal", "enemy Y: " + String.valueOf(enemy1.getY()));
				dungeonGUI.editData("PlayerXVal","xdist player to enemy: " + String.valueOf(playerCharacter.getX() - enemy1.getX()));
				dungeonGUI.editData("PlayerYVal","ydist player to enemy: " + String.valueOf(playerCharacter.getY() - enemy1.getY()));
                dungeonGUI.editData("Collision","Player health: " + String.valueOf(playerCharacter.getHealth()));
                dungeonGUI.editData("Invincible","Angle to Player : " + String.valueOf(enemy1.direction));
                dungeonGUI.render(batch);
                break;
            case 3:
                break;
            case 4:
                gameOverGUI.render(batch);
                batch.draw(playerCharacter.getSprite().getTexture(),playerCharacter.getX(),playerCharacter.getY());
                break;
		}
        batch.end();
	}
    public void collision(){
		 ArrayList<Obstacle> obstacleList = new ArrayList<Obstacle>(controller.getObstacles());
         ArrayList<Enemy> enemyList = new ArrayList<Enemy>(controller.getEnemies());
		 playerCharacter.resetMaxVelocity();
		 for (Obstacle collidable:obstacleList) {
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
            if (playerCharacter.collides(collidable.getBoundingBox())) {
                 if (collidable.isDamaging()){
                     playerCharacter.damage(collidable.getTouchDamage());
                 }
            }
		}
        for (Enemy enemy:enemyList) {
			enemy.resetMaxVelocity();
            if (Intersector.overlaps(playerCharacter.getBottomRectangle(),enemy.getTopRectangle())) {
                playerCharacter.setYVelocity(0);
                playerCharacter.setMaxVelocity(200);
                playerCharacter.setY(enemy.getTopRectangle().getY() + enemy.getTopRectangle().getHeight());
            }
            if (Intersector.overlaps(playerCharacter.getLeftRectangle(),enemy.getRightRectangle())){
                playerCharacter.setXVelocity(0);
                playerCharacter.setMaxVelocity(200);
                playerCharacter.setX(enemy.getRightRectangle().getX() + enemy.getRightRectangle().getWidth());
            }
            if (Intersector.overlaps(playerCharacter.getRightRectangle(),enemy.getLeftRectangle())){
                playerCharacter.setXVelocity(0);
                playerCharacter.setMaxVelocity(200);
                playerCharacter.setX(enemy.getLeftRectangle().getX() - playerCharacter.getSprite().getWidth());
            }
            if (Intersector.overlaps(playerCharacter.getTopRectangle(),enemy.getBottomRectangle())){
                playerCharacter.setYVelocity(0);
                playerCharacter.setMaxVelocity(200);
                playerCharacter.setY(enemy.getY() - (playerCharacter.getHeightOffset()+playerCharacter.getRectangleBorder()-2));//not sure why I need this 2 here. It's wonky.
            }
            if (playerCharacter.collides(enemy.getBoundingBox())) {
                if (enemy.getAttackType() == 0){
                    playerCharacter.damage(enemy.getTouchDamage());
                }
            }
			for (Obstacle obstacle:obstacleList){
				if (Intersector.overlaps(enemy.getBottomRectangle(), obstacle.getTopRectangle())) {
					enemy.setYVelocity(0);
					enemy.setCurrentMaxVelocity(50);
					enemy.setY(obstacle.getTopRectangle().getY() + obstacle.getTopRectangle().getHeight());
				}
				if (Intersector.overlaps(enemy.getLeftRectangle(),obstacle.getRightRectangle())){
					enemy.setXVelocity(0);
					enemy.setCurrentMaxVelocity(200);
					enemy.setX(obstacle.getRightRectangle().getX() + obstacle.getRightRectangle().getWidth());
				}
				if (Intersector.overlaps(enemy.getRightRectangle(), obstacle.getLeftRectangle())){
					enemy.setXVelocity(0);
					enemy.setCurrentMaxVelocity(200);
					enemy.setX(obstacle.getLeftRectangle().getX() - enemy.getSprite().getWidth());
				}
				if (Intersector.overlaps(enemy.getTopRectangle(),obstacle.getBottomRectangle())){
					enemy.setYVelocity(0);
					enemy.setCurrentMaxVelocity(200);
					enemy.setY(enemy.getY() - (enemy.getHeightOffset()+enemy.getRectangleBorder()-2));//not sure why I need this 2 here. It's wonky.
				}
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
