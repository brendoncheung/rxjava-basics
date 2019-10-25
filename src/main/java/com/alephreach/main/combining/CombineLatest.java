package com.alephreach.main.combining;

import io.reactivex.Observable;
import java.util.concurrent.TimeUnit;

import static com.alephreach.main.GlobalUtils.*;

public class CombineLatest {

//    The Observable.combineLatest() factory is somewhat similar to zip(), but for every
//    emission that fires from one of the sources, it will immediately couple up with the latest
//    emission from every other source. It will not queue up unpaired emissions for each source,
//    but rather cache and pair the latest one.

    private static void combineLatest() {

        Observable<String> interval1 = Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(s -> "interval 1: " + s);

        Observable<String> interval2 = Observable.interval(1, TimeUnit.SECONDS)
                .map(s -> convertNumberToLetter(s, String::toUpperCase));

        Observable.combineLatest(interval1, interval2, (zip1, zip2) -> zip1 + " " + zip2)
                .subscribe(s -> System.out.println(s));

        sleep(100000);
    }

    public static void main(String[] args) {

        combineLatest();

    }
}
