/*
 * Copyright (C) 2010 The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.rx

import spock.lang.Specification

/**
 * Test case for ToListReducer
 */
class ToArrayReducerTest extends Specification {

    def 'Test get item'() {
        def Mapper<String> preOpt = Mock(Mapper) {
            hasItem() >>> [true, true, true, true, false]
            getItem() >>> ["1", null, "2", null]
        }

        given:
        ToArrayReducer opt = new ToArrayReducer(preOpt)

        expect:
        opt.getItem() == ["1", null, "2", null] as String[]
    }
}
