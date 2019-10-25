package com.alephreach.main.combining;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

import static com.alephreach.main.GlobalUtils.*;

public class Zipping {

    // zipping doesnt have to be the same type

//    Zipping allows you to take an emission from each Observable source and combine it into
//    a single emission. Each Observable can emit a different type, but you can combine these
//    different emitted types into a single emission. Here is an example, If we have an
//    Observable<String> and an Observable<Integer>, we can zip each String and
//    Integer together in a one-to-one pairing and concatenate it with a lambda:

    private static void zip() {

        Observable<String> source1 = getStringJustObservable();
        Observable<Integer> source2 = Observable.range(1, 6);

        BiFunction<String, Integer, String> mapper = (zip1, zip2) -> zip1 + "-" + zip2;

        Observable.zip(source1, source2, mapper)
                .subscribe(s -> System.out.println(s));
    }

    private static void zip_with_different_intervals() {

//        Note that if one or more sources are producing emissions
//        faster than another, zip() will queue up those rapid emissions as they wait on the slower
//        source to provide emissions.

        Observable<String> interval1 = Observable.interval(1, TimeUnit.SECONDS)
                .map(s -> "Interval 1: " + convertNumberToLetter(s, String::toUpperCase))
                .onErrorReturnItem("done");

        Observable<String> interval2 = Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(s -> "Interval 2: " + s);

        Observable.zip(interval1, interval2, (zip1, zip2) -> zip1 + " " + zip2)
                .subscribe(s -> System.out.println(s));

        sleep(100000);
    }

    private static void zipIterable() {

//        Use Observable.zipIterable() to pass a Boolean delayError
//        argument to delay errors until all sources terminate and an int
//        bufferSize to hint an expected number of elements from each source for
//        queue size optimization. You may specify the latter to increase
//        performance in certain scenarios by buffering emissions before they are
//        zipped.


    }

    public static void main(String[] args) {
//        zip();
        zip_with_different_intervals();
    }
}
