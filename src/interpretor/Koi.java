package interpretor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/* It all starts here...
      ___           ___
     /|  |         /\  \
    |:|  |        /::\  \       ___
    |:|  |       /:/\:\  \     /\__\
  __|:|  |      /:/  \:\  \   /:/__/
 /\ |:|__|____ /:/__/ \:\__\ /::\  \
 \:\/:::::/__/ \:\  \ /:/  / \/\:\  \__
  \::/~~/~      \:\  /:/  /   ~~\:\/\__\
   \:\~~\        \:\/:/  /       \::/  /
    \:\__\        \::/  /        /:/  /
     \/__/         \/__/         \/__/

     Created by Daron Adams
*/

public class Koi {
    private static final Interpreter interpreter = new Interpreter();
    static boolean hadError = false;
    static boolean hadRuntimeError = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: klox [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));

        // Show error in the exit code
        if (hadError)
            System.exit(65);
        if (hadRuntimeError)
            System.exit(70);
    }

    // Runs the prompt where code can be entered and executed one line at a time
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        // Prompt is exited by "Control - D" signaling the end condition to the line reader
        for (;;) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null) break;
            run(line);
            hadError = false; // resetting the flag in the loop
        }
    }

    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();
        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();

        // Stop if there is a syntax error.
        if (hadError) return;

        interpreter.interpret(statements);


    }

    static void error(int line, String message) {
        report(line, "", message);
    }

    // Showing the user what line the error is on
    private static void report(int line, String where, String message) {
        System.err.println(
                "[line " + line + "] Error" + where + "; " + message);
        hadError = true;
    }

    // Basic Error handling
    static void error(Token token, String message) {
        if (token.type == TokenType.EOF) {
            report(token.line, "", message);
        } else {
            report(token.line, " at '" + token.lexeme + "'", message);
        }

    }

    static void runTimeError(RuntimeError error) {
        System.err.println(error.getMessage() +
                "/n[line " + error.token.line + "]");
        hadRuntimeError = true;
    }
}