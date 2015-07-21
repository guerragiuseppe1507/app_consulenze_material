package it.uniba.di.sss1415.app_consulenze.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app_consulenze_material.R;
import it.uniba.di.sss1415.app_consulenze.istances.Appuntamenti;
import it.uniba.di.sss1415.app_consulenze.istances.MieDisp;
import it.uniba.di.sss1415.app_consulenze.util.RecyclerViewClickListener;

/**
 * Created by Giuseppe on 13/07/2015.
 */
public class AppuntamentiAdapter extends  RecyclerView.Adapter<AppuntamentiAdapter.AppuntamentiHolder> {

    private Context context;
    private ArrayList<Appuntamenti> items;
    private RecyclerViewClickListener itemListener;
    private int clickedPos;

    public AppuntamentiAdapter(Context context, ArrayList<Appuntamenti> items, RecyclerViewClickListener listener, int clickedPos) {
        this.context = context;
        this.items = items;
        this.itemListener = listener;
        this.clickedPos=clickedPos;
    }

    // Create new views (invoked by the layrout manage)
    @Override
    public AppuntamentiHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_appuntamenti, parent, false);
        AppuntamentiHolder viewHolder = new AppuntamentiHolder(context, view);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(AppuntamentiHolder viewHolder, int position) {
        Appuntamenti appuntamenti = items.get(position);
        viewHolder.tvData.setText(appuntamenti.getData());
        viewHolder.tvOraInizio.setText(appuntamenti.getOraInizio());
        viewHolder.tvOraFine.setText(appuntamenti.getOraFine());
        viewHolder.tvTipoAppuntamento.setText(appuntamenti.getTipo());
        viewHolder.tvIntervento.setText(appuntamenti.getIntervento());
        viewHolder.tvDottore.setText(appuntamenti.getDottore());
        if(this.clickedPos == position){
            viewHolder.selectedAppuntamento.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        }


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class AppuntamentiHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Context context;
        public TextView tvData;
        public TextView tvOraInizio;
        public TextView tvOraFine;
        public TextView tvTipoAppuntamento;
        public TextView tvIntervento;
        public TextView tvDottore;
        public TextView selectedAppuntamento;
        public LinearLayout parent;


        public AppuntamentiHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            tvData = (TextView) itemView.findViewById(R.id.appuntamenti_tvData);
            tvOraInizio = (TextView) itemView.findViewById(R.id.appuntamenti_tvOraInizio);
            tvOraFine = (TextView) itemView.findViewById(R.id.appuntamenti_tvOraFine);
            tvTipoAppuntamento = (TextView) itemView.findViewById(R.id.appuntamenti_tvTipoAppuntamento);
            tvIntervento = (TextView) itemView.findViewById(R.id.appuntamenti_tvIntervento);
            tvDottore = (TextView) itemView.findViewById(R.id.appuntamenti_tvDottore);
            selectedAppuntamento = (TextView) itemView.findViewById(R.id.selectedAppuntamento);
            parent = (LinearLayout) itemView.findViewById(R.id.item_appuntamenti);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemListener.recyclerViewClicked(v, this.getPosition(),Math.round(parent.getTop()));
        }

    }

}
