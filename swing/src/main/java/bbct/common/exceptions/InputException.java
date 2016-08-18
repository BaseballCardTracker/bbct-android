/*
 * This file is part of BBCT.
 *
 * Copyright 2012-14 codeguru <codeguru@users.sourceforge.net>
 *
 * BBCT is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BBCT is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package bbct.common.exceptions;

/**
 * This exception signals an error during user input operations and is used by
 * the BBCT GUI classes.
 */
@SuppressWarnings("serial")
public class InputException extends Exception {

    /**
     * Creates a new {@link InputException} with the given error message.
     *
     * @param msg An error message explaining the reason for the exception.
     */
    public InputException(String msg) {
        super(msg);
    }

    /**
     * Creates a new {@link InputException} with the given cause.
     *
     * @param cause The {@link java.lang.Throwable} object which caused this
     * {@link InputException}.
     */
    public InputException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new {@link InputException} with the given error message.
     *
     * @param msg An error message explaining the reason for the exception.
     * @param cause The {@link java.lang.Throwable} object which caused this
     * {@link InputException}.
     */
    public InputException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
