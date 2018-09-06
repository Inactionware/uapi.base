/**
 * Copyright (C) 2010 The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi;

import uapi.common.ArgumentChecker;
import uapi.common.StringHelper;

import java.util.ArrayList;

/**
 * A utility class for type definition
 */
public final class Type {

    public static final String VOID             = "void";
    public static final String BOOLEAN          = "boolean";
    public static final String INTEGER          = "int";
    public static final String LONG             = "long";
    public static final String SHORT            = "short";
    public static final String FLOAT            = "float";
    public static final String DOUBLE           = "double";

    public static final String Q_VOID           = Void.class.getCanonicalName();
    public static final String Q_BOOLEAN        = Boolean.class.getCanonicalName();
    public static final String Q_INTEGER        = Integer.class.getCanonicalName();
    public static final String Q_LONG           = Long.class.getCanonicalName();
    public static final String Q_SHORT          = Short.class.getCanonicalName();
    public static final String Q_FLOAT          = Float.class.getCanonicalName();
    public static final String Q_DOUBLE         = Double.class.getCanonicalName();

    public static final String OBJECT           = "Object";
    public static final String STRING           = "String";
    public static final String STRING_ARRAY     = "String[]";
    public static final String STRING_LIST      = "java.util.List<java.lang.String>";

    public static final String Q_CLASS          = Class.class.getCanonicalName();
    public static final String Q_OBJECT         = Object.class.getCanonicalName();
    public static final String Q_STRING         = String.class.getCanonicalName();
    public static final String Q_STRING_ARRAY   = String[].class.getCanonicalName();
    public static final String Q_ARRAY_LIST     = ArrayList.class.getCanonicalName();

    public static final Class<Void>     T_VOID      = Void.class;
    public static final Class<Boolean>  T_BOOLEAN   = Boolean.class;
    public static final Class<Integer>  T_INTEGER   = Integer.class;
    public static final Class<Long>     T_LONG      = Long.class;
    public static final Class<Short>    T_SHORT     = Short.class;
    public static final Class<Float>    T_FLOAT     = Float.class;
    public static final Class<Double>   T_DOUBLE    = Double.class;

    public static final Class<String>   T_STRING    = String.class;

    private Type() { }

    public static String toQType(String nativeType) {
        ArgumentChecker.required(nativeType, "nativeType");
        switch (nativeType) {
            case VOID:
                return Q_VOID;
            case BOOLEAN:
                return Q_BOOLEAN;
            case INTEGER:
                return Q_INTEGER;
            case LONG:
                return Q_LONG;
            case SHORT:
                return Q_SHORT;
            case FLOAT:
                return Q_FLOAT;
            case DOUBLE:
                return Q_DOUBLE;
        }
        return nativeType;
    }

    public static String toNType(String qualifiedType) {
        ArgumentChecker.required(qualifiedType, "qualifiedType");
        if (qualifiedType.equals(Q_VOID)) {
            return VOID;
        } else if (qualifiedType.equals(Q_BOOLEAN)) {
            return BOOLEAN;
        } else if (qualifiedType.equals(Q_INTEGER)) {
            return INTEGER;
        } else if (qualifiedType.equals(Q_LONG)) {
            return LONG;
        } else if (qualifiedType.equals(Q_SHORT)) {
            return SHORT;
        } else if (qualifiedType.equals(Q_FLOAT)) {
            return FLOAT;
        } else if (qualifiedType.equals(Q_DOUBLE)) {
            return DOUBLE;
        } else {
            return qualifiedType;
        }
    }

    public static String toArrayType(final Class<?> itemType) {
        ArgumentChecker.required(itemType, "itemType");
        return itemType.getCanonicalName() + "[]";
    }

    public static String toListType(final Class<?> itemType) {
        ArgumentChecker.required(itemType, "itemType");
        return StringHelper.makeString("java.util.List<{}>", itemType.getCanonicalName());
    }

    public static String toMapType(final Class<?> itemType) {
        ArgumentChecker.required(itemType, "itemType");
        return StringHelper.makeString("java.util.Map<{}>", itemType.getCanonicalName());
    }
}
