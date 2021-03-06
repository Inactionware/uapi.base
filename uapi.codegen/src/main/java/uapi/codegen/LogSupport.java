/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.codegen;

import uapi.common.ArgumentChecker;
import uapi.common.StringHelper;
import uapi.rx.Looper;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/**
 * Support log in compile time
 */
public class LogSupport {

    private final Messager _msger;

    /**
     * Create LogSupport instance
     *
     * @param   processingEnvironment
     *          Context environment of compiling time
     */
    public LogSupport(
            final ProcessingEnvironment processingEnvironment
    ) {
        ArgumentChecker.notNull(processingEnvironment, "processingEnvironment");
        this._msger = processingEnvironment.getMessager();
    }

    /**
     * Output information to compiling console
     *
     * @param   msg
     *          The message which will be print to compiling console
     */
    public void info(
            final String msg
    ) {
        this._msger.printMessage(Diagnostic.Kind.NOTE, msg);
    }

    /**
     * Output information to compiling console
     *
     * @param   msg
     *          The message which will be print to compiling console
     * @param   args
     *          The arguments will be replaced placeholder in message
     */
    public void info(
            final String msg,
            final Object... args
    ) {
        this._msger.printMessage(Diagnostic.Kind.NOTE, StringHelper.makeString(msg, args));
    }

    /**
     * Output error information to compiling console, it will make compiling failed
     *
     * @param   msg
     *          The message which will be print to compiling console
     * @param   args
     *          The  arguments will be replaced placeholder in message
     */
    public void error(
            final String msg,
            final Object... args
    ) {
        this._msger.printMessage(Diagnostic.Kind.ERROR, StringHelper.makeString(msg, args));
    }

    /**
     * Output error information to compiling console, it will make compiling failed
     *
     * @param   msg
     *          The message which wil be print to compiling console
     * @param   element
     *          The annotation element object which is root cause of this error
     * @param   annotation
     *          The annotation self which is root cause of this error
     */
    public void error(
            final String msg,
            final Element element,
            final AnnotationMirror annotation
    ) {
        this._msger.printMessage(Diagnostic.Kind.ERROR, msg, element, annotation);
    }

    /**
     * Output error information which will be print to compiling console
     *
     * @param   t
     *          The exception object
     */
    public void error(
            final Throwable t
    ) {
        printException(t);
    }

    public void error(
            final Throwable t,
            final String msg,
            final String... args
    ) {
        this._msger.printMessage(Diagnostic.Kind.ERROR, StringHelper.makeString(msg, args));
        printException(t);
    }

    public void printException(final Throwable t) {
        if (t.getMessage() != null) {
            this._msger.printMessage(Diagnostic.Kind.ERROR, t.getMessage());
        } else {
            this._msger.printMessage(Diagnostic.Kind.ERROR, t.getClass().getCanonicalName());
        }
        var sts = t.getStackTrace();
        var buffer = new StringBuilder();
        Looper.on(sts)
                .map(st -> StringHelper.makeString("\t{}\n", st.toString()))
                .foreach(buffer::append);
        this._msger.printMessage(Diagnostic.Kind.ERROR, buffer.toString());
        if (t.getCause() != null) {
            printException(t.getCause());
        }
    }
}
