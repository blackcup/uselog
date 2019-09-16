package com.dellemc.rawdata.analyze;


import java.sql.*;

public class FakeAnalyzer implements Analyzer{
    public static Connection conn;
    public static final long initTime = System.currentTimeMillis();
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String connectionUrl = "jdbc:mysql://10.199.197.216:3306/prevaga?useUnicode=true&characterEncoding=UTF-8&user=root&password=ChangeMe";
            conn = DriverManager.getConnection(connectionUrl);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void analyze(String message) {
        System.out.println("received message from receiver: " + message);
        Long duration = (long)(Float.parseFloat(message.split(":")[0].trim()) * 1000);
        String[] split = message.split(":")[1].split("\\[");
        String reason = split[0].trim();
        String[] strings = split[2].split("\\]")[0].trim().split("\\s+");
        Integer spin = Integer.parseInt(strings[0].trim());
        Integer block = Integer.parseInt(strings[1].trim());
        Integer cleanup = Integer.parseInt(strings[2].trim());
        Integer sync = Integer.parseInt(strings[3].trim());
        Integer vm_opeartion = Integer.parseInt(strings[4].trim());
        // 0 - timestamp
        // 1 - reason
        // 2 - spin
        // 3 - block
        // 4 - cleanup
        // 5 - sync
        // 6 - vm


        //String time = strings[0].substring(0, strings[0].length()-1);
        //long timestamp = (long)(Float.parseFloat(time) * 1000);
        try {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO `gc` (createTime,reason,spin,block,cleanup,sync,vmoperation,totalgc,duration) VALUE (?,?,?,?,?,?,?,?,?)");
            pstmt.setTimestamp(1, new Timestamp(initTime + duration));
            pstmt.setString(2,reason);
            pstmt.setInt(3,spin);
            pstmt.setInt(4,block);
            pstmt.setInt(5,cleanup);
            pstmt.setInt(6,sync);
            pstmt.setInt(7,vm_opeartion);

            int totalgc = spin + block + cleanup +sync + vm_opeartion;
            pstmt.setInt(8,totalgc);
            pstmt.setLong(9,duration);
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        conn.close();
    }

}
