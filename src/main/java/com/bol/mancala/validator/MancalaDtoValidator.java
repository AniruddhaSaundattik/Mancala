package com.bol.mancala.validator;

import com.bol.mancala.model.MancalaDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.stream.IntStream;

@Component
public class MancalaDtoValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return MancalaDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        MancalaDTO target = (MancalaDTO) o;
        if (!target.isGameOver() &&
            (IntStream.of(target.getPlayer1().getSmallPitsCount()).sum()!=0
                && IntStream.of(target.getPlayer1().getSmallPitsCount()).sum()!=0)) {
            errors.rejectValue("gameOver", "invalid.gameOver", "Game not yet over");
        }
    }
}
