package com.alephreach.main.operators;

import com.alephreach.main.GlobalUtils;
import io.reactivex.functions.Function;
import jdk.nashorn.internal.objects.Global;

import java.lang.management.GarbageCollectorMXBean;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class CollectionOperator {

//    Collection operators will accumulate all emissions into a collection such as a list or map and
//    then emit that entire collection as a single emission. Collection operators are another form of
//    reducing operators since they consolidate emissions into a single one. We will cover them
//    separately since they are a significant category on their own, though.
//
//    Note that you should avoid reducing emissions into collections for the
//    sake of it. It can undermine the benefits of reactive programming where
//    items are processed in a beginning-to-end, one-at-a-time sequence. You
//    only want to consolidate emissions into collections when you are logically
//    grouping them in some way.

    private static void toList() {

//        For a given Observable<T>, it will collect
//        incoming emissions into a List<T> and then push that entire List<T> as a single emission
//        (through Single<List<T>>)

        GlobalUtils.getStringJustObservable()
                .toList()       // return Single<List<String>>
                .subscribe(s -> System.out.println(s)); // [Alpha, Beta, Gamma, Delta, Epsilon]

//        By default, toList() will use a standard ArrayList implementation. You can optionally
//        specify an integer argument to serve as the capacityHint, and that will optimize the
//        initialization of ArrayList to expect roughly that number of items:

        GlobalUtils.getRangeObservable(1, 10)
                .toList(10)
                .subscribe(s -> System.out.println(s)); // [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]

//        If you want to specify a different list implementation besides ArrayList, you can provide a
//        Callable lambda as an argument to construct one. In the following code snippet, I provide
//        a CopyOnWriteArrayList instance to serve as my list:

        GlobalUtils.getStringJustObservable()
                .toList(CopyOnWriteArrayList::new)  // CopyOnWriteArrayList c = new CopyOnWriteArrayList<>();
                .subscribe(s -> System.out.println(s));
    }

    private static void toSortedList() {

//        A different flavor of toList() is toSortedList(). This will collect the emissions into a
//        list that sorts the items naturally based on their Comparator implementation. Then, it will
//        emit that sorted List<T> forward to the Observer:

        GlobalUtils.getRandomJustIntegerObservable()
                .toSortedList()
                .subscribe(s -> System.out.println(s));

    }

    private static void toMap() {

//        For a given Observable<T>, the toMap() operator will collect emissions into Map<K,T>,
//        where K is the key type derived off a lambda Function<T,K> argument producing the key
//        for each emission.

//        If we want to collect strings into Map<Char,String>, where each string is keyed off their
//        first character, we can do it like this:

        Function<String, Character> toMapper = s -> s.charAt(0);

        GlobalUtils.getStringJustObservable()
                .toMap(toMapper)
                .subscribe(s -> System.out.println(s)); // {De=Delta, Be=Beta, Ga=Gamma, Al=Alpha, Ep=Epsilon}

//        If we wanted to yield a different value other than the emission to associate with the key, we
//        can provide a second lambda argument that maps each emission to a different value. We
//        can, for instance, map each first letter key with the length of that string:

        Function<String, Character> key = s -> s.charAt(0);
        Function<String, Integer> value = s -> s.length();

        GlobalUtils.getStringJustObservable()
                .toMap(key, value)
                .subscribe(s -> System.out.println(s)); // {A=5, B=4, D=5, E=7, G=5

//        By default, toMap() will use HashMap. You can also provide a third lambda argument that
//        provides a different map implementation. For instance, I can provide ConcurrentHashMap
//        instead of HashMap :

        GlobalUtils.getStringJustObservable()
                .toMap(key, value, ConcurrentHashMap::new)
                .subscribe(s -> System.out.println(s)); // {A=5, B=4, D=5, E=7, G=5}

//        Note that if I have a key that maps to multiple emissions, the last emission for that key is
//        going to replace subsequent ones. If I make the string length the key for each emission,
//        Alpha is going to be replaced by Gamma, which is going to be replaced by Delta:

        GlobalUtils.getStringJustObservable()
                .toMap(s -> s.length())                 // remember, map doesn't contain duplicates
                .subscribe(s -> System.out.println(s)); // {4=Beta, 5=Delta, 7=Epsilon}

    }

    private static void toMultiMap() {

//        If you want a given key to map to multiple emissions, you can use toMultiMap() instead,
//        which will maintain a list of corresponding values for each key. Alpha, Gamma, and
//        Delta will then all be put in a list that is keyed off the length five:

        GlobalUtils.getStringJustObservable()
                .toMultimap(String::length)
                .subscribe(s -> System.out.println(s));
    }

    private static void collect() {

//        When none of the collection operators have what you need, you can always use the
//        collect() operator to specify a different type to collect items into. For instance, there is no
//        toSet() operator to collect emissions into a Set<T>, but you can quickly use collect()
//        to effectively do this. You will need to specify two arguments that are built with lambda
//        expressions: initialValueSupplier, which will provide a new HashSetfor a new
//        Observer, and collector, which specifies how each emission is added to that HashSet:

        GlobalUtils.getStringJustObservable()
                .collect(HashSet::new, HashSet::add)
                .subscribe(s -> System.out.println(s));

//        Now our collect() operator will emit a single HashSet<String> containing all the
//        emitted values.
//        Use collect() instead of reduce() when you are putting emissions into a mutable object,
//        and you need a new mutable object seed each time. We can also use collect() for trickier
//        cases that are not straightforward collection implementations.
//
//        Say you added Google Guava as a dependency (h t t p s ://g i t h u b . c o m /g o o g l e /g u a v a ) and
//        you want to collect emissions into an ImmutableList. To create an ImmutableList , you
//        have to call its builder() factory to yield an ImmutableList.Builder<T>. You then call
//        its add() method to put items in the builder, followed by a call to build(), which returns a
//        sealed, final ImmutableList<T> that cannot be modified.
//        To collect emissions into ImmutableList, you can supply
//        an ImmutableList.Builder<T> for your first lambda argument and then add each
//        element through its add() method in the second argument. This will emit
//        ImmutableList.Builder<T> once it is fully populated, and you can map() it to its
//        build() call in order to emit an ImmutableList<T>:

    }

    public static void main(String[] args) {
//        toList();
//        toSortedList();
//        toMap();
        toMultiMap();
    }
}
