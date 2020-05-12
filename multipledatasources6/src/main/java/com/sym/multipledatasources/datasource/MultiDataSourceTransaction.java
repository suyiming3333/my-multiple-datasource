package com.sym.multipledatasources.datasource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.transaction.Transaction;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.springframework.util.Assert.notNull;

/**
 * springboot的声明式事务需要重写Transaction。
 *
 * 配置了事物管理器和拦截Service中的方法后，每次执行Service中方法前会开启一个事务，
 * 并且同时会缓存DataSource、SqlSessionFactory、Connection等;
 * 以致于虽然动态切换了数据库，但获取到的connection仍然是不正确，导致数据提交异常。
 * 所以我们要想能够顺利的切换数据源，实际就是能够动态的根据DatabaseType获取不同的Connection
 */

public class MultiDataSourceTransaction implements Transaction {

    private static final Log LOGGER = LogFactory.getLog(MultiDataSourceTransaction.class);

    private final DataSource dataSource;

    private Connection mainConnection;

    private DataSourceType.DataBaseType mainDatabaseIdentification;

    private ConcurrentMap<DataSourceType.DataBaseType, Connection> otherConnectionMap;

    private boolean isConnectionTransactional;

    private boolean autoCommit;

    public MultiDataSourceTransaction(DataSource dataSource) {
        notNull(dataSource, "No DataSource specified");
        this.dataSource = dataSource;
        otherConnectionMap = new ConcurrentHashMap<>();
        mainDatabaseIdentification=DataSourceType.getDataBaseType();
    }


    @Override
    public Connection getConnection() throws SQLException {

        DataSourceType.DataBaseType databaseIdentification = DataSourceType.getDataBaseType();
        LOGGER.info("ready to get connection from"+databaseIdentification);
        if (databaseIdentification.equals(mainDatabaseIdentification)) {
            if (mainConnection != null) {
                return mainConnection;
            } else {
                openMainConnection();
                mainDatabaseIdentification =databaseIdentification;
                return mainConnection;
            }
        } else {
            if (!otherConnectionMap.containsKey(databaseIdentification)) {
                try {
                    Connection conn = dataSource.getConnection();
                    otherConnectionMap.put(databaseIdentification, conn);
                } catch (SQLException ex) {
                    throw new CannotGetJdbcConnectionException("Could not get JDBC Connection", ex);
                }
            }
            return otherConnectionMap.get(databaseIdentification);
        }
  }

    private void openMainConnection() throws SQLException {
        this.mainConnection = DataSourceUtils.getConnection(this.dataSource);
        this.autoCommit = this.mainConnection.getAutoCommit();
        this.isConnectionTransactional = DataSourceUtils.isConnectionTransactional(this.mainConnection, this.dataSource);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(
                    "JDBC Connection ["
                            + this.mainConnection
                            + "] will"
                            + (this.isConnectionTransactional ? " " : " not ")
                            + "be managed by Spring");
        }
    }

    @Override
    public void commit() throws SQLException {
        if (this.mainConnection != null && !this.isConnectionTransactional && !this.autoCommit) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Committing JDBC Connection [" + this.mainConnection + "]");
            }
            this.mainConnection.commit();
            for (Connection connection : otherConnectionMap.values()) {
                connection.commit();
            }
        }
    }

    @Override
    public void rollback() throws SQLException {

        if (this.mainConnection != null && !this.isConnectionTransactional && !this.autoCommit) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Rolling back JDBC Connection [" + this.mainConnection + "]");
            }
            this.mainConnection.rollback();
            for (Connection connection : otherConnectionMap.values()) {
                connection.rollback();
            }
        }
    }

    @Override
    public void close() throws SQLException {

        DataSourceUtils.releaseConnection(this.mainConnection, this.dataSource);
        for (Connection connection : otherConnectionMap.values()) {
            DataSourceUtils.releaseConnection(connection, this.dataSource);
        }
    }

    @Override
    public Integer getTimeout() throws SQLException {
        return null;
    }
}
