package Model;

/**
 * Classe Sangre que descriu un tipus de batalla amb la fórmula per puntuar.
 * -extends Batalla
 */
public class Sangre extends Batalla{

    /**
     * Constant int per saber el tipus de batalla
     */
    public static final int SANGRE = 2;

    /**
     * Constructor de la classe Sangre
     */
    public Sangre() {
        super();
        setTipus(SANGRE);
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
        puntuacio = (float) Math.PI * rimes * rimes / 4;

        return puntuacio;
    }

}
