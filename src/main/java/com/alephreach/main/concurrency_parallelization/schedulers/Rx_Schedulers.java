package com.alephreach.main.concurrency_parallelization.schedulers;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        
    }

    public static void main(String[] args) {
        from();
    }

}
