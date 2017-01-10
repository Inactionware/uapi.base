/**
 * Copyright (C) 2010 The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission on the authors if you want to
 * use the project into a commercial product
 */

package uapi.rx;

import uapi.common.ArgumentChecker;

import java.util.Collection;
import java.util.Iterator;

/**
 * An Looper will generate IOperator on specified data source.
 * IOperator is a abstract handler for input data, more then one operator can be combined.
 */
public class Looper {

    /**
     * Construct IOperator on data array.
     *
     * @param   items
     *          The input data array
     * @param   <T>
     *          The input data type
     * @return  The operator which can emit data and combine other operator
     */
    @SafeVarargs
    public static <T> IOperator<T> on(
            final T... items
    ) {
        ArgumentChecker.required(items, "items");
        return new OrderedSource<>(items);
    }

    /**
     * Construct IOperator on data collection.
     *
     * @param   items
     *          The input data collection
     * @param   <T>
     *          The input data type
     * @return  The operator which can emit data and combine other operator
     */
    public static <T> IOperator<T> on(
            final Collection<T> items
    ) {
        ArgumentChecker.required(items, "items");
        return new CollectionSource<>(items);
    }

    /**
     * Construct IOperator on Iterator object
     *
     * @param   iterator
     *          A object which implement Iterator interface
     * @param   <T>
     *          The item type
     * @return  The operator which can emit data and combine other operator
     */
    public static <T> IOperator<T> on(
            final Iterator<T> iterator
    ) {
        ArgumentChecker.required(iterator, "iterator");
        return new IteratorSource<>(iterator);
    }

    /**
     * Construct IOperator on Iterable object
     *
     * @param   iterable
     *          A object which implement Iterable interface
     * @param   <T>
     *          The item type
     * @return  The operator which can emit data and combine other operator
     */
    public static <T> IOperator<T> on(
            final Iterable<T> iterable
    ) {
        return on(iterable.iterator());
    }
}
