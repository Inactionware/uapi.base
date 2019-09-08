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
import uapi.common.CollectionHelper;
import uapi.common.StringHelper;
import uapi.rx.Looper;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Represent field in the class
 */
public class FieldMeta {

    private Builder _builder;

    private FieldMeta(final Builder builder) {
        this._builder = builder;
    }

    public String getName() {
        return this._builder._name;
    }

    public String getTypeName() {
        return this._builder._typeName;
    }

    public String getKeyTypeName() {
        return this._builder._keyTypeName;
    }

    public String getValue() {
        return this._builder.getValue();
    }

    public boolean getIsList() {
        return this._builder._isList;
    }

    public boolean getIsMap() {
        return this._builder._isMap;
    }

    public String getModifiers() {
        return CollectionHelper.asString(this._builder._modifiers, " ");
    }

    @Override
    public String toString() {
        return this._builder.toString();
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * The builder for <code>FieldMeta</code>
     */
    public static class Builder extends CodegenBuilder<FieldMeta> {

        private String _name;
        private String _typeName;
        private String _keyTypeName;    // Only for Map type
        private boolean _isList;
        private boolean _isMap;
        private String _value;
        private List<Modifier> _modifiers = new ArrayList<>();

        public Builder setName(
                final String name
        ) throws GeneralException {
            checkStatus();
            this._name = name;
            return this;
        }

        public String getName() {
            return this._name;
        }

        public Builder setTypeName(
                final String typeName
        ) throws GeneralException {
            checkStatus();
            this._typeName = typeName;
            return this;
        }

        public String getTypeName() {
            return this._typeName;
        }

        public Builder setKeyTypeName(
                final String typeName
        ) throws GeneralException {
            checkStatus();
            this._keyTypeName = typeName;
            return this;
        }

        public String getKeyTypeName() {
            return this._keyTypeName;
        }

        public Builder setValue(
                String value
        ) {
            checkStatus();
            this._value = value;
            return this;
        }

        public String getValue() {
            return this._value;
        }

        public Builder setIsList(
                final boolean isList
        ) throws GeneralException {
            checkStatus();
            this._isList = isList;
            return this;
        }

        public Builder setIsMap(
                final boolean isMap
        ) throws GeneralException {
            checkStatus();
            this._isMap = isMap;
            return this;
        }

        public Builder addModifier(
                final Modifier... modifiers
        ) throws GeneralException {
            checkStatus();
            ArgumentChecker.notNull(modifiers, "modifiers");
            if (modifiers.length > 0) {
                Looper.on(modifiers).foreach(this._modifiers::add);
            }
            return this;
        }

        @Override
        protected void validate() throws InvalidArgumentException {
            ArgumentChecker.required(this._name, "fieldName");
            ArgumentChecker.required(this._typeName, "fieldTypeName");
            if (this._isMap) {
                ArgumentChecker.required(this._keyTypeName, "mapKeyTypeName");
            }
        }

        @Override
        protected void initProperties() {
            // Do nothing
        }

        @Override
        protected FieldMeta createInstance() {
            return new FieldMeta(this);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            var builder = (Builder) o;

            if (_isList != builder._isList) return false;
            if (_isMap != builder._isMap) return false;
            if (!_name.equals(builder._name)) return false;
            if (_value != null && !_value.equals(builder._value)) return false;
            if (!_typeName.equals(builder._typeName)) return false;
            if (_isMap) {
                if (!_keyTypeName.equals(builder._keyTypeName)) return false;
            }
            return _modifiers.equals(builder._modifiers);

        }

        @Override
        public int hashCode() {
            var result = _name.hashCode();
            if (_value != null) {
                result = 31 * result + _value.hashCode();
            }
            result = 31 * result + _typeName.hashCode();
            if (_isMap) {
                result = 31 * result + _keyTypeName.hashCode();
            }
            result = 31 * result + (_isList ? 1 : 0);
            result = 31 * result + (_isMap ? 1 : 0);
            result = 31 * result + _modifiers.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return StringHelper.makeString(
                    "FieldMeta[name={}, typeName={}, keyTypeName={}, value={}, isList={}, isMap={}, modifiers={}",
                    this._name, this._typeName, this._keyTypeName, this._value, this._isList, this._isMap, this._modifiers);
        }
    }
}
