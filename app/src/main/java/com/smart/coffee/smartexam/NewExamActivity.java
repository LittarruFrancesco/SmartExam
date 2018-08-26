package com.smart.coffee.smartexam;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;

public class NewExamActivity extends AppCompatActivity
        implements DialogDateFragment.OnDatePass{

    private TextInputEditText newExamDateEdit;
    private TextInputEditText newExamNameEdit;
    private TextInputEditText newExamPageEdit;

    private GregorianCalendar examDate = null;
    private Exam nuovoEsame = new Exam();

    private String examName;
    private String examNote;
    private int examPages;

    private Boolean examPagesFlag;
    private Boolean examDateFlag;

    private TextInputLayout tilName;
    private TextInputLayout tilPages;
    private TextInputLayout tilDate;

    private static final int NOTIFICATION_ID = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.new_exam_actionbar, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_exam);
        Toolbar toolbar = (Toolbar) findViewById(R.id.new_exam_actionbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Nuovo Esame");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        final TextInputLayout tilName = (TextInputLayout) findViewById(R.id.new_exam_course_name_layout);
        final TextInputLayout tilPages = (TextInputLayout) findViewById(R.id.new_exam_pages_number_layout);
        final TextInputLayout tilDate = (TextInputLayout) findViewById(R.id.new_exam_date_layout);

        // crea il listener per visulizzare il fragment del calendario
        newExamDateEdit = (TextInputEditText) findViewById(R.id.new_exam_date);
        newExamDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tilDate.setError(null);
                DialogFragment dateDialogFragment = new DialogDateFragment();
                dateDialogFragment.show(getSupportFragmentManager(), "examDatePicker");
            }
        });

        // listener titolo
        newExamNameEdit = (TextInputEditText) findViewById(R.id.new_exam_name);
        newExamNameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tilName.setError(null);
            }
        });

        // listener pagine
        newExamPageEdit = (TextInputEditText) findViewById(R.id.new_exam_pages);
        newExamPageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tilPages.setError(null);
            }
        });


        /**
         * Prendi l'intent
         * estrai i dati (nome corso e quello che c'è)
         * inseriscilo negli edittext
         * fine
         * */

        Intent intent = getIntent();
        String examName = intent.getStringExtra("examName");
        newExamNameEdit.setText(examName);
        if(intent.hasExtra("examDate")){
            int day = intent.getIntExtra("day",0);
            int month = intent.getIntExtra("month",0);
            int year = intent.getIntExtra("year",0);
            examDate = new GregorianCalendar(year, month, day);
            newExamDateEdit.setText(day+"/"+month+"/"+year);
        }
        if(intent.hasExtra("examPages")){
            examPages = intent.getIntExtra("examPages",0);
            if(examPages > 0){
                newExamPageEdit.setText(""+examPages);
            }
        }
        String notes = intent.getStringExtra("examNote");
        ((TextInputEditText) findViewById(R.id.new_exam_note)).setText(notes);


    }

    @Override
    public void onDatePass(int year, int month, int day) {
        examDate = new GregorianCalendar(year, month+1, day);
        newExamDateEdit.setText(""+
                examDate.get(Calendar.DAY_OF_MONTH)+"/"+
                examDate.get(Calendar.MONTH)+"/"+
                examDate.get(Calendar.YEAR));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        TextInputLayout tilName = (TextInputLayout) findViewById(R.id.new_exam_course_name_layout);
        TextInputLayout tilPages = (TextInputLayout) findViewById(R.id.new_exam_pages_number_layout);
        TextInputLayout tilDate = (TextInputLayout) findViewById(R.id.new_exam_date_layout);

        examDateFlag = false;
        examPagesFlag = false;

        AlertDialog.Builder builder = new AlertDialog.Builder(NewExamActivity.this);

        // Listener per chiedere di mandare notifica ecc ecc
        DialogInterface.OnClickListener temporaryOkListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int choice) {
                switch (choice){
                    case DialogInterface.BUTTON_POSITIVE:


                        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                        // Deve ritornare in questa schermata
                        Intent notifyNewExIntent = new Intent(NewExamActivity.this, NewExamActivity.class);
                        notifyNewExIntent.putExtra("examName", examName);

                        if(!examDateFlag){
                            notifyNewExIntent.putExtra("examDate", true);
                            notifyNewExIntent.putExtra("day", examDate.get(Calendar.DAY_OF_MONTH));
                            notifyNewExIntent.putExtra("month", examDate.get(Calendar.MONTH));
                            notifyNewExIntent.putExtra("year", examDate.get(Calendar.YEAR));
                        }
                        if(!examPagesFlag){notifyNewExIntent.putExtra("examPages", examPages);}
                        notifyNewExIntent.putExtra("examNote",examNote);

                        // Crea il PendingIntent da inserire nella notifica
                        PendingIntent notifyNewExPending = PendingIntent.getActivity
                                (NewExamActivity.this, NOTIFICATION_ID, notifyNewExIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                        // Costruisci la notifica da inviare
                        NotificationCompat.Builder endBuilder =
                                new NotificationCompat.Builder(NewExamActivity.this, "General")
                                        .setContentTitle("Inserimento dati aggiuntivi")
                                        .setContentText("Devi completare i dati per: "+examName)
                                        .setSmallIcon(R.drawable.ic_school)
                                        .setPriority(NotificationCompat.PRIORITY_LOW)
                                        .setAutoCancel(true)
                                        .setOngoing(true)
                                        .setContentIntent(notifyNewExPending);

                        // Compila la notifica pronta per essere notificata
                        Notification newExamNotification = endBuilder.build();
                        notificationManager.notify(NOTIFICATION_ID, newExamNotification);

                        Intent redirectIntent = new Intent(NewExamActivity.this, MainActivity.class);
                        startActivity(redirectIntent);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        builder.setMessage("Puoi impostare una notifica per completare le informazioni " +
                "più tardi oppure annullare e aggiungerle ora")
                .setPositiveButton("Imposta notifica", temporaryOkListener)
                .setNegativeButton("Annulla", temporaryOkListener);

        examName = (((TextInputEditText) findViewById(R.id.new_exam_name)).getText()).toString();
        try {
            examPages = Integer.parseInt((((TextInputEditText) findViewById(R.id.new_exam_pages)).getText()).toString());
        }catch(NumberFormatException e){ examPages = 0; }

        examNote = (((TextInputEditText) findViewById(R.id.new_exam_note)).getText()).toString();

        switch(item.getItemId()){
            case R.id.save_new_exam:    // tasto salva dell'actionbar

                // Titolo non inserito
                if(examName.isEmpty()){
                    tilName.setError("Inserisci il corso");
                    return true;
                }

                // data o numero di pagine non inserito
                if(examPages == 0){ examPagesFlag = true; }
                if(examDate == null){ examDateFlag = true; }

                if(examPagesFlag && examDateFlag){
                    builder.setTitle("Data e Pagine mancanti").show();
                    tilDate.setError("Data mancante");
                    tilPages.setError("Numero di pagine mancanti");
                }else{
                    if(examDateFlag){
                        builder.setTitle("Data mancante").show();
                        tilDate.setError("Data mancante");
                    }else{
                        if(examPagesFlag){
                            if(examDate.compareTo(new GregorianCalendar()) < 0){
                                tilDate.setError("Inserisci una data futura");
                            }else{
                                builder.setTitle("Numero di pagine mancanti").show();
                                tilPages.setError("Numero di pagine mancanti");
                            }
                        }else{
                            if(examDate.compareTo(new GregorianCalendar()) < 0){
                                tilDate.setError("Inserisci una data futura");
                            }else{
                                nuovoEsame.setExamDate(examDate);
                                nuovoEsame.setExamName(examName);
                                nuovoEsame.setPageNumber(examPages);
                                nuovoEsame.addNote(examNote);
                                LinkedList<Exam> lista = Saver.loadExams(getApplicationContext(), "esamiFile");
                                lista.add(nuovoEsame);
                                Boolean v = Saver.saveExams(getApplicationContext(),"esamiFile", lista);
                                Intent intent = new Intent(this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    }
                }
                return true;

            case android.R.id.home:
                startActivity(new Intent(NewExamActivity.this, MainActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}