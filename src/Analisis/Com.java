package Analisis;

import compilerTools.Directory;
import compilerTools.Functions;
import compilerTools.Token;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class Com extends javax.swing.JFrame {

    private ArrayList<Token> tokens;
    int valorAnterior = 1;
    int variable = 1;
    int renglones = 0, columnas = 0, penultimo = 0;
    java.util.Stack<String> pila = new java.util.Stack<String>();
    Boolean banP = true; //Bandera para ver si ya se inicio la Pila
    private DefaultTableModel modeloTabla;
    private Directory directorio;
    NumeroLinea numeroLinea;
    private String title;
    private Timer timerKeyReleased;

    public Com() {
        initComponents();
        colors();
        tokens = new ArrayList<>();
        Inicial();
    }
    
    
     private void Inicial() {

        title = "Compiler";

        // Coloca la ventana en el centro de la pantalla.
        setLocationRelativeTo(null);

        //Numero de linea
        numeroLinea = new NumeroLinea(escritura);
        jScrollPane1.setRowHeaderView(numeroLinea);

        // Crea un nuevo objeto Directory.
        // "this" se refiere al objeto actual, "escritura" es una referencia a algún tipo de componente de texto, "title" es el título y ".ldas"  la extensión de un tipo de archivo.
        directorio = new Directory(this, escritura, title, ".ldas");

        // Agrega un escuchador de eventos a la ventana. Este escuchador responde a cuando la ventana se está cerrando.
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Antes de que la ventana se cierre, se llama a la función Exit() del objeto directorio.
                directorio.Exit();
                // Después se cierra la aplicación.
                System.exit(0);
            }
        });

        // Se llama a una función que parece establecer un número de línea en el componente de texto "escritura".
        //Functions.setLineNumberOnJTextComponent(escritura);
        // Inicializa un nuevo temporizador que se detiene después de 0.3 segundos.
        timerKeyReleased = new Timer((int) (1000 * 0.3), (ActionEvent e) -> {
            timerKeyReleased.stop();
        });

        // Llama a una función que parece insertar un asterisco en el nombre. 
        // La función también reinicia el temporizador cuando se libera una tecla.
        Functions.insertAsteriskInName(this, escritura, () -> {
            timerKeyReleased.restart();
        });

        // Configura un componente de texto "escritura" para autocompletar basado en un conjunto de cadenas vacío.
        // La función también reinicia el temporizador cuando se libera una tecla.
    }
     String Produciones[][] = {
        /*P0*/{"P'", "P"},
        /*P1*/ {"P", "Tipo id V"},
        /*P2*/ {"P", "A"},
        /*P3*/ {"Tipo", "int"},
        /*P4*/ {"Tipo", "float"},
        /*P5*/ {"Tipo", "char"},
        /*P6*/ {"V", ", id V"},
        /*P7*/ {"V", "; P"},
        /*P8*/ {"A", "id = S ;"},
        /*P9*/ {"S", "+ E"},
        /*P10*/ {"S", "- E"},
        /*P11*/ {"S", "E"},
        /*P12*/ {"E", "E + T"},
        /*P13*/ {"E", "E - T"},
        /*P14*/ {"E", "T"},
        /*P15*/ {"T", "T * F"},
        /*P16*/ {"T", "T / F"},
        /*P17*/ {"T", "F"},
        /*P18*/ {"F", "( E )"},
        /*P19*/ {"F", "id"},
        /*P20*/ {"F", "num"},};

    String encabezadosRenglones[] = {"I0", "I1", "I2", "I3", "I4", "I5", "I6", "I7", "I8", "I9", "I10", "I11", "I12", "I13", "I14", "I15", "I16", "I17", "I18", "I19", "I20", "I21", "I22", "I23", "I24", "I25", "I26", "I27", "I28", "I29", "I30", "I31", "I32", "I33", "I34", "I35", "I36", "I37"};
    String encabezadosColumnas[] = {"id", "num", "int", "float", "char", ",", ";", "+", "-", "*", "/", "(", ")", "=", "$", "P", "Tipo", "V", "A", "S", "E", "T", "F"};
  String matriz[][] = {
        //    "id" 0,         "num" 1,   "int" 2,    "float" 3,  "char" 4,   "," 5,      ";" 6,      "+" 7,          "-" 8,          "*" 9,          "/" 10,     "(" 11,     ")" 12,     "=" 13,     "$" 14,         "P" 15,     "Tipo" 16,    "V" 17,     "A" 18,      "S" 19,     "E" 20,     "T" 21,     "F" 22
        /*I0*/  {"I7",        "0",       "I4",       "I5",       "I6",       "0",        "0",        "0",            "0",            "0",            "0",        "0",        "0",        "0",        "0",            "I1",        "I2",        "0",        "I3",        "0",        "0",        "0",        "0"},
        /*I1*/  {"0",         "0",       "0",        "0",        "0",        "0",        "0",        "0",            "0",            "0",            "0",        "0",        "0",        "0",        "ACEPTA",       "0",         "0",         "0",        "0",         "0",        "0",        "0",        "0"},
        /*I2*/  {"I8",        "0",       "0",        "0",        "0",        "0",        "0",        "0",            "0",            "0",            "0",        "0",        "0",        "0",        "0",            "0",         "0",         "0",        "0",         "0",        "0",        "0",        "0"},
        /*I3*/  {"0",         "0",       "0",        "0",        "0",        "0",        "0",        "0",            "0",            "0",            "0",        "0",        "0",        "0",        "P2",           "0",         "0",         "0",        "0",         "0",        "0",        "0",        "0"},
        /*I4*/  {"P3",        "0",       "0",        "0",        "0",        "0",        "0",        "0",            "0",            "0",            "0",        "0",        "0",        "0",        "0",            "0",         "0",         "0",        "0",         "0",        "0",        "0",        "0"},
        /*I5*/  {"P4",        "0",       "0",        "0",        "0",        "0",        "0",        "0",            "0",            "0",            "0",        "0",        "0",        "0",        "0",            "0",         "0",         "0",        "0",         "0",        "0",        "0",        "0"},
        /*I6*/  {"P5",        "0",       "0",        "0",        "0",        "0",        "0",        "0",            "0",            "0",            "0",        "0",        "0",        "0",        "0",            "0",         "0",         "0",        "0",         "0",        "0",        "0",        "0"},
        /*I7*/  {"0",         "0",       "0",        "0",        "0",        "0",        "0",        "0",            "0",            "0",            "0",        "0",        "0",        "I9",       "0",            "0",         "0",         "0",        "0",         "0",        "0",        "0",        "0"},
        /*I8*/  {"0",         "0",       "0",        "0",        "0",        "I11",      "I12",      "0",            "0",            "0",            "0",        "0",        "0",        "0",        "0",            "0",         "0",         "I10",      "0",         "0",        "0",        "0",        "0"},
        /*I9*/  {"I20",       "I21",     "0",        "0",        "0",        "0",        "0",        "I14",          "I15",          "0",            "0",        "I19",      "0",        "0",        "0",            "0",         "0",         "0",        "0",         "I13",      "I16",      "I17",      "I18"},
        /*I10*/ {"0",         "0",       "0",        "0",        "0",        "0",        "0",        "0",            "0",            "0",            "0",        "0",        "0",        "0",        "P1",           "0",         "0",         "0",        "0",         "0",        "0",        "0",        "0"},
        /*I11*/ {"I22",       "0",       "0",        "0",        "0",        "0",        "0",        "0",            "0",            "0",            "0",        "0",        "0",        "0",        "0",            "0",         "0",         "0",        "0",         "0",        "0",        "0",        "0"},
        /*I12*/ {"I7",        "0",       "I4",       "I5",       "I6",       "0",        "0",        "0",            "0",            "0",            "0",        "0",        "0",        "0",        "0",            "I23",       "I2",        "0",        "I3",        "0",        "0",        "0",        "0"},
        /*I13*/ {"0",         "0",       "0",        "0",        "0",        "0",        "I24",      "0",            "0",            "0",            "0",        "0",        "0",        "0",        "0",            "0",         "0",         "0",        "0",         "0",        "0",        "0",        "0"},
        /*I14*/ {"I20",       "I21",     "0",        "0",        "0",        "0",        "0",        "0",            "0",            "0",            "0",        "I19",      "0",        "0",        "0",            "0",         "0",         "0",        "0",         "0",        "I25",      "I17",      "I18"},
        /*I15*/ {"I20",       "I21",     "0",        "0",        "0",        "0",        "0",        "0",            "0",            "0",            "0",        "I19",      "0",        "0",        "0",            "0",         "0",         "0",        "0",         "0",        "I26",      "I17",      "I18"},
        /*I16*/ {"0",         "0",       "0",        "0",        "0",        "0",        "P11",      "I27",          "I28",          "0",            "0",        "0",        "0",        "0",        "0",            "0",         "0",         "0",        "0",         "0",        "0",        "0",        "0"},
        /*I17*/ {"0",         "0",       "0",        "0",        "0",        "0",        "P14",      "P14",          "P14",          "I29",          "I30",      "0",        "P14",      "0",        "0",            "0",         "0",         "0",        "0",         "0",        "0",        "0",        "0"},
        /*I18*/ {"0",         "0",       "0",        "0",        "0",        "0",        "P17",      "P17",          "P17",          "P17",          "P17",      "0",        "P17",      "0",        "0",            "0",         "0",         "0",        "0",         "0",        "0",        "0",        "0"},
        /*I19*/ {"I20",       "I21",     "0",        "0",        "0",        "0",        "0",        "0",            "0",            "0",            "0",        "I19",      "0",        "0",        "0",            "0",         "0",         "0",        "0",         "0",        "I31",      "I17",     "I18"},
        /*I20*/ {"0",         "0",       "0",        "0",        "0",        "0",        "P19",      "P19",          "P19",          "P19",          "P19",      "0",        "P19",      "0",        "0",            "0",         "0",         "0",        "0",         "0",        "0",        "0",        "0"},
        /*I21*/ {"0",         "0",       "0",        "0",        "0",        "0",        "P20",      "P20",          "P20",          "P20",          "P20",      "0",        "P20",      "0",        "0",            "0",         "0",         "0",        "0",         "0",        "0",        "0",        "0"},
        /*I22*/ {"0",         "0",       "0",        "0",        "0",        "I11",      "I12",      "0",            "0",            "0",            "0",        "0",        "0",        "0",        "0",            "0",         "0",         "I32",      "0",         "0",        "0",        "0",        "0"},
        /*I23*/ {"0",         "0",       "0",        "0",        "0",        "0",        "0",        "0",            "0",            "0",            "0",        "0",        "0",        "0",        "P7",           "0",         "0",         "0",        "0",         "0",        "0",        "0",        "0"},
        /*I24*/ {"0",         "0",       "0",        "0",        "0",        "0",        "0",        "0",            "0",            "0",            "0",        "0",        "0",        "0",        "P8",           "0",         "0",         "0",        "0",         "0",        "0",        "0",        "0"},
        /*I25*/ {"0",         "0",       "0",        "0",        "0",        "0",        "P9",       "I27",          "I28",          "0",            "0",        "0",        "0",        "0",        "0",            "0",         "0",         "0",        "0",         "0",        "0",        "0",        "0"},
        /*I26*/ {"0",         "0",       "0",        "0",        "0",        "0",        "P10",      "I27",          "I28",          "0",            "0",        "0",        "0",        "0",        "0",            "0",         "0",         "0",        "0",         "0",        "0",        "0",        "0"},
        /*I27*/ {"I20",       "I21",     "0",        "0",        "0",        "0",        "0",        "0",            "0",            "0",            "0",        "I19",      "0",        "0",        "0",            "0",         "0",         "0",        "0",         "0",        "0",        "I33",      "I18"},
        /*I28*/ {"I20",       "I21",     "0",        "0",        "0",        "0",        "0",        "0",            "0",            "0",            "0",        "I19",      "0",        "0",        "0",            "0",         "0",         "0",        "0",         "0",        "0",        "I34",      "I18"},
        /*I29*/ {"I20",       "I21",     "0",        "0",        "0",        "0",        "0",        "0",            "0",            "0",            "0",        "I19",      "0",        "0",        "0",            "0",         "0",         "0",        "0",         "0",        "0",        "0",        "I35"},
        /*I30*/ {"I20",       "I21",     "0",        "0",        "0",        "0",        "0",        "0",            "0",            "0",            "0",        "I19",      "0",        "0",        "0",            "0",         "0",         "0",        "0",         "0",        "0",        "0",        "I36"},
        /*I31*/ {"0",         "0",       "0",        "0",        "0",        "0",        "0",        "I27",          "I28",          "0",            "0",        "0",        "I37",      "0",        "0",            "0",         "0",         "0",        "0",         "0",        "0",        "0",        "0"},
        /*I32*/ {"0",         "0",       "0",        "0",        "0",        "0",        "0",        "0",            "0",            "0",            "0",        "0",        "0",        "0",        "P6",           "0",         "0",         "0",        "0",         "0",        "0",        "0",        "0"},
        /*I33*/ {"0",         "0",       "0",        "0",        "0",        "0",        "P12",      "P12",          "P12",          "I29",          "I30",      "0",        "P12",      "0",        "0",            "0",         "0",         "0",        "0",         "0",        "0",        "0",        "0"},
        /*I34*/ {"0",         "0",       "0",        "0",        "0",        "0",        "P13",      "P13",          "P13",          "I29",          "I30",      "0",        "P13",      "0",        "0",            "0",         "0",         "0",        "0",         "0",        "0",        "0",        "0"},
        /*I35*/ {"0",         "0",       "0",        "0",        "0",        "0",        "P15",      "P15",          "P15",          "P15",          "P15",      "0",        "P15",      "0",        "0",            "0",         "0",         "0",        "0",         "0",        "0",        "0",        "0"},
        /*I36*/ {"0",         "0",       "0",        "0",        "0",        "0",        "P16",      "P16",          "P16",          "P16",          "P16",      "0",        "P16",      "0",        "0",            "0",         "0",         "0",        "0",         "0",        "0",        "0",        "0"},
        /*I37*/ {"0",         "0",       "0",        "0",        "0",        "0",        "P18",      "P18",          "P18",          "P18",          "P18",      "0",        "P18",      "0",        "0",            "0",         "0",         "0",        "0",         "0",        "0",        "0",        "0"}};
          
    
    //-------------------------------------------------------------------------------------Analizador Lexico--------------------------------------------------------------------------------------------
    private void analisisLexico() {
        Lexer lexer = null;// Creamos un objeto lexer
        try {
            File sourceCodeFile = new File("code.encrypter");// Creamos un nuevo archivo llamado "code.encrypter" 

            FileOutputStream fileOutputStream = new FileOutputStream(sourceCodeFile);// Creamos un flujo de salida para escribir datos al archivo
            byte[] bytesOfText = escritura.getText().getBytes();// Obtenemos el texto de la variable escritura y lo convertimos a bytes      

            fileOutputStream.write(bytesOfText); // Escribimos los bytes al archivo

            BufferedReader fileInputReader = new BufferedReader(new InputStreamReader(new FileInputStream(sourceCodeFile), "UTF8"));// Creamos un flujo de entrada para leer datos del archivo,

            lexer = new Lexer(fileInputReader);// Inicializamos el lexer con el contenido del archivo

            while (true) { // Iteramos a través de cada token producido por el lexer hasta que no haya más
                Token token = lexer.yylex();
                if (token == null) {
                    break;
                }
                // Añadimos cada token a la lista de tokens
                tokens.add(token);
            }
        } catch (FileNotFoundException ex) {
            // En caso de que el archivo no pueda ser encontrado, se muestra un mensaje de error
            System.out.println("El archivo no pudo ser encontrado... " + ex.getMessage());
        } catch (IOException ex) {
            // En caso de un error de entrada/salida, se muestra un mensaje de error
            System.out.println("Error al escribir en el archivo... " + ex.getMessage());
        }
    }

    private void analisisLexicoEerr() {

        // Recorrer todos los tokens
        for (int i = 0; i < tokens.size(); i++) {

            // Si el token es de tipo ERROR
            if ("ERROR".equals(tokens.get(i).getLexicalComp())) {

                // En función del tipo de error, añadir el mensaje correspondiente
                String errorMsg = "Error Lexico linea " + tokens.get(i).getLine() + ": token [ " + tokens.get(i).getLexeme() + " ] ";
                if (tokens.get(i).getLexeme().matches("pAto")) {
                    errorMsg += "se esperaba comilla doble de cierre ";

                } else {
                    errorMsg += "no es valido ";

                }

                // Añadir la línea y columna del error
                errorMsg += "[" + tokens.get(i).getLine() + ", " + tokens.get(i).getColumn() + "]\n";

                // Actualizar el contenido del campo mensajes
                mensajes.setText(mensajes.getText() + errorMsg);
            }

        }
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------ANALIZADOR SINTACTICO-------------------------------------------------------------------------------------------
    public void AnalisisSintacticoInicioPila() {

        // Si la bandera 'ban' es verdadera.
        if (banP) {

            pila.push("$"); // Coloca en la pila el símbolo de fin de archivo o de fin de entrada "$".          
            pila.push("IO"); // Añade a la pila el símbolo no terminal "prog".
            // banP = false;  // Asigna a la bandera 'banP' el valor de false.
            // Imprime en consola el contenido de la pila.
            // Crear una nueva tabla para mostrar la pila y las acciones
            modeloTabla = new DefaultTableModel();
            modeloTabla.addColumn("Entrada");
            modeloTabla.addColumn("Pila");
            modeloTabla.addColumn("Accion");
            jTable1.setModel(modeloTabla);

            System.out.println(pila);

        }

    }
   

    public void AnalisisSintactico(String token, int linea, String caracter) {

        String accion;
        accion = matriz[Renglon()][Columna(token)];
        System.out.println(accion);
        penultimo = 0;

        if (accion.startsWith("I")) {
            modeloTabla.addRow(new Object[]{token, pila.toString(), "[" + pila.peek() + "," + token + "]  " + " Desplaza " + token + " " + accion});
            pila.push(token);
            pila.push(accion);

            System.out.println(pila);

        } else if (accion.startsWith("P")) {

            int NumeroProduccion = obtenerUltimoDigitoEntero(accion);
            System.out.println(NumeroProduccion);
            String produce = Produciones[NumeroProduccion][1];
            System.out.println(produce);

            modeloTabla.addRow(new Object[]{token, pila.toString(), "[" + pila.peek() + "," + token + "]  " + accion + "  " + Produciones[NumeroProduccion][0] + " --> " + Produciones[NumeroProduccion][1]});

            while (!pila.isEmpty()) {
                String elemento = pila.pop();
                if (elemento.equals(obtenerParteAntesDelEspacio(Produciones[NumeroProduccion][1]))) {
                    break; // Detener cuando se encuentra "float"
                }
            }

            pila.push(Produciones[NumeroProduccion][0]);

            String elementoPenultimo = pila.get(pila.size() - 2);
           

            accion = matriz[penultimo()][Columna(pila.peek())];
            System.out.println("{" + (penultimo()) + "-" + (Columna(pila.peek())) + "}");
            System.out.println(pila);
            pila.push(accion);
            System.out.println("{" + (penultimo()) + "-" + (Columna(pila.peek())) + "}");
            System.out.println(pila);
            this.AnalisisSintactico(token, linea, caracter);//Sigue con el mismo toke  
        } else if (accion=="ACEPTA") {
            
             
            mensajes.setText(mensajes.getText() + "Se acepta");
            modeloTabla.addRow(new Object[]{token, pila.toString(),  accion });
            mensajes.setText(mensajes.getText() + "\nAnalisis terminado Correctamente...");
            
        }else if (accion=="0") {
        
             
             mensajes.setText(mensajes.getText() + "ERROR linea "+ linea +". Se encontro un token inesperado :"+token+"\n");
            
     
        }
        

    }

    public static String obtenerParteAntesDelEspacio(String cadena) {
        // Dividir la cadena en palabras utilizando el espacio como separador
        String[] palabras = cadena.split(" ");

        // Si hay al menos una palabra, devuelve la primera
        if (palabras.length > 0) {
            return palabras[0];
        } else {
            // Si no hay espacios, devuelve la cadena completa
            return cadena;
        }
    }

    public static int obtenerUltimoDigitoEntero(String cadena) {
        String digitos = "";

        for (int i = 0; i < cadena.length(); i++) {
            char caracter = cadena.charAt(i);
            if (Character.isDigit(caracter)) {
                digitos += caracter;
            }
        }

        if (digitos.isEmpty()) {
            throw new IllegalArgumentException("La cadena no contiene dígitos.");
        }

        return Integer.parseInt(digitos);
    }

////////////////////////////////POSICIONES E INDICES////////////////////////////////////////////////////////////////////////////////
    //Metodo que busca un elemento dentro del encabezadosRenglones y devuelve el indice o posicion donde lo encontro
    public int Renglon() {
        for (int i = 0; i < encabezadosRenglones.length; i++) {
            if (encabezadosRenglones[i].equals(pila.peek())) {
                renglones = i;
                break;
            }
        }
        return renglones;
    }

    public int penultimo() {
        for (int i = 0; i < encabezadosRenglones.length; i++) {
            if (encabezadosRenglones[i].equals(pila.get(pila.size() - 2))) {
                penultimo = i;
                break;
            }
        }
        return penultimo;
    }
    //Metodo que busca la posicion del token dentro de encabezadosColumnas y devuelve su posicion

    public int Columna(String token) {
        for (int i = 0; i < encabezadosColumnas.length; i++) {
            if (token.equals(encabezadosColumnas[i])) {
                columnas = i;
                break;
            }
        }
        return columnas;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void llenarPila() {
        tokens.forEach(token -> {

            variable = token.getLine();

            //Imprimir el token en mi JTextpanel de mi analizador lexico 
            if (token.getLexicalComp() == "tipoDa") {

                AnalisisSintactico(token.getLexeme(), token.getLine(), token.getLexeme());
            } else if (token.getLexicalComp() == "ERROR") {
                //AnalisisSintactico(token.getLexicalComp(), token.getLine(),token.getLexeme());
            } else {

                AnalisisSintactico(token.getLexicalComp(), token.getLine(), token.getLexeme());
            }
           
           
            
            
        }); 
        
        AnalisisSintactico( "$" , 1, "");

        
    }

    private void llenarLexicoDeRokens() {
        tokens.forEach(token -> {

            variable = token.getLine();

            //////////////solucion rapida para imprimir el salto de linea casda que se encuentre cambio en la linea 
            if (variable != valorAnterior) {
                lexico.setText(lexico.getText() + "\n");// Imprimir salto de línea
                valorAnterior = token.getLine();
            }
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////

            if (token.getLexicalComp() == "tipoDa") {

                lexico.setText(lexico.getText() + " " + token.getLexeme());
            } else if (token.getLexicalComp() == "ERROR") {
                lexico.setText(lexico.getText() + " ");
            } else {

                lexico.setText(lexico.getText() + " " + token.getLexicalComp());
            }

        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bGuardar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        escritura = new javax.swing.JTextPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        lexico = new javax.swing.JTextPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        mensajes = new javax.swing.JTextPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        bNuevo = new javax.swing.JButton();
        bAbrir = new javax.swing.JButton();
        bRun1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        bGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/disquete (1).png"))); // NOI18N
        bGuardar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        bGuardar.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/img/salvar (1).png"))); // NOI18N
        bGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bGuardarActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(escritura);

        jScrollPane2.setViewportView(lexico);

        jScrollPane4.setViewportView(mensajes);

        jTabbedPane1.addTab("ERRORES", jScrollPane4);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane5.setViewportView(jTable1);

        jTabbedPane1.addTab("PILA", jScrollPane5);

        bNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/agregar-archivo.png"))); // NOI18N
        bNuevo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        bNuevo.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/img/agregar-archivo (1).png"))); // NOI18N
        bNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bNuevoActionPerformed(evt);
            }
        });

        bAbrir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/caja (1).png"))); // NOI18N
        bAbrir.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        bAbrir.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/img/caja (2).png"))); // NOI18N
        bAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAbrirActionPerformed(evt);
            }
        });

        bRun1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/boton-de-play.png"))); // NOI18N
        bRun1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        bRun1.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/img/jugar (1).png"))); // NOI18N
        bRun1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRun1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(bNuevo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bAbrir)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bGuardar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bRun1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 487, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTabbedPane1))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bAbrir)
                    .addComponent(bNuevo)
                    .addComponent(bRun1)
                    .addComponent(bGuardar))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void bNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bNuevoActionPerformed
        directorio.New();
        borrarComponentes();// TODO add your handling code here:
    }//GEN-LAST:event_bNuevoActionPerformed

    private void bAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAbrirActionPerformed
        if (directorio.Open()) {

            borrarComponentes();
        }
    }//GEN-LAST:event_bAbrirActionPerformed

    private void bRun1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRun1ActionPerformed
          pila.clear();
        renglones = 0;
        columnas = 0;
        penultimo = 0;

        if (escritura.getText().isEmpty()) {
            borrarComponentes();
            mensajes.setText("No hay nada que analizar");

        } else {
            borrarComponentes();
            analisisLexico();
            llenarLexicoDeRokens();
            analisisLexicoEerr();
            lexico.setText(lexico.getText());
            AnalisisSintacticoInicioPila();
            llenarPila();
        }
    }//GEN-LAST:event_bRun1ActionPerformed

    private void bGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bGuardarActionPerformed
        if (directorio.Save()) {
            borrarComponentes();
        }
    }//GEN-LAST:event_bGuardarActionPerformed

    private void borrarComponentes() {
        tokens.clear();
        lexico.setText("");
        mensajes.setText("");
        tokens.clear();
        valorAnterior = 1;

    }
    
    
    
      // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // COLOREAR LAS COSAS
    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //METODO PARA ENCONTRAR LAS ULTIMAS CADENAS
    private int findLastNonWordChar(String text, int index) {
        while (--index >= 0) {
            //  \\W = [A-Za-Z0-9]
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
        }
        return index;
    }

    //METODO PARA ENCONTRAR LAS PRIMERAS CADENAS 
    private int findFirstNonWordChar(String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
            index++;
        }
        return index;
    }   
 //METODO PARA PINTAS LAS PALABRAS RESEVADAS
    private void colors() {

        final StyleContext cont = StyleContext.getDefaultStyleContext();

        //COLORES 
        final AttributeSet attred = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(225, 123, 13));//naranja
        final AttributeSet attgreen = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(48, 101, 59)); //verde
        final AttributeSet attblue = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(0, 0, 0)); //Blanco
        final AttributeSet attblack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(145, 55, 76));
        final AttributeSet blanco = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, new Color(0, 0, 0)); //Blanco
        //STYLO 
        DefaultStyledDocument doc = new DefaultStyledDocument() {
            public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
                super.insertString(offset, str, a);

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offset);
                if (before < 0) {
                    before = 0;
                }
                int after = findFirstNonWordChar(text, offset + str.length());
                int wordL = before;
                int wordR = before;

                while (wordR <= after) {
                    if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
                        if (text.substring(wordL, wordR).matches("(\\W)*()")) { //Azul 
                            setCharacterAttributes(wordL, wordR - wordL, attblue, false);
                        } else if (text.substring(wordL, wordR).matches("(\\W)*(int|float|char)")) { //verde
                            setCharacterAttributes(wordL, wordR - wordL, attgreen, false);
                        } else if (text.substring(wordL, wordR).matches("(\\W)*(1|2|3|4|5|6|7|8|9|0)")){ // blanco
                            setCharacterAttributes(wordL, wordR - wordL, attred, false);
                        } 
                         else if (text.substring(wordL, wordR).matches("(\\W)*($)")) { // blanco
                            setCharacterAttributes(wordL, wordR - wordL, blanco, false);
                        
                         }else {
                            setCharacterAttributes(wordL, wordR - wordL, attblack, false);
                        }
                        wordL = wordR;

                    }
                    wordR++;
                }
            }

            public void romeve(int offs, int len) throws BadLocationException {
                super.remove(offs, len);

                String text = getText(0, getLength());
                int before = findLastNonWordChar(text, offs);
                if (before < 0) {
                    before = 0;
                }
            }
        };

        JTextPane txt = new JTextPane(doc);
        String temp = escritura.getText();
        escritura.setStyledDocument(txt.getStyledDocument());
        escritura.setText(temp);

    }

    // ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Com.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Com.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Com.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Com.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Com().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bAbrir;
    private javax.swing.JButton bGuardar;
    private javax.swing.JButton bNuevo;
    private javax.swing.JButton bRun1;
    private javax.swing.JTextPane escritura;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextPane lexico;
    private javax.swing.JTextPane mensajes;
    // End of variables declaration//GEN-END:variables
}
