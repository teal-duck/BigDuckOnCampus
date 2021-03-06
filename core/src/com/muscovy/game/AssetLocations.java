package com.muscovy.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.muscovy.game.enums.LevelType;


/**
 * Project URL : http://teal-duck.github.io/teal-duck <br>
 * New class: constants used to reference files containing textures for sprites in game.
 */
public class AssetLocations {
	public static final String BOMB_GUI = "bomb_icon.png";

	// Obstacles
	public static final String RECYLING_BIN = "accommodationAssets/obstacles/binRecycle.png";
	public static final String SPIKES = "spikes.png";

	// Enemies
	public static final String CLEANER = "cleaner_walk.png";
	public static final String CLEANER_WATER = "accommodationAssets/enemies/cleaner/rightCleanerWalk/PNGs/rightCleaner1_water.png";
	public static final String STUDENT = "student_walk.png";
	public static final String STUDENT_WATER = "accommodationAssets/enemies/student/rightStudentWalk/PNGs/rightStudent1_water.png";
	public static final String ACCOMODATION_BOSS = "accommodationAssets/accommodationBoss.png";
	public static final String ACCOMODATION_BOSS_WATER = "accommodationAssets/accommodationBoss_water.png";

	// Player
	public static final String PLAYER = "newduck_walk.png";
	public static final String PLAYER_WATER = "newduck_walk_water.png";
	public static final String PLAYER_SUNGLASSES = "newduck_sunglasses_walk.png";

	// Projectiles
	public static final String BULLET = "breadBullet.png";
	public static final String ENEMY_BULLET = "enemyBullet.png";
	public static final String FLAME = "flame.png";
	// http://opengameart.org/content/bomb-0
	public static final String BOMB = "bomb64.png";
	public static final String EXPLOSION = "explosion.png";

	// Items
	public static final String HEALTHPACK = "healthpack.png";
	public static final String TRIPLE_SHOT = "triple_shot.png";
	public static final String RAPID_FIRE = "rapid_fire.png";
	public static final String FLAME_THROWER = "flame_thrower.png";
	public static final String BOMB_ITEM = "bomb_pickup.png";
	public static final String EXTRA_HEALTH = "health_increase.png";
	// http://opengameart.org/content/duck
	public static final String FLIGHT_ITEM = "flight.png";
	public static final String SUNGLASSES = "sunglasses.png";

	// Open Doors
	public static final String DOOR_UP_OPEN = "accommodationAssets/doorOpen/PNGs/accommodationDoorUp.png";
	public static final String DOOR_RIGHT_OPEN = "accommodationAssets/doorOpen/PNGs/accommodationDoorRight.png";
	public static final String DOOR_DOWN_OPEN = "accommodationAssets/doorOpen/PNGs/accommodationDoorDown.png";
	public static final String DOOR_LEFT_OPEN = "accommodationAssets/doorOpen/PNGs/accommodationDoorLeft.png";

	// Closed Doors
	public static final String DOOR_UP_CLOSED = "accommodationAssets/doorClosed/PNGs/accommodationDoorUpClosed.png";
	public static final String DOOR_RIGHT_CLOSED = "accommodationAssets/doorClosed/PNGs/accommodationDoorRightClosed.png";
	public static final String DOOR_DOWN_CLOSED = "accommodationAssets/doorClosed/PNGs/accommodationDoorDownClosed.png";
	public static final String DOOR_LEFT_CLOSED = "accommodationAssets/doorClosed/PNGs/accommodationDoorLeftClosed.png";

	// Boss Doors
	public static final String DOOR_UP_BOSS = "accommodationAssets/bossDoor/PNGs/accommodationBossDoorUp.png";
	public static final String DOOR_RIGHT_BOSS = "accommodationAssets/bossDoor/PNGs/accommodationBossDoorRight.png";
	public static final String DOOR_DOWN_BOSS = "accommodationAssets/bossDoor/PNGs/accommodationBossDoorDown.png";
	public static final String DOOR_LEFT_BOSS = "accommodationAssets/bossDoor/PNGs/accommodationBossDoorLeft.png";

	// Menu
	public static final String MAIN_MENU = "mainMenu.png";
	public static final String GAME_BUTTON = "GameButton.png";
	public static final String GAME_BUTTON_SELECT = "GameButtonSelect.png";

	// Map
	public static final String SELECTOR = "selector.png";
	public static final String BAD_SELECTOR = "badselector.png";
	public static final String HES_EAST_MAP = "hesEastMap.png";

	// Room layouts
	public static final String ROOM_TEMPLATE_FILE = "room_templates.csv";

	// Room backgrounds
	public static final String CONSTANTINE_BACKGROUND = "accommodationAssets/constantineBackground.png";
	public static final String LANGWITH_BACKGROUND = "accommodationAssets/langwithBackground.png";
	public static final String GOODRICKE_BACKGROUND = "accommodationAssets/goodrickeBackground.png";
	public static final String LMB_BACKGROUND = "accommodationAssets/lmbBackground.png";
	public static final String CATALYST_BACKGROUND = "accommodationAssets/catalystBackground.png";
	public static final String TFTV_BACKGROUND = "accommodationAssets/tftvBackground.png";
	public static final String COMPSCI_BACKGROUND = "accommodationAssets/csBackground.png";
	public static final String RCH_BACKGROUND = "accommodationAssets/rchBackground.png";

	// Fonts
	public static final String FONT_FNT_20 = "fonts/gamefont_20.fnt";
	public static final String FONT_PNG_20 = "fonts/gamefont_20.png";

	public static final String FONT_FNT_32 = "fonts/gamefont_32.fnt";
	public static final String FONT_PNG_32 = "fonts/gamefont_32.png";

	// When running in Eclipse, these save to BigDuckOnCampus/desktop/preferences/...
	public static final String BASE_SAVE_FOLDER = "preferences/";
	public static final String SETTINGS_FOLDER = AssetLocations.BASE_SAVE_FOLDER + "settings/";
	public static final String SAVE_FOLDER = AssetLocations.BASE_SAVE_FOLDER + "saves/";

	public static final String CONTROLS_FILE_NAME = "controls.json";
	public static final String CONTROLS_FILE = AssetLocations.SETTINGS_FOLDER + AssetLocations.CONTROLS_FILE_NAME;


	/**
	 * @param levelType
	 * @return
	 */
	public static String getLevelBackgroundTextureName(LevelType levelType) {
		switch (levelType) {
		case CONSTANTINE:
			return AssetLocations.CONSTANTINE_BACKGROUND;
		case LANGWITH:
			return AssetLocations.LANGWITH_BACKGROUND;
		case GOODRICKE:
			return AssetLocations.GOODRICKE_BACKGROUND;
		case LMB:
			return AssetLocations.LMB_BACKGROUND;
		case CATALYST:
			return AssetLocations.CATALYST_BACKGROUND;
		case TFTV:
			return AssetLocations.TFTV_BACKGROUND;
		case COMP_SCI:
			return AssetLocations.COMPSCI_BACKGROUND;
		case RCH:
			return AssetLocations.RCH_BACKGROUND;
		default:
			return "";
		}
	}


	/**
	 * Queries the texture map to load all the textures (that haven't already been loaded).
	 *
	 * @param textureMap
	 * @return
	 */
	public static TextureMap loadAllTexturesIntoMap(TextureMap textureMap) {
		textureMap.getTextureOrLoadFile(AssetLocations.BOMB_GUI);

		textureMap.getTextureOrLoadFile(AssetLocations.RECYLING_BIN);
		textureMap.getTextureOrLoadFile(AssetLocations.SPIKES);

		textureMap.getTextureOrLoadFile(AssetLocations.CLEANER);
		textureMap.getTextureOrLoadFile(AssetLocations.CLEANER_WATER);
		textureMap.getTextureOrLoadFile(AssetLocations.STUDENT);
		textureMap.getTextureOrLoadFile(AssetLocations.STUDENT_WATER);
		textureMap.getTextureOrLoadFile(AssetLocations.ACCOMODATION_BOSS);
		textureMap.getTextureOrLoadFile(AssetLocations.ACCOMODATION_BOSS_WATER);

		textureMap.getTextureOrLoadFile(AssetLocations.PLAYER);
		textureMap.getTextureOrLoadFile(AssetLocations.PLAYER_WATER);
		textureMap.getTextureOrLoadFile(AssetLocations.PLAYER_SUNGLASSES);

		textureMap.getTextureOrLoadFile(AssetLocations.BULLET);
		textureMap.getTextureOrLoadFile(AssetLocations.ENEMY_BULLET);
		textureMap.getTextureOrLoadFile(AssetLocations.FLAME);
		textureMap.getTextureOrLoadFile(AssetLocations.BOMB);
		textureMap.getTextureOrLoadFile(AssetLocations.EXPLOSION);

		textureMap.getTextureOrLoadFile(AssetLocations.HEALTHPACK);
		textureMap.getTextureOrLoadFile(AssetLocations.TRIPLE_SHOT);
		textureMap.getTextureOrLoadFile(AssetLocations.RAPID_FIRE);
		textureMap.getTextureOrLoadFile(AssetLocations.FLAME_THROWER);
		textureMap.getTextureOrLoadFile(AssetLocations.BOMB_ITEM);
		textureMap.getTextureOrLoadFile(AssetLocations.EXTRA_HEALTH);
		textureMap.getTextureOrLoadFile(AssetLocations.FLIGHT_ITEM);
		textureMap.getTextureOrLoadFile(AssetLocations.SUNGLASSES);

		textureMap.getTextureOrLoadFile(AssetLocations.DOOR_UP_OPEN);
		textureMap.getTextureOrLoadFile(AssetLocations.DOOR_RIGHT_OPEN);
		textureMap.getTextureOrLoadFile(AssetLocations.DOOR_DOWN_OPEN);
		textureMap.getTextureOrLoadFile(AssetLocations.DOOR_LEFT_OPEN);

		textureMap.getTextureOrLoadFile(AssetLocations.DOOR_UP_CLOSED);
		textureMap.getTextureOrLoadFile(AssetLocations.DOOR_RIGHT_CLOSED);
		textureMap.getTextureOrLoadFile(AssetLocations.DOOR_DOWN_CLOSED);
		textureMap.getTextureOrLoadFile(AssetLocations.DOOR_LEFT_CLOSED);

		textureMap.getTextureOrLoadFile(AssetLocations.DOOR_UP_BOSS);
		textureMap.getTextureOrLoadFile(AssetLocations.DOOR_RIGHT_BOSS);
		textureMap.getTextureOrLoadFile(AssetLocations.DOOR_DOWN_BOSS);
		textureMap.getTextureOrLoadFile(AssetLocations.DOOR_LEFT_BOSS);

		textureMap.getTextureOrLoadFile(AssetLocations.MAIN_MENU);
		textureMap.getTextureOrLoadFile(AssetLocations.GAME_BUTTON);
		textureMap.getTextureOrLoadFile(AssetLocations.GAME_BUTTON_SELECT);

		textureMap.getTextureOrLoadFile(AssetLocations.SELECTOR);
		textureMap.getTextureOrLoadFile(AssetLocations.BAD_SELECTOR);
		textureMap.getTextureOrLoadFile(AssetLocations.HES_EAST_MAP);

		textureMap.getTextureOrLoadFile(AssetLocations.CONSTANTINE_BACKGROUND);
		textureMap.getTextureOrLoadFile(AssetLocations.LANGWITH_BACKGROUND);
		textureMap.getTextureOrLoadFile(AssetLocations.GOODRICKE_BACKGROUND);
		textureMap.getTextureOrLoadFile(AssetLocations.LMB_BACKGROUND);
		textureMap.getTextureOrLoadFile(AssetLocations.CATALYST_BACKGROUND);
		textureMap.getTextureOrLoadFile(AssetLocations.TFTV_BACKGROUND);
		textureMap.getTextureOrLoadFile(AssetLocations.COMPSCI_BACKGROUND);
		textureMap.getTextureOrLoadFile(AssetLocations.RCH_BACKGROUND);

		return textureMap;
	}


	/**
	 * Loads a font of size 20.
	 *
	 * @return
	 */
	public static BitmapFont newFont20() {
		return new BitmapFont(Gdx.files.internal(AssetLocations.FONT_FNT_20),
				Gdx.files.internal(AssetLocations.FONT_PNG_20), false);
	}


	/**
	 * Loads a font of size 32.
	 *
	 * @return
	 */
	public static BitmapFont newFont32() {
		return new BitmapFont(Gdx.files.internal(AssetLocations.FONT_FNT_32),
				Gdx.files.internal(AssetLocations.FONT_PNG_32), false);
	}


	private AssetLocations() {
	}
}
