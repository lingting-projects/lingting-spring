package live.lingting.spring.grpc.mapstruct;

import live.lingting.framework.grpc.properties.GrpcClientProperties;
import live.lingting.framework.grpc.properties.GrpcServerProperties;
import live.lingting.spring.grpc.properties.GrpcSpringProperties;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author lingting 2024-02-05 16:18
 */
@Mapper
public interface SpringGrpcMapstruct {

	SpringGrpcMapstruct INSTANCE = Mappers.getMapper(SpringGrpcMapstruct.class);

	GrpcClientProperties client(GrpcSpringProperties properties, GrpcSpringProperties.Client client);

	GrpcServerProperties server(GrpcSpringProperties properties, GrpcSpringProperties.Server server);

}
