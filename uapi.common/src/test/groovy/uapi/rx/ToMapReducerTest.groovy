package uapi.rx

import spock.lang.Specification
import uapi.common.Pair

/**
 * Test case for ToMapReducer
 */
class ToMapReducerTest extends Specification {

    def 'Test getItem method'() {
        def Mapper<String> preOpt = Mock(Mapper) {
            hasItem() >>> [true, true, true, true, false]
            getItem() >>> [new Pair("1", "2"), new Pair("2", "3"), new Pair("3", "4"), null]
        }

        given:
        ToMapReducer opt = new ToMapReducer(preOpt)

        expect:
        opt.getItem() == ["1":"2", "2":"3", "3":"4"]
    }
}
