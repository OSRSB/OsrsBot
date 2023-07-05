package net.runelite.rsb.wrappers;

import net.runelite.api.Actor;
import net.runelite.api.MenuEntry;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.rsb.methods.MethodContext;

import java.lang.ref.SoftReference;

/**
 * Represents a player.
 */
public class RSPlayer extends RSCharacter {

	private final SoftReference<Player> p;

	public RSPlayer(final MethodContext ctx, final Player p) {
		super(ctx);
		this.p = new SoftReference<>(p);
	}

	public Actor getAccessor() {
		return p.get();
	}

	public Actor getInteracting() {
		Actor interacting = getAccessor().getInteracting();
		if (interacting != null) {
			return getAccessor().getInteracting();
		}
		return null;
	}

	public int getCombatLevel() {
		return p.get().getCombatLevel();
	}

	public boolean isLocalPlayerMoving() {
		if (methods.client.getLocalDestinationLocation() != null) {
			return methods.client.getLocalPlayer().getLocalLocation() == methods.client.getLocalDestinationLocation();
		}
		return false;
	}

	public boolean isMoving() {
		var poseAnimation = getAccessor().getPoseAnimation();
		return isLocalPlayerMoving()
				|| poseAnimation == getAccessor().getWalkRotate180()
				|| poseAnimation == getAccessor().getWalkRotateLeft()
				|| poseAnimation == getAccessor().getWalkRotateRight()
				|| poseAnimation == getAccessor().getRunAnimation()
				|| poseAnimation == getAccessor().getWalkAnimation();
  }

	@Override
	public String getName() {
		return p.get().getName();
	}

	public int getTeam() {
		return p.get().getTeam();
	}

	public boolean isIdle() {
		return getAnimation() == -1 && !isInCombat();
	}

	@Override
	public boolean doAction(final String action) {
		return doAction(action, getName());
	}

	@Override
	public boolean doAction(final String action, final String target) {
		final RSModel model = getModel();
		if (model != null && isValid()) {
			return model.doAction(action, target);
		}
		try {
			Point screenLoc;
			for (int i = 0; i < 20; i++) {
				screenLoc = getScreenLocation();
				if (!isValid() || !methods.calc.pointOnScreen(screenLoc)) {
					return false;
				}
				if (methods.mouse.getLocation().equals(screenLoc)) {
					break;
				}
				methods.mouse.move(screenLoc);
			}
			MenuEntry[] entries = methods.menu.getEntries();
			if (entries.length <= 1) {
				return false;
			}
			if (entries[0].getOption().toLowerCase().contains(action.toLowerCase())) {
				methods.mouse.click(true);
				return true;
			} else {
				methods.mouse.click(false);
				return methods.menu.doAction(action, target);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public String toString() {
		return "Player[" + getName() + "]" + super.toString();
	}

	public RSTile getPosition() {
		return getLocation();
	}
}