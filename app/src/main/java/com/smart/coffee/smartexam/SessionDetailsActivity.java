package com.smart.coffee.smartexam;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;

public class SessionDetailsActivity extends AppCompatActivity implements DialogDateFragment.OnDatePass,
        DialogTimeFragment.OnTimePass,
        AdapterView.OnItemSelectedListener {

    private final static int NOTIFICATION_START_ID = 0;

    private String sessionTitle;
    private GregorianCalendar sessionDate = null;
    private int hour=-1, minute=-1;
    private String sessionLength = "";
    private String choice = "Ore";
    private int session_pages = 0;

    private TextInputEditText sessionTitleEdit;
    private TextInputEditText sessionDateEdit;
    private TextInputEditText sessionLengthEdit;
    private TextInputEditText sessionTimeEdit;
    private TextInputEditText sessionPages;
    private TextInputEditText sessionNoteEdit;

    private TextInputLayout til_session_title;
    private TextInputLayout til_session_date;
    private TextInputLayout til_session_start;
    private TextInputLayout til_session_length;
    private TextInputLayout til_session_pages;

    private Session session;
    private Exam esame;
    public String examId;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.actionbar_details,menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_details);
        // imposta la mia Toolbar come actionbar
        Toolbar toolbar = findViewById(R.id.session_actionbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Nuova Sessione");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        /**
         * da savedInstanceState prende informazioni sull'esame per poter inserire parte delle info
         * non implementata ancora
         * */

        Intent esameIntent = getIntent();
        examId = esameIntent.getStringExtra("esameId");

        LinkedList<Exam> esami = Saver.loadExams(SessionDetailsActivity.this,"esamiFile");
        for (Exam es: esami){       // gioco malato per prendere l'esame giusto dalla lista linkata
            if(es.getExamId() == examId){
                esame = es;
            }
        }

        // listener titolo per togliere errori
        til_session_title = (TextInputLayout) findViewById(R.id.til_session_title);
        sessionTitleEdit = (TextInputEditText) findViewById(R.id.session_title);
        sessionTitleEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                til_session_title.setError(null);
            }
        });


        // listener titolo per togliere errori
        til_session_pages = (TextInputLayout) findViewById(R.id.til_session_pages);
        sessionPages = (TextInputEditText) findViewById(R.id.session_pages);
        sessionPages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                til_session_pages.setError(null);
            }
        });

        // crea il listener per visulizzare il fragment del calendario
        til_session_date = (TextInputLayout) findViewById(R.id.til_session_date);
        sessionDateEdit = (TextInputEditText) findViewById(R.id.session_date);
        sessionDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                til_session_date.setError(null);
                DialogFragment dateDialogFragment = new DialogDateFragment();
                dateDialogFragment.show(getSupportFragmentManager(), "sessionDatePicker");
            }
        });

        // crea il listener per visualizzare il fragment dell'orario
        til_session_start = (TextInputLayout) findViewById(R.id.til_session_start);
        sessionTimeEdit = (TextInputEditText) findViewById(R.id.session_time_start);
        sessionTimeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                til_session_start.setError(null);
                DialogFragment timeDialogFragment = new DialogTimeFragment();
                timeDialogFragment.show(getSupportFragmentManager(), "sessionTimePicker");
            }
        });

        // --------- Popola lo spinner -----------
        Spinner spinner = (Spinner) findViewById(R.id.time_spinner);
        // Crea l'array adapter per inserire gli item nello spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (this, R.array.time_selectors, android.R.layout.simple_spinner_item);
        // apre lo spinner quando ci si preme sopra
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Setta lo spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        // -----------------------------------------------------------------------------------------
    }

    // implementazione per prendere i dati dal DialogDateFragment
    @Override
    public void onDatePass(int year, int month, int day) {
        this.sessionDate = new GregorianCalendar(year, month+1, day);
        sessionDateEdit.setText(
                        sessionDate.get(Calendar.DAY_OF_MONTH)+"/"+
                        sessionDate.get(Calendar.MONTH)+"/"+
                        sessionDate.get(Calendar.YEAR));
    }

    // implementazione per prendere i dati dal DialogTimeFragment
    @Override
    public void onTimePass(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
        String h =""+hour;
        String m =""+minute;
        if(hour < 10){
            h="0"+hour;
        }
        if(minute< 10){
            m="0"+minute;
        }
        sessionTimeEdit.setText(h + ":" + m);
    }


    // implementazione per lo spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        choice = adapterView.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        sessionTitleEdit = (TextInputEditText) findViewById(R.id.session_title);
        sessionDateEdit = (TextInputEditText) findViewById(R.id.session_date);
        sessionLengthEdit = (TextInputEditText) findViewById(R.id.session_length);
        sessionTimeEdit = (TextInputEditText) findViewById(R.id.session_time_start);
        sessionPages = (TextInputEditText) findViewById(R.id.session_pages);
        sessionNoteEdit = (TextInputEditText) findViewById(R.id.session_note);

        til_session_title = (TextInputLayout) findViewById(R.id.til_session_title);
        til_session_date = (TextInputLayout) findViewById(R.id.til_session_date);
        til_session_start = (TextInputLayout) findViewById(R.id.til_session_start);
        til_session_length = (TextInputLayout) findViewById(R.id.til_session_length);
        til_session_pages = (TextInputLayout) findViewById(R.id.til_session_pages);

        switch(item.getItemId()){
            case R.id.save_details:

                Log.i("esame",examId);
                /**
                 * Controlla che tutti i campi siano in ordine
                 * */
                Boolean errFlag = false;
                session = new Session();

                // titolo non vuoto, minore = 60
                sessionTitle = sessionTitleEdit.getText().toString();
                if(sessionTitle.length() > 0 && sessionTitle.length() <= 30){
                    session.setTitle(sessionTitle);
                }else{
                    errFlag = true;
                    if(sessionTitle.length() > 60){
                        til_session_title.setError("Titolo troppo lungo");
                    }else{
                        til_session_title.setError("Campo obbligatorio");
                    }
                }

                // data da oggi al futuro
                if(sessionDate == null){
                    errFlag = true;
                    til_session_date.setError("Campo obbligatorio");
                }else{
                    if(sessionDate.compareTo(new GregorianCalendar()) < 0){
                        errFlag = true;
                        til_session_date.setError("Inserisci una data futura");
                    }else{
                        session.setData(sessionDate);
                    }
                }

                // orario inserito
                if(hour == -1 || minute == -1){
                    errFlag = true;
                    til_session_start.setError("Campo obbligatorio");
                }else{
                    session.setHour(hour).setMinute(minute);
                }

                // durata maggiore di 0
                sessionLength = sessionLengthEdit.getText().toString();
                if(sessionLength == "" || sessionLength == "0"){
                    errFlag = true;
                    til_session_length.setError("Inserisci una durata valida");
                }else{
                    if(choice.equals("Minuti")){
                        session.setDuration(sessionLength+" Minuti");
                    }else{
                        session.setDuration(sessionLength+" Ore");
                    }
                }

                // almeno una pagina da studiare
                try {
                    session_pages = Integer.parseInt((sessionPages.getText()).toString());
                }catch(NumberFormatException e){ session_pages = 0; }
                if(session_pages < 1){
                    errFlag = true;
                    til_session_pages.setError("Campo obbligatorio");
                }else{
                    session.setPages(session_pages);
                }

                session.setInfo(sessionNoteEdit.getText().toString());

                if(!errFlag) {
                    LinkedList<Exam> esami = Saver.loadExams(getApplicationContext(), "esamiFile");
                    int i=0;
                    for(Exam e: esami){
                        if(e.getExamId().equals(examId)){
                            break;
                        }i++;
                    }
                    esame = esami.get(i);
                    esame.addSession(session);
                    esami.remove(i);
                    esami.add(esame);
                    Saver.saveExams(getApplicationContext(),"esamiFile", esami);

                    // Richiama i servizi per impostare un allarme a tempo e per mandare una notifica
                    final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                    // crea l'intent da mettere nel PendingIntent
                    Intent broadcastIntent = new Intent(SessionDetailsActivity.this, StudyNotificationReceiver.class);
                    broadcastIntent.putExtra("id", 0);
                    broadcastIntent.putExtra("esameId",examId);
                    broadcastIntent.putExtra("sessioneId",session.getSession_id());

                    // Crea il Pending intent da mandare inserire nell'AlarmManager
                    final PendingIntent pendingStartNotification = PendingIntent
                            .getBroadcast(
                                    SessionDetailsActivity.this,
                                    NOTIFICATION_START_ID,
                                    broadcastIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT);
                    // invia intent a StudyNotificationReceiver per attivare la notifica
                    alarmManager.set(
                            AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            SystemClock.elapsedRealtime() + 5000,
                            pendingStartNotification);
                    Toast.makeText(getApplicationContext(), "Notifica impostata", Toast.LENGTH_SHORT).show();

                    Intent backAgainIntent = new Intent(getApplicationContext(), ExamActivity.class);
                    backAgainIntent.putExtra("sessions_please",true);
                    backAgainIntent.putExtra("esameId",examId);
                    startActivity(backAgainIntent);

                }
                return true;


            case android.R.id.home:
                startActivity(
                        new Intent(SessionDetailsActivity.this, ExamActivity.class)
                                .putExtra("esameId", examId)
                                .putExtra("sessions_please",true));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
