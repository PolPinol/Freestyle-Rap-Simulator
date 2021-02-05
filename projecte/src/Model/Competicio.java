package Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Classe que executa tota la competició
 */
public class Competicio{

    /**
     * Constant int per saber el tipus de batalla
     */
    public static final int A_CAPELLA = 1;

    /**
     * Constant int per saber el tipus de batalla
     */
    public static final int SANGRE = 2;

    /**
     * Constant int per saber el tipus d'error del registre - nom artístic
     */
    public static final int ERROR_REGISTRE_NOM_ARTISTIC = 1;

    /**
     * Constant int per saber el tipus d'error del registre - data de naixement
     */
    public static final int ERROR_REGISTRE_DATA_DE_NAIXEMENT = 2;

    /**
     * Constant int per saber el tipus d'error del registre - nivell expertesa
     */
    public static final int ERROR_REGISTRE_NIVELL_EXPERTESA = 3;

    /**
     * Constant int per saber el tipus d'error del registre - pais
     */
    public static final int ERROR_REGISTRE_PAIS = 4;

    /**
     * Constant int per saber si el registre es correcte
     */
    public static final int REGISTRE_CORRECTE = 0;

    @SerializedName("competition")
    private DescripcioCompeticio descripcioCompeticio;

    @SerializedName("countries")
    private ArrayList<String> paisosAcceptats;

    @SerializedName("rappers")
    private ArrayList<Participant> llistaParticipants;

    private transient ArrayList<Participant> llistaParticipantsEliminats;

    private transient DescripcioBatalla descripcioBatalla;

    /**
     * Constructor de la classe Competició
     */
    public Competicio(){
        this.descripcioCompeticio = new DescripcioCompeticio();
        this.paisosAcceptats = new ArrayList<>();
        this.llistaParticipants = new ArrayList<>();
        this.llistaParticipantsEliminats = new ArrayList<>();
        this.descripcioBatalla = new DescripcioBatalla();
    }

    /**
     * Descripció de la Competició getter
     * @return descripció de la competició
     */
    public DescripcioCompeticio getDescripcioCompeticio() {
        return descripcioCompeticio;
    }

    /**
     * Llista de Participants getter
     * @return llista de participants
     */
    public ArrayList<Participant> getLlistaParticipants() {
        return llistaParticipants;
    }

    /**
     * Métode getter de LlistaParticipants que retorna un arrayList amb els noms artístics de tots els participants
     * @return arrayList de strings amb els noms artístics dels participants
     */
    public ArrayList<String> getNomsArtistics() {
        ArrayList<String> nomsArtistics = new ArrayList<>();

        for (Participant participant : this.llistaParticipants) {
            nomsArtistics.add(participant.getNickname());
        }

        return nomsArtistics;
    }

    /**
     * Métode getter de les puntuacions de tots els participants.
     * @return arrayList amb les puntuacions dels participants
     */
    public ArrayList<Float> getPuntuacionsOrdenades() {
        ArrayList<Float> puntuacions = new ArrayList<>();

        for (Participant participant : this.llistaParticipants) {
            puntuacions.add(participant.getPuntuacio());
        }

        return puntuacions;
    }

    /**
     * Métode que guarda la informacio del participant al registrar-se i l'afegeix a la llista de participants.
     * @param nomComplet string amb el nom complert del participant
     * @param nomArtistic string amb el nom artístic amb que vol competir el participant
     * @param dataDeNaixement string amb la data de naixement del participant que s'està registrant
     * @param paisOrigen string amb el nom del pais d'origen del participant
     * @param nivell enter que conté el nivell d'expertesa del participant
     * @param url string amb la URL de la foto del participant
     */
    public void registraParticipant(String nomComplet, String nomArtistic, String dataDeNaixement,
                                    String paisOrigen, int nivell, String url) {
        Participant participant = new Participant(nomComplet, nomArtistic, dataDeNaixement, paisOrigen, nivell, url);
        llistaParticipants.add(participant);
    }

    /**
     * Métode que comprova si el participant està dins de la llista de participants de la competició.
     * @param nomArtistic string amb el nom artístic del participant
     * @return booleà si el participant està a la llista o no
     */
    public boolean participantContingutEnLaLlista(String nomArtistic){
        for (Participant participant : llistaParticipants) {
            if (participant.getNickname().equals(nomArtistic)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Métode que comprova si el participant està dins de la llista de participants de la competició.
     * @param nom string amb el nom complet del participant
     * @return booleà si el participant està a la llista o no
     */
    public boolean participantNomCompletContingutEnLaLlista(String nom) {
        for (Participant participant : llistaParticipants) {
            if (participant.getName().equals(nom)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Métode que crea el aparellament per una batalla, escollint un rival aleatori i un tipus de batalla aleatoria.
     * @param usuari string del nom artístic de l'usuari
     * @param nombreBatalla enter que indica en quin nombre de batalla està l'usuari
     * @param fase enter que indica la fase de la competició en la que es troba l'usuari
     */
    public void creaAparallamentRivalUsuari(String usuari, int nombreBatalla, int fase) {
        String rival;

        do {
            int randomRival = (int) Math.floor(Math.random() * llistaParticipants.size());
            rival = llistaParticipants.get(randomRival).getNickname();
        } while (rival.equals(usuari));

        descripcioCompeticio.getFases().get(fase).setBatalla(nombreBatalla);
        descripcioCompeticio.getFases().get(fase).getBatalles()[nombreBatalla].setRival(retornaParticipant(rival));
        descripcioCompeticio.getFases().get(fase).getBatalles()[nombreBatalla].setUsuari(retornaParticipant(usuari));
    }

    /**
     * Mètode que simula les batalles de la resta de participants. Només es genera una puntuació random a cada
     * participant en la competició depenent del seu nivell d'expertesa. Si el nivell d'expertesa és 1 llavors hem
     * decidit que podran fer 0 o 2 rimes, i si el nivell d'expertesa és 2, podran fer entre 2, 3 i 4 rimes. Tot això
     * és random, de la mateixa manera que el tipus de batalla també ho és. Es repeteix el mateix algorisme ja que
     * s'ha de calcular per les dues estrofes que es simulen.
     * @param usuari string del nom artístic de l'usuari
     * @param nomRival string amb el nom artístic del rival
     */
    public void simularBatalles(String usuari, String nomRival) {
        for (Participant participant : llistaParticipants) {
            if (!(participant.getNickname().equals(usuari) || participant.getNickname().equals(nomRival))) {
                int randomTipus = (int) Math.floor(Math.random() * 3 + 1);
                if (participant.getNivellExpertesa() == 1) {
                    int randomRimes = (int) Math.floor(Math.random() * 2);
                    int numRimesPrimeraEstrofa, numRimesSegonaEstrofa;
                    float puntuacio = participant.getPuntuacio();

                    if (randomRimes % 2 == 0) {
                        numRimesPrimeraEstrofa = 0;
                    } else {
                        numRimesPrimeraEstrofa = 2;
                    }

                    randomRimes = (int) Math.floor(Math.random() * 2);

                    if (randomRimes % 2 == 0) {
                        numRimesSegonaEstrofa = 0;
                    } else {
                        numRimesSegonaEstrofa = 2;
                    }

                    establirPuntuacionsSimulades(participant, randomTipus, numRimesPrimeraEstrofa, numRimesSegonaEstrofa, puntuacio);
                } else {
                    int randomRimes = (int) Math.floor(Math.random() * 3);
                    int numRimesPrimeraEstrofa, numRimesSegonaEstrofa;
                    float puntuacio = participant.getPuntuacio();

                    if (randomRimes == 0) {
                        numRimesPrimeraEstrofa = 2;
                    } else if (randomRimes == 1){
                        numRimesPrimeraEstrofa = 3;
                    } else {
                        numRimesPrimeraEstrofa = 4;
                    }

                    randomRimes = (int) Math.floor(Math.random() * 2);

                    if (randomRimes % 2 == 0) {
                        numRimesSegonaEstrofa = 3;
                    } else {
                        numRimesSegonaEstrofa = 4;
                    }

                    establirPuntuacionsSimulades(participant, randomTipus, numRimesPrimeraEstrofa, numRimesSegonaEstrofa, puntuacio);
                }
            }
        }
    }

    /**
     * Mètode que gestiona la batalla entre usuari i rival escollint tema i estrofes per així executar-la.
     * @param fase enter que indica en quina fase es troba la competicio
     * @param nomRival String amb el nom del rival que haurà de batallar l'usuari
     * @param batallaActual enter que indica el nombre de la batalla actual
     */
    public void ferBatalla(int fase, String nomRival, int batallaActual) {
        String tema;
        String[] estrofes;
        int nivellExpertesaRival;

        nivellExpertesaRival = retornaNivellExpertesaParticipant(nomRival);
        tema = descripcioBatalla.getRandomTema();
        estrofes = descripcioBatalla.getRandomEstrofes(nivellExpertesaRival, tema);

        descripcioCompeticio.getFases().get(fase).getBatalles()[batallaActual].setTema(tema);
        descripcioCompeticio.getFases().get(fase).getBatalles()[batallaActual].setEstrofes(estrofes);
        descripcioCompeticio.getFases().get(fase).getBatalles()[batallaActual].executaBatalla();
    }

    /**
     * Mètode que assigna la puntuació als participants depenent del tipus de batalla simulada.
     * @param participant participant que es simula la puntuació
     * @param randomTipus enter que s'assigna al tipus de batalla
     * @param numRimesPrimeraEstrofa enter que descriu el nombre de rimes assignades a la primera estrofa
     * @param numRimesSegonaEstrofa enter que descriu el nombre de rimes assignades a la segona estrofa
     * @param puntuacio decimal que descriu la puntuació d'un participant
     */
    private void establirPuntuacionsSimulades(Participant participant, int randomTipus, int numRimesPrimeraEstrofa,
                                              int numRimesSegonaEstrofa, float puntuacio) {
        if (randomTipus == SANGRE) {
            puntuacio += (float) Math.PI * numRimesPrimeraEstrofa * numRimesPrimeraEstrofa / 4;
            puntuacio += (float) Math.PI * numRimesSegonaEstrofa * numRimesSegonaEstrofa / 4;
        } else if (randomTipus == A_CAPELLA) {
            puntuacio += (float) (6 * Math.sqrt(numRimesPrimeraEstrofa) + 3) / 2;
            puntuacio += (float) (6 * Math.sqrt(numRimesSegonaEstrofa) + 3) / 2;
        } else {
            puntuacio += 1 + 3 * numRimesPrimeraEstrofa;
            puntuacio += 1 + 3 * numRimesSegonaEstrofa;
        }
        participant.setPuntuacio(puntuacio);
    }

    /**
     * Funció que retorna el participant amb el nom artístic que li introduim
     * @param nomArtistic:String amb el nom artístic del participant del qual volem obtenir la informació
     * @return getLlistaParticipants().get(i): Participant amb la informació de la llista de participants
     * del participant amb el nom Artístic introduit
     */
    public Participant retornaParticipant(String nomArtistic) {
        for (int i = 0; i < llistaParticipants.size(); i++) {
            if (nomArtistic.equals(llistaParticipants.get(i).getNickname())) {
                return llistaParticipants.get(i);
            }
        }
        return null;
    }

    /**
     * Funció que retorna el participant amb el nom Complet que li introduim
     * @param nomComplet:String amb el nom complet del participant del qual volem obtenir la informació
     * @return getLlistaParticipants().get(i): Participant amb la informació de la llista de participants del
     * participant amb el nom Complet introduit
     */
    public Participant retornaParticipantNomComplet(String nomComplet) {
        for (int i = 0; i < llistaParticipants.size(); i++) {
            if (nomComplet.equals(llistaParticipants.get(i).getName())) {
                return llistaParticipants.get(i);
            }
        }
        return null;
    }

    /**
     * Mètode que retorna el nivell d'expertesa del participant.
     * @param nomArtistic string amb el nom artístic del participant
     * @return enter amb el nivell d'expertesa del participant
     */
    public int retornaNivellExpertesaParticipant(String nomArtistic) {
        for (int i = 0; i < llistaParticipants.size(); i++) {
            if (nomArtistic.equals(llistaParticipants.get(i).getNickname())) {
                return llistaParticipants.get(i).getNivellExpertesa();
            }
        }
        return -1;
    }

    /**
     * Mètode que ordena la llista de participants per la seva puntuació.
     */
    public void ordenaParticipants() {
        //Ordenar dos cops la llista de Participants pq no varii el ranquing si les puntuacions estan igualades
        Collections.sort(llistaParticipants);
        Collections.sort(llistaParticipants);
    }

    /**
     * Mètode que elimina la meitat dels participants per tal de que passin a la segona fase.
    */
    public void eliminaMeitatParticipants() {
        ordenaParticipants();

        int meitat = llistaParticipants.size() / 2;
        int midaLlistaParticipants = llistaParticipants.size();

        for (int i = meitat; i < midaLlistaParticipants; i++) {
            llistaParticipantsEliminats.add(llistaParticipants.get(meitat));
            llistaParticipants.remove(meitat);
        }
    }

    /**
     * Mètode que elimina tots els participants menys dos, perquè passin a la fase final.
     */
    public void eliminaTotsParticipantsMenysDos() {
        ordenaParticipants();

        int midaLlistaParticipants = llistaParticipants.size();

        for (int i = 2; i < midaLlistaParticipants; i++) {
            llistaParticipantsEliminats.add(llistaParticipants.get(2));
            llistaParticipants.remove(2);
        }
    }

    /**
     * Mètode que canvia les puntuacions de tots els participants a 0.
     */
    public void canviaTotesLesPuntuacionsAZero() {
        for (Participant participant : llistaParticipants) {
            participant.setPuntuacio(0);
        }
    }

    /**
     * Mètode que comprova si la llista de participants és senar.
     * @return booleà si la llista de participants és senar o no
     */
    public boolean comprovaLlistaSenar() {
        return llistaParticipants.size() % 2 != 0;
    }

    /**
     * Mètode que elimina un participant aleatori que no sigui el propi usuari.
     * @param usuari string amb el nom artístic del usuari
     */
    public void eliminaUnParticipant(String usuari) {
        int randomParticipant;

        do {
            randomParticipant = (int) Math.floor(Math.random() * llistaParticipants.size());
        } while (llistaParticipants.get(randomParticipant) == retornaParticipant(usuari));
        llistaParticipantsEliminats.add(llistaParticipants.get(randomParticipant));
        llistaParticipants.remove(randomParticipant);
    }

    /**
     * Mètode que retorna el index propi de la llista de participants.
     * @param usuari string amb el nom artístic del usuari
     * @return enter index llista de participants
     */
    public int getIndexOfLlistaParticipants(String usuari) {
        for (int i = 0; i < llistaParticipants.size(); i++) {
            if (llistaParticipants.get(i) == retornaParticipant(usuari)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Mètode que retorna el index propi de la llista de participants.
     * @param nomComplet string amb el nom complet del usuari
     * @return enter index llista de participants
     */
    public int getIndexOfLlistaParticipantsNomComplet(String nomComplet) {
        for (int i = 0; i < llistaParticipants.size(); i++) {
            if (llistaParticipants.get(i) == retornaParticipantNomComplet(nomComplet)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Mètode que comprova si l'usuari segueix competint.
     * @param usuari String del nom artistic de l'usuari
     * @return booleà si el usuari segueix a la competició o no
     */
    public boolean usuariEnCompeticio(String usuari) {
        for (int i = 0; i < llistaParticipants.size(); i++) {
            if (llistaParticipants.get(i).getNickname().equals(usuari)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Mètode que fa que l'usuari abandoni la competició.
     * @param usuari string amb el nom artístic del usuari
     */
    public void abandonaCompeticio(String usuari) {
        llistaParticipants.get(getIndexOfLlistaParticipants(usuari)).setPuntuacio(0);
        llistaParticipants.remove(retornaParticipant(usuari));
    }

    /**
     * Mètode que declara el guanyador de la competició. També el guarda al json.
     * @return string amb el nom artístic del guanyador de la competició
     * @throws IOException io exception error de lectura del json
     */
    public String declaraGuanyador() throws IOException {
        ordenaParticipants();

        String guanyador = llistaParticipants.get(0).getNickname();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String exportarJson = gson.toJson(guanyador);
        Files.writeString(Path.of("guanyador.json"), exportarJson);

        return guanyador;
    }

    /**
     * Mètode que comprova que el registre s'ha realitzat correctament
     * @param dataDeNaixement: String amb la data de naixement del usuari
     * @param nomArtistic: String que conté el nom artístic del usuari
     * @param nivellExpertesa: String amb el nivell d'expertesa del usuari
     * @param paisOrigen: String amb el nom del pais d'origen del usuari
     * @return int que indica si el missatge d'error del registre o si ha sigut correcte
     */
    public int comprovaRegistreCorrecte(String nomArtistic, String dataDeNaixement, String paisOrigen,
                                            String nivellExpertesa) {
        ArrayList<String> nomsDelsParticipants = getNomsArtistics();

        if (nomsDelsParticipants.contains(nomArtistic)) {
            return ERROR_REGISTRE_NOM_ARTISTIC;
        } else if ( !(dataDeNaixement.matches("[1-2]\\d\\d\\d-[0-1]\\d-[0-3]\\d")) ) {
            return ERROR_REGISTRE_DATA_DE_NAIXEMENT;
        } else if ( !(paisosAcceptats.contains(paisOrigen)) ) {
            return ERROR_REGISTRE_PAIS;
        } else if ( !(nivellExpertesa.matches("[1-2]")) )  {
            return ERROR_REGISTRE_NIVELL_EXPERTESA;
        } else return REGISTRE_CORRECTE;
    }

    /**
     * Mètode que obliga que la llista de participants tingui un nombre parell de participants.
     * @param usuari String nom artistic de l'usuari
     */
    public void obligarLlistaParticipantsParella(String usuari) {
        if (comprovaLlistaSenar()) {
            eliminaUnParticipant(usuari);
        }
    }

    /**
     * Métode que comprova si el participant elimninat està dins de la llista de participants de la competició.
     * @param nomArtistic string amb el nom artístic del participant
     * @return booleà si el participant està a la llista o no
     */
    public boolean participantContingutEnLaLlistaEliminats(String nomArtistic){
        for (Participant participant : llistaParticipantsEliminats) {
            if (participant.getNickname().equals(nomArtistic)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Funció que retorna el participant amb el nom artistic que li introduim
     * @param nomArtistic:String amb el nom artistic del participant del qual volem obtenir la informació
     * @return getLlistaParticipants().get(i): model.Participant amb la informació de la llista de participants del participant amb el nom Complet introduit
     */
    public Participant retornaParticipantNomArtisticEliminat(String nomArtistic) {
        for (int i = 0; i < llistaParticipantsEliminats.size(); i++) {
            if (nomArtistic.equals(llistaParticipantsEliminats.get(i).getNickname())) {
                return llistaParticipantsEliminats.get(i);
            }
        }
        return null;
    }

    /**
     * Mètode que retorna el index propi de la llista de participants eliminats.
     * @param nomArtistic string amb el nom artistic del usuari
     * @return enter index llista de participants
     */
    public int getIndexOfLlistaParticipantsEliminats(String nomArtistic) {
        for (int i = 0; i < llistaParticipantsEliminats.size(); i++) {
            if (llistaParticipantsEliminats.get(i) == retornaParticipantNomArtisticEliminat(nomArtistic)) {
                return i;
            }
        }
        return -1;
    }


    /**
     * Funció que retorna el participant amb el nom Complet que li introduim
     * @param nomComplet:String amb el nom complet del participant del qual volem obtenir la informació
     * @return getLlistaParticipants().get(i): model.Participant amb la informació de la llista de participants del participant amb el nom Complet introduit
     */
    public Participant retornaParticipantNomCompletEliminats(String nomComplet) {
        for (int i = 0; i < llistaParticipants.size(); i++) {
            if (nomComplet.equals(llistaParticipants.get(i).getName())) {
                return llistaParticipants.get(i);
            }
        }
        return null;
    }

    /**
     * Mètode que retorna el index propi de la llista de participants eliminats amb nom complet.
     * @param nomComplet string amb el nom complet del usuari
     * @return enter index llista de participants
     */
    public int getIndexOfLlistaParticipantsEliminatsNomComplet(String nomComplet) {
        for (int i = 0; i < llistaParticipantsEliminats.size(); i++) {
            if (llistaParticipantsEliminats.get(i) == retornaParticipantNomCompletEliminats(nomComplet)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Llista de Participants Eliminats getter
     * @return llista de participants
     */
    public ArrayList<Participant> getLlistaParticipantsEliminats() {
        return llistaParticipantsEliminats;
    }

    /**
     * Mètode que comprova si el participant està dins de la llista de participants eliminats de la competició.
     * @param nom string amb el nom complet del participant
     * @return booleà si el participant està a la llista o no
     */
    public boolean participantNomCompletContingutEnLaLlistaEliminats(String nom) {
        for (Participant participant : llistaParticipantsEliminats) {
            if (participant.getName().equals(nom)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Mètode getter de descripcióBatalla
     * @return descripcioBatalla
     */
    public DescripcioBatalla getDescripcioBatalla() {
        return descripcioBatalla;
    }

    /**
     * Mètode que retorna un String que importa el text de "competicio.json"
     * @return text.toString() String del text del json
     */
    public String importarTextCompeticions() {
        StringBuilder text = new StringBuilder();
        File arxiu = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            arxiu = new File ("competició.json");
            fr = new FileReader (arxiu);
            br = new BufferedReader(fr);

            String linea;
            while ( (linea = br.readLine() ) != null) {
                text.append(linea).append("\n");
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if( null != fr ){
                    fr.close();
                }
            }catch (Exception e2){
                e2.printStackTrace();
            }
        }
        return text.toString();
    }

    /**
     * Mètode crida al json per omplir descripcioBatalla
     */
    public void llegirJsonBatalles() {
        String jsonBatalles;

        Gson gson = new Gson();
        jsonBatalles = descripcioBatalla.importarTextBatalles();
        descripcioBatalla = gson.fromJson(jsonBatalles, DescripcioBatalla.class);
    }

    /**
     * Mètode que retorna un String del "guanyador.json"
     * @return String del guanyador de la competicio pasada
     */
    public String importarNomGuanyador() {
        StringBuilder text = new StringBuilder();
        File arxiu = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            arxiu = new File ("guanyador.json");
            fr = new FileReader (arxiu);
            br = new BufferedReader(fr);

            String linea;
            while ( (linea = br.readLine() ) != null) {
                text.append(linea).append("\n");
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if( null != fr ){
                    fr.close();
                }
            }catch (Exception e2){
                e2.printStackTrace();
            }
        }

        Gson gson = new Gson();
        return gson.fromJson(String.valueOf(text), String.class);
    }

    /**
     * Mètode que gestiona el exportar totes les dades de la competició a competició.json.
     * @throws IOException possible errors del json
     */
    public void exportaDadesCompeticioAJson() throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String exportarJson = gson.toJson(this);
        Files.writeString(Path.of("competició.json"), exportarJson);
    }



}
