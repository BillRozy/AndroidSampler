package com.example.fd.sampler;


import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;

public class SoundSample  implements Parcelable {
    private long id;
    private String title;
    private String artist, data, albumkey,size;
    private long alid;

    public SoundSample(long songID, String songTitle, String songArtist, long albumID, String thisdata, String AlbumKey, long songSize) {
        id=songID;
        title=songTitle;
        artist=songArtist;
        alid=albumID;
        data=thisdata;
        albumkey=AlbumKey;
        size = ( new DecimalFormat("#0.00").format((double)songSize/1024.0/1024.0)) + " mb";

    }
    public SoundSample(){

    }
    public long getID(){return id;}
    public String getTitle(){return title;}
    public String getPath(){return data;}
    public String getSize(){return size;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeLong(alid);
        dest.writeLong(id);
        dest.writeString(data);
        dest.writeString(albumkey);
        dest.writeString(size);
    }
    public static final Parcelable.Creator<SoundSample> CREATOR = new Parcelable.Creator<SoundSample>() {
        public SoundSample createFromParcel(Parcel in) {
            SoundSample song = new SoundSample();
            song.title = in.readString();
            song.artist = in.readString();
            song.alid = in.readLong();
            song.id = in.readLong();
            song.data= in.readString();
            song.albumkey=in.readString();
            song.size = in.readString();
            return song;
        }

        public SoundSample[] newArray(int size) {
            return new SoundSample[size];
        }
    };
}