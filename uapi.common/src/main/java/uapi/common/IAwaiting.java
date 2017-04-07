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
 * The interface support await feature
 */
public interface IAwaiting {

    /**
     * Await on current object until someone condition is satisfied or specific time is out
     *
     * @param   waitTime
     *          The wait time
     * @return  true means the condition is satisfied, otherwise means the condition is not satisfied
     */
    boolean await(long waitTime);
}
