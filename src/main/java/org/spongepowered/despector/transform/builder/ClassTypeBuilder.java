/*
 * The MIT License (MIT)
 *
 * Copyright (c) Despector <https://despector.voxelgenesis.com>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.despector.transform.builder;

import org.spongepowered.despector.Language;
import org.spongepowered.despector.ast.SourceSet;
import org.spongepowered.despector.ast.type.ClassEntry;

public class ClassTypeBuilder extends TypeBuilder<ClassTypeBuilder> {

    private String supertype;

    public ClassTypeBuilder(String n) {
        super(n);
        reset();
    }

    public ClassTypeBuilder supertype(String spr) {
        this.supertype = spr;
        return this;
    }

    @Override
    public ClassTypeBuilder reset() {
        super.reset();
        this.supertype = "Ljava/lang/Object;";
        return this;
    }

    public ClassEntry build(SourceSet set) {
        ClassEntry entry = new ClassEntry(set, Language.JAVA, this.name);
        entry.setSuperclass(this.supertype);
        apply(entry);
        return entry;
    }

}
