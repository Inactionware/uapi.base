/*
 * Copyright (C) 2010 The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.rx

import spock.lang.Ignore
import spock.lang.Specification
import uapi.GeneralException

/**
 * Tests for Reducer
 */
@Ignore
class ReducerTest extends Specification {

    def 'Test map method'() {
        given:
        def Mapper<String> preOpt = Mock(Mapper)
        def TestOp op = new TestOp(preOpt)

        when:
        op.map(null)

        then:
        thrown(GeneralException)
    }

    def 'Test flatmap method'() {
        given:
        def Mapper<String> preOpt = Mock(Mapper)
        def TestOp op = new TestOp(preOpt)

        when:
        op.flatmap(null)

        then:
        thrown(GeneralException)
    }

    def 'Test filter method'() {
        given:
        def Mapper<String> preOpt = Mock(Mapper)
        def TestOp op = new TestOp(preOpt)

        when:
        op.filter(null)

        then:
        thrown(GeneralException)
    }

    def 'Test limit method'() {
        given:
        def Mapper<String> preOpt = Mock(Mapper)
        def TestOp op = new TestOp(preOpt)

        when:
        op.limit(1)

        then:
        thrown(GeneralException)
    }

    def 'Test next method'() {
        given:
        def Mapper<String> preOpt = Mock(Mapper)
        def TestOp op = new TestOp(preOpt)

        when:
        op.next(null)

        then:
        thrown(GeneralException)
    }

    def 'Test foreach method'() {
        given:
        def Mapper<String> preOpt = Mock(Mapper)
        def TestOp op = new TestOp(preOpt)

        when:
        op.foreach(null)

        then:
        thrown(GeneralException)
    }

    class TestOp extends Reducer {

        def TestOp(Mapper previously) {
            super(previously)
        }

        @Override
        def Object getItem() throws NoItemException {
            return null
        }
    }
}
