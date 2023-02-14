package com.liuyun.domain.auth.constants;

import lombok.experimental.UtilityClass;

/**
 * App
 *
 * @author W.d
 * @since 2022/12/17 21:03
 **/
@UtilityClass
public class AuthServerConstant {

    public final String AUTHORIZATION_KEY = "AUTHORIZATION";
    public final String AUTHORIZATION_FEIGN_KEY = "FEIGN_TOKEN";
    public final String AUTHORIZATION_FEIGN_TOKEN = "liuyun:api";

    public final String TOKEN_CLAIMS_CLIENT_ID_EXTEND = "clientId";
    public final String TOKEN_CLAIMS_USER_EXTEND = "user";
    public final String TOKEN_CLAIMS_ROLE_EXTEND = "role";


    public final String PARAMETER_PHONE = "phone";

    public final String PARAMETER_PHONE_CODE = "code";



}
