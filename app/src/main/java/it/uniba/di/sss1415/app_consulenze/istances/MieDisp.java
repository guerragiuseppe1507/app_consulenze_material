package it.uniba.di.sss1415.app_consulenze.istances;

/**
 * Created by Giuseppe on 07/07/2015.
 */
public class MieDisp {

    private String id;
    private String data;
    private String oraInizio;
    private String oraFine;
    private String intervento;
    private String ripetizione;
    private String fineRipetizione;

    public MieDisp(String id, String data, String oraInizio, String oraFine, String intervento, String ripetizione, String fineRipetizione){

        this.id = id;
        this.data = data;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.intervento = intervento;
        this.ripetizione = ripetizione;
        this.fineRipetizione = fineRipetizione;

    }

    public String getId(){return this.id;}
    public String getData(){return this.data;}
    public String getOraInizio(){return this.oraInizio;}
    public String getOraFine(){return this.oraFine;}
    public String getIntervento(){return this.intervento;}
    public String getRipetizione(){return this.ripetizione;}
    public String getFineRipetizione(){return this.fineRipetizione;}

}
