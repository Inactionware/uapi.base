/**
 * Copyright (C) 2010 The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission on the authors if you want to
 * use the project into a commercial product
 */

package uapi.rx;

import uapi.GeneralException;
import uapi.common.Functionals;

/**
 * Created by min on 16/6/12.
 */
abstract class TerminatedOperator<T> extends Operator<T> {

    TerminatedOperator(Operator previously) {
        super(previously);
    }

    @Override
    public <O> IOperator<O> map(Functionals.Convert<T, O> operator) {
        throw new GeneralException("The terminated operator can't wire to other operator");
    }

    @Override
    public IOperator<T> filter(Functionals.Filter<T> operator) {
        throw new GeneralException("The terminated operator can't wire to other operator");
    }

    @Override
    public IOperator<T> limit(int count) {
        throw new GeneralException("The terminated operator can't wire to other operator");
    }

    @Override
    public void foreach(Functionals.Action<T> action) {
        throw new GeneralException("The terminated operator can't wire to other operator");
    }
}
