/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.rx;

/**
 * Operate one or more items like a stream
 */
public interface IStream<T> {

    /**
     * Check the stream has item or not
     *
     * @return  True means has next item, otherwise means no item
     */
    boolean hasItem();

    /**
     * Get next item
     *
     * @return  Next available item
     * @throws  NoItemException
     *          When no subsequent item available
     */
    T getItem() throws NoItemException;

    /**
     * End this stream
     */
    void end();
}
