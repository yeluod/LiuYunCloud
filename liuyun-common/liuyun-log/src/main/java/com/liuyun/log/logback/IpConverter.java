package com.liuyun.log.logback;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import cn.hutool.core.lang.Opt;

import java.net.InetAddress;

/**
 * IpConverter
 *
 * @author W.d
 * @since 2022/10/20 14:45
 **/
public class IpConverter extends ClassicConverter {

    @Override
    public String convert(ILoggingEvent arg0) {
        return Opt.ofTry(InetAddress::getLocalHost)
                .map(InetAddress::getHostAddress)
                .orElse("");
    }
}
