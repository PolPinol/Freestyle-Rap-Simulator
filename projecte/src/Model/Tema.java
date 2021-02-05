package Model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

/**
 * Clase que conté els temes amb les seves estrofes
 */
public class Tema {
    @SerializedName("name")
    private final String nom;

    @SerializedName("rhymes")
    private ArrayList<Estrofa> estrofes;

    /**
     * Constructor de la classe model.Tema
     */
    public Tema(){
        this.nom = "";
        this.estrofes = new ArrayList<>();
    }

    /**
     * Mètode getter de les estrofes
     * @return arrayList de les estrofes
     */
    public ArrayList<Estrofa> getEstrofes() {
        return estrofes;
    }

    /**
     * Mètode getter del nom del tema
     * @return String del nom del tema
     */
    public String getNom() {
        return nom;
    }
}
