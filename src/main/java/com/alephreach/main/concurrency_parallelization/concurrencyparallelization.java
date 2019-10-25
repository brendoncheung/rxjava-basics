package com.alephreach.main.concurrency_parallelization;

import com.alephreach.main.GlobalUtils;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

import static com.alephreach.main.GlobalUtils.*;

public class concurrencyparallelization {

//    Concurrency in RxJava is simple to execute, but somewhat abstract to understand. By
//    default, Observables execute work on the immediate thread, which is the thread that
//    declared the Observer and subscribed it. In many of our earlier examples, this was the
//    main thread that kicked off our main() method

    private static void subscribe_on() {

//                Note how both Observables fire emissions slowly as each one is slowed by 0-3 seconds in
//        the map() operation. More importantly, note how the first Observable firing Alpha, Beta,
//        Gamma must finish first and call onComplete() before firing the second Observable
//        emitting the numbers 1 through 6. If we fire both Observables at the same time rather than
//        waiting for one to complete before starting the other, we could get this operation done
//        much more quickly.

//                We can achieve this using the subscribeOn() operator, which suggests to the source to
//        fire emissions on a specified Scheduler. In this case, let us use
//        Rx_Schedulers.computation(), which pools a fixed number of threads appropriate for
//        computation operations. It will provide a thread to push emissions for each Observer.
//        When onComplete() is called, the thread will be given back to Scheduler so it can be
//        reused elsewhere:

        Observable.just("Alpha", "Beta", "Gamma", "Delta")
                .map(s -> intensiveCalculation(s))
                .subscribeOn(Schedulers.computation())
                .subscribe(s -> System.out.println(s));

        Observable.range(1, 6)
                .map(s -> intensiveCalculation(s))
                .subscribe(s -> System.out.println(s));

    }

    private static void operators_on_different_threads() {
//        Something else that is exciting about RxJava is its operators (at least the official ones and the
//        custom ones built properly). They can work with Observables on different threads safely.
//
//                Even operators and factories that combine multiple Observables, such as merge() and
//        zip(), will safely combine emissions pushed by different threads. For instance, we can use
//        zip() on our two Observables in the preceding example even if they are emitting on two
//        separate computation threads:

        Observable<String> source1 = getStringJustObservable()
                .map(s -> intensiveCalculation(s))
                .subscribeOn(Schedulers.computation());

        Observable<String> source2 = Observable.interval(300, TimeUnit.MILLISECONDS)
                .map(s -> "Hello")
                .subscribeOn(Schedulers.computation());

        Observable.zip(source1, source2, (string, integer) -> string + "-" + integer)
                .subscribe(s -> System.out.println(s));

//        Being able to split and combine Observables happening on different threads is powerful
//        and eliminates the pain points of callbacks. Observables are agnostic to whatever thread
//        they work on, making concurrency easy to implement, configure, and evolve at any time.

        sleep(10000000);
    }

    private static void blocking_operators() {

//        You can use blocking operators to stop the declaring thread and wait for emissions.
//        Usually, blocking operators are used for unit testing (as we will discuss in Chapter 10,
//        Testing and Debugging), and they can attract antipatterns if used improperly in production.
//        However, keeping an application alive based on the life cycle of a finite Observable
//        subscription is a valid case to use a blocking operator. As shown here,
//        blockingSubscribe() can be used in place of subscribe() to stop and wait for
//        onComplete() to be called before the main thread is allowed to proceed and exit the
//        application:

        getStringJustObservable()
                .subscribeOn(Schedulers.computation())
                .map(GlobalUtils::intensiveCalculation)
                .blockingSubscribe(s -> System.out.println(s), Throwable::printStackTrace, () -> System.out.println("onComplete()"));

    }

    public static void main(String[] args) {
//        subscribe_on();
//        operators_on_different_threads();
        blocking_operators();
    }
}
