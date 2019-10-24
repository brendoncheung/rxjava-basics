package com.alephreach.main.combining;

import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

import static com.alephreach.main.GlobalUtils.*;

public class WithLatestFrom {

//    Similar to Observable.combineLatest(), but not exactly the same, is the
//    withLatestfrom() operator. It will map each T emission with the latest values from other
//    Observables and combine them, but it will only take one emission from each of the other
//    Observables:

    private static void withLatestFrom() {
        Observable<Long> source1 = Observable.interval(300, TimeUnit.MILLISECONDS);
        Observable<Long> source2 = Observable.interval(1, TimeUnit.SECONDS);

        source2.withLatestFrom(source1, (l1,l2) -> "Slower source 2: " + l1 + " Faster source 1: " + l2
        ) .subscribe(System.out::println);

        sleep(100000);
    }

    public static void main(String[] args) {
        withLatestFrom();
    }


}
