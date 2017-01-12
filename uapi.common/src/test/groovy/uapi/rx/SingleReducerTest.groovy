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
 * Test for SingleReducer
 */
class SingleReducerTest extends Specification {

    def 'Test Get Item'() {
        def Mapper<String> preOpt = Mock(Mapper) {
            hasItem() >>> [true, false]
            getItem() >>> ["1"]
        }

        given:
        SingleReducer opt = new SingleReducer(preOpt)

        expect:
        opt.getItem() == "1"
        ! opt.hasItem()
    }

    def 'Test Get Item with default'() {
        def Mapper<String> preOpt = Mock(Mapper) {
            hasItem() >> false
            getItem() >> {throw new NoItemException()}
        }

        given:
        SingleReducer opt = new SingleReducer(preOpt, 0)

        expect:
        opt.getItem() == 0
    }

    def 'Test Get Item no item'() {
        def Mapper<String> preOpt = Mock(Mapper) {
            hasItem() >> false
            getItem() >> {throw new NoItemException()}
        }

        given:
        SingleReducer opt = new SingleReducer(preOpt)

        when:
        opt.getItem()

        then:
        thrown(NoItemException)
    }

    def 'Test Get Item no item2'() {
        def Mapper<String> preOpt = Mock(Mapper) {
            hasItem() >> true
            getItem() >> {throw new NoItemException()}
        }

        given:
        SingleReducer opt = new SingleReducer(preOpt)

        when:
        opt.getItem()

        then:
        thrown(NoItemException)
    }

    def 'Test Get Item no item3'() {
        def Mapper<String> preOpt = Mock(Mapper) {
            hasItem() >> true
            getItem() >> {throw new NoItemException()}
        }

        given:
        SingleReducer opt = new SingleReducer(preOpt, 0)

        expect:
        opt.getItem() == 0
    }

    def 'Test Get Item more item'() {
        def Mapper<String> preOpt = Mock(Mapper) {
            hasItem() >>> [true, true]
            getItem() >>> ["1", "2"]
        }

        given:
        SingleReducer opt = new SingleReducer(preOpt)

        when:
        opt.getItem()

        then:
        thrown(MoreItemException)
    }
}
