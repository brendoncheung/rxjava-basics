package com.alephreach.main;

import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class GlobalUtils {

    public static void sleep(int millis) {
        try {
            Thread.currentThread().sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Observable<String> getStringJustObservable() {
        return Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon");
    }

    public static Observable<Long> getIntervalObservable(int period, TimeUnit unit) {
        return Observable.interval(period, unit);
    }

    public static Observable<Integer> getRangeObservable(int start, int count) {
        return Observable.range(start, count);
    }
}
