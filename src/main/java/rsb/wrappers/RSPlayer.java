package rsb.wrappers;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import rsb.methods.MethodContext;
import rsb.util.OutputObjectComparer;

import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * Represents a player.
 */
public class RSPlayer extends RSCharacter {

	private final SoftReference<Player> p;

	public RSPlayer(final MethodContext ctx, final Player p) {
		super(ctx);
		this.p = new SoftReference<Player>(p);
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

	@Override
	public String getName() {
		return p.get().getName();
	}

	public int getTeam() {
		return p.get().getTeam();
	}

	/*
	public int getNPCID() {
		PlayerComposition comp = p.get().getPlayerComposition();
		if (comp != null) {
			return comp.getTransformedNpcId();
		}
		return -1;
	}
*/
	public boolean isIdle() {

		return getAnimation() == -1 && isInCombat();
		//return !isMoving() && (getAnimation() == -1) && !isInCombat();
	}


	@Override
	public boolean doAction(final String action) {
		return doAction(action, null);
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