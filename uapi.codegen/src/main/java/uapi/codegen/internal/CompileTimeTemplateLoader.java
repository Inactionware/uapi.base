/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.codegen.internal;

import freemarker.cache.TemplateLoader;
import uapi.GeneralException;
import uapi.common.ArgumentChecker;

import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Reader;

/**
 * The template loader for compiling source time
 */
public class CompileTimeTemplateLoader implements TemplateLoader {

    private final BuilderContext _builderCtx;

    public CompileTimeTemplateLoader(final BuilderContext builderContext) {
        ArgumentChecker.notNull(builderContext, "builderContext");
        this._builderCtx = builderContext;
    }

    /**
     * Load Freemarker template resource from specific name
     *
     * The name should be constructed by below pattern:
     *  module:name
     *
     * @param   name
     *          The template resource name with its module name
     * @return  The Freemarker template object
     * @throws  IOException
     *          If the template file does not exist
     */
    @Override
    public Object findTemplateSource(
            final String name
    ) throws IOException {
        ArgumentChecker.required(name, "name");
        var moduleFile = name.split(":");
        if (moduleFile.length != 2) {
            throw new GeneralException("The template file name should be constructed by [module name]:[file path]");
        }
        FileObject fileObj = this._builderCtx.getFiler().getResource(
                StandardLocation.MODULE_PATH, moduleFile[0] + "/", moduleFile[1]);
        return new TemplateSource(fileObj);
    }

    @Override
    public long getLastModified(
            final Object templateSource
    ) {
        if (! (templateSource instanceof TemplateSource)) {
            throw new GeneralException(
                    "The input must be a TemplateSource - {}",
                    templateSource.getClass().getName());
        }
        TemplateSource tempSrc = (TemplateSource) templateSource;
        return tempSrc.getLastModified();
    }

    @Override
    public Reader getReader(
            final Object templateSource,
            final String encoding
    ) throws IOException {
        if (! (templateSource instanceof TemplateSource)) {
            throw new GeneralException(
                    "The input must be a TemplateSource - {}",
                    templateSource.getClass().getName());
        }
        TemplateSource tempSrc = (TemplateSource) templateSource;
        return tempSrc.openReader();
    }

    @Override
    public void closeTemplateSource(
            final Object templateSource
    ) throws IOException {
        if (! (templateSource instanceof TemplateSource)) {
            throw new GeneralException(
                    "The input must be a TemplateSource - {}",
                    templateSource.getClass().getName());
        }
        TemplateSource tempSrc = (TemplateSource) templateSource;
        tempSrc.close();
    }
}
