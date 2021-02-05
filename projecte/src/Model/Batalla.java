package Model;

import View.Pantalla;
import java.util.ArrayList;

/**
 * Classe que executa una batalla entre el usuari i el seu contrincant
 */
public class Batalla {

    /**
     * Constant String per cridar usuari nova estrofa
     */
    public static final String YOUR_TURN = " your turn! Drop it!\n\nYour turn!\nEnter your verse:\n\n";

    private String tema;
    private int tipus;
    private Participant usuari;
    private Participant rival;
    private String[] estrofes;

    /**
     * Constructor de la classe Batalla
     */
    public Batalla(){
        this.estrofes = new String[2];
        this.usuari = new Participant();
        this.rival = new Participant();
    }

    /**
     * Mètode setter de les estrofes de la batalla
     * @param estrofes Strings de les estrofes de la batalla
     */
    public void setEstrofes(String[] estrofes) {
        this.estrofes = estrofes;
    }

    /**
     * Mètode setter del tema de la batalla
     * @param tema String del tema de la batalla
     */
    public void setTema(String tema) {
        this.tema = tema;
    }

    /**
     * Mètode getter del tipus de batalla.
     * @return enter del tipus de batalla
     */
    public int getTipus() {
        return tipus;
    }

    /**
     * Mètode getter del nom artístic del rival
     * @return String del nom artístic del rival
     */
    public Participant getRival() {
        return rival;
    }

    /**
     * Mètode que retorna i calcula la puntuon la puntuació que obtè cada participant (usuari i rival) per cada estrofa
     * que diu, en funció del tipus de batalla i les rimes que han dit a cada estrofa.
     *
     * @param estrofa String amb la estrofa corresponent per tal de contar les rimes que conté
     * @return puntuacio enter amb la puntuació obtinguda pel participant ja calculada
     */
    public float calcularPuntuacio(String estrofa) {
        return 0;
    }

    /**
     * Mètode que retorna el numero de rimes que ha aconseguit el participant en la batalla ,  juntant les dues
     * estrofes, comparant les terminacions dels versos i contant  les rimes que hi ha a cada estrofa
     * @param estrofa String amb la estrofa corresponent per tal de contar les rimes que conté
     * @return numRimes enter amb el numero de rimes que ha dit un participant en la batalla
     */
    public int calcularRimes(String estrofa) {
        int numRimes = 0;
        ArrayList<String> rimes = new ArrayList();
        String[] linies = estrofa.split("\n");
        for (int i = 0; i < linies.length; i++) {
            String caracters;
            // No hi han rimes si no s'escriu en el format que toca! A més, evitem error per el charAt.
            if (linies[i].length() < 3) {
                return 0;
            }
            char caracter1 = linies[i].charAt(linies[i].length() - 1 - 1);
            char caracter2 = linies[i].charAt(linies[i].length() - 1 - 2);
            caracters = Character.toString(caracter2) + Character.toString(caracter1);
            rimes.add(caracters);
        }

        int count = 1;
        int countMax = 1;
        String primeraRima;
        for (int i = 0; i < rimes.size(); i++) {
            if (count > countMax) {
                countMax = count;
            }
            count = 1;
            primeraRima = rimes.get(i);
            for (int j = 0; j < rimes.size(); j++) {
                if (i != j) {
                    if (primeraRima.equals(rimes.get(j))) {
                        count++;
                    }
                }
            }
        }

        numRimes = countMax;

        if (numRimes == 1) {
            numRimes = 0;
        }

        return numRimes;
    }

    /**
     * Mètode que executa la batalla de l'usuari amb el seu contrincant, fent que el tema i qui comenci sigui aleatori,
     * alternant els torns i sumant el total de punts de cada estrofa.
     */
    public void executaBatalla(){
        String primeraEstrofaUsuari;
        String segonaEstrofaUsuari;

        float puntuacioUsuariAnterior = usuari.getPuntuacio();
        float puntuacioRivalAnterior = rival.getPuntuacio();
        float puntuacioUsuariActual;
        float puntuacioRivalActual;

        String capsaleraTopic = "\n----------------------------------------------------\n" +
                                   "Topic: " + tema + "\n\n" +
                                   "A coin is tossed in the air and...";

        Pantalla.mostraMissatge(capsaleraTopic);

        int randomTorn = (int) Math.floor(Math.random() * 10);

        if (randomTorn % 2 == 0) {
            primeraEstrofaUsuari = tornDelUsuari();
            tornDelRival(estrofes[0]);
            segonaEstrofaUsuari = tornDelUsuari();
            tornDelRival(estrofes[1]);

        } else {
            tornDelRival(estrofes[0]);
            primeraEstrofaUsuari = tornDelUsuari();
            tornDelRival(estrofes[1]);
            segonaEstrofaUsuari = tornDelUsuari();
        }

        puntuacioRivalActual = puntuacioRivalAnterior + calcularPuntuacio(estrofes[0]) + calcularPuntuacio(estrofes[1]);
        puntuacioUsuariActual = puntuacioUsuariAnterior + calcularPuntuacio(primeraEstrofaUsuari) + calcularPuntuacio(segonaEstrofaUsuari);

        rival.setPuntuacio(puntuacioRivalActual);
        usuari.setPuntuacio(puntuacioUsuariActual);
    }

    /**
     * Mètode que demana per llegir i retorna una cadena de caracters amb la estrofa que ha introduit l'usuari per pantalla
     * @return String amb l'estrofa de l'usuari
     */
    private String tornDelUsuari(){
        String estrofa;

        Pantalla.mostraMissatge(usuari.getNickname());
        Pantalla.mostraMissatge(YOUR_TURN);

        estrofa = Pantalla.demanaLinia();

        return estrofa;
    }

    /**
     * Mètode que gestiona i demana per printar per pantalla el torn del rival
     * @param estrofa string amb la estrofa corresponent a mostrar per pantalla
     */
    private void tornDelRival(String estrofa){
        String estrofaSenceraRival = "\n" + rival.getNickname() + " your turn! Drop it!\n\n" +
                rival.getNickname() + ": \n\n" + estrofa + "\n\n";
        Pantalla.mostraMissatge(estrofaSenceraRival);
    }

    /**
     * Mètode setter del rival
     * @param rival Participant del rival de la batalla del usuari
     */
    public void setRival(Participant rival) {
        this.rival = rival;
    }

    /**
     * Mètode setter del tipus
     * @param tipus enter per cada tipus de batalla
     */
    public void setTipus(int tipus) {
        this.tipus = tipus;
    }

    /**
     * Mètode setter del usuari
     * @param usuari Participant usuari de la competició
     */
    public void setUsuari(Participant usuari) {
        this.usuari = usuari;
    }
}
