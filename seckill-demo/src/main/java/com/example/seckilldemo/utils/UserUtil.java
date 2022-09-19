package com.example.seckilldemo.utils;

import com.example.seckilldemo.entity.TUser;
import com.example.seckilldemo.vo.RespBean;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 生成用户工具类
 *
 * @author: LC
 * @date 2022/3/4 3:29 下午
 * @ClassName: UserUtil
 */
public class UserUtil {

    private static void createUser(int count) throws Exception {
        List<TUser> users = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            TUser user = new TUser();
            user.setLoginCount(1);
            user.setId(1300000000L + i);
            user.setNickname("user" + i);
            user.setSalt("1a2b3c");
            user.setPassword(MD5Util.inputPassToDBPass("123456", user.getSalt()));
            user.setRegisterDate(new Date());
            users.add(user);
        }

        System.out.println("create user");

        Connection conn = getConn();

        String sql = "insert into t_user(login_count, nickname, register_date, salt, password,id) values (?,?,?,?,?,?)";

        PreparedStatement preparedStatement = conn.prepareStatement(sql);

        for (TUser user : users) {
            preparedStatement.setInt(1, user.getLoginCount());
            preparedStatement.setString(2, user.getNickname());
            preparedStatement.setTimestamp(3, new Timestamp(user.getRegisterDate().getTime()));
            preparedStatement.setString(4, user.getSalt());
            preparedStatement.setString(5, user.getPassword());
            preparedStatement.setLong(6, user.getId());
            preparedStatement.addBatch();
        }

        preparedStatement.executeBatch();
        preparedStatement.clearParameters();
        conn.close();
        System.out.println("insert into db");

        String loginUrl = "http://localhost:8080/login/doLogin";
        File file = new File("/Users/csk/Desktop/data/config.txt");
        if (file.exists()) {
            file.delete();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(0);
        for (TUser user : users) {
            URL url = new URL(loginUrl);
            HttpURLConnection co = (HttpURLConnection) url.openConnection();
            co.setRequestMethod("POST");
            co.setDoOutput(true);
            OutputStream outputStream = co.getOutputStream();
            String params = "mobile=" + user.getId() + "&password=" + MD5Util.inputPassToFromPass("123456");
            outputStream.write(params.getBytes());
            outputStream.flush();
            InputStream inputStream = co.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buff)) >= 0) {
                bout.write(buff, 0, len);
            }
            inputStream.close();
            bout.close();

            String response = bout.toString();

            ObjectMapper objectMapper = new ObjectMapper();
            RespBean respBean = objectMapper.readValue(response, RespBean.class);
            String userTicket = (String) respBean.getObject();
            System.out.println("create userTicket:" + user.getId());
            String row = user.getId() + "," + userTicket;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
            System.out.println("write to file" + user.getId());
        }

        raf.close();
        System.out.println("over");

    }

    /**
     * insert to db
     *
     * @return conn
     * @throws Exception e
     */
    private static Connection getConn() throws Exception {
        String url = "jdbc:mysql://127.0.0.1:12345/seckill?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "123456";
        String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }

    public static void main(String[] args) throws Exception {
        createUser(10);
    }
}
