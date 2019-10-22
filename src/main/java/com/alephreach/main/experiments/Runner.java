package com.alephreach.main.experiments;

import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Runner {

    int start = 1;
    int count = 10;

    public static void main(String[] args) {
        Thread.currentThread().setName("Client thread");
        Runner r = new Runner();
//        r.m1();
//        r.m2();
//        r.m3();
//        r.m4();
//        r.m5();
//        r.m6();
//        r.callable();
//        r.single();
//        r.maybe();
        r.disposable();
//        r.test();
    }

    private void m1() {
        Observable<String> source =Observable.create(emitter ->{
            emitter.onNext("Alpha");
            emitter.onNext("Beta");
            emitter.onNext("Gamma");
            emitter.onNext("Delta");
            emitter.onNext("Epsilon");
            emitter.onComplete();
        });

        source
                .map(String::length)
                .filter(i -> i >= 5)
                .subscribe( i-> System.out.println(i));
    }

    private void m2() {
        Observable<String> source =Observable.just("Alpha", "beta", "Gramma", "Delta", "Espilon");

        source
                .map(String::length)
                .filter(i -> i >= 5)
                .subscribe(i -> System.out.println(i));

    }

    private void m3() {
       List<String> list = Arrays.asList("Alpha", "beta", "Gamma", "delta", "epsilon");

       Observable<String> observable = Observable.fromIterable(list);

       observable.map(s ->
                s.length())
               .filter(i -> i >= 5)
               .subscribe(i -> {

                   System.out.println(i);
               });
    }

    private void m4() {

        // cold observable vs hot observable
        // cold will replay all emissions when new subscribers join
        // hot will not replay all emissions

        ConnectableObservable<String> observable = Observable.just("Alpha", "Beta", "Gamme", "Delta", "Epsilon").publish();

        observable.subscribe(s -> System.out.println("A on thread: " + Thread.currentThread().getName() + ": " + s));
        observable.subscribe(s -> System.out.println("B on thread: " + Thread.currentThread().getName() + ": " + s));

        observable.connect();
    }

    private void m5() {
        ConnectableObservable<Long> observable = Observable.interval(1, TimeUnit.SECONDS).publish();

        observable.subscribe(s -> {
            System.out.println(Thread.currentThread().getName());
            System.out.println("A: " + s);
        });
        observable.connect();

        sleep(5000);

        observable.subscribe(s -> {
            System.out.println(Thread.currentThread().getName());
            System.out.println("B: " + s);
        });
        observable.connect();

        sleep(5000);
    }

    private void m6() {

        // the below code cannot capture state changes, by using defer(), you can create a fresh observable on each subscription
//        int start = 1;
//        int count = 10;
//
//        Observable<Integer> source = Observable.range(start,count);
//
//        source.subscribe(s -> System.out.println(s));
//
//        count = 100;
//
//        source.subscribe(s -> System.out.println(s));
        Observable<Integer> source = Observable.defer(() -> Observable.range(this.start, this.count));

        source.subscribe(s -> System.out.println("A: " + s));

        this.count= 20;

        source.subscribe(s -> System.out.println("B: " + s));
    }

    private void callable() {

        // the below code failed at the just block, which didnt get sent down the chain,
        // to reactively handle error we need to send the error down the chain and not left it at the call site

//        Observable<Integer> source1 = Observable.just(1/0);
//        source1.subscribe(s -> System.out.println(s),Throwable::printStackTrace);

        Observable<Integer> source = Observable.fromCallable(() -> {
            System.out.println(Thread.currentThread().getName());
            return 1/0;
        });

        source.subscribe(s -> System.out.println(s),
                e -> System.out.println("Error"));
    }

    private void single() {
        Single.just("hello")
                .map(String::length)
                .subscribe(System.out::println, Throwable::printStackTrace);

        Observable.just("A", "B", "C")
                .first("default")
                .subscribe(System.out::print);
    }

    private void maybe() {
        Maybe<Integer> presentSource = Maybe.just(100);
        presentSource.subscribe(s -> System.out.println("Process A: " + s), Throwable::printStackTrace, () -> System.out.println("onComplete"));

        Maybe<Integer> emptyMaybe = Maybe.empty();
        emptyMaybe.subscribe(s -> System.out.println("Process B: " + s), Throwable::printStackTrace, () -> System.out.println("onComplete"));
    }

    private void test() {

//        Observable<String> observable = Observable.just("hello" ,"world");
//        observable.subscribe(s -> System.out.println("A: " + s));
//        sleep(1000);
//        observable.subscribe(s -> System.out.println("B: " + s));
    }

    private void disposable() {
        Observable<Long> seconds = Observable.interval(1, TimeUnit.SECONDS);

        Disposable disposable = seconds.subscribe(i -> System.out.println(i));

        sleep(5000);

        disposable.dispose();

        sleep(5000);
    }

    private void sleep(int s) {
        try {
            Thread.currentThread().sleep(s);
        } catch (Exception e) {

        }
    }

    private void sleep() {
        try {
            Thread.currentThread().sleep(5000);
        } catch (Exception e) {

        }
    }
}
