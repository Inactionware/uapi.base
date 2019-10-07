/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.codegen.internal;

import com.google.auto.service.AutoService;
import freemarker.template.Template;
import uapi.Type;
import uapi.codegen.*;
import uapi.common.StringHelper;
import uapi.rx.Looper;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URL;
import java.util.*;

@AutoService(Processor.class)
public class AnnotationProcessor extends AbstractProcessor {

    private static final String PATH_ANNOTATION_HANDLER =
            "META-INF/services/" + IAnnotationsHandler.class.getCanonicalName();
    private static final String TEMP_SOURCE_FILE = "template/generated_source.ftl";
    private static final String TEMP_MODULE_FILE = "template/generated_module.ftl";

    protected LogSupport _logger;
    private ProcessingEnvironment _procEnv;
    private List<IAnnotationsHandler> _handlers;
    private Set<String> _orderedAnnotations;

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        this._procEnv = processingEnv;
        this._logger = new LogSupport(processingEnv);
        this._handlers = new LinkedList<>();
        this._orderedAnnotations = new HashSet<>();
        loadExternalHandler();
    }

    private void loadExternalHandler() {
        InputStream is = null;
        Scanner scanner = null;

        try {
            final Enumeration<URL> systemResources =
                    this.getClass().getClassLoader().getResources(PATH_ANNOTATION_HANDLER);
            while (systemResources.hasMoreElements()) {
                is = systemResources.nextElement().openStream();
                scanner = new Scanner(is);
                while (scanner.hasNext()) {
                    String handlerClassName = scanner.nextLine();
                    this._logger.info("Initial external annotation handler - " + handlerClassName);
                    Class handlerClass = Class.forName(handlerClassName);
                    Object handler = handlerClass.getDeclaredConstructor().newInstance();
                    if (!(handler instanceof IAnnotationsHandler)) {
                        this._logger.error(
                                "The handler [{}] is not an instance of AnnotationsHandler",
                                handler.getClass().getName());
                        return;
                    }
                    initForHandler((IAnnotationsHandler) handler);
                }
            }
        } catch (Exception ex) {
            this._logger.error(ex);
            return;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    this._logger.error(ex);
                }
            }
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    private void initForHandler(IAnnotationsHandler handler) {
        this._handlers.add(handler);
        Looper.on(handler.getSupportedAnnotations())
                .map(Class::getName)
                .foreach(_orderedAnnotations::add);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return this._orderedAnnotations;
    }

    @Override
    public boolean process(
            final Set<? extends TypeElement> annotations,
            final RoundEnvironment roundEnv) {
        if (roundEnv.processingOver() || annotations.size() == 0) {
            return false;
        }
        this._logger.info("Start processing annotation for {} " + roundEnv.getRootElements());
        BuilderContext buildCtx = new BuilderContext(this._procEnv, roundEnv);
        // Init for builder context
        Looper.on(this._handlers)
                .map(IAnnotationsHandler::getHelper)
                .filter(Objects::nonNull)
                .foreach(buildCtx::putHelper);
        Looper.on(this._handlers)
                .next(handler -> _logger.info("Invoke annotation handler -> {}", handler))
                .foreach(handler -> handler.handle(buildCtx));

        // Generate source
        generateSource(buildCtx);
        buildCtx.clearBuilders();

        this._logger.info("End processing");
        return true;
    }

    int getHandlerCount() {
        return this._handlers.size();
    }

    private void generateModule(BuilderContext builderContext) {
        return;
    }

    private void generateSource(BuilderContext builderContext) {
        List<ClassMeta.Builder> classBuilders = builderContext.getBuilders();

        Template temp;
        try {
            temp = builderContext.loadTemplate(TEMP_SOURCE_FILE);
        } catch (Exception ex) {
            this._logger.error(ex);
            return;
        }

        for (ClassMeta.Builder classBuilder : classBuilders) {
            Writer srcWriter = null;
            try {
                // All generated class must be implemented IGenerated interface
                implementGenerated(classBuilder);
                this._logger.info("Generate source for -> {}.{}",
                        classBuilder.getPackageName(),
                        classBuilder.getGeneratedClassName());
                ClassMeta classMeta = classBuilder.build();
                JavaFileObject fileObj = builderContext.getFiler().createSourceFile(
                        StringHelper.makeString("{}.{}", classMeta.getPackageName(), classMeta.getGeneratedClassName())
                );
                srcWriter = fileObj.openWriter();
                temp.process(classMeta, srcWriter);
                this._logger.info("Done generate source for " + classMeta.getGeneratedClassName());
            } catch (Exception ex) {
                this._logger.error("An error was risen when generate source for - {}.{}",
                        classBuilder.getPackageName(),
                        classBuilder.getGeneratedClassName());
                this._logger.error(ex);
                return;
            } finally {
                if (srcWriter != null) {
                    try {
                        srcWriter.close();
                    } catch (Exception ex) {
                        this._logger.error(ex);
                    }
                }
            }
        }
    }

    private void implementGenerated(final ClassMeta.Builder classBuilder) {
        String codes;
        if (classBuilder.getParentClassName() != null) {
            codes = StringHelper.makeString(
                    "return {}.class;", classBuilder.getParentClassName());
        } else {
            codes = StringHelper.makeString(
                    "return {}.{}.class;",
                    classBuilder.getPackageName(),
                    classBuilder.getClassName() == null ? classBuilder.getGeneratedClassName() : classBuilder.getClassName());
        }

        classBuilder.addImplement(IGenerated.class.getCanonicalName())
                .addMethodBuilder(MethodMeta.builder()
                        .addAnnotationBuilder(AnnotationMeta.builder().setName(AnnotationMeta.OVERRIDE))
                        .addModifier(Modifier.PUBLIC)
                        .setName("originalType")
                        .setReturnTypeName(Type.Q_CLASS)
                        .addCodeBuilder(CodeMeta.builder()
                                .addRawCode(codes)));
    }
}
