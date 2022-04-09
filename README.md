## Setup

Install Maven by
following the [directions](https://maven.apache.org/install.html) from Apache
or using your favored package installer on Mac or Linux.

Build the code in this repo using
```[shell script]
mvn clean install
```

## Bytecode

Java compiles into JVM bytecode in `.class` files, and these (not the original
`.java` files) are run on the JVM when a Java program is run.

To see what that bytecode actually looks like, we can use `javap -c`, which
gives the bytecode stored in `.class` files. For example, in the 
`target/classes` directory, try running:
```shell script
javap -c mutable/HelloWorld
```
It should print the following:
```java
Compiled from "HelloWorld.java"
public class mutable.HelloWorld {
  public mutable.HelloWorld();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: return

  public static void main(java.lang.String[]);
    Code:
       0: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
       3: ldc           #3                  // String Hello, World!
       5: invokevirtual #4                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
       8: return
}
```

The first function, `mutable.HelloWorld()`, is the constructor, which all
Java objects have. It's recognizable by its invocation of `<init>`. 

Of more interest is the second function, `main(String[])`, which is actually defined in the
program in `mutable/HelloWorld.java` as a single invocation of the method
`System.out.println` with the string `Hello, World!`. The bytecode for this
function first gets the object `System.out` with `getstatic`, then loads the
constant string `Hello, World!` with `ldc`, and finally invokes the `println`
method from `System.out` before returning.

Not only does this show us the general form of Java bytecode, it gives us the
specific instructions we need in order to print something, which we'll use
in the next section.

## Challenge 1: Coverage Instrumentation

We're going to instrument bytecode in order to get code coverage information.

To instrument bytecode, we're going to use the `org.objectweb.asm` package from
ow2. It's built on a visitor pattern, providing the ability to change what the 
program does when processing each bytecode instruction. The code we're going
to change for this section is in `instrument.CoverageClassVisitor`.

To get line number information, we're going to override the ClassVisitor's
function for visiting a line number by having it additionally execute the
instructions that we saw print a string in the previous section:
- `getstatic` for `System.out`
- `ldc` the string we want to load (here, the line number)
- `invokevirtual` for `println`

When you think you've correctly overridden the method, test it by uncommenting
line 26 of `main/MainGoal.java` and running
```shell script
mvn clean install
mvn cmu.program-analysis:recitation-10:1.0-SNAPSHOT:main
mvn compile exec:java -Dexec.mainClass="mutable.HelloWorld"
```

## Challenge 2: Program Repair

Our next challenge is to change the program behavior itself. Try running the
other test program, an implementation of Selection Sort, using:
```shell script
mvn compile exec:java -Dexec.mainClass="mutable.SelectionSort" -Dexec.args="3 2 1"
```
We want it to sort the output, printing `[1, 2, 3]`, but, instead, it throws an
ArrayIndexOutOfBoundsException! There's a bug in this sort function (available
to view in `mutable/SelectionSort.java`). We could look for it to fix it 
manually, but, for this exercise, we're going to try instrumenting the code
instead.

Through magic intuition, we know that the problem will be fixed by changing the
subtractions in the program to additions. Try to do this by changing
`visitInsn` in `instrument.ArithmeticClassVisitor`.

When you think you've correctly overridden the method, test it by uncommenting
line 27 of `main/MainGoal.java` and running
```shell script
mvn clean install
mvn cmu.program-analysis:recitation-10:1.0-SNAPSHOT:main
mvn compile exec:java -Dexec.mainClass="mutable.SelectionSort" -Dexec.args="3 2 1"
```

## Resources
[ASM Javadoc](https://asm.ow2.io/javadoc/index.html)
