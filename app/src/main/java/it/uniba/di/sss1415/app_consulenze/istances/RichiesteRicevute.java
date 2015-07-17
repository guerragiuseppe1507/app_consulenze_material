package it.uniba.di.sss1415.app_consulenze.istances;

/**
 * Created by Valerio on 17/07/2015.
 */
public class RichiesteRicevute {

    private String data;
    private String oraInizio;
    private String oraFine;
    private String intervento;
    private String dottore;

    public RichiesteRicevute(String data, String oraInizio, String oraFine, String intervento, String dottore){

        this.data = data;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.intervento = intervento;
        this.dottore = dottore;

    }


    public String getData(){return this.data;}
    public String getOraInizio(){return this.oraInizio;}
    public String getOraFine(){return this.oraFine;}
    public String getIntervento(){return this.intervento;}
    public String getDottore(){return this.dottore;}

}
