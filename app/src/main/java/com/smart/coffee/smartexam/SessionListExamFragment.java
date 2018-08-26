package com.smart.coffee.smartexam;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.LinkedList;

public class SessionListExamFragment extends Fragment implements View.OnClickListener{

    FloatingActionButton fab;
    Exam esame;
    LinkedList<Session> sessioni;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ExamActivity activity = (ExamActivity) getActivity();
        esame = activity.getCurrentExam();
        sessioni = esame.getSessions();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View sessionsView =
                inflater.inflate(R.layout.session_list_exam_fragment, container, false);

        FloatingActionButton fab = (FloatingActionButton) sessionsView.findViewById(R.id.new_session_fab);
        fab.setOnClickListener(this);

        ListView sessionsList = (ListView) sessionsView.findViewById(R.id.sessions_listview);
        if(sessioni.size()>0) {
            SessionsAdapter sessions = new SessionsAdapter(getActivity(), esame.getSessions());
            sessionsList.setAdapter(sessions);

            sessionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    // a seconda della sessione premuta fai vedere quelle info
                    Intent intent = new Intent(getActivity(), InfoSessionActivity.class);
                    intent.putExtra("esameId", esame.getExamId());
                    intent.putExtra("sessioneId", sessioni.get(i).getSession_id());
                    startActivity(intent);
                }
            });
        }else{
            ViewGroup parentView = (ViewGroup) sessionsList.getParent();
            parentView.removeView(sessionsView);
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = layoutInflater.inflate(R.layout.void_sessions_list , parentView);
        }
        return sessionsView;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.new_session_fab:
                esame = ((ExamActivity) getActivity()).getCurrentExam();
                Intent intent = new Intent(getContext(), SessionDetailsActivity.class);
                intent.putExtra("esameId", esame.getExamId());
                startActivity(intent);
                break;
        }
    }

    public interface fromSessionListExamFragment{
        void updateExamsSessionsWith(Session s);    // manda una sessione da salvare
    }
}
