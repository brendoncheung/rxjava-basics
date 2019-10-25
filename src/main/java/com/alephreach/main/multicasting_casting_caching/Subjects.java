package com.alephreach.main.multicasting_casting_caching;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import java.util.concurrent.TimeUnit;

import static com.alephreach.main.GlobalUtils.*;

public class Subjects {

//    Before we discuss Subjects, it would be remiss to not highlight, they have use cases but
//    beginners often use them for the wrong ones, and end up in convoluted situations. As you
//    will learn, they are both an Observer and an Observable, acting as a proxy mulitcasting
//    device (kind of like an event bus). They do have their place in reactive programming, but
//    you should strive to exhaust your other options before utilizing them. Erik Meijer, the
//    creator of ReactiveX, described them as the "mutable variables of reactive programming". Just
//    like mutable variables are necessary at times even though you should strive for
//    immutability, Subjects are sometimes a necessary tool to reconcile imperative paradigms
//    with reactive ones.

//    PublishSubject
//    There are a couple implementations of Subject, which is an abstract type that implements
//    both Observable and Observer. This means that you can manually call onNext(),
//    onComplete(), and onError() on a Subject, and it will, in turn, pass those events
//    downstream toward its Observers.
//    The simplest Subject type is the PublishSubject, which, like all Subjects, hotly
//    broadcasts to its downstream Observers. Other Subject types add more behaviors, but
//    PublishSubject is the "vanilla" type, if you will.
//    We can declare a Subject<String>, create an Observer that maps its lengths and
//    subscribes to it, and then call onNext() to pass three strings. We can also call
//    onComplete() to communicate that no more events will be passed through this Subject:

    private static void publishSubjects() {

//        The simplest Subject type is the PublishSubject, which, like all Subjects, hotly
//        broadcasts to its downstream Observers. Other Subject types add more behaviors, but

        Consumer<Integer> observer = s -> System.out.println(s);

//        PublishSubject is the "vanilla" type, if you will.

        Subject<String> subjects = PublishSubject.create();

        subjects.map(String::length)
                .subscribe(observer);

        subjects.onNext("alpha");
        subjects.onNext("beta");
        subjects.onNext("gamma");
        subjects.onNext("delta");
    }

    private static void when_to_use_subjects() {

//        More likely, you will use Subjects to eagerly subscribe to an unknown number of multiple
//        source Observables and consolidate their emissions as a single Observable. Since Subjects
//        are an Observer, you can pass them to a subscribe() method easily. This can be helpful
//        in modularized code bases where decoupling between Observables and Observers takes
//        place and executing Observable.merge() is not that easy. Here, I use Subject to merge
//        and multicast two Observable interval sources:

        Observable<String> source1 = Observable.interval(1, TimeUnit.SECONDS)
                .map(l -> (l + l) + " seconds");

        Observable<String> source2 = Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(l -> ((l + l) * 300) + " seconds");

        Subject<String> subject = PublishSubject.create(); // acting as an observer

        subject.subscribe(s -> System.out.println(s));

        source1.subscribe(subject);
        source2.subscribe(subject);

        sleep(10000);
    }

    public static void main(String[] args) {
//        publishSubjects();
        when_to_use_subjects();
    }

}
