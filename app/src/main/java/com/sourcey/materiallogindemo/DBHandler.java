/**
 * Created by Bhawana on 9/18/2016.
 */

package com.sourcey.materiallogindemo;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;

        import java.util.ArrayList;


public class DBHandler extends SQLiteOpenHelper{
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "response.db";
    private static final String TABLE_NAME = "restab";
    private static final String KEY_ID = "EventId";
    private static final String KEY_EVENTKEY = "EventKey";
    private static final String KEY_BAROPERATORUSERID = "BarOperatorUserId";
    private static final String KEY_NAME = "Name";
    private static final String KEY_STARTTIME = "DateTimeStartUtc";
    private static final String KEY_ENDTIME = "DateTimeEndUtc";
    private static final String KEY_CLOUDINARY = "CloudinaryPublicImageId";


    String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("+KEY_ID+" INTEGER PRIMARY KEY,"
            +KEY_NAME+" TEXT,"+KEY_EVENTKEY+" TEXT,"+KEY_STARTTIME+" TEXT,"+KEY_ENDTIME+" TEXT,"+
            KEY_CLOUDINARY+" TEXT,"+KEY_BAROPERATORUSERID+" TEXT)";
    String DROP_TABLE = "DROP TABLE IF EXISTS "+TABLE_NAME;

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }


    public void addEvent(Event ev) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            if(getEvent(ev.getId())== 0){
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, ev.getName());
            values.put(KEY_ID, ev.getId());
            values.put(KEY_EVENTKEY,ev.getKey());
            values.put(KEY_BAROPERATORUSERID, ev.getName());
            values.put(KEY_STARTTIME, ev.getStarttime());
            values.put(KEY_ENDTIME,ev.getEndtime());
            values.put(KEY_CLOUDINARY,ev.getCloudinary());
            db.insert(TABLE_NAME, null, values);
            db.close();
            }
        }catch (Exception e){
            Log.e("problem",e+"");
        }
    }
    public int getEvent(String eventid) {
        SQLiteDatabase db = this.getReadableDatabase();

        int existIdFlag = 0;
        try{

            String QUERY = "SELECT * FROM "+TABLE_NAME +" WHERE EventId =" + eventid;
            Cursor cursor = db.rawQuery(QUERY, null);
            if(cursor.getCount() != 0)
            {
                existIdFlag =1;
            }
//            db.close();
        }catch (Exception e){
            Log.e("error",e+"");
        }
        return existIdFlag;
    }


    public ArrayList<Event> getAllEvnets() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Event> eventList = null;
        try{
            eventList = new ArrayList<Event>();
            String QUERY = "SELECT * FROM "+TABLE_NAME +" ORDER BY date(DateTimeStartUtc) DESC";
            Cursor cursor = db.rawQuery(QUERY, null);
            if(!cursor.isLast())
            {
                while (cursor.moveToNext())
                {     Event ev = new Event();
                     ev.setId(cursor.getString(0));
                     ev.setName(cursor.getString(1));
                     ev.setKey(cursor.getString(2));
                     ev.setStarttime(cursor.getString(3));
                     ev.setEndtime(cursor.getString(4));
                     ev.setCloudinary(cursor.getString(5));
                     ev.setBarOperator(cursor.getString(6));
                    eventList.add(ev);

                }
            }
            db.close();
        }catch (Exception e){
            Log.e("error",e+"");
        }
        return eventList;
    }


    public int getEventCount() {
        int num = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            String QUERY = "SELECT * FROM "+TABLE_NAME;
            Cursor cursor = db.rawQuery(QUERY, null);
            num = cursor.getCount();
            db.close();
            return num;
        }catch (Exception e){
            Log.e("error",e+"");
        }
        return 0;
    }
}