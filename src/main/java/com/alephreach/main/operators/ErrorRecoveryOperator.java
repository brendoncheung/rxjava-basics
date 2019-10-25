package com.alephreach.main.operators;

import com.alephreach.main.GlobalUtils;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.functions.Function;

public class ErrorRecoveryOperator {

//    Exceptions can occur in your Observable chain across many operators depending on what
//    you are doing. We already know about the onError() event that is communicated down
//    the Observable chain to the Observer. After that, the subscription terminates and no
//    more emissions will occur. But sometimes, we want to intercept exceptions before they get
//    to the Observer and attempt some form of recovery. We cannot necessarily pretend that
//    the error never happened and expect emissions to resume, but we can attempt resubscribing
//    or switch to an alternate source Observable.

    private static void onErrorReturn() {

//        When you want to resort to a default value when an exception occurs, you can use
//        onErrorReturnItem(). If we want to emit -1 when an exception occurs, we can do it like
//        this:

        Function<Integer, Integer> mapper = i -> 10 / i;

        // the code below will stop emission when the operator reaches the 0
        GlobalUtils.getRandomJustIntegerObservable()    // Observable.just(8, 5, 6, 4, 3, 0, 7, 10, 2, 17);
                .map(mapper)
                .subscribe(s -> System.out.println(s), e -> System.out.println(e));

        System.out.println("======");

        // this returns a default value and stops

        GlobalUtils.getRandomJustIntegerObservable()    // Observable.just(8, 5, 6, 4, 3, 0, 7, 10, 2, 17);
                .map(mapper)
                .onErrorReturnItem(-1)
                .subscribe(s -> System.out.println(s), e -> System.out.println(e));

        System.out.println("======");

        Function<Throwable, Integer> onReact = e -> e instanceof ArithmeticException ? 100 : 0;


        // this returns a value based on what exception you get
        GlobalUtils.getRandomJustIntegerObservable()    // Observable.just(8, 5, 6, 4, 3, 0, 7, 10, 2, 17);
                .map(mapper)
                .onErrorReturn(onReact)
                .subscribe(s -> System.out.println(s), e -> System.out.println(e));
    }

    private static void onErrorResumeNext() {

//        Similar to onErrorReturn() and onErrorReturnItem(), onErrorResumeNext() is very
//        similar. The only difference is that it accepts another Observable as a parameter to emit
//        potentially multiple values, not a single value, in the event of an exception.
//        This is somewhat contrived and likely has no business use case, but we can emit three -1
//        emissions in the event of an error:

        Function<Integer, Integer> mapper = i -> 10 / i;

        GlobalUtils.getRandomJustIntegerObservable()
                .map(mapper)
                .onErrorResumeNext(Observable.just(-1).repeat(3))
                .subscribe(s -> System.out.println(s), e -> System.out.println(e));

        // we can pass in a Observable.empty() to quietly stop the emission

        GlobalUtils.getRandomJustIntegerObservable()
                .map(mapper)
                .onErrorResumeNext(Observable.empty())
                .subscribe(s -> System.out.println(s));
    }

    private static void retry() {

//        Another way to attempt recovery is to use the retry() operator, which has several
//        parameter overloads. It will re-subscribe to the preceding Observable and, hopefully, not
//        have the error again.

        // retry twice, if you don't specify, it will retry until no exception is thrown
        GlobalUtils.getRandomJustIntegerObservable()
                .map(i -> 10 / i)
                .retry(2)
                .onErrorResumeNext(Observable.empty())
                .subscribe(s -> System.out.println(s));
    }

    public static void main(String[] args) {
//        onErrorReturn();
//        onErrorResumeNext();
        retry();
    }


}
