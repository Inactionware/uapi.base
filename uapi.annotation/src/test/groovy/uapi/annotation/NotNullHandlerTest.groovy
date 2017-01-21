/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.annotation

import spock.lang.Specification
import uapi.GeneralException
import uapi.InvalidArgumentException
import uapi.annotation.internal.NotNullHandler
import uapi.codegen.ClassMeta
import uapi.codegen.IBuilderContext
import uapi.codegen.MethodMeta
import uapi.codegen.ParameterMeta

import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.Name

/**
 * Unit test for NotNullHandler
 */
class NotNullHandlerTest extends Specification {

    def 'Test get ordered annotations'() {
        given:
        def handler = new NotNullHandler()

        expect:
        handler.getOrderedAnnotations() == [NotNull] as Class[]
    }

    def 'Test handle annotated elements with incorrect element'() {
        given:
        def paramElem = Mock(Element) {
            getKind() >> kind
            getSimpleName() >> Mock(Name) {
                toString() >> paramName
            }
        }
        def budrCtx = Mock(IBuilderContext)

        when:
        def handler = new NotNullHandler()
        handler.handleAnnotatedElements(budrCtx, NotNull.class, [paramElem] as Set)

        then:
        thrown(GeneralException)

        where:
        kind                        | paramName
        ElementKind.CLASS           | 'param'
        ElementKind.CONSTRUCTOR     | 'param'
        ElementKind.FIELD           | 'param'
        ElementKind.ENUM            | 'param'
        ElementKind.INTERFACE       | 'param'
        ElementKind.LOCAL_VARIABLE  | 'param'
    }

    def 'Test handler annotated elements with incorrect enclosing method modifiers'() {
        given:
        def paramElem = Mock(Element) {
            getKind() >> kind
            getSimpleName() >> Mock(Name) {
                toString() >> paramName
            }
            getEnclosingElement() >> Mock(Element) {
                getModifiers() >> ([incorrectModifer] as Set)
                getEnclosingElement() >> Mock(Element) {
                    getSimpleName() >> Mock(Name) {
                        toString() >> 'Class Element'
                    }
                }
                getSimpleName() >> Mock(Name) {
                    toString() >> 'Method Element'
                }
            }
        }
        def budrCtx = Mock(IBuilderContext)

        when:
        def handler = new NotNullHandler()
        handler.handleAnnotatedElements(budrCtx, NotNull.class, [paramElem] as Set)

        then:
        thrown(GeneralException)

        where:
        kind                        | paramName     | incorrectModifer
        ElementKind.PARAMETER       | 'param'       | Modifier.PRIVATE
        ElementKind.PARAMETER       | 'param'       | Modifier.FINAL
        ElementKind.PARAMETER       | 'param'       | Modifier.STATIC
    }

    def 'Test handler annotated elements with incorrect enclosing class modifiers'() {
        given:
        def paramElem = Mock(Element) {
            getKind() >> kind
            getSimpleName() >> Mock(Name) {
                toString() >> paramName
            }
            getEnclosingElement() >> Mock(Element) {
                getModifiers() >> ([Modifier.PUBLIC] as Set)
                getEnclosingElement() >> Mock(Element) {
                    getModifiers() >> ([incorrectModifer] as Set)
                    getSimpleName() >> Mock(Name) {
                        toString() >> 'Class Element'
                    }
                    getEnclosingElement() >> Mock(Element) {
                        getSimpleName() >> Mock(Name) {
                            toString() >> 'Package Element'
                        }
                    }
                }
            }
        }
        def budrCtx = Mock(IBuilderContext)

        when:
        def handler = new NotNullHandler()
        handler.handleAnnotatedElements(budrCtx, NotNull.class, [paramElem] as Set)

        then:
        thrown(GeneralException)

        where:
        kind                        | paramName     | incorrectModifer
        ElementKind.PARAMETER       | 'param'       | Modifier.PRIVATE
        ElementKind.PARAMETER       | 'param'       | Modifier.FINAL
        ElementKind.PARAMETER       | 'param'       | Modifier.STATIC
    }

    def 'Test handler annotated elements'() {
        given:
        def paramElem = Mock(Element) {
            getKind() >> kind
            getSimpleName() >> Mock(Name) {
                toString() >> paramName
            }
            getEnclosingElement() >> Mock(Element) {
                getModifiers() >> ([Modifier.PUBLIC] as Set)
                getEnclosingElement() >> Mock(Element) {
                    getModifiers() >> ([Modifier.PUBLIC] as Set)
                }
            }
        }
        def paramBuilder = Mock(ParameterMeta.Builder) {
            getName() >> paramName
        }
        def methodBuilder = Mock(MethodMeta.Builder)
        methodBuilder.findParameterBuilder(_, _) >> paramBuilder
        methodBuilder.addThrowTypeName(_) >> methodBuilder
        methodBuilder.setInvokeSuper(_) >> methodBuilder
        methodBuilder.addCodeBuilder(_) >> methodBuilder
        def clsBuilder = Mock(ClassMeta.Builder) {
            findMethodBuilder(_, _) >> methodBuilder
        }
        def budrCtx = Mock(IBuilderContext) {
            findClassBuilder(_) >> clsBuilder
        }

        when:
        def handler = new NotNullHandler()
        handler.handleAnnotatedElements(budrCtx, NotNull.class, [paramElem] as Set)

        then:
        noExceptionThrown()

        where:
        kind                        | paramName     | incorrectModifer
        ElementKind.PARAMETER       | 'param'       | Modifier.PRIVATE
        ElementKind.PARAMETER       | 'param'       | Modifier.FINAL
        ElementKind.PARAMETER       | 'param'       | Modifier.STATIC
    }
}
