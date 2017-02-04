package uapi.rx

import spock.lang.Specification

/**
 * Unit test for SkipMapper
 */
class SkipMapperTest extends Specification {

    def 'Test get item'() {
        def preOpt = Mock(Mapper) {
            hasItem() >>> [true, true, true, true, false]
            getItem() >>> ["1", null, "2", null]
        }

        given:
        SkipMapper opt = new SkipMapper(preOpt, 1)

        expect:
        opt.getItem() == null
        opt.getItem() == '2'
        opt.getItem() == null
        ! opt.hasItem()
    }

    def 'Test get item with 0'() {
        def preOpt = Mock(Mapper) {
            hasItem() >>> [true, true, true, true, false]
            getItem() >>> ["1", null, "2", null]
        }

        given:
        SkipMapper opt = new SkipMapper(preOpt, 0)

        expect:
        opt.getItem() == '1'
        opt.getItem() == null
        opt.getItem() == '2'
        opt.getItem() == null
        ! opt.hasItem()
    }
}
