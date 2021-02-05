package Model;

import com.google.gson.annotations.SerializedName;


/**
 * Classe que descriu les fases de la competició
 */
public class Fase {

    /**
     * Constant int per saber el tipus de batalla
     */
    public static final int A_CAPELLA = 1;

    /**
     * Constant int per saber el tipus de batalla
     */
    public static final int SANGRE = 2;

    @SerializedName("budget")
    private float pressupost;

    @SerializedName("country")
    private String paisCelebrat;

    private transient Batalla[] batalles;

    /**
     * Constructor de la classe Fase
     */
    public Fase(){
        this.pressupost = 0;
        this.paisCelebrat = null;
        this.batalles = new Batalla[2];
    }

    /**
     * Mètode getter de les dues batalles
     * @return array de dues batalles
     */
    public Batalla[] getBatalles() {
        return batalles;
    }

    /**
     * Mètode setter de cada batalla
     * @param indexBatalla enter que descriu el index del array batalles
     */
    public void setBatalla(int indexBatalla) {
        Batalla batalla;

        int randomTipus = (int) Math.floor(Math.random()*3+1);

        if (randomTipus == SANGRE) {
            batalla = new Sangre();
        } else if (randomTipus == A_CAPELLA) {
            batalla = new Acapella();
        } else {
            batalla = new Escrita();
        }

        batalles[indexBatalla] = batalla;
    }

}
