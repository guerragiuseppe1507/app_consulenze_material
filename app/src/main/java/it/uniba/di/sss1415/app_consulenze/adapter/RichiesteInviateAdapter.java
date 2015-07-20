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
import it.uniba.di.sss1415.app_consulenze.activity.MainActivity;
import it.uniba.di.sss1415.app_consulenze.istances.RichiesteInviate;

/**
 * Created by Valerio on 17/07/2015.
 */
public class RichiesteInviateAdapter extends  RecyclerView.Adapter<RichiesteInviateAdapter.RichiesteInviateHolder> {

    private Context context;
    private ArrayList<RichiesteInviate> items;

    public RichiesteInviateAdapter(Context context, ArrayList<RichiesteInviate> items) {
        this.context = context;
        this.items = items;
    }

     // Create new views (invoked by the layrout manage)
     @Override
     public RichiesteInviateHolder onCreateViewHolder (ViewGroup parent,int viewType){
     View view = LayoutInflater.from(context).inflate(R.layout.item_richieste_inviate, parent, false);
     RichiesteInviateHolder viewHolder = new RichiesteInviateHolder(context, view);
     return viewHolder;
     }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RichiesteInviateHolder viewHolder, int position) {
        RichiesteInviate request = items.get(position);
        viewHolder.tvDataRequest.setText(request.getData());
        viewHolder.tvOraInizioRequest.setText(request.getOraInizio());
        viewHolder.tvOraFineRequest.setText(request.getOraFine());
        viewHolder.tvInterventoRequest.setText(request.getIntervento());
        viewHolder.tvNomeTutorRequest.setText(request.getNomeTutor());
        viewHolder.tvCognomeTutorRequest.setText(request.getCognomeTutor());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class RichiesteInviateHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Context context;
        public TextView tvDataRequest;
        public TextView tvOraInizioRequest;
        public TextView tvOraFineRequest;
        public TextView tvInterventoRequest;
        public TextView tvNomeTutorRequest;
        public TextView tvCognomeTutorRequest;



        public RichiesteInviateHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            tvDataRequest = (TextView) itemView.findViewById(R.id.tvDataRequest);
            tvOraInizioRequest = (TextView) itemView.findViewById(R.id.tvOraInizioRequest);
            tvOraFineRequest = (TextView) itemView.findViewById(R.id.tvOraFineRequest);
            tvInterventoRequest = (TextView) itemView.findViewById(R.id.tvInterventoRequest);
            tvNomeTutorRequest = (TextView) itemView.findViewById(R.id.tvNomeTutorRequest);
            tvCognomeTutorRequest = (TextView) itemView.findViewById(R.id.tvCognomeTutorRequest);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, tvDataRequest.getText().toString(), Toast.LENGTH_SHORT).show();
        }

    }



}
