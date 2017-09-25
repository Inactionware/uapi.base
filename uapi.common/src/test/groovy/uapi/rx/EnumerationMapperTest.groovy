package uapi.rx

import spock.lang.Specification

/**
 * Unit tests for EnumerationMapper
 */
class EnumerationMapperTest extends Specification {

    def 'Test getItem'() {
        setup:
        Enumeration<String> enumeration = Mock(Enumeration) {
            hasMoreElements() >>> [true, true, true, false]
            nextElement() >>> [item1, item2, item3, item4]
        }

        when:
        EnumerationMapper<String> enumMapper = new EnumerationMapper(enumeration)

        then:
        enumMapper.getItem() == item1
        enumMapper.getItem() == item2
        enumMapper.getItem() == item3
//        enumMapper.getItem() == null

        where:
        item1   | item2 | item3 | item4
        '1'     | '2'   | '3'   | null
    }
}
