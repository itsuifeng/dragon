package com.tianshouzhi.dragon.common.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;

/**
 * Created by TIANSHOUZHI336 on 2016/12/12.
 */
public interface DragonPrepareStatement extends PreparedStatement{
    //===================================execute 相关方法==========================================
    enum PrepareExecuteType{
        PREPARE_EXECUTE, PREPARE_EXECUTE_UPDATE,PREPARE_EXECUTE_QUERY
    }
    //===============================参数设置相关=========================================

      class ParamSetting {
        public ParamType paramType;
        public Object[] values;

        public ParamSetting(ParamType paramType, Object[] values) {
            this.paramType = paramType;
            this.values = values;
        }
    }
     enum ParamType{
        setNull, setBoolean, setByte, setShort, setInt, setLong, setFloat,
        setDouble, setBigDecimal, setString, setBytes, setDate, setTime,
        setTimestamp, setAsciiStream2, setUnicodeStream2, setBinaryStream2,
        setObject2, setObject, setCharacterStream2, setRef, setBlob, setClob,
        setArray, setDate2, setTime2, setTimestamp2, setNull2, setURL, setRowId,
        setNString, setNCharacterStream, setCharacterStream, setBinaryStream,
        setAsciiStream, setObject3, setSQLXML, setNClob2, setBlob2, setClob2,
        setNCharacterStream2, setNClob;

        public static void setPrepareStatementParams(PreparedStatement ps, int parameterIndex, Object[] values, DragonPrepareStatement.ParamType paramType) throws SQLException {
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
    }
    //====================================批处理相关============================================
     enum BatchType{
        STATEMENT,PREPARESTATEMENT;
    }
    class BatchExecuteInfo {
        public BatchType batchType;
        public Object paramter;

        public BatchExecuteInfo(BatchType batchType, Object paramter) {
            this.batchType = batchType;
            this.paramter = paramter;
        }
    }
}
