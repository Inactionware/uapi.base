package uapi.codegen;

import com.google.common.base.Strings;
import uapi.GeneralException;
import uapi.InvalidArgumentException;
import uapi.common.ArgumentChecker;
import uapi.common.StringHelper;

import java.util.Objects;

public class PropertyMeta {

    public static Builder builder() {
        return new Builder();
    }

    private final Builder _builder;

    private PropertyMeta(final Builder builder) {
        this._builder = builder;
    }

    public String fieldName() {
        return this._builder.fieldName();
    }

    public String fieldType() {
        return this._builder.fieldType();
    }

    public boolean generateField() {
        return this._builder.generateField();
    }

    public boolean generateSetter() {
        return this._builder.generateSetter();
    }

    public boolean generateGetter() {
        return this._builder.generateGetter();
    }

    public String setterName() {
        return this._builder.setterName();
    }

    public String getterName() {
        return this._builder.getterName();
    }

    public boolean isCollection() {
        return this._builder.isCollection();
    }

    public boolean isMap() {
        return this._builder.isMap();
    }

    public static class Builder extends CodegenBuilder<PropertyMeta> {

        private String _fieldName;
        private String _fieldType;
        private boolean _genField;
        private boolean _genSetter;
        private boolean _genGetter;
        private boolean _isCollection;
        private boolean _isMap;

        public Builder setFieldName(String fieldName) {
            checkStatus();
            this._fieldName = fieldName;
            return this;
        }

        public String fieldName() {
            return this._fieldName;
        }

        public Builder setFieldType(String fieldType) {
            checkStatus();
            this._fieldType = fieldType;
            return this;
        }

        public String fieldType() {
            return this._fieldType;
        }

        public Builder setGenerateField(boolean genField) {
            checkStatus();
            this._genField = genField;
            return this;
        }

        public boolean generateField() {
            return this._genField;
        }

        public Builder setGenerateSetter(boolean hasSetter) {
            checkStatus();
            this._genSetter = hasSetter;
            return this;
        }

        public boolean generateSetter() {
            return this._genSetter;
        }

        public Builder setGenerateGetter(boolean hasGetter) {
            checkStatus();
            this._genGetter = hasGetter;
            return this;
        }

        public boolean generateGetter() {
            return this._genGetter;
        }

        public Builder setIsCollection(boolean isCollection) {
            checkStatus();
            this._isCollection = isCollection;
            return this;
        }

        public boolean isCollection() {
            return this._isCollection;
        }

        public Builder setIsMap(boolean isMap) {
            checkStatus();
            this._isMap = isMap;
            return this;
        }

        public boolean isMap() {
            return this._isMap;
        }

        public String setterName() {
            if (Strings.isNullOrEmpty(this._fieldName)) {
                return null;
            }
            return ClassHelper.makeSetterName(this._fieldName, this._isCollection, this._isMap);
        }

        public String getterName() {
            if (Strings.isNullOrEmpty(this._fieldName)) {
                return null;
            }
            return ClassHelper.makeGetterName(this._fieldName);
        }

        @Override
        protected void validate() throws InvalidArgumentException {
            ArgumentChecker.required(this._fieldName, "fieldName");
            ArgumentChecker.required(this._fieldType, "fieldType");
            if (this._isCollection && this._isMap) {
                throw new GeneralException("The property cannot be a collection and a map - {}", this._fieldName);
            }
        }

        @Override
        protected void initProperties() {
            // do nothing
        }

        @Override
        protected PropertyMeta createInstance() {
            return new PropertyMeta(this);
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (other == null || getClass() != other.getClass()) return false;
            var builder = (Builder) other;
            return Objects.equals(this._fieldName, builder._fieldName) &&
                    this._fieldType.equals(builder._fieldType) &&
                    this._isCollection == builder._isCollection &&
                    this._isMap == builder._isMap;
        }

        @Override
        public String toString() {
            return StringHelper.makeString(
                    "PropertyMeta[" +
                            "fieldName={}, " +
                            "fieldType={}, " +
                            "generateField={}, " +
                            "generateSetter={}, " +
                            "generateGetter={}, " +
                            "isCollection={}, " +
                            "isMap={}]",
                    this._fieldName,
                    this._fieldType,
                    this._genField,
                    this._genSetter,
                    this._genGetter,
                    this._isCollection,
                    this._isMap
            );
        }

        @Override
        public int hashCode() {
            return Objects.hash(
                    this._fieldName,
                    this._fieldType,
                    this._isCollection,
                    this._isMap);
        }
    }
}
