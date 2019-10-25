package com.alephreach.main.operators;

import com.alephreach.main.GlobalUtils;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class ActionOperators {

//    Some helpful operators that can assist in debugging as
//    well as getting visibility into an Observable chain. These are the action or doOn operators.

    private static void doOnNext() {

//        The doOnNext() operator allows you to peek at each emission coming out of an operator
//        and going into the next. This operator does not affect the operation or transform the
//        emissions in any way. We just create a side-effect for each event that occurs at that point in
//        the chain. For instance, we can perform an action with each string before it is mapped to its
//        length. In this case, we will just print them by providing a Consumer<T> lambda:

        Consumer<String> peekOnNext = System.out::println;

        GlobalUtils.getStringJustObservable()
                .doOnNext(peekOnNext)
                .map(String::length)
                .subscribe(s -> System.out.println(s));

        // you can use doAfterNext() which is called after the emission is sent to the consumer
    }

    private static void doOnComplete() {

        Action beforeEnd = () -> System.out.println("Before onComplete() is called on consumer");

        GlobalUtils.getStringJustObservable()
                .doOnComplete(beforeEnd)
                .map(String::length)
                .subscribe(i -> System.out.println(i),
                        Throwable::printStackTrace,
                        () -> System.out.println("onComplete Called"));
//        5
//        4
//        5
//        5
//        7
//        Before onComplete() is called on consumer
//        onComplete Called





    }

    private static void doOnError() {

//        onError() will peek at the error being emitted up the chain, and you can
//        perform an action with it. This can be helpful to put between operators to see which one is
//        to blame for an error:

        Function<Integer, Integer> mapper = i -> 10 / i;

        GlobalUtils.getRandomJustIntegerObservable()
                .map(mapper)
                .doOnError(e -> System.out.println("This error: >" + e.toString() + "< is about to happen!"))
                .subscribe(s -> System.out.println(s), Throwable::printStackTrace);
    }

    private static void doOnEach() {

        // have doOnNext() , doOnComplete(), doOnError() in one
        GlobalUtils.getStringJustObservable()
                .doOnEach(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("peek onSubscribe()");
                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println("peek onNext(): " + s);

                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("peek onError()");

                    }

                    @Override
                    public void onComplete() {
                        System.out.println("peek onComplete()");

                    }
                })
                .subscribe(s -> System.out.println(s));
    }

    private static void doOnSubscribe_doOnDispose() {
        GlobalUtils.getStringJustObservable()
                .doOnSubscribe(d -> System.out.println("about to subscribe!"))
                .doOnDispose(() -> System.out.println("about to be disposed"))
                .subscribe(s -> System.out.println(s));
    }

    private static void doFinally() {
        GlobalUtils.getStringJustObservable()
                .doFinally(() -> System.out.println("call after a onError() or onComplete()"))
                .subscribe(s -> System.out.println(s), Throwable::printStackTrace, () -> System.out.println("onComplete is called"));
    }

    private static void doOnSuccess() {

//        Remember that Maybe and Single types do not have an onNext() event but rather an
//        onSuccess() operator to pass a single emission. Therefore, there is no doOnNext()
//        operator on either of these types, as observed in the following code snippet, but rather a
//        doOnSuccess() operator. Its usage should effectively feel like doOnNext():

        GlobalUtils.getRandomJustIntegerObservable()
                .reduce((total, next) -> total += next)
                .doOnSuccess(i -> System.out.println("peek onto the final result: " + i))
                .subscribe(s -> System.out.println(s));
    }

    public static void main(String[] args) {
//        doOnNext();
//        doOnComplete();
//        doOnError();
//        doOnEach();
//        doOnSubscribe_doOnDispose();
//        doFinally();
        doOnSuccess();
    }
}
