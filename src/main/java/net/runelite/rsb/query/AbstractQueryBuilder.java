package net.runelite.rsb.query;

import net.runelite.rsb.query.request.QueryRequest;
import net.runelite.rsb.query.result.QueryResult;

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
        return (R) new QueryResult(asList());
    }

    public R results() {
        return (R) new QueryResult(asList());
    }

    public boolean accepts(Object a) {
        return getRequest().accepts(a);
    }

    public List<T> asList() {
        return getRequest().request();
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
        return (Set)getRequest().request().stream().collect(Collectors.toSet());
    }

}
