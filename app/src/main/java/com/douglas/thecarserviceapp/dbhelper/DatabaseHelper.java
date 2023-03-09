package com.douglas.thecarserviceapp.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.douglas.thecarserviceapp.model.Appointment;
import com.douglas.thecarserviceapp.model.User;
import com.douglas.thecarserviceapp.util.Util;

import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "Fixer.db";
    private static final int DATABASE_VERSION = 1;

    protected static final String TABLE_USERS = "USERS";
    protected static final String U_COLUMN_ID = "user_id";
    protected static final String U_COLUMN_FNAME = "first_name";
    protected static final String U_COLUMN_LNAME = "last_name";
    protected static final String U_COLUMN_ADDRESS = "address";
    protected static final String U_COLUMN_PHONE = "phone_number";
    protected static final String U_COLUMN_EMAIL = "email";
    protected static final String U_COLUMN_PASSWORD = "u_password";
    protected static final String U_COLUMN_TYPE = "user_type";

    private static final String TABLE_SERVICETYPE = "SERVICETYPE";
    private static final String ST_COLUMN_ID = "service_type_id";
    private static final String ST_COLUMN_STYPE = "type";

    private static final String TABLE_SERVICE = "SERVICE";
    private static final String S_COLUMN_ID = "service_id";
    private static final String S_COLUMN_COST = "cost";
    private static final String S_COLUMN_PROVIDER_ID = "provider_id";
    private static final String S_COLUMN_STYPE_ID = "service_type_id";

    private static final String TABLE_APPOINTMENT = "APPOINTMENT";
    private static final String AP_COLUMN_ID = "appointment_id";
    private static final String AP_COLUMN_UID = "user_id";
    private static final String AP_COLUMN_PROVIDER_ID = "provider_id";
    private static final String AP_COLUMN_SERVICE_ID = "service_id";
    private static final String AP_COLUMN_ADATE = "a_date";
    private static final String AP_COLUMN_ATIME = "a_time";
    private static final String AP_COLUMN_COMMENTS = "comments";
    private static final String AP_COLUMN_TYPE = "type";
    private static final String AP_COLUMN_STATUS = "status";

    private static final String TABLE_REPORTS = "REPORTS";
    private static final String R_COLUMN_ID = "report_id";
    private static final String R_COLUMN_AID = "appointment_id";
    private static final String R_COLUMN_COMMENTS = "comments";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USERS +
                        " (" + U_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        U_COLUMN_FNAME + " TEXT, " +
                        U_COLUMN_LNAME + " TEXT, " +
                        U_COLUMN_ADDRESS + " TEXT, " +
                        U_COLUMN_PHONE + " TEXT, " +
                        U_COLUMN_EMAIL + " TEXT, " +
                        U_COLUMN_PASSWORD + " TEXT, " +
                        U_COLUMN_TYPE + " TEXT);";
        db.execSQL(CREATE_USER_TABLE);

        String CREATE_SERVICETYPE_TABLE = "CREATE TABLE " + TABLE_SERVICETYPE +
                         " (" + ST_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                         ST_COLUMN_STYPE + " TEXT);";
        db.execSQL(CREATE_SERVICETYPE_TABLE);

        String CREATE_SERVICE_TABLE = "CREATE TABLE " + TABLE_SERVICE +
                " (" + S_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                S_COLUMN_COST + " REAL NOT NULL, " +
                S_COLUMN_PROVIDER_ID + " INTEGER NOT NULL, " +
                S_COLUMN_STYPE_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + S_COLUMN_PROVIDER_ID + ") REFERENCES " + TABLE_USERS + " (" + U_COLUMN_ID + "), " +
                "FOREIGN KEY (" + S_COLUMN_STYPE_ID + ") REFERENCES " + TABLE_SERVICETYPE + " (" + ST_COLUMN_ID + "));";

        db.execSQL(CREATE_SERVICE_TABLE);

        String CREATE_APPOINTMENT_TABLE = "CREATE TABLE " + TABLE_APPOINTMENT +
                " (" + AP_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                AP_COLUMN_UID + " INTEGER NOT NULL, " +
                AP_COLUMN_PROVIDER_ID + " INTEGER NOT NULL, " +
                AP_COLUMN_SERVICE_ID + " INTEGER NOT NULL, " +
                AP_COLUMN_ADATE + " TEXT NOT NULL, " +
                AP_COLUMN_ATIME + " TEXT NOT NULL, " +
                AP_COLUMN_COMMENTS + " TEXT, " +
                AP_COLUMN_TYPE + " TEXT NOT NULL, " +
                AP_COLUMN_STATUS + " TEXT, " +
                "FOREIGN KEY (" + AP_COLUMN_UID + ") REFERENCES " + TABLE_USERS + " (" + U_COLUMN_ID + "), " +
                "FOREIGN KEY (" + AP_COLUMN_PROVIDER_ID + ") REFERENCES " + TABLE_USERS + " (" + U_COLUMN_ID + "), " +
                "FOREIGN KEY (" + AP_COLUMN_SERVICE_ID + ") REFERENCES " + TABLE_SERVICE + " (" + S_COLUMN_ID + "));";

        db.execSQL(CREATE_APPOINTMENT_TABLE);

        String CREATE_REPORTS_TABLE = "CREATE TABLE " + TABLE_REPORTS +
                " (" + R_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                R_COLUMN_AID + " INTEGER NOT NULL, " +
                R_COLUMN_COMMENTS + " TEXT, " +
                "FOREIGN KEY (" + R_COLUMN_AID + ") REFERENCES " + TABLE_APPOINTMENT + " (" + AP_COLUMN_ID + "));";
        db.execSQL(CREATE_REPORTS_TABLE);
        initDatas(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICETYPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORTS);
        onCreate(db);
    }

    private void initDatas(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        addTestUsers(db,cv);
        addTestServiceType(db,cv);
        addTestService(db,cv);
        addTestAppointment(db,cv);
        addTestReports(db,cv);
        cv.clear();
    }

    private void addTestUsers(SQLiteDatabase db, ContentValues cv){
        /* INSERT INTO User (userId, firstName, lastName, address, phoneNumber, email, upassword, userType) VALUES */
        // (1,'John', 'Doe', '123 Main St', '555-1234', 'john.doe@example.com', 'mypassword', 'CUSTOMER'),
        cv.put(U_COLUMN_FNAME, "John");
        cv.put(U_COLUMN_LNAME, "Doe");
        cv.put(U_COLUMN_ADDRESS, "123 Main St");
        cv.put(U_COLUMN_PHONE, "555-1234");
        cv.put(U_COLUMN_EMAIL, "john.doe@example.com");
        cv.put(U_COLUMN_PASSWORD, "mypassword");
        cv.put(U_COLUMN_TYPE, "CUSTOMER");
        db.insert(TABLE_USERS, null, cv);
        // (2,'Jane', 'Doe', '456 Oak Ave', '555-5678', 'jane.doe@example.com', 'mypassword', 'CUSTOMER'),
        cv.clear();
        cv.put(U_COLUMN_FNAME, "Jane");
        cv.put(U_COLUMN_LNAME, "Doe");
        cv.put(U_COLUMN_ADDRESS, "456 Oak Ave");
        cv.put(U_COLUMN_PHONE, "555-5678");
        cv.put(U_COLUMN_EMAIL, "jane.doe@example.com");
        cv.put(U_COLUMN_PASSWORD, "mypassword");
        cv.put(U_COLUMN_TYPE, "CUSTOMER");
        db.insert(TABLE_USERS, null, cv);

        //  (3,'Bob', 'Smith', '789 Pine Rd', '555-9012', 'bob.smith@example.com', 'mypassword', 'PROVIDER'),
        cv.clear();
        cv.put(U_COLUMN_FNAME, "Bob");
        cv.put(U_COLUMN_LNAME, "Smith");
        cv.put(U_COLUMN_ADDRESS, "789 Pine Rd");
        cv.put(U_COLUMN_PHONE, "555-9012");
        cv.put(U_COLUMN_EMAIL, "bob.smith@example.com");
        cv.put(U_COLUMN_PASSWORD, "mypassword");
        cv.put(U_COLUMN_TYPE, "PROVIDER");
        db.insert(TABLE_USERS, null, cv);

        // (4,'Alice', 'Johnson', '321 Elm St', '555-3456', 'alice.johnson@example.com', 'mypassword', 'PROVIDER'),
        cv.clear();
        cv.put(U_COLUMN_FNAME, "Alice");
        cv.put(U_COLUMN_LNAME, "Johnson");
        cv.put(U_COLUMN_ADDRESS, "321 Elm St");
        cv.put(U_COLUMN_PHONE, "555-3456");
        cv.put(U_COLUMN_EMAIL, "alice.johnson@example.com");
        cv.put(U_COLUMN_PASSWORD, "mypassword");
        cv.put(U_COLUMN_TYPE, "PROVIDER");
        db.insert(TABLE_USERS, null, cv);

        // (5,'Charlie', 'Brown', '654 Birch Ln', '555-7890', 'charlie.brown@example.com', 'mypassword', 'CUSTOMER');
        cv.clear();
        cv.put(U_COLUMN_FNAME, "Charlie");
        cv.put(U_COLUMN_LNAME, "Brown");
        cv.put(U_COLUMN_ADDRESS, "654 Birch Ln");
        cv.put(U_COLUMN_PHONE, "555-7890");
        cv.put(U_COLUMN_EMAIL, "charlie.brown@example.com");
        cv.put(U_COLUMN_PASSWORD, "mypassword");
        cv.put(U_COLUMN_TYPE, "CUSTOMER");
        db.insert(TABLE_USERS, null, cv);
    }

    private void addTestServiceType(SQLiteDatabase db, ContentValues cv){

        /* INSERT INTO ServiceType(serviceTypeId,type) VALUES */

        // (1001,'Oil change'),
        cv.clear();
        cv.put(ST_COLUMN_ID,1001);
        cv.put(ST_COLUMN_STYPE,"Oil change");
        db.insert(TABLE_SERVICETYPE, null, cv);

        // (1002,'Brake Discs'),
        cv.clear();
        cv.put(ST_COLUMN_ID,1002);
        cv.put(ST_COLUMN_STYPE,"Brake Discs");
        db.insert(TABLE_SERVICETYPE, null, cv);

        // (1003,'Engine Air Filter Replacement');
        cv.clear();
        cv.put(ST_COLUMN_ID,1003);
        cv.put(ST_COLUMN_STYPE,"Engine Air Filter Replacement");
        db.insert(TABLE_SERVICETYPE, null, cv);

    }

    private void addTestService(SQLiteDatabase db, ContentValues cv){
         /*
         INSERT INTO Service (serviceId , cost, providerId, serviceTypeId) VALUES
         */
        // (1,50.0, 3, (SELECT serviceTypeId FROM ServiceType WHERE type = 'Oil change')),
        cv.clear();
        cv.put(S_COLUMN_COST,50.0);
        cv.put(S_COLUMN_PROVIDER_ID,3);
        cv.put(S_COLUMN_STYPE_ID,1001);
        db.insert(TABLE_SERVICE, null, cv);

        // (2, 80.0, 3, (SELECT serviceTypeId FROM ServiceType WHERE type = 'Brake Discs')),
        cv.clear();
        cv.put(S_COLUMN_COST,80.0);
        cv.put(S_COLUMN_PROVIDER_ID,3);
        cv.put(S_COLUMN_STYPE_ID,1002);
        db.insert(TABLE_SERVICE, null, cv);

        // (3, 30.0, 3, (SELECT serviceTypeId FROM ServiceType WHERE type = 'Engine Air Filter Replacement')),
        cv.clear();
        cv.put(S_COLUMN_COST,30.0);
        cv.put(S_COLUMN_PROVIDER_ID,3);
        cv.put(S_COLUMN_STYPE_ID,1003);
        db.insert(TABLE_SERVICE, null, cv);

        // (4, 60.0, 4, (SELECT serviceTypeId FROM ServiceType WHERE type = 'Oil change')),
        cv.clear();
        cv.put(S_COLUMN_COST,60.0);
        cv.put(S_COLUMN_PROVIDER_ID,4);
        cv.put(S_COLUMN_STYPE_ID,1001);
        db.insert(TABLE_SERVICE, null, cv);

        // (5, 100.0, 4, (SELECT serviceTypeId FROM ServiceType WHERE type = 'Brake Discs')),
        cv.clear();
        cv.put(S_COLUMN_COST,100.0);
        cv.put(S_COLUMN_PROVIDER_ID,4);
        cv.put(S_COLUMN_STYPE_ID,1002);
        db.insert(TABLE_SERVICE, null, cv);

        // (6, 40.0, 4, (SELECT serviceTypeId FROM ServiceType WHERE type = 'Engine Air Filter Replacement'));
        cv.clear();
        cv.put(S_COLUMN_COST,40.0);
        cv.put(S_COLUMN_PROVIDER_ID,4);
        cv.put(S_COLUMN_STYPE_ID,1003);
        db.insert(TABLE_SERVICE, null, cv);
    }

    private void addTestAppointment(SQLiteDatabase db, ContentValues cv){

        //INSERT INTO Appointment (appointmentId, userId, providerId, serviceId, aDate, aTime, type)
        //VALUES (1, 1, 3, 1, '2023-03-04', '15:00:00', 'Pick up');
        cv.clear();
        cv.put(AP_COLUMN_UID,1);
        cv.put(AP_COLUMN_PROVIDER_ID,3);
        cv.put(AP_COLUMN_SERVICE_ID,1);
        cv.put(AP_COLUMN_ADATE,"2023-03-04");
        cv.put(AP_COLUMN_ATIME,"15:00");
        cv.put(AP_COLUMN_TYPE,"Pick up");
        db.insert(TABLE_APPOINTMENT, null, cv);

        //INSERT INTO Appointment (appointmentId, userId, providerId, serviceId, aDate, aTime, type, comments)
        //VALUES (2, 1, 4, 4, '2023-03-05', '10:00:00', 'Drop off', 'Use only Shell Plus oil for the engine.');
        cv.clear();
        cv.put(AP_COLUMN_UID,1);
        cv.put(AP_COLUMN_PROVIDER_ID,4);
        cv.put(AP_COLUMN_SERVICE_ID,4);
        cv.put(AP_COLUMN_ADATE,"2023-03-05");
        cv.put(AP_COLUMN_ATIME,"10:00");
        cv.put(AP_COLUMN_TYPE,"Drop off");
        cv.put(AP_COLUMN_COMMENTS,"Use only Shell Plus oil for the engine");
        db.insert(TABLE_APPOINTMENT, null, cv);

        //INSERT INTO Appointment (appointmentId, userId, providerId, serviceId, aDate, aTime, type)
        //VALUES (3, 2, 3, 2, '2023-03-08', '11:00:00', 'Pick up');
        cv.clear();
        cv.put(AP_COLUMN_UID,2);
        cv.put(AP_COLUMN_PROVIDER_ID,3);
        cv.put(AP_COLUMN_SERVICE_ID,2);
        cv.put(AP_COLUMN_ADATE,"2023-03-08");
        cv.put(AP_COLUMN_ATIME,"11:00");
        cv.put(AP_COLUMN_TYPE,"Pick up");
        db.insert(TABLE_APPOINTMENT, null, cv);

        //INSERT INTO Appointment (appointmentId, userId, providerId, serviceId, aDate, aTime, type, comments)
        //VALUES (4, 5, 3, 3, '2023-03-04', '15:00:00', 'Drop off', 'The brake discs need to be replaced.');
        cv.clear();
        cv.put(AP_COLUMN_UID,5);
        cv.put(AP_COLUMN_PROVIDER_ID,3);
        cv.put(AP_COLUMN_SERVICE_ID,3);
        cv.put(AP_COLUMN_ADATE,"2023-03-04");
        cv.put(AP_COLUMN_ATIME,"15:00");
        cv.put(AP_COLUMN_TYPE,"Drop off");
        cv.put(AP_COLUMN_COMMENTS,"The brake discs need to be replaced");
        db.insert(TABLE_APPOINTMENT, null, cv);
    }

    private void addTestReports(SQLiteDatabase db, ContentValues cv){
        //INSERT INTO Reports (reportId, appointmentId, comments)
        //VALUES (1, 2, 'Customer was satisfied with the service provided.');
        cv.clear();
        cv.put(R_COLUMN_AID, 2);
        cv.put(R_COLUMN_COMMENTS, "Customer was satisfied with the service provided");
        db.insert(TABLE_REPORTS, null, cv);
    }

    /** USER DB METHODS **/

    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { U_COLUMN_ID, U_COLUMN_FNAME, U_COLUMN_LNAME, U_COLUMN_ADDRESS, U_COLUMN_PHONE, U_COLUMN_PASSWORD, U_COLUMN_TYPE };
        String selection = U_COLUMN_EMAIL + " = ?";
        String[] selectionArgs = { email };
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        User user = null;
        if (cursor.moveToFirst()) {
            int userId = cursor.getInt(0);
            String firstName = cursor.getString(1);
            String lastName = cursor.getString(2);
            String address = cursor.getString(3);
            String phoneNumber = cursor.getString(4);
            String password = cursor.getString(5);
            String userType = cursor.getString(6);
            user = new User(userId, firstName, lastName, address, phoneNumber, email, password, userType);
        }
        cursor.close();
        db.close();
        return user;
    }

    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(U_COLUMN_FNAME, user.getFirstName());
        cv.put(U_COLUMN_LNAME, user.getLastName());
        cv.put(U_COLUMN_ADDRESS, user.getAddress());
        cv.put(U_COLUMN_PHONE, user.getPhoneNumber());
        cv.put(U_COLUMN_EMAIL, user.getEmail());
        cv.put(U_COLUMN_PASSWORD, user.getPassword());
        cv.put(U_COLUMN_TYPE, user.getUserType().toString());
        long result = db.insert(TABLE_USERS, null, cv);
        if(result == -1){
            Toast.makeText(context, "Unexpected error in adding user.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "User has been added successfully.", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(U_COLUMN_FNAME, user.getFirstName());
        cv.put(U_COLUMN_LNAME, user.getLastName());
        cv.put(U_COLUMN_ADDRESS, user.getAddress());
        cv.put(U_COLUMN_PHONE, user.getPhoneNumber());
       // cv.put(U_COLUMN_EMAIL, user.getEmail()); USER can't change their email later.
        cv.put(U_COLUMN_PASSWORD, user.getPassword());
        cv.put(U_COLUMN_TYPE, user.getUserType().toString());
        String selection = U_COLUMN_EMAIL + " = ?";
        String[] selectionArgs = { String.valueOf(user.getEmail()) };
        long result = db.update(TABLE_USERS, cv, selection, selectionArgs);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }

    }

    public String getUserName(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { U_COLUMN_FNAME, U_COLUMN_LNAME};
        String selection = U_COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(userId) };
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        String userName = null;
        if (cursor.moveToFirst()) {
            userName = cursor.getString(0);
            userName += " " + cursor.getString(1);
        }
        cursor.close();
        db.close();
        return userName;
    }

    public String getUserAddress(int userId){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { U_COLUMN_ADDRESS };
        String selection = U_COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(userId) };
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        String userAddress = null;
        if (cursor.moveToFirst()) {
            userAddress = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return userAddress;
    }

    public List<Appointment> getAllAppointmentsForProvider(int userId) throws ParseException {
        List<Appointment> appointments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { "appointment_id", "user_id", "provider_id", "service_id", "a_date", "a_time", "comments", "type" };
        String selection = "provider_id = ?";
        String[] selectionArgs = { String.valueOf(userId) };
        Cursor cursor = db.query("APPOINTMENT", columns, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            int appointmentId = cursor.getInt(0);
            int appointmentUserId = cursor.getInt(1);
            int appointmentProviderId = cursor.getInt(2);
            int serviceId = cursor.getInt(3);
            Date date = Util.convertDate(cursor.getString(4));
            Time time = Util.convertTime(cursor.getString(5));
            String comments = cursor.getString(6);
            String type = cursor.getString(7);
            Appointment appointment = new Appointment(appointmentId, appointmentUserId, appointmentProviderId, serviceId, date, time, comments, type);
            appointments.add(appointment);
        }
        cursor.close();
        db.close();
        return appointments;
    }

    // :TO DO
    public List<Appointment> getUpcomingAppointmentsForProvider(int userId) {
        return null;
    }

    // :TO DO
    public List<Appointment> getServiceHistoryForProvider(int userId) {
        return null;
    }

    // :TO DO
    public List<Appointment> getUpcomingAppointmentsForCustomer(int userId) {
        return null;
    }

    // :TO DO
    public List<Appointment> getServiceHistoryForCustomer(int userId) {
        return null;
    }



    /** SERVICE DB METHODS **/

    public String getServiceType(int serviceId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { S_COLUMN_STYPE_ID};
        String selection = S_COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(serviceId) };
        Cursor cursor = db.query(TABLE_SERVICE, columns, selection, selectionArgs, null, null, null);
        String serviceType = null;
        if (cursor.moveToFirst()) {
            int serviceTypeId = cursor.getInt(0);
            String[] columns2 = { ST_COLUMN_STYPE };
            String selection2 = ST_COLUMN_ID + " = ?";
            String[] selectionArgs2 = { String.valueOf(serviceTypeId) };
            Cursor cursor2 = db.query(TABLE_SERVICETYPE, columns2, selection2, selectionArgs2, null, null, null);
            if (cursor2.moveToFirst()) {
                serviceType = cursor2.getString(0);
            }
            cursor2.close();
        }
        cursor.close();
        db.close();
        return serviceType;
    }





}