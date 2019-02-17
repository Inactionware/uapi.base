package uapi.common

import spock.lang.Specification

class NumericTest extends Specification {

    def 'Test increase mutable integer'() {
        given:
        def mInt = Numeric.mutableInteger()

        when:
        mInt.increase()

        then:
        mInt.value() == 1
    }

    def 'Test decrease mutable integer'() {
        given:
        def mInt = Numeric.mutableInteger()

        when:
        mInt.decrease()

        then:
        mInt.value() == -1
    }

    def 'Test set mutable integer'() {
        given:
        def mInt = Numeric.mutableInteger()

        when:
        mInt.setValue(newValue)
        mInt.increase()
        mInt.decrease()
        mInt.decrease()

        then:
        mInt.value() == expectedValue

        where:
        newValue    | expectedValue
        100         | 99
    }
}
