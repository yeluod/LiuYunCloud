package com.liuyun.database.mybatis.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * AbTypeHandler
 *
 * @author W.d
 * @since 2022/10/21 15:19
 **/
public abstract class AbstractEncryptTypeHandler extends BaseTypeHandler<String> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, this.encrypt(parameter));
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return Optional.ofNullable(rs.getString(columnName))
                .map(this::decrypt)
                .orElse(null);
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return Optional.ofNullable(rs.getString(columnIndex))
                .map(this::decrypt)
                .orElse(null);
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return Optional.ofNullable(cs.getString(columnIndex))
                .map(this::decrypt)
                .orElse(null);
    }

    /**
     * 加密
     *
     * @param str {@link String} 明文
     * @return 密文
     * @author W.d
     * @since 2022/10/21 15:22
     **/
    protected abstract String encrypt(String str);

    /**
     * 解密
     *
     * @param str {@link String} 密文
     * @return 明文
     * @author W.d
     * @since 2022/10/21 15:22
     **/
    protected abstract String decrypt(String str);
}
