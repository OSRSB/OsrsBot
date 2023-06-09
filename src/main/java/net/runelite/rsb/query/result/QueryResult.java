package net.runelite.rsb.query.result;

import net.runelite.rsb.util.StdRandom;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class QueryResult<T> implements Collection<T> {
    private List<T> objects;

    public QueryResult(List<T> list) {
        objects = list;
    }

    public QueryResult<T> reverse() {
        Collections.reverse(this.objects);
        return this;
    }

    public T random() {
        return objects.isEmpty() ? null : objects.get(StdRandom.uniform(0, this.size()));
    }

    public QueryResult<T> shuffle() {
        Collections.shuffle(objects);
        return this;
    }

    public boolean contains(Object a) {
        return objects.contains(a);
    }

    public boolean removeAll(Collection<?> a) {
        return objects.removeAll(a);
    }

    public boolean remove(Object a) {
        return objects.remove(a);
    }

    public boolean retainAll(Collection<?> a) {
        return objects.retainAll(a);
    }

    public boolean add(Object a) {
        return objects.add((T)a);
    }

    public int size() {
        return objects.size();
    }

    public QueryResult<T> limit(int i, int j) {
        objects = (List)objects.stream().skip((long)i).limit((long)j).collect(Collectors.toList());
        return this;
    }

    public T[] toArray(Object[] a) {
        return (T[])Arrays.copyOf(objects.toArray(), a.length);
    }

    public QueryResult<T> limit(int i) {
        objects = (List)objects.stream().limit((long)i).collect(Collectors.toList());
        return this;
    }

    public Object[] toArray() {
        return objects.toArray();
    }

    public List<T> toList() {
        return objects;
    }

    public boolean containsAll(Collection<?> collection) {
        return objects.containsAll(collection);
    }

    public boolean addAll(Collection<? extends T> collection) {
        return objects.addAll(collection);
    }

    public QueryResult<T> sort(Comparator<? super T> comparator) {
        objects.sort(comparator);
        return this;
    }

    public T first() {
        return objects.isEmpty() ? null : objects.get(0);
    }

    public void clear() {
        objects.clear();
    }

    public boolean isEmpty() {
        return objects.isEmpty();
    }

    public T last() {
        return objects.isEmpty() ? null : objects.get(this.size() - 1);
    }

    public Iterator<T> iterator() {
        return objects.iterator();
    }
}