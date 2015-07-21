package it.uniba.di.sss1415.app_consulenze.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app_consulenze_material.R;
import it.uniba.di.sss1415.app_consulenze.istances.RichiesteInviate;
import it.uniba.di.sss1415.app_consulenze.istances.RichiesteRicevute;
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
    public void onBindViewHolder(RichiesteRicevuteHolder viewHolder, int position) {
        RichiesteRicevute request = items.get(position);
        viewHolder.tvDataRequest.setText(request.getData());
        viewHolder.tvOraInizioRequest.setText(request.getOraInizio());
        viewHolder.tvOraFineRequest.setText(request.getOraFine());
        viewHolder.tvInterventoRequest.setText(request.getIntervento());
        viewHolder.tvDottoreRequest.setText(request.getDottore());
        if(this.clickedPos == position){
            viewHolder.selectedRichiesta.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        }

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
        public LinearLayout parent;


        public RichiesteRicevuteHolder(Context context, final View itemView) {
            super(itemView);
            this.context = context;
            tvDataRequest = (TextView) itemView.findViewById(R.id.tvDataRequestReceived);
            tvOraInizioRequest = (TextView) itemView.findViewById(R.id.tvOraInizioRequestReceived);
            tvOraFineRequest = (TextView) itemView.findViewById(R.id.tvOraFineRequestReceived);
            tvInterventoRequest = (TextView) itemView.findViewById(R.id.tvInterventoRequestReceived);
            tvDottoreRequest = (TextView) itemView.findViewById(R.id.tvDottoreRequestReceived);
            selectedRichiesta = (TextView) itemView.findViewById(R.id.selectedRichiestaRic);
            parent = (LinearLayout) itemView.findViewById(R.id.item_richieste_ricevute);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemListener.recyclerViewClicked(v, this.getPosition(),Math.round(parent.getTop()));
        }

    }



}