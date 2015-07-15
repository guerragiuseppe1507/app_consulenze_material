package it.uniba.di.sss1415.app_consulenze.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import app_consulenze_material.R;
import it.uniba.di.sss1415.app_consulenze.istances.DateDispon;

/**
 * Created by Pasen on 15/07/2015.
 */
public class DateDisponAdapter extends  RecyclerView.Adapter<DateDisponAdapter.DateDispHolder>  {
    private Context context;
    private ArrayList<DateDispon> items;

    public DateDisponAdapter(Context context, ArrayList<DateDispon> items) {
        this.context = context;
        this.items = items;
    }

    // Create new views (invoked by the layrout manage)
    @Override
    public DateDispHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_datadispon, parent, false);
        DateDispHolder viewHolder = new DateDispHolder(context, view);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(DateDispHolder viewHolder, int position) {
        DateDispon disp = items.get(position);
        viewHolder.tvData.setText(disp.getData());
        viewHolder.tvOraInizio.setText(disp.getOraInizio());
        viewHolder.tvOraFine.setText(disp.getOraFine());
        viewHolder.tvNome.setText(disp.getNome());
        viewHolder.tvCognome.setText(disp.getCognome());
        viewHolder.tvScore.setText(disp.getScore());





    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class DateDispHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Context context;
        public TextView tvData;
        public TextView tvOraInizio;
        public TextView tvOraFine;
        public TextView tvNome;
        public TextView tvCognome;
        public TextView tvScore;


        public DateDispHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            tvData = (TextView) itemView.findViewById(R.id.tvDataDisp);
            tvOraInizio = (TextView) itemView.findViewById(R.id.tvOraInizioDisp);
            tvOraFine = (TextView) itemView.findViewById(R.id.tvOraFineDisp);
            tvNome = (TextView) itemView.findViewById(R.id.tvNomeTDisp);
            tvCognome = (TextView) itemView.findViewById(R.id.tvCongnomeDisp);
            tvScore = (TextView) itemView.findViewById(R.id.tvScoreTDisp);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, tvData.getText().toString(), Toast.LENGTH_SHORT).show();
        }

    }
}
