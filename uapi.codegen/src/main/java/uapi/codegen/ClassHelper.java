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
import uapi.common.StringHelper;
import uapi.common.WordHelper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public final class ClassHelper {

    private static final String FIELD_PREFIX    = "_";
    private static final String SETTER_PREFIX   = "set";
    private static final String GETTER_PREFIX   = "get";
    private static final String ADD_PREFIX      = "add";
    private static final String PUT_PREFIX      = "put";

    public static String makeSetterName(
            final String fieldName,
            final boolean isCollection,
            final boolean isMap
    ) {
        if (StringHelper.isNullOrEmpty(fieldName)) {
            throw new InvalidArgumentException("The field name can't be empty or null");
        }
        String propName;
        if (fieldName.startsWith(FIELD_PREFIX)) {
            propName = fieldName.substring(1);
        } else {
            propName = fieldName;
        }
        String setterName;
        if (isCollection) {
            propName = WordHelper.singularize(propName);
            setterName = ADD_PREFIX + propName.substring(0, 1).toUpperCase() + propName.substring(1, propName.length());
        } else if (isMap) {
            propName = WordHelper.singularize(propName);
            setterName = PUT_PREFIX + propName.substring(0, 1).toUpperCase() + propName.substring(1, propName.length());
        } else {
            setterName = SETTER_PREFIX + propName.substring(0, 1).toUpperCase() + propName.substring(1, propName.length());
        }
        return setterName;
    }

    public static String makeGetterName(
            final String fieldName
    ) {
        ArgumentChecker.required(fieldName, "fieldName");
        String propName;
        if (fieldName.startsWith(FIELD_PREFIX)) {
            propName = fieldName.substring(1);
        } else {
            propName = fieldName;
        }
        return GETTER_PREFIX + propName.substring(0, 1).toUpperCase() + propName.substring(1, propName.length());
    }

    public static Class<?>[] getInterfaceParameterizedClasses(
            final Class<?> type,
            final Class<?> interfaceType
    ) {
        if (type == null) {
            throw new InvalidArgumentException("type", InvalidArgumentException.InvalidArgumentType.EMPTY);
        }
        Class<?>[] paramClasses = null;
        var intfTypes = type.getGenericInterfaces();
        for (Type intfType : intfTypes) {
            if (! (intfType instanceof ParameterizedType)) {
                continue;
            }
            var rowType = ((ParameterizedType) intfType).getRawType();
            if (! rowType.equals(interfaceType)) {
                continue;
            }
            var paramTypes = ((ParameterizedType) intfType).getActualTypeArguments();
            paramClasses = new Class<?>[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++) {
                paramClasses[i] = (Class<?>) paramTypes[i];
            }
        }
        return paramClasses;
    }

    public static String getPackageName(final String qualifiedClassName) {
        ArgumentChecker.required(qualifiedClassName, "qualifiedClassName");
        if (! qualifiedClassName.contains(".")) {
            throw new GeneralException("Not valid qualified class name: {}", qualifiedClassName);
        }
        return qualifiedClassName.substring(0, qualifiedClassName.lastIndexOf("."));
    }

    public static String getClassName(final String qualifiedClassName) {
        ArgumentChecker.required(qualifiedClassName, "qualifiedClassName");
        if (! qualifiedClassName.contains(".")) {
            throw new GeneralException("Not valid qualified class name: {}", qualifiedClassName);
        }
        return qualifiedClassName.substring(qualifiedClassName.lastIndexOf(".") + 1, qualifiedClassName.length());
    }
}
