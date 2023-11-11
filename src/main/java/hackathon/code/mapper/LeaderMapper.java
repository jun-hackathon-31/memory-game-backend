package hackathon.code.mapper;

import hackathon.code.dto.LeaderDTO;
import hackathon.code.model.Leader;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        uses = { ReferenceMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class LeaderMapper {

    @Mapping(target = "name", source = "user.name")
    public abstract LeaderDTO map(Leader model);
}
