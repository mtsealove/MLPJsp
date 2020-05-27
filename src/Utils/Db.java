package Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Db {
    private Connection conn;
    private PreparedStatement pstmt;
    private double[][] input;
    private int[][] output;
    private int row;

    public Db() {
        String dbURL = "jdbc:mysql://localhost:3306/AI?serverTimezone=UTC";
        String dbID = "root";
        String dbPassword = "Fucker0916!";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
            execute();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // db에 학습 패턴 저장, 배열 형식이 아닌 ','로 구분한 -1, 1의 형태의 varchar형식으로 저장
    public int insert(String line, String teach) {
        String SQL = "INSERT INTO Pattern set Line=?, Teach=?";
        try {
            pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, line);
            pstmt.setString(2, teach);
            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

//  db에 저장된 학습 패턴을 객체로 변환하여 반환
    public ArrayList<Pattern> getPatterns() {
        ArrayList<Pattern> patterns = new ArrayList<>();
        String query = "select Line from Pattern";
        try {
            pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
//            결과를 String 형식으로 받아 배열로 변환한 객체로 저장
            while (rs.next()) {
                patterns.add(new Pattern(rs.getString("Line")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patterns;
    }


    // 입출력 배열 생성
    private void execute() {
        String query = "select * from Pattern";
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            row = 0;
            if (rs.last()) {
                row = rs.getRow();
                rs.beforeFirst();
            }
            output = new int[row][10];
            input = new double[row][25];
            int idx = 0;
            while (rs.next()) {
                String[] ins = rs.getString("Line").split(",");
                String[] outs = rs.getString("Teach").split("");
                for (int i = 0; i < ins.length; i++) {
                    int num = Integer.parseInt(ins[i]);
                    //if (num == -1) {
//                        num = 0;
                    //}
                    input[idx][i] = num;
                }
                for (int i = 0; i < outs.length; i++) {
                    output[idx][i] = Integer.parseInt(outs[i]);
                }
                idx++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int[][] getOutput() {
        return output;
    }

    public double[][] getInput() {
        return input;
    }

    public int getRow() {
        return row;
    }
}
