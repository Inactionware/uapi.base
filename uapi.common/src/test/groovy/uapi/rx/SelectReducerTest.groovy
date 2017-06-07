package uapi.rx

import spock.lang.Specification

/**
 * Unit test for SelectReducer
 */
class SelectReducerTest extends Specification {

    def 'Test Get Item'() {
        def Mapper<String> preOpt = Mock(Mapper) {
            hasItem() >>> [true, false]
            getItem() >>> ["1"]
        }

        given:
        SelectReducer reducer = new SelectReducer(preOpt, { item, selected -> return true})

        expect:
        reducer.getItem() == "1"
        ! reducer.hasItem()
    }

    def 'Test Get Item from multiple'() {
        def Mapper<String> preOpt = Mock(Mapper) {
            hasItem() >>> [true, true, true, false]
            getItem() >>> [1, 2, 3]
        }

        given:
        SelectReducer reducer = new SelectReducer(preOpt, { item, selected -> return item >= selected})

        expect:
        reducer.getItem() == 3
        ! reducer.hasItem()
    }
}
