package com.alephreach.main.disposable;

import com.alephreach.main.GlobalUtils;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import java.util.concurrent.TimeUnit;

public class CompositeDisposableDemo {

    private static final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static void main(String[] args) {

        // if you have several subscriptions that need to be managed and disposed of

        Observable<Long> seconds = Observable.interval(1, TimeUnit.SECONDS);

        Disposable disposable1 = seconds.subscribe(i -> System.out.println("Observer1 1: " + i));
        Disposable disposable2 = seconds.subscribe(i -> System.out.println("Observer1 2: " + i));

        compositeDisposable.addAll(disposable1, disposable2);

        GlobalUtils.sleep(5000);

        compositeDisposable.dispose();

        GlobalUtils.sleep(5000);


    }
}
