package club.hm.matrix.client.server.auth.controller;

import club.hm.matrix.client.server.common.domain.SupportedVersions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/_matrix/client/v3")
public class ClientV3Controller {

    @GetMapping("/pushrules/")
    public Mono<String> getPushrules() {
        return Mono.just("""
                {
                    "global": {
                        "underride": [
                            {
                                "conditions": [
                                    {
                                        "kind": "event_match",
                                        "key": "type",
                                        "pattern": "m.call.invite"
                                    }
                                ],
                                "actions": [
                                    "notify",
                                    {
                                        "set_tweak": "sound",
                                        "value": "ring"
                                    },
                                    {
                                        "set_tweak": "highlight",
                                        "value": false
                                    }
                                ],
                                "rule_id": ".m.rule.call",
                                "default": true,
                                "enabled": true
                            },
                            {
                                "conditions": [
                                    {
                                        "kind": "event_match",
                                        "key": "type",
                                        "pattern": "m.room.encrypted"
                                    },
                                    {
                                        "kind": "room_member_count",
                                        "is": "2"
                                    }
                                ],
                                "actions": [
                                    "notify",
                                    {
                                        "set_tweak": "sound",
                                        "value": "default"
                                    },
                                    {
                                        "set_tweak": "highlight",
                                        "value": false
                                    }
                                ],
                                "rule_id": ".m.rule.encrypted_room_one_to_one",
                                "default": true,
                                "enabled": true
                            },
                            {
                                "conditions": [
                                    {
                                        "kind": "event_match",
                                        "key": "type",
                                        "pattern": "m.room.message"
                                    },
                                    {
                                        "kind": "room_member_count",
                                        "is": "2"
                                    }
                                ],
                                "actions": [
                                    "notify",
                                    {
                                        "set_tweak": "sound",
                                        "value": "default"
                                    },
                                    {
                                        "set_tweak": "highlight",
                                        "value": false
                                    }
                                ],
                                "rule_id": ".m.rule.room_one_to_one",
                                "default": true,
                                "enabled": true
                            },
                            {
                                "conditions": [
                                    {
                                        "kind": "event_match",
                                        "key": "type",
                                        "pattern": "m.room.message"
                                    }
                                ],
                                "actions": [
                                    "notify",
                                    {
                                        "set_tweak": "highlight",
                                        "value": false
                                    }
                                ],
                                "rule_id": ".m.rule.message",
                                "default": true,
                                "enabled": true
                            },
                            {
                                "conditions": [
                                    {
                                        "kind": "event_match",
                                        "key": "type",
                                        "pattern": "m.room.encrypted"
                                    }
                                ],
                                "actions": [
                                    "notify",
                                    {
                                        "set_tweak": "highlight",
                                        "value": false
                                    }
                                ],
                                "rule_id": ".m.rule.encrypted",
                                "default": true,
                                "enabled": true
                            },
                            {
                                "conditions": [
                                    {
                                        "kind": "event_match",
                                        "key": "type",
                                        "pattern": "im.vector.modular.widgets"
                                    },
                                    {
                                        "kind": "event_match",
                                        "key": "content.type",
                                        "pattern": "jitsi"
                                    },
                                    {
                                        "kind": "event_match",
                                        "key": "state_key",
                                        "pattern": "*"
                                    }
                                ],
                                "actions": [
                                    "notify",
                                    {
                                        "set_tweak": "highlight",
                                        "value": false
                                    }
                                ],
                                "rule_id": ".im.vector.jitsi",
                                "default": true,
                                "enabled": true
                            }
                        ],
                        "sender": [],
                        "room": [],
                        "content": [
                            {
                                "actions": [
                                    "notify",
                                    {
                                        "set_tweak": "highlight"
                                    },
                                    {
                                        "set_tweak": "sound",
                                        "value": "default"
                                    }
                                ],
                                "rule_id": ".m.rule.contains_user_name",
                                "default": true,
                                "pattern": "wahaha",
                                "enabled": true
                            }
                        ],
                        "override": [
                            {
                                "conditions": [],
                                "actions": [],
                                "rule_id": ".m.rule.master",
                                "default": true,
                                "enabled": false
                            },
                            {
                                "conditions": [
                                    {
                                        "kind": "event_match",
                                        "key": "content.msgtype",
                                        "pattern": "m.notice"
                                    }
                                ],
                                "actions": [],
                                "rule_id": ".m.rule.suppress_notices",
                                "default": true,
                                "enabled": true
                            },
                            {
                                "conditions": [
                                    {
                                        "kind": "event_match",
                                        "key": "type",
                                        "pattern": "m.room.member"
                                    },
                                    {
                                        "kind": "event_match",
                                        "key": "content.membership",
                                        "pattern": "invite"
                                    },
                                    {
                                        "kind": "event_match",
                                        "key": "state_key",
                                        "pattern": "@wahaha:chat-server-test.homemart.club"
                                    }
                                ],
                                "actions": [
                                    "notify",
                                    {
                                        "set_tweak": "highlight",
                                        "value": false
                                    },
                                    {
                                        "set_tweak": "sound",
                                        "value": "default"
                                    }
                                ],
                                "rule_id": ".m.rule.invite_for_me",
                                "default": true,
                                "enabled": true
                            },
                            {
                                "conditions": [
                                    {
                                        "kind": "event_match",
                                        "key": "type",
                                        "pattern": "m.room.member"
                                    }
                                ],
                                "actions": [],
                                "rule_id": ".m.rule.member_event",
                                "default": true,
                                "enabled": true
                            },
                            {
                                "conditions": [
                                    {
                                        "kind": "event_property_contains",
                                        "key": "content.m\\\\.mentions.user_ids",
                                        "value": "@wahaha:chat-server-test.homemart.club"
                                    }
                                ],
                                "actions": [
                                    "notify",
                                    {
                                        "set_tweak": "highlight"
                                    },
                                    {
                                        "set_tweak": "sound",
                                        "value": "default"
                                    }
                                ],
                                "rule_id": ".m.rule.is_user_mention",
                                "default": true,
                                "enabled": true
                            },
                            {
                                "conditions": [
                                    {
                                        "kind": "contains_display_name"
                                    }
                                ],
                                "actions": [
                                    "notify",
                                    {
                                        "set_tweak": "highlight"
                                    },
                                    {
                                        "set_tweak": "sound",
                                        "value": "default"
                                    }
                                ],
                                "rule_id": ".m.rule.contains_display_name",
                                "default": true,
                                "enabled": true
                            },
                            {
                                "conditions": [
                                    {
                                        "kind": "event_property_is",
                                        "key": "content.m\\\\.mentions.room",
                                        "value": true
                                    },
                                    {
                                        "kind": "sender_notification_permission",
                                        "key": "room"
                                    }
                                ],
                                "actions": [
                                    "notify",
                                    {
                                        "set_tweak": "highlight"
                                    }
                                ],
                                "rule_id": ".m.rule.is_room_mention",
                                "default": true,
                                "enabled": true
                            },
                            {
                                "conditions": [
                                    {
                                        "kind": "sender_notification_permission",
                                        "key": "room"
                                    },
                                    {
                                        "kind": "event_match",
                                        "key": "content.body",
                                        "pattern": "@room"
                                    }
                                ],
                                "actions": [
                                    "notify",
                                    {
                                        "set_tweak": "highlight"
                                    }
                                ],
                                "rule_id": ".m.rule.roomnotif",
                                "default": true,
                                "enabled": true
                            },
                            {
                                "conditions": [
                                    {
                                        "kind": "event_match",
                                        "key": "type",
                                        "pattern": "m.room.tombstone"
                                    },
                                    {
                                        "kind": "event_match",
                                        "key": "state_key",
                                        "pattern": ""
                                    }
                                ],
                                "actions": [
                                    "notify",
                                    {
                                        "set_tweak": "highlight"
                                    }
                                ],
                                "rule_id": ".m.rule.tombstone",
                                "default": true,
                                "enabled": true
                            },
                            {
                                "conditions": [
                                    {
                                        "kind": "event_match",
                                        "key": "type",
                                        "pattern": "m.reaction"
                                    }
                                ],
                                "actions": [],
                                "rule_id": ".m.rule.reaction",
                                "default": true,
                                "enabled": true
                            },
                            {
                                "conditions": [
                                    {
                                        "kind": "event_match",
                                        "key": "type",
                                        "pattern": "m.room.server_acl"
                                    },
                                    {
                                        "kind": "event_match",
                                        "key": "state_key",
                                        "pattern": ""
                                    }
                                ],
                                "actions": [],
                                "rule_id": ".m.rule.room.server_acl",
                                "default": true,
                                "enabled": true
                            },
                            {
                                "conditions": [
                                    {
                                        "kind": "event_property_is",
                                        "key": "content.m\\\\.relates_to.rel_type",
                                        "value": "m.replace"
                                    }
                                ],
                                "actions": [],
                                "rule_id": ".m.rule.suppress_edits",
                                "default": true,
                                "enabled": true
                            }
                        ]
                    }
                }""");
    }

    @GetMapping("/room_keys/version")
    public Mono<String> getRoomKeysVersion()
    {
        return Mono.just("""
                {
                  "algorithm": "m.megolm_backup.v1.curve25519-aes-sha2",
                  "auth_data": {
                    "public_key": "abcdefg",
                    "signatures": {
                      "@alice:example.org": {
                        "ed25519:deviceid": "signature"
                      }
                    }
                  },
                  "count": 42,
                  "etag": "anopaquestring",
                  "version": "1"
                }
                """);
    }
}
