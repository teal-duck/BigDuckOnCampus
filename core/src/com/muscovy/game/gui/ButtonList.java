package com.muscovy.game.gui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.muscovy.game.AssetLocations;
import com.muscovy.game.TextureMap;
import com.muscovy.game.input.Action;
import com.muscovy.game.input.ControlMap;


/**
 * Renders and provides logic for a list of GUI buttons for use in menus.
 */
public class ButtonList {
	private String[] buttonTexts;
	private Button[] buttons;

	private ControlMap controlMap;
	private Controller controller;

	private int selected = 0;
	private boolean justMoved = true;
	private boolean enterJustPushed = true;

	// Default button sizes
	// Used for consistency between menus
	// But setDimensions() allows you to change the sizes for a specific button list
	public static final int BUTTON_WIDTH = 350;
	public static final int BUTTON_HEIGHT = 80;
	public static final int BUTTON_DIFFERENCE = 20;
	public static final int BUTTON_TEXT_VERTICAL_OFFSET = 55;
	public static final int WINDOW_EDGE_OFFSET = 32;


	/**
	 * @param buttonTexts
	 * @param font
	 * @param guiCamera
	 * @param controlMap
	 * @param controller
	 */
	public ButtonList(String[] buttonTexts, BitmapFont font, TextureMap textureMap, ControlMap controlMap,
			Controller controller) {
		this.buttonTexts = buttonTexts;
		this.controlMap = controlMap;
		this.controller = controller;

		Texture deselectedTexture = textureMap.getTextureOrLoadFile(AssetLocations.GAME_BUTTON);
		Texture selectedTexture = textureMap.getTextureOrLoadFile(AssetLocations.GAME_BUTTON_SELECT);

		buttons = new Button[buttonTexts.length];
		for (int i = 0; i < buttonTexts.length; i += 1) {
			buttons[i] = new Button(buttonTexts[i], 0, 0, 0, 0, 0, font, selectedTexture,
					deselectedTexture);
		}

		setDimensions(0, 0, ButtonList.BUTTON_WIDTH, ButtonList.BUTTON_HEIGHT, ButtonList.BUTTON_DIFFERENCE,
				ButtonList.BUTTON_TEXT_VERTICAL_OFFSET);
	}


	public int getButtonCount() {
		return buttons.length;
	}


	public Button getButtonAtIndex(int index) {
		if ((index >= 0) && (index < buttons.length)) {
			return buttons[index];
		} else {
			return null;
		}
	}


	public void changeTextOnButton(int index, String text) {
		Button button = getButtonAtIndex(index);
		if (button != null) {
			button.setText(text);
		}
	}


	public void changePositionOfButton(int index, float x, float y) {
		Button button = getButtonAtIndex(index);
		if (button != null) {
			button.setX(x);
			button.setY(y);
		}
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
	public void setDimensions(float topLeftX, float topLeftY, float width, float height, float yDifference,
			float textYOffset) {
		float y = topLeftY - height;
		for (Button button : buttons) {
			button.setX(topLeftX);
			button.setY(y);
			button.setWidth(width);
			button.setHeight(height);
			button.setTextYOffset(textYOffset);

			y -= height + yDifference;
		}
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
	 * @return
	 */
	public int getSelected() {
		return selected;
	}


	/**
	 * True if enter key/controller button/mouse is pressed whilst over a button. Only call this once per frame as
	 * it updates state for the previous frame.
	 *
	 * @return
	 */
	public boolean isSelectedSelected(OrthographicCamera camera) {
		boolean isSelected = false;

		float selectState = controlMap.getStateForAction(Action.ENTER, controller);
		if (selectState > 0) {
			isSelected = true;
		}

		if (getMouseOverButton(camera) == selected) {
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				isSelected = true;
			}
		}

		if (enterJustPushed) {
			enterJustPushed = isSelected || Gdx.input.isButtonPressed(Buttons.LEFT);
			return false;
		} else {
			enterJustPushed = isSelected || Gdx.input.isButtonPressed(Buttons.LEFT);
			return isSelected;
		}
	}


	/**
	 * Update the selected property based on keys/controller up/down input or location of mouse.
	 */
	public void updateSelected(OrthographicCamera camera) {
		float upState = controlMap.getStateForActions(Action.WALK_UP, Action.SHOOT_UP, controller);
		float downState = controlMap.getStateForActions(Action.WALK_DOWN, Action.SHOOT_DOWN, controller);

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
		int over = getMouseOverButton(camera);
		if (over >= 0) {
			selected = over;
		}

		for (int i = 0; i < buttons.length; i += 1) {
			buttons[i].setSelected(selected == i);
		}
	}


	/**
	 * Returns -1 if the mouse isn't over a button, else returns the index of the button.
	 *
	 * @return
	 */
	private int getMouseOverButton(OrthographicCamera camera) {
		float originalMouseX = Gdx.input.getX();
		float originalMouseY = Gdx.input.getY();

		Vector3 posInWorld3 = camera.unproject(new Vector3(originalMouseX, originalMouseY, 0));

		float mouseX = posInWorld3.x;
		float mouseY = posInWorld3.y;

		int overButton = -1;
		for (int i = 0; i < buttons.length; i += 1) {
			Button button = buttons[i];
			if (button.pointOnButton(mouseX, mouseY)) {
				overButton = i;
			}
		}

		return overButton;
	}


	/**
	 * Increments or decrements selected by amount, performing wrap-around if necessary
	 *
	 * @param amount
	 */
	private void moveSelected(int amount) {
		selected += amount;

		final int buttons = buttonTexts.length;

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
		for (Button button : buttons) {
			button.render(batch);
		}
	}
}