/**
 * Copyright (C) 2009-2010 Wilfred Springer
 *
 * This file is part of Preon.
 *
 * Preon is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2, or (at your option) any later version.
 *
 * Preon is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Preon; see the file COPYING. If not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Linking this library statically or dynamically with other modules is making a
 * combined work based on this library. Thus, the terms and conditions of the
 * GNU General Public License cover the whole combination.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce an
 * executable, regardless of the license terms of these independent modules, and
 * to copy and distribute the resulting executable under terms of your choice,
 * provided that you also meet, for each linked independent module, the terms
 * and conditions of the license of that module. An independent module is a
 * module which is not derived from or based on this library. If you modify this
 * library, you may extend this exception to your version of the library, but
 * you are not obligated to do so. If you do not wish to do so, delete this
 * exception statement from your version.
 */
package org.codehaus.preon.el.ctx;

import org.codehaus.preon.el.*;
import org.codehaus.preon.el.util.StringBuilderDocument;
import static org.junit.Assert.*;
import org.junit.Test;

import static org.codehaus.preon.el.Bindings.EarlyBinding;

public class ClassReferenceContextTest {

    @Test
    public void testFromBindings() {
        Expression<Integer, Person> expr = Expressions.from(Person.class)
                .using(EarlyBinding).toInteger("age * 2");
        Person wilfred = new Person();
        wilfred.name = "Wilfred";
        wilfred.age = 35;
        assertEquals(70, expr.eval(wilfred).intValue());
    }
    @Test
    public void testValidReferences() {
        ReferenceContext<Person> context = new ClassReferenceContext<Person>(
                Person.class);
        Reference<Person> personsName = context.selectAttribute("name");
        Reference<Person> fathersName = context.selectAttribute("father")
                .selectAttribute("name");
        Person wilfred = new Person();
        wilfred.name = "Wilfred";
        wilfred.age = 35;
        Person levi = new Person();
        levi.name = "Levi";
        levi.age = 8;
        levi.father = wilfred;
        assertEquals("Levi", personsName.resolve(levi));
        assertEquals("Wilfred", fathersName.resolve(levi));
        assertEquals("Wilfred", personsName.resolve(wilfred));
        StringBuilder builder = new StringBuilder();
        fathersName.document(new StringBuilderDocument(builder));
        System.out.println(builder.toString());
    }

    @Test(expected=BindingException.class)
    public void testInvalidReferences() {
        ReferenceContext<Person> context = new ClassReferenceContext<Person>(
                Person.class);
        context.selectAttribute("gender");
    }

    private static class Person {
        String name;
        int age;
        Person father;
        Person mother;
    }

}
