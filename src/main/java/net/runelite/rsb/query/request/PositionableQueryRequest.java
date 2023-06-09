package net.runelite.rsb.query.request;

import net.runelite.rsb.wrappers.RSArea;
import net.runelite.rsb.wrappers.common.Positionable;
import net.runelite.rsb.wrappers.RSTile;

import java.util.ArrayList;
import java.util.List;

import static net.runelite.rsb.methods.MethodProvider.methods;

public abstract class PositionableQueryRequest<T extends Positionable> extends QueryRequest<T> {
    private int distance;
    private List<RSArea> within;
    private List<RSArea> notWithin;
    private List<Positionable> located;
    private List<Positionable> notLocated;

    public PositionableQueryRequest(int distance) {
        super();
        distance = distance;
        located = new ArrayList();
        notLocated = new ArrayList();
        within = new ArrayList();
        notWithin = new ArrayList();
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        distance = Math.max(0, (int)Math.ceil(distance));
    }
    public List<RSArea> getWithin() {
        return within;
    }
    public List<RSArea> getNotWithin() {
        return notWithin;
    }
    public List<Positionable> getLocated() {
        return located;
    }

    public List<Positionable> getNotLocated() {
        return notLocated;
    }

    public void within(RSArea... areas) {
        if (areas != null) {
            for (RSArea area : areas) {
                if (area != null && !within.contains(area)) {
                    within.add(area);
                }
            }
        }
    }
    public void notWithin(RSArea... areas) {
        if (areas != null) {
            for (RSArea area : areas) {
                if (area != null && !notWithin.contains(area)) {
                    notWithin.add(area);
                }
            }
        }
    }
    public void located(Positionable... positionables) {
        if (positionables != null) {
            for (Positionable positionable : positionables) {
                if (positionable != null && !located.contains(positionable)) {
                    located.add(positionable);
                }
            }
        }
    }
    public void notLocated(Positionable... positionables) {
        if (positionables != null) {
            for (Positionable positionable : positionables) {
                if (positionable != null && !notLocated.contains(positionable)) {
                    notLocated.add(positionable);
                }
            }
        }
    }

    public boolean accepts(int i, int j, int k) {
        if (methods.calc.distanceTo(i, j, k) > distance)
            return false;
        if (!this.located.isEmpty() && this.located.stream().noneMatch(locatable -> (locatable.getLocation().getX() == i && locatable.getLocation().getY() == j)))
            return false;
        if (!this.notLocated.isEmpty() && this.notLocated.stream().anyMatch(locatable -> (locatable.getLocation().getX() == i && locatable.getLocation().getY() == j && (k == -1 || locatable.getLocation().getPlane() == k))))
            return false;
        if (!this.within.isEmpty() && this.within.stream().noneMatch(area -> area.contains(i, j)))
            return false;
        if (!this.notWithin.isEmpty() && this.notWithin.stream().anyMatch(area -> area.contains(i, j)))
            return false;
        return true;
    }

    public boolean accepts(Positionable positionable) {
        RSTile tile = positionable.getLocation();
        return accepts(tile.getX(), tile.getY(), tile.getPlane()) && super.accepts(positionable);
    }
}
