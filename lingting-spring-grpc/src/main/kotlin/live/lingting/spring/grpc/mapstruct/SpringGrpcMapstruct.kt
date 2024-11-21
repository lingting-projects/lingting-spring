package live.lingting.spring.grpc.mapstruct

import live.lingting.spring.grpc.properties.GrpcSpringProperties
import org.mapstruct.Mapper

/**
 * @author lingting 2024-02-05 16:18
 */
@Mapper
interface SpringGrpcMapstruct {
    fun client(properties: GrpcSpringProperties, client: GrpcSpringProperties.Client): GrpcClientProperties

    fun server(properties: GrpcSpringProperties, server: GrpcSpringProperties.Server): GrpcServerProperties

    companion object {
        val INSTANCE: SpringGrpcMapstruct = Mappers.getMapper(SpringGrpcMapstruct::class.java)
    }
}
