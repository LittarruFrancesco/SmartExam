package com.smart.coffee.smartexam;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

import java.util.LinkedList;

import static android.content.Context.NOTIFICATION_SERVICE;

public class StudyNotificationReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 0;
    private static final int NOTIFICATION_START_ID = 1;
    private static final int NOTIFICATION_STOP_ID = 2;
    private static final int NOTIFICATION_SKIP_ID = 3;
    private static final String NOTIFICATION_CHANNEL = "General";

    public StudyNotificationReceiver(){}

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        // Permette l'estrazione dei dati dall'intent
        Bundle intentBundle = intent.getExtras();
        String esameId = intent.getStringExtra("esameId");
        String sessioneId = intent.getStringExtra("sessioneId");

        LinkedList<Exam> esami = Saver.loadExams(context, "esamiFile");
        int eIdx=0;
        for(Exam e: esami){
            if(e.getExamId().equals(esameId)){
                break;
            }eIdx++;
        }
        Exam esame = esami.get(eIdx);

        LinkedList<Session> sessioni = esame.getSessions();
        Session currentSession=null;
        for(Session s: sessioni){
            if(s.getSession_id().equals(sessioneId)){
                currentSession = s;
                break;
            }
        }

        switch(intentBundle.getInt("id")) {

            case NOTIFICATION_ID:   // arrivo di una notifica da impostare a un determinato orario

                // ------------- Notifica di richiesta di studio -------------
                // Crea l'intent da inserire nel PendingIntent
                Intent notifyIntent = new Intent(context, InfoSessionActivity.class);
                notifyIntent.putExtra("esameId", esameId);
                notifyIntent.putExtra("sessioneId", sessioneId);
                // Crea il PendingIntent da inserire nella notifica
                PendingIntent notifyPending = PendingIntent.getActivity
                        (context, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                // ------- Notifica di inizio studio da inserire nel pending dell'action play-------
                Intent startStudyIntent = new Intent(context, StudyNotificationReceiver.class);
                startStudyIntent.putExtra("id", NOTIFICATION_START_ID);
                startStudyIntent.putExtra("esameId", esameId);
                startStudyIntent.putExtra("sessioneId", sessioneId);
                // pending da inserire nell'action
                PendingIntent startStudyPendingIntent = PendingIntent.getBroadcast
                        (context, NOTIFICATION_START_ID, startStudyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                // ------- Azione da inserire nell'action skip -------
                Intent skipIntent = new Intent(context, StudyNotificationReceiver.class);
                skipIntent.putExtra("id", NOTIFICATION_SKIP_ID);
                skipIntent.putExtra("esameId", esameId);
                skipIntent.putExtra("sessioneId", sessioneId);
                // pending per chiusura
                PendingIntent skipStudyPendingIntent = PendingIntent.getBroadcast
                        (context, NOTIFICATION_SKIP_ID, skipIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                // Costruisci la notifica da inviare
                NotificationCompat.Builder startBuilder =
                        new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
                                .setContentTitle(esame.getExamName())
                                .setContentText(currentSession.getTitle())
                                .setSmallIcon(R.drawable.ic_school)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                .setAutoCancel(false)
                                .setOngoing(true)
                                .addAction(R.drawable.ic_play, "Inizia", startStudyPendingIntent)
                                .addAction(R.drawable.ic_skip, "Salta", skipStudyPendingIntent)
                                .setContentIntent(notifyPending);

                // Compila la notifica pronta per essere notificata
                Notification studyNotification = startBuilder.build();
                notificationManager.notify(NOTIFICATION_ID, studyNotification);
                break;

            case NOTIFICATION_START_ID: // notifica di inizio studio -> utente ha iniziato a studiare

                notificationManager.cancelAll();

                // Crea l'avviso di fine studio
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                // crea l'intent da mandare in broadcast

                // fine studio
                Intent endStudyIntent = new Intent(context, StudyNotificationReceiver.class);
                endStudyIntent.putExtra("id",NOTIFICATION_STOP_ID);
                endStudyIntent.putExtra("esameId", esameId);
                endStudyIntent.putExtra("sessioneId", sessioneId);
                // inserisci l'intent in un pending intent da mandare in broadcast
                PendingIntent endStudyPendingIntent = PendingIntent.getBroadcast
                        (context, NOTIFICATION_STOP_ID, endStudyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                // andare alla pagina di riepilogo di studio col timer
                // Crea l'intent da inserire nel PendingIntent
                Intent infoIntent = new Intent(context, InfoSessionActivity.class);
                infoIntent.putExtra("esameId", esameId);
                infoIntent.putExtra("sessioneId", sessioneId);
                // Crea il PendingIntent da inserire nella notifica
                PendingIntent infoPending = PendingIntent.getActivity
                        (context, NOTIFICATION_ID, infoIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.set(
                        AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime() + 5000,  // 5 secondi dopo il primo
                        endStudyPendingIntent);

                // Sostituisci la notifica con una nuova
                NotificationCompat.Builder pauseBuilder =
                        new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
                                .setContentTitle(esame.getExamName())
                                .setContentText(currentSession.getTitle())
                                .setSmallIcon(R.drawable.ic_school)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setAutoCancel(false)
                                .addAction(R.drawable.ic_pause, "Pausa", null)
                                .setOngoing(true) // evita che la notifica possa essere rimossa
                                .setContentIntent(infoPending);

                // Compila la notifica pronta per essere notificata
                Notification pauseStudyNotification = pauseBuilder.build();
                notificationManager.notify(NOTIFICATION_ID, pauseStudyNotification);

                break;

            case NOTIFICATION_STOP_ID: // il tempo è scaduto, utente deve inserire i dati di studio

                notificationManager.cancelAll();
                // ------------------------------ Notifica di fine studio ------------------------------
                // Crea l'intent da inserire nel PendingIntent
                Intent notifyEndIntent = new Intent(context, EndActivity.class);
                notifyEndIntent.putExtra("esameId", esameId);
                notifyEndIntent.putExtra("sessioneId", sessioneId);
                // Crea il PendingIntent da inserire nella notifica
                PendingIntent notifyEndPending = PendingIntent.getActivity
                        (context, NOTIFICATION_STOP_ID, notifyEndIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                // Costruisci la notifica da inviare
                NotificationCompat.Builder endBuilder =
                        new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
                                .setContentTitle("Sessione terminata di "+esame.getExamName())
                                .setContentText("Hai terminato la tua sessione corrente di studio")
                                .setSmallIcon(R.drawable.ic_school)
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                .setAutoCancel(true)
                                .setOngoing(true)
                                .setContentIntent(notifyEndPending);

                // Compila la notifica pronta per essere notificata
                Notification studyEndNotification = endBuilder.build();
                notificationManager.notify(NOTIFICATION_STOP_ID, studyEndNotification);
                break;

            case NOTIFICATION_SKIP_ID:
                notificationManager.cancelAll();
                break;

        }
    }
}
