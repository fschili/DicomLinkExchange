package de.fschili.dlx.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import de.fschili.dlx.jwt.JWTUtil;
import de.fschili.dlx.openapi.api.TokentfaApiDelegate;
import de.fschili.dlx.openapi.model.TfaAnswer;
import de.fschili.dlx.openapi.model.TfaAnswers;

@Service
public class TokenTfaService implements TokentfaApiDelegate {

    private final static Logger log = LoggerFactory.getLogger(TokentfaApiDelegate.class);

    @Override
    public ResponseEntity<String> tokentfaValuePost(String value, TfaAnswers tfaAnswers) {
        log.debug("Got token tfa request for: " + value);
        List<TfaAnswer> answers = tfaAnswers.getTfaAnswer();
        if (answers == null || answers.isEmpty()) {
            log.error("Got empty tfaAnswers.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        for (TfaAnswer tfa : answers) {
            log.debug("Got tfaAnswers: " + tfa.getQuestionId() + " -> " + tfa.getAnswer());
        }

        if (value.equalsIgnoreCase(TokenService.TOKEN_BIRTHDATE)) {
            for (TfaAnswer tfa : answers) {
                if (tfa.getQuestionId().equals("1")) {
                    if (tfa.getAnswer().equalsIgnoreCase(TokenService.TOKEN_BIRTHDATE_ANSWER)) {
                        log.info("Got correct answers for birthday token '" + value + "'");
                        return new ResponseEntity<String>(JWTUtil.generateJwt(value), HttpStatus.OK);
                    }
                }
            }
        }
        else if (value.equalsIgnoreCase(TokenService.TOKEN_PASSWORD)) {
            for (TfaAnswer tfa : answers) {
                if (tfa.getQuestionId().equals("1")) {
                    if (tfa.getAnswer().equalsIgnoreCase(TokenService.TOKEN_PASSWORD_ANSWER)) {
                        log.info("Got correct answers for password token '" + value + "'");
                        return new ResponseEntity<String>(JWTUtil.generateJwt(value), HttpStatus.OK);
                    }
                }
            }
        }
        else if (value.equalsIgnoreCase(TokenService.TOKEN_CUSTOM)) {
            boolean answer1 = false;
            boolean answer2 = false;
            for (TfaAnswer tfa : answers) {
                if (tfa.getQuestionId().equals("1")) {
                    answer1 = tfa.getAnswer().equalsIgnoreCase(TokenService.TOKEN_CUSTOM_ANSWER_1);
                }
                else if (tfa.getQuestionId().equals("2")) {
                    answer2 = tfa.getAnswer().equalsIgnoreCase(TokenService.TOKEN_CUSTOM_ANSWER_2);
                }
                if (answer1 && answer2) {
                    log.info("Got correct answers for custom token '" + value + "'");
                    return new ResponseEntity<String>(JWTUtil.generateJwt(value), HttpStatus.OK);
                }
            }
        }

        log.error("Got wrong tfaAnswers.");

        // return unauthorized for everything else!
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

}
