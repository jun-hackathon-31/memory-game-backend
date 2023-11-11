package hackathon.code.mapper;

import hackathon.code.dto.RoundCreateDTO;
import hackathon.code.dto.RoundDTO;
import hackathon.code.model.Round;
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
public abstract class RoundMapper {

    @Mapping(target = "gamer.name", source = "userName")
    public abstract Round map(RoundCreateDTO dto);

    @Mapping(source = "gamer.name", target = "userName")
    public abstract RoundDTO map(Round model);
}
