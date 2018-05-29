package uapi.common;

import uapi.GeneralException;

public enum CapacityUnit {

    Byte("Byte"),

    KB("KB"),

    MB("MB"),

    GB("GB"),

    TB("TB"),

    PB("PB");

    public static CapacityUnit parse(String unitString) {
        ArgumentChecker.required(unitString, "unitString");
        if (Byte.unitName().equals(unitString)) {
            return Byte;
        } else if (KB.unitName().equals(unitString)) {
            return KB;
        } else if (MB.unitName().equals(unitString)) {
            return MB;
        } else if (GB.unitName().equals(unitString)) {
            return GB;
        } else if (TB.unitName().equals(unitString)) {
            return TB;
        } else if (PB.unitName().equals(unitString)) {
            return PB;
        } else {
            throw new GeneralException("Unsupported unit - {}", unitString);
        }
    }

    private final String _unitName;

    CapacityUnit(final String unitName) {
        ArgumentChecker.required(unitName, "unitName");
        this._unitName = unitName;
    }

    String unitName() {
        return this._unitName;
    }
}
