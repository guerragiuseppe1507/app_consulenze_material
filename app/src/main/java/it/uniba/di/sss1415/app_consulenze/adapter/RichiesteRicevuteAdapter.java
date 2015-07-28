package it.uniba.di.sss1415.app_consulenze.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import app_consulenze_material.R;
import it.uniba.di.sss1415.app_consulenze.istances.RichiesteInviate;
import it.uniba.di.sss1415.app_consulenze.istances.RichiesteRicevute;
import it.uniba.di.sss1415.app_consulenze.istances.UserSessionInfo;
import it.uniba.di.sss1415.app_consulenze.util.RecyclerViewClickListener;

/**
 * Created by Valerio on 17/07/2015.
 */
public class RichiesteRicevuteAdapter extends  RecyclerView.Adapter<RichiesteRicevuteAdapter.RichiesteRicevuteHolder> {

    private Context context;
    private ArrayList<RichiesteRicevute> items;
    private static RecyclerViewClickListener itemListener;
    private int clickedPos;

    public RichiesteRicevuteAdapter(Context context, ArrayList<RichiesteRicevute> items, RecyclerViewClickListener listener, int clickedPos) {
        this.context = context;
        this.items = items;
        this.itemListener = listener;
        this.clickedPos=clickedPos;
    }

    // Create new views (invoked by the layrout manage)
    @Override
    public RichiesteRicevuteHolder onCreateViewHolder (ViewGroup parent,int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.item_richieste_ricevute, parent, false);
        RichiesteRicevuteHolder viewHolder = new RichiesteRicevuteHolder(context, view);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RichiesteRicevuteHolder viewHolder,final int position) {
        RichiesteRicevute request = items.get(position);
        viewHolder.tvDataRequest.setText(request.getData());
        viewHolder.tvOraInizioRequest.setText(request.getOraInizio());
        viewHolder.tvOraFineRequest.setText(request.getOraFine());
        viewHolder.tvInterventoRequest.setText(request.getIntervento());
        viewHolder.tvDottoreRequest.setText(request.getDottore());
        if(this.clickedPos == position){
            viewHolder.selectedRichiesta.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            viewHolder.confirm.setVisibility(View.VISIBLE);
            UserSessionInfo.posClicked = position;
        }else{
            viewHolder.confirm.setVisibility(View.GONE);
        }

        viewHolder.bottoneAccetta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removeAt(position);

            }
        });

        viewHolder.bottoneDeclina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removeAt(position);
            }
        });

    }

    public void removeAt(int position) {
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class RichiesteRicevuteHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Context context;
        public TextView tvDataRequest;
        public TextView tvOraInizioRequest;
        public TextView tvOraFineRequest;
        public TextView tvInterventoRequest;
        public TextView tvDottoreRequest;
        public TextView selectedRichiesta;
        public CardView item;
        private LinearLayout parent;
        private LinearLayout confirm;
        private Button bottoneAccetta;
        private Button bottoneDeclina;
        private int top;


        public RichiesteRicevuteHolder(Context context, final View itemView) {
            super(itemView);
            this.context = context;
            tvDataRequest = (TextView) itemView.findViewById(R.id.tvDataRequestReceived);
            tvOraInizioRequest = (TextView) itemView.findViewById(R.id.tvOraInizioRequestReceived);
            tvOraFineRequest = (TextView) itemView.findViewById(R.id.tvOraFineRequestReceived);
            tvInterventoRequest = (TextView) itemView.findViewById(R.id.tvInterventoRequestReceived);
            tvDottoreRequest = (TextView) itemView.findViewById(R.id.tvDottoreRequestReceived);
            selectedRichiesta = (TextView) itemView.findViewById(R.id.selectedRichiestaRic);
            item = (CardView) itemView.findViewById(R.id.item_richieste_ricevute);
            parent = (LinearLayout) itemView.findViewById(R.id.item_richieste_ricevute_parent);
            top = parent.getTop();
            confirm = (LinearLayout) itemView.findViewById(R.id.richieste_ricevute_confirm);
            bottoneAccetta = (Button) itemView.findViewById(R.id.bottone_accetta_richiesta);
            bottoneDeclina = (Button) itemView.findViewById(R.id.bottone_rifiuta_richiesta);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(this.getPosition() == 0){
                itemListener.recyclerViewClicked(v, this.getPosition(),Math.round(parent.getTop()));
            }else if (this.getPosition() < UserSessionInfo.posClicked){
                itemListener.recyclerViewClicked(v, this.getPosition(),Math.round(parent.getTop()));
            } else {
                itemListener.recyclerViewClicked(v, this.getPosition(),Math.round(parent.getTop())-175);
            }
        }

    }



}