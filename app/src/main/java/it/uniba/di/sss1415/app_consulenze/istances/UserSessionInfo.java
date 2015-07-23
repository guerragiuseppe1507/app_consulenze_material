package it.uniba.di.sss1415.app_consulenze.istances;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;

/**
 * Created by Valerio on 13/07/2015.
 */
public class UserSessionInfo {

    //Dichiarazione e instanziazione di un oggetto della classe Context
    private final static UserSessionInfo instance = new UserSessionInfo();

    //Metodo che restituisce l'oggetto statico instance di tipo Context
    public static UserSessionInfo getInstance() {
        return instance;
    }

    //Instanziazione degli oggetti Email
    private String email = "";
    private ArrayList<String> branche ;
    private String pass;
    private String nome;
    private String cognome;
    private String score;
    private String provinciaIscrizione;
    private String annoIscr;
    private String numeroIscr;
    private String primariaEx; //prima expertise
    public static String interventoScelto;
    public static Tutors tutorScelto;

    //Di seguito i metodi che permettono di accedere in lettura e scrittura alla mail
    public void setParams(Context c , String dati){
        String notset = "Not set yet";

        String[] datiRicevuti = dati.toString().split(",",10);

        for(int i = 0 ; i < 8; i++){

            if(datiRicevuti[i].equals(""))datiRicevuti[i]=notset;

        }

        setEmail(datiRicevuti[0]);
        setPassword(datiRicevuti[1]);
        setScore(datiRicevuti[2]);
        setProvincia(datiRicevuti[3]);
        setNumIscr(datiRicevuti[4]);
        setAnnoIscr(datiRicevuti[5]);
        setNome(datiRicevuti[6]);
        setCognome(datiRicevuti[7]);

        if(datiRicevuti[8].equals("")){
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
            setExp(prefs.getString("exp", notset));
        }else{
            setExp(datiRicevuti[8]);
        }

    }


    public void setEmail(String input) {email = input;}

    public String getEmail(){return email;}

    public void setPassword(String input) {pass = input;}

    public String getPassword(){return pass;}

    public void setScore(String input) {score = input;}

    public String getScore(){return score;}

    public void setProvincia(String input) {provinciaIscrizione = input;}

    public String getProvincia(){return provinciaIscrizione;}

    public void setNumIscr(String input) {numeroIscr = input;}

    public String getNumIscr(){return numeroIscr;}

    public void setAnnoIscr(String input) {annoIscr = input;}

    public String getAnnoIscr(){return annoIscr;}

    public void setNome(String input) {nome = input;}

    public String getNome(){return nome;}

    public void setCognome(String input) {cognome = input;}

    public String getCognome(){return cognome;}

    public void setExp(String input) {primariaEx = input;}

    public String getExp(){return primariaEx;}



    public void setBranche(String branche){
        String[] temp =branche.split(",");
        this.branche = new ArrayList<String>();
        for (int i = 0 ; i < temp.length;i++){
            this.branche.add(temp[i]);
        }
    }

    public ArrayList<String> getBranche(){
        return branche;
    }

}