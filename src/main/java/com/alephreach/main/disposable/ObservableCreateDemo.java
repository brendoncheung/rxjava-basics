package com.alephreach.main.disposable;


import io.reactivex.Observable;

public class ObservableCreateDemo {

    public static void main(String[] args) {
        Observable<Integer> source = Observable.create(emitter -> {

            for(int i = 0; i <= 10; i++) {
                while(!emitter.isDisposed()) {
                    emitter.onNext(i);
                }

                if(emitter.isDisposed()) {
                    return;
                }
            }

            emitter.onComplete();
        });
    }
}
