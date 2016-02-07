package com.muscovy.game.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.muscovy.game.AssetLocations;
import com.muscovy.game.input.Action;
import com.muscovy.game.input.ControlMap;


/**
 * Renders and provides logic for a list of GUI buttons for use in menus.
 */
public class ButtonList {
	private String[] buttonTexts;
	private GlyphLayout[] buttonLayouts;
	private BitmapFont font;
	private OrthographicCamera guiCamera;

	private ControlMap controlMap;
	private Controller controller;

	private int topLeftX = 0;
	private int topLeftY = 0;
	private int buttonWidth = 0;
	private int buttonHeight = 0;
	private int buttonDifference = 0;
	private int buttonTextVerticalOffset = 0;

	private int halfButtonWidth = buttonWidth / 2;

	private int selected = 0;
	private boolean justMoved = true;
	private boolean enterJustPushed = true;

	// Default button sizes
	// Used for consistency between menus
	// But setDimensions() allows you to change the sizes for a specific button list
	public static final int BUTTON_WIDTH = 350;
	public static final int BUTTON_HEIGHT = 80;
	public static final int BUTTON_DIFFERENCE = 10;
	public static final int BUTTON_TEXT_VERTICAL_OFFSET = 30;
	public static final int WINDOW_EDGE_OFFSET = 32;

	private Color selectedColour = Color.WHITE;
	private Color deselectedColour = Color.BLACK;


	private Sprite sprite;
	
	/**
	 * @param buttonTexts
	 * @param font
	 * @param guiCamera
	 * @param controlMap
	 * @param controller
	 */
	public ButtonList(String[] buttonTexts, BitmapFont font, OrthographicCamera guiCamera, ControlMap controlMap,
			Controller controller) {
		this.buttonTexts = buttonTexts;
		this.font = font;
		this.guiCamera = guiCamera;
		this.controlMap = controlMap;
		this.controller = controller;

		buttonLayouts = new GlyphLayout[buttonTexts.length];
		for (int i = 0; i < buttonTexts.length; i += 1) {
			buttonLayouts[i] = new GlyphLayout(font, buttonTexts[i]);
		}
		
		sprite = new Sprite();
	}


	/**
	 * Returns the height in pixels a button list with default sizes will take up if it had buttonCount buttons.
	 *
	 * @param buttonCount
	 * @return
	 */
	public static int getHeightForDefaultButtonList(int buttonCount) {
		return (buttonCount * (ButtonList.BUTTON_HEIGHT + ButtonList.BUTTON_DIFFERENCE))
				- ButtonList.BUTTON_DIFFERENCE;
	}


	/**
	 * @param topLeftX
	 * @param topLeftY
	 * @param buttonWidth
	 * @param buttonHeight
	 * @param buttonDifference
	 * @param buttonTextVerticalOffset
	 */
	public void setDimensions(int topLeftX, int topLeftY, int buttonWidth, int buttonHeight, int buttonDifference,
			int buttonTextVerticalOffset) {
		this.topLeftX = topLeftX;
		this.topLeftY = topLeftY;
		this.buttonWidth = buttonWidth;
		this.buttonHeight = buttonHeight;
		this.buttonDifference = buttonDifference;
		this.buttonTextVerticalOffset = buttonTextVerticalOffset;
		halfButtonWidth = buttonWidth / 2;
	}


	/**
	 * Set the sizes for the buttons to the default, and position the buttons on the screen.
	 *
	 * @param topLeftX
	 * @param topLeftY
	 */
	public void setPositionDefaultSize(int topLeftX, int topLeftY) {
		setDimensions(topLeftX, topLeftY, ButtonList.BUTTON_WIDTH, ButtonList.BUTTON_HEIGHT,
				ButtonList.BUTTON_DIFFERENCE, ButtonList.BUTTON_TEXT_VERTICAL_OFFSET);
	}


	/**
	 * @param selectedColour
	 * @param deselectedColour
	 * @param outlineColour
	 */
	public void setColours(Color selectedColour, Color deselectedColour, Color outlineColour) {
		this.selectedColour = selectedColour;
		this.deselectedColour = deselectedColour;
	}

	public int getSelected() {
		return selected;
	}


	/**
	 * True if enter key/controller button/mouse is pressed whilst over a button. Only call this once per frame as
	 * it updates state for the previous frame.
	 *
	 * @return
	 */
	public boolean isSelectedSelected() {
		boolean isSelected = false;

		float selectState = controlMap.getStateForAction(Action.ENTER, controller);
		if (selectState > 0) {
			isSelected = true;
		}

		if (getMouseOverButton() == selected) {
			if (Gdx.input.isButtonPressed(0)) {
				isSelected = true;
			}
		}

		if (enterJustPushed) {
			enterJustPushed = isSelected || Gdx.input.isButtonPressed(0);
			return false;
		} else {
			enterJustPushed = isSelected || Gdx.input.isButtonPressed(0);
			return isSelected;
		}
	}


	/**
	 * Update the selected property based on keys/controller up/down input or location of mouse.
	 */
	public void updateSelected() {
		float upState = controlMap.getStateForAction(Action.WALK_UP, controller);
		float downState = controlMap.getStateForAction(Action.WALK_DOWN, controller);

		int directionToMove = 0;
		if (upState > 0) {
			directionToMove -= 1;
		}
		if (downState > 0) {
			directionToMove += 1;
		}

		if (!justMoved) {
			moveSelected(directionToMove);
		}

		justMoved = (directionToMove != 0);
		int over = getMouseOverButton();
		if (over >= 0) {
			selected = over;
		}
	}


	/**
	 * Returns -1 if the mouse isn't over a button, else returns the index of the button.
	 *
	 * @return
	 */
	private int getMouseOverButton() {
		float originalMouseX = Gdx.input.getX();
		float originalMouseY = Gdx.input.getY();

		Vector3 posInWorld3 = guiCamera.unproject(new Vector3(originalMouseX, originalMouseY, 0));

		float mouseX = posInWorld3.x;
		float mouseY = posInWorld3.y;

		if ((mouseY <= topLeftY) && (mouseX >= topLeftX) && (mouseX <= (topLeftX + buttonWidth))) {
			int over = 0;
			int testY = topLeftY;
			boolean overButton = false;

			while (over < buttonTexts.length) {
				testY -= buttonHeight;
				if (testY <= mouseY) {
					overButton = true;
					break;
				}
				over += 1;
				testY -= buttonDifference;
				if (testY < mouseY) {
					overButton = false;
					break;
				}
			}

			if (overButton) {
				return over;
			}
		}
		return -1;
	}


	/**
	 * Increments or decrements selected by amount, performing wrap-around if necessary
	 *
	 * @param amount
	 */
	private void moveSelected(int amount) {
		selected += amount;

		int buttons = buttonTexts.length;

		while (selected < 0) {
			selected = buttons + selected;
		}
		while (selected >= buttons) {
			selected = selected - buttons;
		}
	}


	/**
	 * Renders the button list onto the batch.
	 *
	 * @param batch
	 */
	public void render(SpriteBatch batch) {
		
		//Button sprite's
		batch.setProjectionMatrix(guiCamera.combined);
		batch.begin();
		batch.enableBlending();
		
		int y = topLeftY - buttonHeight;
		
		for (int i = 0; i < buttonTexts.length; i += 1) {
			Texture button = new Texture(Gdx.files.internal(AssetLocations.GAME_BUTTON));
			Texture buttonSelected = new Texture(Gdx.files.internal(AssetLocations.GAME_BUTTON_SELECT));
			
			if (selected == i) {
				sprite = new Sprite(buttonSelected);
			} else {
				sprite = new Sprite(button);
			}
			
			sprite.setSize(buttonWidth,buttonHeight);
			sprite.setPosition(topLeftX, y);
			sprite.draw(batch);
			
			y -= sprite.getHeight();
			y -= buttonDifference;
			
		}
		batch.end();

		//Text on button
		batch.setProjectionMatrix(guiCamera.combined);
		batch.begin();
		batch.enableBlending();
		
		y = topLeftY + buttonTextVerticalOffset - (2*buttonHeight/3) ;

		for (int i = 0; i < buttonTexts.length; i += 1) {
			String text = buttonTexts[i];
			GlyphLayout layout = buttonLayouts[i];

			if (selected == i) {
				font.setColor(selectedColour);				
			} else {
				font.setColor(deselectedColour);
			}

			int x = (int) ((topLeftX + halfButtonWidth) - (layout.width / 2));
			font.draw(batch, text, x, y);

			y -= buttonDifference;
			y -= buttonHeight;
		}
		batch.end();
		
	}
}