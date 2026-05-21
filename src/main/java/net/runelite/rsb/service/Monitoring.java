package net.runelite.rsb.service;

import lombok.extern.slf4j.Slf4j;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class Monitoring {

    public enum Event {
        Start, Finish, Random, Script, Hack
    }

    private static final Map<Event, AtomicLong> counters = new EnumMap<>(Event.class);

    static {
        for (Event e : Event.values()) {
            counters.put(e, new AtomicLong(0));
        }
    }

    public static void RandomStarted(final String name) {
        RaiseEvent(Event.Random, new String[]{name, "started"});
    }

    public static void RandomFinished(final String name, final boolean passed) {
        RaiseEvent(Event.Random, new String[]{name, passed ? "passed" : "failed"});
    }

    public static void RaiseEvent(final Event type, String[] params) {
        long count = counters.get(type).incrementAndGet();
        log.info("event={} count={} params={}", type, count, params);
    }

    public static long getEventCount(Event type) {
        return counters.get(type).get();
    }

    public static void resetCounters() {
        counters.values().forEach(c -> c.set(0));
    }
}
