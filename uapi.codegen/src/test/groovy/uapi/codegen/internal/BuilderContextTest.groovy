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
import uapi.GeneralException
import uapi.codegen.IHandlerHelper

import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.*
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.FileObject
import javax.tools.StandardLocation
import java.lang.annotation.Annotation

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

    @Ignore
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

    def 'Test check modifiers'() {
        given:
        def mockElemt = Mock(Element) {
            getModifiers() >> ([Modifier.PUBLIC, Modifier.FINAL] as Set)
        }
        def procEnv = Mock(ProcessingEnvironment)
        def roundEnv = Mock(RoundEnvironment)
        def budrCtx = new BuilderContext(procEnv, roundEnv)

        when:
        budrCtx.checkModifiers(mockElemt, TestAnno.class, Modifier.PRIVATE)

        then:
        noExceptionThrown()
    }

    def 'Test check modifiers which unexpected'() {
        given:
        def mockElemt = Mock(Element) {
            getKind() >> ElementKind.CLASS
            getEnclosingElement() >> Mock(Element) {
                getSimpleName() >> Mock(Name) {
                    toString() >> 'Test'
                }
            }
            getSimpleName() >> Mock(Name) {
                toString() >> clsName
            }
            getModifiers() >> ([Modifier.PUBLIC, Modifier.FINAL] as Set)
        }
        def procEnv = Mock(ProcessingEnvironment)
        def roundEnv = Mock(RoundEnvironment)
        def budrCtx = new BuilderContext(procEnv, roundEnv)

        when:
        budrCtx.checkModifiers(mockElemt, TestAnno.class, Modifier.FINAL)

        then:
        thrown(GeneralException)

        where:
        clsName | pkgName
        'AAA'   | 'com.test'
    }

    def 'Test check annotation'() {
        given:
        def elem = Mock(Element) {
            getAnnotation(TestAnno.class) >> Mock(Annotation)
        }
        def procEnv = Mock(ProcessingEnvironment)
        def roundEnv = Mock(RoundEnvironment)
        def budrCtx = new BuilderContext(procEnv, roundEnv)

        when:
        budrCtx.checkAnnotations(elem, TestAnno.class)

        then:
        noExceptionThrown()
    }

    def 'Test check annotation which unexpected'() {
        given:
        def elem = Mock(Element) {
            getKind() >> ElementKind.CLASS
            getSimpleName() >> Mock(Name) {
                toString() >> 'AA'
            }
        }
        def procEnv = Mock(ProcessingEnvironment)
        def roundEnv = Mock(RoundEnvironment)
        def budrCtx = new BuilderContext(procEnv, roundEnv)

        when:
        budrCtx.checkAnnotations(elem, TestAnno.class)

        then:
        thrown(GeneralException)
    }

    def 'Test find field with'() {
        given:
        def fieldElem = Mock(Element) {
            getKind() >> ElementKind.FIELD
            asType() >> Mock(TypeMirror) {
                toString() >> typeName
            }
            getAnnotation(TestAnno.class) >> Mock(Annotation)
        }
        def classElem = Mock(Element) {
            getKind() >> ElementKind.CLASS
            getEnclosedElements() >> [fieldElem]
        }
        def procEnv = Mock(ProcessingEnvironment)
        def roundEnv = Mock(RoundEnvironment)
        def budrCtx = new BuilderContext(procEnv, roundEnv)

        when:
        def found = budrCtx.findFieldWith(classElem, type, TestAnno.class)

        then:
        noExceptionThrown()
        found == fieldElem

        where:
        typeName                    | type
        String.class.canonicalName  | String.class
    }

    def 'Test find field with nothing'() {
        given:
        def classElem = Mock(Element) {
            getKind() >> ElementKind.CLASS
            getEnclosedElements() >> []
        }
        def procEnv = Mock(ProcessingEnvironment)
        def roundEnv = Mock(RoundEnvironment)
        def budrCtx = new BuilderContext(procEnv, roundEnv)

        when:
        def found = budrCtx.findFieldWith(classElem, type, TestAnno.class)

        then:
        noExceptionThrown()
        found == null

        where:
        typeName                    | type
        String.class.canonicalName  | String.class
    }

    def 'Test load template'() {
        given:
        def filer = Mock(Filer) {
            getResource(StandardLocation.CLASS_PATH, _ as String, tempPath) >> Mock(FileObject) {
                openReader(_) >> new FileReader('src/main/resources/' + tempPath)
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
        'template/generated_source.ftl'    | null
    }

    def 'Test clear builders'() {
        given:
        def mockElemt = Mock(Element) {
            getKind() >> ElementKind.CLASS
            getSimpleName() >> Mock(Name) {
                toString() >> 'Class'
            }
        }
        def procEnv = Mock(ProcessingEnvironment) {
            getElementUtils() >> Mock(Elements) {
                getPackageOf(mockElemt) >> Mock(PackageElement) {
                    getQualifiedName() >> Mock(Name) {
                        toString() >> 'PKG'
                    }
                }
            }
        }
        def roundEnv = Mock(RoundEnvironment)
        def budrCtx = new BuilderContext(procEnv, roundEnv)

        expect:
        budrCtx.findClassBuilder(mockElemt) != null
        budrCtx.builders.size() == 1
        budrCtx.clearBuilders()
        budrCtx.builders.size() == 0
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

    def 'Test is assignable by element'() {
        given:
        def dtype1 = Mock(DeclaredType)
        def dtype2 = Mock(DeclaredType)
        def type1Elem = Mock(TypeElement) {
            asType() >> dtype1
        }
        def type2Elem = Mock(TypeElement)
        def procEnv = Mock(ProcessingEnvironment) {
            getElementUtils() >> Mock(Elements) {
                getTypeElement(typeName) >> type2Elem
            }
            getTypeUtils() >> Mock(Types) {
                getDeclaredType(type2Elem) >> dtype2
                isAssignable(dtype1, dtype2) >> true
            }
        }
        def roundEnv = Mock(RoundEnvironment)
        def budrCtx = new BuilderContext(procEnv, roundEnv)

        expect:
        budrCtx.isAssignable(type1Elem, typeClass)

        where:
        typeClass       | typeName
        String.class    | String.canonicalName
    }
}
