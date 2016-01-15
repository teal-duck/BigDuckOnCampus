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
	GUI mainMenuGUI, dungeonGUI, overworldGUI, pauseGUI, gameOverGUI;
    EntityManager[] levels;
	boolean keyflagW,keyflagD,keyflagA,keyflagS, keyflagUP, keyflagRIGHT, keyflagLEFT, keyflagDOWN, firing = false;
    private BitmapFont xVal, yVal, gameOverFont;
	float w = 1280;
	float h = 960;
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
        dungeonGUI.addData("PlayerXVal", "X Position: " + String.valueOf(playerCharacter.getX()), xVal, 400, 900);
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
        testSprite1 = new Sprite();
        testSprite1.setTexture(new Texture("core/assets/rock.png"));
        obstacle1 = new Obstacle(testSprite1);
        obstacle1.setXTiles(10);
        obstacle1.setYTiles(100);
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
        enemy1.setAttackType(1);
		enemy1.setMovementType(0);
        enemy1.setTouchDamage(30);
        enemy1.setShotType(1);
        testSprite1 = new Sprite(new Texture("core/assets/testEnemy.png"));
        enemy2 = new Enemy(testSprite1);
        enemy2.setXTiles(0);
        enemy2.setYTiles(1);
        enemy2.setAttackType(0);
        enemy2.setMovementType(0);
        enemy2.setTouchDamage(30);
        enemy2.setShotType(0);
		levels[level] = new EntityManager(drawRoom);
		levels[level].addNewObstacle(obstacle1);
		//levels[level].addNewObstacle(obstacle2);
        levels[level].addNewEnemy(enemy1);
        //levels[level].addNewEnemy(enemy2);
		levels[level].addNewDrawable(playerCharacter);
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
        for (Enemy enemy:levels[level].getEnemies()){
            enemy.update(playerCharacter);
			if ((enemy.getAttackType() != 0)&&(enemy.checkRangedAttack())) {
                levels[level].addNewProjectiles(enemy.rangedAttack(playerCharacter));
			}
            playerEnemyCollision(enemy);
            enemyWallCollision(enemy);
            playerEnemyCollision(enemy);
            enemyWallCollision(enemy);
        }
    }
    public void projectilesUpdate(){
        ArrayList<Projectile> projectileList = new ArrayList<Projectile>(levels[level].getProjectiles());
        for (Projectile projectile:projectileList){
            projectile.update();
            projectileWallCollision(projectile);
            projectilePlayerCollision(projectile);
        }
    }
    public void cleanupDeadThings(){
        levels[level].killEnemies();
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
                mainMenuGUI.render(batch);
				break;
            case 1:
				overworldGUI.render(batch);
				break;
            case 2:
                levels[level].render(batch);
                dungeonGUI.editData("EnemyXVal", "enemy X: " + String.valueOf(enemy1.getX()));
				dungeonGUI.editData("EnemyYVal", "enemy Y: " + String.valueOf(enemy1.getY()));
				dungeonGUI.editData("PlayerXVal","angle enemy to player: " + String.valueOf(enemy1.getAngleTo(playerCharacter)));
				dungeonGUI.editData("PlayerYVal","ydist player to enemy: " + String.valueOf(playerCharacter.getY() - enemy1.getY()));
                //dungeonGUI.editData("Collision","Player health: " + String.valueOf(playerCharacter.getHealth()));
                dungeonGUI.editData("Invincible","Time since last attack: " + String.valueOf(playerCharacter.getTimeSinceLastAttack()));
                dungeonGUI.render(batch);
                levels[level].render(batch);
                break;
            case 3:
                dungeonGUI.render(batch);
                levels[level].render(batch);
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
        ArrayList<Obstacle> obstacleList = new ArrayList<Obstacle>(levels[level].getObstacles());
        ArrayList<Enemy> enemyList = new ArrayList<Enemy>(levels[level].getEnemies());
        ArrayList<Projectile> projectileList = new ArrayList<Projectile>(levels[level].getProjectiles());
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
        if (Intersector.overlaps(projectile.getCollisionBox(),levels[level].getCurrentRoom().getProjectileWallBottom())){
            projectile.kill();
        }
        if (Intersector.overlaps(projectile.getCollisionBox(),levels[level].getCurrentRoom().getProjectileWallTop())){
            projectile.kill();
        }
        if (Intersector.overlaps(projectile.getCollisionBox(),levels[level].getCurrentRoom().getProjectileWallLeft())){
            projectile.kill();
        }
        if (Intersector.overlaps(projectile.getCollisionBox(),levels[level].getCurrentRoom().getProjectileWallRight())){
            projectile.kill();
        }
    }
    public void playerObstacleCollision(Obstacle obstacle){
        if (Intersector.overlaps(playerCharacter.getCircleHitbox(), obstacle.getRectangleHitbox())) {
            //playerCharacter.moveToNearestEdgeRectangle(obstacle);
            dungeonGUI.editData("Collides","Collided");
        }

        /*if (Intersector.overlaps(playerCharacter.getCircleHitbox(),obstacle.getRectangleHitbox())) {
            /*if(playerCharacter.getCircleHitbox().x>(obstacle.getX()-playerCharacter.getCircleHitbox().radius)-1){
                playerCharacter.setYVelocity(0);
                //playerCharacter.setXVelocity(0);
                playerCharacter.setMaxVelocity(200);
                playerCharacter.backOneStep();
                if (obstacle.isDamaging()){
                    playerCharacter.damage(obstacle.getTouchDamage());

            }
        }
        /*if (Intersector.overlaps(playerCharacter.getCircleHitbox(),obstacle.getRightRectangle())){
            /*playerCharacter.setXVelocity(0);
            playerCharacter.setMaxVelocity(200);
            playerCharacter.setHitboxCentre(obstacle.getRightRectangle().getX() + obstacle.getRightRectangle().getWidth()+playerCharacter.getCircleHitbox().radius,playerCharacter.getCircleHitbox().y);
            if (obstacle.isDamaging()){
                playerCharacter.damage(obstacle.getTouchDamage());
            }
            if(playerCharacter.getCircleHitbox().x>(obstacle.getX()-playerCharacter.getCircleHitbox().radius)-1){
                playerCharacter.setYVelocity(0);
                playerCharacter.setMaxVelocity(200);
                playerCharacter.backOneStep();
                if (obstacle.isDamaging()){
                    playerCharacter.damage(obstacle.getTouchDamage());
                }
            }
        }
       if (Intersector.overlaps(playerCharacter.getCircleHitbox(),obstacle.getLeftRectangle())){
           /* playerCharacter.setXVelocity(0);
            playerCharacter.setMaxVelocity(200);
            playerCharacter.setHitboxCentre(obstacle.getLeftRectangle().getX()-playerCharacter.getCircleHitbox().radius,playerCharacter.getCircleHitbox().y);
            if (obstacle.isDamaging()){
                playerCharacter.damage(obstacle.getTouchDamage());
            }
           if(playerCharacter.getCircleHitbox().x>(obstacle.getX()-playerCharacter.getCircleHitbox().radius)-1){
               playerCharacter.setYVelocity(0);
               playerCharacter.setMaxVelocity(200);
               playerCharacter.backOneStep();
               if (obstacle.isDamaging()){
                   playerCharacter.damage(obstacle.getTouchDamage());
               }
           }
        }
        /*if (Intersector.overlaps(playerCharacter.getCircleHitbox(),obstacle.getBottomRectangle())){
            playerCharacter.setYVelocity(0);
            playerCharacter.setMaxVelocity(200);
            playerCharacter.setY(obstacle.getY()-playerCharacter.getHeight()+playerCharacter.getHeightOffset());
            if (obstacle.isDamaging()){
                playerCharacter.damage(obstacle.getTouchDamage());
            }
        }
        /*if (playerCharacter.collides(obstacle)) {
            if(playerCharacter.getCircleHitbox().x >= obstacle.getCircleHitbox().x){
                if(playerCharacter.getCircleHitbox().y >= obstacle.getCircleHitbox().y){
                    playerCharacter.backOneStepY();
                }else{

                }
            }else if(playerCharacter.getCircleHitbox().x < obstacle.getCircleHitbox().x) {
                if (playerCharacter.getCircleHitbox().y >= obstacle.getCircleHitbox().y) {

                } else {

                }
            }
        }*/
    }
    public void enemyObstacleCollision(Enemy enemy, Obstacle obstacle){
        /*if (Intersector.overlaps(enemy.getBottomRectangle(), obstacle.getTopRectangle())) {
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
            enemy.setX(obstacle.getLeftRectangle().getX() - enemy.getWidth());
            enemy.setCollidingWithSomething(true);
        }
        if (Intersector.overlaps(enemy.getTopRectangle(),obstacle.getBottomRectangle())){
            enemy.setYVelocity(0);
            enemy.setMaxVelocity(50);
            enemy.setY(obstacle.getY() -enemy.getHeight() + enemy.getHeightOffset());
            enemy.setCollidingWithSomething(true);
        }*/
        if (enemy.collides(obstacle)){
            enemy.moveToNearestEdgeCircle(obstacle);
        }
    }
    public void enemyWallCollision(Enemy enemy){
        if (Intersector.overlaps(levels[level].getCurrentRoom().getTopRectangle(),enemy.getTopRectangle())) {
            enemy.setYVelocity(0);
            enemy.setMaxVelocity(50);
            enemy.setY(levels[level].getCurrentRoom().getTopRectangle().getY() - enemy.getCircleHitbox().radius);
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
        /*if (Intersector.overlaps(enemy.getBottomRectangle(),playerCharacter.getTopRectangle())) {
            playerCharacter.setMaxVelocity(playerCharacter.getMaxVelocity()/2);
            if (enemy.isCollidingWithSomething()) {
                playerCharacter.setY(enemy.getY() - playerCharacter.getHeight() + playerCharacter.getHeightOffset());
                playerCharacter.setYVelocity(0);
            }else{
                enemy.moveToNearestEdgeCircle(playerCharacter);
                enemy.setDirection(playerCharacter.getDirection());
                enemy.setY(playerCharacter.getTopRectangle().getY() + playerCharacter.getTopRectangle().getHeight());
                enemy.setXVelocity(0);
                enemy.setYVelocity(0);
            }
        }
        if (Intersector.overlaps(enemy.getLeftRectangle(),playerCharacter.getRightRectangle())){
            playerCharacter.setMaxVelocity(playerCharacter.getMaxVelocity()/2);
            if (enemy.isCollidingWithSomething()) {
                playerCharacter.setX(enemy.getLeftRectangle().getX() - playerCharacter.getWidth());
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
                enemy.setX(playerCharacter.getLeftRectangle().getX() - enemy.getWidth());
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
        }*/
        if (playerCharacter.collides(enemy)) { //Doesn't damage twice because of invincibility frames
            playerCharacter.setMaxVelocity(100);
            enemy.moveToNearestEdgeCircle(playerCharacter);
            if (enemy.getAttackType() != 1){
                playerCharacter.damage(enemy.getTouchDamage());
            }
        }
    }
    public void playerWallCollision(){
        if (Intersector.overlaps(levels[level].getCurrentRoom().getTopRectangle(),playerCharacter.getTopRectangle())) {
            playerCharacter.setYVelocity(0);
            playerCharacter.setMaxVelocity(200);
            playerCharacter.setY(levels[level].getCurrentRoom().getTopRectangle().getY() - playerCharacter.getCircleHitbox().radius);
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
                levels[level].addNewProjectiles(playerCharacter.rangedAttack());
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
                if(keycode == Input.Keys.ENTER) gameState = 2;
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
