/*
 * Copyright (C) 2017. The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.codegen;

import uapi.GeneralException;
import uapi.InvalidArgumentException;
import uapi.common.ArgumentChecker;
import uapi.common.Functionals;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xquan on 2/3/2017.
 */
public abstract class CodegenBuilder<T> {

    private boolean _built = false;
    private Map<String, Object> _transience = new HashMap<>();

    /**
     * Put a transience object into the builder.
     * A transience object will be removed after the instance is created which means
     * the transience object is only used by instance creation
     *
     * @param   name
     *          The transience object name
     * @param   object
     *          The transience object
     */
    public void putTransience(final String name, final Object object) {
        ArgumentChecker.notEmpty(name, "name");
        this._transience.put(name, object);
    }

    /**
     * Receive previously saved transience object by its name
     *
     * @param   name
     *          The name of transience object
     * @param   <E>
     *          The type of transience object
     * @return  The transience object or null
     */
    @SuppressWarnings("unchecked")
    public <E> E getTransience(final String name) {
        ArgumentChecker.notEmpty(name, "name");
        return (E) this._transience.get(name);
    }

    /**
     * Receive previously saved transience object by its name
     * The default value will be returned if no such transience object
     *
     * @param   name
     *          The name of transience object
     * @param   defaultValue
     *          Default value
     * @param   <E>
     *          The type of transience object
     * @return  The transience object or null
     */
    public <E> E getTransience(
            final String name,
            final E defaultValue) {
        E value = getTransience(name);
        return value == null ? defaultValue : value;
    }

    /**
     * Receive previously saved transience object by its name
     * The creator will be invoked if the related transience object is absent
     *
     * @param   name
     *          The name of transience object
     * @param   <E>
     *          The type of transience object
     * @return  The transience object or null
     */
    @SuppressWarnings("unchecked")
    public <E> E createTransienceIfAbsent(
            final String name,
            final Functionals.Creator<E> creator) {
        ArgumentChecker.required(name, "name");
        E e = (E) this._transience.get(name);
        if (e != null) {
            return e;
        }
        ArgumentChecker.required(creator, "creator");
        e = creator.accept();
        this._transience.put(name, e);
        return e;
    }

    /**
     * Build instance by currently properties setting
     *
     * @return  The instance
     * @throws GeneralException
     *          Validation failed
     */
    public T build() throws GeneralException {
        checkStatus();
        validate();
        initProperties();
        this._transience.clear();
        T obj = createInstance();
        this._built = true;
        return obj;
    }



    protected void checkStatus() {
        if (this._built) {
            throw new GeneralException("The builder[{}] is already used", this.getClass().getName());
        }
    }

    protected abstract void validate() throws InvalidArgumentException;

    protected abstract void initProperties();

    protected abstract T createInstance();
}
