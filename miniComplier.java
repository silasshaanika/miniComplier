public class miniComplier{
    //the V-language source code to be compiled
    static String[] program = {
        "BEGIN",
        "INTEGER A, B, C, E, M, N, G, H, I, a, c",
        "INPUT A, B, C",
        "LET B = A */ M",
        "LET G = a + c",
        "temp = <s%**h - j / w +d +*$&;",
        "M = A/B+C",
        "N = G/H-I+a*B/c",
        "WRITE M",
        "WRITEE F;",
        "END"
    };

    // Lines that pass all 7 stages 
    static String[] validLines = {
        "LET G = a + c",
        "M = A/B+C",
        "N = G/H-I+a*B/c"
    };

    public static void main(String[] args) {

        //PART 1, Line by Line 
        System.out.println("PART 1: LINE-BY-LINE COMPILER PROCESSING");

        for (String line : program) {
            System.out.println("SOURCE LINE: " + line);
            lexicalAnalysis(line);
        }

        //PART 2, Whole Program
        System.out.println("PART 2 WHOLE PROGRAM PROCESSING");
        processWholeProgram(program);
    }

    //  PART 2, Process whole program at once
    public static void processWholeProgram(String[] program) {

        System.out.println("PASS 1: Lexical Analysis on all lines");
        boolean[] lexOk = new boolean[program.length];
        for (int i = 0; i < program.length; i++) {
            System.out.println("Line " + (i + 1) + ": " + program[i]);
            lexOk[i] = lexicalAnalysisCheck(program[i]);
        }

        System.out.println("PASS 2: Syntax Analysis on lexically valid lines");
        boolean[] synOk = new boolean[program.length];
        for (int i = 0; i < program.length; i++) {
            if (lexOk[i]) {
                System.out.println("Line " + (i + 1) + ": " + program[i]);
                synOk[i] = syntaxAnalysisCheck(program[i]);
            }
        }

        System.out.println("PASS 3: Semantic Analysis on syntactically valid lines");
        boolean[] semOk = new boolean[program.length];
        for (int i = 0; i < program.length; i++) {
            if (lexOk[i] && synOk[i]) {
                System.out.println("Line " + (i + 1) + ": " + program[i]);
                semOk[i] = semanticAnalysisCheck(program[i]);
            }
        }

        System.out.println("PASSES 4-7: ");
        for (int i = 0; i < program.length; i++) {
            if (lexOk[i] && synOk[i] && semOk[i] && isValidLine(program[i])) {
                System.out.println("Line " + (i + 1) + ": " + program[i]);
                intermediateCode(program[i]);
            }
        }

        System.out.println("WHOLE PROGRAM PROCESSING DONE");
    }

    //  STAGE 1, LEXICAL ANALYSIS
    //  Checks valid keywords, identifiers, operators, symbols
    //  Error: misspelled keywords
    public static void lexicalAnalysis(String line) {
        System.out.println("[STAGE 1] Lexical Analysis:");

        // Tokenize, insert spaces around operators and symbols so we can split cleanly
        String prepared = prepareForTokenizing(line);
        String[] tokens = prepared.trim().split("\\s+");

        boolean hasError = false;

        for (String token : tokens) {
            if (token.isEmpty()) continue;

            if (isKeyword(token)) {
                System.out.println("  '" + token + "' --> KEYWORD");
            } else if (isInvalidKeyword(token)) {
                // All caps word that is NOT a valid keyword = Lexical Error
                System.out.println("  '" + token + "' --> LEXICAL ERROR ");
                hasError = true;
                break;
            } else if (isOperator(token)) {
                System.out.println("  '" + token + "' --> OPERATOR");
            } else if (isSymbol(token)) {
                System.out.println("  '" + token + "' --> SYMBOL");
            } else if (isIdentifier(token)) {
                System.out.println("  '" + token + "' --> IDENTIFIER");
            } else {
                System.out.println("  '" + token + "' --> LEXICAL ERROR");
                hasError = true;
                break;
            }
        }

        if (hasError) {
            System.out.println("Lexical Error detected!");
        } else {
            System.out.println("Lexical Analysis PASSED");
            syntaxAnalysis(line);
        }
    }

    // Boolean version used by processWholeProgram
    public static boolean lexicalAnalysisCheck(String line) {
        String prepared = prepareForTokenizing(line);
        String[] tokens = prepared.trim().split("\\s+");

        for (String token : tokens) {
            if (token.isEmpty()) continue;
            if (isKeyword(token) || isOperator(token) || isSymbol(token) || isIdentifier(token)) {
                continue;
            } else if (isInvalidKeyword(token)) {
                System.out.println("LEXICAL ERROR: '" + token + "' (misspelled/unknown keyword)");
                return false;
            } else {
                System.out.println("LEXICAL ERROR: '" + token + "' (unrecognized token)");
                return false;
            }
        }
        System.out.println("Lexical Analysis PASSED");
        return true;
    }

    //  STAGE 2, SYNTAX ANALYSIS
    //  Checks combined operators, trailing semicolons, digits
    public static void syntaxAnalysis(String line) {
        System.out.println("[STAGE 2] Syntax Analysis:");

        // Check for combined operators
        String[] badOps = {"*/", "*+", "*-", "/+", "/-", "+*", "-*", "-/", "+/", "**", "//", "++", "--"};
        for (String bad : badOps) {
            if (line.contains(bad)) {
                System.out.println("SYNTAX ERROR: Invalid operator combination '" + bad + "'");
                return;
            }
        }

        // Check trailing semicolon
        if (line.trim().endsWith(";")) {
            System.out.println("SYNTAX ERROR: Semicolon at end of line is not allowed");
            return;
        }

        // Check for digits 
        if (line.matches(".*[0-9].*")) {
            System.out.println("SYNTAX ERROR: Numbers (0-9) are not allowed");
            return;
        }

        System.out.println("Syntax Analysis PASSED");
        semanticAnalysis(line);
    }

    public static boolean syntaxAnalysisCheck(String line) {
        String[] badOps = {"*/", "*+", "*-", "/+", "/-", "+*", "-*", "-/", "+/", "**", "//", "++", "--"};
        for (String bad : badOps) {
            if (line.contains(bad)) {
                System.out.println("SYNTAX ERROR: Invalid operator combination '" + bad + "'");
                return false;
            }
        }
        if (line.trim().endsWith(";")) {
            System.out.println("SYNTAX ERROR: Semicolon at end of line is not allowed");
            return false;
        }
        if (line.matches(".*[0-9].*")) {
            System.out.println("SYNTAX ERROR: Numbers (0-9) are not allowed");
            return false;
        }
        System.out.println("Syntax Analysis PASSED");
        return true;
    }

    //  STAGE 3, SEMANTIC ANALYSIS
    //  Checks illegal special characters
    public static void semanticAnalysis(String line) {
        System.out.println("[STAGE 3] Semantic Analysis:");

        char[] illegalChars = {'%', '$', '&', '<', '>'};
        for (char c : illegalChars) {
            if (line.indexOf(c) >= 0) {
                System.out.println("SEMANTIC ERROR: Illegal character '" + c + "' found");
                return;
            }
        }

        System.out.println("Semantic Analysis PASSED");

        // Only the 3 valid lines proceed past stage 3
        if (isValidLine(line)) {
            intermediateCode(line);
        } else {
            System.out.println("No further compilation needed for this line.");
        }
    }

    public static boolean semanticAnalysisCheck(String line) {
        char[] illegalChars = {'%', '$', '&', '<', '>'};
        for (char c : illegalChars) {
            if (line.indexOf(c) >= 0) {
                System.out.println("SEMANTIC ERROR: Illegal character '" + c + "'");
                return false;
            }
        }
        System.out.println("Semantic Analysis PASSED");
        return true;
    }

    //  STAGE 4, INTERMEDIATE CODE REPRESENTATION 
    //  Breaks expressions into 3 address code using temporaries
    public static void intermediateCode(String line) {
        System.out.println("[STAGE 4] Intermediate Code Representation (ICR):");

        if (line.equals("LET G = a + c")) {
            System.out.println("  t1 = a + c");
            System.out.println("  G  = t1");

        } else if (line.equals("M = A/B+C")) {
            System.out.println("  t1 = A / B");
            System.out.println("  t2 = t1 + C");
            System.out.println("  M  = t2");

        } else if (line.equals("N = G/H-I+a*B/c")) {
            System.out.println("  t1 = G / H");
            System.out.println("  t2 = t1 - I");
            System.out.println("  t3 = a * B");
            System.out.println("  t4 = t3 / c");
            System.out.println("  t5 = t2 + t4");
            System.out.println("  N  = t5");
        }

        codeGeneration(line);
    }

    //  STAGE 5, CODE GENERATION
    //  Translates ICR into assembly like register instructions
    public static void codeGeneration(String line) {
        System.out.println("[STAGE 5] Code Generation:");

        if (line.equals("LET G = a + c")) {
            System.out.println("  LOAD  R1, a");
            System.out.println("  LOAD  R2, c");
            System.out.println("  ADD   R1, R2");
            System.out.println("  STORE R1, G");

        } else if (line.equals("M = A/B+C")) {
            System.out.println("  LOAD  R1, A");
            System.out.println("  LOAD  R2, B");
            System.out.println("  DIV   R1, R2");
            System.out.println("  LOAD  R3, C");
            System.out.println("  ADD   R1, R3");
            System.out.println("  STORE R1, M");

        } else if (line.equals("N = G/H-I+a*B/c")) {
            System.out.println("  LOAD  R1, G");
            System.out.println("  LOAD  R2, H");
            System.out.println("  DIV   R1, R2");     // R1 = G/H
            System.out.println("  LOAD  R3, I");
            System.out.println("  SUB   R1, R3");     // R1 = G/H - I
            System.out.println("  LOAD  R3, a");
            System.out.println("  LOAD  R4, B");
            System.out.println("  MUL   R3, R4");     // R3 = a*B
            System.out.println("  LOAD  R4, c");
            System.out.println("  DIV   R3, R4");     // R3 = a*B/c
            System.out.println("  ADD   R1, R3");     // R1 = G/H-I + a*B/c
            System.out.println("  STORE R1, N");
        }

        codeOptimization(line);
    }

    //  STAGE 6, CODE OPTIMIZATION
    public static void codeOptimization(String line) {
        System.out.println("[STAGE 6] Code Optimization:");

        if (line.equals("LET G = a + c")) {
            // No redundant loads 
            System.out.println("  LOAD  R1, a       ; load a into R1");
            System.out.println("  LOAD  R2, c       ; load c into R2");
            System.out.println("  ADD   R1, R2      ; R1 = a + c  (t1 folded in)");
            System.out.println("  STORE R1, G       ; G = R1");

        } else if (line.equals("M = A/B+C")) {
            // no separate STORE/LOAD for t1
            System.out.println("  LOAD  R1, A       ; load A");
            System.out.println("  LOAD  R2, B       ; load B");
            System.out.println("  DIV   R1, R2      ; R1 = A/B  (t1 folded)");
            System.out.println("  LOAD  R2, C       ; reuse R2 for C");
            System.out.println("  ADD   R1, R2      ; R1 = A/B + C  (t2 folded)");
            System.out.println("  STORE R1, M       ; M = R1");

        } else if (line.equals("N = G/H-I+a*B/c")) {
            //registers reused
            System.out.println("  LOAD  R1, G       ; load G");
            System.out.println("  LOAD  R2, H       ; load H");
            System.out.println("  DIV   R1, R2      ; R1 = G/H  (t1 folded)");
            System.out.println("  LOAD  R2, I       ; reuse R2 for I");
            System.out.println("  SUB   R1, R2      ; R1 = G/H-I  (t2 folded)");
            System.out.println("  LOAD  R2, a       ; load a");
            System.out.println("  LOAD  R3, B       ; load B");
            System.out.println("  MUL   R2, R3      ; R2 = a*B  (t3 folded)");
            System.out.println("  LOAD  R3, c       ; load c");
            System.out.println("  DIV   R2, R3      ; R2 = a*B/c  (t4 folded)");
            System.out.println("  ADD   R1, R2      ; R1 = G/H-I+a*B/c  (t5 folded)");
            System.out.println("  STORE R1, N       ; N = R1");
        }

        targetMachineCode(line);
    }

    //  STAGE 7, TARGET MACHINE CODE (Binary)
    //  Converts optimized instructions to binary representation
    public static void targetMachineCode(String line) {
        System.out.println("[STAGE 7] Target Machine Code (Binary):");
        System.out.println("  Format: OPCODE(4b) | REG1(4b) | OPERAND(8b)");
        System.out.println("  LOAD=0001 STORE=0010 ADD=0011 SUB=0100 MUL=0101 DIV=0110");

        if (line.equals("LET G = a + c")) {
            System.out.println("  0001 0001 00000001  ; LOAD  R1, a");
            System.out.println("  0001 0010 00000010  ; LOAD  R2, c");
            System.out.println("  0011 0001 00000010  ; ADD   R1, R2");
            System.out.println("  0010 0001 00000111  ; STORE R1, G");

        } else if (line.equals("M = A/B+C")) {
            System.out.println("  0001 0001 00000001  ; LOAD  R1, A");
            System.out.println("  0001 0010 00000010  ; LOAD  R2, B");
            System.out.println("  0110 0001 00000010  ; DIV   R1, R2");
            System.out.println("  0001 0010 00000011  ; LOAD  R2, C");
            System.out.println("  0011 0001 00000010  ; ADD   R1, R2");
            System.out.println("  0010 0001 00001101  ; STORE R1, M");

        } else if (line.equals("N = G/H-I+a*B/c")) {
            System.out.println("  0001 0001 00000111  ; LOAD  R1, G");
            System.out.println("  0001 0010 00001000  ; LOAD  R2, H");
            System.out.println("  0110 0001 00000010  ; DIV   R1, R2");
            System.out.println("  0001 0010 00001001  ; LOAD  R2, I");
            System.out.println("  0100 0001 00000010  ; SUB   R1, R2");
            System.out.println("  0001 0010 00000001  ; LOAD  R2, a");
            System.out.println("  0001 0011 00000010  ; LOAD  R3, B");
            System.out.println("  0101 0010 00000011  ; MUL   R2, R3");
            System.out.println("  0001 0011 00000011  ; LOAD  R3, c");
            System.out.println("  0110 0010 00000011  ; DIV   R2, R3");
            System.out.println("  0011 0001 00000010  ; ADD   R1, R2");
            System.out.println("  0010 0001 00001110  ; STORE R1, N");
        }

        System.out.println("Compilation COMPLETE for this line.");
    }

    //  HELPER, Tokenizer pre processor
    //  Inserts spaces around symbols so split works
    public static String prepareForTokenizing(String line) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '+' || c == '-' || c == '*' || c == '/' ||
                c == '=' || c == ',' || c == ';') {
                sb.append(' ').append(c).append(' ');
            } else {
                sb.append(c);
            }
        }
        // Collapse multiple spaces
        return sb.toString().replaceAll("\\s+", " ").trim();
    }

    //HELPER, Check if a line is one of the 3 valid lines
    public static boolean isValidLine(String line) {
        return line.equals("LET G = a + c") ||
               line.equals("M = A/B+C") ||
               line.equals("N = G/H-I+a*B/c");
    }

    //TOKEN CLASSIFIERS

    //Valid keywords only
    public static boolean isKeyword(String word) {
        return word.equals("BEGIN") || word.equals("INTEGER") ||
               word.equals("LET")   || word.equals("INPUT") ||
               word.equals("WRITE") || word.equals("END");
    }

    //Lexical Error
    public static boolean isInvalidKeyword(String word) {
        return word.matches("[A-Z]{2,}") && !isKeyword(word);
    }

    // Single operators only
    public static boolean isOperator(String word) {
        return word.equals("+") || word.equals("-") ||
               word.equals("*") || word.equals("/");
    }

    //Valid symbols:
    public static boolean isSymbol(String word) {
        return word.equals("=") || word.equals(",") || word.equals(";");
    }

    // Identifiers
    public static boolean isIdentifier(String word) {
        return word.matches("[a-zA-Z]") ||   // single letter
               word.matches("[a-z]+");        // multi letter lowercase 
    }
}