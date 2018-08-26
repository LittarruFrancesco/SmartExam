package com.smart.coffee.smartexam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Informazioni di riepilogo sulla sessione di studio selezionata
 * Sia informazioni di contesto sull'esame e sulla sua percentuale di completamento globale,
 * sia informazioni sulla sessione in modo ordinario.
 *
 * */
public class InfoSessionActivity extends AppCompatActivity {

    private String esameId;
    private String sessioneId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_session);
        Toolbar toolbar = (Toolbar) findViewById(R.id.info_session_actionbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Sessione");
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        esameId = intent.getStringExtra("esameId");
        sessioneId = intent.getStringExtra("sessioneId");

        // ---------- Carica esame e sessione corrente ----------
        Exam esame =
                Exam.findExamById(
                        Saver.loadExams(getApplicationContext(),"esamiFile"),
                        esameId);
        Session currentSession = esame.getSessionById(sessioneId);

        String day,month,year;
        GregorianCalendar date = esame.getExamDate();
        day = ""+date.get(Calendar.DAY_OF_MONTH);
        month = ""+date.get(Calendar.MONTH);
        year = ""+date.get(Calendar.YEAR);
        ((TextView) findViewById(R.id.info_session_exam_title)).setText(esame.getExamName());
        ((TextView) findViewById(R.id.info_session_exam_date))
                .setText("Esame il "+day+"/"+month+"/"+year);

        date = currentSession.getCalendarDate();
        day = ""+date.get(Calendar.DAY_OF_MONTH);
        month = ""+date.get(Calendar.MONTH);
        year = ""+date.get(Calendar.YEAR);
        if(currentSession.isBeenCompleted()) {
            ((TextView) findViewById(R.id.session_info_pages))
                    .setText("" + currentSession.getPagesCompleted());
            if(currentSession.getInfoCompleted().length() == 0){
                ((TextView) findViewById(R.id.session_info_notes))
                        .setText("Non sono state inserite note");
            }else{
                ((TextView) findViewById(R.id.session_info_notes))
                        .setText(currentSession.getInfoCompleted());
            }
            ((TextView) findViewById(R.id.session_info_date))
                    .setText("Completata il "+day+"/"+month+"/"+year);

        }else{
            ((TextView) findViewById(R.id.session_info_pages))
                    .setText("" + currentSession.getPages());
            if(currentSession.getInfo().length() == 0){
                ((TextView) findViewById(R.id.session_info_notes))
                        .setText("Non sono state inserite note");
            }else{
                ((TextView) findViewById(R.id.session_info_notes))
                        .setText(currentSession.getInfo());
            }
            ((TextView) findViewById(R.id.session_info_date))
                    .setText("Programmata per il "+day+"/"+month+"/"+year);

        }

        int prcnt = (int) ((float) currentSession.getPages()/(float) esame.getPageNumber()*100);
        TextView percView = (TextView) findViewById(R.id.percentage_pages);
        Log.i("percentuale", ""+prcnt);
        Log.i("sessP", ""+currentSession.getPages());
        Log.i("esamP", ""+esame.getPageNumber());
        if(prcnt>100){
            percView.setText("100%");
        }else{
            percView.setText(prcnt+"%");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                startActivity(
                        new Intent(InfoSessionActivity.this, ExamActivity.class)
                                .putExtra("esameId",esameId)
                                .putExtra("sessioneId", sessioneId));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
