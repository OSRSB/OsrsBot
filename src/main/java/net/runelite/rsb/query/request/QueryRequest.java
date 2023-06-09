package net.runelite.rsb.query.request;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public abstract class QueryRequest<T> {
    private List<Predicate<T>> filters;

    public abstract List<T> request();

    public QueryRequest() {
        filters = new ArrayList();
    }

    public void filter(Predicate<T> predicate) {
        filters.add(predicate);
    }

    public boolean accepts(Object o) {
        Iterator iterator = filters.iterator();

        do {
            if (!iterator.hasNext()) {
                return true;
            }
        } while(((Predicate)iterator.next()).test(o));

        return false;
    }
}
