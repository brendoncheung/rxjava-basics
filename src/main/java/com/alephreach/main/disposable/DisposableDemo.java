package com.alephreach.main.disposable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.ResourceObserver;

import java.util.concurrent.TimeUnit;

public class DisposableDemo {

    public static void main(String[] args) {

        // subscribeWith() returns the observer type that you just put in

        // subscribe() returns void if you pass in the observer type,
        // this is assuming you are handling the disposing within the observer

        // use ResourceObserver if you want rxjava to handle your disposing

        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS);

        ResourceObserver<Long> observer1 = new ResourceObserver<Long>() {
            @Override
            public void onNext(Long aLong) {
                System.out.println(aLong);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };


        Observer<Long> observer2 = new Observer<Long>() {

            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long aLong) {
                System.out.println(aLong);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };

        Disposable disposable = observable.subscribeWith(observer1);


     }
}
