package Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Classe que conte la informacio del pais
 */
public class InformacioPais {
    private transient String nom;

    @SerializedName("languages")
    private ArrayList<Idioma> idiomes;

    @SerializedName("flag")
    private String bandera;

    /**
     * Constructor de la clase informacioPais
     */
    public InformacioPais(){
        this.nom = null;
        this.idiomes = new ArrayList<>();
        this.bandera = null;
    }

    /**
     * Mètode getter de la bandera
     * @return string de la bandera
     */
    public String getBandera() {
        return bandera;
    }

    /**
     * Mètode getter de idiome
     * @return string de idiomes
     */
    public ArrayList<Idioma> getIdiomes() {
        return idiomes;
    }
}