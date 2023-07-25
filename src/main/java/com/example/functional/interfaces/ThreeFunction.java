package com.example.functional.interfaces;

/**
 * Functional Interface to receive three parameters
 * 
 * @author Thiago de Luca
 *
 * @param <T1> First Parameter
 * @param <T2> Second Parameter
 * @param <T3> Third  Parameter
 * @param <R>  The return value
 */
@FunctionalInterface
public interface ThreeFunction<T1, T2, T3, R> {
	
	/**
	 * Apply method
	 * 
	 * @param t1 First Parameter
	 * @param t2 Second Parameter
	 * @param t3 Third  Parameter
	 * @return  The return value
	 */
    R apply(T1 t1, T2 t2, T3 t3);
}