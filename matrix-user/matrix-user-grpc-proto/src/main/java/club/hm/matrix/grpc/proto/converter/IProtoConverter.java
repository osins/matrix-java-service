package club.hm.matrix.grpc.proto.converter;

import java.util.List;

public interface IProtoConverter<RPC,ENTITY> {

    RPC to(ENTITY f);

    ENTITY from(RPC t);

    List<RPC> to(List<ENTITY> f);

    List<ENTITY> from(List<RPC> t);
}
