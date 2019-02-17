package uapi.common;

public class Numeric {

    public static MutableInteger mutableInteger() {
        return mutableInteger(0);
    }

    public static MutableInteger mutableInteger(int value) {
        return new MutableInteger(value);
    }

    public static class MutableInteger {

        private int _value;

        private MutableInteger(int value) {
            this._value = value;
        }

        public int value() {
            return this._value;
        }

        public void setValue(int value) {
            this._value = value;
        }

        public void increase() {
            this._value++;
        }

        public void decrease() {
            this._value--;
        }
    }
}
