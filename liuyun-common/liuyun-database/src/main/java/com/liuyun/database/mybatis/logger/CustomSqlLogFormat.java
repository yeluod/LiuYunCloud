package com.liuyun.database.mybatis.logger;

import cn.hutool.core.util.StrUtil;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

/**
 * 自定义Sql执行日志打印格式
 *
 * @author W.d
 * @since 2022/7/14 10:03
 **/
public class CustomSqlLogFormat implements MessageFormattingStrategy {

    /**
     * Formats a log message for the logging module
     *
     * @param connectionId the id of the connection
     * @param now          the current ime expressing in milliseconds
     * @param elapsed      the time in milliseconds that the operation took to complete
     * @param category     the category of the operation
     * @param prepared     the SQL statement with all bind variables replaced with actual values
     * @param sql          the sql statement executed
     * @param url          the database url where the sql statement executed
     * @return the formatted log message
     */
    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        return StrUtil.isNotBlank(sql) ? "\n Consume Time：" + elapsed + " ms " + now +
                                         "\n Execute SQL：" + sql.replaceAll("\\s+", " ") : "";
    }

}
