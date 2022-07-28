package com.sparky.rider;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.GeneratorAdapter;

import java.util.Objects;

import static org.objectweb.asm.Opcodes.ASM4;

public class RazorEncWritingAccessProviderAdapter extends ClassVisitor {
    protected RazorEncWritingAccessProviderAdapter(ClassVisitor classVisitor) {
        super(ASM4, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor visitor = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (Objects.equals(name, "isSupportedFile") || Objects.equals(name, "isReadonly"))
            return new RazorEncWritingAccessProviderMethodAdapter(visitor, access, name, descriptor);
        return visitor;
    }

    static class RazorEncWritingAccessProviderMethodAdapter extends GeneratorAdapter {
        public RazorEncWritingAccessProviderMethodAdapter(MethodVisitor methodVisitor, int access, String name, String descriptor) {
            super(ASM4, methodVisitor, access, name, descriptor);
        }

        @Override
        public void visitLdcInsn(Object value) {
            if (Objects.equals(value, "Razor")) {
                super.visitLdcInsn("Blazor");
            } else {
                super.visitLdcInsn(value);
            }
        }
    }
}
