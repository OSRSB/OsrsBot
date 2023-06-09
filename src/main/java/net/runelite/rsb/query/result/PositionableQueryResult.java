package net.runelite.rsb.query.result;

import net.runelite.rsb.wrappers.RSTile;
import net.runelite.rsb.wrappers.common.Positionable;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import static net.runelite.rsb.methods.MethodProvider.methods;

public class PositionableQueryResult<T extends Positionable> extends QueryResult<T> {
    public PositionableQueryResult(List<T> list) {
        super(list);
    }

    public PositionableQueryResult<T> sort(RSTile tile) {
        sort(Comparator.comparingDouble(positionable -> {
            return positionable.getLocation().distanceTo(tile);
        }));
        return this;
    }

    public T nearest() {
        return nearest(100.0, 0.0);
    }

    public T nearest(double near, double diff) {
        shuffle();
        double oldDistance = Double.MAX_VALUE;
        Positionable oldPositionable = null;
        Iterator iterator = iterator();

        while(iterator.hasNext()) {
            Positionable newPositionable = (Positionable)iterator.next();
            double newDistance = methods.calc.distanceTo(newPositionable.getLocation());
            if (oldPositionable == null) {
                oldPositionable = newPositionable;
                oldDistance = newDistance;
            } else if (newDistance <= near) {
                if (oldDistance > near || newDistance <= oldDistance + diff) {
                    oldPositionable = newPositionable;
                    oldDistance = newDistance;
                }
            } else if (newDistance < oldDistance) {
                oldPositionable = newPositionable;
                oldDistance = newDistance;
            }
        }

        return (T)oldPositionable;
    }

    public List<T> nearestList() {
        sort(Comparator.comparingDouble((positional) -> {
            return methods.calc.distanceTo(positional.getLocation());
        }));
        return toList();
    }
}
