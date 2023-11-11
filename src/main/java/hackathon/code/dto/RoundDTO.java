package hackathon.code.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoundDTO {
    private Long id;
    private Integer complexity;
    private Integer moves;
    private String userName;
}
