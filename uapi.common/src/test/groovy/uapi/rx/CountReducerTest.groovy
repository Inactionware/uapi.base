package uapi.rx

import spock.lang.Specification

/**
 * Unit test for CountReducer
 */
class CountReducerTest extends Specification {

    def 'Test get count'() {
        given:
        Mapper<String> preOpt = Mock(Mapper) {
            hasItem() >>> [true, true, true, false]
            getItem() >>> [1, 2, 3, null]
        }

        when:
        CountReducer opt = new CountReducer(preOpt)

        then:
        opt.getItem() == 3
    }
}
