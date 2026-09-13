package pt.ua.claudino;

import java.io.*;
import java.sql.*;

public class App {
    public static void main(String[] args) throws Exception {
        String url = "jdbc:postgresql://" + env("DB_HOST") + ":" + env("DB_PORT") + "/" + env("DB_NAME");
        String user = env("DB_USER");
        String pass = env("DB_PASSWORD");
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            if (args.length == 0 || args[0].equals("process")) {
                processCSV(conn, "/app/alunos.csv");
            } else if (args[0].equals("average") && args.length == 2) {
                printAverage(conn, args[1]);
            } else if (args[0].equals("grades") && args.length == 2) {
                printGrades(conn, args[1]);
            } else {
                System.out.println("Usage: java App [process|average <num_mec>|grades <num_mec>]");
            }
        }
    }

    static String env(String key) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing environment variable: " + key);
        }
        return value;
    }

    static void processCSV(Connection conn, String file) throws Exception {
        int ok = 0, bad = 0;
        System.out.println("Reading CSV file: " + file);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNum = 0;
            while ((line = br.readLine()) != null) {
                lineNum++;
                System.out.println("Line " + lineNum + ": " + line);
                if (line.startsWith("num_mec")) {
                    continue;
                }
                String[] p = line.split(";");
                if (p.length != 5) {
                    System.out.println("- Rejected: wrong field count (" + p.length + ")");
                    bad++;
                    continue;
                }
                int num;
                double g1, g2, g3;
                try {
                    num = Integer.parseInt(p[0].trim());
                    g1 = Double.parseDouble(p[2].trim());
                    g2 = Double.parseDouble(p[3].trim());
                    g3 = Double.parseDouble(p[4].trim());
                } catch (Exception e) {
                    System.out.println("- Rejected: parsing error - " + e.getMessage());
                    bad++;
                    continue;
                }
                if (!valid(g1) || !valid(g2) || !valid(g3) || p[1].trim().isEmpty()) {
                    System.out.println("- Rejected: invalid grades or empty name");
                    bad++;
                    continue;
                }
                String[] names = p[1].trim().split(" ", 2);
                String first = names[0], last = names.length > 1 ? names[1] : "";
                int sid = insertStudent(conn, num, first, last);
                insertGrade(conn, sid, 1, g1);
                insertGrade(conn, sid, 2, g2);
                insertGrade(conn, sid, 3, g3);
                System.out.println("Processed successfully");
                ok++;
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Processed: " + ok + ", Rejected: " + bad);
    }

    static boolean valid(double g) { return g >= 0 && g <= 20; }

    static int insertStudent(Connection c, int num, String f, String l) throws SQLException {
        String sql = "INSERT INTO students (num_mec, first_name, last_name) VALUES (?, ?, ?) ON CONFLICT (num_mec) DO UPDATE SET first_name=EXCLUDED.first_name, last_name=EXCLUDED.last_name RETURNING id";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, num); ps.setString(2, f); ps.setString(3, l);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
    }

    static void insertGrade(Connection c, int sid, int exam, double grade) throws SQLException {
        String del = "DELETE FROM grades WHERE student_id=? AND exam=?";
        try (PreparedStatement ps = c.prepareStatement(del)) {
            ps.setInt(1, sid); ps.setInt(2, exam); ps.executeUpdate();
        }
        String ins = "INSERT INTO grades (student_id, exam, grade) VALUES (?, ?, ?)";
        try (PreparedStatement ps = c.prepareStatement(ins)) {
            ps.setInt(1, sid); ps.setInt(2, exam); ps.setDouble(3, grade); ps.executeUpdate();
        }
    }

    static void printAverage(Connection c, String numMec) throws SQLException {
        String sql = "SELECT AVG(g.grade) FROM grades g JOIN students s ON g.student_id=s.id WHERE s.num_mec=?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(numMec));
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                System.out.println("Student not found");
                return;
            }
            double avg = rs.getDouble(1);
            if (rs.wasNull()) {
                System.out.println("No grades recorded for student " + numMec);
            } else {
                System.out.println("Average: " + avg);
            }
        }
    }

    static void printGrades(Connection c, String numMec) throws SQLException {
        String sql = "SELECT g.exam, g.grade FROM grades g JOIN students s ON g.student_id=s.id WHERE s.num_mec=? ORDER BY g.exam";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(numMec));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) System.out.println("Exam " + rs.getInt(1) + ": " + rs.getDouble(2));
        }
    }
}

