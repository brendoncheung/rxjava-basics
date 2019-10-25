package com.alephreach.main.combining;

import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.alephreach.main.GlobalUtils.*;

public class Concatenation {

    // concat must be same type
    // Concatenation is ordered, ob1.concatWith(ob2), ob1 will need to call onComplete() befure ob2 can start

//    Concatenation is remarkably similar to merging, but with an important nuance: it will fire
//    elements of each provided Observable sequentially and in the order specified. It will not
//    move on to the next Observable until the current one calls onComplete(). This makes it
//    great to ensure that merged Observables fire their emissions in a guaranteed order.

//    You should prefer concatenation when you want to guarantee that
//    Observables fire their emissions in order. If you do not care about
//    ordering, prefer merging instead.

    private static void concat() {
        Observable source1 = Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon");
        Observable source2 = Observable.just("Apple", "Orange", "Pear", "Strawberry", "Banana");

        source1.concatWith(source2)
                .subscribe(s -> System.out.println(s));
    }

    private static void concat_infinite() {

//        If we use Observable.concat() with infinite Observables, it will forever emit from the
//        first one it encounters and prevent any following Observables from firing. If we ever want
//        to put an infinite Observable anywhere in a concatenation operation, it would likely be
//        specified last. This ensures that it does not hold up any Observables following it because
//        there are none. We can also use take() operators to make infinite Observables finite.

        Observable<String> interval1 = Observable.interval(1, TimeUnit.SECONDS)
                .map(l -> convertNumberToLetter((l), String::toUpperCase))
                .take(10)
                .map(s -> "interval 1: " + s);


        Observable<String> interval2 = Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(s -> "interval 2: " + s);

        interval1.concatWith(interval2)
                .subscribe(s -> System.out.println(s));

//        Observable.concat(interval1, interval2)
//                .subscribe(s -> System.out.println(s));

        sleep(100000);

    }

    private static void concatMap() {

        // it is pretty much the same as flatMap() but order will be conserved

//        Just as there is flatMap(), which dynamically merges Observables derived off each
//        emission, there is a concatenation counterpart called concatMap(). You should prefer this
//        operator if you care about ordering and want each Observable mapped from each
//        emission to finish before starting the next one. More specifically, concatMap() will merge
//        each mapped Observable sequentially and fire it one at a time. It will only move to the
//        next Observable when the current one calls onComplete(). If source emissions produce
//        Observables faster than concatMap() can emit from them, those Observables will be
//        queued.



    }



    public static void main(String[] args) {
//
        concat_infinite();

    }
}
