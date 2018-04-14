package com.example.zhangxiong.fcb;


import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MySqlHandle {
    // mysql连接
    private static String user = "root";
    private static String pass = "fk123456=";
    private static String url = "jdbc:mysql://115.159.79.222:3306/FeiCheBao";
    private static Connection conn;
    private static Statement stmt;
    public static boolean isconnect=false;

    MySqlHandle() {

    }
    public static void connect() {
        //判断api版本(否则无法远程连接mysql数据库)
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        // 定位驱动
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("加载驱动成功!");
        } catch (ClassNotFoundException e) {
            System.out.println("加载驱动失败!");
            e.printStackTrace();
        }
        // 建立连接
        try {
            conn = DriverManager.getConnection(url, user, pass);
            stmt = conn.createStatement();
            isconnect=true;
            System.out.println("数据库连接成功!");
        } catch(SQLException e) {
            System.out.println("数据库连接失败!");
            e.printStackTrace();
        }
    }
    // 登录验证
    public static boolean login(String account,String pass) {
        String sql="select count(*) from fcb where account='"+account+"' and pass='"+pass+"'";
        ResultSet rs;
        boolean res = false;
        try {
            rs = stmt.executeQuery(sql);
            rs.next();
            if (rs.getInt(1) > 0)
                res = true;
            rs.close();
        } catch (SQLException e) {
            System.out.println("登录验证失败!");
            e.printStackTrace();
        }
        return res;
    }
    //添加用户
    public static void addUser(String account,String pass){
        String sql = "insert into fcb (account,pass,name,money) values('"
                + account
                + "','"
                + pass
                + "','默认昵称',"
                + 0 + ")";
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("用户添加失败!");
            e.printStackTrace();
        }
    }
    // 验证用户账号
    public static boolean valiuser(String account) {
        String sql = "select count(*) from fcb where account='" +account+ "'";
        ResultSet rs;
        boolean res = false;
        try {
            rs = stmt.executeQuery(sql);
            rs.next();
            if (rs.getInt(1) > 0)
                res = true;
            rs.close();
        } catch (SQLException e) {
            System.out.println("用户账号验证失败!");
            e.printStackTrace();
        }
        return res;
    }
    //更新用户数据
    public static void updateuser(String account, String col,String value){
        String sql="update fcb set "+col+"='"+value+"' where account='"+account+"'";
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("更新用户"+col+"失败!");
            e.printStackTrace();
        }
    }
    public static void updateaccount(String oldaccount, String newaccount){
        String sql="update record set account='"+newaccount+"' where account='"+oldaccount+"'";
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("更新account失败!");
            e.printStackTrace();
        }
    }
    //获取用户数据
    public static User getuesr(String account){
        User user=new User();
        ResultSet rs = null;
        String sql="select * from fcb where account='"+account+"'";
        try {
            rs = stmt.executeQuery(sql);
            if (rs == null)
                return null;
            while (rs.next()) {
                user.setAccount(rs.getString("account"));
                user.setPass(rs.getString("pass"));
                user.setMoney(rs.getDouble("money"));
                user.setName(rs.getString("name"));
            }
            rs.close();
        }
        catch (SQLException e) {
            System.out.println("数据查询失败!");
            e.printStackTrace();
        }
        return user;
    }
    //添加记录
    public static void addRecord(Record record){
        try {
            String sql = "insert into record(carnumber,account,jindu,weidu,date,address,photo,state) values(?,?,?,?,?,?,?,?)";
            PreparedStatement stmt = null;
            stmt = conn.prepareStatement(sql);
            stmt.setString(1,record.getCarnumber());
            stmt.setString(2,record.getAccount());
            stmt.setDouble(3,record.getJindu());
            stmt.setDouble(4,record.getWeidu());
            stmt.setString(5,record.getDate());
            stmt.setString(6,record.getAddress());
            stmt.setBytes(7, record.getPhoto());
            stmt.setInt(8,record.getState());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("记录添加失败!");
            System.out.println(record.getCarnumber()+record.getAccount()+record.getJindu()
            +record.getWeidu()+record.getDate()+ record.getAddress()+ record.getPhoto()+ record.getState());
            e.printStackTrace();
        }
    }
    // 验证车牌
    public static boolean valicarnum(String carnumber) {

        String sql = "select count(*) from record where carnumber='" +carnumber+ "'";
        ResultSet rs;
        boolean res = false;
        try {
            rs = stmt.executeQuery(sql);
            rs.next();
            if (rs.getInt(1) > 0)
                res = true;
            rs.close();
        } catch (SQLException e) {
            System.out.println("车牌验证失败!");
            e.printStackTrace();
        }
        return res;
    }
    //更新审核状态
    public static void updaterecord(int rid,int value){
        String sql="update record set state="+value+" where rid="+rid;
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("更新审核状态失败!");
            e.printStackTrace();
        }
    }
    //获取某一车牌数据记录
    public static Record getrecord(int rid){
        Record record=new Record();
        ResultSet rs = null;
        String sql="select * from record where rid="+rid;
        try {
            rs = stmt.executeQuery(sql);
            if (rs == null)
                return null;
            while (rs.next()) {
                record.setRid(rs.getInt("rid"));
                record.setAccount(rs.getString("account"));
                record.setCarnumber(rs.getString("carnumber"));
                record.setJindu(rs.getDouble("jindu"));
                record.setWeidu(rs.getDouble("weidu"));
                record.setAddress(rs.getString("address"));
                record.setDate(rs.getString("date"));
                record.setState(rs.getInt("state"));
                record.setPhoto(rs.getBytes("photo"));
            }
            rs.close();
        }
        catch (SQLException e) {
            System.out.println("获取车牌数据记录失败!");
            e.printStackTrace();
        }
        return record;
    }
    //获取某一车牌除图片数据记录
    public static Record getrecordnop(int rid){
        Record record=new Record();
        ResultSet rs = null;
        String sql="select rid,account,carnumber,jindu,weidu,address,date,state from record where rid="+rid;
        try {
            rs = stmt.executeQuery(sql);
            if (rs == null)
                return null;
            while (rs.next()) {
                record.setRid(rs.getInt("rid"));
                record.setAccount(rs.getString("account"));
                record.setCarnumber(rs.getString("carnumber"));
                record.setJindu(rs.getDouble("jindu"));
                record.setWeidu(rs.getDouble("weidu"));
                record.setAddress(rs.getString("address"));
                record.setDate(rs.getString("date"));
                record.setState(rs.getInt("state"));
            }
            rs.close();
        }
        catch (SQLException e) {
            System.out.println("获取车牌数据记录失败!");
            e.printStackTrace();
        }
        return record;
    }
    //获取用户所有记录数据
    public static ArrayList<Record> getAllrecord(String account){
        ArrayList<Record> data=new ArrayList<Record>();
        ResultSet rs = null;
        String sql="select rid,account,carnumber,jindu,weidu,address,date,state from record where account='"+account+"'";
        try {
            rs = stmt.executeQuery(sql);
            if (rs == null)
                return null;
            while (rs.next()) {
                Record record=new Record();
                record.setRid(rs.getInt("rid"));
                record.setAccount(rs.getString("account"));
                record.setCarnumber(rs.getString("carnumber"));
                record.setJindu(rs.getDouble("jindu"));
                record.setWeidu(rs.getDouble("weidu"));
                record.setAddress(rs.getString("address"));
                record.setDate(rs.getString("date"));
                record.setState(rs.getInt("state"));
                //record.setPhoto(rs.getBytes("photo"));
                data.add(record);
            }
            rs.close();
        }
        catch (SQLException e) {
            System.out.println("获取所有记录失败!");
            e.printStackTrace();
        }
        return data;
    }

    //关闭数据库连接
    public static void closeconnect(){
        try {
            conn.close();
            stmt.close();
            isconnect=false;
            System.out.println("关闭连接成功!");
        } catch (SQLException e) {
            System.out.println("关闭连接失败!");
        }
    }
}
