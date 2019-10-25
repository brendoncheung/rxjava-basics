package com.alephreach.main.multicasting_casting_caching;

import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static com.alephreach.main.GlobalUtils.*;

public class ConnectableObservers {

    private static void emission_to_all_observers_simultaneously() {

//        Using ConnectableObservable will force emissions from the source to become hot,
//        pushing a single stream of emissions to all Observers at the same time rather than giving a
//        separate stream to each Observer. This idea of stream consolidation is known as
//        multicasting, but there are nuances to it, especially when operators become involved. Even
//        when you call publish() and use a ConnectableObservable, any operators that follow
//        can create separate streams again. We will take a look at this behavior and how to manage it
//        next.

        ConnectableObservable<Integer> source = Observable.range(1, 10).publish();

        source.subscribe(s -> System.out.println("A: " + s), Throwable::printStackTrace, () -> System.out.println("A: onComplete()"));
        source.subscribe(s -> System.out.println("B: " + s), Throwable::printStackTrace, () -> System.out.println("B: onComplete()"));

        source.connect();
    }

    private static void how_multicasting_works() {

        Observable<Integer> source = Observable.range(1, 3)
                .map(s -> getRandomNumber(10000));

//        these two observers will get a different data

//        What happens here is that the Observable.range() source will yield two separate
//        emission generators, and each will coldly emit a separate stream for each Observer. Each
//        stream also has its own separate map() instance, hence each Observer gets different
//        random integers. You can visually see this structure of two separate streams in the
//        following figure:

        source.subscribe(s -> System.out.println("A: " + s));
        source.subscribe(s -> System.out.println("B: " + s));

        // you might be tempted to do this:

        Observable<Integer> random = Observable.range(1, 3)
                .publish();

        random.map(s -> getRandomNumber(10000));

        // this will not work because the publish chain will have the map operator in it
        // to ensure you observers receive the same data, you put all your operators before the .publish() call:
        ConnectableObservable<Integer> source2 = Observable.range(1, 3)
                .map(s -> getRandomNumber(10000))
                .publish();

        // all emission happens on the main thread
        source2.subscribe(s -> System.out.println("Thread A " + Thread.currentThread().getName() + " " + s));
        source2.subscribe(s -> System.out.println("Thread B " + Thread.currentThread().getName() + " " + s));

        source2.connect();
    }

    private static void when_to_multicast() {

//        Multicasting is helpful in preventing redundant work being done by multiple Observers
//        and instead makes all Observers subscribe to a single stream, at least to the point where
//        they have operations in common. You may do this to increase performance, reducing
//        memory and CPU usage, or simply because your business logic requires pushing the same
//        emissions to all Observers.

//        Data-driven cold Observables should only be multicast when you are doing so for
//        performance reasons and have multiple Observers receiving the same data simultaneously.
//        Remember that multicasting creates hot ConnectableObservables, and you have to be
//        careful and time the connect() call so data is not missed by Observers. Typically in your
//        API, keep your cold Observables cold and call publish() when you need to make them
//        hot.

//        Even if your source Observable is hot (such as a UI event in JavaFX or Android), putting
//        operators against that Observable can cause redundant work and listeners. It is not
//        necessary to multicast when there is only a single Observer (and multicasting can cause
//        unnecessary overhead). But if there are multiple Observers, you need to find the proxy
//        point where you can multicast and consolidate the upstream operations. This point is
//        typically the boundary where Observers have common operations upstream and diverge
//        into different operations downstream.

//        For instance, you may have one Observer that prints the random integers but another one
//        that finds the sum with reduce(). At this point, that single stream should, in fact, fork into
//        two separate streams because they are no longer redundant and doing different work, as
//        shown in the following code snippet:

        ConnectableObservable<Integer> source = getRangeObservable(1, 3)
                .map(i -> getRandomNumber(10000))
                .publish();

        source.subscribe(s -> System.out.println("Obs A: " + s));
        source.reduce(0, (total, next) -> total + next)
                .subscribe(s -> System.out.println("Obs B: " + s));

        source.connect();
    }

    private static void automatic_connection() {

//        There are definitely times you will want to manually call connect() on
//        ConnectableObservable to precisely control when the emissions start firing. There are
//        convenient operators that automatically call connect() for you, but with this convenience,
//        it is important to have awareness of their subscribe timing behaviors. Allowing an
//        Observable to dynamically connect can backfire if you are not careful, as emissions can be
//        missed by Observers.

        Observable<Integer> source = Observable.range(1, 3)
                .map(s -> getRandomNumber(10000))
                .publish()
                // this source will fire when two observers are subscribed,
                // the third observer will not receive emissions
                // Note that if you pass no argument for numberOfSubscribers, it will default to 1. This can
                // be helpful if you want it to start firing on the first subscription and do not care about any
                // subsequent Observers missing previous emissions.
                .autoConnect(2);

        source.subscribe(s -> System.out.println("A: " + s));
        source.subscribe(s -> System.out.println("B: " + s));
        source.subscribe(s -> System.out.println("C: " +s));

        Observable<Long> source2 = Observable.interval(1, TimeUnit.SECONDS)
                .publish()
                .autoConnect();

        source2.subscribe(s -> System.out.println("First subscriber: " + s));

        sleep(3000);

        source2.subscribe(s -> System.out.println("Second subscriber: " + s));

        sleep(100000000);
    }

    private static void refCount_share() {

//        The refCount() operator on ConnectableObservable is similar to
//        autoConnect(1), which fires after getting one subscription. But there is one important
//        difference; when it has no Observers anymore, it will dispose of itself and start over when a
//        new one comes in. It does not persist the subscription to the source when it has no more
//        Observers, and when another Observer follows, it will essentially "start over".

        // the source below won't start over when the next observer joins, the last observer will miss emissions

        Observable<Long> source = Observable.interval(1, TimeUnit.SECONDS)
                .publish()
                .autoConnect();

        source.take(3)
                .subscribe(s -> System.out.println("A: " + s));

        sleep(3000);

        source.take(3)
                .subscribe(s -> System.out.println("B: " + s));

        sleep(3000);

        // the below is an example when there are no observers subscribed to a source, the
        // source itself will dispose itself and when the next observer subscribes, it will
        // start over again

//        Using refCount() can be helpful to multicast between multiple Observers but dispose of
//        the upstream connection when no downstream Observers are present anymore. You can
//        also use an alias for publish().refCount() using the share() operator. This will
//        accomplish the same result:

        Observable<Long> source2 = Observable.interval(1, TimeUnit.SECONDS)
                .publish()
                .autoConnect();

        source2.take(3)
                .subscribe(s -> System.out.println("A: " + s));

        sleep(3000);

        source2.take(3)
                .subscribe(s -> System.out.println("B: " + s));

        sleep(3000);


        // share() is the same as publish() and refCount() combined
    }

    private static void replaying_caching() {

        // replays whatever that is missed to the next subscribed observer

//        The replay() operator is a powerful way to hold onto previous emissions within a certain
//        scope and re-emit them when a new Observer comes in. It will return a
//        ConnectableObservable that will both multicast emissions as well as emit previous
//        emissions defined in a scope. Previous emissions it caches will fire immediately to a new

//        Observer so it is caught up, and then it will fire current emissions from that point forward.
//        Let's start with a replay() with no arguments. This will replay all previous emissions to
//        tardy Observers, and then emit current emissions as soon as the tardy Observer is caught
//        up. If we use Observable.interval() to emit every second, we can call replay() on it
//        to multicast and replay previous integer emissions. Since replay() returns
//        ConnectableObservable, let's use autoConnect() so it starts firing on the first
//        subscription. After 3 seconds, we will bring in a second Observer. Look closely at what
//        happens:

        Observable<Long> source = Observable.interval(1, TimeUnit.SECONDS)
                // replay() with no argument will cache all
                // replay(k) will cache the last k emissions
                .replay(1)
                .autoConnect();

        // replay() should always be used with autoConnect() because with refCount() the cache will dispose

        source.subscribe(s -> System.out.println("A: " + s));

        sleep(5000);

        source.subscribe(s -> System.out.println("B: " + s));

        sleep(5000);
    }

    private static void replay_with_time() {

//        There are other overloads for replay(), particularly a time-based window you can specify.
//
        Observable<Long> source = Observable.interval(1, TimeUnit.SECONDS)
                .replay(1, TimeUnit.SECONDS)
                .autoConnect();

        source.subscribe(s -> System.out.println("A :" + s));

        sleep(3000);

        source.subscribe(s -> System.out.println("B :" + s));

        sleep(3000);
    }

    private static void caching() {

//        When you want to cache all emissions indefinitely for the long term and do not need to
//        control the subscription behavior to the source with ConnectableObservable, you can
//        use the cache() operator. It will subscribe to the source on the first downstream Observer
//        that subscribes and hold all values indefinitely. This makes it an unlikely candidate for
//        infinite Observables or large amounts of data that could tax your memory:

        Observable<Integer> source = Observable.just(6, 5, 4, 7, 8, 9, 12)
                .scan(0, (total, next) -> total + next)
                .cache();

        source.subscribe(s -> System.out.println(s));

        Observable<Integer> source2 = Observable.just(6, 5, 4, 7, 8, 9, 12)
                .scan(0, (total, next) -> total + next)
                .cacheWithInitialCapacity(5);

        source2.subscribe(s -> System.out.println(s));




    }

    public static void main(String[] args) {
//        emission_to_all_observers_simultaneously();
//        how_multicasting_works();
//        when_to_multicast();
//        automatic_connection();
//        refCount_share();
//        replaying_caching();
//        replay_with_time();
        caching();
    }


}



