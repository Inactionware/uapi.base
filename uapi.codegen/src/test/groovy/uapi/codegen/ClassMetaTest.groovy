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

import javax.lang.model.element.*
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements

/**
 * Test case for ClassMeta
 */
class ClassMetaTest extends Specification {

    def 'Test build'() {
        def mockAnnoBudr = new MockAnnotationBuilder()
        def mockFieldBudr = new MockFieldBuilder()
        def mockMethodBudr = new MockMethodBuilder()

        when:
        ClassMeta clsMeta = ClassMeta.builder()
                .setPackageName(pkgName)
                .setClassName(clsName)
                .setGeneratedClassName(genClsName)
                .addAnnotationBuilder(mockAnnoBudr)
                .addFieldBuilder(mockFieldBudr)
                .addImplement(impl)
                .addImport(impot)
                .addMethodBuilder(mockMethodBudr)
                .build()

        then:
        clsMeta.getPackageName() == pkgName
        clsMeta.getClassName() == clsName
        clsMeta.getGeneratedClassName() == genClsName
        clsMeta.getQualifiedClassName() == qClsName
        clsMeta.getAnnotations().size() == 1
        clsMeta.getFields().size() == 1
        clsMeta.getImplements().size() == 1
        clsMeta.getImports().size() == 1
        clsMeta.getMethods().size() == 1

        where:
        pkgName     | clsName   | genClsName    | impl      | impot     | qClsName
        'pkgName'   | 'clsName' | 'clsName_gen' | 'Test'    | 'abc'     | 'pkgName.clsName_gen'
    }

    def 'Test build by class'() {
        def mockAnnoBudr = new MockAnnotationBuilder()
        def mockFieldBudr = new MockFieldBuilder()
        def mockMethodBudr = new MockMethodBuilder()

        when:
        ClassMeta clsMeta = ClassMeta.builder()
                .setPackageName(pkgName)
                .setClassName(clsName)
                .setGeneratedClassName(genClsName)
                .addAnnotationBuilder(mockAnnoBudr)
                .addFieldBuilder(mockFieldBudr)
                .addImplement(impl)
                .addImport(impot)
                .addMethodBuilder(mockMethodBudr)
                .build()

        then:
        clsMeta.getPackageName() == pkgName
        clsMeta.getClassName() == clsName
        clsMeta.getGeneratedClassName() == genClsName
        clsMeta.getQualifiedClassName() == qClsName
        clsMeta.getAnnotations().size() == 1
        clsMeta.getFields().size() == 1
        clsMeta.getImplements().size() == 1
        clsMeta.getImports().size() == 1
        clsMeta.getMethods().size() == 1

        where:
        pkgName     | clsName   | genClsName    | impl              | impot     | qClsName
        'pkgName'   | 'clsName' | 'clsName_gen' | Cloneable.class   | 'abc'     | 'pkgName.clsName_gen'
    }

    def 'Test build from Element'() {
        def mockElemt = Mock(Element) {
            getKind() >> ElementKind.CLASS
            getSimpleName() >> Mock(Name) {
                toString() >> clsName
            }
        }
        def mockBudrCtx = Mock(IBuilderContext) {
            getElementUtils() >> Mock(Elements) {
                getPackageOf(_) >> Mock(PackageElement) {
                    getQualifiedName() >> Mock(Name) {
                        toString() >> pkgName
                    }
                }
            }
        }

        when:
        ClassMeta.Builder clsBudr = ClassMeta.builder(mockElemt, mockBudrCtx)

        then:
        clsBudr.getPackageName() == pkgName
        clsBudr.getClassName() == clsName
        clsBudr.getGeneratedClassName() == genClsName

        where:
        pkgName     | clsName   | genClsName
        'pkgName'   | 'clsName' | 'clsName_Generated'
    }

    def 'Test build from innner Element'() {
        def mockElemt = Mock(Element) {
            getKind() >> ElementKind.CLASS
            getSimpleName() >> Mock(Name) {
                toString() >> clsName
            }
            getEnclosingElement() >> Mock(Element) {
                getKind() >> ElementKind.CLASS
                getSimpleName() >> Mock(Name) {
                    toString() >> outerClsName
                }
            }
        }
        def mockBudrCtx = Mock(IBuilderContext) {
            getElementUtils() >> Mock(Elements) {
                getPackageOf(_) >> Mock(PackageElement) {
                    getQualifiedName() >> Mock(Name) {
                        toString() >> pkgName
                    }
                }
            }
        }

        when:
        ClassMeta.Builder clsBudr = ClassMeta.builder(mockElemt, mockBudrCtx)

        then:
        clsBudr.getPackageName() == pkgName
        clsBudr.getClassName() == finClsName
        clsBudr.getGeneratedClassName() == genClsName

        where:
        pkgName     | clsName   | genClsName            | outerClsName      | finClsName
        'pkgName'   | 'clsName' | 'clsName_Generated'   | 'outerClsName'    | 'outerClsName.clsName'
    }

    def mockAnnoMeta = Mock(AnnotationMeta)
    def mockFieldMeta = Mock(FieldMeta)
    def mockMethodMeta = Mock(MethodMeta)

    def 'Test find field builder'() {
        def mockFieldBudr = new MockFieldBuilder()

        when:
        ClassMeta.Builder clsBudr = ClassMeta.builder()
                .addFieldBuilder(mockFieldBudr)

        then:
        clsBudr.findFieldBuilder(mockFieldBudr) != null
    }

    def 'Test find field builder by name'() {
        def mockFieldBudr = new MockFieldBuilder()
        mockFieldBudr.setName(name)
        mockFieldBudr.setTypeName(type)

        when:
        ClassMeta.Builder clsBudr = ClassMeta.builder()
                .addFieldBuilder(mockFieldBudr)

        then:
        clsBudr.findFieldBuilder(name, type) != null

        where:
        name    | type
        'Name'  | 'String'
    }

    def 'Test add field builder if absent'() {
        given:
        def mockFieldBudr = new MockFieldBuilder()
        mockFieldBudr.setName(name)
        mockFieldBudr.setTypeName(type)

        expect:
        ClassMeta.Builder clsBudr = ClassMeta.builder()
                .addFieldBuilderIfAbsent(mockFieldBudr)
        clsBudr.findFieldBuilder(name, type) != null
        clsBudr.addFieldBuilderIfAbsent(mockFieldBudr)
        clsBudr.findFieldBuilder(name, type) != null

        where:
        name    | type
        'Name'  | 'String'
    }

    def 'Test find method builder by element'() {
        def mockElemt = Mock(ExecutableElement) {
            getKind() >> ElementKind.METHOD
            getSimpleName() >> Mock(Name) {
                toString() >> methodName
            }
            getReturnType() >> Mock(TypeMirror) {
                toString() >> rtnType
            }
            getModifiers() >> [Modifier.PUBLIC]
            getThrownTypes() >> [ Mock(TypeMirror) {
                toString() >> thrownType
            } ]
            getParameters() >> []
        }
        def mockBudrCtx = Mock(IBuilderContext)

        def methodBudr = new MockMethodBuilder()

        when:
        ClassMeta.Builder clsBudr = ClassMeta.builder()
                .addMethodBuilder(methodBudr)

        then:
        clsBudr.findMethodBuilder(mockElemt, mockBudrCtx)  != null

        where:
        methodName  | rtnType   | thrownType
        'name'      | 'rtnType' | 'thrownType'
    }

    def 'Test find method builders by name'() {
        given:
        def methodBudr = new MockMethodBuilder()
        methodBudr.setName(methodName)
        methodBudr.setReturnTypeName(rtnType)
        def methodBudr2 = new MockMethodBuilder()
        methodBudr2.setName(methodName)
        methodBudr2.setReturnTypeName(rtnType2)

        when:
        ClassMeta.Builder clsBudr = ClassMeta.builder()
                .addMethodBuilder(methodBudr)
        clsBudr.addMethodBuilder(methodBudr2)

        then:
        def methods = clsBudr.findMethodBuilder(methodName)
        methods  != null
        methods.size() == 2

        where:
        methodName  | rtnType   | rtnType2      | thrownType
        'name'      | 'rtnType' | 'rtnType2'    | 'thrownType'
    }

    def 'Test find method build by name'() {
        given:
        def methodBudr = new MockMethodBuilder()
        methodBudr.setName(methodName)

        when:
        ClassMeta.Builder clsBudr = ClassMeta.builder()
                .addMethodBuilder(methodBudr)

        then:
        def methods = clsBudr.findMethodBuilder(methodName)
        methods  != null
        methods.size() == 1

        where:
        methodName  | rtnType   | thrownType
        'name'      | 'rtnType' | 'thrownType'
    }

    def 'Test find setter builders'() {
        given:
        def setterBuilder = new MockMethodBuilder()
        setterBuilder.setIsSetter(true)

        when:
        ClassMeta.Builder clsBudr = ClassMeta.builder()
                .addMethodBuilder(setterBuilder)

        then:
        def setters = clsBudr.findSetterBuilders()
        setters != null
        setters.size() == 1
        setters.get(0) == setterBuilder
    }

    def 'Test add method build if absent'() {
        given:
        def methodBudr = new MockMethodBuilder()
        methodBudr.setName('abc')
        def methodBudr2 = new MockMethodBuilder()
        methodBudr2.setName('abc')
        ClassMeta.Builder clsBudr = ClassMeta.builder()

        expect:
        clsBudr.addMethodBuilderIfAbsent(methodBudr) == methodBudr
        clsBudr.findMethodBuilder('abc') != null
        clsBudr.addMethodBuilderIfAbsent(methodBudr2) == methodBudr
    }

    def 'Test add property builder'() {
        when:
        def propBudr = new PropertyMeta.Builder()
        ClassMeta.Builder clsBudr = ClassMeta.builder()
        clsBudr.addPropertyBuilder(propBudr)

        then:
        noExceptionThrown()
    }

    def 'Test override method builder'() {
        given:
        def methodBudr = new MockMethodBuilder()
        methodBudr.setName('abc')
        def methodBudr2 = new MockMethodBuilder()
        methodBudr2.setName('abc')
        methodBudr2.addCodeBuilder(CodeMeta.builder())
        ClassMeta.Builder clsBudr = ClassMeta.builder();

        when:
        clsBudr.addMethodBuilder(methodBudr)
        clsBudr.overrideMethodBuilder(methodBudr2)

        then:
        clsBudr.findMethodBuilder('abc').get(0) == methodBudr2
    }

    def 'Test toString'() {
        given:
        ClassMeta.Builder clsBudr = ClassMeta.builder()
                .setPackageName('pkgname')
                .setClassName('clsname')
                .setGeneratedClassName('genclassname');

        expect:
        clsBudr.toString() == string

        where:
        pkgName     | className | generatedClassName    | string
        'pkgname'   | 'clsname' | 'genclassname'        | 'ClassMeta[packageName=pkgname, className=clsname, generatedClassName=genclassname, implements=[], annotations=[], fields=[], fieldBuilders=[], methods=[], properties=[]]'
    }

    def 'Test equals'() {
        def mockAnnoBudr = new MockAnnotationBuilder()
        def mockFieldBudr = new MockFieldBuilder()
        def mockMethodBudr = new MockMethodBuilder()

        when:
        ClassMeta.Builder clsBudr1 = ClassMeta.builder()
                .setPackageName(pkgName)
                .setClassName(clsName)
                .setGeneratedClassName(genClsName)
                .addAnnotationBuilder(mockAnnoBudr)
                .addFieldBuilder(mockFieldBudr)
                .addImplement(impl)
                .addImport(impot)
                .addMethodBuilder(mockMethodBudr)
        ClassMeta.Builder clsBudr2 = ClassMeta.builder()
                .setPackageName(pkgName)
                .setClassName(clsName)
                .setGeneratedClassName(genClsName)
                .addAnnotationBuilder(mockAnnoBudr)
                .addFieldBuilder(mockFieldBudr)
                .addImplement(impl)
                .addImport(impot)
                .addMethodBuilder(mockMethodBudr)
        ClassMeta.Builder clsBudr3 = ClassMeta.builder()
                .setPackageName(pkgName)
                .setClassName('sss')
                .setGeneratedClassName(genClsName)
                .addAnnotationBuilder(mockAnnoBudr)
                .addFieldBuilder(mockFieldBudr)
                .addImport(impot)
                .addMethodBuilder(mockMethodBudr)

        then:
        clsBudr1 == clsBudr2
        clsBudr1 != clsBudr3
        clsBudr2 != clsBudr3

        where:
        pkgName     | clsName   | genClsName    | impl      | impot
        'pkgName'   | 'clsName' | 'clsName_gen' | 'Test'    | 'abc'
    }

    def 'Test findFieldBuilder'() {
        given:
        def mockAnnoBudr = new MockAnnotationBuilder()
        def mockFieldBudr = new MockFieldBuilder()
        def mockMethodBudr = new MockMethodBuilder()
        ClassMeta.Builder clsBudr = ClassMeta.builder()
                .setPackageName(pkgName)
                .setClassName(clsName)
                .setGeneratedClassName(genClsName)
                .addAnnotationBuilder(mockAnnoBudr)
                .addFieldBuilder(mockFieldBudr)
                .addImplement(impl)
                .addImport(impot)
                .addMethodBuilder(mockMethodBudr)

        when:
        def fieldBudr = clsBudr.findFieldBuilder(mockFieldBudr)

        then:
        fieldBudr != null

        where:
        pkgName     | clsName   | genClsName    | impl      | impot
        'pkgName'   | 'clsName' | 'clsName_gen' | 'Test'    | 'abc'
    }

    class MockAnnotationBuilder extends AnnotationMeta.Builder {
        private int _initInvokedCount = 0
        private int _validationCount = 0;
        private int _createInstCount = 0

        public void initProperties() {
            this._initInvokedCount++
        }

        public void validate() {
            this._validationCount++
        }

        public AnnotationMeta createInstance() {
            this._createInstCount++
            return mockAnnoMeta
        }
    }

    class MockFieldBuilder extends FieldMeta.Builder {
        private int _initInvokedCount = 0
        private int _validationCount = 0;
        private int _createInstCount = 0

        public void initProperties() {
            this._initInvokedCount++
        }

        public void validate() {
            this._validationCount++
        }

        public FieldMeta createInstance() {
            this._createInstCount++
            return mockFieldMeta
        }

        public boolean equals(Object obj) {
            return true
        }
    }

    class MockMethodBuilder extends MethodMeta.Builder {
        private int _initInvokedCount = 0
        private int _validationCount = 0;
        private int _createInstCount = 0

        public void initProperties() {
            this._initInvokedCount++
        }

        public void validate() {
            this._validationCount++
        }

        public MethodMeta createInstance() {
            this._createInstCount++
            return mockMethodMeta
        }
    }
}
