package com.ruijing.sequence.helper;

import com.ruijing.sequence.exception.SequenceException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * 操作DB config帮助类
 *
 * @author mwup
 * @version 1.0
 * @created 2019/02/13 13:51
 **/
public abstract class SeqConfigHelper {

    private final static String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS sequence_config (id bigint(20) NOT NULL AUTO_INCREMENT,max_id bigint(20) NOT NULL,biz_name varchar(32) NOT NULL,update_time DATETIME NOT NULL,PRIMARY KEY (`id`),UNIQUE uk_biz_name (`biz_name`))";

    private final static String SQL_INSERT_RANGE = "INSERT IGNORE INTO sequence_config (biz_name,max_id,update_time) VALUE(?,?,?)";

    private final static String SQL_UPDATE_RANGE = "UPDATE sequence_config SET max_id=? WHERE biz_name=? AND max_id=?";

    private final static String SQL_SELECT_RANGE = "SELECT id,biz_name,model,type,init_value,step,retry_times,token,update_time FROM sequence_config WHERE biz_name=?";

    /**
     * 插入数据区间
     *
     * @param dataSource DB来源
     * @param tableName  表名
     * @param name       区间名称
     * @param stepStart  初始位置
     */
    private static void insertRange(DataSource dataSource, String tableName, String name, long stepStart) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(SQL_INSERT_RANGE.replace("#tableName", tableName));
            stmt.setString(1, name);
            stmt.setLong(2, stepStart);
            stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SequenceException(e);
        } finally {
            closeQuietly(stmt);
            closeQuietly(conn);
        }
    }

    /**
     * 更新区间，乐观策略
     *
     * @param dataSource DB来源
     * @param tableName  表名
     * @param newValue   更新新数据
     * @param oldValue   更新旧数据
     * @param name       区间名称
     * @return 成功/失败
     */
    public static boolean updateRange(DataSource dataSource, String tableName, Long newValue, Long oldValue, String name) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(SQL_UPDATE_RANGE);
            stmt.setString(2, name);
            stmt.setLong(1, newValue);
            stmt.setLong(3, oldValue);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new SequenceException(e);
        } finally {
            closeQuietly(stmt);
            closeQuietly(conn);
        }
    }

    private static void closeQuietly(AutoCloseable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (Throwable e) {
                //Ignore
            }
        }
    }

}
