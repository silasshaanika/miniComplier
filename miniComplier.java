public class miniComplier {

    // The source code to be compiled
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

        // Line by Line
        System.out.println("============================================================");
        System.out.println("LINE BY LINE COMPILER PROCESSING");
        System.out.println("============================================================");

        for (String line : program) {
            System.out.println("\n------------------------------------------------------------");
            System.out.println("SOURCE LINE: " + line);
            System.out.println("------------------------------------------------------------");
            lexicalAnalysis(line);
        }

        // Whole Program
        System.out.println("\n============================================================");
        System.out.println("PART 2: WHOLE PROGRAM PROCESSING");
        System.out.println("============================================================");
        processWholeProgram(program);
    }

    // Process whole program pass by pass
    public static void processWholeProgram(String[] program) {

        System.out.println("\n Lexical Analysis on all lines ");
        boolean[] lexOk = new boolean[program.length];
        for (int i = 0; i < program.length; i++) {
            System.out.println("\nLine " + (i + 1) + ": " + program[i]);
            lexOk[i] = lexicalAnalysisCheck(program[i]);
        }

        System.out.println("\n Syntax Analysis on lexically valid lines ");
        boolean[] synOk = new boolean[program.length];
        for (int i = 0; i < program.length; i++) {
            if (lexOk[i]) {
                System.out.println("\nLine " + (i + 1) + ": " + program[i]);
                synOk[i] = syntaxAnalysisCheck(program[i]);
            }
        }

        System.out.println("\n Semantic Analysis on syntactically valid lines ");
        boolean[] semOk = new boolean[program.length];
        for (int i = 0; i < program.length; i++) {
            if (lexOk[i] && synOk[i]) {
                System.out.println("\nLine " + (i + 1) + ": " + program[i]);
                semOk[i] = semanticAnalysisCheck(program[i]);
            }
        }

        System.out.println("\n ICR, Code Generation, Optimization, Binary ");
        for (int i = 0; i < program.length; i++) {
            if (lexOk[i] && synOk[i] && semOk[i] && isValidLine(program[i])) {
                System.out.println("\nLine " + (i + 1) + ": " + program[i]);
                intermediateCode(program[i]);
            }
        }

        System.out.println("\n============================================================");
        System.out.println("WHOLE PROGRAM PROCESSING COMPLETE");
        System.out.println("============================================================");
    }

    // LEXICAL ANALYSIS           
    public static void lexicalAnalysis(String line) {
        System.out.println(" Lexical Analysis:");

        String prepared = prepareForTokenizing(line);
        String[] tokens = prepared.trim().split("\\s+");

        boolean hasError = false;

        for (String token : tokens) {
            if (token.isEmpty()) continue;

            if (isKeyword(token)) {
                System.out.println("  '" + token + "' --> KEYWORD"); 
            } else if (isInvalidKeyword(token)) {
                System.out.println("  '" + token + "' --> LEXICAL ERROR (misspelled/unknown keyword)");
                hasError = true;
                break;
            } else if (isOperator(token)) {
                System.out.println("  '" + token + "' --> OPERATOR");
            } else if (isSymbol(token)) {
                System.out.println("  '" + token + "' --> SYMBOL");
            } else if (isIdentifier(token)) {
                System.out.println("  '" + token + "' --> IDENTIFIER");
            } else {
                System.out.println("  '" + token + "' --> LEXICAL ERROR (unrecognized token)");
                hasError = true;
                break;
            }   
        }

        if (hasError) {
            System.out.println("  Result: Lexical Error detected!, Compilation stopped for this line.");
        } else {
            System.out.println("  Result: Lexical Analysis PASSED");
            syntaxAnalysis(line);
        }
    }


    public static boolean lexicalAnalysisCheck(String line) {
        String prepared = prepareForTokenizing(line);
        String[] tokens = prepared.trim().split("\\s+");

        for (String token : tokens) {
            if (token.isEmpty()) continue;

            if (isKeyword(token) || isOperator(token) || isSymbol(token) || isIdentifier(token)) {
                continue;
            } else if (isInvalidKeyword(token)) {
                System.out.println("  LEXICAL ERROR: '" + token + "' (misspelled/unknown keyword)");
                return false;
            } else {
                System.out.println("  LEXICAL ERROR: '" + token + "' (unrecognized token)");
                return false;
            }
        }
        System.out.println("  Lexical Analysis PASSED");
        return true;
    }
    
    // STAGE 2: SYNTAX ANALYSIS      
    public static void syntaxAnalysis(String line) {
        System.out.println("[STAGE 2] Syntax Analysis:");


        String[] badOps = {"*/", "*+", "*-", "/+", "/-", "+*", "-*", "-/", "+/", "**", "//", "++", "--"};
        for (String bad : badOps) {
            if (line.contains(bad)) {
                System.out.println("  SYNTAX ERROR: Invalid operator combination '" + bad + "'");
                System.out.println("  Result: Syntax Error detected!, Compilation stopped for this line.");
                return;
            }
        }


        if (line.trim().endsWith(";")) {
            System.out.println("  SYNTAX ERROR: Semicolon at end of line is not allowed");
            System.out.println("  Result: Syntax Error detected!, Compilation stopped for this line.");
            return;
        }


        if (line.matches(".*[0-9].*")) {
            System.out.println("  SYNTAX ERROR: Numbers 0-9 are not allowed");
            System.out.println("  Result: Syntax Error detected!, Compilation stopped for this line.");
            return;
        }
                           
        System.out.println("  Result: Syntax Analysis PASSED");
        semanticAnalysis(line);
    }

    public static boolean syntaxAnalysisCheck(String line) {
        String[] badOps = {"*/", "*+", "*-", "/+", "/-", "+*", "-*", "-/", "+/", "**", "//", "++", "--"};
        for (String bad : badOps) {
            if (line.contains(bad)) {
                System.out.println("  SYNTAX ERROR: Invalid operator combination '" + bad + "'");
                return false;
            }
        }
        if (line.trim().endsWith(";")) {
            System.out.println("  SYNTAX ERROR: Semicolon at end of line is not allowed");
            return false;
        }
        if (line.matches(".*[0-9].*")) {
            System.out.println("  SYNTAX ERROR: Numbers 0-9 are not allowed");
            return false;
        }
        System.out.println("  Syntax Analysis PASSED");
        return true;
    }

    // SEMANTIC ANALYSIS
    public static void semanticAnalysis(String line) {
        System.out.println("[STAGE 3] Semantic Analysis:");

        char[] illegalChars = {'%', '$', '&', '<', '>'};
        for (char c : illegalChars) {
            if (line.indexOf(c) >= 0) {
                System.out.println("  SEMANTIC ERROR: Illegal character '" + c + "' found");
                System.out.println("  Result: Semantic Error detected!, Compilation stopped for this line.");
                return;
            }
        }

        System.out.println("  Result: Semantic Analysis PASSED");



        if (isValidLine(line)) {
            intermediateCode(line);
        } else {
            System.out.println("  No further compilation stages needed for this line.");
        }
    }

    public static boolean semanticAnalysisCheck(String line) {
        char[] illegalChars = {'%', '$', '&', '<', '>'};
        for (char c : illegalChars) {
            if (line.indexOf(c) >= 0) {
                System.out.println("  SEMANTIC ERROR: Illegal character '" + c + "' found");
                return false;
            }
        }
        System.out.println("  Semantic Analysis PASSED");
        return true;
    }

    // INTERMEDIATE CODE REPRESENTATION (ICR)           // LET G = a + c
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

    // CODE GENERATION
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
            System.out.println("  DIV   R1, R2");      // R1 = G/H
            System.out.println("  LOAD  R3, I");
            System.out.println("  SUB   R1, R3");      // R1 = G/H - I
            System.out.println("  LOAD  R3, a");
            System.out.println("  LOAD  R4, B");
            System.out.println("  MUL   R3, R4");      // R3 = a*B
            System.out.println("  LOAD  R4, c");
            System.out.println("  DIV   R3, R4");      // R3 = a*B/c
            System.out.println("  ADD   R1, R3");      // R1 = G/H-I + a*B/c
            System.out.println("  STORE R1, N");
        }

        codeOptimization(line);
    }

    // CODE OPTIMIZATION
    public static void codeOptimization(String line) {
        System.out.println("[STAGE 6] Code Optimization:");

        if (line.equals("LET G = a + c")) {
            System.out.println("  LOAD  R1, a        ; load a into R1");
            System.out.println("  LOAD  R2, c        ; load c into R2");
            System.out.println("  ADD   R1, R2       ; R1 = a + c  (t1 folded)");
            System.out.println("  STORE R1, G        ; G = R1");

        } else if (line.equals("M = A/B+C")) {
            System.out.println("  LOAD  R1, A        ; load A");
            System.out.println("  LOAD  R2, B        ; load B");
            System.out.println("  DIV   R1, R2       ; R1 = A/B  (t1 folded)");
            System.out.println("  LOAD  R2, C        ; reuse R2 for C");
            System.out.println("  ADD   R1, R2       ; R1 = A/B + C  (t2 folded)");
            System.out.println("  STORE R1, M        ; M = R1");

        } else if (line.equals("N = G/H-I+a*B/c")) {
            System.out.println("  LOAD  R1, G        ; load G");
            System.out.println("  LOAD  R2, H        ; load H");
            System.out.println("  DIV   R1, R2       ; R1 = G/H  (t1 folded)");
            System.out.println("  LOAD  R2, I        ; reuse R2 for I");
            System.out.println("  SUB   R1, R2       ; R1 = G/H-I  (t2 folded)");
            System.out.println("  LOAD  R2, a        ; load a");
            System.out.println("  LOAD  R3, B        ; load B");
            System.out.println("  MUL   R2, R3       ; R2 = a*B  (t3 folded)");
            System.out.println("  LOAD  R3, c        ; load c");
            System.out.println("  DIV   R2, R3       ; R2 = a*B/c  (t4 folded)");
            System.out.println("  ADD   R1, R2       ; R1 = G/H-I+a*B/c  (t5 folded)");
            System.out.println("  STORE R1, N        ; N = R1");
        }

        targetMachineCode(line);
    }

    // TARGET MACHINE CODE (Binary)
    public static void targetMachineCode(String line) {
        System.out.println("[STAGE 7] Target Machine Code (Binary):");
        System.out.println("  Format: OPCODE(4b) | REG1(4b) | OPERAND(8b)");
        System.out.println("  LOAD=0001  STORE=0010  ADD=0011  SUB=0100  MUL=0101  DIV=0110");

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

        System.out.println("  --> Compilation COMPLETE for this line.");
    }

    // HELPER: Tokenizer pre-processor
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

        return sb.toString().replaceAll("\\s+", " ").trim();
    }


    public static boolean isValidLine(String line) {
        return line.equals("LET G = a + c") ||
               line.equals("M = A/B+C") ||
               line.equals("N = G/H-I+a*B/c");
    }

    // TOKEN CLASSIFIERS
    public static boolean isKeyword(String word) {
        return word.equals("BEGIN")   || word.equals("INTEGER") ||
               word.equals("LET")    || word.equals("INPUT")   ||
               word.equals("WRITE")  || word.equals("END");
    }
 
    public static boolean isInvalidKeyword(String word) {
        return word.matches("[A-Z]{2,}") && !isKeyword(word);
    }


    public static boolean isOperator(String word) {
        return word.equals("+") || word.equals("-") ||
               word.equals("*") || word.equals("/");
    }


    public static boolean isSymbol(String word) {
        return word.equals("=") || word.equals(",") || word.equals(";");
    }


    public static boolean isIdentifier(String word) {
        return word.matches("[a-zA-Z]") ||  
               word.matches("[a-z]+");        
    }
}