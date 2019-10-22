package com.alephreach.main.observabletypes;

import io.reactivex.Maybe;

public class MaybeDemo {

    // allows no emissions to occur all at
    // A given Maybe<T> will only emit 0 or 1 emissions

//    public interface MaybeObserver<T> {
//        void onSubscribe(Disposable d);
//        void onSuccess(T value);
//        void onError(Throwable e);
//        void onComplete();
//    }

    public static void main(String[] args) {

        Maybe<Integer> presentSource = Maybe.just(100);
        presentSource.subscribe(s -> System.out.println("Process 1 received: " + s),
                                Throwable::printStackTrace,
                                () -> System.out.println("Process 1 done!"));
        //no emission
        Maybe<Integer> emptySource = Maybe.empty();
        emptySource.subscribe(s -> System.out.println("Process 2 received: " + s),
                                Throwable::printStackTrace,
                                () -> System.out.println("Process 2 done!"));
    }
}
