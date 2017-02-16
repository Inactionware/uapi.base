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
import uapi.GeneralException
import uapi.codegen.internal.TestAnno

import javax.lang.model.element.*
import javax.lang.model.type.DeclaredType
import java.lang.annotation.Annotation

/**
 * Test for AnnotationsHandler
 */
class AnnotationsHandlerTest extends Specification {

    def handler = new AnnotationsHandler() {

        int handleCount = 0;

        @Override
        protected Class<? extends Annotation>[] getOrderedAnnotations() {
            return [TestAnno.class ] as Class<? extends Annotation>[]
        }

        @Override
        protected void handleAnnotatedElements(
                IBuilderContext builderContext,
                Class<? extends Annotation> annotationType,
                Set<? extends Element> elements
        ) throws GeneralException {
            handleCount++
        }
    }

//    def 'Test check modifiers'() {
//        def mockElemt = Mock(Element) {
//            getModifiers() >> supports
//            getKind() >> ElementKind.CLASS
//            getEnclosingElement() >> Mock(Element) {
//                getSimpleName() >> Mock(Name) {
//                    toString() >> 'aaa'
//                }
//            }
//            getSimpleName() >> Mock(Name) {
//                toString() >> 'bbb'
//            }
//        }
//
//        when:
//        handler.checkModifiers(mockElemt, TestAnno, unsupport)
//
//        then:
//        thrown(GeneralException)
//
//        where:
//        supports                                    | unsupport
//        [Modifier.PUBLIC, Modifier.FINAL ] as Set   | Modifier.PUBLIC
//    }

//    def 'Test check annotations'() {
//        def mockElemt = Mock(Element) {
//            getAnnotation(_) >> null
//            getSimpleName() >> Mock(Name) {
//                toString() >> 'bbb'
//            }
//        }
//
//        when:
//        handler.checkAnnotations(mockElemt, TestAnno)
//
//        then:
//        thrown(GeneralException)
//    }

    def 'Test get type in annotation without field definition'() {
        given:
        def anno = Mock(AnnotationMirror) {
            getElementValues() >> new HashMap<>()
        }

        when:
        def type = handler.getTypeInAnnotation(anno, fieldName)

        then:
        type == null

        where:
        fieldName   | typeName
        'AAA'       | 'String'
    }

    def 'Test get type in annotation'() {
        given:
        def exeElem = Mock(ExecutableElement) {
            getSimpleName() >> Mock(Name) {
                toString() >> fieldName
            }
        }
        def annoValue = Mock(AnnotationValue) {
            getValue() >> Mock(DeclaredType) {
                asElement() >> Mock(TypeElement) {
                    getQualifiedName() >> Mock(Name) {
                        toString() >> typeName
                    }
                }
            }
        }
        def annoMap = new HashMap()
        annoMap.put(exeElem, annoValue)
        def anno = Mock(AnnotationMirror) {
            getElementValues() >> annoMap
        }

        when:
        def type = handler.getTypeInAnnotation(anno, fieldName)

        then:
        type == typeName

        where:
        fieldName   | typeName
        'AAA'       | 'String'
    }

    def 'Test get type in annotation with more type'() {
        given:
        def exeElem = Mock(ExecutableElement) {
            getSimpleName() >> Mock(Name) {
                toString() >> fieldName
            }
        }
        def annoValue = Mock(AnnotationValue) {
            getValue() >> Mock(DeclaredType) {
                asElement() >> Mock(TypeElement) {
                    getQualifiedName() >> Mock(Name) {
                        toString() >> typeName
                    }
                }
            }
        }
        def exeElem2 = Mock(ExecutableElement) {
            getSimpleName() >> Mock(Name) {
                toString() >> fieldName
            }
        }
        def annoValue2 = Mock(AnnotationValue) {
            getValue() >> Mock(DeclaredType) {
                asElement() >> Mock(TypeElement) {
                    getQualifiedName() >> Mock(Name) {
                        toString() >> typeName2
                    }
                }
            }
        }
        def annoMap = new HashMap()
        annoMap.put(exeElem, annoValue)
        annoMap.put(exeElem2, annoValue2)
        def anno = Mock(AnnotationMirror) {
            getElementValues() >> annoMap
            getAnnotationType() >> Mock(DeclaredType) {
                toString() >> 'Mock Annotation'
            }
        }

        when:
        handler.getTypeInAnnotation(anno, fieldName)

        then:
        thrown(GeneralException)

        where:
        fieldName   | typeName  | typeName2
        'AAA'       | 'String'  | 'Int'
    }

    def 'Test get types in annotation'() {
        given:
        def exeElem = Mock(ExecutableElement) {
            getSimpleName() >> Mock(Name) {
                toString() >> fieldName
            }
        }
        def annoValue1 = Mock(AnnotationValue) {
            getValue() >> Mock(DeclaredType) {
                asElement() >> Mock(TypeElement) {
                    getQualifiedName() >> Mock(Name) {
                        toString() >> typeName
                    }
                }
            }
        }
        def annoValue2 = Mock(AnnotationValue) {
            getValue() >> Mock(DeclaredType) {
                asElement() >> Mock(TypeElement) {
                    getQualifiedName() >> Mock(Name) {
                        toString() >> typeName2
                    }
                }
            }
        }
        def annoValue = Mock(AnnotationValue) {
            getValue() >> [annoValue1, annoValue2]
        }

        def annoMap = new HashMap()
        annoMap.put(exeElem, annoValue)
        def anno = Mock(AnnotationMirror) {
            getElementValues() >> annoMap
        }

        when:
        def types = handler.getTypesInAnnotation(anno, fieldName)

        then:
        types.size() == 2

        where:
        fieldName   | typeName  | typeName2
        'AAA'       | 'String'  | 'Int'
    }

    def 'Test handle'() {
        def budrCtx = Mock(IBuilderContext) {
            getElementsAnnotatedWith(_) >> Mock(Set)
        }

        when:
        handler.handle(budrCtx)

        then:
        handler.handleCount == 1
    }

    def 'Test get helper'() {
        expect:
        handler.getHelper() == null
    }

    def 'Test to string'() {
        expect:
        handler.toString() == 'AnnotationsHandler[supportedAnnotations=interface uapi.codegen.internal.TestAnno]'
    }
}
