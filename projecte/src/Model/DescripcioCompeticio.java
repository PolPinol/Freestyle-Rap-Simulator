package Model;

import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Classe que guarda la informació de la competició
 */
public class DescripcioCompeticio {
    @SerializedName("name")
    private final String nom;

    @SerializedName("startDate")
    private final String dataInici;

    @SerializedName("endDate")
    private final String dataFinal;

    @SerializedName("phases")
    private final ArrayList<Fase> fases;

    /**
     * Constructor de la classe DescripcioCompeticio
     */
    public DescripcioCompeticio(){
        this.nom = null;
        this.dataInici = null;
        this.dataFinal = null;
        this.fases = new ArrayList<>();
    }

    /**
     * Mètode getter del nom de la competició
     * @return String del nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Mètode getter de les fases
     * @return Fases de la competició
     */
    public ArrayList<Fase> getFases() {
        return fases;
    }

    /**
     * Mètode getter de la data final en format date de la competició
     * @return Date de la data final de la competició
     * @throws ParseException error al parsejar
     */
    public Date getDataFinal() throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(dataFinal);
    }

    /**
     * Mètode getter de la data inici en format Date de la competició
     * @return Date de la data inici de la competició
     * @throws ParseException error al parsejar
     */
    public Date getDataInici() throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(dataInici);
    }
}
