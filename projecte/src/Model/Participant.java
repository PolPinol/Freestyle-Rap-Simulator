package Model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import edu.salleurl.profile.Profile;
import edu.salleurl.profile.ProfileFactory;
import edu.salleurl.profile.Profileable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 * Classe que conté tota la informacio dels participants
 * - implements Comparable, Profileable
 */
public class Participant implements Comparable<Participant>, Profileable {
    @SerializedName("realName")
    private String nomComplet;

    @SerializedName("stageName")
    private String nomArtistic;

    @SerializedName("birth")
    private String dataDeNaixement;

    @SerializedName("nationality")
    private String paisOrigen;

    @SerializedName("level")
    private int nivellExpertesa;

    @SerializedName("photo")
    private String URLfoto;

    private transient ArrayList<InformacioPais> informacioPaisOrigen;

    private transient float puntuacio;

    /**
     * Constant int per saber si un participant ha estat eliminat
     */
    public static final int ELIMINAT = -1;

    /**
     * Constructor de la classe Participant (sense entrar dades)
     */
    public Participant(){
        this.nomComplet = null;
        this.nomArtistic = null;
        this.dataDeNaixement = null;
        this.paisOrigen = null;
        this.nivellExpertesa = 1;
        this.URLfoto = null;
        this.puntuacio = 0;
        this.informacioPaisOrigen = new ArrayList<>();
    }

    /**
     * Constructor de la classe Participant
     * @param nomComplet String nom complet del participant
     * @param nomArtistic String nom artistic del participant
     * @param dataDeNaixement String amb la data de naixement
     * @param paisOrigen String amb el pais d'origen
     * @param nivellExpertesa enter amb el nivell d'expertesa
     * @param URLfoto String amb la url de la foto
     */
    public Participant(String nomComplet, String nomArtistic, String dataDeNaixement, String paisOrigen,
                       int nivellExpertesa, String URLfoto){
        this.nomComplet = nomComplet;
        this.nomArtistic = nomArtistic;
        this.dataDeNaixement = dataDeNaixement;
        this.paisOrigen = paisOrigen;
        this.nivellExpertesa = nivellExpertesa;
        this.URLfoto = URLfoto;
        this.puntuacio = 0;
        this.informacioPaisOrigen = new ArrayList<>();
    }

    /**
     * Mètode getter del pais d'origen
     * @return string del pais d'origen
     */
    public String getPaisOrigen() {
        return paisOrigen;
    }

    /**
     * Mètode getter de la puntuació.
     * @return decimal de la puntuació
     */
    public float getPuntuacio() {
        return puntuacio;
    }

    /**
     * Mètode getter del nivell d'expertesa.
     * @return enter del nivell d'expertesa
     */
    public int getNivellExpertesa() {
        return nivellExpertesa;
    }

    /**
     * Mètode setter de la puntuació.
     * @param puntuacio enter de la puntuació
     */
    public void setPuntuacio(float puntuacio) {
        this.puntuacio = puntuacio;
    }

    /**
     * Mètode que compara la puntuacio de dos participants
     * @param o: model.Participant amb qui es compara la puntuació
     */
    @Override
    public int compareTo(Participant o) {
        if (o.getPuntuacio() - this.puntuacio > 0) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * Mètode getter del nom Complet del rapero
     * @return string del nom Complet del rapero
     */
    @Override
    public String getName() {
        return nomComplet;
    }

    /**
     * Mètode getter del nom Artistic del rapero
     * @return string del nom Artistic del rapero
     */
    @Override
    public String getNickname() {
        return nomArtistic;
    }

    /**
     * Mètode getter de la data de naixement del rapero
     * @return string de la data de naixement del rapero
     */
    @Override
    public String getBirthdate() {
        return dataDeNaixement;
    }

    /**
     * Mètode getter del url de la foto
     * @return string del url de la foto
     */
    @Override
    public String getPictureUrl() {
        return URLfoto;
    }

    /**
     * Mètode getter del nom Complet del rapero
     * @param posicio: enter que indica la posició del ranking en la que es troba el rapero
     * @throws IOException io exception
     */
    public void creaPerfil(int posicio) throws IOException {
        Profile perfil;

        Profileable profileable = new Participant(nomComplet, nomArtistic, dataDeNaixement, paisOrigen, nivellExpertesa, URLfoto);

        String nomFitxer = toLowerCamelCase(nomArtistic);
        nomFitxer = "profiles\\" + nomFitxer + ".html";
        perfil = ProfileFactory.createProfile(nomFitxer);

        setInformacioPaisOrigen();

        if (posicio == ELIMINAT) {
            perfil.addExtra("Position", "Eliminated");
        } else {
            perfil.addExtra("Points", String.valueOf(puntuacio));
            perfil.addExtra("Position", String.valueOf(posicio + 1));
        }

        for (int i = 0; i < informacioPaisOrigen.get(0).getIdiomes().size(); i++) {
            perfil.addLanguage(informacioPaisOrigen.get(0).getIdiomes().get(i).getNomAngles());
        }

        perfil.setCountry(paisOrigen);

        perfil.setFlagUrl(informacioPaisOrigen.get(0).getBandera());

        perfil.setProfileable(profileable);

        perfil.writeAndOpen();
    }

    /**
     * Mètode setter de informacioPaisOrigen
     */
    public void setInformacioPaisOrigen() {
        try {
            StringBuilder text = new StringBuilder();
            String urlCompleta = "https://restcountries.eu/rest/v2/name/"  + paisOrigen + "?fields=languages;flag;";
            URL url = new URL(urlCompleta);

            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

            String linea;
            while ((linea = br.readLine()) != null)
                text.append(linea).append("\n");
            br.close();

            Gson gson = new Gson();
            informacioPaisOrigen = gson.fromJson(text.toString(), new TypeToken<ArrayList<InformacioPais>>(){}.getType());
        } catch (IOException e) {
            System.err.println("\nError de lectura JSON Pais: " + e.getMessage());
        }
    }

    /**
     * Mètode que retorna la paraula escrita en lowerCamelCase
     * @return paraula en lowerCamelCase
     * @param paraula: string que s'ha de transformar a lowerCamelCase
     */
    public static String toLowerCamelCase(String paraula) {
        StringBuilder lowerCamelCase = new StringBuilder();

        for (String parts : paraula.split(" ")) {
            char upper = Character.toUpperCase(parts.charAt(0));
            lowerCamelCase.append(upper);
            for (int i = 1; i < parts.length(); i++) {
                char lower = Character.toLowerCase(parts.charAt(i));
                lowerCamelCase.append(lower);
            }
        }

        char primeraLletra = Character.toLowerCase(lowerCamelCase.charAt(0));
        lowerCamelCase.deleteCharAt(0);
        lowerCamelCase.insert(0, primeraLletra);

        return lowerCamelCase.toString();
    }
}
