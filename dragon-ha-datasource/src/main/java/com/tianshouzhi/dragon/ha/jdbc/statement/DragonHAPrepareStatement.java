package com.tianshouzhi.dragon.ha.jdbc.statement;

import com.tianshouzhi.dragon.ha.jdbc.connection.DragonHAConnection;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.tianshouzhi.dragon.ha.jdbc.statement.DragonHAPrepareStatement.BatchType.PREPARESTATEMENT;
import static com.tianshouzhi.dragon.ha.jdbc.statement.DragonHAPrepareStatement.ParamType.*;

/**
 * Created by TIANSHOUZHI336 on 2016/12/4.
 */
public class DragonHAPrepareStatement extends DragonHAStatement implements PreparedStatement{

    private List<ParamSetting> params=new ArrayList<ParamSetting>();
    public DragonHAPrepareStatement(String sql, DragonHAConnection dragonHAConnection) throws SQLException {
        super(dragonHAConnection);
        this.sql=sql;
        this.executeType=ExecuteType.EXECUTE;
    }

    public DragonHAPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, DragonHAConnection dragonHAConnection) throws SQLException {
        super(resultSetType, resultSetConcurrency, dragonHAConnection);
        this.sql=sql;
        this.executeType=ExecuteType.EXECUTE;
        this.resultSetType=resultSetType;
        this.resultSetConcurrency=resultSetConcurrency;
    }

    public DragonHAPrepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability, DragonHAConnection dragonHAConnection) throws SQLException {
        super(resultSetType, resultSetConcurrency, resultSetHoldability, dragonHAConnection);
        this.sql=sql;
        this.executeType=ExecuteType.EXECUTE;
        this.resultSetType=resultSetType;
        this.resultSetConcurrency=resultSetConcurrency;
        this.resultSetHoldability=resultSetHoldability;
    }

    public DragonHAPrepareStatement(String sql, int autoGeneratedKeys, DragonHAConnection dragonHAConnection) throws SQLException {
        super(dragonHAConnection);
        this.sql=sql;
        this.executeType=ExecuteType.EXECUTE_WITH_AUTOGENERATEDKEYS;
        this.autoGeneratedKeys=autoGeneratedKeys;
    }

    public DragonHAPrepareStatement(String sql, int[] columnIndexes, DragonHAConnection dragonHAConnection) throws SQLException {
        super(dragonHAConnection);
        this.sql=sql;
        this.executeType=ExecuteType.EXECUTE_WITH_COLUMNINDEXES;
        this.columnIndexes=columnIndexes;
    }

    public DragonHAPrepareStatement(String sql, String[] columnNames, DragonHAConnection dragonHAConnection) throws SQLException {
        super(dragonHAConnection);
        this.sql=sql;
        this.executeType=ExecuteType.EXECUTE_WITH_COLUMNINDEXES;
        this.columnNames=columnNames;
    }

    @Override
    protected void createRealStatement(Connection realConnection) throws SQLException {
        for (int i = 0; i == 0; i++) {
            if (resultSetType == null && resultSetConcurrency == null && resultSetHoldability == null) {
                realStatement = realConnection.prepareStatement(sql);
                break;
            }
            if (resultSetType != null && resultSetConcurrency != null && resultSetHoldability == null) {
                realStatement = realConnection.prepareStatement(sql,resultSetType, resultSetConcurrency);
                break;
            }
            realStatement = realConnection.prepareStatement(sql,resultSetType, resultSetConcurrency, resultSetHoldability);
            break;
        }
        setStatementParams();
        if(batchInfoList==null)return;
        for (BatchInfo batchInfo : batchInfoList) {
            switch (batchInfo.batchType) {
                case STATEMENT:
                    realStatement.addBatch((String) batchInfo.paramter);
                    break;
                case PREPARESTATEMENT:
                    List<ParamSetting> parameters= (List<ParamSetting>) batchInfo.paramter;
                    for (ParamSetting parameter : parameters) {
                        setPrepareStatementParams((PreparedStatement) realStatement,parameter.parameterIndex,parameter.values,parameter.paramType);
                    }
                    ((PreparedStatement) realStatement).addBatch();
                    break;
            }
        }
    }

    @Override
    protected void setStatementParams() throws SQLException {
        super.setStatementParams();
        PreparedStatement ps= (PreparedStatement) realStatement;
        for (ParamSetting param : params) {
            int parameterIndex = param.parameterIndex;
            Object[] values = param.values;
            ParamType paramType = param.paramType;
            setPrepareStatementParams(ps, parameterIndex, values, paramType);
        }
    }

    protected static void setPrepareStatementParams(PreparedStatement ps, int parameterIndex, Object[] values, ParamType paramType) throws SQLException {
        switch (paramType) {
            case setNull:
                ps.setNull(parameterIndex, (Integer) values[0]);
                break;
            case setBoolean:
                ps.setBoolean(parameterIndex, (Boolean) values[0]);
                break;
            case setByte:
                ps.setByte(parameterIndex, (Byte) values[0]);
                break;
            case setShort:
                ps.setShort(parameterIndex, (Short) values[0]);
                break;
            case setInt:
                ps.setInt(parameterIndex, (Integer) values[0]);
                break;
            case setLong:
                ps.setLong(parameterIndex, (Long) values[0]);
                break;
            case setFloat:
                ps.setFloat(parameterIndex, (Float) values[0]);
                break;
            case setDouble:
                ps.setDouble(parameterIndex, (Double) values[0]);
                break;
            case setBigDecimal:
                ps.setBigDecimal(parameterIndex, (BigDecimal) values[0]);
                break;
            case setString:
                ps.setString(parameterIndex, (String) values[0]);
                break;
            case setBytes:
                ps.setBytes(parameterIndex, (byte[]) values[0]);
                break;
            case setDate:
                ps.setDate(parameterIndex, (Date) values[0]);
                break;
            case setTime:
                ps.setTime(parameterIndex, (Time) values[0]);
                break;
            case setTimestamp:
                ps.setTimestamp(parameterIndex, (Timestamp) values[0]);
                break;
            case setAsciiStream2:
                ps.setAsciiStream(parameterIndex, (InputStream)values[0],(Integer)values[1]);
                break;
            case setUnicodeStream2:
                ps.setUnicodeStream(parameterIndex, (InputStream) values[0],(Integer) values[1]);
                break;
            case setBinaryStream2:
                ps.setBinaryStream(parameterIndex, (InputStream)values[0],(Integer)values[1]);
                break;
            case setObject2:
                ps.setObject(parameterIndex, values[0], (Integer) values[1]);
                break;
            case setObject:
                ps.setObject(parameterIndex, values[0]);
                break;
            case setCharacterStream2:
                ps.setCharacterStream(parameterIndex, (Reader)values[0],(Integer) values[1]);
                break;
            case setRef:
                ps.setRef(parameterIndex, (Ref) values[0]);
                break;
            case setBlob:
                ps.setBlob(parameterIndex, (InputStream) values[0]);
                break;
            case setClob:
                ps.setClob(parameterIndex, (Reader) values[0]);
                break;
            case setArray:
                ps.setArray(parameterIndex, (Array) values[0]);
                break;
            case setDate2:
                ps.setDate(parameterIndex, (Date) values[0], (Calendar) values[1]);
                break;
            case setTime2:
                ps.setTime(parameterIndex, (Time) values[0], (Calendar) values[1]);
                break;
            case setTimestamp2:
                ps.setTimestamp(parameterIndex, (Timestamp) values[0],(Calendar)values[1]);
                break;
            case setNull2:
                ps.setNull(parameterIndex, (Integer) values[0],(String)values[1]);
                break;
            case setURL:
                ps.setURL(parameterIndex, (URL) values[0]);
                break;
            case setRowId:
                ps.setRowId(parameterIndex, (RowId) values[0]);
                break;
            case setNString:
                ps.setNString(parameterIndex, (String) values[0]);
                break;
            case setNCharacterStream:
                ps.setNCharacterStream(parameterIndex, (Reader) values[0]);
                break;
            case setCharacterStream:
                ps.setCharacterStream(parameterIndex, (Reader) values[0]);
                break;
            case setBinaryStream:
                ps.setBinaryStream(parameterIndex, (InputStream) values[0]);
                break;
            case setAsciiStream:
                ps.setAsciiStream(parameterIndex, (InputStream) values[0]);
                break;
            case setObject3:
                ps.setObject(parameterIndex, values[0],(Integer) values[1],(Integer) values[2]);
                break;
            case setSQLXML:
                ps.setSQLXML(parameterIndex, (SQLXML) values[0]);
                break;
            case setNClob2:
                ps.setNClob(parameterIndex, (Reader) values[0],(Long)values[1]);
                break;
            case setBlob2:
                ps.setBlob(parameterIndex, (InputStream) values[0],(Long)values[1]);
                break;
            case setClob2:
                ps.setClob(parameterIndex, (Reader) values[0],(Long)values[1]);
                break;
            case setNCharacterStream2:
                ps.setNCharacterStream(parameterIndex, (Reader) values[0],(Long)values[1]);
                break;
            case setNClob:
                ps.setNClob(parameterIndex, (Reader) values[0]);
                break;
        }
    }

    protected static class ParamSetting {
        int parameterIndex;
        private ParamType paramType;
        Object[] values;

        public ParamSetting(int parameterIndex,ParamType paramType, Object[] values) {
            this.parameterIndex = parameterIndex;
            this.paramType = paramType;
            this.values = values;
        }
    }
    protected static enum ParamType{
        setNull,setBoolean,setByte,setShort,setInt,setLong,setFloat,
        setDouble,setBigDecimal,setString,setBytes,setDate,setTime,setTimestamp,setAsciiStream2,setUnicodeStream2
        ,setBinaryStream2,setObject2, setObject,setCharacterStream2,setRef,setBlob,setClob,setArray,setDate2,setTime2
        ,setTimestamp2,setNull2,setURL,setRowId,setNString,setNCharacterStream, setCharacterStream, setBinaryStream, setAsciiStream, setObject3, setSQLXML, setNClob2, setBlob2, setClob2, setNCharacterStream2, setNClob
    }
    @Override
    public void clearParameters() throws SQLException {
        params.clear();
    }

    /**
     * 优点：能够预处理，执行效率高；参数不同的同一条sql语句执行简便
       缺点：只能批处理参数不同的同一条sql语句
     * @throws SQLException
     */
    enum BatchType{
        STATEMENT,PREPARESTATEMENT;
    }
    static class BatchInfo{
        BatchType batchType;
        Object paramter;

        public BatchInfo(BatchType batchType, Object paramter) {
            this.batchType = batchType;
            this.paramter = paramter;
        }
    }
    protected List<BatchInfo> batchInfoList=null;
    @Override
    public void addBatch() throws SQLException {
        if(batchInfoList==null){
            batchInfoList=new ArrayList<BatchInfo>();
        }
        batchInfoList.add(new BatchInfo(PREPARESTATEMENT, params));
        params.clear();
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        if(batchInfoList==null){
            batchInfoList=new ArrayList<BatchInfo>();
        }
        batchInfoList.add(new BatchInfo(PREPARESTATEMENT, sql));
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        throw new UnsupportedOperationException("getMetaData");
    }

    /**
     * JDBC规范规定：PreparedStatement执行完成之后，可以获取这个对象
     * 由于PreparedStatement是预编译的，因此可能不需要执行，也能获取，这里要求必须执行完成之后，才能返回
     * @return
     * @throws SQLException
     */
    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        if(realStatement!=null){
            return( (PreparedStatement)realStatement).getMetaData();
        }
        return null;
    }
    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setNull,new Object[]{sqlType}));
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setBoolean,new Object[]{x}));
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setByte,new Object[]{x}));
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setShort,new Object[]{x}));
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setInt,new Object[]{x}));
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setLong,new Object[]{x}));
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setFloat,new Object[]{x}));
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setDouble,new Object[]{x}));
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setBigDecimal,new Object[]{x}));
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setString,new Object[]{x}));
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setBytes,new Object[]{x}));
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setDate,new Object[]{x}));
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setTime,new Object[]{x}));
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setTimestamp,new Object[]{x}));
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setAsciiStream2,new Object[]{x,length}));
    }

    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setUnicodeStream2,new Object[]{x,length}));
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setBinaryStream2,new Object[]{x,length}));
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setObject2,new Object[]{x}));
    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setObject,new Object[]{x}));
    }


    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setCharacterStream2,new Object[]{reader,length}));
    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setRef,new Object[]{x}));
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setBlob,new Object[]{x}));
    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setClob,new Object[]{x}));
    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setArray,new Object[]{x}));
    }


    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setDate2,new Object[]{x,cal}));
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setTime2,new Object[]{x,cal}));
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setTimestamp,new Object[]{x,cal}));
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setNull2,new Object[]{sqlType,typeName}));
    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setURL,new Object[]{x}));
    }



    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setRowId,new Object[]{x}));
    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setNString,new Object[]{value}));
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setNCharacterStream2,new Object[]{value,length}));
    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setNClob,new Object[]{value}));
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setClob2,new Object[]{reader,length}));
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setBlob2,new Object[]{inputStream,length}));
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setNClob2,new Object[]{reader,length}));
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setSQLXML,new Object[]{xmlObject}));
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setObject3,new Object[]{x,targetSqlType,scaleOrLength}));
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setAsciiStream2,new Object[]{x,length}));
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setBinaryStream2,new Object[]{x,length}));
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setCharacterStream2,new Object[]{reader,length}));
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setAsciiStream,new Object[]{x}));
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setBinaryStream,new Object[]{x}));
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setCharacterStream,new Object[]{reader}));
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setNCharacterStream,new Object[]{value}));
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setClob,new Object[]{reader}));
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setBlob,new Object[]{inputStream}));
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        params.add(new ParamSetting(parameterIndex,setNClob,new Object[]{reader}));
    }
}
