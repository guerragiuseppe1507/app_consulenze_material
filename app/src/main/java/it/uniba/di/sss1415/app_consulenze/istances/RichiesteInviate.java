package it.uniba.di.sss1415.app_consulenze.istances;

/**
 * Created by Valerio on 17/07/2015.
 */
public class RichiesteInviate {

    private String id;
    private String data;
    private String oraInizio;
    private String oraFine;
    private String intervento;
    private String nomeTutor;
    private String cognomeTutor;
    private String percorso;

    public RichiesteInviate(String id, String data, String oraInizio, String oraFine, String intervento, String nomeTutor,
                            String cognomeTutor, String percorso){

        this.id = id;
        this.data = data;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;
        this.intervento = intervento;
        this.nomeTutor = nomeTutor;
        this.cognomeTutor = cognomeTutor;
        this.percorso = percorso;

    }

    public String getId(){return this.id;}
    public String getData(){return this.data;}
    public String getOraInizio(){return this.oraInizio;}
    public String getOraFine(){return this.oraFine;}
    public String getIntervento(){return this.intervento;}
    public String getNomeTutor(){return this.nomeTutor;}
    public String getCognomeTutor() {return this.cognomeTutor;}
    public String getPercorso(){return this.percorso;}


}
