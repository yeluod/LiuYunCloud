package com.liuyun.database.mybatis.handler;

import com.baomidou.mybatisplus.core.toolkit.AES;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * EncryptHandler
 *
 * @author W.d
 * @since 2022/10/21 15:05
 **/
@Slf4j
@MappedTypes({String.class})
@MappedJdbcTypes(JdbcType.VARCHAR)
public class AesEncryptTypeHandler extends AbstractEncryptTypeHandler {

    private static final String KEY = "liuyunEncrypt123";

    /**
     * 加密
     *
     * @param value {@link String} 明文
     * @return 密文
     * @author W.d
     * @since 2022/10/21 15:22
     **/
    @Override
    protected String encrypt(String value) {
        return AES.encrypt(value, KEY);
    }

    /**
     * 解密
     *
     * @param value {@link String} 密文
     * @return 明文
     * @author W.d
     * @since 2022/10/21 15:22
     **/
    @Override
    protected String decrypt(String value) {
        return AES.decrypt(value, KEY);
    }

}
