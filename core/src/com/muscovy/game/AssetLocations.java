package com.muscovy.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.muscovy.game.enums.LevelType;


public class AssetLocations {
	public static final String RECYLING_BIN = "accommodationAssets/obstacles/binRecycle.png";
	public static final String WASTE_BIN = "accommodationAssets/obstacles/binWaste.png";

	public static final String CLEANER = "accommodationAssets/enemies/cleaner/rightCleanerWalk/PNGs/rightCleaner1.png";
	public static final String STUDENT = "accommodationAssets/enemies/student/rightStudentWalk/PNGs/rightStudent1.png";
	public static final String ACCOMODATION_BOSS = "accommodationAssets/accommodationBoss.png";

	public static final String PLAYER = "duck.png";
	public static final String BULLET = "breadBullet.png";

	public static final String DOOR_UP_OPEN = "accommodationAssets/doorOpen/PNGs/accommodationDoorUp.png";
	public static final String DOOR_RIGHT_OPEN = "accommodationAssets/doorOpen/PNGs/accommodationDoorRight.png";
	public static final String DOOR_DOWN_OPEN = "accommodationAssets/doorOpen/PNGs/accommodationDoorDown.png";
	public static final String DOOR_LEFT_OPEN = "accommodationAssets/doorOpen/PNGs/accommodationDoorLeft.png";

	public static final String DOOR_UP_CLOSED = "accommodationAssets/doorClosed/PNGs/accommodationDoorUpClosed.png";
	public static final String DOOR_RIGHT_CLOSED = "accommodationAssets/doorClosed/PNGs/accommodationDoorRightClosed.png";
	public static final String DOOR_DOWN_CLOSED = "accommodationAssets/doorClosed/PNGs/accommodationDoorDownClosed.png";
	public static final String DOOR_LEFT_CLOSED = "accommodationAssets/doorClosed/PNGs/accommodationDoorLeftClosed.png";
	
	public static final String DOOR_UP_BOSS = "accommodationAssets/bossDoor/PNGs/accommodationBossDoorUp.png";
	public static final String DOOR_RIGHT_BOSS = "accommodationAssets/bossDoor/PNGs/accommodationBossDoorRight.png";
	public static final String DOOR_DOWN_BOSS = "accommodationAssets/bossDoor/PNGs/accommodationBossDoorDown.png";
	public static final String DOOR_LEFT_BOSS = "accommodationAssets/bossDoor/PNGs/accommodationBossDoorLeft.png";

	public static final String MAIN_MENU = "mainMenu.png";
	public static final String START_GAME_BUTTON = "startGameButton.png";
	public static final String GUI_FRAME = "guiFrame.png";

	public static final String SELECTOR = "selector.png";
	public static final String BAD_SELECTOR = "badselector.png";
	public static final String HES_EAST_MAP = "hesEastMap.png";

	public static final String ROOM_TEMPLATE_FILE = "room_templates.csv";

	public static final String CONSTANTINE_BACKGROUND = "accommodationAssets/constantineBackground.png";;
	public static final String LANGWITH_BACKGROUND = "accommodationAssets/langwithBackground.png";
	public static final String GOODRICKE_BACKGROUND = "accommodationAssets/goodrickeBackground.png";
	public static final String LMB_BACKGROUND = "accommodationAssets/lmbBackground.png";
	public static final String CATALYST_BACKGROUND = "accommodationAssets/catalystBackground.png";
	public static final String TFTV_BACKGROUND = "accommodationAssets/tftvBackground.png";
	public static final String COMPSCI_BACKGROUND = "accommodationAssets/csBackground.png";
	public static final String RCH_BACKGROUND = "accommodationAssets/rchBackground.png";

	public static final String FONT_FNT = "fonts/gamefont.fnt";
	public static final String FONT_PNG = "fonts/gamefont.png";


	public static String getLevelBackground(LevelType levelType) {
		// TODO: Make level backgrounds an array
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


	public static TextureMap loadAllTexturesIntoMap(TextureMap textureMap) {
		textureMap.getTextureOrLoadFile(AssetLocations.RECYLING_BIN);
		textureMap.getTextureOrLoadFile(AssetLocations.WASTE_BIN);

		textureMap.getTextureOrLoadFile(AssetLocations.CLEANER);
		textureMap.getTextureOrLoadFile(AssetLocations.STUDENT);
		textureMap.getTextureOrLoadFile(AssetLocations.ACCOMODATION_BOSS);

		textureMap.getTextureOrLoadFile(AssetLocations.PLAYER);
		textureMap.getTextureOrLoadFile(AssetLocations.BULLET);

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
		textureMap.getTextureOrLoadFile(AssetLocations.START_GAME_BUTTON);
		textureMap.getTextureOrLoadFile(AssetLocations.GUI_FRAME);

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


	public static BitmapFont newFont() {
		return new BitmapFont(Gdx.files.internal(AssetLocations.FONT_FNT),
				Gdx.files.internal(AssetLocations.FONT_PNG), false);
	}


	private AssetLocations() {
	}
}
