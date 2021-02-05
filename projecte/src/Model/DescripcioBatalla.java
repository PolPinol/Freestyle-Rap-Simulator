package Model;

import com.google.gson.annotations.SerializedName;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Classe que guarda tots els temes possibles a una batalla
 */
public class DescripcioBatalla {
    /**
     * Constant que descriu si el rapero es queda en blanc en una estrofa
     */
    public static final String ESTROFA_EN_BLANC = "Mmmmmmm...pffff...";

    @SerializedName("themes")
    private ArrayList<Tema> temes;

    /**
     * Constructor de la classe DescripcioBatalla
     */
    public DescripcioBatalla(){
        this.temes = new ArrayList<>();
    }

    /**
     * Mètode que retorna un tema aleatori.
     * @return String d'un tema aleatori.
     */
    public String getRandomTema() {
        String tema;
        int randomTema;
        int nombreDeTemes;

        nombreDeTemes = temes.size();
        randomTema = (int) Math.floor(Math.random() * nombreDeTemes);
        tema = temes.get(randomTema).getNom();

        return tema;
    }

    /**
     * Mètode que retorna dues estrofes aleatoriament de la llista que es té.
     * @param nivellExpertesaRival enter que descriu el nivell d'un rival
     * @param tema String del tema
     * @return retorna dues estrofes aleatories de la llista
     */
    public String[] getRandomEstrofes(int nivellExpertesaRival, String tema) {
        String[] estrofes = new String[2];
        int nombreDeEstrofes;
        int indexDelTema = getIndexDelTema(tema);

        if (nivellExpertesaRival == 1) {
            nombreDeEstrofes = temes.get(indexDelTema).getEstrofes().get(0).getFirst().size();
            if (nombreDeEstrofes == 0) {
                estrofes[0] = ESTROFA_EN_BLANC;
                estrofes[1] = ESTROFA_EN_BLANC;
            } else if (nombreDeEstrofes == 1) {
                estrofes[0] = temes.get(indexDelTema).getEstrofes().get(0).getFirst().get(0);
                estrofes[1] = ESTROFA_EN_BLANC;
            } else {
                int randomEstrofa = (int) Math.floor(Math.random() * nombreDeEstrofes);
                estrofes[0] = temes.get(indexDelTema).getEstrofes().get(0).getFirst().get(randomEstrofa);
                do {
                    randomEstrofa = (int) Math.floor(Math.random() * nombreDeEstrofes);
                    estrofes[1] = temes.get(indexDelTema).getEstrofes().get(0).getFirst().get(randomEstrofa);
                } while (estrofes[1].equals(estrofes[0]));
            }
        } else {
            nombreDeEstrofes = temes.get(indexDelTema).getEstrofes().get(0).getSecond().size();
            if (nombreDeEstrofes == 0) {
                estrofes[0] = ESTROFA_EN_BLANC;
                estrofes[1] = ESTROFA_EN_BLANC;
            } else if (nombreDeEstrofes == 1) {
                estrofes[0] = temes.get(indexDelTema).getEstrofes().get(0).getSecond().get(0);
                estrofes[1] = ESTROFA_EN_BLANC;
            } else {
                int randomEstrofa = (int) Math.floor(Math.random() * nombreDeEstrofes);
                estrofes[0] = temes.get(indexDelTema).getEstrofes().get(0).getSecond().get(randomEstrofa);
                do {
                    randomEstrofa = (int) Math.floor(Math.random() * nombreDeEstrofes);
                    estrofes[1] = temes.get(indexDelTema).getEstrofes().get(0).getSecond().get(randomEstrofa);
                } while (estrofes[1].equals(estrofes[0]));
            }
        }
        return estrofes;
    }

    /**
     * Mètode que retorna el índex del arrayList del tema entrat.
     * @param tema nom del tema
     * @return enter que descriu el índex del arrayList del tema
     */
    public int getIndexDelTema(String tema) {
        for (int i = 0; i < temes.size(); i++) {
            if (tema.equals(temes.get(i).getNom())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Mètode que retorna un String que importa el text de "batalles.json"
     * @return text.toString() String del text del json
     */
    public String importarTextBatalles() {
        StringBuilder text = new StringBuilder();
        File arxiu = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            arxiu = new File ("batalles.json");
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
}
