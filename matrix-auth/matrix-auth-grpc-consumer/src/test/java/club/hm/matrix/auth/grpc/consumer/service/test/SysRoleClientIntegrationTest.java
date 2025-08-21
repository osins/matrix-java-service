//package club.hm.matrix.auth.grpc.consumer.service.test;
//
//import club.hm.matrix.auth.grpc.*;
//import club.hm.matrix.auth.grpc.api.service.SysRoleClientService;
//import club.hm.matrix.auth.grpc.consumer.config.GrpcClientConfig;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import reactor.test.StepVerifier;
//
//@Slf4j
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes = GrpcClientConfig.class)
//class SysRoleClientIntegrationTest {
//    @Autowired
//    private SysRoleClientService sysRoleClient;
//
//    @Test
//    void testFindAllRoles() {
//        StepVerifier.create(sysRoleClient.findAll())
//                .expectNextMatches(list -> list.getRolesCount() > 0)
//                .verifyComplete();
//    }
//
//    @Test
//    void testFindById() {
//        long roleId = 1; // 需保证这个角色在测试数据库中存在
//        StepVerifier.create(sysRoleClient.findById(roleId))
//                .expectNextMatches(resp -> resp.getRole().getId() == roleId)
//                .verifyComplete();
//    }
//
//    @Test
//    void testCreateRole() {
//        SysRole newRole = SysRole.newBuilder()
//                .setName("TEST_ROLE")
//                .setDescription("测试角色")
//                .build();
//
//        StepVerifier.create(sysRoleClient.createRole(newRole))
//                .expectNextMatches(resp -> resp.getRole().getName().equals("TEST_ROLE"))
//                .verifyComplete();
//    }
//
//    @Test
//    void testDeleteRole() {
//        long roleIdToDelete = 99; // 需保证这个角色存在或之前创建
//        StepVerifier.create(sysRoleClient.deleteRole(roleIdToDelete))
//                .expectNextMatches(DeleteResponse::getSuccess)
//                .verifyComplete();
//    }
//}