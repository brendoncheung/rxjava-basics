package com.alephreach.main.combining;

import io.reactivex.Observable;

import java.sql.Time;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static com.alephreach.main.GlobalUtils.*;

public class Ambigous {

    // basically the amb() takes an array of Observable<T>, and will only emit the first observable that emits,
    // others will be disposed on

//    The Observable.amb() factory (amb stands for ambiguous) will accept an
//    Iterable<Observable<T>> and emit the emissions of the first Observable that emits,
//    while the others are disposed of. The first Observable with an emission is the one whose
//    emissions go through. This is helpful when you have multiple sources for the same data or
//    events and you want the fastest one to win.

    private static void amb() {

        // if both intervals are the same, which one will emit is non-deterministic

        Observable<String> interval1 = Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(() -> System.out.println("Interval 1, i'm too slow!"))
                .take(5)
                .map(s -> "Interval 1: " + convertNumberToLetter(s, String::toUpperCase));

        Observable<String> interval2 = Observable.interval(1, TimeUnit.SECONDS)
                .doOnDispose(() -> System.out.println("Interval 2, i'm too slow!"))
                .map(s -> "Interval 2: " + s);

        Observable.amb(Arrays.asList(interval1, interval2))
                .subscribe(s -> System.out.println(s));

        sleep(100000);
    }


    public static void main(String[] args) {
        amb();

    }

}
