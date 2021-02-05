package Controller;

import com.google.gson.Gson;
import Model.Competicio;
import View.Pantalla;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Classe Controller que va gestionant el funcionament del programa
 */
public class Controller {

    /**
     * Constant boolean per saber si la competicio està acabada
     */
    public static final boolean COMPETICIO_ACABADA = true;

    /**
     * Constant boolean per saber si la competicio està en curs
     */
    public static final boolean COMPETICIO_EN_CURS = false;

    /**
     * Constant boolean per saber si la competicio està guanyada per l'usuari
     */
    public static final boolean COMPETICIO_GUANYADA = true;

    /**
     * Constant boolean per saber si la competicio està perduda per l'usuari
     */
    public static final boolean COMPETICIO_PERDUDA = false;

    /**
     * Constant boolean per saber si el login ha estat correcte
     */
    public static final boolean LOGIN_CORRECTE = true;

    /**
     * Constant int per saber si l'usuari ha estat eliminat de la competició
     */
    public static final int ELIMINAT = -1;

    /**
     * Constant int per saber si el registre és vàlid
     */
    public static final int REGISTRE_CORRECTE = 0;

    private final Pantalla pantalla;
    private Competicio competicio;
    private String usuari;

    /**
     * Constructor de la classe Controller
     */
    public Controller(){
        this.pantalla = new Pantalla();
        this.competicio = new Competicio();
    }

    /**
     * Mètode que gestiona la execució del programa, carrega les dades del json al programa,
     * i les compara amb la data actual per comprovar en quin punt de la competició ens trobem.
     * Depèn de la data actual respecte a les dates de la competició, el programa crida les funcions privades
     * coresponents en cada cas.
     * @throws IOException IO exception
     * @throws ParseException Parse exception
     */
    public void executaPrograma() throws IOException, ParseException {
        Date dataInici, dataFinal, dataAvui;

        llegirJson();

        dataInici = competicio.getDescripcioCompeticio().getDataInici();
        dataFinal = competicio.getDescripcioCompeticio().getDataFinal();
        dataAvui = Calendar.getInstance().getTime();

        informacioGeneral(dataInici, dataFinal);

        if (dataAvui.before(dataInici)) {
            executaMenuRegistrar();
        } else {
            if (dataAvui.before(dataFinal)) {
                if (gestionaOpcionsMenuLogin() == LOGIN_CORRECTE) {
                    gestionaCompeticio();
                }
            } else {
                dataActualPosteriorDataFinalCompeticio();
            }
        }
    }

    /**
     * Mètode que gestiona la lectura dels fitxers json (batalles.json i competicio.json) i volca les dades
     * a les classes Competició i DescripcióBatalla.
     */
    private void llegirJson(){
        String jsonCompeticio;

        Gson gson = new Gson();
        jsonCompeticio = competicio.importarTextCompeticions();
        competicio = gson.fromJson(jsonCompeticio, Competicio.class);

        competicio.llegirJsonBatalles();
    }

    /**
     * Mètode que mostra les dades generals de la competició
     * @param dataInici: Data en que comença la competició
     * @param dataFinal: Data en que finalitza la competició
     */
    private void informacioGeneral(Date dataInici, Date dataFinal) {
        String nomCompeticio;
        int fases;
        int nombreDeParticipants;

        nomCompeticio = competicio.getDescripcioCompeticio().getNom();
        fases = competicio.getDescripcioCompeticio().getFases().size();
        nombreDeParticipants = competicio.getLlistaParticipants().size();

        pantalla.mostraDadesGenerals(nomCompeticio, dataInici, dataFinal, fases, nombreDeParticipants);
    }

    /**
     * Mètode que executa el menú de registrar amb la opció de registrar un participant o de sortir del programa.
     */
    private void executaMenuRegistrar() throws IOException {
        String opcio;

        Pantalla.mostraMissatge(Pantalla.MENU_REGISTRAR);

        label:
        while(true) {
            Pantalla.mostraMissatge(Pantalla.DEMANA_OPCIO);
            opcio = pantalla.demanaOpcio();
            switch (opcio) {
                case "1":
                    registrar();
                    break label;
                case "2":
                    break label;
                default:
                    Pantalla.mostraMissatge(Pantalla.ERROR_INPUT);
                    break;
            }
        }
    }

    /**
     * Mètode que gestiona l'algorisme per registrar un participant.
     */
    private void registrar() throws IOException {
        String nomComplet;
        String nomArtistic;
        String dataDeNaixement;
        String paisOrigen;
        String nivellExpertesa;
        String URLFoto;

        int sortidaRegistre;

        while (true) {
            Pantalla.mostraMissatge(Pantalla.CAPSALERA_REGISTRE);
            Pantalla.mostraMissatge(Pantalla.REGISTRE_NOM_COMPLET);
            nomComplet = pantalla.demanaOpcio();

            Pantalla.mostraMissatge(Pantalla.REGISTRE_NOM_ARTISTIC);
            nomArtistic = pantalla.demanaOpcio();

            Pantalla.mostraMissatge(Pantalla.REGISTRE_DATA_DE_NAIXEMENT);
            dataDeNaixement = pantalla.demanaOpcio();

            Pantalla.mostraMissatge(Pantalla.REGISTRE_PAIS_DE_ORIGEN);
            paisOrigen = pantalla.demanaOpcio();

            Pantalla.mostraMissatge(Pantalla.REGISTRE_NIVELL_DE_EXPERTESA);
            nivellExpertesa = pantalla.demanaOpcio();

            Pantalla.mostraMissatge(Pantalla.REGISTRE_URL_FOTO);
            URLFoto = pantalla.demanaOpcio();

            sortidaRegistre = competicio.comprovaRegistreCorrecte(nomArtistic, dataDeNaixement, paisOrigen, nivellExpertesa);

            if (sortidaRegistre == REGISTRE_CORRECTE) {
                Pantalla.mostraMissatge(Pantalla.FINAL_REGISTRE);

                competicio.registraParticipant(nomComplet, nomArtistic, dataDeNaixement, paisOrigen,
                        Integer.parseInt(nivellExpertesa), URLFoto);

                competicio.exportaDadesCompeticioAJson();

                break;
            } else if (sortidaRegistre == Competicio.ERROR_REGISTRE_NOM_ARTISTIC) {
                Pantalla.mostraMissatge(Pantalla.ERROR_MISSATGE_REGISTRE_NOM_ARTISTIC);
            } else if (sortidaRegistre == Competicio.ERROR_REGISTRE_DATA_DE_NAIXEMENT) {
                Pantalla.mostraMissatge(Pantalla.ERROR_MISSATGE_REGISTRE_DATA_DE_NAIXEMENT);
            } else if (sortidaRegistre == Competicio.ERROR_REGISTRE_PAIS) {
                Pantalla.mostraMissatge(Pantalla.ERROR_MISSATGE_REGISTRE_PAIS);
            } else if (sortidaRegistre == Competicio.ERROR_REGISTRE_NIVELL_EXPERTESA) {
                Pantalla.mostraMissatge(Pantalla.ERROR_MISSATGE_REGISTRE_NIVELL_EXPERTESA);
            }
        }
    }

    /**
     * Mètode que executa el menú de login amb la opció de fer login un participant o de sortir del programa.
     * @return booleà de si l'usuari vol fer un login o no.
     */
    private boolean gestionaOpcionsMenuLogin() {
        String opcio;

        Pantalla.mostraMissatge(Pantalla.MENU_LOGIN);

        while(true) {
            Pantalla.mostraMissatge(Pantalla.DEMANA_OPCIO);
            opcio = pantalla.demanaOpcio();
            switch (opcio) {
                case "1":
                    gestionaParticipantEnLlista();
                    return true;
                case "2":
                    return false;
                default:
                    Pantalla.mostraMissatge(Pantalla.ERROR_INPUT);
                    break;
            }
        }
    }

    /**
     * Mètode que gestiona la introducció d'un participant i si està o no en contingut a la llista de la competició.
     */
    private void gestionaParticipantEnLlista() {
        String nomArtistic;
        while (true) {
            Pantalla.mostraMissatge(Pantalla.DEMANA_NOM_ARTISTIC);

            nomArtistic = pantalla.demanaOpcio();

            if (competicio.participantContingutEnLaLlista(nomArtistic))  {
                usuari = nomArtistic;
                break;
            } else {
                pantalla.mostraErrorNomArtisticNoTrobat(nomArtistic);
            }
        }
    }

    /**
     * Mètode que gestiona l'algorisme de la competició
     */
    private void gestionaCompeticio() throws IOException {
        int fasesTotal;
        fasesTotal = competicio.getDescripcioCompeticio().getFases().size();

        // Gestionem si la competicio té 2 fases
        if (fasesTotal == 2) {
            // Si el usuari ha guanyat la fase inicial, seguirà en curs la
            // competició, sinó acabarem el programa amb gestionaGuanyador()
            if (gestionaFaseInicial(fasesTotal) == COMPETICIO_EN_CURS) {
                // Acabem indiferentment del resultat de fase final, la competició amb gestionaGuanyador()
                gestionaFaseFinal(fasesTotal);
            }
        }

        // Gestionem si la competicio té 3 fases
        else {
            // Si el usuari ha guanyat la fase inicial, seguirà en curs la
            // competició, sinó acabarem el programa amb gestionaGuanyador()
            if (gestionaFaseInicial(fasesTotal) == COMPETICIO_EN_CURS) {
                // Si el usuari ha guanyat la fase intermitja, seguirà en curs la
                // competició, sinó acabarem el programa amb gestionaGuanyador()
                if (gestionaFaseIntermitja(fasesTotal) == COMPETICIO_EN_CURS) {
                    // Acabem indiferentment del resultat de fase final, la competició amb gestionaGuanyador()
                    gestionaFaseFinal(fasesTotal);
                }
            }
        }

        gestionaGuanyador();
    }

    /**
     * Mètode que gestiona la fase inicial de la competició. Aquest elimina un participant aleatori si hi ha un nombre
     * de participants senar a la competició. En cap cas, aquest participant eliminat serà l'usuari.
     * @param fasesTotals numero de la fase final de la competició. Pot ser 2 o 3.
     * @return booleà de si l'usuari ha acabat la competició o no.
     */
    private boolean gestionaFaseInicial(int fasesTotals) throws IOException {
        //Obliguem a que la llista sigui parella (sense eliminar usuari) i gestionem la fase inicial
        competicio.obligarLlistaParticipantsParella(usuari);
        return gestionaFase(0, fasesTotals);
    }

    /**
     * Mètode que gestiona la fase intermitja de la competició.  Canvia totes les puntuacions a 0 i elimina
     * de la competició a la meitat de participants amb menys puntuació. Aquest elimina un participant aleatori
     * si hi ha un nombre de participants senar a la competició. En cap cas, aquest participant eliminat serà el
     * Usuari.
     * @param fasesTotals numero de la fase final de la competició. Pot ser 2 o 3.
     * @return booleà de si l'usuari ha acabat la competició o no.
     */
    private boolean gestionaFaseIntermitja(int fasesTotals) throws IOException {
        float puntuacioDelUsuari;
        int indexDelUsuari;

        indexDelUsuari = competicio.getIndexOfLlistaParticipants(usuari);
        puntuacioDelUsuari = competicio.getLlistaParticipants().get(indexDelUsuari).getPuntuacio();

        //Eliminem a la meitat de Participants
        competicio.eliminaMeitatParticipants();

        //Obliguem a que la llista sigui parella (sense eliminar usuari)
        competicio.obligarLlistaParticipantsParella(usuari);

        //Resetegem les puntuacions
        competicio.canviaTotesLesPuntuacionsAZero();

        //Gestionem la fase intermitja sempre i quan el usuari estigui contingut a la llista en competició
        if (competicio.usuariEnCompeticio(usuari)) {
            if (gestionaFase(1, fasesTotals) == COMPETICIO_EN_CURS) {
                return COMPETICIO_EN_CURS;
            }
        } else {
            competicioAcabada(puntuacioDelUsuari, 1, 3, COMPETICIO_PERDUDA);
        }
        return COMPETICIO_ACABADA;
    }

    /**
     * Mètode que executa la fase final de la competició.  Canvia totes les puntuacions a 0 i elimina de la
     * competició a tots els participants menys als dos amb més puntuació.
     * @param fasesTotals numero de la fase final de la competició. Pot ser 2 o 3.
     */
    private void gestionaFaseFinal(int fasesTotals) throws IOException {
        float puntuacioDelUsuari;
        int indexDelUsuari;

        indexDelUsuari = competicio.getIndexOfLlistaParticipants(usuari);
        puntuacioDelUsuari = competicio.getLlistaParticipants().get(indexDelUsuari).getPuntuacio();

        //Eliminem a tots els Participants menys als de més Puntuació
        competicio.eliminaTotsParticipantsMenysDos();

        //Resetegem les puntuacions
        competicio.canviaTotesLesPuntuacionsAZero();

        //Gestionem la fase final sempre i quan el usuari estigui contingut a la llista en competició
        if (competicio.usuariEnCompeticio(usuari)) {
            gestionaFase(fasesTotals - 1, fasesTotals);

            //Gestionem el guanyador i el Menu Final depenent si el usuari ha guanyat o perdut. (Menu diferent)
            if (competicio.usuariEnCompeticio(usuari)) {
                indexDelUsuari = competicio.getIndexOfLlistaParticipants(usuari);
                puntuacioDelUsuari = competicio.getLlistaParticipants().get(indexDelUsuari).getPuntuacio();

                String nomGuanyador = competicio.declaraGuanyador();

                if (nomGuanyador.equals(usuari)) {
                    //El Usuari ha arribat a la última fase i ha guanyat la competició
                    competicioAcabada(puntuacioDelUsuari, fasesTotals, fasesTotals, COMPETICIO_GUANYADA);
                } else {
                    //El Usuari ha arribat a la última fase però no ha guanyat la competició
                    competicioAcabada(puntuacioDelUsuari, fasesTotals, fasesTotals, COMPETICIO_PERDUDA);
                }
            }
        } else {
            //El Usuari no ha arribat a la última fase
            competicioAcabada(puntuacioDelUsuari, fasesTotals - 1, fasesTotals, COMPETICIO_PERDUDA);
        }
    }

    /**
     * Mètode que gestiona una fase. Demana crear aparallaments aleatoris i mostra el menu per començar a executar la batalla
     * assignada, mostrar el ranquing, crear un perfil o abandonar la competició.
     * @param fase enter que indica en quina fase es troba la competició
     */
    private boolean gestionaFase(int fase, int fasesTotals) throws IOException {
        int tipusBatalla;
        float puntuacioUsuari;
        String nomRival;
        String opcio;

        //Cada fase està formada per 2 Batalles
        for (int i = 0; i < 2; i++) {
            competicio.creaAparallamentRivalUsuari(usuari, i, fase);

            puntuacioUsuari = competicio.retornaParticipant(usuari).getPuntuacio();
            nomRival = competicio.getDescripcioCompeticio().getFases().get(fase).getBatalles()[i].getRival().getNickname();
            tipusBatalla = competicio.getDescripcioCompeticio().getFases().get(fase).getBatalles()[i].getTipus();

            //Gestionem el Menu la Fase
            label:
            while(true) {
                pantalla.mostraLobby(fase+1, fasesTotals, puntuacioUsuari, i + 1, 2, tipusBatalla, nomRival);
                Pantalla.mostraMissatge(Pantalla.MENU_LOBBY);
                Pantalla.mostraMissatge(Pantalla.DEMANA_OPCIO);
                opcio = pantalla.demanaOpcio();
                switch (opcio) {
                    case "1":
                        executaGestioBatalla(fase, nomRival, i);
                        break label;
                    case "2":
                        gestionaRanquings();
                        break;
                    case "3":
                        gestioCreaPerfil();
                        break;
                    case "4":
                        competicio.abandonaCompeticio(usuari);
                        return COMPETICIO_ACABADA;
                    default:
                        Pantalla.mostraMissatge(Pantalla.ERROR_INPUT);
                        break;
                }
            }
        }
        return COMPETICIO_EN_CURS;
    }

    /**
     * Mètode que gestiona una la creació de perfil. Demana les dades suficients per la creació d'un perfil.
     * Cal destacar que el nom demanat per pantalla pot ser Nom Artístic o Nom Complet, per tant s'haurà de comprovar
     * que aquests dos estiguin o a la llista de participants en competició o a la dels eliminats.
     */
    private void gestioCreaPerfil() throws IOException {
        String nom;
        String paisOrigen;
        int posicio;

        Pantalla.mostraMissatge(Pantalla.DEMANA_NOM_PERFIL);

        nom = pantalla.demanaOpcio();

        competicio.ordenaParticipants();

        // Nom Artistic contingut a la Llista de Participants en Competicio
        if (competicio.participantContingutEnLaLlista(nom)) {
            posicio = competicio.getIndexOfLlistaParticipants(nom);
            paisOrigen = competicio.getLlistaParticipants().get(posicio).getPaisOrigen();
            pantalla.mostraInformacioPaisOrigen(paisOrigen);
            Pantalla.mostraMissatge(Pantalla.GENERA_HTML);
            competicio.getLlistaParticipants().get(posicio).creaPerfil(posicio);
        } else {
            // Nom Complet contingut a la Llista de Participants en Competicio
            if (competicio.participantNomCompletContingutEnLaLlista(nom)) {
                posicio = competicio.getIndexOfLlistaParticipantsNomComplet(nom);
                paisOrigen = competicio.getLlistaParticipants().get(posicio).getPaisOrigen();
                pantalla.mostraInformacioPaisOrigen(paisOrigen);
                Pantalla.mostraMissatge(Pantalla.GENERA_HTML);
                competicio.getLlistaParticipants().get(posicio).creaPerfil(posicio);
            } else {
                // Nom Artistic contingut a la Llista de Participants Eliminats
                if (competicio.participantContingutEnLaLlistaEliminats(nom)) {
                    posicio = competicio.getIndexOfLlistaParticipantsEliminats(nom);
                    paisOrigen = competicio.getLlistaParticipantsEliminats().get(posicio).getPaisOrigen();
                    pantalla.mostraInformacioPaisOrigen(paisOrigen);
                    Pantalla.mostraMissatge(Pantalla.GENERA_HTML);
                    competicio.getLlistaParticipantsEliminats().get(posicio).creaPerfil(ELIMINAT);
                } else {
                    // Nom Complet contingut a la Llista de Participants Eliminats
                    if (competicio.participantNomCompletContingutEnLaLlistaEliminats(nom)) {
                        posicio = competicio.getIndexOfLlistaParticipantsEliminatsNomComplet(nom);
                        paisOrigen = competicio.getLlistaParticipantsEliminats().get(posicio).getPaisOrigen();
                        pantalla.mostraInformacioPaisOrigen(paisOrigen);
                        Pantalla.mostraMissatge(Pantalla.GENERA_HTML);
                        competicio.getLlistaParticipantsEliminats().get(posicio).creaPerfil(ELIMINAT);
                    } else {
                        // Ni Nom Complet ni Nom Artistic en cap llista.
                        pantalla.mostraErrorNomArtisticNoTrobat(nom);
                    }
                }
            }
        }
    }

    /**
     * Mètode que gestiona l'algorisme d'execució d'una batalla i les seves simulacions.
     * @param fase enter que indica en quina fase es troba la competicio
     * @param nomRival String amb el nom del rival que haurà de batallar l'usuari
     * @param batallaActual enter que indica el nombre de la batalla actual
     */
    private void executaGestioBatalla(int fase, String nomRival, int batallaActual) {
        competicio.simularBatalles(usuari, nomRival);
        competicio.ferBatalla(fase, nomRival, batallaActual);
    }

    /**
     * Mètode que demana ordenar els participants segons la seva puntuació per després mostrar-los per pantalla,
     * en el format ranquings (de major a menor puntuació).
     */
    private void gestionaRanquings() {
        Pantalla.mostraMissatge(Pantalla.CAPSALERA_RANQUINGS);
        competicio.ordenaParticipants();

        ArrayList<String> nomsArtistics = competicio.getNomsArtistics();
        ArrayList<Float> puntuacions = competicio.getPuntuacionsOrdenades();

        pantalla.mostraRanquigsPosicions(nomsArtistics, puntuacions, usuari);
    }

    /**
     * Mètode que gestiona el guanyador de la competició (participant amb més puntuació).
     */
    private void gestionaGuanyador() throws IOException {
        String guanyador = competicio.declaraGuanyador();
        pantalla.mostraGuanyador(guanyador);
    }

    /**
     * Mètode que gestiona el menú d'opcions disponibles un cop la competició està finalitzada.
     */
    private void competicioAcabada(float puntuacio, int faseActual, int fasesTotals, boolean estat) throws IOException {
        label:
        while(true) {
            pantalla.mostraLobbyCompeticioAcabada(faseActual,fasesTotals, puntuacio, estat);
            Pantalla.mostraMissatge(Pantalla.MENU_LOBBY_COMPETICIO_ACABADA);
            Pantalla.mostraMissatge(Pantalla.DEMANA_OPCIO);
            String opcio = pantalla.demanaOpcio();
            switch (opcio) {
                case "1":
                    Pantalla.mostraMissatge(Pantalla.ERROR_BATALLAR_COMPETICIO_ACABADA);
                    break;
                case "2":
                    gestionaRanquings();
                    break;
                case "3":
                    gestioCreaPerfil();
                    break;
                case "4":
                    break label;
                default:
                    Pantalla.mostraMissatge(Pantalla.ERROR_INPUT);
                    break;
            }
        }
    }

    /**
     * Mètode que informa del guanyador de la competició quan la data actual és posterior a la data finalització
     * de la competició. Llegeix de guanyador.json el nom del guanyador per imprimir-lo per pantalla. Suposem que
     * sempre existirá aquest guanyador al ser la data actual posterior a la competició i no tenir sentit la no
     * existència del fitxer guanyador.json.
     */
    private void dataActualPosteriorDataFinalCompeticio() {
        String guanyador;
        String opcio;

        guanyador = competicio.importarNomGuanyador();
        pantalla.mostraGuanyador(guanyador);

        Pantalla.mostraMissatge(Pantalla.MENU_COMPETICIO_DESPRES_DATA);

        while(true) {
            Pantalla.mostraMissatge(Pantalla.DEMANA_OPCIO);
            opcio = pantalla.demanaOpcio();
            if ("1".equals(opcio)) {
                break;
            } else {
                Pantalla.mostraMissatge(Pantalla.ERROR_INPUT);
            }
        }
    }

}
