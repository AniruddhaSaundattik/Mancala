package com.bol.mancala.validator;

import com.bol.mancala.model.GameStartDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class GameStartDtoValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return GameStartDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        GameStartDTO target = (GameStartDTO) o;
        if(StringUtils.isNotEmpty(target.getPlayer1())
                && StringUtils.isNotEmpty(target.getPlayer2())
                && target.getPlayer1().equalsIgnoreCase(target.getPlayer2())){
            errors.rejectValue("player1","incorrect.playerName","Both players cannot have the same name");
        }
    }
}
