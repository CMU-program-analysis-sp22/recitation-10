package main;

import instrument.ArithmeticClassVisitor;
import instrument.CoverageClassVisitor;
import instrument.ExploratoryClassVisitor;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;

public class MainGoal {
    public static void main(String[] args) throws MojoExecutionException, MojoFailureException {
        try {
            //TODO invalid major version?
            explore("mutable.HelloWorld"); //TODO this might just be redundant
            //coverageInstrument("mutable.HelloWorld"); //TODO uncomment me to test Challenge 1
            //arithmeticMutate("mutable.SelectionSort"); //TODO uncomment me to test Challenge 2
            //coverageInstrument("mutable.SelectionSort"); //TODO uncomment me to test Challenges 1 and 2 together
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void explore(String className) throws IOException {
        File f = new File("target/classes/" + className.replace(".", "/") + ".class");
        byte[] classBytes;
        try(FileInputStream fis = new FileInputStream(f)) {
            classBytes = fis.readAllBytes();
        }
        ClassReader reader = new ClassReader(classBytes);
        ClassWriter writer = new ClassWriter(reader, COMPUTE_FRAMES | COMPUTE_MAXS);
        ClassVisitor visitor = new ExploratoryClassVisitor(writer);
        reader.accept(visitor, 0);
        try(FileOutputStream fos = new FileOutputStream(f, false)) {
            fos.write(writer.toByteArray());
        }
    }

    public static void coverageInstrument(String className) throws IOException {
        File f = new File("target/classes/" + className.replace(".", "/") + ".class");
        byte[] classBytes;
        try(FileInputStream fis = new FileInputStream(f)) {
            classBytes = fis.readAllBytes();
        }
        ClassReader reader = new ClassReader(classBytes);
        ClassVisitor visitor = new CoverageClassVisitor();
        reader.accept(visitor, 0);
        ClassWriter writer = new ClassWriter(reader, 0);
        try(FileOutputStream fos = new FileOutputStream(f)) {
            fos.write(writer.toByteArray());
        }
    }

    public static void arithmeticMutate(String className) throws IOException {
        File f = new File("target/classes/" + className.replace(".", "/") + ".class");
        byte[] classBytes;
        try(FileInputStream fis = new FileInputStream(f)) {
            classBytes = fis.readAllBytes();
        }
        ClassReader reader = new ClassReader(classBytes);
        ClassVisitor visitor = new ArithmeticClassVisitor();
        reader.accept(visitor, 0);
        ClassWriter writer = new ClassWriter(reader, 0);
        try(FileOutputStream fos = new FileOutputStream(f)) {
            fos.write(writer.toByteArray());
        }
    }
}
