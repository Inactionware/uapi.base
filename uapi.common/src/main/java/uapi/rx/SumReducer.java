/*
 * Copyright (C) 2010 The UAPI Authors
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the LICENSE file.
 *
 * You must gained the permission from the authors if you want to
 * use the project into a commercial product
 */

package uapi.rx;

import uapi.GeneralException;

import java.math.BigDecimal;

/**
 * A SumReducer is used to sum all item's value
 */
public class SumReducer<T> extends Reducer<T> {

    private SupportedType _type;

    private BigDecimal _sum = new BigDecimal("0");

    SumReducer(Mapper<T> previously) {
        super(previously);
    }

    @Override
    public T getItem() throws NoItemException {
        while (hasItem()) {
            try {
                T item = (T) getPreviously().getItem();
                if (item instanceof Integer) {
                    checkType(SupportedType.INT);
                } else if (item instanceof Float) {
                    checkType(SupportedType.FLOAT);
                } else if (item instanceof Double) {
                    checkType(SupportedType.DOUBLE);
                } else if (item instanceof Long) {
                    checkType(SupportedType.LONG);
                } else {
                    throw new GeneralException("Unsupported type - {}", item.getClass().getName());
                }
                this._sum = this._sum.add(new BigDecimal(item.toString()));
            } catch (NoItemException ex) {
                break;
            }
        }
        switch (this._type) {
            case INT:
                return (T) new Integer(this._sum.intValue());
            case FLOAT:
                return (T) new Float(this._sum.floatValue());
            case DOUBLE:
                return (T) new Double(this._sum.doubleValue());
            case LONG:
                return (T) new Long(this._sum.longValue());
            default:
                throw new GeneralException("Unsupported type - {}", this._type);

        }
    }

    private void checkType(SupportedType type) {
        if (this._type == null) {
            this._type = type;
        } else {
            if (this._type != type) {
                throw new GeneralException("The current item type {} does not match previously item type {}", type, this._type);
            }
        }
    }

    private enum SupportedType {
        INT, FLOAT, DOUBLE, LONG
    }
}
