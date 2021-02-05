package Model;

import com.google.gson.annotations.SerializedName;

/**
 * Classe que conté la informacio de l'idioma
 */
public class Idioma {
    @SerializedName("iso639_1")
    private String nomAbreujat1;

    @SerializedName("iso639_2")
    private String nomAbreujat2;

    @SerializedName("nativeName")
    private String nomNatiu;

    @SerializedName("name")
    private String nomAngles;

    /**
     * Constructor de la classe Idioma
     */
    public Idioma(){
        this.nomAngles = null;
        this.nomNatiu = null;
        this.nomAbreujat1 = null;
        this.nomAbreujat2 = null;
    }

    /**
     * Mètode getter del Nom en Angles
     * @return nomAngles String del nom en angles del idioma
     */
    public String getNomAngles() {
        return nomAngles;
    }

}