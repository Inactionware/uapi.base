/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.codegen

import spock.lang.Specification

import javax.lang.model.element.Modifier

/**
 * Test case for FieldMeta
 */
class FieldMetaTest extends Specification {

    def 'Test build'() {
        when:
        FieldMeta fieldMeta = FieldMeta.builder()
                .setIsList(isList)
                .setIsMap(isMap)
                .setName(name)
                .setTypeName(type)
                .setValue(value)
                .addModifier(modify1)
                .addModifier(modify2)
                .build()

        then:
        fieldMeta.getIsList() == isList
        fieldMeta.getIsMap() == isMap
        fieldMeta.getName() == name
        fieldMeta.getTypeName() == type
        fieldMeta.getModifiers() == modifies

        where:
        name    | type      | modify1         | modify2        | modifies       | isList    | isMap     | value
        'Test'  | 'String'  | Modifier.PUBLIC | Modifier.FINAL | "public final" | true      | false     | 'a'
    }

    def 'Test equals and hashcode'() {
        when:
        FieldMeta.Builder fieldBuilder1 = FieldMeta.builder()
                .setIsList(isList)
                .setIsMap(isMap)
                .setName(name)
                .setTypeName(type)
                .addModifier(modify1)
                .addModifier(modify2)
        FieldMeta.Builder fieldBuilder2 = FieldMeta.builder()
                .setIsList(isList)
                .setIsMap(isMap)
                .setName(name)
                .setTypeName(type)
                .addModifier(modify1)
                .addModifier(modify2)
        FieldMeta.Builder fieldBuilder3 = FieldMeta.builder()
                .setIsList(isList)
                .setIsMap(isMap)
                .setName(name)
                .setTypeName(type)
                .addModifier(modify1)

        then:
        fieldBuilder1 == fieldBuilder2
        fieldBuilder1 != fieldBuilder3
        fieldBuilder2 != fieldBuilder3
        fieldBuilder1.hashCode() == fieldBuilder2.hashCode()
        fieldBuilder1.hashCode() != fieldBuilder3.hashCode()
        fieldBuilder2.hashCode() != fieldBuilder3.hashCode()

        where:
        name    | type      | modify1         | modify2        | modifies       | isList    | isMap
        'Test'  | 'String'  | Modifier.PUBLIC | Modifier.FINAL | "public final" | true      | false
    }

    def 'Test Builder.toString'() {
        when:
        FieldMeta.Builder builder = FieldMeta.builder()
                .setIsList(isList)
                .setIsMap(isMap)
                .setName(name)
                .setTypeName(type)
                .addModifier(modify1)
                .addModifier(modify2)

        then:
        builder.toString() == string

        where:
        name    | type      | modify1         | modify2        | modifies       | isList    | isMap | string
        'Test'  | 'String'  | Modifier.PUBLIC | Modifier.FINAL | "public final" | true      | false | 'FieldMeta[name=Test, typeName=String, value=, isList=true, isMap=false, modifiers=[public, final]'
    }

    def 'Test toString'() {
        when:
        FieldMeta meta = FieldMeta.builder()
                .setIsList(isList)
                .setIsMap(isMap)
                .setName(name)
                .setTypeName(type)
                .addModifier(modify1)
                .addModifier(modify2)
                .build()

        then:
        meta.toString() == string

        where:
        name    | type      | modify1         | modify2        | modifies       | isList    | isMap | string
        'Test'  | 'String'  | Modifier.PUBLIC | Modifier.FINAL | "public final" | true      | false | 'FieldMeta[name=Test, typeName=String, value=, isList=true, isMap=false, modifiers=[public, final]'
    }
}
