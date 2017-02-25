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
import uapi.common.Guarder;
import uapi.common.StringHelper;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The exception add ability to receive exception message from property file.
 *
 * Every exception extend from PropertiedException will provide two attributes: error code and category, the two
 * attributes will be mapped to property key which is defined in a property file and map to a exception message
 * template string.
 *
 * The category is a integer value, 0x0000 ~ 0xFFFF is reserved by framework.
 * The projects which are in the uapi.base repo will reserve category id from 0x0000 ~ 0x00FF
 * The projects which are in the uapi.cornerstone will reserve category id from 0x0100 ~ 0x01FF
 */
public class PropertiedException extends UapiException {

    private static final Lock CATEGORY_REG_LOCK                                             = new ReentrantLock();
    private static final Map<Integer, Class<? extends PropertiedException>> CATEGORY_REG    = new HashMap<>();

    /**
     * Check is the exception category registered by other exception or not.
     *
     * @param   category
     *          The exception category
     * @param   exceptionClass
     *          The exception class
     * @throws  GeneralException
     *          If the exception category is registered by other exception class
     */
    protected static void checkCategory(
            final int category,
            final Class<? extends PropertiedException> exceptionClass
    ) throws GeneralException {
        ArgumentChecker.required(exceptionClass, "exceptionClass");
        Guarder.by(CATEGORY_REG_LOCK).run(() -> {
            Class<? extends PropertiedException> regExClass = CATEGORY_REG.get(category);
            if (regExClass == null) {
                CATEGORY_REG.put(category, exceptionClass);
            } else if (! regExClass.equals(exceptionClass)) {
                if (! exceptionClass.isAssignableFrom(regExClass) && ! regExClass.isAssignableFrom(exceptionClass)) {
                    throw new GeneralException("The category [{}] is registered by exception - {}", category, regExClass);
                }
                // Using super exception class to register
                if (regExClass.isAssignableFrom(exceptionClass)) {
                    CATEGORY_REG.put(category, exceptionClass);
                }
            }
        });
    }

    private final ExceptionBuilder _builder;

    protected PropertiedException(final ExceptionBuilder builder) {
        super();
        checkCategory(builder._category, this.getClass());
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
        String propKey = this._builder._errors.getMappedKey(errorCode());
        if (Strings.isNullOrEmpty(propKey)) {
            return super.getMessage();
        }
        String propFile = this._builder._errors.getPropertiesFile(this);
        if (propFile == null) {
            throw new GeneralException("No properties file is mapped to category - {}", category());
        }
        String msgTemp = null;
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(PropertiedException.class.getResourceAsStream(propFile)))) {
            String line = reader.readLine();
            while (line != null) {
                if (line.indexOf(propKey) != 0) {
                    continue;
                }
                int idx = line.indexOf("=");
                if (idx <= 0) {
                    throw new GeneralException("Incorrect property line in commonErrors.properties - {}", line);
                }
                msgTemp = line.substring(idx).trim();
                break;
            }
        } catch (IOException ex) {
            throw new GeneralException(ex);
        }
        if (msgTemp == null) {
            return super.getMessage();
        } else {
            if (this._builder._varBuilder == null) {
                return msgTemp;
            } else {
                Object variables = this._builder._varBuilder.build();
                if (variables instanceof Map) {
                    return StringHelper.makeString(msgTemp, (Map) variables);
                } else if (variables instanceof Object[]) {
                    return StringHelper.makeString(msgTemp, (Object[]) variables);
                } else {
                    throw new GeneralException("Unsupported return type - {}", variables.getClass().getCanonicalName());
                }
            }
        }
    }

    public static abstract class ExceptionBuilder<E extends PropertiedException, B extends ExceptionBuilder>
            extends Builder<E> {

        private int _errCode = -1;
        private int _category = -1;
        private final ExceptionErrors _errors;
//        private Object[] _args;
        private ExceptionErrors.IVariableBuilder _varBuilder;

        public ExceptionBuilder(final int category, final ExceptionErrors errors) {
            if (category < 0) {
                throw new GeneralException("The exception category cant be negative");
            }
            if (errors == null) {
                throw new GeneralException("The ExceptionErrors is not specified");
            }
            this._category = category;
            this._errors = errors;
        }

        public B errorCode(int errorCode) {
            this._errCode = errorCode;
            return (B) this;
        }

        public B variableBuilder(ExceptionErrors.IVariableBuilder builder) {
            this._varBuilder = builder;
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
            // do nothing
        }

        @Override
        protected void afterCreateInstance() {
            // do nothing
        }
    }
}
