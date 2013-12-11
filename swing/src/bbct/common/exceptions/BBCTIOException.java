/*
 * This file is part of BBCT.
 *
 * Copyright 2012 codeguru <codeguru@users.sourceforge.net>
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
 * This exception signals any I/O errors in
 * {@link bbct.common.data.BaseballCardIO} implementations.
<<<<<<< HEAD
 *
 * @author codeguru <codeguru@users.sourceforge.net>
=======
>>>>>>> Javadocs: Remove all @author tags.
 */
@SuppressWarnings("serial")
public class BBCTIOException extends Exception {

    /**
     * Creates a new {@link BBCTIOException} with the given source.
     *
     * @param source The source of this {@link BBCTIOException}.
     */
    public BBCTIOException(Throwable source) {
        super(source);
    }

    /**
     * Creates a new {@link BBCTIOException} with the given message and source.
     *
     * @param msg A human-readable message describing the reason for this
     * exception.
     * @param source The source of this {@link BBCTIOException}.
     */
    public BBCTIOException(String msg, Throwable source) {
        super(msg, source);
    }
}
