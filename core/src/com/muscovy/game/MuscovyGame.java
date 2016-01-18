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
    float timer = 0;
	SpriteBatch batch;
	DungeonRoom drawDungeonRoom;
	Obstacle obstacle1, obstacle2;
    Enemy enemy1, enemy2;
	PlayerCharacter playerCharacter;
	GUI mainMenuGUI, dungeonGUI, overworldGUI, pauseGUI, gameOverGUI;
    EntityManager entityManager;
	boolean keyflagW,keyflagD,keyflagA,keyflagS, keyflagUP, keyflagRIGHT, keyflagLEFT, keyflagDOWN, firing = false;
	Sprite roomSprite, testSprite1, testSprite2, guiMapSprite, guiSelector;
    private BitmapFont xVal, yVal, gameOverFont;
	float w = 1280;
	float h = 960;
	int gameState; // 0 = Main Menu, 1 = Overworld/Map, 2 = Dungeon/LevelGenerator, 3 = Pause, 4 = Game Over
	int MapSelected; // 0 = Constantine

	public void cursorLocation(){
		switch (MapSelected){
			case 1:
				guiSelector.setX(950);
				guiSelector.setY(680);
				break;
			case 2:
				guiSelector.setX(650);
				guiSelector.setY(600);
				break;
			case 3:
				guiSelector.setX(300);
				guiSelector.setY(600);
				break;
			case 4:
				guiSelector.setX(230);
				guiSelector.setY(420);
				break;
			case 5:
				guiSelector.setX(110);
				guiSelector.setY(360);
				break;
			case 6:
				guiSelector.setX(160);
				guiSelector.setY(230);
				break;
			case 7:
				guiSelector.setX(330);
				guiSelector.setY(270);
				break;
			case 8:
				guiSelector.setX(440);
				guiSelector.setY(340);
				break;
		}
	}

	public void initaliseOverworld(){
		MapSelected = 1;
		guiMapSprite = new Sprite();
		guiSelector = new Sprite();
		overworldGUI = new GUI();
		guiSelector.setTexture(new Texture("core/assets/selector.png"));
		cursorLocation();
		guiMapSprite.setTexture(new Texture("core/assets/hesEastMap.png"));
		guiMapSprite.setX(0);
		guiMapSprite.setY(0);
		overworldGUI.addElement(guiMapSprite);
		overworldGUI.addElement(guiSelector);
	}

	@Override
	public void create() {
		gameState = 0;
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);
        initialisePlayerCharacter();
        initialiseLevels();
        initialiseGUIs();
        //initialiseTesting();
        Gdx.input.setInputProcessor(this);
		initaliseOverworld();
    }
    private void initialiseLevels(){
        entityManager = new EntityManager();
        entityManager.generateLevels();
    }
    private void initialiseGUIs(){
        Sprite mainMenuSprite = new Sprite();
        Sprite guiMapSprite = new Sprite();
        Sprite guiDungeonSprite = new Sprite();
        //Main menu
        mainMenuGUI = new GUI();
        mainMenuSprite.setTexture(new Texture("core/assets/mainMenu.png"));
        mainMenuSprite.setX(0);
        mainMenuSprite.setY(0);
        mainMenuGUI.addElement(mainMenuSprite);
        //Overworld
        overworldGUI = new GUI();
        guiMapSprite.setTexture(new Texture("core/assets/hesEastMap.png"));
        guiMapSprite.setX(0);
        guiMapSprite.setY(0);
        overworldGUI.addElement(guiMapSprite);
        //Dungeon
        dungeonGUI = new GUI();
        guiDungeonSprite.setTexture(new Texture("core/assets/guiFrame.png"));
        guiDungeonSprite.setX(0);
        guiDungeonSprite.setY(0);
        dungeonGUI.addElement(guiDungeonSprite);
        xVal = new BitmapFont();
        xVal.setColor(Color.BLACK);
        yVal = new BitmapFont();
        yVal.setColor(Color.BLACK);
        dungeonGUI.addData("PlayerHealth", "Health: " + String.valueOf(playerCharacter.getHealth()), xVal, 400, 900);
        dungeonGUI.addData("EnemyXVal", "Y Position: " + String.valueOf(playerCharacter.getY()), yVal, 650, 900);
        dungeonGUI.addData("PlayerYVal", "X Position: " + String.valueOf(playerCharacter.getX()), xVal, 400, 650);
        dungeonGUI.addData("EnemyYVal", "Y Position: " + String.valueOf(playerCharacter.getY()), yVal, 650, 800);
        dungeonGUI.addData("Collision", "Collision?: False", yVal, 850, 900);
        dungeonGUI.addData("Invincible", "Time since last attack: " + String.valueOf(playerCharacter.getTimeSinceLastAttack()), yVal, 850, 650);
        //GameOver
        gameOverFont = new BitmapFont();
        gameOverFont.setColor(Color.RED);
        gameOverGUI = new GUI();
        gameOverGUI.addData("Gameover", "Game Over",gameOverFont,640,150);
        pauseGUI = new GUI();
        pauseGUI.addData("Pause", "PAUSE",gameOverFont,1280/2,720/2);
    }
    private void initialisePlayerCharacter(){
        playerCharacter = new PlayerCharacter();
        playerCharacter.setY(300);
        playerCharacter.setX(300);
    }
	private void initialiseTesting(){
        gameState = 0;
		entityManager = new EntityManager();
		entityManager.addNewDrawable(playerCharacter);
	}
    public void update(){
        switch (gameState){
            case 0:
                break;
            case 1:
                break;
            case 2:
                playerUpdate();
                playerCharacter.update();
                projectilesUpdate();
                enemiesUpdate();
				if (playerCharacter.getHealth() <= 0){this.gameState = 4;}
                collision();
                cleanupDeadThings();
                break;
            case 3:
                break;
            case 4:
                break;
            }
        if (timer > 10){
            timer = 0; //Useful for debugging. Put a break point here if you need to see the variables after 10 seconds
        }
        timer += Gdx.graphics.getDeltaTime();
    }
    public void enemiesUpdate(){
        for (Enemy enemy: entityManager.getEnemies()){
            enemy.update(playerCharacter);
			if ((enemy.getAttackType() != 0)&&(enemy.checkRangedAttack())) {
                entityManager.addNewProjectiles(enemy.rangedAttack(playerCharacter));
			}
            playerEnemyCollision(enemy);
            enemyWallCollision(enemy);
            playerEnemyCollision(enemy);
            enemyWallCollision(enemy);
        }
    }
    public void projectilesUpdate(){
        ArrayList<Projectile> projectileList = new ArrayList<Projectile>(entityManager.getProjectiles());
        for (Projectile projectile:projectileList){
            projectile.update();
            projectileWallCollision(projectile);
            projectilePlayerCollision(projectile);
        }
    }
    public void cleanupDeadThings(){
        entityManager.killEnemies();
        entityManager.killProjectiles();
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
                mainMenuGUI.render(batch);
				break;
            case 1:
				overworldGUI.render(batch);
				break;
            case 2:
                entityManager.render(batch);
                dungeonGUI.render(batch);
                entityManager.render(batch);
                break;
            case 3:
                dungeonGUI.render(batch);
                entityManager.render(batch);
                batch.draw(playerCharacter.getSprite().getTexture(),playerCharacter.getX(),playerCharacter.getY());
                pauseGUI.render(batch);
                break;
            case 4:
                gameOverGUI.render(batch);
                batch.draw(playerCharacter.getSprite().getTexture(),playerCharacter.getX(),playerCharacter.getY());
                break;
		}
        batch.end();
	}
    public void collision(){
        ArrayList<Obstacle> obstacleList = new ArrayList<Obstacle>(entityManager.getObstacles());
        ArrayList<Enemy> enemyList = new ArrayList<Enemy>(entityManager.getEnemies());
        ArrayList<Projectile> projectileList = new ArrayList<Projectile>(entityManager.getProjectiles());
        boolean firstLoop = true;
        playerCharacter.resetMaxVelocity();
        for (Obstacle obstacle:obstacleList) {
            playerObstacleCollision(obstacle);
            for (Enemy enemy:enemyList) {
                for (Projectile projectile:projectileList){
                    if (firstLoop){
                    projectileObstacleCollision(projectile, obstacle);
                    }
                    projectileEnemyCollision(projectile,enemy);
                }
                enemy.resetMaxVelocity();
                enemy.setCollidingWithSomething(false);
                enemyObstacleCollision(enemy,obstacle);
                firstLoop = false;
            }
        }
        playerWallCollision();
        if (entityManager.getCurrentDungeonRoom().isEnemiesDead()) {
            playerDoorCollision();
        }
	}
    public void projectileEnemyCollision(Projectile projectile, Enemy enemy){
        if ((Intersector.overlaps(enemy.getCircleHitbox(),projectile.getCollisionBox()))&& !(projectile.getDamagesWho() == 0)){
            projectile.kill();
            enemy.damage(projectile.getDamage());
        }
    }
    public void projectileObstacleCollision(Projectile projectile, Obstacle obstacle){
        if (Intersector.overlaps(obstacle.getCircleHitbox(),projectile.getCollisionBox())){
            projectile.kill();
        }
    }
    public void projectilePlayerCollision(Projectile projectile){
        if ((Intersector.overlaps(playerCharacter.getCircleHitbox(),projectile.getCollisionBox()))&& !(projectile.getDamagesWho() == 1)){
            projectile.kill();
            playerCharacter.damage(projectile.getDamage());
        }
    }
    public void projectileWallCollision(Projectile projectile){
        if (Intersector.overlaps(projectile.getCollisionBox(), entityManager.getCurrentDungeonRoom().getProjectileWallBottom())){
            projectile.kill();
        }
        if (Intersector.overlaps(projectile.getCollisionBox(), entityManager.getCurrentDungeonRoom().getProjectileWallTop())){
            projectile.kill();
        }
        if (Intersector.overlaps(projectile.getCollisionBox(), entityManager.getCurrentDungeonRoom().getProjectileWallLeft())){
            projectile.kill();
        }
        if (Intersector.overlaps(projectile.getCollisionBox(), entityManager.getCurrentDungeonRoom().getProjectileWallRight())){
            projectile.kill();
        }
    }
    public void playerObstacleCollision(Obstacle obstacle){
        if (Intersector.overlaps(playerCharacter.getCircleHitbox(), obstacle.getRectangleHitbox())) {
            playerCharacter.moveToNearestEdgeRectangle(obstacle);
            playerCharacter.setMaxVelocity(100);
            dungeonGUI.editData("Collides", "Collided");
            if (obstacle.isDamaging()){
                playerCharacter.damage(obstacle.getTouchDamage());
            }
        }
    }
    public void enemyObstacleCollision(Enemy enemy, Obstacle obstacle){
        if (enemy.collides(obstacle)){
            enemy.moveToNearestEdgeRectangle(obstacle);
            if (obstacle.isDamaging()){
                enemy.damage(obstacle.getTouchDamage());
            }
        }
    }
    public void enemyWallCollision(Enemy enemy){
        if (Intersector.overlaps(entityManager.getCurrentDungeonRoom().getTopRectangle(),enemy.getTopRectangle())) {
            enemy.setYVelocity(0);
            enemy.setMaxVelocity(50);
            enemy.setY(entityManager.getCurrentDungeonRoom().getTopRectangle().getY() - enemy.getCircleHitbox().radius);
            enemy.setCollidingWithSomething(true);
        }
        if (Intersector.overlaps(entityManager.getCurrentDungeonRoom().getRightRectangle(),enemy.getRightRectangle())){
            enemy.setXVelocity(0);
            enemy.setMaxVelocity(50);
            enemy.setX(entityManager.getCurrentDungeonRoom().getRightRectangle().getX()-enemy.getWidth());
            enemy.setCollidingWithSomething(true);
        }
        if (Intersector.overlaps(entityManager.getCurrentDungeonRoom().getLeftRectangle(),enemy.getLeftRectangle())){
            enemy.setXVelocity(0);
            enemy.setMaxVelocity(50);
            enemy.setX(entityManager.getCurrentDungeonRoom().getX()+64);
            enemy.setCollidingWithSomething(true);
        }
        if (Intersector.overlaps(entityManager.getCurrentDungeonRoom().getBottomRectangle(), enemy.getBottomRectangle())){
            enemy.setYVelocity(0);
            enemy.setMaxVelocity(50);
            enemy.setY(64);
            enemy.setCollidingWithSomething(true);
        }
    }
    public void playerEnemyCollision(Enemy enemy){
        if (playerCharacter.collides(enemy)) { //Doesn't damage twice because of invincibility frames
            playerCharacter.setMaxVelocity(100);
            if (enemy.isCollidingWithSomething()){
                playerCharacter.moveToNearestEdgeCircle(enemy);
            }else{
                enemy.moveToNearestEdgeCircle(playerCharacter);
            }
            if (enemy.getAttackType() != 1){
                playerCharacter.damage(enemy.getTouchDamage());
            }
        }
    }
    public void playerWallCollision(){
        if (Intersector.overlaps(playerCharacter.getCircleHitbox(), entityManager.getCurrentDungeonRoom().getTopRectangle())) {
            playerCharacter.setYVelocity(0);
            playerCharacter.setMaxVelocity(200);
            playerCharacter.setHitboxCentre(playerCharacter.getCircleHitbox().x, entityManager.getCurrentDungeonRoom().getTopRectangle().getY() - playerCharacter.getCircleHitbox().radius);
        }
        if (Intersector.overlaps(playerCharacter.getCircleHitbox(), entityManager.getCurrentDungeonRoom().getRightRectangle())){
            playerCharacter.setXVelocity(0);
            playerCharacter.setMaxVelocity(200);
            playerCharacter.setHitboxCentre(entityManager.getCurrentDungeonRoom().getRightRectangle().getX() - playerCharacter.getCircleHitbox().radius, playerCharacter.getCircleHitbox().y);
        }
        if (Intersector.overlaps(playerCharacter.getCircleHitbox(), entityManager.getCurrentDungeonRoom().getLeftRectangle())){
            playerCharacter.setXVelocity(0);
            playerCharacter.setMaxVelocity(200);
            playerCharacter.setHitboxCentre(64 + playerCharacter.getCircleHitbox().radius, playerCharacter.getCircleHitbox().y);
        }
        if (Intersector.overlaps(playerCharacter.getCircleHitbox(), entityManager.getCurrentDungeonRoom().getBottomRectangle())){
            playerCharacter.setYVelocity(0);
            playerCharacter.setMaxVelocity(200);
            playerCharacter.setHitboxCentre(playerCharacter.getCircleHitbox().x, 64 + playerCharacter.getCircleHitbox().radius);
        }
    }
    public void playerDoorCollision(){
        if (entityManager.getCurrentDungeonRoom().getUpDoor()){
            if (Intersector.overlaps(playerCharacter.getRectangleHitbox(), entityManager.getCurrentDungeonRoom().getNorthDoor())) {
                playerCharacter.setYVelocity(0);
                playerCharacter.setXVelocity(0);
                entityManager.moveNorth();
                playerCharacter.setX(1280 / 2 - playerCharacter.getWidth());
                playerCharacter.setY(70);
            }
        }
        if (entityManager.getCurrentDungeonRoom().getDownDoor()){
            if (Intersector.overlaps(playerCharacter.getRectangleHitbox(), entityManager.getCurrentDungeonRoom().getSouthDoor())) {
                playerCharacter.setYVelocity(0);
                playerCharacter.setXVelocity(0);
                entityManager.moveSouth();
                playerCharacter.setX(1280 / 2 - playerCharacter.getWidth());
                playerCharacter.setY(768 - playerCharacter.getHeight()-70);
            }
        }
        if (entityManager.getCurrentDungeonRoom().getRightDoor()){
            if (Intersector.overlaps(playerCharacter.getRectangleHitbox(), entityManager.getCurrentDungeonRoom().getEastDoor())) {
                playerCharacter.setYVelocity(0);
                playerCharacter.setXVelocity(0);
                entityManager.moveEast();
                playerCharacter.setX(70);
                playerCharacter.setY(768 / 2 - playerCharacter.getHeight());
            }
        }
        if (entityManager.getCurrentDungeonRoom().getLeftDoor()){
            if (Intersector.overlaps(playerCharacter.getRectangleHitbox(), entityManager.getCurrentDungeonRoom().getWestDoor())) {
            playerCharacter.setYVelocity(0);
            playerCharacter.setXVelocity(0);
            entityManager.moveWest();
            playerCharacter.setX(1280 - playerCharacter.getWidth() - 70);
            playerCharacter.setY(768 / 2 - playerCharacter.getHeight());
            }
        }
    }
    public void playerUpdate(){
        playerMovement();
        playerAttack();
    }
    public void playerAttack(){
        if (firing) {
            if (playerCharacter.checkRangedAttack()) {
                if (keyflagRIGHT) playerCharacter.setShotDirection((float) (Math.PI / 2));
                if (keyflagLEFT) playerCharacter.setShotDirection((float) (3 * Math.PI / 2));
                if (keyflagUP) playerCharacter.setShotDirection(0);
                if (keyflagDOWN) playerCharacter.setShotDirection((float) Math.PI);
                entityManager.addNewProjectiles(playerCharacter.rangedAttack());
            }
        }else{
            if (playerCharacter.getTimeSinceLastAttack() < 0.5) {
                playerCharacter.incrementTimeSinceLastAttack();
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
		if (keyflagD) playerCharacter.setDirection((float)(Math.PI/2));
		if (keyflagA) playerCharacter.setDirection((float)(3*Math.PI/2));
		//if (keyflagA && keyflagD) playerCharacter.setDirection(2);
		if (keyflagW) playerCharacter.setDirection(0);
		if (keyflagS) playerCharacter.setDirection((float)Math.PI);
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (gameState){
			case 0:
                if(keycode == Input.Keys.ENTER) gameState = 1;
				break;
			case 1:
                if((keycode == Input.Keys.A)&&(MapSelected < 8)) {MapSelected += 1; cursorLocation();}
                if((keycode == Input.Keys.D)&&(MapSelected > 1)) {MapSelected -= 1; cursorLocation();}
                if(keycode == Input.Keys.ENTER){
                    entityManager.setLevel(MapSelected - 1);
                    entityManager.startLevel(playerCharacter);
                    gameState = 2;
                }
				break;
			case 2:
				if(keycode == Input.Keys.W) keyflagW = true;
				if(keycode == Input.Keys.S) keyflagS = true;
				if(keycode == Input.Keys.D) keyflagD = true;
				if(keycode == Input.Keys.A) keyflagA = true;
                if(keycode == Input.Keys.RIGHT){ keyflagRIGHT = true; firing = true;}
                if(keycode == Input.Keys.UP){ keyflagUP = true; firing = true;}
                if(keycode == Input.Keys.LEFT){ keyflagLEFT = true; firing = true;}
                if(keycode == Input.Keys.DOWN){ keyflagDOWN = true; firing = true;}
				break;
			case 3:
                if(keycode == Input.Keys.W) keyflagW = true;
                if(keycode == Input.Keys.S) keyflagS = true;
                if(keycode == Input.Keys.D) keyflagD = true;
                if(keycode == Input.Keys.A) keyflagA = true;
                if(keycode == Input.Keys.RIGHT){ keyflagRIGHT = true; firing = true;}
                if(keycode == Input.Keys.UP){ keyflagUP = true; firing = true;}
                if(keycode == Input.Keys.LEFT){ keyflagLEFT = true; firing = true;}
                if(keycode == Input.Keys.DOWN){ keyflagDOWN = true; firing = true;}
				break;
            case 4:
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
                if(keycode == Input.Keys.RIGHT){ keyflagRIGHT = false; firing = false;}
                if(keycode == Input.Keys.UP){ keyflagUP = false; firing = false;}
                if(keycode == Input.Keys.LEFT){ keyflagLEFT = false; firing = false;}
                if(keycode == Input.Keys.DOWN){ keyflagDOWN = false; firing = false;}
                if(keyflagDOWN || keyflagLEFT || keyflagUP || keyflagRIGHT){firing = true;}
                if(keycode == Input.Keys.P) {
                    gameState = 3;
                }
                if(keycode == Input.Keys.R){enemy1.setHitboxCentre(600,300);}
                if(keycode == Input.Keys.T){playerCharacter.setHitboxCentre(300,300);}
				break;
			case 3:
                if(keycode == Input.Keys.W) keyflagW = false;
                if(keycode == Input.Keys.S) keyflagS = false;
                if(keycode == Input.Keys.D) keyflagD = false;
                if(keycode == Input.Keys.A) keyflagA = false;
                if(keycode == Input.Keys.RIGHT){ keyflagRIGHT = false; firing = false;}
                if(keycode == Input.Keys.UP){ keyflagUP = false; firing = false;}
                if(keycode == Input.Keys.LEFT){ keyflagLEFT = false; firing = false;}
                if(keycode == Input.Keys.DOWN){ keyflagDOWN = false; firing = false;}
                if(keyflagDOWN || keyflagLEFT || keyflagUP || keyflagRIGHT){firing = true;}
                if(keycode == Input.Keys.P) gameState = 2;
                break;
            case 4:
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
