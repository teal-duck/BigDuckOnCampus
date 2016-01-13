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
	Room drawRoom;
	Obstacle obstacle1, obstacle2;
    Enemy enemy1, enemy2;
	PlayerCharacter playerCharacter;
	Sprite roomSprite, testSprite1, testSprite2;
	GUI dungeonGUI, overworldGUI, pauseGUI, gameOverGUI;
    EntityManager[] levels;
	boolean keyflagW,keyflagD,keyflagA,keyflagS;
    private BitmapFont xVal, yVal, gameOverFont;
	float w = 1280;
	float h = 720;
	int gameState; // 0 = Main Menu, 1 = Overworld/Map, 2 = Dungeon/Building, 3 = Pause, 4 = Game Over
    int level = 0;

	@Override
	public void create() {
		gameState = 0;
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);
        initialisePlayerCharacter();
        initialiseLevels();
        initialiseGUIs();
        initialiseTesting();
        Gdx.input.setInputProcessor(this);
	}
    private void initialiseLevels(){
        roomSprite = new Sprite();
        roomSprite.setTexture(new Texture("core/assets/testMap.png"));
        roomSprite.setX(0);
        roomSprite.setY(0);
        drawRoom = new Room();
        drawRoom.setSprite(roomSprite);
        levels = new EntityManager[8];
        levels[0] = new EntityManager(drawRoom);
    }
    private void initialiseGUIs(){
        Sprite guiMapSprite = new Sprite();
        Sprite guiDungeonSprite = new Sprite();
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
        dungeonGUI.addData("PlayerXVal", "X Position: " + String.valueOf(playerCharacter.getX()), xVal, 400, 700);
        dungeonGUI.addData("EnemyXVal", "Y Position: " + String.valueOf(playerCharacter.getY()), yVal, 650, 700);
        dungeonGUI.addData("PlayerYVal", "X Position: " + String.valueOf(playerCharacter.getX()), xVal, 400, 650);
        dungeonGUI.addData("EnemyYVal", "Y Position: " + String.valueOf(playerCharacter.getY()), yVal, 650, 650);
        dungeonGUI.addData("Collision", "Collision?: False", yVal, 850, 700);
        dungeonGUI.addData("Invincible", "Invincibility counter: False", yVal, 850, 650);
        //GameOver
        gameOverFont = new BitmapFont();
        gameOverFont.setColor(Color.RED);
        gameOverGUI = new GUI();
        gameOverGUI.addData("Gameover", "Game Over",gameOverFont,640,150);
    }
    private void initialisePlayerCharacter(){
        playerCharacter = new PlayerCharacter();
        playerCharacter.setY(300);
        playerCharacter.setX(300);
    }
	private void initialiseTesting(){
        gameState = 1;
        testSprite1 = new Sprite();
        testSprite1.setTexture(new Texture("core/assets/thing.gif"));
        obstacle1 = new Obstacle(testSprite1);
        obstacle1.setX(350);
        obstacle1.setY(480);
		testSprite2 = new Sprite();
		testSprite2.setTexture(new Texture("core/assets/thing2.gif"));
		obstacle2 = new Obstacle(testSprite2);
		obstacle2.setX(500);
		obstacle2.setY(100);
        obstacle2.setDamaging(true);
        obstacle2.setTouchDamage(10.0f);
        testSprite1 = new Sprite(new Texture("core/assets/testEnemy.png"));
        enemy1 = new Enemy(testSprite1);
        enemy1.setX(1111);
        enemy1.setY(300);
        enemy1.setAttackType(3);
		enemy1.setMovementType(1);
        enemy1.setTouchDamage(30);
        enemy1.setShotType(1);
        testSprite1 = new Sprite(new Texture("core/assets/testEnemy.png"));
        enemy2 = new Enemy(testSprite1);
        enemy2.setX(1024);
        enemy2.setY(300);
        enemy2.setAttackType(3);
        enemy2.setMovementType(0);
        enemy2.setTouchDamage(30);
        enemy2.setShotType(3);
        enemy2.setAttackRange(1024);
        enemy2.setProjectileLife(1);
        enemy2.setProjectileVelocity(400);
		levels[level] = new EntityManager(drawRoom);
		levels[level].addNewObstacle(obstacle1);
		levels[level].addNewObstacle(obstacle2);
        levels[level].addNewEnemy(enemy1);
        levels[level].addNewEnemy(enemy2);
		levels[level].addNewDrawable(playerCharacter);
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
                projectilesUpdate();
                enemiesUpdate();
				if (playerCharacter.getHealth() <= 0){this.gameState = 4;}
				collision();
                break;
            case 3:
                break;

            }
        if (timer > 10){
            timer = 0; //Useful for debugging. Put a break point here if you need to see the variables after 10 seconds
        }
        timer += Gdx.graphics.getDeltaTime();
    }
    public void enemiesUpdate(){
        for (Enemy enemy:levels[level].getEnemies()){
            enemy.update(playerCharacter);
			if (enemy.getAttackType() != 1) {
				if (enemy.checkRangedAttack()) {
					levels[level].addNewProjectiles(enemy.rangedAttack(playerCharacter));
				}
			}
        }
    }
    public void projectilesUpdate(){
		ArrayList<Projectile> projectiles = new ArrayList<Projectile>(levels[level].getProjectiles());
        for (Projectile projectile:projectiles){
			if (Intersector.overlaps(playerCharacter.getBoundingBox(),projectile.getCollisionBox())){
				projectile.kill();
				playerCharacter.damage(projectile.getDamage());
			}
			projectile.update();
        }
		levels[level].killProjectiles();
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
                levels[level].render(batch);
                dungeonGUI.editData("EnemyXVal", "enemy X: " + String.valueOf(enemy1.getX()));
				dungeonGUI.editData("EnemyYVal", "enemy Y: " + String.valueOf(enemy1.getY()));
				dungeonGUI.editData("PlayerXVal","xdist player to enemy: " + String.valueOf(playerCharacter.getX() - enemy1.getX()));
				dungeonGUI.editData("PlayerYVal","ydist player to enemy: " + String.valueOf(playerCharacter.getY() - enemy1.getY()));
                dungeonGUI.editData("Collision","Player health: " + String.valueOf(playerCharacter.getHealth()));
                dungeonGUI.editData("Invincible","Interrupt timer : " + String.valueOf(timer));
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
		 ArrayList<Obstacle> obstacleList = new ArrayList<Obstacle>(levels[level].getObstacles());
         ArrayList<Enemy> enemyList = new ArrayList<Enemy>(levels[level].getEnemies());
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
                playerCharacter.setY(collidable.getY()-playerCharacter.getHeight()+playerCharacter.getHeightOffset());
            }
            if (playerCharacter.collides(collidable.getBoundingBox())) {
                 if (collidable.isDamaging()){
                     playerCharacter.damage(collidable.getTouchDamage());
                 }
            }
		}
        for (Enemy enemy:enemyList) {
			enemy.resetMaxVelocity();
            enemy.setCollidingWithSomething(false);
            //Enemy-Obstacle Collisions
			for (Obstacle obstacle:obstacleList){
				enemyObstacleCollision(enemy,obstacle);
                playerEnemyCollision(enemy);
			}
            //Enemy-Wall Collisions
            enemyWallCollision(enemy);
            playerEnemyCollision(enemy);
        }
        playerWallCollision();
	}
    public void enemyObstacleCollision(Enemy enemy, Obstacle obstacle){
        if (Intersector.overlaps(enemy.getBottomRectangle(), obstacle.getTopRectangle())) {
            enemy.setYVelocity(0);
            enemy.setMaxVelocity(50);
            enemy.setY(obstacle.getTopRectangle().getY() + obstacle.getTopRectangle().getHeight());
            enemy.setCollidingWithSomething(true);
        }
        if (Intersector.overlaps(enemy.getLeftRectangle(),obstacle.getRightRectangle())){
            enemy.setXVelocity(0);
            enemy.setMaxVelocity(50);
            enemy.setX(obstacle.getRightRectangle().getX() + obstacle.getRightRectangle().getWidth());
            enemy.setCollidingWithSomething(true);
        }
        if (Intersector.overlaps(enemy.getRightRectangle(), obstacle.getLeftRectangle())){
            enemy.setXVelocity(0);
            enemy.setMaxVelocity(50);
            enemy.setX(obstacle.getLeftRectangle().getX() - enemy.getSprite().getWidth());
            enemy.setCollidingWithSomething(true);
        }
        if (Intersector.overlaps(enemy.getTopRectangle(),obstacle.getBottomRectangle())){
            enemy.setYVelocity(0);
            enemy.setMaxVelocity(50);
            enemy.setY(enemy.getY() - (enemy.getHeightOffset()+enemy.getRectangleBorderSize()-2));
            enemy.setCollidingWithSomething(true);
        }
    }
    public void enemyWallCollision(Enemy enemy){
        if (Intersector.overlaps(levels[level].getCurrentRoom().getTopRectangle(),enemy.getTopRectangle())) {
            enemy.setYVelocity(0);
            enemy.setMaxVelocity(50);
            enemy.setY(levels[level].getCurrentRoom().getTopRectangle().getY() - enemy.getBoundingBox().getHeight());
            enemy.setCollidingWithSomething(true);
        }
        if (Intersector.overlaps(levels[level].getCurrentRoom().getRightRectangle(),enemy.getRightRectangle())){
            enemy.setXVelocity(0);
            enemy.setMaxVelocity(50);
            enemy.setX(levels[level].getCurrentRoom().getRightRectangle().getX()-enemy.getWidth());
            enemy.setCollidingWithSomething(true);
        }
        if (Intersector.overlaps(levels[level].getCurrentRoom().getLeftRectangle(),enemy.getLeftRectangle())){
            enemy.setXVelocity(0);
            enemy.setMaxVelocity(50);
            enemy.setX(levels[level].getCurrentRoom().getX()+64);
            enemy.setCollidingWithSomething(true);
        }
        if (Intersector.overlaps(levels[level].getCurrentRoom().getBottomRectangle(),enemy.getBottomRectangle())){
            enemy.setYVelocity(0);
            enemy.setMaxVelocity(50);
            enemy.setY(64);
            enemy.setCollidingWithSomething(true);
        }
    }
    public void playerEnemyCollision(Enemy enemy){
        if (Intersector.overlaps(enemy.getBottomRectangle(),playerCharacter.getTopRectangle())) {
            playerCharacter.setMaxVelocity(playerCharacter.getMaxVelocity()/2);
            if (enemy.isCollidingWithSomething()) {
                playerCharacter.setY(enemy.getY() - playerCharacter.getHeight() + playerCharacter.getHeightOffset());
                playerCharacter.setYVelocity(0);
            }else{
                enemy.setDirection(playerCharacter.getDirection());
                enemy.setY(playerCharacter.getTopRectangle().getY() + playerCharacter.getTopRectangle().getHeight());
                enemy.setXVelocity(0);
                enemy.setYVelocity(0);
            }
        }
        if (Intersector.overlaps(enemy.getLeftRectangle(),playerCharacter.getRightRectangle())){
            playerCharacter.setMaxVelocity(playerCharacter.getMaxVelocity()/2);
            if (enemy.isCollidingWithSomething()) {
                playerCharacter.setX(enemy.getLeftRectangle().getX() - playerCharacter.getSprite().getWidth());
                playerCharacter.setXVelocity(0);
            }else{
                enemy.setDirection(playerCharacter.getDirection());
                enemy.setX(playerCharacter.getRightRectangle().getX() + playerCharacter.getRightRectangle().getWidth());
                enemy.setXVelocity(0);
                enemy.setYVelocity(0);
            }
        }
        if (Intersector.overlaps(enemy.getRightRectangle(),playerCharacter.getLeftRectangle())){
            playerCharacter.setMaxVelocity(playerCharacter.getMaxVelocity()/2);
            if (enemy.isCollidingWithSomething()) {
                playerCharacter.setX(enemy.getRightRectangle().getX() + enemy.getRightRectangle().getWidth());
                playerCharacter.setXVelocity(0);
            }else{
                enemy.setDirection(playerCharacter.getDirection());
                enemy.setX(playerCharacter.getLeftRectangle().getX() - enemy.getSprite().getWidth());
                enemy.setXVelocity(0);
                enemy.setYVelocity(0);
            }
        }
        if (Intersector.overlaps(enemy.getTopRectangle(),playerCharacter.getBottomRectangle())){
            playerCharacter.setMaxVelocity(playerCharacter.getMaxVelocity()/2);
            if (enemy.isCollidingWithSomething()){
                playerCharacter.setY(enemy.getTopRectangle().getY() + enemy.getTopRectangle().getHeight());
                playerCharacter.setYVelocity(0);
            }else{
                enemy.setDirection(playerCharacter.getDirection());
                enemy.setY(playerCharacter.getY() - enemy.getHeight() + enemy.getHeightOffset()-1);
                enemy.setXVelocity(0);
                enemy.setYVelocity(0);
            }
        }
        if (playerCharacter.collides(enemy.getBoundingBox())) { //Doesn't damage twice because of invincibility frames
            if (enemy.getAttackType() != 1){
                playerCharacter.damage(enemy.getTouchDamage());
            }
        }
    }
    public void playerWallCollision(){
        if (Intersector.overlaps(levels[level].getCurrentRoom().getTopRectangle(),playerCharacter.getTopRectangle())) {
            playerCharacter.setYVelocity(0);
            playerCharacter.setMaxVelocity(200);
            playerCharacter.setY(levels[level].getCurrentRoom().getTopRectangle().getY() - playerCharacter.getBoundingBox().getHeight());
        }
        if (Intersector.overlaps(levels[level].getCurrentRoom().getRightRectangle(),playerCharacter.getRightRectangle())){
            playerCharacter.setXVelocity(0);
            playerCharacter.setMaxVelocity(200);
            playerCharacter.setX(levels[level].getCurrentRoom().getRightRectangle().getX()-playerCharacter.getWidth());
        }
        if (Intersector.overlaps(levels[level].getCurrentRoom().getLeftRectangle(),playerCharacter.getLeftRectangle())){
            playerCharacter.setXVelocity(0);
            playerCharacter.setMaxVelocity(200);
            playerCharacter.setX(levels[level].getCurrentRoom().getX()+64);
        }
        if (Intersector.overlaps(levels[level].getCurrentRoom().getBottomRectangle(),playerCharacter.getBottomRectangle())){
            playerCharacter.setYVelocity(0);
            playerCharacter.setMaxVelocity(200);
            playerCharacter.setY(64);//not sure why I need this 2 here. It's wonky.
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
