/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.common;

/**
 * Useful functional interface is defined here
 */
public interface Functionals {

    @FunctionalInterface
    interface Extractor<I, O, T extends Throwable> {
        O accept(I instance) throws T;
    }

    /**
     * Create new data
     *
     * @param   <T>
     *          The created data's type
     */
    @FunctionalInterface
    interface Creator<T> {
        T accept();
    }

    /**
     * Convert input data to output data
     *
     * @param   <I>
     *          The input data type
     * @param   <O>
     *          The output data type
     */
    @FunctionalInterface
    interface Convert<I, O> {
        O accept(I in);
    }

    /**
     * Apply specific action on input data
     *
     * @param   <I>
     *          The input data type
     */
    @FunctionalInterface
    interface Action<I> {
        void accept(I in);
    }

    /**
     * Check input data by specific logic
     *
     * @param   <T>
     *          input data
     */
    @FunctionalInterface
    interface Filter<T> {
        boolean accept(T in);
    }

    /**
     * Select one item from multiple items
     *
     * @param   <T>
     *          Item type
     */
    interface FilterOne<T> {
        boolean accept(T in, T selected);
    }

    @FunctionalInterface
    interface Evaluator {
        boolean accept(IAttributed in);
    }
}
