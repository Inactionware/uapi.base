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
import uapi.common.Range;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * An Looper will generate IMapper on specified data source.
 * IMapper is a abstract handler for input data, more then one operator can be combined.
 */
public class Looper {

    /**
     * Construct IMapper on data array.
     *
     * @param   items
     *          The input data array
     * @param   <T>
     *          The input data type
     * @return  The operator which can emit data and combine other operator
     */
    @SafeVarargs
    public static <T> IMapper<T> on(
            final T... items
    ) {
        ArgumentChecker.required(items, "items");
        return new OrderedMapper<>(items);
    }

    /**
     * Construct IMapper on data collection.
     *
     * @param   items
     *          The input data collection
     * @param   <T>
     *          The input data type
     * @return  The operator which can emit data and combine other operator
     */
    public static <T> IMapper<T> on(
            final Collection<T> items
    ) {
        ArgumentChecker.required(items, "items");
        return new CollectionMapper<>(items);
    }

    /**
     * Construct IMapper on Iterator object
     *
     * @param   iterator
     *          A object which implement Iterator interface
     * @param   <T>
     *          The item type
     * @return  The operator which can emit data and combine other operator
     */
    public static <T> IMapper<T> on(
            final Iterator<T> iterator
    ) {
        ArgumentChecker.required(iterator, "iterator");
        return new IteratorMapper<>(iterator);
    }

    /**
     * Construct IMapper on Iterable object
     *
     * @param   iterable
     *          A object which implement Iterable interface
     * @param   <T>
     *          The item type
     * @return  The operator which can emit data and combine other operator
     */
    public static <T> IMapper<T> on(
            final Iterable<T> iterable
    ) {
        return on(iterable.iterator());
    }


    /**
     * Construct IMapper on Range object
     *
     * @param   range
     *          A object represent a range
     * @return  The operator which can emit data and combine other operator
     */
    public static IMapper<Integer> on(
            final Range range
    ) {
        ArgumentChecker.required(range, "range");
        return new IteratorMapper<>(range.iterator());
    }

    /**
     * Construct IMapper on Enumeration object
     *
     * @param   enumeration
     *          An enumeration object
     * @return  The operator which can emit data and combine other operator
     */
    public static <T> IMapper<T> on(
            final Enumeration<T> enumeration
    ) {
        return new EnumerationMapper<>(enumeration);
    }
}
