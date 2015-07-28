package it.uniba.di.sss1415.app_consulenze.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import app_consulenze_material.R;
import it.uniba.di.sss1415.app_consulenze.activity.MainActivity;
import it.uniba.di.sss1415.app_consulenze.istances.MieDisp;
import it.uniba.di.sss1415.app_consulenze.istances.UserSessionInfo;
import it.uniba.di.sss1415.app_consulenze.util.RecyclerViewClickListener;

/**
 * Created by Giuseppe on 07/07/2015.
 */
public class DispListAdapter extends  RecyclerView.Adapter<DispListAdapter.MieDispHolder> {

    private Context context;
    private ArrayList<MieDisp> items;
    private RecyclerViewClickListener itemListener;
    private int clickedPos;


    private Button btnEdit;
    private Button btnDelete;
    private LinearLayout layoutButtons;

    public DispListAdapter(Context context, ArrayList<MieDisp> items, RecyclerViewClickListener listener, int clickedPos) {
        this.context = context;
        this.items = items;
        this.itemListener = listener;
        this.clickedPos=clickedPos;
    }

    // Create new views (invoked by the layrout manage)
    @Override
    public MieDispHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_miadisp, parent, false);
        MieDispHolder viewHolder = new MieDispHolder(context, view);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MieDispHolder viewHolder, int position) {
        MieDisp disp = items.get(position);
        viewHolder.tvData.setText(disp.getData());
        viewHolder.tvOraInizio.setText(disp.getOraInizio());
        viewHolder.tvOraFine.setText(disp.getOraFine());
        viewHolder.tvIntervento.setText(disp.getIntervento());

        if(disp.getRipetizione().equals("")){
            viewHolder.tvRipetizioneLabel.setVisibility(View.GONE);
            viewHolder.tvRipetizione.setVisibility(View.GONE);
            viewHolder.tvFineRipetizioneLabel.setVisibility(View.GONE);
            viewHolder.tvFineRipetizione.setVisibility(View.GONE);

        } else {
            viewHolder.tvRipetizioneLabel.setVisibility(View.VISIBLE);
            viewHolder.tvRipetizione.setText(disp.getRipetizione());
            viewHolder.tvFineRipetizioneLabel.setVisibility(View.VISIBLE);
            viewHolder.tvFineRipetizione.setText(disp.getFineRipetizione());
        }

        if(this.clickedPos == position){
            viewHolder.selectedDispon.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            final String id = disp.getId();
            final String data = disp.getData();
            final String orainizio = disp.getOraInizio();
            final String orafine = disp.getOraFine();
            final String intervento = disp.getIntervento();
            final String ripetizione = disp.getRipetizione();
            final String fineripetizione = disp.getFineRipetizione();

            UserSessionInfo.miaDispScelta = new MieDisp(id,data,orainizio,orafine,intervento,ripetizione,fineripetizione);

            btnEdit.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);

        } else {

            layoutButtons.setVisibility(View.GONE);
        }

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                creaMessaggio(context.getResources().getString(R.string.availabilityDeleted));
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MieDisp m = UserSessionInfo.miaDispScelta;
                ((MainActivity) context).setMiaDispScelta(m.getId(),
                        m.getData(), m.getOraInizio(), m.getOraFine(),
                        m.getIntervento(), m.getRipetizione(), m.getFineRipetizione());
                ((MainActivity) context).showFragment("ModificaDisponibilitaFragment", false);
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MieDispHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Context context;
        public TextView tvData;
        public TextView tvOraInizio;
        public TextView tvOraFine;
        public TextView tvIntervento;
        public TextView tvRipetizione;
        public TextView tvFineRipetizione;
        public TextView tvRipetizioneLabel;
        public TextView tvFineRipetizioneLabel;
        public TextView selectedDispon;
        public CardView item;
        public LinearLayout parent;


        public MieDispHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            tvData = (TextView) itemView.findViewById(R.id.tvData);
            tvOraInizio = (TextView) itemView.findViewById(R.id.tvOraInizio);
            tvOraFine = (TextView) itemView.findViewById(R.id.tvOraFine);
            tvIntervento = (TextView) itemView.findViewById(R.id.tvIntervento);
            tvRipetizione = (TextView) itemView.findViewById(R.id.tvRipetizione);
            tvFineRipetizione = (TextView) itemView.findViewById(R.id.tvFineRipetizione);
            tvRipetizioneLabel = (TextView) itemView.findViewById(R.id.tvRipetizioneLabel);
            tvFineRipetizioneLabel = (TextView) itemView.findViewById(R.id.tvFineRipetizioneLabel);
            selectedDispon = (TextView) itemView.findViewById(R.id.selectedDispon);
            btnDelete = (Button) itemView.findViewById(R.id.bottone_cancella_miaDisp);
            btnEdit = (Button) itemView.findViewById(R.id.bottone_modifica_miaDisp);
            layoutButtons = (LinearLayout) itemView.findViewById(R.id.richieste_miedisp_confirm);
            item = (CardView) itemView.findViewById(R.id.item_miadisp);
            parent = (LinearLayout) itemView.findViewById(R.id.item_miadisp_parent);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemListener.recyclerViewClicked(v, this.getPosition(),Math.round(parent.getTop()));
        }

    }

    public void creaMessaggio(CharSequence message){

        Toast toastMessage = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toastMessage.show();
    }

}
