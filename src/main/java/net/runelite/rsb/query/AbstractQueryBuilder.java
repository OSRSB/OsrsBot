package net.runelite.rsb.query;

import net.runelite.rsb.query.request.QueryRequest;
import net.runelite.rsb.query.result.QueryResult;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class AbstractQueryBuilder<T, Q extends AbstractQueryBuilder<T, Q, R, RQ>, R extends QueryResult<T>, RQ extends QueryRequest<T>> {
    protected RQ request;

    public AbstractQueryBuilder() {
        request = createRequest();
    }

    public R request() {
        return (R) new QueryResult(request.request());
    }

    public R results() {
        return (R) new QueryResult(requestAsList().stream().filter(this::accepts).collect(Collectors.toList()));
    }

    public boolean accepts(Object a) {
        return getRequest().accepts(a);
    }

    public List<T> requestAsList() {
        return request().toList();
    }

    protected RQ getRequest() {
        return request;
    }

    public abstract RQ createRequest();

    public Q filter(Predicate<T> predicate) {
        request.filter(predicate);
        return (Q)this;
    }

    public Set<T> asSet() {
        return (Set)results().stream().collect(Collectors.toSet());
    }

/* Result Functions */

    public QueryResult<T> reverse() {
        return results().reverse();
    }

    public T random() {
        return results().random();
    }

    public QueryResult<T> shuffle() {
        return results().shuffle();
    }

    public boolean contains(Object a) {
        return results().contains(a);
    }

    public int size() {
        return results().size();
    }

    public QueryResult<T> limit(int i, int j) {
        return results().limit(i,j);
    }

    public QueryResult<T> limit(int i) {
        return results().limit(i);
    }

    public Object[] toArray() {
        return results().toArray();
    }

    public List<T> toList() {
        return results().toList();
    }

    public boolean containsAll(Collection<?> collection) {
        return results().containsAll(collection);
    }

    public QueryResult<T> sort(Comparator<? super T> comparator) {
        return results().sort(comparator);
    }

    public T first() {
        return results().first();
    }

    public boolean isEmpty() {
        return results().isEmpty();
    }

    public T last() {
        return results().last();
    }

}
