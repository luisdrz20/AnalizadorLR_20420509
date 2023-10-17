package Analisis;


import jflex.exceptions.SilentExit;

/**
 *
 * @author yisus
 */
public class ExecuteJFlex {

    public static void main(String omega[]) {
        String lexerFile = System.getProperty("user.dir") + "/src/Analisis/Lexer.flex";
        try {
            jflex.Main.generate(new String[]{lexerFile});
        } catch (SilentExit ex) {
            System.out.println("Error al compilar/generar el archivo flex: " + ex);
        }
    }
}
