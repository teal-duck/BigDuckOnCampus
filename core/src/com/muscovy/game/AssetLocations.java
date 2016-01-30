package com.muscovy.game;


import com.muscovy.game.enums.LevelType;


public class AssetLocations {
	public static final String RECYLING_BIN = "accommodationAssets/obstacles/binRecycle.png";
	public static final String WASTE_BIN = "accommodationAssets/obstacles/binWaste.png";

	public static final String CLEANER = "accommodationAssets/enemies/cleaner/rightCleanerWalk/PNGs/rightCleaner1.png";
	public static final String STUDENT = "accommodationAssets/enemies/student/rightStudentWalk/PNGs/rightStudent1.png";

	public static final String ACCOMODATION_BOSS = "accommodationAssets/accommodationBoss.png";

	public static final String DOOR_UP_OPEN = "accommodationAssets/doorOpen/PNGs/accommodationDoorUp.png";
	public static final String DOOR_RIGHT_OPEN = "accommodationAssets/doorOpen/PNGs/accommodationDoorRight.png";
	public static final String DOOR_DOWN_OPEN = "accommodationAssets/doorOpen/PNGs/accommodationDoorDown.png";
	public static final String DOOR_LEFT_OPEN = "accommodationAssets/doorOpen/PNGs/accommodationDoorLeft.png";

	public static final String DOOR_UP_CLOSED = "accommodationAssets/doorClosed/PNGs/accommodationDoorUpClosed.png";
	public static final String DOOR_RIGHT_CLOSED = "accommodationAssets/doorClosed/PNGs/accommodationDoorRightClosed.png";
	public static final String DOOR_DOWN_CLOSED = "accommodationAssets/doorClosed/PNGs/accommodationDoorDownClosed.png";
	public static final String DOOR_LEFT_CLOSED = "accommodationAssets/doorClosed/PNGs/accommodationDoorLeftClosed.png";

	public static final String ROOM_TEMPLATE_FILE = "room_templates.csv";

	public static final String MAIN_MENU = "mainMenu.png";
	public static final String START_GAME_BUTTON = "startGameButton.png";
	public static final String GUI_FRAME = "guiFrame.png";

	public static final String PLAYER = "duck.png";

	public static final String SELECTOR = "selector.png";
	public static final String BAD_SELECTOR = "badselector.png";
	public static final String HES_EAST_MAP = "hesEastMap.png";

	public static final String BULLET = "breadBullet.png";
	
	public static final String FONT_FNT = "Fonts/gamefont.fnt";
	public static final String FONT_PNG = "Fonts/gamefont.png";
	


	public static String getLevelBackground(LevelType levelType) {
		// TODO: Make level backgrounds an array
		switch (levelType) {
		case CONSTANTINE:
			return "accommodationAssets/constantineBackground.png";
		case LANGWITH:
			return "accommodationAssets/langwithBackground.png";
		case GOODRICKE:
			return "accommodationAssets/goodrickeBackground.png";
		case LMB:
			return "accommodationAssets/lmbBackground.png";
		case CATALYST:
			return "accommodationAssets/catalystBackground.png";
		case TFTV:
			return "accommodationAssets/tftvBackground.png";
		case COMP_SCI:
			return "accommodationAssets/csBackground.png";
		case RCH:
			return "accommodationAssets/rchBackground.png";
		default:
			return "";
		}
	}


	private AssetLocations() {
	}
}
