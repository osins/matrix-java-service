package club.hm.matrix.user.grpc.service.mapper;

public interface IProtoMapper<RPC,ENTITY> {

    RPC to(ENTITY f);

    ENTITY from(RPC t);
}
