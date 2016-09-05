package com.example.bas_bk.dstunotify;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.Time;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


/**
 * Created by BAS_BK on 18.08.2016.
 */


public class Message extends RealmObject implements Parcelable{
    @PrimaryKey
    public long remoteId;



    public String Sender;
    public String Theme;
    public String Date;
    public String Text;
    public Boolean watched;
    public Boolean favorite;

    protected Message(Parcel in) {
        remoteId = in.readLong();
        Text = in.readString();
        Sender = in.readString();
        Theme = in.readString();
        Date = in.readString();
        watched = in.readInt() != 0;
        favorite = in.readInt() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(remoteId);
        dest.writeString(Text);
        dest.writeString(Sender);
        dest.writeString(Theme);
        dest.writeString(Date);
        dest.writeInt(watched ? 1 : 0);
        dest.writeInt(favorite ? 1 : 0);
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public long getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(long remoteId) {
        this.remoteId = remoteId;
    }

    public String getText(){
        return Text;
    }

    public String getSender(){
        return Sender;
    }

    public String getTheme(){
        return Theme;
    }

    public String getTime(){
        return Date;
    }

    public boolean isWatched(){
        return watched;
    }

    public boolean isSelected(){
        return favorite;
    }

    public void setSelect(boolean select){
        favorite = select;
    }

    public Message(){
        super();
    }

    public Message(Integer remoteId, String Text, String sender,
                   String Theme, String Date, boolean watched) {
        this.remoteId = remoteId;
        this.Text = Text;
        this.Sender = sender;
        this.Theme = Theme;
        this.Date = Date;
        this.watched = watched;
        favorite = false;
    }

    public void watch(){
        this.watched = true;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
