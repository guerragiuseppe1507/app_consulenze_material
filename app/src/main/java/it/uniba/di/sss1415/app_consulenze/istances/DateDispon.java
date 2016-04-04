package it.uniba.di.sss1415.app_consulenze.istances;


/**
 * Version 1.0
 * Created by Pasen on 15/07/2015.
 */

public class DateDispon {

    private String data;
    private String oraInizio;
    private String oraFine;
    private String nome;
    private String cognome;
    private String score;

    public DateDispon(String data, String oraInizio, String oraFine, String nome, String cognome, String score){


        this.data = data;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.nome = nome;
        this.cognome = cognome;
        this.score = score;

    }


    public String getData(){return this.data;}
    public String getOraInizio(){return this.oraInizio;}
    public String getOraFine(){return this.oraFine;}
    public String getNome(){return this.nome;}
    public String getCognome(){return this.cognome;}
    public String getScore(){return this.score;}

}
