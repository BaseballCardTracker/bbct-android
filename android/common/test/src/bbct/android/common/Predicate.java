/*
 * This file is part of bbct.
 *
 * Copyright 2012 codeguru <codeguru@users.sourceforge.net>
 *
 * bbct is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * bbct is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package bbct.android.common;

/**
 * This is a generic template which provides a tool for functional programming
 * in Java. Any class implementing this template must provide a single method,
 * {@link #doTest}, which tests if the given object agrees with the predicate
 * defined by the implementation.
 *
 * @param <T> The class of objects to which this {@link Predicate} applies.
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public interface Predicate<T> {

    /**
     * Tests if the given object satisfies the predicate defined by the
     * implementation.
     *
     * @param obj The object to test.
     * @return <code>true</code> if the given object satisfies the predicate
     * defined by the implementation. <code>false</code>, otherwise.
     */
    public boolean doTest(T obj);
}
