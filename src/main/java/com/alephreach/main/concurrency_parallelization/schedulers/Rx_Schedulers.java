package com.alephreach.main.concurrency_parallelization.schedulers;

import com.alephreach.main.GlobalUtils;
import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;

import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.alephreach.main.GlobalUtils.*;

public class Rx_Schedulers {

//    As discussed earlier, thread pools are a collection of threads. Depending on the policy of
//    that thread pool, threads may be persisted and maintained so they can be reused. A queue
//    of tasks is then executed by that thread pool.

//    Some thread pools hold a fixed number of threads (such as the computation() one we
//    used earlier), while others dynamically create and destroy threads as needed. Typically in
//    Java, you use an ExecutorService as your thread pool. However, RxJava implements its
//    own concurrency abstraction called Scheduler. It define methods and rules that an actual
//    concurrency provider such as an ExecutorService or actor system must obey. The
//    construct flexibly makes RxJava non-opinionated on the source of concurrency.

//    Many of the default Scheduler implementations can be found in the Rx_Schedulers static
//    factory class. For a given Observer, a Scheduler will provide a thread from its pool that
//    will push the emissions. When onComplete() is called, the operation will be disposed of
//    and the thread will be given back to the pool, where it may be persisted and reused by
//    another Observer.

    private static void computation() {

//        We already saw the computation Scheduler, which you can get the global instance of by
//        calling Rx_Schedulers.computation(). This will maintain a fixed number of threads based
//        on the processor count available to your Java session, making it appropriate for
//        computational tasks. Computational tasks (such as math, algorithms, and complex logic)
//        may utilize cores to their fullest extent. Therefore, there is no benefit in having more worker
//        threads than available cores to perform such work, and the computational Scheduler will
//        ensure that:

//        A number of operators and factories will use the computation Scheduler
//        by default unless you specify a different one as an argument. These
//        include one or more overloads for interval(), delay(), timer(),
//        timeout(), buffer(), take(), skip(), takeWhile(), skipWhile(),
//        window(), and a few others.

    }

    private static void io() {

//        IO tasks such as reading and writing databases, web requests, and disk storage are less
//        expensive on the CPU and often have idle time waiting for the data to be sent or come back.
//        This means you can create threads more liberally, and Rx_Schedulers.io() is appropriate for
//        this. It will maintain as many threads as there are tasks and will dynamically grow, cache, and reduce the number of threads as needed. For instance, you may use Rx_Schedulers.io()
//        to perform SQL operations using RxJava-JDBC (https://github.com/davidmoten/rxjava/jdbc):

//        But you have to be careful! As a rule of thumb, assume that each subscription will result in
//        a new thread.
    }

    private static void new_thread() {

//        The Rx_Schedulers.newThread() factory will return a Scheduler that does not pool
//        threads at all. It will create a new thread for each Observer and then destroy the thread
//        when it is done. This is different than Rx_Schedulers.io() because it does not attempt to
//        persist and cache threads for reuse:

//        This may be helpful in cases where you want to create, use, and then destroy a thread
//        immediately so it does not take up memory. But for complex applications generally, you
//        will want to use Rx_Schedulers.io() so there is some attempt to reuse threads if possible.
//         You also have to be careful as Rx_Schedulers.newThread() can run amok in complex
//        applications (as can Rx_Schedulers.io()) and create a high volume of threads, which could
//        crash your application.
    }

    private static void single() {

//        When you want to run tasks sequentially on a single thread, you can invoke
//        Rx_Schedulers.single(). This is backed by a single-threaded implementation appropriate
//        for event looping. It can also be helpful to isolate fragile, non-threadsafe operations to a
//        single thread:

    }

    private static void trampoline() {

//        Rx_Schedulers.trampoline() is an interesting Scheduler. In practicality, you will not
//        invoke it often as it is used primarily in RxJava's internal implementation. Its pattern is also
//        borrowed for UI Rx_Schedulers such as RxJavaFX and RxAndroid. It is just like default
//        scheduling on the immediate thread, but it prevents cases of recursive scheduling where a
//        task schedules a task while on the same thread. Instead of causing a stack overflow error, it
//        will allow the current task to finish and then execute that new scheduled task afterward.
    }

    private static void from() {

//        You can build a Scheduler off a standard Java ExecutorService. You may choose to do
//        this in order to have more custom and fine-tuned control over your thread management
//        policies. For example, say, we want to create a Scheduler that uses 20 threads. We can create
//        a new fixed ExecutorService specified with this number of threads. Then, you can wrap
//        it inside a Scheduler implementation by calling Rx_Schedulers.from():

        int numberOfThreads = 20;

        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        getStringJustObservable()
                .subscribeOn(Schedulers.from(executor))
                .doFinally(() -> executor.shutdown())
                .subscribe(s -> System.out.println(s));
    }

    private static void starting_shutting_down_schedulers() {

//        Each default Scheduler is lazily instantiated when you first invoke its usage. You can
//        dispose the computation(), io(), newThread(), single(), and trampoline()
//        Schedulers at any time by calling their shutdown() method or all of them by calling
//        Schedulers.shutdown(). This will stop all their threads and forbid new tasks from
//        coming in and will throw an error if you try otherwise. You can also call their start()
//        method, or Schedulersers.start(), to reinitialize the Schedulers so they can accept
//        tasks again.

//        In desktop and mobile app environments, you should not run into many
//        cases where you have to start and stop the Schedulers. On the server side,
//        however, J2EE-based applications (for example, Servlets) may get
//        unloaded and reloaded and use a different classloader, causing the old
//        Schedulers instances to leak. To prevent this from occurring, the Servlet
//        should shut down the Schedulers manually in its destroy() method.

//        Only manage the life cycle of your Schedulers if you absolutely have to. It is better to let the
//        Schedulers dynamically manage their usage of resources and keep them initialized and
//        available so tasks can quickly be executed at a moment's notice. Note carefully that it is
//        better to ensure that all outstanding tasks are completed or disposed of before you shut
//        down the Schedulers, or else you may leave the sequences in an inconsistent state


    }

    private static void understanding_subscribeOn() {

//        The subscribeOn() operator will suggest to the source Observable upstream which
//        Scheduler to use and how to execute operations on one of its threads. If that source is not
//        already tied to a particular Scheduler, it will use the Scheduler you specify. It will then
//        push emissions all the way to the final Observer using that thread (unless you add
//        observeOn() calls, which we will cover later). You can put subscribeOn() anywhere in
//        the Observable chain, and it will suggest to the upstream all the way to the origin
//        Observable which thread to execute emissions with.

//        In the following example, it makes no difference whether you put this subscribeOn()
//        right after Observable.just() or after one of the operators. The subscribeOn() will
//        communicate upstream to the Observable.just() which Scheduler to use no matter
//        where you put it. For clarity, though, you should place it as close to the source as possible:

        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .subscribeOn(Schedulers.computation()) //preferred
                .map(String::length)
                .filter(i -> i > 5)
                .subscribe(System.out::println);

        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .map(String::length)
                .subscribeOn(Schedulers.computation())
                .filter(i -> i > 5)
                .subscribe(System.out::println);

        Observable.just("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
                .map(String::length)
                .filter(i -> i > 5)
                .subscribeOn(Schedulers.computation())
                .subscribe(System.out::println);

//        Having multiple Observers to the same Observable with subscribeOn() will result in
//        each one getting its own thread (or have them waiting for an available thread if none are
//        available). In the Observer, you can print the executing thread's name by calling
//        Thread.currentThread().getName(). We will print that with each emission to see that
//        two threads, in fact, are being used for both Observers:

        Observable<String> observable = getDateJustStringObservable();

        observable
                .subscribeOn(Schedulers.computation())
                .map(s -> intensiveCalculation(s))
                .map(String::length)
                .subscribe(s -> System.out.println(Thread.currentThread().getName()));  // RxComputationThreadPool-x

        observable
                .subscribeOn(Schedulers.computation())
                .map(s -> intensiveCalculation(s))
                .map(String::length)
                .subscribe(s -> System.out.println(Thread.currentThread().getName()));

        observable
                .subscribeOn(Schedulers.computation())
                .map(s -> intensiveCalculation(s))
                .map(String::length)
                .subscribe(s -> System.out.println(Thread.currentThread().getName()));


        sleep(100000);


    }

    private static void when_subsribeOn_is_not_specified() {

        // if subscribe on is not specified, the chain will be executed on the caller's thread

        Observable<String> observable = getStringJustObservable();

        observable
                .map(s -> intensiveCalculation(s))
                .map(String::length)
                .subscribe(s -> System.out.println(Thread.currentThread().getName()));  // main

        observable
                .map(s -> intensiveCalculation(s))
                .map(String::length)
                .subscribe(s -> System.out.println(Thread.currentThread().getName()));

        observable
                .map(s -> intensiveCalculation(s))
                .map(String::length)
                .subscribe(s -> System.out.println(Thread.currentThread().getName()));
    }

    private static void when_io_scheduler_is_specified() {

        Observable<String> observable = getStringJustObservable();

        observable
                .subscribeOn(Schedulers.io())
                .map(s -> intensiveCalculation(s))
                .map(String::length)
                .subscribe(s -> System.out.println(Thread.currentThread().getName()));  // RxCachedThreadScheduler-x

        observable
                .subscribeOn(Schedulers.io())
                .map(s -> intensiveCalculation(s))
                .map(String::length)
                .subscribe(s -> System.out.println(Thread.currentThread().getName()));

        observable
                .subscribeOn(Schedulers.io())
                .map(s -> intensiveCalculation(s))
                .map(String::length)
                .subscribe(s -> System.out.println(Thread.currentThread().getName()));

        sleep(100000);
    }

    private static void one_thread_serve_both_Observers() {

        Observable<String> observable = getStringJustObservable()
                .subscribeOn(Schedulers.computation())
                .map(GlobalUtils::intensiveCalculation)
                .publish()
                .autoConnect(2);

        observable.subscribe(s -> System.out.println(Thread.currentThread().getName()));
        observable.subscribe(s -> System.out.println(Thread.currentThread().getName()));

        sleep(100000);
    }

    private static void from_callable() {

//        Most Observable factories, such as Observable.fromIterable() and
//        Observable.just(), will emit items on the Scheduler specified by subscribeOn(). For
//        factories such as Observable.fromCallable() and Observable.defer(), the
//        initialization of these sources will also run on the specified Scheduler when using
//        subscribeOn(). For instance, if you use Observable.fromCallable() to wait on a URL
//        response, you can actually do that work on the IO Scheduler so the main thread is not
//        blocking and waiting for it:

        Observable.fromCallable(() -> getResponse("https://api.github.com/users/thomasnield/starred"))
                .subscribeOn(Schedulers.io())
                .subscribe(s -> System.out.println(s));

        sleep(1000000);
    }

    private static void nuances_of_subscribeOn() {

//        It is important to note that subscribeOn() will have no practical effect with certain
//        sources (and will keep a worker thread unnecessarily on standby until that operation
//        terminates). This might be because these Observables already use a specific Scheduler,
//        and if you want to change it, you can provide a Scheduler as an argument. For
//        example, Observable.interval() will use Schedulers.computation() and will
//        ignore any subscribeOn()you specify otherwise. But you can provide a third argument to
//        specify a different Scheduler to use. Here, I specify Observable.interval() to
//        use Schedulers.newThread(), as shown here:

        Observable
                .interval(1, TimeUnit.SECONDS, Schedulers.newThread())
                .subscribe(i -> System.out.println(Thread.currentThread().getName())); //RxNewThreadScheduler-1


//        This also brings up another point: if you have multiple subscribeOn() calls on a given
//        Observable chain, the top-most one, or the one closest to the source, will win and cause
//        any subsequent ones to have no practical effect (other than unnecessary resource usage). If I
//        call subscribeOn() with Schedulers.computation() and then call subscribeOn() for
//        Schedulers.io(), Schedulers.computation() is the one that will be used:

        Observable<String> observable = getStringJustObservable();

        observable
                .subscribeOn(Schedulers.computation()) // this will win
                .filter(s -> s.charAt(0) == 'A')
                .subscribeOn(Schedulers.io())
                .subscribe(s -> System.out.println(s + " " + Thread.currentThread().getName()));

        sleep(100000);
    }

    private static void understanding_observeOn() {

//        The subscribeOn() operator instructs the source Observable which Scheduler to emit
//        emissions on. If subscribeOn() is the only concurrent operation in an Observable chain,
//        the thread from that Scheduler will work the entire Observable chain, pushing emissions
//        from the source all the way to the final Observer. The observeOn() operator, however,
//        will intercept emissions at that point in the Observable chain and switch them to a
//        different Scheduler going forward.

//        Unlike subscribeOn(), the placement of observeOn() matters. It will leave all operations
//        upstream on the default or subscribeOn()-defined Scheduler, but will switch to a
//        different Scheduler downstream. Here, I can have an Observable emit a series of strings
//        that are /-separated values and break them up on an IO Scheduler. But after that, I can
//        switch to a computation Scheduler to filter only numbers and calculate their sum, as
//        shown in the following code snippet:

        Observable.just("WHISKEY/27653/TANGO", "6555/BRAVO", "232352/5675675/FOXTROT")
                .subscribeOn(Schedulers.io())
                .flatMap(s -> Observable.fromArray(s.split("/")))
                .observeOn(Schedulers.computation())
                .filter(s -> {
                    System.out.println("filtering: " + s);
                    return s.matches("[0-9]+");
                })
                .map(Integer::valueOf)
                .reduce((total, next) -> total + next)
                .subscribe(i -> System.out.println(i + " " + Thread.currentThread().getName()));

        sleep(100000);

    }

    public static void main(String[] args) {
//        from();
//        understanding_subscribeOn();
//        when_subsribeOn_is_not_specified();
//        when_io_scheduler_is_specified();
//        one_thread_serve_both_Observers();
//        from_callable();
//        nuances_of_subscribeOn();
        understanding_observeOn();
    }



    private static String getResponse(String path) {
        try {
            return new Scanner(new URL(path).openStream(),
                    "UTF-8").useDelimiter("\\A").next();
        } catch (Exception e) {
            return e.getMessage();
        }
    }


}
