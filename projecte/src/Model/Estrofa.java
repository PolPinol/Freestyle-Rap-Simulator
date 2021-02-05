package Model;

import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;

/**
 * Classe que descriu el contingut d'una estrofa
 */
public class Estrofa {
    @SerializedName("1")
    private final ArrayList<String> first;

    @SerializedName("2")
    private final ArrayList<String> second;

    /**
     * Constructor de la classe Estrofa
     */
    public Estrofa(){
        this.first = new ArrayList<>();
        this.second = new ArrayList<>();
    }

    /**
     * Mètode getter del arrayList de estrofes de nivell d'experiència dos.
     * @return arrayList de estrofes de nivell d'experiència dos
     */
    public ArrayList<String> getSecond() {
        return second;
    }

    /**
     * Mètode getter del arrayList de estrofes de nivell d'experiència un.
     * @return arrayList de estrofes de nivell d'experiència un
     */
    public ArrayList<String> getFirst() {
        return first;
    }
}
