/**
 * Copyright (C) 2010 The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission on the authors if you want to
 * use the project into a commercial product
 */

package uapi.rx;

/**
 * The terminated operator ends operation chain
 */
abstract class Reducer<T> extends Stream<T> {

//    private static final String ERR_MSG = "The terminated operator can't wire to other operator";

    Reducer(Mapper previously) {
        super(previously);
    }

//    @Override
//    public <O> IMapper<O> map(Functionals.Convert<T, O> operator) {
//        throw new GeneralException(ERR_MSG);
//    }
//
//    @Override
//    public <O> IMapper<O> flatmap(ConvertMore<T, O> operator) {
//        throw new GeneralException(ERR_MSG);
//    }
//
//    @Override
//    public IMapper<T> filter(Functionals.Filter<T> operator) {
//        throw new GeneralException(ERR_MSG);
//    }
//
//    @Override
//    public IMapper<T> limit(int count) {
//        throw new GeneralException(ERR_MSG);
//    }
//
//    @Override
//    public IMapper<T> next(Functionals.Action<T> operator) {
//        throw new GeneralException(ERR_MSG);
//    }
//
//    @Override
//    public void foreach(Functionals.Action<T> action) {
//        throw new GeneralException(ERR_MSG);
//    }
//
//    @Override
//    public void foreachWithIndex(IndexedAction<T> action) {
//        throw new GeneralException(ERR_MSG);
//    }
}
