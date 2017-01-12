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
import uapi.GeneralException

/**
 * Test for SumReducer
 */
class SumReducerTest extends Specification {

    def 'Test get int item'() {
        def Mapper<String> preOpt = Mock(Mapper) {
            hasItem() >>> [true, true, true, false]
            getItem() >>> [1, 2, 3, null]
        }

        when:
        SumReducer opt = new SumReducer(preOpt)

        then:
        opt.getItem() == 6
    }

    def 'Test get float item'() {
        def Mapper<String> preOpt = Mock(Mapper) {
            hasItem() >>> [true, true, true, false]
            getItem() >>> [1.1f, 2.1f, 3.1f, null]
        }

        when:
        SumReducer opt = new SumReducer(preOpt)

        then:
        opt.getItem() == 6.3f
    }

    def 'Test get double item'() {
        def Mapper<String> preOpt = Mock(Mapper) {
            hasItem() >>> [true, true, true, false]
            getItem() >>> [1.1d, 2.1d, 3.1d, null]
        }

        when:
        SumReducer opt = new SumReducer(preOpt)

        then:
        opt.getItem() == 6.3d
    }

    def 'Test get long item'() {
        def Mapper<String> preOpt = Mock(Mapper) {
            hasItem() >>> [true, true, true, false]
            getItem() >>> [1l, 2l, 3l, null]
        }

        when:
        SumReducer opt = new SumReducer(preOpt)

        then:
        opt.getItem() == 6l
    }

    def 'Test unsupported item'() {
        def Mapper<String> preOpt = Mock(Mapper) {
            hasItem() >>> [true, true, true, false]
            getItem() >>> [false, 2l, 3l, null]
        }

        when:
        SumReducer opt = new SumReducer(preOpt)
        opt.getItem()

        then:
        thrown(GeneralException)
    }
}
