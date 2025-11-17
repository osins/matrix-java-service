package io.osins.matrix.auth.oauth2.server.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClientType {

	/**
	 * 开发者端
	 */
	DEVELOPER(-1),

	/**
	 * 平台端
	 */
	PLATFORM(0),

	/**
	 * 商家端
	 */
	SHOP(1),

	/**
	 * 消费端
	 */
	CONSUMER(2),

	/**
	 * 门店端
	 */
	STORE(3),

	/**
	 * 供应商端
	 */
	SUPPLIER(4),
	;

	private final Integer value;
}
