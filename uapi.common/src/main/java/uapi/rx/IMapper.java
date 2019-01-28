/**
 * Copyright (C) 2010 The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission on the authors if you want to
 * use the project into a commercial product
 */

package uapi.rx;

import uapi.common.Functionals;

import java.util.List;
import java.util.Map;

/**
 * All rx operator need to implement this interface
 */
public interface IMapper<T> {

    /**
     * Return an operator which can map input data to output data by specific logic
     *
     * @param   operator
     *          The logic used for map input data to output data
     * @param   <O>
     *          The output data type
     * @return  The map operator
     */
    <O> IMapper<O> map(Functionals.Convert<T, O> operator);

    /**
     * Return an operator which can map one input data to more than one output data by specific logic
     *
     * @param   operator
     *          The logic used for map input data to output data
     * @param   <O>
     *          The output data type
     * @return  The flatmap operator
     */
    <O> IMapper<O> flatmap(ConvertMore<T, O> operator);

    /**
     * Return a operator which can filter out input data by specific logic
     *
     * @param   operator
     *          The filter logic
     * @return  The filter operator instance
     */
    IMapper<T> filter(Functionals.Filter<T> operator);

    /**
     * Construct an operator which can limit data size by specified count
     *
     * @param   count
     *          Limited count
     * @return  The limitation operator
     */
    IMapper<T> limit(int count);

    /**
     * Construct an operator which can skip data item by specific count
     *
     * @return  The count of skipped item
     */
    IMapper<T> skip(int count);

    /**
     * Construct an operator which do specific action on each data
     *
     * @param   operator
     *          The operator which contains specific action
     * @return  The next operator instance
     */
    IMapper<T> next(Functionals.Action<T> operator);

    /**
     * Iterate all of input data by specific logic
     *
     * @param   action
     *          The iteration logic
     */
    void foreach(Functionals.Action<T> action);

    /**
     * Iterate all of input data with its index by specific logic
     *
     * @param   action
     *          The iteration logic
     */
    void foreachWithIndex(IndexedAction<T> action);

    /**
     * Return first element of data
     *
     * @return  The first element
     */
    T first() throws NoItemException;

    /**
     * Return first element of data or return default value if no element is reached
     *
     * @param   defaultValue
     *          Default value if no element is reached
     * @return  The first element or default value
     */
    T first(T defaultValue);

    /**
     * Except return only one element of input data
     *
     * @return  Single element
     * @throws  NoItemException
     *          If no element can be returned
     * @throws  MoreItemException
     *          Found more element can be returned
     */
    T single() throws NoItemException, MoreItemException;

    /**
     * Except return only one element of input data
     * The default value will be returned if no element can be returned
     *
     * @return  Single element
     * @throws  MoreItemException
     *          Found more element can be returned
     */
    T single(T defaultValue) throws MoreItemException;

    /**
     * Sum all item's value
     *
     * @return  The sum value
     * @throws  NoItemException
     *          If no item can be reached
     */
    T sum() throws NoItemException;

    /**
     * Count all item number
     *
     * @return  Item number
     */
    int count();

    /**
     * Return all element to a list, emptyArray list will be returned if no element
     *
     * @return  A list contains all element
     */
    List<T> toList();

    T[] toArray();

    /**
     * Return all element to a map
     *
     * @param   <KT>
     *          The type of key of the map
     * @param   <VT>
     *          The type of value of the value
     * @return  A map contains all element
     */
    <KT, VT> Map<KT, VT> toMap();

    T select(Functionals.FilterOne<T> operation);
}
