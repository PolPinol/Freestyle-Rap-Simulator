import Controller.Controller;
import java.io.IOException;
import java.text.ParseException;

/**
 * Classe main que executa el programa
 */
public class Main {
    /**
     * Mètode Main princial
     * @param args String[] args
     * @throws IOException error de lectura d'algun json
     * @throws ParseException error al parsejar dates
     */
    public static void main(String[] args) throws IOException, ParseException {
        Controller c = new Controller();
        c.executaPrograma();
    }
}
