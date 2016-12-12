package com.tianshouzhi.dragon.common.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.tianshouzhi.dragon.common.jdbc.DragonStatement.CreateType.*;
import static com.tianshouzhi.dragon.common.jdbc.DragonStatement.ExecuteType.*;

/**
 * Created by TIANSHOUZHI336 on 2016/12/12.
 */
public abstract class DragonStatement extends WrapperAdapter implements Statement{
    protected Lock batchLock=new ReentrantLock();
    protected DragonConnection dragonConnection;
    //Statement构造创建参数
    protected Integer resultSetType;
    protected Integer resultSetConcurrency;
    protected Integer resultSetHoldability;

    //Statement属性参数，执行前进行设置
    protected Integer queryTimeout;
    protected Integer fetchDirection;
    protected Integer fetchSize;
    protected Boolean poolable;
    protected Integer maxRows;
    protected Integer maxFieldSize;
    protected Boolean enableEscapeProcessing;

    protected  String sql;
    protected Integer autoGeneratedKeys;
    protected int[] columnIndexes;
    protected String[] columnNames;
    //Statement状态参数，用户主动调用api设置
    protected boolean isClosed = false;

    //Statement查询执行结果
    protected ResultSet resultSet;
    //Statement更新执行结果
    protected ResultSet generatedKeys;
    protected int updateCount = 0;
    protected  int[] batchExecuteResult;

    protected ExecuteType executeType =null;
    protected CreateType createType =null;
    public enum ExecuteType {
        EXECUTE_QUERY,
        EXECUTE_UPDATE,
        EXECUTE,
        EXECUTE_UPDATE_WITH_AUTOGENERATEDKEYS,
        EXECUTE_UPDATE_WITH_COLUMNINDEXES,
        EXECUTE_WITH_AUTOGENERATEDKEYS,
        EXECUTE_WITH_COLUMNINDEXES,
        EXECUTE_WITH_COLUMNNAMES,
        EXECUTE_BATCH,
        EXECUTE_UPDATE_WITH_COLUMNNAMES
    }
    protected enum CreateType {
        NONE,
        RESULTSET_TYPE_CONCURRENCY,
        RESULTSET_TYPE_CONCURRENCY_HOLDABILITY
    }

    protected DragonStatement(DragonConnection dragonConnection) {
        this.dragonConnection=dragonConnection;
        this.createType =NONE;
    }

    protected DragonStatement(Integer resultSetType, Integer resultSetConcurrency, DragonConnection dragonConnection) {
        this.dragonConnection=dragonConnection;
        this.resultSetType=resultSetType;
        this.resultSetConcurrency=resultSetConcurrency;
        this.createType =RESULTSET_TYPE_CONCURRENCY;
    }

    protected DragonStatement(Integer resultSetType, Integer resultSetConcurrency, Integer resultSetHoldability, DragonConnection dragonConnection) {
        this.dragonConnection = dragonConnection;
        this.resultSetType = resultSetType;
        this.resultSetConcurrency = resultSetConcurrency;
        this.resultSetHoldability = resultSetHoldability;
        this.createType =RESULTSET_TYPE_CONCURRENCY_HOLDABILITY;
    }

    //=======================显示指定进行查询==================
    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        this.sql=sql;
        executeType = EXECUTE_QUERY;
        doExecute();
        return resultSet;
    }

    protected void setStatementParams(Statement realStatement) throws SQLException {
        if(queryTimeout!=null){
            realStatement.setQueryTimeout(queryTimeout);
        }
        if(fetchSize!=null){
            realStatement.setFetchSize(fetchSize);
        }
        if(fetchDirection!=null){
            realStatement.setFetchSize(fetchDirection);
        }
        if(poolable!=null){
            realStatement.setPoolable(poolable);
        }
        if(maxFieldSize!=null){
            realStatement.setMaxFieldSize(maxFieldSize);
        }
        if(maxRows!=null){
            realStatement.setMaxRows(maxRows);
        }
        if(enableEscapeProcessing!=null){
            realStatement.setEscapeProcessing(enableEscapeProcessing);
        }
    }

    //===================显示指定进行更新 =======================
    @Override
    public int executeUpdate(String sql) throws SQLException {
        this.sql=sql;
        this.executeType = ExecuteType.EXECUTE_UPDATE;
        doExecute();
        return updateCount;
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        this.sql=sql;
        this.autoGeneratedKeys=autoGeneratedKeys;
        this.executeType = ExecuteType.EXECUTE_UPDATE_WITH_AUTOGENERATEDKEYS;
        doExecute();
        return updateCount;
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        this.sql=sql;
        this.columnIndexes=columnIndexes;
        this.executeType = ExecuteType.EXECUTE_UPDATE_WITH_COLUMNINDEXES;
        doExecute();
        return updateCount;
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        this.sql=sql;
        this.columnNames=columnNames;
        this.executeType = ExecuteType.EXECUTE_UPDATE_WITH_COLUMNNAMES;
        doExecute();
        return updateCount;
    }

    //=================不指定sql是查询还是更新，程序自动判断========================
    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        this.sql=sql;
        this.autoGeneratedKeys=autoGeneratedKeys;
        this.executeType = ExecuteType.EXECUTE_WITH_AUTOGENERATEDKEYS;
        return doExecute();
    }
    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        this.sql=sql;
        this.columnIndexes=columnIndexes;
        this.executeType = ExecuteType.EXECUTE_WITH_COLUMNINDEXES;
        return doExecute();
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        this.sql=sql;
        this.columnNames=columnNames;
        this.executeType = ExecuteType.EXECUTE_WITH_COLUMNNAMES;
        return doExecute();
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        this.sql=sql;
        this.executeType = EXECUTE;
        return doExecute();
    }

    //============================批处理操作，JDBC规范规定只能是更新语句，因此总是获取写connection==========

    /**
     * http://lavasoft.blog.51cto.com/62575/238651/
     * 优点：能够处理多种不同结构的sql语句
     缺点：不能预处理，执行效率较差。对于参数不同的同一条sql语句需要多次调用addBatch()
     * @return
     * @throws SQLException
     */
    @Override
    public int[] executeBatch() throws SQLException {
        this.executeType =EXECUTE_BATCH;
        doExecute();
        return batchExecuteResult;
    }

    protected abstract boolean doExecute() throws SQLException;
    @Override
    public int getResultSetConcurrency() throws SQLException {
        return this.resultSetConcurrency;
    }
    @Override
    public int getResultSetType() throws SQLException {
        return resultSetType;
    }

    @Override
    public DragonConnection getConnection() throws SQLException {
        return dragonConnection;
    }


    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return generatedKeys;
    }
    @Override
    public int getMaxFieldSize() throws SQLException {
        return maxFieldSize;
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        this.maxFieldSize=max;
    }

    @Override
    public int getMaxRows() throws SQLException {
        return maxRows;
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        this.maxRows=max;
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        this.enableEscapeProcessing=enable;
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return queryTimeout;
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        this.queryTimeout=seconds;
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return resultSetHoldability;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return isClosed;
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        this.poolable=poolable;
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return this.poolable;
    }
    @Override
    public void setFetchDirection(int direction) throws SQLException {
        this.fetchDirection=direction;
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return this.fetchDirection;
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        this.fetchSize=rows;
    }

    @Override
    public int getFetchSize() throws SQLException {
        return fetchSize;
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return this.resultSet;
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return this.updateCount;
    }
}
