package com.alephreach.main.operators;

//You will likely have moments where you want to take a series of emissions and consolidate
//them into a single emission (usually emitted through a Single). We will cover a few
//operators that accomplish this. Note that nearly all of these operators only work on a finite
//Observable that calls onComplete() because typically, we can consolidate only finite
//datasets. We will explore this behavior as we cover these operators.

import com.alephreach.main.GlobalUtils;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

import java.time.LocalDate;

public class ReducingOperators {

    private static void count() {

//        The simplest operator to consolidate emissions into a single one is count(). It will count
//        the number of emissions and emit through a Single once onComplete() is called, shown
//        as follows:

        GlobalUtils.getStringJustObservable()
                .count() // outputs a single observable
                .subscribe(s -> System.out.println(s));

//        Like most reduction operators, this should not be used on an infinite Observable. It will
//        hang up and work infinitely, never emitting a count or calling onComplete(). You should
//        consider using scan() to emit a rolling count instead.

    }

    private static void reduce() {

//        The reduce() operator is syntactically identical to scan(), but it only emits the final
//        accumulation when the source calls onComplete(). Depending on which overload you
//        use, it can yield Single or Maybe. If you want to emit the sum of all integer emissions, you
//        can take each one and add it to the rolling total. But it will only emit once it is finalized:

        Observable.just(5, 7, 4, 1, 10, 14)
                .reduce((accumulator, next) -> accumulator + next)
                .subscribe(s -> System.out.println(s));

        // scan() will produce the rolling aggregate, the final number will be the same as using reduce()

        Observable.just(5, 7, 4, 1, 10, 14)
                .reduce((accumulator, next) -> accumulator + next)
                .subscribe(s -> System.out.println(s));

//        Similar to scan(), there is a seed argument that you can provide that will serve as the
//        initial value to accumulate on. If we wanted to turn our emissions into a single commaseparated
//        value string, we could use reduce() like this, shown as follows:

        Observable.just(5, 3, 7, 10, 2, 14)
                .reduce("", (total, next) -> total + (total.equals("") ? "" : ",") + next)
                .subscribe(s -> System.out.println("Received: " + s)); // Received: 5,3,7,10,2,14

    }

    private static void all() {

//        The all() operator verifies that each emission qualifies with a specified condition and
//        return a Single<Boolean>. If they all pass, it will emit True. If it encounters one that fails,
//        it will immediately emit False. In the following code snippet, we emit a test against six
//        integers, verifying that they all are less than 10:

        Predicate<Integer> condition1 = i -> i <= 1000;

        GlobalUtils.getRandomJustIntegerObservable()    // Observable.just(8, 5, 6, 4, 3, 7, 10, 2, 17);
                .all(condition1)                        // check if the numbers inside are less than or equal to 1000
                .subscribe(s -> System.out.println(s));

        Predicate<Integer> condition2 = i -> i > 5;

        GlobalUtils.getRandomJustIntegerObservable()
                .all(condition2)                        // check if the number is less than 5
                .subscribe(s -> System.out.println(s)); // once the operator reach the 8 number, it immediately returns false
    }

    private static void any() {
//        The any() method will check whether at least one emission meets a specific criterion and
//        return a Single<Boolean>. The moment it finds an emission that qualifies, it will emit true
//        and then call onComplete(). If it processes all emissions and finds that they all are false, it
//        will emit false and call onComplete().

        Function<String, LocalDate> stringToDate = s -> LocalDate.parse(s);

        GlobalUtils.getDateJustStringObservable()
                .map(stringToDate)
                .any(date -> date.getMonthValue() >= 6)
                .subscribe(s -> System.out.println(s));
    }

    private static void contains() {

//        The contains() operator will check whether a specific element (based on the
//        hashCode()/equals() implementation) ever emits from an Observable. It will return a
//        Single<Boolean> that will emit true if it is found and false if it is not.

        GlobalUtils.getRangeObservable(1, 10000)
                .contains(9786)
                .subscribe(s -> System.out.println(s));
    }

    public static void main(String[] args) {
//        count();
//        reduce();
//        all();
//        any();
        contains();
    }

}
