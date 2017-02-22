/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi;

import com.google.common.base.Strings;
import uapi.common.ArgumentChecker;
import uapi.common.Builder;
import uapi.common.StringHelper;

import java.io.*;

/**
 * The exception add ability to receive exception message from property file.
 *
 * Every exception extend from PropertiedException will provide two attributes: error code and category, the two
 * attributes will be mapped to property key which is defined in a property file and map to a exception message
 * template string.
 *
 * The category is a integer value, 0 ~ 8192 is reserved by framework.
 */
public class PropertiedException extends UapiException {

    private static final String PROPERTY_FILE   = "exception.properties";

    private final ExceptionBuilder _builder;

    protected PropertiedException(final ExceptionBuilder builder) {
        super();
        this._builder = builder;
    }

    public int errorCode() {
        return this._builder._errCode;
    }

    public int category() {
        return this._builder._category;
    }

    @Override
    public String getMessage() {
        String propKey = getPropertyKey();
        if (Strings.isNullOrEmpty(propKey)) {
            return super.getMessage();
        }
        String msgTemp = null;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(PropertiedException.class.getResourceAsStream(PROPERTY_FILE)))) {
            String line = reader.readLine();
            while (line != null) {
                if (line.indexOf(propKey) != 0) {
                    continue;
                }
                int idx = line.indexOf("=");
                if (idx <= 0) {
                    throw new GeneralException("Incorrect property line in exception.properties - {}", line);
                }
                msgTemp = line.substring(idx).trim();
                break;
            }
        } catch (IOException ex) {
            throw new GeneralException(ex);
        }
        if (msgTemp == null) {
            return super.getMessage();
        }
        return StringHelper.makeString(msgTemp, this._builder._args);
    }

    protected String getPropertyKey() {
        return this._builder._errCode + "." + this._builder._category;
    }

    public static abstract class ExceptionBuilder<E extends PropertiedException, B extends ExceptionBuilder>
            extends Builder<E> {

        private int _errCode = -1;
        private int _category = -1;
        private Object[] _args;

        public ExceptionBuilder(final int category) {
            if (category < 0) {
                throw new GeneralException("The exception category cant be negative");
            }
            this._category = category;
        }

        public B errorCode(int errorCode) {
            this._errCode = errorCode;
            return (B) this;
        }

        public B arguments(Object... arguments) {
            ArgumentChecker.required(arguments, "arguments");
            this._args = arguments;
            return (B) this;
        }

        @Override
        protected void validate() throws InvalidArgumentException {
            if (this._errCode == -1) {
                throw new InvalidArgumentException("The error code must be provider");
            }
            if (this._category == -1) {
                throw new InvalidArgumentException("The category must be provider");
            }
        }

        @Override
        protected void beforeCreateInstance() {
            if (this._args == null) {
                this._args = new Object[0];
            }
        }

        @Override
        protected void afterCreateInstance() {
            // do nothing
        }
    }
}
