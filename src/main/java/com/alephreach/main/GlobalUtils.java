package com.alephreach.main;

import io.reactivex.Observable;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

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

    public static Observable<Integer> getRandomJustIntegerObservable() {
        return Observable.just(8, 5, 6, 4, 3, 0, 7, 10, 2, 17);
    }

    public static Observable<Integer> getOrderedJustIntegerObservable() {
        return Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    }

    public static Observable<String> getDateJustStringObservable() {
        return Observable.just("2016-01-01", "2016-05-02", "2016-09-12", "2016-04-03");
    }

    public static Observable<Long> getIntervalObservable(int period, TimeUnit unit) {
        return Observable.interval(period, unit);
    }

    public static Observable<Integer> getRangeObservable(int start, int count) {
        return Observable.range(start, count);
    }

    public static int getRandomNumber(int seed) {
        return ThreadLocalRandom.current().nextInt(seed);
    }

    public static <T> T intensiveCalculation(T object) {
        sleep(3000);
        return object;
    }

    public static String convertNumberToLetter(Long index, Function<String, String> f) {

        String a = "ABCDEFGHIJKLMNOPQRXTUVWXYZ";

        return f.apply(String.valueOf(a.charAt(index.intValue())));
    }
}
