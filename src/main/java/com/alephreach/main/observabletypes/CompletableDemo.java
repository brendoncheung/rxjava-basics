package com.alephreach.main.observabletypes;

import com.alephreach.main.GlobalUtils;
import io.reactivex.Completable;

public class CompletableDemo {

//    Completable is simply concerned with an action being executed, but it does not receive
//    any emissions. Logically, it does not have onNext() or onSuccess() to receive emissions,
//    but it does have onError() and onComplete():
//
//    interface CompletableObserver<T> {
//        void onSubscribe(Disposable d);
//        void onComplete();
//        void onError(Throwable error);
//    }

    public static void main(String[] args) {

        Completable.fromRunnable(() -> GlobalUtils.sleep(5000))
                .subscribe(() -> System.out.println("done"));

    }
}
