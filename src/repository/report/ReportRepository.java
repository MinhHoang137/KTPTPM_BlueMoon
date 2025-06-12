package repository.report;

import model.BaseModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class ReportRepository extends BaseModel {

    public Map<String, Double> getTotalCollectedByFeeName() {
        String sql = """
        SELECT fi.ten_khoan_thu, 
               COALESCE(SUM(p.so_tien_nop), 0) AS total_collected
          FROM fee_items fi
          LEFT JOIN payments p ON p.fee_item_id = fi.id AND p.trang_thai = 'Đã nộp'
         GROUP BY fi.ten_khoan_thu;
        """;
        Map<String, Double> result = new LinkedHashMap<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String feeName = rs.getString("ten_khoan_thu");
                double total  = rs.getDouble("total_collected");
                result.put(feeName, total);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public Map<String, Double> getTotalCollectedByHousehold() {
        String sql = """
            SELECT h.household_registration_number,
                   COALESCE(SUM(p.so_tien_nop), 0) AS total_collected
              FROM households h
              LEFT JOIN payments p ON p.id_household = h.id AND p.trang_thai = 'Đã nộp'
             GROUP BY h.household_registration_number;
            """;
        Map<String, Double> result = new LinkedHashMap<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String regNum = rs.getString("household_registration_number");
                double total  = rs.getDouble("total_collected");
                result.put(regNum, total);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public Map<String, Double> getTotalCollectedByMonth() {
        String sql = """
            SELECT DATE_FORMAT(p.ngay_nop, '%Y-%m') AS month,
                   COALESCE(SUM(p.so_tien_nop), 0) AS total_collected
              FROM payments p
             WHERE p.trang_thai = 'Đã nộp'
             GROUP BY DATE_FORMAT(p.ngay_nop, '%Y-%m')
             ORDER BY month;
            """;
        Map<String, Double> result = new LinkedHashMap<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String month = rs.getString("month");
                double total = rs.getDouble("total_collected");
                result.put(month, total);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public Map<String, List<String>> getHouseholdPaymentStatusLists() {
        String sqlPaid = """
            SELECT DISTINCT h.household_registration_number
              FROM households h
              JOIN payments p ON p.id_household = h.id
             WHERE p.trang_thai = 'Đã nộp';
            """;
        String sqlUnpaid = """
            SELECT DISTINCT h.household_registration_number
              FROM households h
              LEFT JOIN payments p ON p.id_household = h.id
             GROUP BY h.id
            HAVING SUM(CASE WHEN p.trang_thai = 'Đã nộp' THEN 1 ELSE 0 END) = 0;
            """;
        Map<String, List<String>> result = new LinkedHashMap<>();
        List<String> paidList = new ArrayList<>();
        List<String> unpaidList = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement psPaid = conn.prepareStatement(sqlPaid);
             ResultSet rsPaid = psPaid.executeQuery()) {
            while (rsPaid.next()) {
                paidList.add(rsPaid.getString("household_registration_number"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try (Connection conn = getConnection();
             PreparedStatement psUnpaid = conn.prepareStatement(sqlUnpaid);
             ResultSet rsUnpaid = psUnpaid.executeQuery()) {
            while (rsUnpaid.next()) {
                unpaidList.add(rsUnpaid.getString("household_registration_number"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        result.put("Đã nộp", paidList);
        result.put("Chưa nộp", unpaidList);
        return result;
    }

    public Map<String, Integer> getPopulationByGender() {
        String sql = """
            SELECT gender, COUNT(*) AS cnt
              FROM persons
             GROUP BY gender;
            """;
        Map<String, Integer> result = new LinkedHashMap<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String gender = rs.getString("gender");
                int count = rs.getInt("cnt");
                result.put(gender, count);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public Map<String, Integer> getPopulationByAgeGroup() {
        String sql = """
            SELECT
              CASE
                WHEN TIMESTAMPDIFF(YEAR, date_of_birth, CURDATE()) <= 18 THEN '0-18'
                WHEN TIMESTAMPDIFF(YEAR, date_of_birth, CURDATE()) BETWEEN 19 AND 35 THEN '19-35'
                WHEN TIMESTAMPDIFF(YEAR, date_of_birth, CURDATE()) BETWEEN 36 AND 60 THEN '36-60'
                ELSE '>60'
              END AS age_range,
              COUNT(*) AS cnt
            FROM persons
            GROUP BY age_range;
            """;
        Map<String, Integer> result = new LinkedHashMap<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.put(rs.getString("age_range"), rs.getInt("cnt"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public Map<String, Integer> getPopulationByReligion() {
        String sql = """
            SELECT religion, COUNT(*) AS cnt
              FROM persons
             GROUP BY religion;
            """;
        Map<String, Integer> result = new LinkedHashMap<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.put(rs.getString("religion"), rs.getInt("cnt"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public Map<String, Integer> getPopulationByWard() {
        String sql = """
            SELECT h.ward, COUNT(p.id) AS cnt
              FROM households h
              JOIN persons p ON p.household_id = h.id
             GROUP BY h.ward;
            """;
        Map<String, Integer> result = new LinkedHashMap<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String ward = rs.getString("ward");
                result.put(ward, rs.getInt("cnt"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
