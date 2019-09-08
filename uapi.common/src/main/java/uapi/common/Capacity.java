package uapi.common;

import uapi.GeneralException;
import uapi.InvalidArgumentException;

/**
 * Supported below:
 * 1B   == 1 Byte
 * 1KB  == 1 Kilobyte
 * 1MB  == 1 Megabyte
 * 1GB  == 1 Gigabyte
 * 1TB  == 1 Terabyte
 * 1PB  == 1 Petabyte
 *
 * Unsupported below:
 * 1EB  == 1 Exabyte
 * 1ZB  == 1 Zettabyte
 * 1YB  == 1 Yottabyte
 * ...
 */
public class Capacity {

    private static final long CARRY_VALUE   = 1024L;

    private static final long BYTE_OF_KB    = CARRY_VALUE;
    private static final long BYTE_OF_MB    = BYTE_OF_KB * CARRY_VALUE;
    private static final long BYTE_OF_GB    = BYTE_OF_MB * CARRY_VALUE;
    private static final long BYTE_OF_TB    = BYTE_OF_GB * CARRY_VALUE;
    private static final long BYTE_OF_PB    = BYTE_OF_TB * CARRY_VALUE;

    private static final long KB_OF_MB      = CARRY_VALUE;
    private static final long KB_OF_GB      = KB_OF_MB * CARRY_VALUE;
    private static final long KB_OF_TB      = KB_OF_GB * CARRY_VALUE;
    private static final long KB_OF_PB      = KB_OF_TB * CARRY_VALUE;

    private static final long MB_OF_GB      = CARRY_VALUE;
    private static final long MB_OF_TB      = MB_OF_GB * CARRY_VALUE;
    private static final long MB_OF_PB      = MB_OF_TB * CARRY_VALUE;

    private static final long GB_OF_TB      = CARRY_VALUE;
    private static final long GB_OF_PB      = GB_OF_TB * CARRY_VALUE;

    private static final long TB_OF_PB      = CARRY_VALUE;

    public static Capacity parse(String capacityString) {
        ArgumentChecker.required(capacityString, "capacityString");
        var numberBuffer = new StringBuilder();
        var unitBuffer = new StringBuilder();
        var lastNumber = true;
        for (var i = 0; i < capacityString.length(); i++) {
            var c = capacityString.charAt(i);
            if (c >= '0' && c <='9') {
                if (! lastNumber) {
                    throw new InvalidArgumentException(capacityString, InvalidArgumentException.InvalidArgumentType.FORMAT);
                }
                numberBuffer.append(c);
                lastNumber = true;
            } else {
                if (i == 0) {
                    // the first char must be number
                    throw new InvalidArgumentException(capacityString, InvalidArgumentException.InvalidArgumentType.FORMAT);
                }
                unitBuffer.append(c);
                lastNumber = false;
            }
        }
        var unit = CapacityUnit.parse(unitBuffer.toString());
        var value = Long.parseLong(numberBuffer.toString());
        return new Capacity(value, unit);
    }

    private final long _value;
    private final CapacityUnit _unit;

    public Capacity(
            final long value,
            final CapacityUnit unit
    ) {
        ArgumentChecker.checkLong(value, "value", 0, Long.MAX_VALUE);
        ArgumentChecker.required(unit, "unit");
        this._value = value;
        this._unit = unit;
    }

    public long toByteVolume() {
        switch (this._unit) {
            case Byte:
                return this._value;
            case KB:
                return this._value * BYTE_OF_KB;
            case MB:
                return this._value * BYTE_OF_MB;
            case GB:
                return this._value * BYTE_OF_GB;
            case TB:
                return this._value * BYTE_OF_TB;
            case PB:
                return this._value * BYTE_OF_PB;
            default:
                throw new GeneralException("Unsupported unit - {}", this._unit);
        }
    }

    public long toKBVolume() {
        switch (this._unit) {
            case Byte:
                return this._value / BYTE_OF_KB;
            case KB:
                return this._value;
            case MB:
                return this._value * KB_OF_MB;
            case GB:
                return this._value * KB_OF_GB;
            case TB:
                return this._value * KB_OF_TB;
            case PB:
                return this._value * KB_OF_PB;
            default:
                throw new GeneralException("Unsupported unit - {}", this._unit);
        }
    }

    public long toMBVolume() {
        switch (this._unit) {
            case Byte:
                return this._value / BYTE_OF_MB;
            case KB:
                return this._value / KB_OF_MB;
            case MB:
                return this._value;
            case GB:
                return this._value * MB_OF_GB;
            case TB:
                return this._value * MB_OF_TB;
            case PB:
                return this._value * MB_OF_PB;
            default:
                throw new GeneralException("Unsupported unit - {}", this._unit);
        }
    }

    public long toGBVolume() {
        switch (this._unit) {
            case Byte:
                return this._value / BYTE_OF_GB;
            case KB:
                return this._value / KB_OF_GB;
            case MB:
                return this._value / MB_OF_GB;
            case GB:
                return this._value;
            case TB:
                return this._value * GB_OF_TB;
            case PB:
                return this._value * GB_OF_PB;
            default:
                throw new GeneralException("Unsupported unit - {}", this._unit);
        }
    }

    public long toTBVolume() {
        switch (this._unit) {
            case Byte:
                return this._value / BYTE_OF_TB;
            case KB:
                return this._value / KB_OF_TB;
            case MB:
                return this._value / MB_OF_TB;
            case GB:
                return this._value / GB_OF_TB;
            case TB:
                return this._value;
            case PB:
                return this._value * TB_OF_PB;
            default:
                throw new GeneralException("Unsupported unit - {}", this._unit);
        }
    }

    public long toPBVolume() {
        switch (this._unit) {
            case Byte:
                return this._value / BYTE_OF_PB;
            case KB:
                return this._value / KB_OF_PB;
            case MB:
                return this._value / MB_OF_PB;
            case GB:
                return this._value / GB_OF_PB;
            case TB:
                return this._value / TB_OF_PB;
            case PB:
                return this._value;
            default:
                throw new GeneralException("Unsupported unit - {}", this._unit);
        }
    }
}
