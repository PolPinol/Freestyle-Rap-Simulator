package View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

/**
 * Classe que mostra el programa per pantalla
 */
public class Pantalla{

    /**
     * Constant per definir el tipus batalla acapella
     */
    public static final int A_CAPELLA = 1;

    /**
     * Constant per definir el tipus batalla sangre
     */
    public static final int SANGRE = 2;

    /**
     * Constant booleana per saber si la competicio està guanyada o no
     */
    public static final boolean COMPETICIO_GUANYADA = true;

    /**
     * Constant String per Error Input
     */
    public static final String ERROR_INPUT = "\nPls enter a valid Input!\n\n";

    /**
     * Constant String per Error Registre
     */
    public static final String ERROR_MISSATGE_REGISTRE_NOM_ARTISTIC = "\nError in artistic name. The artistic name already exists!\n";

    /**
     * Constant String per Error Registre
     */
    public static final String ERROR_MISSATGE_REGISTRE_DATA_DE_NAIXEMENT = "\nError in birthdate format!\n";

    /**
     * Constant String per Error Registre
     */
    public static final String ERROR_MISSATGE_REGISTRE_NIVELL_EXPERTESA = "\nError in level. Must be 1 or 2!\n";

    /**
     * Constant String per Error Registre
     */
    public static final String ERROR_MISSATGE_REGISTRE_PAIS = "\nError in Country. This Country is not Accepted.\n";

    /**
     * Constant String per Error Batallar model.Competicio Acabada
     */
    public static final String ERROR_BATALLAR_COMPETICIO_ACABADA = "\n\nCompetition ended. You cannot battle anyone else!\n\n";

    /**
     * Constant String per Demanar Opcio
     */
    public static final String DEMANA_OPCIO = "Choose an option: ";

    /**
     * Constant String per Demanar Nom Artistic
     */
    public static final String DEMANA_NOM_ARTISTIC = "\nEnter your artistic name: ";

    /**
     * Constant String per Demanar Nom Artistic
     */
    public static final String DEMANA_NOM_PERFIL = "\nEnter the name of the rapper: ";

    /**
     * Constant String per Registre Nom Complet
     */
    public static final String REGISTRE_NOM_COMPLET = "- Full name: ";

    /**
     * Constant String per Registre Nom Artistic
     */
    public static final String REGISTRE_NOM_ARTISTIC = "- Artistic name: ";

    /**
     * Constant String per Registre Data de Naixement
     */
    public static final String REGISTRE_DATA_DE_NAIXEMENT = "- Birth date (YYYY-MM-dd): ";

    /**
     * Constant String per Registre Pais Origen
     */
    public static final String REGISTRE_PAIS_DE_ORIGEN = "- Country: ";

    /**
     * Constant String per Registre Nivell Expertesa
     */
    public static final String REGISTRE_NIVELL_DE_EXPERTESA = "- Level: ";

    /**
     * Constant String per Registre URL Foto
     */
    public static final String REGISTRE_URL_FOTO = "- Photo URL: ";

    /**
     * Constant String per Registre URL Foto
     */
    public static final String GENERA_HTML = "\nGenerating HTML file...\n\nDone! The profile will open in your default browser.\n";

    /**
     * Constant String per Capçalera Ranquings
     */
    public static final String CAPSALERA_RANQUINGS =
            "\n" +
            "-------------------------------------\n" +
            "Pos.  |  Name  |  Score\n" +
            "-------------------------------------\n\n";

    /**
     * Constant String per Capçalera Registre
     */
    public static final String CAPSALERA_REGISTRE =
            "\n" +
            "--------------------------------------------------\n" +
            "Please, enter your personal information:\n";

    /**
     * Constant String per Final Registre
     */
    public static final String FINAL_REGISTRE =
            "\nRegistration Completed!\n" +
            "--------------------------------------------------\n";

    /**
     * Constant String per Menu Lobby
     */
    public static final String MENU_LOBBY =
            "1. Start the battle\n" +
            "2. Show Ranking\n" +
            "3. Create Profile\n" +
            "4. Leave Competition\n\n";

    /**
     * Constant String per Menu Lobby model.Competicio Acabada
     */
    public static final String MENU_LOBBY_COMPETICIO_ACABADA =
            "1. Start the battle (deactivated)\n" +
            "2. Show Ranking\n" +
            "3. Create Profile\n" +
            "4. Leave Competition\n\n";

    /**
     * Constant String per Menu Registrar
     */
    public static final String MENU_REGISTRAR =
            "Competiton hasn't started yet. Do you want to:\n\n" +
            "1. Register\n" +
            "2. Leave\n\n";

    /**
     * Constant String per Menu Login
     */
    public static final String MENU_LOGIN =
            "Competiton started. Do you want to:\n\n" +
            "1. Login\n" +
            "2. Leave\n\n";

    /**
     * Constant String per Menu model.Competicio després Data Final
     */
    public static final String MENU_COMPETICIO_DESPRES_DATA =
            "1. Leave\n\n";


    /**
     * Constructor de Pantalla
     */
    public Pantalla(){

    }

    /**
     * Métode que mostra les dades generals per pantalla
     * @param nomCompeticio: String amb el nom de la competicio
     * @param dataInici: Date amb la data d'inici de la competicio
     * @param dataFinal: Date amb la data final de la competicio
     * @param fases: Enter amb el numero de fases de la competicio
     * @param participants: Enter amb el numero de participants
     */
    public void mostraDadesGenerals(String nomCompeticio, Date dataInici, Date dataFinal, int fases, int participants){
        System.out.println("Welcome to competition: "+ nomCompeticio);
        System.out.println("Starts on " + String.format("%1$td/%1$tm/%1$tY", dataInici));
        System.out.println("Ends on " + String.format("%1$td/%1$tm/%1$tY", dataFinal));
        System.out.println("Phases: " + fases);
        System.out.println("Currently: " + participants + " participants\n");
    }

    /**
     * Métode que mostra un missatge d'error si no existeix el nom artístic introduit a la llista de participants
     * @param nomArtistic String nom Artistic del participant
     */
    public void mostraErrorNomArtisticNoTrobat(String nomArtistic) {
        System.out.println("\nYo' bro, there's no \"" + nomArtistic + "\" in ma' list.");
    }

    /**
     * Métode que mostra el lobby per pantalla
     * @param faseActual enter que indica la fase on es troba la competicio en aquest instant
     * @param fasesTotals enter que insica el nombre total de fases que te la competicio
     * @param puntuacio decimal amb la puntuacio que te l'usuari
     * @param batallesTotals enter amb el numero total de batalles que tindra l'usuari en aquesta fase
     * @param batallaActual enter amb el numero de la batalla actual on es troba l'usuari
     * @param tipusBatalla enter que indica de quin tipus es la batalla
     * @param nomRival string amb el nom del rival en aquesta batalla
     */
    public void mostraLobby(int faseActual, int fasesTotals, float puntuacio, int batallaActual, int batallesTotals,
                            int tipusBatalla, String nomRival){
        String tipusBatallaEnString;

        if (tipusBatalla == A_CAPELLA) {
            tipusBatallaEnString = "acapella";
        } else if (tipusBatalla == SANGRE) {
            tipusBatallaEnString = "sangre";
        } else {
            tipusBatallaEnString = "escrita";
        }

        System.out.println("\n-------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Phase: " + faseActual +" / " + fasesTotals + " | Score: " + puntuacio + " | Battle " + batallaActual + " / " + batallesTotals + ": "+ tipusBatallaEnString +" | Rival: " + nomRival);
        System.out.println("--------------------------------------------------------------------------------------------------------------------------\n");
    }

    /**
     * Métode que mostra el ranquing per pantalla
     * @param posicions array amb les posicions de cada rapero en el ranquing
     * @param puntuacions array amb les puntacions de cada rapero
     * @param usuari cadena amb el nom de l'usuari
     */
    public void mostraRanquigsPosicions(ArrayList<String> posicions, ArrayList<Float> puntuacions, String usuari) {
        for (int i = 0; i < posicions.size(); i++) {
            if (!posicions.get(i).equals(usuari)) {
                System.out.println(i+1 + " " + posicions.get(i) + " - " + puntuacions.get(i));
            } else {
                System.out.println(i+1 + " " + posicions.get(i) + " - " + puntuacions.get(i) + " <-- You");
            }
        }
        System.out.println("");
    }

    /**
     * Métode que mostra el lobby quan la competició està acabada
     * @param faseActual enter que indica la fase on es troba la competicio en aquest instant
     * @param fasesTotals enter que insica el nombre total de fases que te la competicio
     * @param puntuacio decimal amb la puntuacio que te l'usuari
     * @param estat booleà que indica si la competició està guanyada o no
     */
    public void mostraLobbyCompeticioAcabada(int faseActual, int fasesTotals, float puntuacio, boolean estat){
        if (estat == COMPETICIO_GUANYADA) {
            System.out.println("\n-------------------------------------------------------------------------------------------------------------------------");
            System.out.println("Phase: " + faseActual +" / " + fasesTotals + " | Score: " + puntuacio + " |  You are the W I N N E R !");
            System.out.println("--------------------------------------------------------------------------------------------------------------------------\n");
        } else if (faseActual == fasesTotals){
            System.out.println("\n-------------------------------------------------------------------------------------------------------------------------");
            System.out.println("Phase: " + faseActual +" / " + fasesTotals + " | Score: " + puntuacio + " |  You've lost kid, I'm sure you'll do better next time...");
            System.out.println("--------------------------------------------------------------------------------------------------------------------------\n");
        } else {
            System.out.println("\n-------------------------------------------------------------------------------------------------------------------------");
            System.out.println("Phase: " + (faseActual + 1) +" / " + fasesTotals + " | Score: " + puntuacio + " |  You've lost kid, I'm sure you'll do better next time...");
            System.out.println("--------------------------------------------------------------------------------------------------------------------------\n");
        }
    }

    /**
     * Métode que mostra el guanyador per pantalla.
     * @param nomGuanyador String nom artistic del guanyador de la competicio
     */
    public void mostraGuanyador(String nomGuanyador) {
        System.out.println("\nCompetition ended! The Winner is " + nomGuanyador + ".\n");
    }

    /**
     * Métode que mostra missatge d'informació del país d'origen d'un participant
     * @param pais String pais d'un participant
     */
    public void mostraInformacioPaisOrigen(String pais) {
        System.out.println("\nGetting information about their country of origin (" + pais + ")...");
    }

    /**
     * Métode que mostra un missatge per pantalla.
     * @param missatge String del missatge
     */
    public static void mostraMissatge(String missatge) {
        System.out.print(missatge);
    }

    /**
     * Mètode que llegeix la opcio introduida per l'usuari.
     * @return string amb la opcio que l'usuari ha introduit per pantalla
     */
    public String demanaOpcio() {
        String opcio = "";
        try {
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            opcio = bufferRead.readLine();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return opcio;
    }

    /**
     * Mètode que llegeix les 4 linies per la estrofa de l'usuari.
     * @return string amb les 4 línies de l'estrofa.
     */
    public static String demanaLinia() {
        StringBuilder text = new StringBuilder();
        Scanner reader = new Scanner(System.in);
        String linia;


        for (int i = 0; i < 3; i++) {
            linia = reader.next();
            text.append(linia).append("\n");
        }
        linia = reader.next();
        text.append(linia);

        return text.toString();
    }
}
