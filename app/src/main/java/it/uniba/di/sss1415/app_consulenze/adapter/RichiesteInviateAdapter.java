package it.uniba.di.sss1415.app_consulenze.adapter;

import android.content.Context;
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
import it.uniba.di.sss1415.app_consulenze.activity.MainActivity;
import it.uniba.di.sss1415.app_consulenze.istances.RichiesteInviate;
import it.uniba.di.sss1415.app_consulenze.util.RecyclerViewClickListener;

/**
 * Created by Valerio on 17/07/2015.
 */
public class RichiesteInviateAdapter extends  RecyclerView.Adapter<RichiesteInviateAdapter.RichiesteInviateHolder> {

    private Context context;
    private ArrayList<RichiesteInviate> items;
    private RecyclerViewClickListener itemListener;
    private int clickedPos;

    public RichiesteInviateAdapter(Context context, ArrayList<RichiesteInviate> items, RecyclerViewClickListener listener, int clickedPos) {
        this.context = context;
        this.items = items;
        this.itemListener = listener;
        this.clickedPos=clickedPos;
    }

     // Create new views (invoked by the layout manager)
     @Override
     public RichiesteInviateHolder onCreateViewHolder (ViewGroup parent,int viewType){
     View view = LayoutInflater.from(context).inflate(R.layout.item_richieste_inviate, parent, false);
     RichiesteInviateHolder viewHolder = new RichiesteInviateHolder(context, view);
     return viewHolder;
     }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RichiesteInviateHolder viewHolder,final int position) {
        RichiesteInviate request = items.get(position);
        viewHolder.tvDataRequest.setText(request.getData());
        viewHolder.tvOraInizioRequest.setText(request.getOraInizio());
        viewHolder.tvOraFineRequest.setText(request.getOraFine());
        viewHolder.tvInterventoRequest.setText(request.getIntervento());
        if(request.getNomeTutor().equals("")){
            viewHolder.tvNomeTutorRequest.setVisibility(View.GONE);
            viewHolder.tvCognomeTutorRequest.setVisibility(View.GONE);
        }else{
            viewHolder.tvNomeTutorRequest.setText(request.getNomeTutor());
            viewHolder.tvCognomeTutorRequest.setText(request.getCognomeTutor());
        }

        if(this.clickedPos == position){
            viewHolder.selectedRichiesta.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            viewHolder.confirmDialog.setVisibility(View.VISIBLE);
        } else{
            viewHolder.confirmDialog.setVisibility(View.GONE);
        }

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewHolder.parent.setVisibility(View.GONE);
                removeAt(position);
            }
        });

        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creaMessaggio("TO-DO");
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

    public class RichiesteInviateHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Context context;
        public TextView tvDataRequest;
        public TextView tvOraInizioRequest;
        public TextView tvOraFineRequest;
        public TextView tvInterventoRequest;
        public TextView tvNomeTutorRequest;
        public TextView tvCognomeTutorRequest;
        public TextView selectedRichiesta;
        public CardView item;
        public LinearLayout parent;
        public LinearLayout confirmDialog;
        public Button btnEdit;
        public Button btnDelete;

        public RichiesteInviateHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            tvDataRequest = (TextView) itemView.findViewById(R.id.tvDataRequest);
            tvOraInizioRequest = (TextView) itemView.findViewById(R.id.tvOraInizioRequest);
            tvOraFineRequest = (TextView) itemView.findViewById(R.id.tvOraFineRequest);
            tvInterventoRequest = (TextView) itemView.findViewById(R.id.tvInterventoRequest);
            tvNomeTutorRequest = (TextView) itemView.findViewById(R.id.tvNomeTutorRequest);
            tvCognomeTutorRequest = (TextView) itemView.findViewById(R.id.tvCognomeTutorRequest);
            selectedRichiesta = (TextView) itemView.findViewById(R.id.selectedRichiestaInv);
            item = (CardView) itemView.findViewById(R.id.cv_richiesta_inviata);
            parent = (LinearLayout) itemView.findViewById(R.id.item_richieste_inviate);
            confirmDialog = (LinearLayout) itemView.findViewById(R.id.richieste_inviate_confirm);
            btnEdit = (Button) itemView.findViewById(R.id.bottone_modifica_richiesta_inviata);
            btnDelete = (Button) itemView.findViewById(R.id.bottone_cancella_richiesta_ricevuta);

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
