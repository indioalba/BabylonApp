package babylon.babylonapp.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import babylon.babylonapp.Model.Comment;
import babylon.babylonapp.Model.Post;
import babylon.babylonapp.Model.User;

/**
 * Created by manuel on 9/3/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    public static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "blog";

    // Posts table name
    public static final String TABLE_POST = "posts";

    // Users Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_TITLE = "title";
    public static final String KEY_BODY = "body";

    // Users table name
    public static final String TABLE_USERS = "users";

    // Users Table Columns names
    //private static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_STREET = "street";
    public static final String KEY_SUITE = "suite";
    public static final String KEY_CITY = "city";
    public static final String KEY_ZIPCODE = "zipcode";
    public static final String KEY_LAT = "lat";
    public static final String KEY_LNG = "lng";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_WEBSITE = "website";
    public static final String KEY_COMPANY_NAME = "company_name";
    public static final String KEY_COMPANY_CATCH_PHRASE = "catchPhrase";
    public static final String KEY_COMPANY_BS = "bs";


    // Comments table name
    private static final String TABLE_COMMENTS = "comments";

    // Comments Table Columns names
    //private static final String KEY_ID = "id";
    //private static final String KEY_NAME = "name";
    private static final String KEY_POST_ID = "postId";
    //private static final String KEY_EMAIL = "email";
    //private static final String KEY_BODY = "body";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_POST_TABLE = "CREATE TABLE " + TABLE_POST + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USER_ID + " INTEGER,"
                + KEY_TITLE + " TEXT," + KEY_BODY + " TEXT" + ")";
        db.execSQL(CREATE_POST_TABLE);

        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_USERNAME + " TEXT," + KEY_EMAIL + " TEXT,"
                + KEY_STREET + " TEXT," + KEY_SUITE + " TEXT,"
                + KEY_CITY + " TEXT," + KEY_ZIPCODE + " INTEGER,"
                + KEY_LAT + " REAL," + KEY_LNG + " REAL,"
                + KEY_PHONE + " TEXT," + KEY_WEBSITE+" TEXT,"
                + KEY_COMPANY_NAME + " TEXT,"+ KEY_COMPANY_CATCH_PHRASE+ " TEXT,"
                + KEY_COMPANY_BS + " TEXT" +")";

        db.execSQL(CREATE_USER_TABLE);

        String CREATE_COMMENT_TABLE = "CREATE TABLE " + TABLE_COMMENTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_POST_ID + " INTEGER," + KEY_EMAIL + " TEXT,"
                + KEY_BODY + " TEXT" + ")";
        db.execSQL(CREATE_COMMENT_TABLE);


    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);

        // Create tables again
        onCreate(db);
    }

    /***************** POST TABLE ***********************/
    // Adding new POST
    public void addPost(Post post) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, post.getId());
        values.put(KEY_USER_ID, post.getUserId());
        values.put(KEY_TITLE, post.getTitle());
        values.put(KEY_BODY, post.getBody());

        // Inserting Row
        db.insert(TABLE_POST, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    public Post getPost(int id) {

        // creating the query
        String selectQuery = "SELECT * FROM " + TABLE_POST + " WHERE "
                + KEY_ID + " = " + id;

        Log.e("DataBaseHelper", selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Post post = new Post();
        // getting the values from cursor
        post.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        post.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
        post.setBody(c.getString(c.getColumnIndex(KEY_BODY)));
        post.setUserId(c.getInt(c.getColumnIndex(KEY_USER_ID)));

        db.close(); // Closing database connection

        return post;
    }

    // Getting All Post
    public ArrayList<Post> getAllPost() {
        ArrayList<Post> postList = new ArrayList<Post>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_POST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Post post = new Post();
                post.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                post.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
                post.setBody(c.getString(c.getColumnIndex(KEY_BODY)));
                post.setUserId(c.getInt(c.getColumnIndex(KEY_USER_ID)));
                // Adding contact to list
                postList.add(post);
            } while (c.moveToNext());
        }

        db.close(); // Closing database connection

        // return contact list
        return postList;
    }

    /***************** USER TABLE ***********************/
    // Adding new USER
        public void addUser(User user) {

        // check if the user is already saved
        User tempUser = getUser(user.getId());
        if(tempUser!= null){
            return;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, user.getId());
        values.put(KEY_NAME, user.getName());
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_STREET, user.getAddress().getStreet());
        values.put(KEY_SUITE, user.getAddress().getSuite());
        values.put(KEY_CITY, user.getAddress().getCity());
        values.put(KEY_ZIPCODE, user.getAddress().getZipcode());
        values.put(KEY_LAT, user.getAddress().getGeo().getLat());
        values.put(KEY_LNG, user.getAddress().getGeo().getLng());
        values.put(KEY_PHONE, user.getPhone());
        values.put(KEY_WEBSITE, user.getWebsite());
        values.put(KEY_COMPANY_NAME, user.getCompany().getName());
        values.put(KEY_COMPANY_CATCH_PHRASE, user.getCompany().getCatchPhrase());
        values.put(KEY_COMPANY_BS, user.getCompany().getBs());

        // Inserting Row
        db.insert(TABLE_USERS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single USER
    public User getUser(int id) {

        // creating the query
        String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE "
                + KEY_ID + " = " + id;

        Log.e("DataBaseHelper", selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        User user = null;

        if (c.moveToFirst()) {
            user = new User();
            // getting the values from cursor
            user.setId(c.getInt(c.getColumnIndex(KEY_ID)));
            user.setName(c.getString(c.getColumnIndex(KEY_NAME)));
            user.setUsername(c.getString(c.getColumnIndex(KEY_USERNAME)));
            user.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
            user.getAddress().setStreet(c.getString(c.getColumnIndex(KEY_STREET)));
            user.getAddress().setSuite(c.getString(c.getColumnIndex(KEY_SUITE)));
            user.getAddress().setCity(c.getString(c.getColumnIndex(KEY_CITY)));
            user.getAddress().setZipcode(c.getString(c.getColumnIndex(KEY_ZIPCODE)));
            user.getAddress().getGeo().setLat(c.getDouble(c.getColumnIndex(KEY_LAT)));
            user.getAddress().getGeo().setLng(c.getDouble(c.getColumnIndex(KEY_LNG)));
            user.setPhone(c.getString(c.getColumnIndex(KEY_PHONE)));
            user.setWebsite(c.getString(c.getColumnIndex(KEY_WEBSITE)));
            user.getCompany().setName(c.getString(c.getColumnIndex(KEY_COMPANY_NAME)));
            user.getCompany().setCatchPhrase(c.getString(c.getColumnIndex(KEY_COMPANY_CATCH_PHRASE)));
            user.getCompany().setBs(c.getString(c.getColumnIndex(KEY_COMPANY_BS)));
        }

        db.close(); // Closing database connection

        return user;
    }

    // Getting single USER email
    public String getEmailUser(int id) {

        // creating the query
        String selectQuery = "SELECT " + KEY_EMAIL + " FROM " + TABLE_USERS + " WHERE "
                + KEY_ID + " = " + id;

        Log.e("DataBaseHelper", selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        String userEmail = null;

        if (c.moveToFirst()) {
            // getting the values from cursor
            userEmail = c.getString(c.getColumnIndex(KEY_EMAIL));
        }

        db.close(); // Closing database connection

        return userEmail;
    }

    // Getting All Post
    public ArrayList<User> getAllUser() {
        ArrayList<User> userList = new ArrayList<User>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                User user = new User();
                user.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                user.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                user.setUsername(c.getString(c.getColumnIndex(KEY_USERNAME)));
                user.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
                user.getAddress().setStreet(c.getString(c.getColumnIndex(KEY_STREET)));
                user.getAddress().setSuite(c.getString(c.getColumnIndex(KEY_SUITE)));
                user.getAddress().setCity(c.getString(c.getColumnIndex(KEY_CITY)));
                user.getAddress().setZipcode(c.getString(c.getColumnIndex(KEY_ZIPCODE)));
                user.getAddress().getGeo().setLat(c.getDouble(c.getColumnIndex(KEY_LAT)));
                user.getAddress().getGeo().setLng(c.getDouble(c.getColumnIndex(KEY_LNG)));
                user.setPhone(c.getString(c.getColumnIndex(KEY_PHONE)));
                user.setWebsite(c.getString(c.getColumnIndex(KEY_WEBSITE)));
                user.getCompany().setName(c.getString(c.getColumnIndex(KEY_COMPANY_NAME)));
                user.getCompany().setCatchPhrase(c.getString(c.getColumnIndex(KEY_COMPANY_CATCH_PHRASE)));
                user.getCompany().setBs(c.getString(c.getColumnIndex(KEY_COMPANY_BS)));
                // Adding USER to list
                userList.add(user);
            } while (c.moveToNext());
        }

        db.close(); // Closing database connection

        // return user list
        return userList;
    }


    /********************* COMMENTS ***************************/
    // Getting All comment By postId
    public ArrayList<Comment> getAllCommentByPostId(int postId) {
        ArrayList<Comment> commentList = new ArrayList<Comment>();
        // Select All Query
        // creating the query
        String selectQuery = "SELECT * FROM " + TABLE_COMMENTS + " WHERE "
                + KEY_POST_ID + " = " + postId;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Comment comment = new Comment();
                comment.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                comment.setPostId(c.getInt(c.getColumnIndex(KEY_POST_ID)));
                comment.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                comment.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
                comment.setBody(c.getString(c.getColumnIndex(KEY_BODY)));
                // Adding COMMENT to list
                commentList.add(comment);
            } while (c.moveToNext());
        }

        db.close(); // Closing database connection
        // return comment list
        return commentList;
    }

    // Getting Number of Comments by PostId
    public int getCommentsCountByPostId(int postId) {
        String countQuery = "SELECT * FROM " + TABLE_COMMENTS + " WHERE "
                + KEY_POST_ID + " = " + postId;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    //  all this method are not needed in this app at the moment
    // Adding new COMMENTS
    public void addComment(Comment comment) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, comment.getId());
        values.put(KEY_POST_ID, comment.getPostId());
        values.put(KEY_NAME, comment.getName());
        values.put(KEY_EMAIL, comment.getEmail());
        values.put(KEY_BODY, comment.getBody());

        // Inserting Row
        db.insert(TABLE_COMMENTS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single Comment
    public Comment getComment(int id) {

        // creating the query
        String selectQuery = "SELECT * FROM " + TABLE_COMMENTS + " WHERE "
                + KEY_ID + " = " + id;

        Log.e("DataBaseHelper", selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Comment comment = new Comment();
        // getting the values from cursor
        comment.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        comment.setPostId(c.getInt(c.getColumnIndex(KEY_POST_ID)));
        comment.setName(c.getString(c.getColumnIndex(KEY_NAME)));
        comment.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
        comment.setBody(c.getString(c.getColumnIndex(KEY_BODY)));

        db.close(); // Closing database connection
        return comment;
    }

}
