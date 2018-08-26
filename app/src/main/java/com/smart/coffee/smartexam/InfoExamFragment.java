package com.smart.coffee.smartexam;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.LinkedList;

public class InfoExamFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View infoExamView =
                inflater.inflate(R.layout.info_exam_fragment, container, false);

        ExamActivity activity = (ExamActivity) getActivity();
        Exam esame = activity.getCurrentExam();

        TextView nameTextView = (TextView) infoExamView.findViewById(R.id.exam_name);
        nameTextView.setText(esame.getExamName());

        TextView dateTextView = (TextView) infoExamView.findViewById(R.id.exam_date);
        int day,month,year;
        day = esame.getExamDate().get(Calendar.DAY_OF_MONTH);
        month = esame.getExamDate().get(Calendar.MONTH);
        year = esame.getExamDate().get(Calendar.YEAR);
        if(esame.getExamDate() != null) {
            dateTextView.setText("Data dell'esame "+day+"/"+month+"/"+year);
        }

        TextView pagesTextView = (TextView) infoExamView.findViewById(R.id.exam_pages);
        if(esame.getPageNumber() != 0){
            pagesTextView.setText(""+esame.getPageNumber());
        }

        int donePages=0;
        LinkedList<Session> sessioni = esame.getSessions();
        for(Session s:sessioni){
            if(s.isBeenCompleted()){
                Log.i("fatte",s.getPagesCompleted()+"");
                donePages+=s.getPagesCompleted();
            }
        }
        TextView pagesDone = (TextView) infoExamView.findViewById(R.id.pages_done);
        pagesDone.setText(""+donePages);


        int prcnt = (int) ((float) donePages/(float) esame.getPageNumber()*100);
        TextView percView = (TextView) infoExamView.findViewById(R.id.percentage_pages);
        if(prcnt>100){
            percView.setText("100%");
        }else{
            percView.setText(prcnt+"%");
        }

        TextView notesTextView = (TextView) infoExamView.findViewById(R.id.exam_notes);
        if(esame.getNote().length() != 0){
            notesTextView.setText(esame.getNote());
        }else{
            notesTextView.setText("Non ci sono note da visualizzare.");
            notesTextView.setAlpha((float) 0.4);
        }
        return infoExamView;
    }


    public interface fromInfoFragment{
        void updateExamWith(Exam e);    // manda i dati aggiornati dell'esame

        Exam getCurrentExam(); // chiede esame corrente all'activity
    }

}
