package rsb.wrappers;

import net.runelite.api.Actor;
import net.runelite.api.Model;
import rsb.methods.Calculations;
import rsb.methods.MethodContext;

/**
 * @author GigiaJ
 */
public class RSCharacterModel extends RSModel {

	private final Actor c;

	private final int[] x_base, z_base;

	RSCharacterModel(MethodContext ctx, Model model, Actor c) {
		super(ctx, model);
		this.c = c;
		x_base = xPoints;
		z_base = zPoints;
		xPoints = new int[xPoints.length];
		zPoints = new int[zPoints.length];
	}

	/**
	 * Performs a y rotation camera transform, where
	 * the character's orientation is the rotation around
	 * the y axis in fixed point radians.
	 * <p/>
	 * [cos(t), 0, sin(t)
	 * 0, 1, 0
	 * -sin(t), 0, cos(t)]
	 */
	@Override
	protected void update() {
		int theta = c.getOrientation() & 0x3fff;
		int sin = Calculations.SIN_TABLE[theta];
		int cos = Calculations.COS_TABLE[theta];
		for (int i = 0; i < x_base.length; ++i) {
			// Note that the second row of the matrix would result
			// in no change, as the y coordinates are always unchanged
			// by rotation about the y axis.
			xPoints[i] = x_base[i] * cos + z_base[i] * sin >> 15;
			zPoints[i] = z_base[i] * cos - x_base[i] * sin >> 15;
		}
	}

	@Override
	protected int getLocalX() {
		return c.getLocalLocation().getX();
	}

	@Override
	protected int getLocalY() {
		return c.getLocalLocation().getY();
	}

	@Override
	public int getOrientation() {return c.getOrientation();}
}
