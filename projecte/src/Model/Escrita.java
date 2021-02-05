package Model;

/**
 * Classe Escrita que descriu un tipus de batalla amb la fórmula per puntuar.
 * -extends Batalla
 */
public class Escrita extends Batalla{

    /**
     * Constant int per saber el tipus de batalla
     */
    public static final int ESCRITA = 3;

    /**
     * Constructor de la classe Escrita
     */
    public Escrita() {
        super();
        setTipus(ESCRITA);
    }

    /**
     * Mètode que retorna i calcula la puntuació que obtè cada participant (usuari i rival) per cada estrofa
     * que diu, en funció de les rimes que han dit a cada estrofa.
     * @param estrofa String amb la estrofa corresponent per tal de contar les rimes que conté
     * @return puntuacio float amb la puntuació obtinguda pel participant ja calculada
     */
    @Override
    public float calcularPuntuacio(String estrofa){
        int rimes;
        float puntuacio;

        rimes = calcularRimes(estrofa);
        puntuacio = 1 + 3 * rimes;

        return puntuacio;
    }
}
