package com.bbld.yxpt.dbSearch;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class UserDataBaseOperate {

	private static final String TAG = "DBSearch";
	private static final boolean DEBUG = true;

	protected SQLiteDatabase mDB = null;

	public UserDataBaseOperate(SQLiteDatabase db) {
		if (null == db) {
			throw new NullPointerException("The db cannot be null.");
		}
		mDB = db;
	}

	public long insertToUser(SearchDBBean searchBean) {
		ContentValues values = new ContentValues();
		values.put(UserSQLiteOpenHelper.COL_SHOP_NAME, searchBean.getName());
		values.put(UserSQLiteOpenHelper.COL_SHOP_ADDR, searchBean.getAddr());
		values.put(UserSQLiteOpenHelper.COL_SHOP_TYPE, searchBean.getType());
		values.put(UserSQLiteOpenHelper.COL_SHOP_SHOPX, searchBean.getPointX());
		values.put(UserSQLiteOpenHelper.COL_SHOP_SHOPY, searchBean.getPointY());
		return mDB.insert(UserSQLiteOpenHelper.DATABASE_TABLE_USER, null,
				values);
	}

	public long updateName(SearchDBBean searchBean) {
		ContentValues values = new ContentValues();
		values.put(UserSQLiteOpenHelper.COL_SHOP_NAME, searchBean.getName());
		values.put(UserSQLiteOpenHelper.COL_SHOP_ADDR, searchBean.getAddr());
		values.put(UserSQLiteOpenHelper.COL_SHOP_TYPE, searchBean.getType());
		values.put(UserSQLiteOpenHelper.COL_SHOP_SHOPX, searchBean.getPointX());
		values.put(UserSQLiteOpenHelper.COL_SHOP_SHOPY, searchBean.getPointY());
		return mDB.update(UserSQLiteOpenHelper.DATABASE_TABLE_USER, values,
				"shopname=?", new String[] { ""+searchBean.getName() });
	}

	// clear databases
	public long deleteAll() {
		return mDB.delete(UserSQLiteOpenHelper.DATABASE_TABLE_USER, null, null);
	}

	public long deleteUserByname(String name){
		return mDB.delete("yxpt_search_info", "shopname=?", new String[]{name});
	}

	public long getCount(String conditions, String[] args) {
		long count = 0;
		if (TextUtils.isEmpty(conditions)) {
			conditions = " 1 = 1 ";
		}
		StringBuilder builder = new StringBuilder();
		builder.append("SELECT COUNT(1) AS count FROM ");
		builder.append(UserSQLiteOpenHelper.DATABASE_TABLE_USER).append(" ");
		builder.append("WHERE ");
		builder.append(conditions);
		if (DEBUG)
			Log.d(TAG, "SQL: " + builder.toString());
		Cursor cursor = mDB.rawQuery(builder.toString(), args);
		if (null != cursor) {
			if (cursor.moveToNext()) {
				count = cursor.getLong(cursor.getColumnIndex("count"));
			}
			cursor.close();
		}
		return count;
	}
	public List<SearchDBBean> findAll() {
		List<SearchDBBean> searchBeans = new ArrayList<SearchDBBean>();
		//order by modifytime desc
		Cursor cursor = mDB.query(UserSQLiteOpenHelper.DATABASE_TABLE_USER,
				null, null, null, null, null, UserSQLiteOpenHelper.COL_ID
						+ " asc");
		if (null != cursor) {
			while (cursor.moveToNext()) {
				SearchDBBean searchBean = new SearchDBBean();
				searchBean.setName(cursor.getString(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_SHOP_NAME)));
				searchBean.setAddr(cursor.getString(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_SHOP_ADDR)));
				searchBean.setType(cursor.getInt(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_SHOP_TYPE)));
				searchBean.setPointX(cursor.getString(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_SHOP_SHOPX)));
				searchBean.setPointY(cursor.getString(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_SHOP_SHOPY)));
				searchBeans.add(searchBean);
			}
			cursor.close();
		}
		return searchBeans;
	}
	public SearchDBBean findUserLatest() {
		SearchDBBean searchBean = null;
		Cursor cursor = mDB.query(UserSQLiteOpenHelper.DATABASE_TABLE_USER,
				null, null, null, null, null, UserSQLiteOpenHelper.COL_ID
						+ " asc");
		if (null != cursor) {
			if (cursor.moveToFirst()) {
				searchBean = new SearchDBBean();
				searchBean.setName(cursor.getString(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_SHOP_NAME)));
				searchBean.setAddr(cursor.getString(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_SHOP_ADDR)));
				searchBean.setType(cursor.getInt(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_SHOP_TYPE)));
				searchBean.setPointX(cursor.getString(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_SHOP_SHOPX)));
				searchBean.setPointY(cursor.getString(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_SHOP_SHOPY)));
			}
			cursor.close();
		}
		return searchBean;
	}

	public List<SearchDBBean> findUserByName(String name) {

		List<SearchDBBean> searchBeans = new ArrayList<SearchDBBean>();
		//模糊查询			
//		Cursor cursor = mDB.query(UserSQLiteOpenHelper.DATABASE_TABLE_USER,
//				null, UserSQLiteOpenHelper.COL_PRODUCT_ID + " like?",
//				new String[] {"%"+productId+"%"}, null, null, UserSQLiteOpenHelper.COL_ID
//				+ " asc");
		
		Cursor cursor = mDB.query(UserSQLiteOpenHelper.DATABASE_TABLE_USER,
			null, UserSQLiteOpenHelper.COL_SHOP_NAME + " =?",
			new String[] {name}, null, null, UserSQLiteOpenHelper.COL_ID
			+ " asc");
		
		//多个条件查询
//		Cursor cursor = mDB.query(UserSQLiteOpenHelper.DATABASE_TABLE_USER,
//				null, UserSQLiteOpenHelper.COL_NAME + " like?"+" and "+UserSQLiteOpenHelper.COL_ID+" >?",
//				new String[] {"%"+name+"%",2+""}, null, null, UserSQLiteOpenHelper.COL_ID
//				+ " desc");
		if (null != cursor) {
			while (cursor.moveToNext()) {
				SearchDBBean searchBean = new SearchDBBean();
				searchBean.setName(cursor.getString(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_SHOP_NAME)));
				searchBean.setAddr(cursor.getString(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_SHOP_ADDR)));
				searchBean.setType(cursor.getInt(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_SHOP_TYPE)));
				searchBean.setPointX(cursor.getString(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_SHOP_SHOPX)));
				searchBean.setPointY(cursor.getString(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_SHOP_SHOPY)));
				searchBeans.add(searchBean);
			}
			cursor.close();
		}
		return searchBeans;
	}
	public List<SearchDBBean> findUserByAddr(String shopaddr) {

		List<SearchDBBean> searchBeans = new ArrayList<SearchDBBean>();
		//模糊查询
//		Cursor cursor = mDB.query(UserSQLiteOpenHelper.DATABASE_TABLE_USER,
//				null, UserSQLiteOpenHelper.COL_PRODUCT_ID + " like?",
//				new String[] {"%"+productId+"%"}, null, null, UserSQLiteOpenHelper.COL_ID
//				+ " asc");

		Cursor cursor = mDB.query(UserSQLiteOpenHelper.DATABASE_TABLE_USER,
			null, UserSQLiteOpenHelper.COL_SHOP_ADDR + " =?",
			new String[] {shopaddr}, null, null, UserSQLiteOpenHelper.COL_ID
			+ " asc");

		//多个条件查询
//		Cursor cursor = mDB.query(UserSQLiteOpenHelper.DATABASE_TABLE_USER,
//				null, UserSQLiteOpenHelper.COL_NAME + " like?"+" and "+UserSQLiteOpenHelper.COL_ID+" >?",
//				new String[] {"%"+name+"%",2+""}, null, null, UserSQLiteOpenHelper.COL_ID
//				+ " desc");
		if (null != cursor) {
			while (cursor.moveToNext()) {
				SearchDBBean searchBean = new SearchDBBean();
				searchBean.setName(cursor.getString(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_SHOP_NAME)));
				searchBean.setAddr(cursor.getString(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_SHOP_ADDR)));
				searchBean.setType(cursor.getInt(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_SHOP_TYPE)));
				searchBean.setPointX(cursor.getString(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_SHOP_SHOPX)));
				searchBean.setPointY(cursor.getString(cursor
						.getColumnIndex(UserSQLiteOpenHelper.COL_SHOP_SHOPY)));
				searchBeans.add(searchBean);
			}
			cursor.close();
		}
		return searchBeans;
	}
	private static SQLiteDatabase mTestDb;
//	public static void InsertTest(){
//		//
//		mTestDb = SQLiteDatabase.openOrCreateDatabase("/data/data/cn.vn.sqlitedatademo/databases/mmjhcart.db", null);
//		//
//		mTestDb.execSQL("CREATE TABLE IF NOT EXISTS user_info(_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT,pwd TEXT,modifyTime INTEGER)");
//		CartSQLBean cart = new CartSQLBean();
//		cart.setShopId("xiaopihai");
//		cart.setShopName("12345678");
//		cart.setProductId("12");
//		cart.setProductName("66600");
//		cart.setProductPrice("52");
//		cart.setFirstImage("http//");
//		//增加一条数据，因s为有5列，所以需要写5个数据，否则会失败,
//		//第一个数据位null，这是因为它是自动增长的。.
//		mTestDb.execSQL("INSERT INTO user_info VALUES(null,'xiaopihai','12345678',"+cart.getProductId()+","+cart.getProductName()+","+cart.getProductPrice()+","+cart.getFirstImage()+")");
//		//
//		//mTestDb.execSQL("INSERT INTO user_info VALUES(12,'zhangsan','mima1111',"+System.currentTimeMillis()+")");
//		//SQLiteConstraintException: PRIMARY KEY must be unique (code 19)
//		//下面是增加一条数据（只设置某个或某几个内容），默认没添加的数据时空的。
//		mTestDb.execSQL("INSERT INTO user_info(name,pwd) VALUES('lisi','mimajjjj')");
//		mTestDb.execSQL("INSERT INTO user_info VALUES(null,?,?,?)",new Object[]{user.getName(),user.getPwd(),user.getModifyTime()});
//		mTestDb.execSQL("INSERT INTO user_info(name,pwd) VALUES(?,?)",new Object[]{"lisi","mimajjjj"});
//
//		ContentValues values = new ContentValues();
//		values.put(UserSQLiteOpenHelper.COL_NAME,"qwerdf");
//		//values.put(UserSQLiteOpenHelper.COL_PWD, "qwerdflol");
//		values.put(UserSQLiteOpenHelper.COL_TIME, user.getModifyTime());
//		mTestDb.insert("user_info", null,values);
//	}
//
//public static void updateTest(){
//	mTestDb.execSQL("UPDATE user_info SET name = 'update1' WHERE _id = 1;");
//	mTestDb.execSQL("UPDATE user_info SET name =? WHERE _id=?",new Object[]{"update1",1});
//	mTestDb.execSQL("UPDATE user_info SET name = 'update2',modifyTime=111 WHERE _id = 2;");
//	mTestDb.execSQL("UPDATE user_info SET name =? ,modifyTime =? WHERE _id=?",new Object[]{"update1",111,2});
//
//	ContentValues values = new ContentValues();
//	values.put("name","update3");
//	values.put("pwd", "2222222");
//	values.put("modifyTime", 898);
//	mTestDb.update("user_info", values, "_id=?", new String[]{String.valueOf(3)});
//}
//
//public static void deleteTest(){
//	mTestDb.execSQL("delete from user_info where _id=1");
//	//mTestDb.execSQL("delete from user_info where _id=?",new Object[]{1});
//
//	int de = mTestDb.delete("user_info", "_id=?", new String[]{String.valueOf(2)});
//	int de8 = mTestDb.delete("user_info", "_id=?", new String[]{String.valueOf(8)});
//	int deall =mTestDb.delete("user_info", null, null);
//	Log.d(TAG, "de ="+de+" de8="+de8+" deall="+deall);
//}
}
