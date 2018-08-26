package com.smart.coffee.smartexam;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class ExamAdapter extends ArrayAdapter {

    public ExamAdapter(Context context, List<Exam> objects){
        super(context,0,objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        // vista composta da nuovi elementi
        if(convertView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.exam_view, parent, false);
        }

        // prendi elemento corrente
        final Exam currentExam = (Exam) getItem(position);

        GregorianCalendar examDate = currentExam.getExamDate();
        String date = "Esame il "+
                examDate.get(Calendar.DAY_OF_MONTH)+"/"+
                examDate.get(Calendar.MONTH)+"/"+
                examDate.get(Calendar.YEAR);
        TextView dateView = (TextView) listItemView.findViewById(R.id.exam_date);
        dateView.setText(date);

        final ImageView notSwitch = (ImageView) listItemView.findViewById(R.id.exam_switch);
        notSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(notSwitch
                        .getDrawable()
                        .getConstantState()
                            .equals(getContext()
                                    .getResources()
                                    .getDrawable(R.drawable.ic_alarm_off_black_56dp)
                                    .getConstantState())){
                    notSwitch.setImageResource(R.drawable.ic_alarm_on_black_56dp);
                }else{
                    notSwitch.setImageResource(R.drawable.ic_alarm_off_black_56dp);
                }
            }
        });

        TextView textView = (TextView) listItemView.findViewById(R.id.exam);
        textView.setText(currentExam.getExamName());
        return listItemView;
    }
}
