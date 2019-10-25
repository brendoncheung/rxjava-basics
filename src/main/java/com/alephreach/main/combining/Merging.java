package com.alephreach.main.combining;

import static com.alephreach.main.GlobalUtils.*;
import io.reactivex.Observable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.alephreach.main.GlobalUtils.*;
import static java.util.Arrays.asList;

public class Merging {

    // merge must be of the same type
    // merge does not conserve order, two observables will be interleaved

//    A common task done in ReactiveX is taking two or more Observable<T> instances and
//    merging them into one Observable<T>. This merged Observable<T> will subscribe to all
//    of its merged sources simultaneously, making it effective for merging both finite and
//    infinite Observables. There are a few ways that we can leverage this merging behavior
//    using factories as well as operators.

    private static void merge() {

//        The Observable.merge() operator will take two or more Observable<T> sources
//        emitting the same type T and then consolidate them into a single Observable<T>.
//        If we have only two to four Observable<T> sources to merge, you can pass each one as an
//        argument to the Observable.merge() factory. In the following code snippet, I have
//        merged two Observable<String> instances into one Observable<String>:

        Observable<String> source1 = getStringJustObservable();
        Observable<String> source2 = getStringJustObservable();

        Observable.merge(source1, source2)
                .subscribe(s -> System.out.println(s)); // ...Epsilon, Alpha.....

        // the above is the factory method to merge, you can use mergeWith() on a existing observable
        getOrderedJustIntegerObservable().mergeWith(getOrderedJustIntegerObservable())
                .subscribe(s -> System.out.println(s));

//        You can pass Iterable<Observable<T>> to Observable.merge() as well. It will merge
//        all the Observable<T> instances in that Iterable. I could achieve the preceding example
//        in a more type-safe way by putting all these sources in List<Observable<T>> and passing
//        them to Observable.merge():

        List<Observable<String>> list =
                asList(
                        getStringJustObservable(),
                        getStringJustObservable(),
                        getStringJustObservable(),
                        getStringJustObservable());

        Observable.merge(list)
                .subscribe(s -> System.out.println(s));
    }

    private static void merge_infinite() {

//        The Observable.merge() works with infinite Observables. Since it will subscribe to all
//        Observables and fire their emissions as soon as they are available, you can merge multiple
//        infinite sources into a single stream. Here, we merge two Observable.interval()
//        sources that emit at one second and 300 millisecond intervals, respectively. But before we
//        merge, we do some math with the emitted index to figure out how much time has elapsed
//        and emit it with the source name in a string. We let this process run for three seconds:

        Observable<String> source1 = Observable
                .interval(1, TimeUnit.SECONDS)
                .map(l -> l + 1)
                .map(l -> "Source A: " + l + " second");

        Observable<String> source2 = Observable
                .interval(300, TimeUnit.MILLISECONDS)
                .map(l -> l + 1)
                .map(l -> "Source B: " + l + " second");

        source1.mergeWith(source2)
                .subscribe(s -> System.out.println(s));

        sleep(1000000);
    }

    private static void example_of_not_in_order() {
        Observable<String> interval1 = Observable.interval(1, TimeUnit.SECONDS)
                .map(l -> convertNumberToLetter((l), String::toUpperCase))
                .take(10)
                .map(s -> "interval 1: " + s);


        Observable<String> interval2 = Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(s -> "interval 2: " + s);

        interval1.mergeWith(interval2)
                .subscribe(s -> System.out.println(s));

//        Observable.concat(interval1, interval2)
//                .subscribe(s -> System.out.println(s));

        sleep(100000);

    }



    public static void main(String[] args) {
//        merge();
        merge_infinite();
    }

}
