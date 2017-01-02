package uapi.rx;

import uapi.GeneralException;

/**
 * The exception indicate no item can be generated
 */
public class NoItemException extends GeneralException {

    public NoItemException() {
        super("There is no item from previously operator");
    }
}
