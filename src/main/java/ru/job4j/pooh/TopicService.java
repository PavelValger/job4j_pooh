package ru.job4j.pooh;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {
    private final Map<String, ConcurrentHashMap<String,
            ConcurrentLinkedQueue<String>>> store = new ConcurrentHashMap<>();
    private static final String STATUS_ON = "200";
    private static final String STATUS_OFF = "204";

    @Override
    public Resp process(Req req) {
        if ("GET".equals(req.httpRequestType())) {
            store.putIfAbsent(req.sourceName(), new ConcurrentHashMap<>());
            var queue = store.getOrDefault(req.sourceName(), new ConcurrentHashMap<>())
                    .putIfAbsent(req.param(), new ConcurrentLinkedQueue<>());
            if (queue != null && !queue.isEmpty()) {
                return new Resp(queue.poll(), STATUS_ON);
            }
            return new Resp("", STATUS_OFF);
        }
        if ("POST".equals(req.httpRequestType())) {
            var queue = store.getOrDefault(req.sourceName(), new ConcurrentHashMap<>());
            if (queue.isEmpty()) {
                return new Resp("", STATUS_OFF);
            }
            queue.forEach((key, value) -> value.add(req.param()));
            return new Resp("", STATUS_ON);
        }
        return new Resp("", STATUS_OFF);
    }
}
