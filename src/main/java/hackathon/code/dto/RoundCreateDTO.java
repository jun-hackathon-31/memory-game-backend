package hackathon.code.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoundCreateDTO {

    @NotNull
    private Integer complexity;

    @NotNull
    private Integer moves;

    @NotNull
    private String userName;
}
