package tool;

import java.beans.Expression;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/* Tool to help generate the numerous expression children classes
   that we will need. */

public class GenerateAst {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output directory>");
            System.exit(64);
        }
        String outputDir = args[0];

        defineAst(outputDir, "Expr", Arrays.asList(
                "Binary : Expression left, Token Operator, Expression right",
                "Grouping : Expr expression",
                "Literal : Object value",
                "Unary : Token operator, Expr right"
        ));
    }

    public static void defineAst(
            String outputDir, String baseName, List<String> types)
            throws IOException {
        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        writer.println("package interpretor");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseName + "{");

        writer.println("}");

    }
}
