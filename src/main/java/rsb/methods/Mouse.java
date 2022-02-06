package rsb.methods;

import rsb.internal.MouseHandler;
import net.runelite.api.Point;

/**
 * Mouse related operations.
 */
public class Mouse extends MethodProvider {

	private int mouseSpeed = MouseHandler.DEFAULT_MOUSE_SPEED;

	Mouse(final MethodContext ctx) {
		super(ctx);
	}


	/**
	 * Author - Enfilade Moves the mouse a random distance between 1 and
	 * maxDistance from the current position of the mouse by generating a random
	 * vector and then multiplying it by a random number between 1 and
	 * maxDistance. The maximum distance is cut short if the mouse would go off
	 * screen in the direction it chose.
	 *
	 * @param maxDistance The maximum distance the cursor will move (exclusive)
	 */
	public void moveRandomly(final int maxDistance) {
		moveRandomly(1, maxDistance);
	}

	/**
	 * Author - Enfilade Moves the mouse a random distance between minDistance
	 * and maxDistance from the current position of the mouse by generating
	 * random vector and then multiplying it by a random number between
	 * minDistance and maxDistance. The maximum distance is cut short if the
	 * mouse would go off screen in the direction it chose.
	 *
	 * @param minDistance The minimum distance the cursor will move
	 * @param maxDistance The maximum distance the cursor will move (exclusive)
	 */
	public void moveRandomly(final int minDistance, final int maxDistance) {
		/* Generate a random vector for the direction the mouse will move in */
		double xvec = Math.random();
		if (random(0, 2) == 1) {
			xvec = -xvec;
		}
		double yvec = Math.sqrt(1 - xvec * xvec);
		if (random(0, 2) == 1) {
			yvec = -yvec;
		}
		/* Start the maximum distance at maxDistance */
		double distance = maxDistance;
		/* Get the current location of the cursor */
		Point p = getLocation();
		/* Calculate the x coordinate if the mouse moved the maximum distance */
		int maxX = (int) Math.round(xvec * distance + p.getX());
		/*
		 * If the maximum x is offscreen, subtract that distance/xvec from the
		 * maximum distance so the maximum distance will give a valid X
		 * coordinate
		 */
		distance -= Math.abs((maxX - Math.max(0,
				Math.min(methods.game.getWidth(), maxX)))
				/ xvec);
		/* Do the same thing with the Y coordinate */
		int maxY = (int) Math.round(yvec * distance + p.getY());
		distance -= Math.abs((maxY - Math.max(0,
				Math.min(methods.game.getHeight(), maxY)))
				/ yvec);
		/*
		 * If the maximum distance in the generated direction is too small,
		 * don't move the mouse at all
		 */
		if (distance < minDistance) {
			return;
		}
		/*
		 * With the calculated maximum distance, pick a random distance to move
		 * the mouse between maxDistance and the calculated maximum distance
		 */
		distance = random(minDistance, (int) distance);
		/* Generate the point to move the mouse to and move it there */
		move((int) (xvec * distance) + p.getX(), (int) (yvec * distance) + p.getY());
	}

	/**
	 * Moves the mouse off the screen in a random direction.
	 */
	public void moveOffScreen() {
		if (isPresent()) {
			switch (random(0, 4)) {
				case 0: // up
					move(random(-10, methods.game.getWidth() + 10),
							random(-100, -10));
					break;
				case 1: // down
					move(random(-10, methods.game.getWidth() + 10),
							methods.game.getHeight() + random(10, 100));
					break;
				case 2: // left
					move(random(-100, -10),
							random(-10, methods.game.getHeight() + 10));
					break;
				case 3: // right
					move(random(10, 100) + methods.game.getWidth(),
							random(-10, methods.game.getHeight() + 10));
					break;
			}
		}
	}

	/**
	 * Drag the mouse from the current position to a certain other position.
	 *
	 * @param x The x coordinate to drag to.
	 * @param y The y coordinate to drag to.
	 */
	public void drag(final int x, final int y) {
		methods.inputManager.dragMouse(x, y);
	}

	/**
	 * Drag the mouse from the current position to a certain other position.
	 *
	 * @param p The point to drag to.
	 * @see #drag(int, int)
	 */
	public void drag(final Point p) {
		drag(p.getX(), p.getY());
	}

	/**
	 * Clicks the mouse at its current location.
	 *
	 * @param leftClick <tt>true</tt> to left-click, <tt>false</tt>to right-click.
	 */
	public void click(final boolean leftClick) {
		click(leftClick, MouseHandler.DEFAULT_MAX_MOVE_AFTER);
	}

	public synchronized void click(final boolean leftClick,
	                               final int moveAfterDist) {
		methods.inputManager.clickMouse(leftClick);
		if (moveAfterDist > 0) {
			sleep(random(50, 350));
			Point pos = getLocation();
			move(pos.getX() - moveAfterDist, pos.getY() - moveAfterDist,
					moveAfterDist * 2, moveAfterDist * 2);
		}
	}

	/**
	 * Moves the mouse to a given location then clicks.
	 *
	 * @param x         x coordinate
	 * @param y         y coordinate
	 * @param leftClick <tt>true</tt> to left-click, <tt>false</tt>to right-click.
	 */
	public void click(final int x, final int y, final boolean leftClick) {
		click(x, y, 0, 0, leftClick);
	}

	/**
	 * Moves the mouse to a given location with given randomness then clicks.
	 *
	 * @param x         x coordinate
	 * @param y         y coordinate
	 * @param randX     x randomness (added to x)
	 * @param randY     y randomness (added to y)
	 * @param leftClick <tt>true</tt> to left-click, <tt>false</tt>to right-click.
	 * @see #move(int, int, int, int)
	 */
	public synchronized void click(final int x, final int y, final int randX,
	                               final int randY, final boolean leftClick) {
		move(x, y, randX, randY);
		sleep(random(50, 350));
		click(leftClick, MouseHandler.DEFAULT_MAX_MOVE_AFTER);
	}

	/**
	 * Moves the mouse to a given location with given randomness then clicks,
	 * then moves a random distance up to <code>afterOffset</code>.
	 *
	 * @param x             x coordinate
	 * @param y             y coordinate
	 * @param randX         x randomness (added to x)
	 * @param randY         y randomness (added to y)
	 * @param leftClick     <tt>true</tt> to left-click, <tt>false</tt>to right-click.
	 * @param moveAfterDist The maximum distance in pixels to move on both axes shortly
	 *                      after moving to the destination.
	 */
	public synchronized void click(final int x, final int y, final int randX,
	                               final int randY, final boolean leftClick, final int moveAfterDist) {
		move(x, y, randX, randY);
		sleep(random(50, 350));
		click(leftClick, moveAfterDist);
	}

	/**
	 * Moves the mouse to a given location then clicks.
	 *
	 * @param p         The point to click.
	 * @param leftClick <tt>true</tt> to left-click, <tt>false</tt>to right-click.
	 */
	public void click(final Point p, final boolean leftClick) {
		click(p.getX(), p.getY(), leftClick);
	}

	public void click(final Point p, final int x, final int y,
	                  final boolean leftClick) {
		click(p.getX(), p.getY(), x, y, leftClick);
	}

	/**
	 * Moves the mouse to a given location with given randomness then clicks,
	 * then moves a random distance up to <code>afterOffset</code>.
	 *
	 * @param p             The destination Point.
	 * @param x             x coordinate
	 * @param y             y coordinate
	 * @param leftClick     <tt>true</tt> to left-click, <tt>false</tt>to right-click.
	 * @param moveAfterDist The maximum distance in pixels to move on both axes shortly
	 *                      after moving to the destination.
	 */
	public void click(final Point p, final int x, final int y,
	                  final boolean leftClick, final int moveAfterDist) {
		click(p.getX(), p.getY(), x, y, leftClick, moveAfterDist);
	}

	/**
	 * Moves the mouse slightly depending on where it currently is and clicks.
	 */
	public void clickSlightly() {
		Point p = new Point(
				(int) (getLocation().getX() + (Math.random() * 50 > 25 ? 1 : -1)
						* (30 + Math.random() * 90)), (int) (getLocation()
						.getY() + (Math.random() * 50 > 25 ? 1 : -1)
						* (30 + Math.random() * 90)));
		if (p.getX() < 1 || p.getY() < 1 || p.getX() > 761 || p.getY() > 499) {
			clickSlightly();
			return;
		}
		click(p, true);
	}

	/**
	 * Gets the mouse speed.
	 *
	 * @return the current mouse speed.
	 * @see #setSpeed(int)
	 */
	public int getSpeed() {
		return mouseSpeed;
	}

	/**
	 * Changes the mouse speed
	 *
	 * @param speed The speed to move the mouse at. 4-10 is advised, 1 being the
	 *              fastest.
	 * @see #getSpeed()
	 */
	public void setSpeed(final int speed) {
		mouseSpeed = speed;
	}

	/**
	 * Moves mouse to location (x,y) at default speed.
	 *
	 * @param x x coordinate
	 * @param y y coordinate
	 * @see #move(int, int, int, int)
	 * @see #setSpeed(int)
	 */
	public void move(final int x, final int y) {
		move(x, y, 0, 0);
	}

	/**
	 * Moves the mouse to the specified point and then by a randomized offset at default speed.
	 *
	 * @param x     The x destination.
	 * @param y     The y destination.
	 * @param afterOffset The maximum distance in pixels to move on both axes shortly
	 *                    after moving to the destination.
	 * @see #move(int, int, int, int, int, int)
	 */
	public void move(final int x, final int y, final int afterOffset) {
		move(getSpeed(), x, y, 0, 0, afterOffset);
	}

	/**
	 * Moves the mouse to the specified point with a randomized variation at default speed.
	 *
	 * @param x     The x destination.
	 * @param y     The y destination.
	 * @param randX x-axis randomness (added to x).
	 * @param randY y-axis randomness (added to y).
	 * @see #move(int, int, int, int, int, int)
	 * @see #setSpeed(int)
	 */
	public void move(final int x, final int y, final int randX, final int randY) {
		move(getSpeed(), x, y, randX, randY, 0);
	}

	/**
	 * Moves the mouse to the specified point at a certain speed.
	 *
	 * @param speed The lower, the faster.
	 * @param x     The x destination.
	 * @param y     The y destination.
	 * @param randX x-axis randomness (added to x).
	 * @param randY y-axis randomness (added to y).
	 * @see #move(int, int, int, int, int, int)
	 */
	public void move(final int speed, final int x, final int y,
	                 final int randX, final int randY) {
		move(speed, x, y, randX, randY, 0);
	}

	/**
	 * Moves the mouse to the specified point at a certain speed with variance in the x and y, then moves a
	 * random distance up to <tt>afterOffset</tt>.
	 *
	 * @param speed       The lower, the faster.
	 * @param x           The x destination.
	 * @param y           The y destination.
	 * @param randX       X-axis randomness (added to x).
	 * @param randY       X-axis randomness (added to y).
	 * @param afterOffset The maximum distance in pixels to move on both axes shortly
	 *                    after moving to the destination.
	 */
	public synchronized void move(final int speed, final int x, final int y,
	                              final int randX, final int randY, final int afterOffset) {
		if (x != -1 || y != -1) {
			methods.inputManager.moveMouse(speed, x, y, randX, randY);
			if (afterOffset > 0) {
				sleep(random(60, 300));
				Point pos = getLocation();
				move(pos.getX() - afterOffset, pos.getY() - afterOffset, afterOffset * 2,
						afterOffset * 2);
			}
		}
	}

	/**
	 * Moves the mouse to the specified point at a certain speed
	 *
	 * @param speed       The lower, the faster.
	 * @param p           The x and y destination.
	 */
	public void move(final int speed, final Point p) {
		move(speed, p.getX(), p.getY(), 0, 0, 0);
	}

	/**
	 * Moves the mouse to the specified point
	 *
	 * @param p           The x and y destination.
	 */
	public void move(final Point p) {
		move(p.getX(), p.getY(), 0, 0);
	}

	/**
	 * Moves the mouse to the specified point then adds random distance up to <tt>afterOffset</tt>.
	 * @param p           The x and y destination.
	 * @param afterOffset The maximum distance in pixels to move on both axes shortly
	 *                    after moving to the destination.
	 *
	 */
	public void move(final Point p, final int afterOffset) {
		move(getSpeed(), p.getX(), p.getY(), 0, 0, afterOffset);
	}

	/**
	 * Moves the mouse to the specified point then adds random distance within to randX and randY
	 * @param p           The x and y destination.
	 * @param randX       X-axis randomness (added to x).
	 * @param randY       X-axis randomness (added to y).
	 */
	public void move(final Point p, final int randX, final int randY) {
		move(p.getX(), p.getY(), randX, randY);
	}

	/**
	 * Moves the mouse to the specified point at a certain speed with variance in the x and y, then moves a
	 * random distance up to <tt>afterOffset</tt>.
	 *
	 * @param p           The x and y destination.
	 * @param randX       X-axis randomness (added to x).
	 * @param randY       X-axis randomness (added to y).
	 * @param afterOffset The maximum distance in pixels to move on both axes shortly
	 *                    after moving to the destination.
	 */
	public void move(final Point p, final int randX, final int randY,
	                 final int afterOffset) {
		move(getSpeed(), p.getX(), p.getY(), randX, randY, afterOffset);
	}

	/**
	 * Hops mouse to the specified coordinate.
	 *
	 * @param x The x coordinate.
	 * @param y The y coordinate
	 */
	public synchronized void hop(final int x, final int y) {
		methods.inputManager.hopMouse(x, y);
	}

	/**
	 * Hops mouse to the specified point.
	 *
	 * @param p The coordinate point.
	 * @see #hop(Point)
	 */
	public void hop(final Point p) {
		hop(p.getX(), p.getY());
	}

	/**
	 * Hops mouse to the certain coordinate.
	 *
	 * @param x     The x coordinate.
	 * @param y     The y coordinate.
	 * @param randX The x coordinate randomization.
	 * @param randY The y coordinate randomization.
	 * @see #hop(int, int)
	 */
	public void hop(final int x, final int y, final int randX, final int randY) {
		hop(x + random(-randX, randX), y + random(-randX, randY));
	}

	/**
	 * Hops mouse to the certain point.
	 *
	 * @param p     The coordinate point.
	 * @param randX The x coordinate randomization.
	 * @param randY The y coordinate randomization.
	 * @see #hop(int, int, int, int)
	 */
	public void hop(final Point p, final int randX, final int randY) {
		hop(p.getX(), p.getY(), randX, randY);
	}

	/**
	 * Moves the mouse slightly depending on where it currently is.
	 */
	public void moveSlightly() {
		Point p = new Point(
				(int) (getLocation().getX() + (Math.random() * 50 > 25 ? 1 : -1)
						* (30 + Math.random() * 90)), (int) (getLocation()
						.getY() + (Math.random() * 50 > 25 ? 1 : -1)
						* (30 + Math.random() * 90)));
		if (p.getX() < 1 || p.getY() < 1 || p.getX() > 761 || p.getY() > 499) {
			moveSlightly();
			return;
		}
		move(p);
	}

	/**
	 * @param maxDistance The maximum distance outwards.
	 * @return A random x value between the current client location and the max
	 *         distance outwards.
	 */
	public int getRandomX(final int maxDistance) {
		Point p = getLocation();
		if (p.getX() < 0 || maxDistance <= 0) {
			return -1;
		}
		if (random(0, 2) == 0) {
			return p.getX() - random(0, p.getX() < maxDistance ? p.getX() : maxDistance);
		} else {
			int dist = methods.game.getWidth() - p.getX();
			return p.getX()
					+ random(1, dist < maxDistance && dist > 0 ? dist
					: maxDistance);
		}
	}

	/**
	 * @param maxDistance The maximum distance outwards.
	 * @return A random y value between the current client location and the max
	 *         distance outwards.
	 */
	public int getRandomY(final int maxDistance) {
		Point p = getLocation();
		if (p.getY() < 0 || maxDistance <= 0) {
			return -1;
		}
		if (random(0, 2) == 0) {
			return p.getY() - random(0, p.getY() < maxDistance ? p.getY() : maxDistance);
		} else {
			int dist = methods.game.getHeight() - p.getY();
			return p.getY()
					+ random(1, dist < maxDistance && dist > 0 ? dist
					: maxDistance);
		}
	}

	/**
	 * The location of the bot's mouse; or Point(-1, -1) if off screen.
	 *
	 * @return A <tt>Point</tt> containing the bot's mouse's x and y coordinates.
	 */
	public Point getLocation() {
		return new Point(methods.virtualMouse.getClientX(), methods.virtualMouse.getClientY());
	}

	/**
	 * @return The <tt>Point</tt> at which the bot's mouse was last clicked.
	 */
	public Point getPressLocation() {
		return new Point(methods.virtualMouse.getClientPressX(), methods.virtualMouse.getClientPressY());
	}

	/**
	 * @return The system time when the bot's mouse was last pressed.
	 */
	public long getPressTime() {
		return methods.virtualMouse.getClientPressTime();
	}

	/**
	 * @return <tt>true</tt> if the bot's mouse is present.
	 */
	public boolean isPresent() {
		return methods.virtualMouse.isClientPresent();
	}

	/**
	 * @return <tt>true</tt> if the bot's mouse is pressed.
	 */
	public boolean isPressed() {
		return methods.virtualMouse.isClientPressed();
	}

}
