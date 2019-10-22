package com.alephreach.main.observabletypes;

public class SingleDemo {

    // Single<T> is an Observable<T> that will only emit one item
    // onSuccess() consolidates onNext() and onComplete() into a single event
    // when you subscribe against a Single<T>, you supply the lambdas for onSuccess() and onComplete();

//    interface SingleObserver<T> {
//        void onSubscribe(Disposable d);
//        void onSuccess(T value);
//        void onError(Throwable error);
//    }

    public static void main(String[] args) {

    }
}
