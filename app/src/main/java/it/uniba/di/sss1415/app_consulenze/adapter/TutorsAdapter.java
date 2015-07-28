package it.uniba.di.sss1415.app_consulenze.adapter;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app_consulenze_material.R;
import it.uniba.di.sss1415.app_consulenze.fragment.TutorFragment;
import it.uniba.di.sss1415.app_consulenze.fragment.TutorRating;
import it.uniba.di.sss1415.app_consulenze.istances.Tutors;
import it.uniba.di.sss1415.app_consulenze.istances.UserSessionInfo;
import it.uniba.di.sss1415.app_consulenze.util.RecyclerViewClickListener;

/**
 * Created by Pasen on 21/07/2015.
 */
public class TutorsAdapter  extends  RecyclerView.Adapter<TutorsAdapter.TutorsHolder>  {

    private Context context;
    private ArrayList<Tutors> items;
    private RecyclerViewClickListener itemListener;
    private int clickedPos;
    TutorFragment fr;

    TutorRating dialogRate;


    public TutorsAdapter(Context context, ArrayList<Tutors> items, RecyclerViewClickListener listener, int clickedPos, TutorFragment fr) {
        this.context = context;
        this.items = items;
        this.itemListener = listener;
        this.clickedPos=clickedPos;
        this.fr = fr;

    }

    // Create new views (invoked by the layrout manage)
    @Override
    public TutorsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tutor, parent, false);
        TutorsHolder viewHolder = new TutorsHolder(context, view);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(TutorsHolder viewHolder, int position) {
        Tutors tutor = items.get(position);
        viewHolder.tvNome.setText(tutor.getNome());
        viewHolder.tvCognome.setText(tutor.getCognome());
        viewHolder.tvScore.setText(tutor.getScore());

        if(this.clickedPos == position){
            viewHolder.selectedTutor.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            UserSessionInfo.posClicked = position;
            viewHolder.tutorOp.setVisibility((View.VISIBLE));
        } else {
            viewHolder.tutorOp.setVisibility((View.GONE));
        }

        viewHolder.buttonRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Tutors t = UserSessionInfo.tutorScelto;
                dialogRate = TutorRating.newInstance(t.getNome(), t.getCognome(), t.getScore());
                FragmentTransaction ft = fr.getFragmentManager().beginTransaction();
                dialogRate.show(ft, "Rate");

            }
        });

        viewHolder.buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                creaMessaggio("TO-DO");
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class TutorsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public Button buttonProfile;
        public Button buttonRate;
        public LinearLayout tutorOp;
        private Context context; // ??
        public TextView tvNome;
        public TextView tvCognome;
        public TextView tvScore;
        public TextView selectedTutor;
        public LinearLayout parent;


        public TutorsHolder(Context context, View itemView) {
            super(itemView);
            this.context = context; // ??
            tvNome = (TextView) itemView.findViewById(R.id.tvNomeT);
            tvCognome = (TextView) itemView.findViewById(R.id.tvCognomeT);
            tvScore = (TextView) itemView.findViewById(R.id.tvScoreT);
            selectedTutor = (TextView) itemView.findViewById(R.id.selectedTutor);
            parent = (LinearLayout) itemView.findViewById(R.id.item_valuta_tutor_parent);
            tutorOp = (LinearLayout) itemView.findViewById(R.id.tutor_op);
            buttonRate = (Button) itemView.findViewById(R.id.rateBtn);
            buttonProfile = (Button) itemView.findViewById(R.id.viewProfileBtn);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            UserSessionInfo.tutorScelto = new Tutors(tvNome.getText().toString(),tvCognome.getText().toString(),tvScore.getText().toString());
            if(this.getPosition() == 0){
                itemListener.recyclerViewClicked(v, this.getPosition(),Math.round(parent.getTop()));
            }else if (this.getPosition() < UserSessionInfo.posClicked){
                itemListener.recyclerViewClicked(v, this.getPosition(),Math.round(parent.getTop()));
            } else {
                itemListener.recyclerViewClicked(v, this.getPosition(),Math.round(parent.getTop())-175);
            }
        }

    }

    public void creaMessaggio(CharSequence message){
        Toast toastMessage = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toastMessage.show();
    }

}
