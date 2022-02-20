package com.bol.mancala.validator;

import com.bol.mancala.model.PlayerDTO;
import com.bol.mancala.model.TurnDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class TurnDtoValidator implements Validator {


    @Override
    public boolean supports(Class<?> aClass) {
        return TurnDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        TurnDTO target = (TurnDTO) o;
        if (!target.getPlayer().equals(target.getMancalaObj().getPlayer1().getName())
            && !target.getPlayer().equals(target.getMancalaObj().getPlayer2().getName())) {
            errors.rejectValue("player", "invalid.player", "None of the players");
        }
        PlayerDTO currentPlayer = target.getPlayer().equals(target.getMancalaObj().getPlayer1().getName())?
                target.getMancalaObj().getPlayer1():target.getMancalaObj().getPlayer2();
        if (currentPlayer.getSmallPitsCount()[target.getPitClicked()] <= 0) {
            errors.rejectValue("pitClicked", "invalid.pitClicked", "Clicked pit is empty");
        }
    }
}
