package solution;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ArithmeticClassVisitor extends ClassVisitor {
    /** API Version */
    private static final int API = Opcodes.ASM9;

    /** Constructor */
    public ArithmeticClassVisitor(ClassWriter cw) {
        super(API, cw);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return new MethodVisitor(API, super.visitMethod(access, name, desc, signature, exceptions)) {
            @Override
            public void visitInsn(int opcode) {
                if(opcode == Opcodes.ISUB) {
                    super.visitInsn(Opcodes.IADD);
                } else super.visitInsn(opcode);
            }
        };
    }
}
