    package com.example.homework5;

    import android.content.Context;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;
    import android.util.Log;

    import androidx.annotation.Nullable;

    import java.util.ArrayList;

    public class PhotoHandler extends SQLiteOpenHelper {

        public static final String DB_NAME = "HomeWork5";
        public static final int DB_VERSION = 1;
        private static final String TABLE_NAME = "Photo";
        private static final String id = "id";
        private static final String name = "name";
        private static final String description = "desciption";
        private static final String photo = "photo";
        private static final String PATH = "/data/data/com.example.homework5/database/HomeWork5.db";
        public PhotoHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, DB_NAME, factory, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
        public ArrayList<Photo> getDatas(){
            ArrayList<Photo> photoArrayList = new ArrayList<>();
            SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(PATH,null, SQLiteDatabase.OPEN_READONLY);
            String query = "Select * from " + TABLE_NAME;
            Cursor cursor = sqLiteDatabase.rawQuery(query,null);
            if(cursor != null){
                if(cursor.moveToFirst()){
                    do{
                        String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
                        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                        String description = cursor.getString(cursor.getColumnIndexOrThrow("desciption"));
                        byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("photo"));
                        Photo photo = new Photo(id, name, description, image);
                        photoArrayList.add(photo);
                    }while(cursor.moveToNext());
                }
                cursor.close();
            }
            sqLiteDatabase.close();
            return photoArrayList;
        }
        public void insertPhoto(Photo p)
        {
            SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.CREATE_IF_NECESSARY);
            String insertSQL = "INSERT INTO " + TABLE_NAME + " (" +
                    id + ", " + name + ", " + description + ", " + photo + ") VALUES (?, ?, ?, ?)";
            sqLiteDatabase.execSQL(insertSQL, new Object[]{p.getId(), p.getName(), p.getDescription(), p.getImage()});
            sqLiteDatabase.close();
        }

        public void deletePhoto(String id) {
            SQLiteDatabase db = getWritableDatabase();
            db.delete("Photo", "id = ?", new String[]{String.valueOf(id)});
            db.close();
        }


    }
