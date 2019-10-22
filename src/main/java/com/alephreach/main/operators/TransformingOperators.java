package com.alephreach.main.operators;

import com.alephreach.main.GlobalUtils;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.concurrent.TimeUnit;


public class TransformingOperators {

    private static void map() {
//        For a given Observable<T>, the map() operator will transform a T emission into an R
//        emission using the provided Function<T,R> lambda. We have already used this operator
//        many times, turning strings into lengths. Here is a new example: we can take raw date
//        strings and use the map() operator to turn each one into a LocalDate emission, as shown
//        in the following code snippet:

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy");

        Function<String, LocalDate> mapper = s -> LocalDate.parse(s, dtf);

        // Observable<String> --mapper--> Observable<LocalDate> --> consumer

        Observable.just("1/3/2016", "5/9/2016", "10/12/2016")
                .map(mapper) // after the mapping, the emission will be of type LocalDate
                .subscribe(d -> System.out.println(d));
    }

    private static void cast() {

//        A simple, map-like operator to cast each emission to a different type is cast(). If we want
//        to take Observable<String> and cast each emission to an object (and return an
//        Observable<Object>), we could use the map() operator like this:

        Observable<Object> items1 =
                Observable.just("Alpha", "Beta", "Gamma").map(s -> (Object) s);

//        But a shorthand we can use instead is cast(), and we can simply pass the class type we
//                want to cast to, as shown in the following code snippet:

        Observable<Object> items2 = Observable.just("Alpha", "Beta", "Gamma").cast(Object.class);
    }

    private static void startWith() {

//        For a given Observable<T>, the startWith() operator allows you to insert a T emission
//        that precedes all the other emissions. For instance, if we have an Observable<String>that
//        emits items on a menu we want to print, we can use startWith() to append a title header
//        first:

        Observable.just("Coffee", "Tea", "Espresso", "Latte")
                .startWith("COFFEE SHOP MENU")
                .subscribe(s -> System.out.println(s));

        Observable.just("Coffee", "Tea", "Espresso", "Latte")
                .startWithArray("------------", "COFFEE SHOP MENU", "------------")
                .subscribe(s -> System.out.println(s));
    }

    private static void defaultIfEmpty() {

//        For a given Observable<T>, we can specify a default T emission
//        if no emissions occur when onComplete() is called.

        Predicate<String> condition = s -> s.startsWith("S");

        GlobalUtils.getStringJustObservable()
                .filter(condition)
                .defaultIfEmpty("None found")
                .subscribe(s -> System.out.println(s));
    }

    private static void switchIfEmpty() {

//        Similar to defaultIfEmpty(), switchIfEmpty() specifies a different Observable to
//        emit values from if the source Observable is empty. This allows you specify a different
//        sequence of emissions in the event that the source is empty rather than emitting just one
//        value.

        Predicate<String> condiiton = s -> s.startsWith("Z");

        GlobalUtils.getStringJustObservable()
                .filter(condiiton)
                .switchIfEmpty(Observable.just("Zeta", "Eta", "Theta"))
                .subscribe(s -> System.out.println(s));
    }

    private static void sorted() {

//        If you have a finite Observable<T> emitting items that implement Comparable<T>, you
//        can use sorted() to sort the emissions. Internally, it will collect all the emissions and then
//        re-emit them in their sorted order. In the following code snippet, we sort emissions
//        from Observable<Integer>so that they are emitted in their natural order:

        Observable.just(16, 5, 7, 5, 4, 8, 9)
                .sorted()
                .subscribe(i -> System.out.println(i));

        Observable.just(6, 2, 5, 7, 1, 4, 9, 8, 3)
                .sorted(Comparator.reverseOrder())
                .subscribe(System.out::println);


        GlobalUtils.getStringJustObservable()
                .sorted((x,y) -> Integer.compare(x.length(), y.length()))
                .subscribe(System.out::println);
    }

    private static void delay() {

//        We can postpone emissions using the delay() operator. It will hold any received emissions
//        and delay each one for the specified time period. If we wanted to delay emissions by three
//        seconds, we could do it like this:

        // {------3 second------, emission}

        // delay() operatets on a different scheduler, therefore we need sleep()

        GlobalUtils.getStringJustObservable()
                .delay(3, TimeUnit.SECONDS)
                .subscribe(s -> System.out.println(s));

        GlobalUtils.sleep(5000);

//        Because delay() operates on a different scheduler (such as Observable.interval()), we
//        need to leverage a sleep() method to keep the application alive long enough to see this
//        happen.

//        Each emission will be delayed by three seconds. You can pass an optional third
//        Boolean argument indicating whether you want to delay error notifications as well.
//        For more advanced cases, you can pass another Observable as your delay() argument,
//                and this will delay emissions until that other Observable emits something.

    }

    private static void repeat() {

//        The repeat() operator will repeat subscription upstream after onComplete() a specified
//        number of times.

        GlobalUtils.getStringJustObservable()
                .repeat(2)
                .subscribe(s -> System.out.println(s),
                        Throwable::printStackTrace,
                        () -> System.out.println("onComplete"));
    }

    private static void scan() {
//        The scan() method is a rolling aggregator. For every emission, you add it to an
//        accumulation. Then, it will emit each incremental accumulation.
//
//        For instance, you can emit the rolling sum for each emission by passing a lambda to
//        thescan() method that adds each next emission to the accumulator:

        Observable.just(5, 4, 6, 7, 14, 1, 19)
                .scan((accumulator, next) -> {
                    System.out.println("accumulator: " + accumulator);
                    System.out.println("next: " + next);
                    return 0; // this return is to the accumulator
                })
                .subscribe(i -> System.out.println(i));

        Observable.just(5, 4, 6, 7, 14, 1, 19)
                .scan((accumulator, next) -> accumulator + next)
                .subscribe(i -> System.out.println(i));
    }

    private static void fibonacci(int index) {
        Observable.range(1, index) // 1 to 10 inclusive
                .scan((accumulator, next) -> accumulator + next)
                .subscribe(i -> System.out.println(i));
    }

    public static void main(String[] args) {
//        map();
//        cast();
//        startWith();
//        defaultIfEmpty();
//        switchIfEmpty();
//        sorted();
//        delay();
//        repeat();
//        scan();
        fibonacci(10); // broken lol
    }
}
