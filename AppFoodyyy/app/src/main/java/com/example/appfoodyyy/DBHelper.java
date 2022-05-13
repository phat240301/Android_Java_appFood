package com.example.appfoodyyy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;


import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class DBHelper extends SQLiteOpenHelper {

    private static String dbname = "test2.db";
    private static int dbVersion = 1;

    private static String roleTable = "role";
    private static String foodTable = "food";
    private static String postTable = "post";
    private static String userTable = "user";
    private static String likeTable = "likes";
    private static String commentTable = "comment";

    private static String idColumn = "id";
    private static String nameColumn = "name";
    private static String priceColumn = "price";
    private static String descriptionColumn = "description";
    private static String addressColumn = "address";
    private static String rolesIdColumn = "roleId";
    private static String imageColumn = "images";

    private static String usernameColumn = "username";
    private static String passwordColumn = "password";

    private static String useridColumn = "userId";
    private static String foodIdColumn = "foodId";
    private static String contentColumn = "content";

    private static String postIdColumn = "postId";
    private static String scoreColumn = "score";

    public DBHelper(@Nullable Context context) {
        super(context, dbname, null, dbVersion);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + roleTable + "(" +
                idColumn + " integer primary key autoincrement, " +
                nameColumn + " text" +
                ")");

        sqLiteDatabase.execSQL("create table " + userTable + "(" +
                idColumn + " integer primary key autoincrement, " +
                usernameColumn + " text, " +
                nameColumn + " text, " +
                addressColumn + " text, " +
                rolesIdColumn + " integer references " + roleTable + "(" + idColumn + "), " +
                passwordColumn + " text, " +
                imageColumn + " blob" +
                ")");

        sqLiteDatabase.execSQL("create table " + foodTable + "(" +
                idColumn + " integer primary key autoincrement, " +
                nameColumn + " text, " +
                priceColumn + " real, " +
                descriptionColumn + " text, " +
                imageColumn + " blob, " +
                useridColumn + " integer references " + userTable + "(" + idColumn + ")" +
                ")");

        sqLiteDatabase.execSQL("create table " + postTable + "(" +
                idColumn + " integer primary key autoincrement, " +
                contentColumn + " text, " +
                foodIdColumn + " integer references " + foodTable + "(" + idColumn + "), " +
                useridColumn + " integer references " + userTable + "(" + idColumn + "), " +
                scoreColumn + " real" +
                ")");

        sqLiteDatabase.execSQL("create table " + likeTable + "(" +
                idColumn + " integer primary key autoincrement, " +
                postIdColumn + " integer references " + postTable + "(" + idColumn + "), " +
                useridColumn + " integer references " + userTable + "(" + idColumn + ")" +
                ")");

        sqLiteDatabase.execSQL("create table " + commentTable + "(" +
                idColumn + " integer primary key autoincrement, " +
                contentColumn + " text, " +
                postIdColumn + " integer references " + postTable + "(" + idColumn + "), " +
                useridColumn + " integer references " + userTable + "(" + idColumn + ")" +
                ")");
    }

    public void QueryData(String sql) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public Cursor GetData(String sql) {
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + roleTable);
        sqLiteDatabase.execSQL("drop table if exists " + foodTable);
        sqLiteDatabase.execSQL("drop table if exists " + userTable);
        sqLiteDatabase.execSQL("drop table if exists " + postTable);
        sqLiteDatabase.execSQL("drop table if exists " + likeTable);
        sqLiteDatabase.execSQL("drop table if exists " + commentTable);
        onCreate(sqLiteDatabase);
    }

    public List<User> findAllUser(){
        List<User> users = null;
        try {
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select * from " + userTable,null );
            if (cursor.moveToFirst()){
                users = new ArrayList<User>();
                do{
                    User user = new User();
                    user.setId(cursor.getInt(0));
                    user.setUsername(cursor.getString(1));
                    user.setName(cursor.getString(2));
                    user.setAddress(cursor.getString(3));
                    user.setRoles(cursor.getInt(4));
                    user.setPassword(cursor.getString(5));
                    users.add(user);
                }while (cursor.moveToNext());
            }
        }catch (Exception e){
            users = null;
        }
        return users;
    }

    public List<Role> findAllRole() {
        List<Role> roles = null;
        try {
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select * from " + roleTable, null);
            if (cursor.moveToFirst()) {
                roles = new ArrayList<Role>();
                do {
                    Role role = new Role();
                    role.setId(cursor.getInt(0));
                    role.setName(cursor.getString(1));
                    roles.add(role);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            roles = null;
        }
        return roles;
    }



    //Kiểm tra username đã tồn tại trong bảng dữ liệu hay chưa
    public boolean checkUserName(String username) {
        //Gọi phương thức getWritableDatabase để đọc và ghi dữ liệu vào DB
        SQLiteDatabase db = this.getWritableDatabase();
        //Truy cập đến kết quả truy vấn
        Cursor cursor = db.rawQuery("select * from restaurant where username=?", new String[]{username});
        //Kiểm tra tên người dùng tồn tại
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }
//    usernameColumn + " text, " +
//    nameColumn + " text, " +
//    addressColumn + " text, " +
//    rolesIdColumn + " integer references " + roleTable + "(" + idColumn + "), " +
//    passwordColumn + " text, " +
//    imageColumn + " blob" +
    //Them du lieu vao restaurant
    public boolean insertDataUser(String username, String fullname, String address, int role, String password, byte[] image) {
        //Gọi phương thức getWritableDatabase để đọc và ghi dữ liệu vào DB
        SQLiteDatabase db = this.getWritableDatabase();
        // Tạo một đối tượng thuộc ContentValues để chèn(put) vào dữ liệu(column)
        ContentValues values = new ContentValues();

        //Đưa vào column username với giá trị username
        values.put(usernameColumn, username);
        values.put(nameColumn, fullname);
        values.put(addressColumn, address);
        values.put(rolesIdColumn, role);
        values.put(passwordColumn, password);
        values.put(imageColumn, image);

        // nullColumnHack: các cột có thể nhận giá trị NUll, trừ khóa chính
        Long result = db.insert(userTable, null, values);
        //Nếu không chèn được dữ liệu trả về false
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    //Kiểm tra username đã tồn tại trong bảng dữ liệu hay chưa
    public boolean checkUserNameUser(String username) {
        //Gọi phương thức getWritableDatabase để đọc và ghi dữ liệu vào DB
        SQLiteDatabase db = this.getWritableDatabase();
        //Truy cập đến kết quả truy vấn
        Cursor cursor = db.rawQuery("select * from user where username=?", new String[]{username});
        //Kiểm tra tên người dùng tồn tại
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    //    usernameColumn + " text, " +
//    nameColumn + " text, " +
//    addressColumn + " text, " +
//    rolesIdColumn + " integer references " + roleTable + "(" + idColumn + "), " +
//    passwordColumn + " text, " +
//    imageColumn + " blob" +
    //Them du lieu vao restaurant
    public User CheckUsernameProfile(String username){
        User user = null;
        try{
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select * from  "+ userTable +" where  username = ?", new String[] { username});
            if(cursor.moveToFirst())
            {
                user = new User();
                user.setId(cursor.getInt(0));
                user.setUsername(cursor.getString(1));
                user.setName(cursor.getString(2));
                user.setAddress(cursor.getString(3));
                user.setRoles(cursor.getInt(4));
                user.setPassword(cursor.getString(5));
                user.setImage(cursor.getBlob(6));
            }
        }catch(Exception e)
        {
            user = null;
        }
        return user;
    }
    public boolean update (User user)
    {
        boolean result = true;
        try{
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(usernameColumn, user.getUsername());
            contentValues.put(nameColumn, user.getName());
            contentValues.put(addressColumn, user.getAddress());
            contentValues.put(rolesIdColumn, user.getRoles());
            contentValues.put(passwordColumn, user.getPassword());
            contentValues.put(imageColumn, user.getImage());

            result = sqLiteDatabase.update(userTable,contentValues,idColumn + " = ?",new String[] {String.valueOf(user.getId()) }) > 0;
        }catch(Exception e)
        {
            result = false;

        }
        return result;
    }

    public User loginUser(String username, String password) {
        User user = null;
        try {
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select * from  " + userTable + " where  username = ? and password = ?", new String[]{username, password});
            if (cursor.moveToFirst()) {
                user = new User();
                user.setId(cursor.getInt(0));
                user.setName(cursor.getString(1));
                user.setUsername(cursor.getString(2));
                user.setAddress(cursor.getString(3));
                user.setRoles(cursor.getInt(4));
                user.setPassword(cursor.getString(5));
            }
        } catch (Exception e) {
            user = null;
        }
        return user;
    }

    public String getIdRes(int id) {
        String s = null;
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + userTable, null);
        Cursor cursorFood = sqLiteDatabase.rawQuery("select * from " + foodTable, null);
        if (cursorFood.moveToFirst()) {
            do {
                    if(cursor.moveToFirst()){
                        do {
                            if (id == cursorFood.getInt(0))
                            {
                                if(cursorFood.getInt(5) == cursor.getInt(0)){
                                    s = cursor.getString(2);
                                }
                            }
                        }while (cursor.moveToNext());
                    }
            } while (cursorFood.moveToNext());
        }
        return s;
    }

    public int getRoles(int id) {
        int role = 0;
        SQLiteDatabase data = getReadableDatabase();
        Cursor cursor = data.rawQuery("select * from " + userTable, null);
            if (cursor.moveToFirst()) {
                do {
                    if (cursor.getInt(0) == id) {
                        if (cursor.getInt(4) == 1) {
                            role = 1;
                        } else
                            role = 2;
                    }
                } while (cursor.moveToNext());
            }
        return role;
    }

    public boolean createFood(Food food) {
        try {
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(nameColumn, food.getName());
            contentValues.put(priceColumn, food.getPrice());
            contentValues.put(descriptionColumn, food.getDescription());
            contentValues.put(imageColumn, food.getImage());
            contentValues.put(useridColumn, food.getUsertId());
            return sqLiteDatabase.insert(foodTable, null, contentValues) > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Food> findAllFood() {
        List<Food> foods = null;
        try {
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("Select * from " + foodTable, null);
            if (cursor.moveToFirst()) {
                foods = new ArrayList<Food>();
                do {
                    Food food = new Food();
                    food.setId(cursor.getInt(0));
                    food.setName(cursor.getString(1));
                    food.setPrice(cursor.getDouble(2));
                    food.setDescription(cursor.getString(3));
                    food.setImage(cursor.getBlob(4));
                    food.setUsertId(cursor.getInt(5));
                    foods.add(food);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            foods = null;
        }
        return foods;
    }

    //    usernameColumn + " text, " +
//    nameColumn + " text, " +
//    addressColumn + " text, " +
//    rolesIdColumn + " integer references " + roleTable + "(" + idColumn + "), " +
//    passwordColumn + " text, " +
//    imageColumn + " blob" +
    public User find(int id){
        User user = null;
        try{
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select * from " + userTable + " where  id = ?",
                    new String[] { String.valueOf(id)});
            if(cursor.moveToFirst())
            {
                user = new User();
                user.setId(cursor.getInt(0));
                user.setUsername(cursor.getString(1));
                user.setName(cursor.getString(2));
                user.setAddress(cursor.getString(3));
                user.setRoles(cursor.getInt(4));
                user.setPassword(cursor.getString(5));
                user.setImage(cursor.getBlob(6));

            }
        }catch(Exception e)
        {
            user = null;
        }
        return user;
    }

    public void UPDATE_FOODS(String name, String price, String Mota, byte[] image, int idUser, int id) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "UPDATE food SET name = ?, price = ?, description = ?, images = ?, userId = ? WHERE id = ?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, name);
        statement.bindString(2, price);
        statement.bindString(3, Mota);
        statement.bindBlob(4, image);
        statement.bindLong(5, idUser);
        statement.bindDouble(6, (double) id);
        statement.execute();
        database.close();
    }

    public List<Post> findAllPost() {
        List<Post> posts = null;
        try {
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("Select * from " + postTable, null);
            if (cursor.moveToFirst()) {
                posts = new ArrayList<Post>();
                do {
                    Post post = new Post();
                    post.setId(cursor.getInt(0));
                    post.setContent(cursor.getString(1));
                    post.setFoodId(cursor.getInt(2));
                    post.setUserId(cursor.getInt(3));
                    post.setScore(cursor.getDouble(4));
                    posts.add(post);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            posts = null;
        }
        return posts;
    }

    public String getIdUser (int idUser) {
        String sUser = null;
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + userTable, null);
        {
            if (cursor.moveToFirst()) {
                do {
                    if(idUser == cursor.getInt(0)){
                        sUser = cursor.getString(2);
                    }
                } while (cursor.moveToNext());
            }
        }
        return sUser;
    }

    public String getIdFood (int idFood) {
        String sFood = null;
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + foodTable, null);
        {
            if (cursor.moveToFirst()) {
                do {
                    if(idFood == cursor.getInt(0)){
                        sFood = cursor.getString(1);
                    }
                } while (cursor.moveToNext());
            }
        }
        return sFood;
    }

    public double getIdScore (int id) {
        double d = 0;
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + postTable, null);
        {
            if (cursor.moveToFirst()) {
                do {
                    if(id == cursor.getInt(0)){
                        d = cursor.getDouble(4);
                    }
                } while (cursor.moveToNext());
            }
        }
        return d;
    }

    public int countIdUser (int idPost) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select count(userID) from likes WHERE postId = '"+idPost+"'", null);
        cursor.moveToNext();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public int countComment (int idPost) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select count(userID) from comment WHERE "+postIdColumn+" = "+idPost, null);
        cursor.moveToNext();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public Like findUserIdAndPostId(int userId, int postId){
        Like like = null;
        try{
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select * from  "+likeTable+" where  userId = ? and postId = ?", new String[] { String.valueOf(userId), String.valueOf( postId)});
            if(cursor.moveToFirst())
            {
                like = new Like();
                like.setId(cursor.getInt(0));
                like.setPostId(cursor.getInt(1));
                like.setUserId(cursor.getInt(2));
            }
        }catch(Exception e)
        {
            like = null;
        }
        return like;
    }

    public  boolean createLike(Like like){
        try {
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(postIdColumn, like.getPostId());
            contentValues.put(useridColumn, like.getUserId());
            return sqLiteDatabase.insert(likeTable, null, contentValues) > 0;
        }catch (Exception e){
            return false;
        }
    }

    public ArrayList<Comment> displayData(int id) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from comment where "+postIdColumn+" = "+id,null);
        ArrayList<Comment> modelArrayList = new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                modelArrayList.add(new Comment(cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getInt(3)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return modelArrayList;
    }

    public Cursor ReadPostByIDpost(int id) {
        String query = "SELECT * FROM "+ postTable+" WHERE "+idColumn+" = "+id;
        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = null;
        if(database!=null){
            cursor = database.rawQuery(query,null);
        }
        return cursor;
    }

    public  boolean createPost(Post post){
        try {
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(contentColumn, post.getContent());
            contentValues.put(foodIdColumn, post.getFoodId());
            contentValues.put(useridColumn, post.getUserId());
            contentValues.put(scoreColumn, post.getScore());
            return sqLiteDatabase.insert(postTable, null, contentValues) > 0;
        }catch (Exception e){
            return false;
        }
    }

    public Cursor ReadFoodByIDFood(int id) {
        String query = "SELECT * FROM "+ postTable+" WHERE "+foodIdColumn+" = "+id;
        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = null;
        if(database!=null){
            cursor = database.rawQuery(query,null);
        }
        return cursor;
    }

    public List<Post> findAllPostByIdFood(int idFood) {
        List<Post> posts = null;
        try {
            SQLiteDatabase sqLiteDatabase = getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select * from post where foodId = '"+idFood+"'", null);
            if (cursor.moveToFirst()) {
                posts = new ArrayList<Post>();
                do {
                    Post post = new Post();
                    post.setId(cursor.getInt(0));
                    post.setContent(cursor.getString(1));
                    post.setFoodId(cursor.getInt(2));
                    post.setUserId(cursor.getInt(3));
                    post.setScore(cursor.getDouble(4));
                    posts.add(post);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            posts = null;
        }
        return posts;
    }

    public int returnIdFood (String nameFood) {
        int u = 0;
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor1 = sqLiteDatabase.rawQuery("select * from " + userTable, null);
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + foodTable, null);
        {
            if (cursor.moveToFirst()) {
                do {
                    if (cursor1.moveToFirst()){
                        do {
                            if (cursor1.getString(2).equals(nameFood))
                            {
                                if(cursor1.getInt(0) == cursor.getInt(5)){
                                    u = cursor.getInt(0);
                                }
                            }
                        }while (cursor1.moveToNext());
                    }
                } while (cursor.moveToNext());
            }
        }
        return u;
    }

    public Cursor ReadUser(int id) {
        String query = "SELECT * FROM "+ userTable +" WHERE "+idColumn+" = "+id;
        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = null;
        if(database!=null){
            cursor = database.rawQuery(query,null);
        }
        return cursor;
    }
}