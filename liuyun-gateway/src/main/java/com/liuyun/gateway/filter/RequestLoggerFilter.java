package com.liuyun.gateway.filter;

import cn.hutool.core.net.NetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Optional;

/**
 * ReqLoggerFilter
 *
 * @author W.d
 * @since 2022/12/22 21:47
 **/
@Slf4j
@Order
public class RequestLoggerFilter implements GlobalFilter {

    private static final String START_TIME = "startTime";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var request = exchange.getRequest();
        var requestMethod = request.getMethod().name();
        var path = request.getURI().getRawPath();
        var headers = request.getHeaders();
        var authorization = headers.get(HttpHeaders.AUTHORIZATION);
        var requestUrl = Optional.of(exchange)
                .map(item -> item.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR))
                .map(URI.class::cast)
                .map(URI::toString)
                .orElse("");
        var clientIp = this.getClientIp(request);
        exchange.getAttributes().put(START_TIME, System.currentTimeMillis());
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            var resCode = Optional.of(exchange)
                    .map(ServerWebExchange::getResponse)
                    .map(ServerHttpResponse::getStatusCode)
                    .map(HttpStatusCode::value)
                    .orElse(null);
            var executeTime = Optional.of(exchange)
                    .map(item -> item.getAttribute(START_TIME))
                    .map(Long.class::cast)
                    .map(item -> System.currentTimeMillis() - item)
                    .orElse(0L);
            log.info("===> ClientIp -> [{}], Method -> [{}], Path -> [{}], Authorization -> [{}], RequestUrl -> [{}], ResCode -> [{}], ExecuteTime -> [{}]",
                    clientIp,
                    requestMethod,
                    path,
                    authorization,
                    requestUrl,
                    resCode,
                    executeTime + "ms"
            );
        }));
    }

    private String getClientIp(ServerHttpRequest request) {
        String[] headerNames = {"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};
        String ip;
        for (String header : headerNames) {
            ip = request.getHeaders().getFirst(header);
            if (!NetUtil.isUnknown(ip)) {
                return NetUtil.getMultistageReverseProxyIp(ip);
            }
        }
        ip = Optional.of(request)
                .map(ServerHttpRequest::getRemoteAddress)
                .map(InetSocketAddress::getAddress)
                .map(InetAddress::toString)
                .orElse("");
        return NetUtil.getMultistageReverseProxyIp(ip);
    }
}
