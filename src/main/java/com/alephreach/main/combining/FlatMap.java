package com.alephreach.main.combining;

import com.alephreach.main.GlobalUtils;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.operators.observable.ObservableFromArray;

import java.util.concurrent.TimeUnit;

import static com.alephreach.main.GlobalUtils.*;

public class FlatMap {

//    One of the most powerful and critical operators in RxJava is flatMap(). If you have to
//    invest time in understanding any RxJava operator, this is the one. It is an operator that
//    performs a dynamic Observable.merge() by taking each emission and mapping it to an
//    Observable. Then, it merges the emissions from the resulting Observables into a single
//    stream.

    private static void flatMap() {

        // basically, each emission can be converted to another observable, you do something to that observable
        // and then merge onto the rest of the emission

//        The simplest application of flatMap() is to map one emission to many emissions. Say, we
//        want to emit the characters from each string coming from Observable<String>. We can
//        use flatMap() to specify a Function<T,Observable<R>> lambda that maps each string
//        to an Observable<String>, which will emit the letters. Note that the mapped
//        Observable<R> can emit any type R, different from the source T emissions. In this
//        example, it just happened to be String, like the source:

        Observable<String> source = getStringJustObservable();

        source
                .flatMap(s -> Observable.fromArray(s.split("")))
                .doOnNext(s -> System.out.println("onNext(): " + s))
                .subscribe(s -> System.out.println(s));

        getRandomJustIntegerObservable()
                .flatMap(s -> Observable.just(s).repeat(s))
                .doOnNext(s -> System.out.println("Display the number: " + s + ", " + s + " times"))
                .subscribe(s -> System.out.println(s));
    }

    private static void flatMap2() {

//        Here is another example: let's take a sequence of String values (each a concatenated series
//        of values separated by "/"), use flatMap() on them, and filter for only numeric values
//        before converting them into Integer emissions:

        Observable<String> source =
                Observable.just("521934/2342/FOXTROT", "21962/12112/78886 /TANGO", "283242/4542/WHISKEY/2348562");

        source.flatMap(s -> Observable.fromArray(s.split("/")))
                .filter(s -> s.matches("[0-9]+"))
                .map(Integer::valueOf)
                .subscribe(s -> System.out.println(s));

//        We broke up each String by the / character, which yielded an array. We turned that into
//        an Observable and used flatMap() on it to emit each String. We filtered only for String
//        values that are numeric using a regular expression [0-9]+ (eliminating FOXTROT, TANGO,
//        and WHISKEY) and then turned each emission into an Integer.

    }

    private static void flatMap3() {
        Observable.just(2, 3, 10, 7)
                .flatMap(i -> Observable.interval(i, TimeUnit.SECONDS).map(i2 -> i + "s interval: " + ((i + 1) * i) + " seconds elapsed"))
                .subscribe(s -> System.out.println(s));


        sleep(12000);
    }

    private static void flatMap4() {

//        We can pass a second
//        combiner argument, which is a BiFunction<T,U,R> lambda, to associate the originally
//        emitted T value with each flat-mapped U value and turn both into an R value. In our earlier
//        example of emitting letters from each string, we can associate each letter with the original
//        string emission it was mapped from:

        Observable<String> source = Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon");

        // BiFunction<T, U, R>
        // T = original emitted value
        // U = Flat-mapped value
        // R = Returned value
        BiFunction<String, String, String> mapper = (s, c) -> s + "-" + c;

        source
                .flatMap(s -> Observable.fromArray(s.split("")), mapper)
                .subscribe(System.out::println);

//        We can also use flatMapIterable() to map each T emission into an Iterable<R>
//        instead of an Observable<R>. It will then emit all the R values for each Iterable<R>,
//        saving us the step and overhead of converting it into an Observable. There are also
//        flatMap() variants that map to Singles (flatMapSingle()), Maybes
//        (flatMapMaybe()), and Completables (flatMapCompletable()).
    }

    private static void test() {

        BiFunction<String, Integer, String> mapper = (original, flatted) -> original + " has " + flatted + " letters";

        getStringJustObservable()
                .flatMap(s -> Observable.just(s.length()), mapper)
                .subscribe(s -> System.out.println(s));
    }

    private static void test2() {

        getStringJustObservable()
                .startWith(s -> System.out.println("Hello"))
                .onErrorReturnItem("ops, something is wrong")
                .map(s -> s.length())
                .subscribe(s -> System.out.println(s));

        GlobalUtils.sleep(1000000);

    }

    public static void main(String[] args) {
//        flatMap();
//        flatMap2();
//        flatMap3();
        flatMap4();
//        test();
//        test2();
    }


}
