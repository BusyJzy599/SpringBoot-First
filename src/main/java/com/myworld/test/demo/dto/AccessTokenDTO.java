package com.myworld.test.demo.dto;

import lombok.Data;

/**
 *
 * 获取github的Access_Token的类以及所需的相关参数
 */
@Data
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;

}
