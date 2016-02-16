package com.muscovy.game.entity;


import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.muscovy.game.MuscovyGame;
import com.muscovy.game.level.DungeonRoom;


/**
 * Project URL : http://teal-duck.github.io/teal-duck
 *
 * Base class for all collidable entities. Provides methods to help perform collision calculations.
 */
public abstract class Collidable extends OnscreenDrawable {
	private Circle circleHitbox;
	private Rectangle rectangleHitbox;

	private int widthTiles;
	private int heightTiles;

	// Offset from centre of collidable
	private float hitboxYOffset = 0;

	
	public Collidable(MuscovyGame game, String textureName, Vector2 position) {
		this(game, textureName, position, OnscreenDrawable.DEFAULT_ENTITY_WIDTH, OnscreenDrawable.DEFAULT_ENTITY_HEIGHT);
	}
	

	public Collidable(MuscovyGame game, String textureName, Vector2 position, int width, int height) {
		super(game, textureName, position, width, height);
		setUpBoxes();
	}
	

	/**
	 * Initialises circle and rectangle hitboxes based on current x and y, width and height. Circle hitbox
	 * automatically has radius of width/2. Rectangle hitbox automatically same size as the sprite image.
	 */
	public void setUpBoxes() {
		float x = getX();
		float y = getY();
		float width = getWidth();
		float height = getHeight();

		widthTiles = (int) Collidable.floorDiv((long) width, game.getTileSize());
		heightTiles = (int) Collidable.floorDiv((long) height, game.getTileSize());
		circleHitbox = new Circle(x + (width / 2), y + (height / 2) + hitboxYOffset, width / 2);
		rectangleHitbox = new Rectangle(x, y, width, height);
	}


	/**
	 * Sets the x co-ordinate for the entity and updates the hitbox accordingly.
	 *
	 * @param x
	 *                {@inheritDoc}
	 */
	@Override
	public void setX(float x) {
		super.setX(x);
		updateBoxesPosition();
	}


	/**
	 * Sets the y co-ordinate for the entity and updates the hitbox accordingly.
	 *
	 * @param y
	 *                {@inheritDoc}
	 */
	@Override
	public void setY(float y) {
		super.setY(y);
		updateBoxesPosition();
	}


	/**
	 * Sets the position for the entity and updates the hitbox accordingly.
	 *
	 * @param position
	 *                {@inheritDoc}
	 */
	@Override
	public void setPosition(Vector2 position) {
		super.setPosition(position);
		updateBoxesPosition();
	}


	/*
	 * setXTiles and setYTiles moves the collidable to fit on the grid directly. Clamps to walls of dungeon room,
	 * assuming a 64x64 collidable.
	 * Useful for placing stuff in the dungeon rooms
	 */

	/**
	 * Use this when setting something in the playable space to make sure it is on the grid. Sets the entity's x
	 * co-ordinate to place it on the given tile, clamping the value to ensure that it is placed within the room's
	 * walls.
	 *
	 * @param xTiles
	 */
	public void setXTiles(int xTiles) {
		if (xTiles > (DungeonRoom.FLOOR_WIDTH_IN_TILES - widthTiles)) {
			xTiles = DungeonRoom.FLOOR_WIDTH_IN_TILES - widthTiles;
		}

		setX(((xTiles + 1) * game.getTileSize()) + game.getWallEdge());
	}


	/**
	 * Use this when setting something in the playable space to make sure it is on the grid. Sets the entity's y
	 * co-ordinate to place it on the given tile, clamping the value to ensure that it is placed within the room's
	 * walls.
	 *
	 * @param yTiles
	 */
	public void setYTiles(int yTiles) {
		if (yTiles > (DungeonRoom.FLOOR_HEIGHT_IN_TILES - heightTiles)) {
			yTiles = DungeonRoom.FLOOR_HEIGHT_IN_TILES - heightTiles;
		}

		setY(((yTiles + 1) * game.getTileSize()) + game.getWallEdge());
	}


	/**
	 * Places the entity such that its hitbox is located at the given co-ordinates.
	 *
	 * @param x
	 * @param y
	 */
	public void setHitboxCentre(float x, float y) {
		setX((x - circleHitbox.radius) - ((getWidth() / 2) - circleHitbox.radius));
		setY((y - circleHitbox.radius) - ((getHeight() / 2) - circleHitbox.radius) - hitboxYOffset);
	}


	/**
	 * Raises the position of the hitbox with regard to the sprite. Use to align hitbox with sprite.
	 *
	 * @param hitboxYOffset
	 */
	public void setHitboxYOffset(float hitboxYOffset) {
		this.hitboxYOffset = hitboxYOffset;
	}


	/**
	 * @param radius
	 *                Length of radius for circle hitbox in pixels.
	 */
	public void setHitboxRadius(float radius) {
		circleHitbox.setRadius(radius);
	}


	/**
	 * @return Instance of the rectangle hitbox.
	 */
	public Rectangle getRectangleHitbox() {
		return rectangleHitbox;
	}


	/**
	 * @return Instance of the circle hitbox.
	 */
	public Circle getCircleHitbox() {
		return circleHitbox;
	}


	/*
	 * Collision Methods
	 */
	/**
	 * Calculates angle between the centre of the circles, and calculates the x & y distance needed so the centres
	 * are this radius + collidable radius away. (MOVES THE CALLING ENTITY AWAY FROM THE GIVEN COLLIDABLE)
	 *
	 * @param collidable
	 */
	public void moveToNearestEdgeCircle(Collidable collidable) {
		float angle = getAngleFrom(collidable);
		float distance = collidable.getCircleHitbox().radius + circleHitbox.radius;

		if (getCircleHitbox().x >= collidable.getCircleHitbox().x) {
			if (getCircleHitbox().y >= collidable.getCircleHitbox().y) {

			} else {
				angle = (float) Math.PI - angle;
			}
		} else if (getCircleHitbox().x < collidable.getCircleHitbox().x) {
			if (getCircleHitbox().y >= collidable.getCircleHitbox().y) {
				angle = (float) (angle - Math.PI);
			} else {
				angle = (float) ((2 * Math.PI) - angle);
			}
		}

		float x = collidable.getCircleHitbox().x;
		float y = collidable.getCircleHitbox().y;
		float distanceSinAngle = distance * MathUtils.sin(angle);
		float distanceCosAngle = distance * MathUtils.cos(angle);

		if (getCircleHitbox().x >= collidable.getCircleHitbox().x) {
			x += distanceSinAngle;
			if (getCircleHitbox().y >= collidable.getCircleHitbox().y) {
				y += distanceCosAngle;

			} else {
				y -= distanceCosAngle;
			}
		} else if (getCircleHitbox().x < collidable.getCircleHitbox().x) {
			x -= distanceSinAngle;
			if (getCircleHitbox().y >= collidable.getCircleHitbox().y) {
				y -= distanceCosAngle;
			} else {
				y += distanceCosAngle;
			}
		}

		setHitboxCentre(x, y);
	}


	/**
	 * Checks where the centre of the circle of the other collidable is in relation to the rectangle, and moves it
	 * accordingly. (MOVES THE CALLING ENTITY AWAY FROM THE GIVEN COLLIDABLE)
	 *
	 * @param collidable
	 */
	public void moveToNearestEdgeRectangle(Collidable collidable) {
		float angle = getAngleFrom(collidable);
		float thisX = getCircleHitbox().x;
		float thisY = getCircleHitbox().y;
		float thatX = collidable.getCircleHitbox().x;
		float thatY = collidable.getCircleHitbox().y;
		float x, y;
		if ((thisX > collidable.getX()) && (thisX < (collidable.getX() + collidable.getWidth()))
				&& (thisY > thatY)) {
			y = collidable.getY() + collidable.getRectangleHitbox().getHeight() + circleHitbox.radius + 1;
			setHitboxCentre(thisX, y);
		} else if ((thisX > collidable.getX()) && (thisX < (collidable.getX() + collidable.getWidth()))
				&& (thisY < thatY)) {
			y = collidable.getY() - circleHitbox.radius;
			setHitboxCentre(thisX, y);
		} else if ((thisY > collidable.getY()) && (thisY < (collidable.getY() + collidable.getHeight()))
				&& (thisX > thatX)) {
			x = collidable.getRectangleHitbox().getX() + collidable.getRectangleHitbox().getWidth()
					+ circleHitbox.radius;
			setHitboxCentre(x, thisY);
		} else if ((thisY > collidable.getY()) && (thisY < (collidable.getY() + collidable.getHeight()))
				&& (thisX < thatX)) {
			x = collidable.getRectangleHitbox().getX() - circleHitbox.radius;
			setHitboxCentre(x, thisY);
		} else if ((thisY < collidable.getRectangleHitbox().getY())
				&& (thisX < collidable.getRectangleHitbox().getX())) {
			angle = (float) Math.atan((thisY - collidable.getRectangleHitbox().getY())
					/ (thisX - collidable.getRectangleHitbox().getX()));
			y = collidable.getRectangleHitbox().getY() - (float) (circleHitbox.radius * Math.sin(angle));
			x = collidable.getRectangleHitbox().getX() - (float) (circleHitbox.radius * Math.cos(angle));
			setHitboxCentre(x, y);
		} else if ((thisY < collidable.getRectangleHitbox().getY())
				&& (thisX > (collidable.getRectangleHitbox().getX()
						+ collidable.getRectangleHitbox().getWidth()))) {
			angle = (float) Math.atan((collidable.getRectangleHitbox().getY() - thisY)
					/ (thisX - (collidable.getRectangleHitbox().getX()
							+ collidable.getRectangleHitbox().getWidth())));
			y = collidable.getRectangleHitbox().getY() - (float) (circleHitbox.radius * Math.sin(angle));
			x = collidable.getRectangleHitbox().getX() + collidable.getRectangleHitbox().getWidth()
					+ (float) (circleHitbox.radius * Math.cos(angle));
			setHitboxCentre(x, y);
		} else if ((thisY > (collidable.getRectangleHitbox().getY()
				+ collidable.getRectangleHitbox().getHeight()))
				&& (thisX > (collidable.getRectangleHitbox().getX()
						+ collidable.getRectangleHitbox().getWidth()))) {
			angle = (float) Math.atan((thisY - (collidable.getRectangleHitbox().getY()
					+ collidable.getRectangleHitbox().getHeight()))
					/ (thisX - (collidable.getRectangleHitbox().getX()
							+ collidable.getRectangleHitbox().getWidth())));
			y = collidable.getRectangleHitbox().getY() + collidable.getRectangleHitbox().getHeight()
					+ (float) (circleHitbox.radius * Math.sin(angle));
			x = collidable.getRectangleHitbox().getX() + collidable.getRectangleHitbox().getWidth()
					+ (float) (circleHitbox.radius * Math.cos(angle));
			setHitboxCentre(x, y);
		} else if ((thisY > (collidable.getRectangleHitbox().getY()
				+ collidable.getRectangleHitbox().getHeight()))
				&& (thisX < collidable.getRectangleHitbox().getX())) {
			angle = (float) Math.atan((thisY - (collidable.getRectangleHitbox().getY()
					+ collidable.getRectangleHitbox().getHeight()))
					/ (collidable.getRectangleHitbox().getX() - thisX));
			y = collidable.getRectangleHitbox().getY() + collidable.getRectangleHitbox().getHeight()
					+ (float) (circleHitbox.radius * Math.sin(angle));
			x = collidable.getRectangleHitbox().getX() - (float) (circleHitbox.radius * Math.cos(angle));
			setHitboxCentre(x, y);
		}
	}


	/*
	 * Angle methods use trig to calculate angles, then it's position to calculate the angle clockwise from the
	 * vertical
	 */
	// TODO: Use Math.atan2 or vector.angleRad()
	/**
	 * @param collidable
	 * @return
	 */
	public float getAngleFrom(Collidable collidable) {
		float x = (getCircleHitbox().x - collidable.getCircleHitbox().x);
		float y = (getCircleHitbox().y - collidable.getCircleHitbox().y);
		float angle = (float) Math.atan(x / y); // MathUtils.atan2(y, x)
		if (x >= 0) {
			if (y >= 0) {
				return angle;
			} else if (y < 0) {
				return (float) (Math.PI + angle);
			}
		} else if (x < 0) {
			if (y >= 0) {
				return (float) ((2 * Math.PI) + angle);
			} else if (y < 0) {
				return (float) (Math.PI + angle);
			}
		}
		return angle;
	}


	/**
	 * @param collidable
	 * @return
	 */
	public float getAngleTo(Collidable collidable) {
		float x = (collidable.getCircleHitbox().x - circleHitbox.x);
		float y = (collidable.getCircleHitbox().y - circleHitbox.y);
		float angle = (float) Math.atan(x / y); // MathUtils.atan2(y, x);
		if (x >= 0) {
			if (y >= 0) {
				return angle;
			} else if (y < 0) {
				return (float) (Math.PI + angle);
			}
		} else if (x < 0) {
			if (y >= 0) {
				return (float) ((2 * Math.PI) + angle);
			} else if (y < 0) {
				return (float) (Math.PI + angle);
			}
		}
		return angle;
	}


	/**
	 * Sets the hitboxes' positions to the entity's current position.
	 */
	public void updateBoxesPosition() {
		circleHitbox.setX(getX() + (getWidth() / 2));
		circleHitbox.setY(getY() + (getHeight() / 2) + hitboxYOffset);
		rectangleHitbox.setX(getX());
		rectangleHitbox.setY(getY());
	}


	/**
	 * @param collidable
	 * @return True if the two entities are colliding.
	 */
	public boolean collides(Collidable collidable) {
		return Intersector.overlaps(circleHitbox, collidable.getCircleHitbox());
	}


	// http://stackoverflow.com/questions/27643616/ceil-conterpart-for-math-floordiv-in-java
	/**
	 * Originally this code used Java 8's Math.floorDiv. However for Java 7 compatibility, its implementation is
	 * included here.
	 *
	 * @return x/y rounded down to next long.
	 */
	public static long floorDiv(long x, long y) {
		long r = x / y;
		// if the signs are different and modulo not zero, round down
		if (((x ^ y) < 0) && ((r * y) != x)) {
			r--;
		}
		return r;
	}
}