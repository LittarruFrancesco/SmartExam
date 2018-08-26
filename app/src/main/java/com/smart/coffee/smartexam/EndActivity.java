package com.smart.coffee.smartexam;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.LinkedList;

public class EndActivity extends AppCompatActivity {

    TextView examName;
    TextView sessionTitle;
    TextView sessionStart;
    TextView sessionDuration;
    TextView sessionPages;

    Exam esame;
    Session currentSession;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.save_end_session, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        Toolbar toolbar = findViewById(R.id.end_session_actionbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Riepilogo Sessione");

        // Prendi id di esame e sessione per caricarli
        Intent intent = getIntent();
        String esameId = intent.getStringExtra("esameId");
        String sessioneId = intent.getStringExtra("sessioneId");

        examName = (TextView) findViewById(R.id.session_exam_title);
        sessionTitle = (TextView) findViewById(R.id.end_info_title);
        sessionStart = (TextView) findViewById(R.id.end_info_start);
        sessionDuration = (TextView) findViewById(R.id.end_info_duration);
        sessionPages = (TextView) findViewById(R.id.end_info_pages);

        TextInputEditText pagesCompleted = (TextInputEditText) findViewById(R.id.end_session_pages_edit_text);
        final TextInputLayout til_pagesCompleted = (TextInputLayout) findViewById(R.id.til_end_session_pages);
        pagesCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                til_pagesCompleted.setError(null);
            }
        });

        TextInputEditText notes = (TextInputEditText) findViewById(R.id.end_session_notes_edit_text);
        final TextInputLayout til_notes = (TextInputLayout) findViewById(R.id.til_end_session_notes);
        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                til_notes.setError(null);
            }
        });

        // ---------- Carica esame e sessione corrente ----------
        LinkedList<Exam> esami = Saver.loadExams(getApplicationContext(), "esamiFile");
        int eIdx=0;
        for(Exam e: esami){
            if(e.getExamId().equals(esameId)){
                break;
            }eIdx++;
        }
        esame = esami.get(eIdx);

        LinkedList<Session> sessioni = esame.getSessions();
        currentSession=null;
        for(Session s: sessioni){
            if(s.getSession_id().equals(sessioneId)){
                currentSession = s;
                break;
            }
        }

        // Popola il form per richiedere l'esito della sessione
        examName.setText(esame.getExamName());
        sessionTitle.setText(currentSession.getTitle());
        sessionStart.setText(
                currentSession.getHour()+":"+
                currentSession.getMinute());
        sessionDuration.setText(currentSession.getDuration());
        sessionPages.setText(""+currentSession.getPages());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.save_end_session_data:

                TextInputEditText pagesCompleted = (TextInputEditText) findViewById(R.id.end_session_pages_edit_text);
                TextInputLayout til_pagesCompleted = (TextInputLayout) findViewById(R.id.til_end_session_pages);
                final int pages;
                int flag = 0;
                try{
                    flag = Integer.parseInt(pagesCompleted.getText().toString());
                }catch (NumberFormatException e){flag=0; }
                pages=flag;
                if(pages == 0){
                    til_pagesCompleted.setError("Campo obbligatorio");
                    return true;
                }

                int pagine = Integer.parseInt(pagesCompleted.getText().toString());
                TextInputEditText notes_et = (TextInputEditText) findViewById(R.id.end_session_notes_edit_text);
                String notes = notes_et.getText().toString();
                LinkedList<Exam> esami = Saver.loadExams(getApplicationContext(), "esamiFile");
                for(Exam e: esami){
                    if(e.getExamId().equals(esame.getExamId())){
                        e.getSessionById(currentSession.getSession_id()).setCompleted();
                        e.getSessionById(currentSession.getSession_id()).setPagesCompleted(pagine);
                        e.getSessionById(currentSession.getSession_id()).setInfoCompleted(notes);
                    }
                }
                Boolean v = Saver.saveExams(getApplicationContext(),"esamiFile", esami);

                Intent intent = new Intent(EndActivity.this, ExamActivity.class);
                intent.putExtra("esameId", esame.getExamId());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
