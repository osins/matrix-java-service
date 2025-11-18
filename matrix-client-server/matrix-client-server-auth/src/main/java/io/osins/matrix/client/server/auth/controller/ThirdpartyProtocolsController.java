package io.osins.matrix.client.server.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/_matrix/client/v3/thirdparty")
public class ThirdpartyProtocolsController {

    @GetMapping("/protocols")
    public ResponseEntity<Object> getProtocols() {
        // 创建符合Matrix规范的第三方协议响应，结构遵循您提供的schema
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(getMatrixThirdPartyProtocolsResponse());
    }

    private Object getMatrixThirdPartyProtocolsResponse() {
        // 创建符合schema的数据结构
        java.util.Map<String, Object> response = new java.util.HashMap<>();

        // 添加IRC协议
        java.util.Map<String, Object> ircProtocol = new java.util.HashMap<>();

        // user_fields
        java.util.Map<String, String> ircUserFields = new java.util.HashMap<>();
        ircUserFields.put("nickname", "IRC 用户的昵称");
        ircUserFields.put("username", "IRC 用户名");
        ircProtocol.put("user_fields", ircUserFields);

        // location_fields
        java.util.Map<String, String> ircLocationFields = new java.util.HashMap<>();
        ircLocationFields.put("channel", "IRC 频道名称");
        ircLocationFields.put("network", "IRC 网络");
        ircProtocol.put("location_fields", ircLocationFields);

        // field_types
        java.util.Map<String, Object> ircFieldTypes = new java.util.HashMap<>();
        java.util.Map<String, Object> nicknameFieldType = new java.util.HashMap<>();
        nicknameFieldType.put("type", "string");
        ircFieldTypes.put("nickname", nicknameFieldType);
        java.util.Map<String, Object> channelFieldType = new java.util.HashMap<>();
        channelFieldType.put("type", "string");
        ircFieldTypes.put("channel", channelFieldType);
        ircProtocol.put("field_types", ircFieldTypes);

        // instances
        java.util.List<java.util.Map<String, Object>> ircInstances = new java.util.ArrayList<>();
        java.util.Map<String, Object> ircInstance = new java.util.HashMap<>();
        ircInstance.put("desc", "Freenode IRC 网络");
        java.util.Map<String, Object> ircInstanceFields = new java.util.HashMap<>();
        ircInstanceFields.put("server", "irc.freenode.net");
        ircInstanceFields.put("port", 6667);
        ircInstance.put("fields", ircInstanceFields);
        ircInstances.add(ircInstance);
        ircProtocol.put("instances", ircInstances);

        // icon
        ircProtocol.put("icon", "mxc://irc.example.com/freenode-icon");

        // 添加Telegram协议
        java.util.Map<String, Object> telegramProtocol = new java.util.HashMap<>();

        // user_fields
        java.util.Map<String, String> telegramUserFields = new java.util.HashMap<>();
        telegramUserFields.put("username", "Telegram 用户名");
        telegramProtocol.put("user_fields", telegramUserFields);

        // location_fields
        java.util.Map<String, String> telegramLocationFields = new java.util.HashMap<>();
        telegramLocationFields.put("chat_id", "Telegram 聊天ID");
        telegramProtocol.put("location_fields", telegramLocationFields);

        // field_types
        java.util.Map<String, Object> telegramFieldTypes = new java.util.HashMap<>();
        java.util.Map<String, Object> usernameFieldType = new java.util.HashMap<>();
        usernameFieldType.put("type", "string");
        telegramFieldTypes.put("username", usernameFieldType);
        java.util.Map<String, Object> chatIdFieldType = new java.util.HashMap<>();
        chatIdFieldType.put("type", "string");
        telegramFieldTypes.put("chat_id", chatIdFieldType);
        telegramProtocol.put("field_types", telegramFieldTypes);

        // instances
        java.util.List<java.util.Map<String, Object>> telegramInstances = new java.util.ArrayList<>();
        java.util.Map<String, Object> telegramInstance = new java.util.HashMap<>();
        telegramInstance.put("desc", "Telegram Bot API");
        java.util.Map<String, Object> telegramInstanceFields = new java.util.HashMap<>();
        telegramInstanceFields.put("bot_name", "matrix_bridge_bot");
        telegramInstance.put("fields", telegramInstanceFields);
        telegramInstances.add(telegramInstance);
        telegramProtocol.put("instances", telegramInstances);

        // icon
        telegramProtocol.put("icon", "mxc://telegram.example.com/telegram-icon");

        // 将协议添加到响应中
        response.put("irc", ircProtocol);
        response.put("telegram", telegramProtocol);

        return response;
    }
}
