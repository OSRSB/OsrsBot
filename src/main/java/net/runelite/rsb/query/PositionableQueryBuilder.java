package net.runelite.rsb.query;


import net.runelite.rsb.query.request.PositionableQueryRequest;
import net.runelite.rsb.query.result.PositionableQueryResult;
import net.runelite.rsb.query.result.QueryResult;
import net.runelite.rsb.wrappers.RSArea;
import net.runelite.rsb.wrappers.RSTile;
import net.runelite.rsb.wrappers.common.Positionable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static net.runelite.rsb.methods.MethodProvider.methods;

public abstract class PositionableQueryBuilder<T extends Positionable, Q extends PositionableQueryBuilder<T, Q, R, RQ>, R extends PositionableQueryResult<T>, RQ extends PositionableQueryRequest<T>> extends AbstractQueryBuilder<T, Q, R, RQ> {
    public PositionableQueryBuilder() {
        super();
    }

    public Q distance(double maximum) {
        getRequest().setDistance(maximum);
        return filter((positionable) -> {
            return methods.calc.distanceTo(positionable) <= maximum;
        });
    }

    public Q distance(double minimum, double maximum) {
        ((PositionableQueryRequest)getRequest()).setDistance(maximum);
        return filter((positionable) -> {
            double distance = methods.calc.distanceTo(positionable);
            return distance >= minimum && distance <= maximum;
        });
    }

    public final Q distance(Positionable to, double maximum) {
        return filter((from) -> {
            return methods.calc.distanceBetween(from.getLocation(), to.getLocation()) <= maximum;
        });
    }

    public final Q distance(Positionable to, double minimum, double maximum) {
        return filter((from) -> {
            double distance = methods.calc.distanceBetween(from.getLocation(), to.getLocation());
            return distance >= minimum && distance <= maximum;
        });
    }

    public final Q within(RSArea... areas) {
        if (areas == null || Arrays.stream(areas).allMatch((x) -> x == null)) return (Q)this;
        getRequest().within(areas);
        return filter(positionable -> {
            for (RSArea area : areas) {
                if (area.contains(positionable.getLocation())) {
                    return true;
                }
            }
            return false;
        });
    }
    public final Q notWithin(RSArea... areas) {
        if (areas == null || Arrays.stream(areas).allMatch((x) -> x == null)) return (Q)this;
        getRequest().notWithin(areas);
        return filter(positionable -> {
            for (RSArea area : areas) {
                if (area.contains(positionable.getLocation())) {
                    return false;
                }
            }
            return true;
        });
    }


    public final Q located(Positionable... positionables) {
        if (positionables == null || Arrays.stream(positionables).allMatch((x) -> x == null)) return (Q)this;
        getRequest().located(positionables);
        return filter(location -> {
            for (Positionable positionable : positionables) {
                if (location.getLocation().distanceTo(positionable) < 1) {
                    return true;
                }
            }
            return false;
        });
    }

    public final Q notLocated(Positionable... positionables) {
        if (positionables == null || Arrays.stream(positionables).allMatch((x) -> x == null)) return (Q)this;
        getRequest().notLocated(positionables);
        return filter(location -> {
            for (Positionable positionable : positionables) {
                if (location.getLocation().distanceTo(positionable) < 1) {
                    return false;
                }
            }
            return true;
        });
    }

    public PositionableQueryBuilder reachable() {
        return filter((positionable) -> {
            return methods.calc.canReach(positionable.getLocation(), false);
        });
    }

    public PositionableQueryBuilder unreachable() {
        return filter((positionable) -> {
            return !methods.calc.canReach(positionable.getLocation(), false);
        });
    }

    public R results() {
        return (R) new PositionableQueryResult(requestAsList().stream().filter(this::accepts).collect(Collectors.toList()));
    }

/* Result Functions */


    public PositionableQueryResult<T> sort(RSTile tile) {
        return results().sort(tile);
    }

    public T nearest() {
        return (T)results().nearest(100.0, 0.0);
    }

    public T nearest(double near, double diff) {
        return (T)results().nearest(near, diff);
    }

    public List<T> nearestList() {
        return results().nearestList();
    }

}
