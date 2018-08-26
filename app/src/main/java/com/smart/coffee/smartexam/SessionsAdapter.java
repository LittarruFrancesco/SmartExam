package com.smart.coffee.smartexam;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class SessionsAdapter extends ArrayAdapter<Session> {

    public SessionsAdapter(@NonNull Context context, List<Session> sessions) {
        super(context, 0, sessions);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        // se la vista non è riciclata ma nuova
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.single_session, parent, false);
        }
        // ottieni l'oggetto corrente dall'adapter alla posizione da visualizzare
        Session currentSession = getItem(position);

        TextView sessionName = (TextView) listItemView.findViewById(R.id.single_session_name);
        TextView adjView = (TextView) listItemView.findViewById(R.id.single_session_adj);
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.single_session_date);
        TextView timeView = (TextView) listItemView.findViewById(R.id.single_session_start);
        TextView sessionTextView = (TextView) listItemView.findViewById(R.id.single_session_duration);

        sessionName.setText(currentSession.getTitle());

        if(currentSession.isBeenCompleted()){
            adjView.setText("Completata");
            adjView.setAlpha(0.30f);
            sessionName.setAlpha(0.30f);
            dateTextView.setAlpha(0.30f);
            timeView.setAlpha(0.30f);
            sessionTextView.setAlpha(0.30f);
        }else{
            adjView.setText("Programmata");
        }

        GregorianCalendar date = currentSession.getCalendarDate();
        String sessionDate =
                date.get(Calendar.DAY_OF_MONTH)+"/"+
                date.get(Calendar.MONTH)+"/"+
                date.get(Calendar.YEAR);

        dateTextView.setText(sessionDate);

        timeView.setText(currentSession.getHour()+":"+currentSession.getMinute());

        String sessionDuration = ""+currentSession.getDuration();
        sessionTextView.setText(sessionDuration);

        return listItemView;
    }
}
