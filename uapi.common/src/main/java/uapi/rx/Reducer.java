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

    Reducer(Mapper previously) {
        super(previously);
    }
}
