package com.alephreach.main.combining;

import io.reactivex.Observable;
import io.reactivex.observables.GroupedObservable;

import static com.alephreach.main.GlobalUtils.*;

public class Grouping {

//    A powerful operation that you can achieve with RxJava is to group emissions by a specified
//    key into separate Observables. This can be achieved by calling the groupBy() operator,
//    which accepts a lambda mapping each emission to a key. It will then return an
//    Observable<GroupedObservable<K,T>>, which emits a special type of Observable
//    called GroupedObservable. GroupedObservable<K,T> is just like any other
//    Observable, but it has the key K value accessible as a property. It will emit the T emissions
//    that are mapped for that given key.

    private static void group() {

//        For instance, we can use the groupBy() operator to group emissions for an
//        Observable<String> by each String's length. We will subscribe to it in a moment, but
//        here is how we declare it:

        Observable<String> source = Observable.just("Apple", "Banana", "Apricot", "Blueberry", "Burger", "Pear");

        Observable<GroupedObservable<Integer, String>> byLengths = source.groupBy(s -> s.length());

        byLengths.flatMapSingle(group -> group.toList())
                .subscribe(s -> System.out.println(s));
    }

    public static void main(String[] args) {
        group();
    }


}
