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
import uapi.common.Pair;

import java.util.List;
import java.util.Map;

/**
 * Generic Mapper
 */
abstract class Mapper<T> extends Stream<T> implements IMapper<T> {

//    private final Mapper<?> _previously;

    Mapper() {
        super();
//        this._previously = null;
    }

    Mapper(Mapper<?> previously) {
//        ArgumentChecker.required(previously, "previously");
//        this._previously = previously;
        super(previously);
    }

//    Mapper<?> getPreviously() {
//        return this._previously;
//    }
//
//    boolean hasItem() {
//        return this._previously != null && this._previously.hasItem();
//    }
//
//    abstract T getItem() throws NoItemException;
//
//    void done() {
//        getPreviously().done();
//    }

    @Override
    public <O> IMapper<O> map(Functionals.Convert<T, O> operator) {
        return new MapMapper<>(this, operator);
    }

    @Override
    public <O> IMapper<O> flatmap(ConvertMore<T, O> operator) {
        return new FlatMapMapper<>(this, operator);
    }

    @Override
    public IMapper<T> filter(Functionals.Filter<T> operator) {
        return new FilterMapper<>(this, operator);
    }

    @Override
    public IMapper<T> limit(int count) {
        return new LimitMapper<>(this, count);
    }

    @Override
    public IMapper<T> next(Functionals.Action<T> operator) {
        return new NextMapper<>(this, operator);
    }

    // ----------------------------------------------------
    // Terminated operations
    // ----------------------------------------------------

    @Override
    public void foreach(Functionals.Action<T> action) {
        ForeachReducer operator = new ForeachReducer(this, action);
        operator.getItem();
        operator.end();
    }

    @Override
    public void foreachWithIndex(IndexedAction<T> action) {
        IndexedForeachReducer operator = new IndexedForeachReducer<>(this, action);
        operator.getItem();
        operator.end();
    }

    @Override
    public T first() {
        FirstOperator<T> operator = new FirstOperator<>(this);
        T result = operator.getItem();
        operator.end();
        return result;
    }

    @Override
    public T first(T defaultValue) {
        FirstOperator<T> operator = new FirstOperator<>(this, defaultValue);
        T result = operator.getItem();
        operator.end();
        return result;
    }

    @Override
    public T single() {
        SingleReducer<T> operator = new SingleReducer<T>(this);
        T result = operator.getItem();
        operator.end();
        return result;
    }

    @Override
    public T single(T defaultValue) {
        SingleReducer<T> operator = new SingleReducer<>(this, defaultValue);
        T result = operator.getItem();
        operator.end();
        return result;
    }

    @Override
    public T sum() {
        SumReducer<T> operator = new SumReducer<>(this);
        T result = operator.getItem();
        operator.end();
        return result;
    }

    @Override
    public List<T> toList() {
        ToListReducer<T> operator = new ToListReducer<>(this);
        List<T> result = operator.getItem();
        operator.end();
        return result;
    }

    @Override
    public <KT, VT> Map<KT, VT> toMap() {
        ToMapReducer<KT, VT> operator = new ToMapReducer<>((Mapper<Pair<KT, VT>>) this);
        Map<KT, VT> result = operator.getItem();
        operator.end();
        return result;
    }
}
