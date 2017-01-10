/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.codegen;

import freemarker.template.Template;
import uapi.GeneralException;
import uapi.InvalidArgumentException;
import uapi.common.ArgumentChecker;
import uapi.common.StringHelper;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * CodeBlock metadata used to generate codes in specific method
 */
public class CodeBlockBuilder extends uapi.common.Builder<ICodeBlock> {

    private Object _model;
    private Template _temp;
    private List<String> _rawCodes = new ArrayList<>();

    // ----------------------------------------------------------
    // Public methods
    // ----------------------------------------------------------

    public CodeBlockBuilder model(
            final Object model
    ) throws GeneralException {
        checkStatus();
        ArgumentChecker.notNull(model, "model");
        this._model = model;
        return this;
    }

    public Object model() {
        return this._model;
    }

    public CodeBlockBuilder template(
            final Template template
    ) throws GeneralException {
        checkStatus();
        ArgumentChecker.notNull(template, "template");
        this._temp = template;
        return this;
    }

    public CodeBlockBuilder addRawCode(
            final String code
    ) throws GeneralException {
        checkStatus();
        ArgumentChecker.notEmpty(code, "code");
        this._rawCodes.add(code);
        return this;
    }

    // ----------------------------------------------------------
    // Methods override from Builder
    // ----------------------------------------------------------

    @Override
    protected void validate() throws InvalidArgumentException {
        if (this._temp != null) {
            ArgumentChecker.notNull(this._model, "model");
            ArgumentChecker.notNull(this._temp, "template");
        } else {
            ArgumentChecker.notZero(this._rawCodes, "rawCodes");
        }
    }

    @Override
    protected void initProperties() {
        // Do nothing
    }

    @Override
    protected ICodeBlock createInstance() {
        return new CodeBlock();
    }

    // ----------------------------------------------------------
    // Methods override from Object
    // ----------------------------------------------------------

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        CodeBlockBuilder other = (CodeBlockBuilder) obj;
        if (this._temp != null) {
            if (other._temp == null) {
                return false;
            }
            if (! this._temp.getSourceName().equals(other._temp.getSourceName())) {
                return false;
            }
            if (this._model == null || other._model == null) {
                return false;
            }
            return this._model.equals(other._model);
        } else {
            return this._rawCodes.equals(other._rawCodes);
        }
    }

    @Override
    public int hashCode() {
        int result = _model != null ? _model.hashCode() : 0;
        result = 31 * result + (_temp != null ? _temp.hashCode() : 0);
        result = 31 * result + (_rawCodes != null ? _rawCodes.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return StringHelper.makeString(
                "CodeBlock[model={}, template={}, rawCodes={}",
                this._model, this._temp, this._rawCodes);
    }

    /**
     * ICodeBlock implementation
     */
    private class CodeBlock implements ICodeBlock {

        @Override
        public String code() {
            if (CodeBlockBuilder.this._temp != null) {
                StringWriter writer = new StringWriter();
                try {
                    CodeBlockBuilder.this._temp.process(CodeBlockBuilder.this._model, writer);
                } catch (Exception ex) {
                    throw new GeneralException(ex);
                }
                return writer.toString();
            } else {
                StringBuilder sb = new StringBuilder();
                CodeBlockBuilder.this._rawCodes.forEach(sb::append);
                return sb.toString();
            }
        }
    }
}
