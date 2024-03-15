package de.fschili.dlx.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import de.fschili.dlx.JWTUtil;
import de.fschili.dlx.openapi.api.TokentfaApiDelegate;
import de.fschili.dlx.openapi.model.TfaAnswersInner;

@Service
public class TokenTfa implements TokentfaApiDelegate {

    private final static Logger log = LoggerFactory.getLogger(TokentfaApiDelegate.class);

    @Override
    public ResponseEntity<String> tokentfaIdPost(String id, List<TfaAnswersInner> tfaAnswersInner) {
        log.debug("Got token tfa request for: " + id);
        for (TfaAnswersInner tfa : tfaAnswersInner) {
            log.debug("Got tfa Q/A: " + tfa.getQuestionId() + " -> " + tfa.getAnswer());
        }

        if (id.equalsIgnoreCase(Token.TOKEN_BIRTHDATE)) {
            for (TfaAnswersInner tfa : tfaAnswersInner) {
                if (tfa.getQuestionId().equals("1")) {
                    if (tfa.getAnswer().equalsIgnoreCase(Token.TOKEN_BIRTHDATE_ANSWER)) {
                        return new ResponseEntity<String>(JWTUtil.generateJwt(id), HttpStatus.OK);
                    }
                }
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        }
        else if (id.equalsIgnoreCase(Token.TOKEN_CUSTOM)) {
            boolean answer1 = false;
            boolean answer2 = false;
            for (TfaAnswersInner tfa : tfaAnswersInner) {
                if (tfa.getQuestionId().equals("1")) {
                    answer1 = tfa.getAnswer().equalsIgnoreCase(Token.TOKEN_CUSTOM_ANSWER_1);
                }
                else if (tfa.getQuestionId().equals("2")) {
                    answer2 = tfa.getAnswer().equalsIgnoreCase(Token.TOKEN_CUSTOM_ANSWER_2);
                }
                if (answer1 && answer2) {
                    return new ResponseEntity<String>(JWTUtil.generateJwt(id), HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
