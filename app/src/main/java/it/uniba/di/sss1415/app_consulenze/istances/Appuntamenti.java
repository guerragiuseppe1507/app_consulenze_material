package it.uniba.di.sss1415.app_consulenze.istances;

/**
 * Created by Giuseppe on 13/07/2015.
 */
public class Appuntamenti {

    private String data;
    private String oraInizio;
    private String oraFine;
    private String tipoAppuntamento;
    private String intervento;
    private String dottore;

    public Appuntamenti(String data, String oraInizio, String oraFine, String tipoAppuntamento, String intervento, String dottore){

        this.data = data;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.tipoAppuntamento = tipoAppuntamento;
        this.intervento = intervento;
        this.dottore = dottore;
    }

    public String getData(){return this.data;}
    public String getOraInizio(){return this.oraInizio;}
    public String getOraFine(){return this.oraFine;}
    public String getTipo(){return this.tipoAppuntamento;};
    public String getIntervento(){return this.intervento;}
    public String getDottore(){return this.dottore;}

}
