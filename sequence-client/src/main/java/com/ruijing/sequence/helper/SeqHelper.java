package com.ruijing.sequence.helper;

import com.ruijing.sequence.exception.SequenceException;

import javax.sql.DataSource;
import java.sql.*;

/**
 * 操作DB帮助类
 *
 * @author mwup
 * @version 1.0
 * @created 2019/02/13 13:51
 **/
public abstract class SeqHelper {

    private static final long DELTA = 100000000L;

    private final static String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS sequence_data (id bigint(20) NOT NULL AUTO_INCREMENT,max_id bigint(20) NOT NULL,biz_name varchar(32) NOT NULL,update_time DATETIME NOT NULL,PRIMARY KEY (`id`),UNIQUE uk_biz_name (`biz_name`))";

    private final static String SQL_INSERT_RANGE = "INSERT IGNORE INTO sequence_data (biz_name,max_id,update_time) VALUE(?,?,?)";

    private final static String SQL_UPDATE_RANGE = "UPDATE sequence_data SET max_id=? WHERE biz_name=? AND max_id=?";

    private final static String SQL_SELECT_RANGE = "SELECT max_id FROM sequence_data WHERE biz_name=?";

    /**
     * 创建表
     *
     * @param dataSource DB来源
     * @param tableName  表名
     */
    public static void createTable(DataSource dataSource, final String tableName) {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = dataSource.getConnection();
            stmt = conn.createStatement();
            stmt.executeUpdate(SQL_CREATE_TABLE);
        } catch (SQLException e) {
            throw new SequenceException(e);
        } finally {
            closeQuietly(stmt);
            closeQuietly(conn);
        }
    }

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
            stmt = conn.prepareStatement(SQL_INSERT_RANGE);
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
            stmt.setLong(1, newValue);
            stmt.setString(2, name);
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

    /**
     * 查询区间，如果区间不存在，会新增一个区间，并返回null，由上层重新执行
     *
     * @param dataSource DB来源
     * @param tableName  来源
     * @param name       区间名称
     * @param stepStart  初始位置
     * @return 区间值
     */
    public static Long selectRange(DataSource dataSource, String tableName, String name, long stepStart) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        long oldValue;

        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(SQL_SELECT_RANGE);
            stmt.setString(1, name);
            rs = stmt.executeQuery();
            if (!rs.next()) {
                //没有此类型数据，需要初始化
                insertRange(dataSource, tableName, name, stepStart);
                return null;
            }
            oldValue = rs.getLong(1);

            if (oldValue < 0) {
                String msg = "snowflake generator max_id cannot be less than zero, max_id = " + oldValue + ", please check table snowflake generator" + tableName;
                throw new SequenceException(msg);
            }

            if (oldValue > Long.MAX_VALUE - DELTA) {
                String msg = "snowflake generator max_id overflow, max_id = " + oldValue + ", please check table snowflake generator" + tableName;
                throw new SequenceException(msg);
            }

            return oldValue;
        } catch (SQLException e) {
            throw new SequenceException(e);
        } finally {
            closeQuietly(rs);
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
