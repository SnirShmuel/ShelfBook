package com.snir.shelfbook.model;

import androidx.room.Room;

import com.snir.shelfbook.MyApplication;

public class AppLocalDb{
    static public AppLocalDbRepository db =
            Room.databaseBuilder(MyApplication.context,
                    AppLocalDbRepository.class,
                    "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();

}