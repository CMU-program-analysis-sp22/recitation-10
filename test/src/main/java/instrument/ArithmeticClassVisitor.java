package instrument;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ArithmeticClassVisitor extends ClassVisitor {
    /** API Version */
    private static final int API = Opcodes.ASM9;

    /** Constructor */
    public ArithmeticClassVisitor() {
        super(API);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return new MethodVisitor(API, super.visitMethod(access, name, desc, signature, exceptions)) {
            @Override
            public void visitInsn(int opcode) {
                //TODO: Change instances of subtraction to addition!
                //  (currently, this doesn't change anything)
                super.visitInsn(opcode);

                if(opcode == Opcodes.ISUB) {
                    super.visitInsn(Opcodes.IADD);
                } else super.visitInsn(opcode);
            }
        };
    }
}
