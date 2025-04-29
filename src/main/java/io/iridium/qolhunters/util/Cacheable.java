package io.iridium.qolhunters.util;

import java.util.function.Supplier;

public class Cacheable<T> {

    private T value;
    private long timestamp;
    private long timeout;

    private final Supplier<T> callbackFunction;

    public Cacheable(long timeout, Supplier<T> callbackFunction) {
        this.timeout = timeout;
        this.callbackFunction = callbackFunction;
    }

    public T get() {
        if (System.currentTimeMillis() - timestamp > timeout) {
            value = callbackFunction.get();
            timestamp = System.currentTimeMillis();
        }
        return value;
    }





}
