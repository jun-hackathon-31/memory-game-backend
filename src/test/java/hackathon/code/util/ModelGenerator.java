package hackathon.code.util;

import hackathon.code.model.Round;
import hackathon.code.model.User;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ModelGenerator {
    private Model<Round> roundModel;
    private Model<User> userModel;

    @Autowired
    private Faker faker;

    @PostConstruct
    private void init() {
        userModel = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getRounds))
                .supply(Select.field(User::getName), () -> faker.name().fullName())
                .toModel();

        roundModel = Instancio.of(Round.class)
                .ignore(Select.field(Round::getId))
                .ignore(Select.field(Round::getGamer))
                .supply(Select.field(Round::getComplexity), () -> 4)
                .supply(Select.field(Round::getMoves), () -> (Integer) faker.number().numberBetween(8, 30))
                .toModel();
    }
}
