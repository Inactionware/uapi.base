/*
 * Copyright (c) 2019. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product.
 */

package uapi.rx

import spock.lang.Specification

class LastReducerTest extends Specification {

    def 'Test get item'() {
        Mapper<String> preOpt = Mock(Mapper) {
            hasItem() >>> [true, true, true, true, false]
            getItem() >>> ["1", null, "2", null]
        }

        given:
        LastReducer opt = new LastReducer(preOpt)

        expect:
        opt.getItem() == "2"
        ! opt.hasItem()
    }

    def 'Test get item with default'() {
        Mapper<String> preOpt = Mock(Mapper) {
            hasItem() >>> [false]
        }

        given:
        LastReducer opt = new LastReducer(preOpt, "1")

        expect:
        opt.getItem() == "1"
        ! opt.hasItem()
    }

}
