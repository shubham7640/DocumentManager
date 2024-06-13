package com.springReact.DocumentManager.function;

//import java.util.Objects;
//import java.util.function.Consumer;

@FunctionalInterface
public interface TriConsumer<T,U,V> {
    /**
     * Performs this operation on the given argument.
     *
     * @param t the input argument
     */
    void accept(T t,U u, V v);


//    default TriConsumer<T,U,V> andThen(TriConsumer<? super T> after) {
//        Objects.requireNonNull(after);
//        return (T t,U u,V v) -> { accept(t,u,v); after.accept(t,u,v); };
//    }

}
