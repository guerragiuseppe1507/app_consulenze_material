package it.uniba.di.sss1415.app_consulenze.istances;


/**
 * Version 1.0
 * Created by Pasen on 21/07/2015.
 */

public class Tutors {
    private String nomeT;
    private String cognomeT;
    private String scoreT;


    public Tutors(String nomeT, String cognomeT, String scoreT){

        this.nomeT = nomeT;
        this.cognomeT = cognomeT;
        this.scoreT = scoreT;


    }

    public String getNome(){return this.nomeT;}
    public String getCognome(){return this.cognomeT;}
    public String getScore(){return this.scoreT;}

}
