/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.codegen.internal

import spock.lang.Ignore
import spock.lang.Specification
import uapi.codegen.IHandlerHelper
import uapi.annotation.NotNull

import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.*
import javax.lang.model.type.DeclaredType
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.FileObject
import javax.tools.StandardLocation

/**
 * Unit test for BuilderContext
 */
class BuilderContextTest extends Specification {

    def 'Test get basic properties'() {
        given:
        def budrCtx = new BuilderContext(Mock(ProcessingEnvironment), Mock(RoundEnvironment))

        expect:
        budrCtx.getProcessingEnvironment() != null
        budrCtx.getRoundEnvironment() != null
        budrCtx.getLogger() != null
    }

    def 'Test get Filer'() {
        given:
        def procEnv = Mock(ProcessingEnvironment) {
            getFiler() >> Mock(Filer)
        }
        def roundEnv = Mock(RoundEnvironment)
        def budrCtx = new BuilderContext(procEnv, roundEnv)

        expect:
        budrCtx.getFiler() != null
    }

    def 'Test getElementsAnnotatedWith'() {
        given:
        def procEnv = Mock(ProcessingEnvironment)
        def roundEnv = Mock(RoundEnvironment) {
            getElementsAnnotatedWith(NotNull) >> elements
        }
        def budrCtx = new BuilderContext(procEnv, roundEnv)

        expect:
        budrCtx.getElementsAnnotatedWith(NotNull) == elements

        where:
        elements                | placeholder
        [Mock(Element)] as Set  | null
    }

    def 'Test find class builder by element'() {
        given:
        def mockElemt = Mock(Element) {
            getKind() >> ElementKind.CLASS
            getSimpleName() >> Mock(Name) {
                toString() >> clsName
            }
        }
        def procEnv = Mock(ProcessingEnvironment) {
            getElementUtils() >> Mock(Elements) {
                getPackageOf(mockElemt) >> Mock(PackageElement) {
                    getQualifiedName() >> Mock(Name) {
                        toString() >> pkgName
                    }
                }
            }
        }
        def roundEnv = Mock(RoundEnvironment)
        def budrCtx = new BuilderContext(procEnv, roundEnv)


        when:
        def found = budrCtx.findClassBuilder(mockElemt)
        def found2 = budrCtx.getBuilders()

        then:
        found != null
        found.className == clsName
        found.packageName == pkgName
        found2.size() == 1

        where:
        clsName | pkgName
        'AAA'   | 'com.test'
    }

    def 'Test find class builder by duplicated element'() {
        given:
        def mockElemt = Mock(Element) {
            getKind() >> ElementKind.CLASS
            getSimpleName() >> Mock(Name) {
                toString() >> clsName
            }
        }
        def procEnv = Mock(ProcessingEnvironment) {
            getElementUtils() >> Mock(Elements) {
                getPackageOf(mockElemt) >> Mock(PackageElement) {
                    getQualifiedName() >> Mock(Name) {
                        toString() >> pkgName
                    }
                }
            }
        }
        def roundEnv = Mock(RoundEnvironment)
        def budrCtx = new BuilderContext(procEnv, roundEnv)


        when:
        budrCtx.findClassBuilder(mockElemt)
        def found = budrCtx.findClassBuilder(mockElemt)

        then:
        found != null
        found.className == clsName
        found.packageName == pkgName
        budrCtx.getBuilders().size() == 1

        where:
        clsName | pkgName
        'AAA'   | 'com.test'
    }

    @Ignore
    def 'Test load template'() {
        given:
        def filer = Mock(Filer) {
            getResource(StandardLocation.CLASS_PATH, _ as String, tempPath) >> Mock(FileObject) {
                openReader(_) >> Mock(Reader)
            }
        }
        def procEnv = Mock(ProcessingEnvironment) {
            getFiler() >> filer
        }
        def roundEnv = Mock(RoundEnvironment)
        def budrCtx = new BuilderContext(procEnv, roundEnv)

        when:
        def temp = budrCtx.loadTemplate(tempPath)

        then:
        noExceptionThrown()
        temp != null

        where:
        tempPath                            | placeholder
        'template/generated_sources.ftl'    | null
    }

    def 'Test put and get handler helper'() {
        given:
        def procEnv = Mock(ProcessingEnvironment)
        def roundEnv = Mock(RoundEnvironment)
        def budrCtx = new BuilderContext(procEnv, roundEnv)
        def handlerHelper = Mock(IHandlerHelper) {
            getName() >> 'name'
        }

        when:
        budrCtx.putHelper(handlerHelper)

        then:
        budrCtx.getHelper('name') == handlerHelper
    }

    def 'Test is assignable'() {
        given:
        def type1Elem = Mock(TypeElement)
        def type2Elem = Mock(TypeElement)
        def dtype1 = Mock(DeclaredType)
        def dtype2 = Mock(DeclaredType)
        def procEnv = Mock(ProcessingEnvironment) {
            getElementUtils() >> Mock(Elements) {
                getTypeElement(type1) >> type1Elem
                getTypeElement(type2) >> type2Elem
            }
            getTypeUtils() >> Mock(Types) {
                getDeclaredType(type1Elem) >> dtype1
                getDeclaredType(type2Elem) >> dtype2
                isAssignable(dtype1, dtype2) >> true
            }
        }
        def roundEnv = Mock(RoundEnvironment)
        def budrCtx = new BuilderContext(procEnv, roundEnv)

        expect:
        budrCtx.isAssignable(type1, type2)

        where:
        type1   | type2
        'A'     | 'B'
    }

    def 'Test is assignable by class'() {
        given:
        def type1Elem = Mock(TypeElement)
        def type2Elem = Mock(TypeElement)
        def dtype1 = Mock(DeclaredType)
        def dtype2 = Mock(DeclaredType)
        def procEnv = Mock(ProcessingEnvironment) {
            getElementUtils() >> Mock(Elements) {
                getTypeElement(type1) >> type1Elem
                getTypeElement(type2) >> type2Elem
            }
            getTypeUtils() >> Mock(Types) {
                getDeclaredType(type1Elem) >> dtype1
                getDeclaredType(type2Elem) >> dtype2
                isAssignable(dtype1, dtype2) >> true
            }
        }
        def roundEnv = Mock(RoundEnvironment)
        def budrCtx = new BuilderContext(procEnv, roundEnv)

        expect:
        budrCtx.isAssignable(type1, String.class)

        where:
        type1   | type2
        'A'     | 'java.lang.String'
    }
}
