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
package org.spongepowered.despector.decompiler.method.graph.data.opcode;

import org.spongepowered.despector.decompiler.ir.Insn;
import org.spongepowered.despector.decompiler.method.graph.data.block.BlockSection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a section of opcodes in the original method that forms part of a
 * graph representing the control flow through the method. The opcode blocks are
 * created by sectioning the original opcodes at any point that may cause a jump
 * and any point targeted by a jump.
 */
public abstract class OpcodeBlock {

    protected int start_pc;
    protected int end_pc;
    protected final List<Insn> opcodes = new ArrayList<>();
    protected OpcodeBlock target;

    protected Set<OpcodeBlock> targetted_by = new HashSet<>();
    protected boolean exclude_from_ternary_check = false;

    public OpcodeBlock(int start, int end) {
        this.start_pc = start;
        this.end_pc = end;
    }

    /**
     * Gets the index of the opcode which this block was formed starting at. Can
     * be used as a general method of ordering blocks but beware as it is not
     * recalculated if block are split or joined.
     */
    public int getStart() {
        return this.start_pc;
    }

    public int getEnd() {
        return this.end_pc;
    }

    public void setBounds(int start, int end) {
        this.start_pc = start;
        this.end_pc = end;
    }

    /**
     * Gets the opcodes that are part of this block.
     */
    public List<Insn> getOpcodes() {
        return this.opcodes;
    }

    public Insn getLast() {
        return this.opcodes.get(this.opcodes.size() - 1);
    }

    /**
     * Gets the block targeted by this block. If this block ends with a
     * condition jump the target will be the block that control will be passed
     * to if the condition is true.
     */
    public OpcodeBlock getTarget() {
        return this.target;
    }

    /**
     * Gets if this block has a target block.
     */
    public boolean hasTarget() {
        return this.target != null;
    }

    /**
     * Sets the target block of this block.
     */
    public void setTarget(OpcodeBlock block) {
        this.target = block;
    }

    /**
     * Gets a set of blocks which target this block.
     */
    public Set<OpcodeBlock> getTargettedBy() {
        return this.targetted_by;
    }

    /**
     * Adds a block as having this block as a target.
     */
    public void targettedBy(OpcodeBlock block) {
        this.targetted_by.add(block);
    }

    /**
     * Gets if this block is omitted from the check for ternaries.
     * 
     * <p>As an example: typically statements do not overlap blocks so a check
     * is done if a block's initial opcodes expect values to be already on the
     * stack which indicates that the block is preceeded by a ternary which is
     * then precompiled in a pre-pass.</p>
     * 
     * <p>However in the event of a try-catch statement ending in a return the
     * marker for the end of the try block will split the return from the values
     * it is returning.</p>
     */
    public boolean isOmittedFromTernaryCheck() {
        return this.exclude_from_ternary_check;
    }

    /**
     * Sets this block as omitted from the ternary check.
     */
    public void omitFromTernaryCheck(boolean state) {
        this.exclude_from_ternary_check = state;
    }

    public abstract BlockSection toBlockSection();

    /**
     * Prints this block's information for debugging.
     */
    public void print() {
        System.out.println(toString());
    }

    /**
     * Gets a single line of information for emitting this block in a comment
     * for debugging.
     */
    public abstract String getDebugHeader();

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getDebugHeader()).append("\n");
        for (Insn insn : this.opcodes) {
            builder.append("  ").append(insn.toString()).append("\n");
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OpcodeBlock)) {
            return false;
        }
        OpcodeBlock op = (OpcodeBlock) o;
        return op.getStart() == this.start_pc && op.getEnd() == this.end_pc;
    }

}
