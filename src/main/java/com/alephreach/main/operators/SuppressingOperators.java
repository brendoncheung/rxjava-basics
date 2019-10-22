package com.alephreach.main.operators;

import com.alephreach.main.GlobalUtils;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

import java.util.concurrent.TimeUnit;

public class SuppressingOperators {

    // operators themselves are observables
    // if you call map() on an observable, the return is an observable

    private static void filter() {

        // it accepts a Predicate<T> for a given Observable<T>
        // emissions with false will not go through

        Predicate<String> lengthLimit = s -> s.length() != 5;

        GlobalUtils.getStringJustObservable()
                .filter(lengthLimit)
                .subscribe(System.out::println);
    }

    private static void take() {
        // two overloaded methods
        // 1. Take a specified number of emissions and then call onComplete(), then dispose the entire subscription
        // 2. Take within a specific time duration and then call onComplete()

        GlobalUtils.getStringJustObservable()
                .take(3) // if you have specified more than the source, it will emit all and call onComplete()
                .subscribe(s -> System.out.println(s));

        GlobalUtils.getIntervalObservable(500, TimeUnit.MILLISECONDS) // emit every 300 ms
                .take(3, TimeUnit.SECONDS) // take emissions for 2s and then call onComplete()
                .subscribe(s -> System.out.println(s));

        GlobalUtils.sleep(1000000);

    }

    private static void skip() {
        // skip() is opposite of take(), it skips x number of emissions

        GlobalUtils.getRangeObservable(1, 100)
                .skip(90) // skips the first 90 emissions, first emission will be 91
                .subscribe(i -> System.out.println(i));
    }

    private static void takeWhile() {
        // similar to take(), but can apply conditionals

        Predicate<Integer> rangeLimit = i -> i < 5;

        GlobalUtils.getRangeObservable(1, 100)
                .takeWhile(rangeLimit)
                .subscribe(i -> System.out.println(i));
    }

    private static void skipWhile() {
        // similar to skip(), but can accept conditionals

        Predicate<Integer> range = i -> i <= 95;

        GlobalUtils.getRangeObservable(1, 100)
                .skipWhile(range)
                .subscribe(i -> System.out.println(i));

    }

    private static void distinct() {
        // distinct() will emit each unique emissions and suppress any duplicates that follow
        // the equality is based on hashCode() and equals() of the emitted object

        // this can be memory intensive if you have a wide spectrum of objects,
        // hashset can get large and consume a lot of memory

        GlobalUtils.getStringJustObservable()
                .map(s -> s.length())
                .distinct()
                .subscribe(i -> System.out.println(i));

        // we can pass lambda expression to map each emission to a key used for equality
        // this allows the emission and not the key to go forward whue using the key for distinct logic
        // page 71 <- read

        GlobalUtils.getStringJustObservable()
                .distinct(String::length) // length is our uniqueness key
                .subscribe(s -> System.out.println(s));
    }

    private static void distinctUntilChanged() {

        // tl;dr distinct() never emit the same value, distinctUntilChanged() never emit the same value **consecutively**

//        The distinctUntilChanged() function will ignore duplicate consecutive emissions. It is
//        a helpful way to ignore repetitions until they change. If the same value is being emitted
//        repeatedly, all the duplicates will be ignored until a new value is emitted. Duplicates of the
//        next value will be ignored until it changes again, and so on. Observe the output for the
//        following code to see this behavior in action:

        Observable.just(1,1,2,3,3,2,2,1,1,1)
                .distinctUntilChanged()
                .subscribe(i -> System.out.println(i));

        // the string length will be our key for uniqueness
        Function<String, Integer> keySelector = String::length;

        // what is will do is it will never emit words with the same length consecutively
        // the word delta will be skipped because gamma has the number of letters
        GlobalUtils.getStringJustObservable()
                .distinctUntilChanged(keySelector)
                .subscribe(i -> System.out.println(i));
    }

    private static void elementAt() {
        // elementAt() will emit the element by index, starting at 0
        // after the element has been emitted, onCompleted() will be called

        // elementAt() returns a Maybe<T>

        GlobalUtils.getStringJustObservable()
                .elementAt(2) // if the element is not found, onComplete() will be called
                .subscribe(s -> System.out.println(s), Throwable::printStackTrace, () -> System.out.println("onComplete"));
    }

    public static void main(String[] args) {
        //filter();
        //take();
        //skip();
        //takeWhile();
        //skipWhile();
        //distinct();
        //distinctUntilChanged();
        elementAt();
    }



}






