package de.fschili.dlx.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import de.fschili.dlx.openapi.api.TokenApiDelegate;
import de.fschili.dlx.openapi.model.TfaQuestionsInner;
import de.fschili.dlx.openapi.model.TfaQuestionsInner.AnswerFormatEnum;
import de.fschili.dlx.openapi.model.TfaQuestionsInner.QuestionTypeEnum;

@Service
public class Token implements TokenApiDelegate {

    private final static Logger log = LoggerFactory.getLogger(TokenApiDelegate.class);

    public static final String TOKEN_BIRTHDATE = "ABC-S1Z-98A";
    public static final String TOKEN_BIRTHDATE_QUESTION = "Wann haben Sie Geburtstag?";
    public static final String TOKEN_BIRTHDATE_ANSWER = "19700102";

    public static final String TOKEN_CUSTOM = "HDF-34F-HK6";
    public static final String TOKEN_CUSTOM_QUESTION_1 = "Wann war ihre Untersuchung?";
    public static final String TOKEN_CUSTOM_ANSWER_1 = "20240203";
    public static final String TOKEN_CUSTOM_QUESTION_2 = "Wie heißt Ihr behandelnder Arzt?";
    public static final String TOKEN_CUSTOM_ANSWER_2 = "Dr. Mayer";

    @Override
    public ResponseEntity<List<TfaQuestionsInner>> tokenIdGet(String id) {
        log.debug("Got token request for: " + id);

        if (!(isBirthdayToken(id) || isCustomToken(id))) {
            log.error("Token '" + id + "' is not valide!");
            //return ResponseEntity.status(HttpStatus.NOT_FOUND).header("Status", "Not Found").build();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (isBirthdayToken(id)) {
            log.error("Return birthdate question for token '" + id + "'");
            TfaQuestionsInner question = new TfaQuestionsInner();
            question.setQuestion(TOKEN_BIRTHDATE_QUESTION);
            question.setQuestionId("1");
            question.setQuestionType(QuestionTypeEnum.PAT_BIRTH_DATE);
            question.setAnswerFormat(AnswerFormatEnum.DATE);

            List<TfaQuestionsInner> questions = new ArrayList<TfaQuestionsInner>();
            questions.add(question);

            return new ResponseEntity<List<TfaQuestionsInner>>(questions, HttpStatus.OK);
        }
        else if (isCustomToken(id)) {
            log.error("Return custom questions for token '" + id + "'");
            TfaQuestionsInner question = new TfaQuestionsInner();
            question.setQuestion(TOKEN_CUSTOM_QUESTION_1);
            question.setQuestionId("1");
            question.setQuestionType(QuestionTypeEnum.STUDY_DATE);
            question.setAnswerFormat(AnswerFormatEnum.DATE);

            TfaQuestionsInner question2 = new TfaQuestionsInner();
            question2.setQuestion(TOKEN_CUSTOM_QUESTION_2);
            question2.setQuestionId("2");
            question2.setQuestionType(QuestionTypeEnum.CUSTOM);
            question2.setAnswerFormat(AnswerFormatEnum.STRING);

            List<TfaQuestionsInner> questions = new ArrayList<TfaQuestionsInner>();
            questions.add(question);
            questions.add(question2);

            return new ResponseEntity<List<TfaQuestionsInner>>(questions, HttpStatus.OK);
        }
        else {
            log.error("Token type not implemented.");
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        }
    }

    private boolean isBirthdayToken(String token) {
        return (token != null && token.equalsIgnoreCase(TOKEN_BIRTHDATE));
    }

    private boolean isCustomToken(String token) {
        return (token != null && token.equalsIgnoreCase(TOKEN_CUSTOM));
    }

}
