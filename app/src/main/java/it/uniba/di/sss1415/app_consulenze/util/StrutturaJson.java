package it.uniba.di.sss1415.app_consulenze.util;

/**
 * Created by Giuseppe on 04/06/2015.
 */
public class StrutturaJson {

    public StrutturaJson (){}

    public static String[] getStruct(String tipoJson){
        switch(tipoJson){
            case"appuntamenti":
                return new String[]{"data", "oraInizio", "oraFine", "tipoAppuntamento", "intervento", "dottore"};
            /* todo
            case"storico":
                return new String[]{};
                */
            case"richiesteValutare":
                return new String[]{"data", "oraInizio", "oraFine", "intervento", "dottore"};
            case"mieRichiesteInserite":
                return new String[]{"id", "data", "oraInizio", "oraFine", "intervento", "nomeTutor", "cognomeTutor", "percorso", "utente"};
            case"dispon":
                return new String[]{"id", "data", "oraInizio", "oraFine", "intervento", "ripetizione", "fineRipetizione", "utente"};
            case"tutor":
                return new String[]{"nomeT", "cognomeT", "scoreT"};
            case"dateDispon":
                return new String[]{"data", "oraInizio", "oraFine", "nomeT", "cognomeT", "scoreT"};
            case"registrazione":
                return new String[]{"email", "numeroIscrizione", "annoIscrizione","provincia", "password"};
            case"accediSistema":
                return new String[]{"email", "password"};
            case"openSexion":
                return new String[]{"email"};
            case"expertise":
                return new String[]{"nomeEx"};
            case"cambioPsw":
                return new String[]{"email", "nuovaP"};
            case"modificaP":
                return new String[]{"email", "nome", "cognome", "provincia", "anno", "numero", "primaEx", "altreEx"};
            case"interventiMedici":
                return new String[]{"campoMedico"};
        }
        throw new IllegalArgumentException(tipoJson);
    }

    public static String getReturnStructName(String nomeRichiesta) {

        switch (nomeRichiesta) {
            case "appuntamenti":
                return "appuntamenti";
            case "richiesteValutare":
                return "richieste";
            case "mieRichiesteInserite":
                return "richiesteUtente";
            case "dispon":
                return "disponibilita";
            case "tutor":
                return "tutor";
            case "dateDispon":
                return "dateDisponibili";
        }

        throw new IllegalArgumentException(nomeRichiesta);
    }

    public static String[] getReturnStruct(String returnStructName ){

        switch(returnStructName){
            case"appuntamenti":
                return new String[]{"data", "oraInizio", "oraFine", "tipoAppuntamento", "intervento", "dottore"};
            /* todo
            case"storico":
                return new String[]{};
                */
            case"richieste":
                return new String[]{"data", "oraInizio", "oraFine", "intervento", "dottore"};
            case"richiesteUtente":
                return new String[]{"id", "data", "oraInizio", "oraFine", "intervento", "nomeTutor", "cognomeTutor", "percorso", "utente"};
            case"disponibilita":
                return new String[]{"id", "data", "oraInizio", "oraFine", "intervento", "ripetizione", "fineRipetizione"};
            case"tutor":
                return new String[]{"nomeT", "cognomeT", "scoreT"};
            case"dateDisponibili":
                return new String[]{"data", "oraInizio", "oraFine", "nomeT", "cognomeT", "scoreT"};

            //TO-DO Altri casi di possibili risposte da verificare.
        }
        throw new IllegalArgumentException(returnStructName);
    }



}
