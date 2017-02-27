/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi;

import java.util.HashMap;
import java.util.Map;

/**
 * The class is used to define error code and its message string property key
 */
public abstract class ExceptionErrors<T extends PropertiedException> {

    private static final Map<Integer, String> codeKeyMapper = new HashMap<>();

    protected static void mapCodeKey(int code, String key) {
        codeKeyMapper.put(code, key);
    }

    /**
     * Receive the error code mapped property key
     *
     * @param   code
     *          The error code
     * @return  The property key which is mapped to the error key
     */
    protected String getMappedKey(int code) {
        return codeKeyMapper.get(code);
    }

    /**
     * Get error defined properties file based on category and other tags
     *
     * @param   exception
     *          The exception
     * @return  The properties file which is defined errors belongs the category
     */
    protected abstract String getPropertiesFile(T exception);

    protected interface IVariables<T> {

        T get();
    }

    protected static abstract class NamedVariables implements IVariables<Map> { }

    protected static class IndexedVariables implements IVariables<Object[]> {

        private Object[] _vars;

        public <T extends IndexedVariables> T set(Object... vars) {
            this._vars = vars;
            return (T) this;
        }

        @Override
        public Object[] get() {
            return this._vars;
        }
    }
}
