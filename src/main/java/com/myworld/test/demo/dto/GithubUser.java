package com.myworld.test.demo.dto;

import lombok.Data;

import java.net.Inet4Address;

/**
 * GitHub用户类
 */
@Data
public class GithubUser {
    private String name;
    private Integer id;
    private String bio;

}
