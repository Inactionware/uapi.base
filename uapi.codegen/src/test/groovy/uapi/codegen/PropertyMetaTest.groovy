package uapi.codegen

import spock.lang.Specification
import uapi.GeneralException

class PropertyMetaTest extends Specification {

    def 'Test create instance'() {
        given:
        def propBuilder = PropertyMeta.builder()
                .setFieldName(fieldName)
                .setFieldType(fieldType)
                .setGenerateField(genField)
                .setGenerateGetter(genGetter)
                .setGenerateSetter(genSetter)
                .setIsCollection(isCollection)
                .setIsMap(isMap)

        when:
        def budr = propBuilder.createInstance()

        then:
        budr.fieldName() == fieldName
        budr.fieldType() == fieldType
        budr.generateField() == genField
        budr.generateGetter() == genGetter
        budr.generateSetter() == genSetter
        budr.isCollection() == isCollection
        budr.isMap() == isMap
        budr.setterName() == setterName
        budr.getterName() == getterName

        where:
        fieldName   | fieldType     | genField  | genGetter | genSetter | isCollection  | isMap | setterName    | getterName
        '_a'        | 'String'      | true      | false     | true      | false         | false | 'setA'        | 'getA'
        '_a'        | 'int'         | true      | false     | true      | true          | false | 'addA'        | 'getA'
        '_a'        | 'int'         | true      | false     | true      | false         | true  | 'putA'        | 'getA'
        'a'         | 'int'         | true      | false     | true      | false         | false | 'setA'        | 'getA'
    }

    def 'Test create instance with invalid argument'() {
        given:
        def propBuilder = PropertyMeta.builder()
                .setFieldName(fieldName)
                .setFieldType(fieldType)
                .setGenerateField(genField)
                .setGenerateGetter(genGetter)
                .setGenerateSetter(genSetter)
                .setIsCollection(isCollection)
                .setIsMap(isMap)

        when:
        propBuilder.validate()

        then:
        thrown(GeneralException)

        where:
        fieldName   | fieldType     | genField  | genGetter | genSetter | isCollection  | isMap
        null        | 'String'      | true      | false     | true      | false         | false
        '_a'        | null          | true      | false     | true      | false         | false
        '_a'        | 'int'         | true      | false     | true      | true          | true
    }

    def 'Test equals'() {
        given:
        def propBuilder1 = PropertyMeta.builder()
                .setFieldName(fieldName)
                .setFieldType(fieldType)
                .setGenerateField(genField)
                .setGenerateGetter(genGetter)
                .setGenerateSetter(genSetter)
                .setIsCollection(isCollection)
                .setIsMap(isMap)
        def propBuilder2 = PropertyMeta.builder()
                .setFieldName(fieldName)
                .setFieldType(fieldType)
                .setIsCollection(isCollection)
                .setIsMap(isMap)
        def propBuilder3 = PropertyMeta.builder()
                .setFieldType(fieldType)
                .setIsCollection(isCollection)
                .setIsMap(isMap)
        def propBuilder4 = PropertyMeta.builder()
                .setFieldName(fieldName)
                .setIsCollection(isCollection)
                .setIsMap(isMap)
        def propBuilder5 = PropertyMeta.builder()
                .setFieldName(fieldName)
                .setFieldType(fieldType)
                .setIsMap(isMap)
        def propBuilder6 = PropertyMeta.builder()
                .setFieldName(fieldName)
                .setFieldType(fieldType)
                .setIsCollection(isCollection)

        expect:
        propBuilder1 == propBuilder2
        propBuilder1 != propBuilder3
        propBuilder1 != propBuilder4
        propBuilder1 != propBuilder5
        propBuilder1 != propBuilder6

        where:
        fieldName   | fieldType     | genField  | genGetter | genSetter | isCollection  | isMap
        'a'         | 'String'      | true      | false     | true      | true          | true
    }

    def 'Test to string'() {
        given:
        def propBuilder = PropertyMeta.builder()
                .setFieldName(fieldName)
                .setFieldType(fieldType)
                .setGenerateField(genField)
                .setGenerateGetter(genGetter)
                .setGenerateSetter(genSetter)
                .setIsCollection(isCollection)
                .setIsMap(isMap)
        expect:
        propBuilder.toString() == stringBuilder

        where:
        fieldName   | fieldType     | genField  | genGetter | genSetter | isCollection  | isMap | stringBuilder
        'a'         | 'String'      | true      | false     | true      | true          | true  | 'PropertyMeta[fieldName=a, fieldType=String, generateField=true, generateSetter=true, generateGetter=false, isCollection=true, isMap=true]'
    }

    def 'Test hashcode'() {
        given:
        def propBuilder = PropertyMeta.builder()
                .setFieldName(fieldName)
                .setFieldType(fieldType)
                .setGenerateField(genField)
                .setGenerateGetter(genGetter)
                .setGenerateSetter(genSetter)
                .setIsCollection(isCollection)
                .setIsMap(isMap)
        expect:
        propBuilder.hashCode() == hashcode

        where:
        fieldName   | fieldType     | genField  | genGetter | genSetter | isCollection  | isMap | hashcode
        'a'         | 'String'      | true      | false     | true      | true          | true  | Objects.hash('a', 'String', true, true)
    }
}
