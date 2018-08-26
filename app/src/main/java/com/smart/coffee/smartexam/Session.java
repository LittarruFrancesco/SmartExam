package com.smart.coffee.smartexam;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;
import java.util.GregorianCalendar;

/**
 * Classe indicante una singola sessione di studio
 */

public class Session implements Parcelable, java.io.Serializable{

    /**
     * La sessione di studio è formata da:
     * ID -> permette di recuperarla dalla lista delle sessioni per un esame
     * Giorno/mese/anno -> giorno del calendario
     * Orario -> ora di partenza della sessione di studio
     * Durata -> quanto tempo deve durare la sessione di studio
     * Titolo -> informazione concisa sul contenuto della sessione
     * Info -> informazione in linguaggio naturale riguardante gli argomenti
     */

    private String session_id;
    private GregorianCalendar calendarDate;
    private int hour;
    private int minute;
    private String duration;
    private String title;
    private String info = "";
    private int pages;
    private int completed = 0;
    private int pagesCompleted;
    private String infoCompleted = "";

    // costruttore -> costruzione come stream
    public Session(){
        this.session_id = ""+(new Timestamp(System.currentTimeMillis())).getTime();
    }

    // Setters

    public Session setData(GregorianCalendar calendarDate){
        this.calendarDate = calendarDate;
        return this;
    }

    public Session setHour(int hour){
        this.hour = hour;
        return this;
    }

    public Session setMinute(int minute){
        this.minute = minute;
        return this;
    }

    public Session setDuration(String duration){
        this.duration = duration;
        return this;
    }

    public Session setTitle(String title){
        this.title = title;
        return this;
    }

    public Session setInfo(String info){
        this.info = info;
        return this;
    }

    public Session setPages(int pages){
        this.pages = pages;
        return this;
    }

    public void setCompleted(){
        this.completed = 1;
    }

    public void setPagesCompleted(int pagesCompleted){
        this.pagesCompleted = pagesCompleted;
    }

    public void setInfoCompleted(String infoCompleted){
        this.infoCompleted = infoCompleted;
    }
    // Getters


    public String getSession_id() {
        return session_id;
    }

    public GregorianCalendar getCalendarDate() {
        return calendarDate;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getDuration() {
        return duration;
    }

    public String getTitle() {
        return title;
    }

    public String getInfo() {
        return info;
    }

    public int getPages(){
        return pages;
    }

    public Boolean isBeenCompleted(){
        return completed == 1;
    }

    public int getPagesCompleted(){
        return pagesCompleted;
    }

    public String getInfoCompleted(){
        return infoCompleted;
    }

    // parcelable interface

    public static final Creator<Session> CREATOR = new Creator<Session>() {
        @Override
        public Session createFromParcel(Parcel parcel) {
            return new Session(parcel);
        }

        @Override
        public Session[] newArray(int i) {
            return new Session[0];
        }
    };


    private Session(Parcel in){
        this.session_id = in.readString();
        this.calendarDate = (GregorianCalendar) in.readSerializable();
        this.hour = in.readInt();
        this.minute = in.readInt();
        this.duration = in.readString();
        this.title = in.readString();
        this.info = in.readString();
        this.pages = in.readInt();
        this.completed = in.readInt();
        this.pagesCompleted = in.readInt();
        this.infoCompleted = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(session_id);
        parcel.writeSerializable(calendarDate);
        parcel.writeInt(hour);
        parcel.writeInt(minute);
        parcel.writeString(duration);
        parcel.writeString(title);
        parcel.writeString(info);
        parcel.writeInt(pages);
        parcel.writeInt(completed);
        parcel.writeInt(pagesCompleted);
        parcel.writeString(infoCompleted);
    }
}
