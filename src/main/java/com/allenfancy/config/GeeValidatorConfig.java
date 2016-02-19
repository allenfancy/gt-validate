package com.allenfancy.config;

/**
 * GeetestWeb配置文件
 */
public class GeeValidatorConfig {

	// 填入自己的captcha_id和private_key
	private static final String captcha_id = "2f92027df9b2d5f444d73e9fcffd2b05";
	private static final String private_key = "f4262b32d555b07a0d10bcd925fded38";

	public static final String getCaptcha_id() {
		return captcha_id;
	}

	public static final String getPrivate_key() {
		return private_key;
	}

}
