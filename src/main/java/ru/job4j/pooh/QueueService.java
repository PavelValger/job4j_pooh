package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {
    private static final String STATUS_ON = "200";
    private static final String STATUS_OFF = "204";
    private final Map<String, ConcurrentLinkedQueue<String>> store = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        if ("POST".equals(req.httpRequestType())) {
            store.putIfAbsent(req.sourceName(), new ConcurrentLinkedQueue<>());
            store.get(req.sourceName()).add(req.param());
            return new Resp("", STATUS_ON);
        }
        if ("GET".equals(req.httpRequestType())) {
            String text = store.getOrDefault(req.sourceName(), new ConcurrentLinkedQueue<>()).poll();
            if (text == null) {
                return new Resp("", STATUS_OFF);
            }
            return new Resp(text, STATUS_ON);
        }
        return new Resp("", STATUS_OFF);
    }
}
