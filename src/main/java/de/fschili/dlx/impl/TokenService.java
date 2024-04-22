package de.fschili.dlx.impl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;

import de.fschili.dlx.openapi.api.TokenApiDelegate;
import de.fschili.dlx.openapi.model.TfaQuestion;
import de.fschili.dlx.openapi.model.TfaQuestions;
import de.fschili.dlx.openapi.model.TfaQuestion.AnswerFormatEnum;
import de.fschili.dlx.openapi.model.TfaQuestion.QuestionTypeEnum;

@Service
public class TokenService implements TokenApiDelegate {

    private final static Logger log = LoggerFactory.getLogger(TokenApiDelegate.class);

    public static final String TOKEN_BIRTHDATE = "ABC-S1Z-98A";
    public static final String TOKEN_BIRTHDATE_QUESTION = "Wann haben Sie Geburtstag?";
    public static final String TOKEN_BIRTHDATE_ANSWER = "19700101";

    public static final String TOKEN_PASSWORD = "DF2-11Z-KS4";
    public static final String TOKEN_PASSWORD_QUESTION = "Wie lautet Ihr Einmal-Passwort?";
    public static final String TOKEN_PASSWORD_ANSWER = "132-238-252";

    public static final String TOKEN_CUSTOM = "HDF-34F-HK6";
    public static final String TOKEN_CUSTOM_QUESTION_1 = "Wann war ihre Untersuchung?";
    public static final String TOKEN_CUSTOM_ANSWER_1 = "20240419";
    public static final String TOKEN_CUSTOM_QUESTION_2 = "Wie heißt Ihr behandelnder Arzt?";
    public static final String TOKEN_CUSTOM_ANSWER_2 = "Dr. Mayer";

    public static final String TOKEN_QUESTION_401 = "Wann haben Sie Geburtstag?";

    public static final Set<String> AVAILABLE_TOKENS = Stream.of(TOKEN_BIRTHDATE, TOKEN_PASSWORD, TOKEN_CUSTOM)
            .collect(Collectors.toCollection(HashSet::new));

    @Autowired
    private NativeWebRequest nativeWebRequest;

    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(nativeWebRequest);
    }

    @Override
    public ResponseEntity<TfaQuestions> tokenIdGet(String id, String X_DICOM_LINK_EXCHANGE) {
        log.debug("Got token request for: " + id + " (DLX API: " + X_DICOM_LINK_EXCHANGE + ")");

        if (X_DICOM_LINK_EXCHANGE == null || X_DICOM_LINK_EXCHANGE.isEmpty()) {
            log.info("X_DICOM_LINK_EXCHANGE flag is not set. Returning normal portal page without DLX functionality. But continue in DEMO mode..");
        }

        if (!(isBirthdayToken(id) || isCustomToken(id))) {
            log.warn("Token '" + id + "' is not valide or known! Will retrun default question for security reasons.");
        }

        TfaQuestions questions = new TfaQuestions();
        questions.addApiInfoItem(ApiInfoService.getApiInfo(getRequest()));

        if (isBirthdayToken(id)) {
            log.info("Return birthdate question for token '" + id + "'");
            TfaQuestion question = new TfaQuestion();
            question.setQuestion(TOKEN_BIRTHDATE_QUESTION);
            question.setQuestionId("1");
            question.setQuestionType(QuestionTypeEnum.PAT_BIRTH_DATE);
            question.setAnswerFormat(AnswerFormatEnum.DATE);

            questions.addTfaQuestionItem(question);
        }
        else if (isPasswordToken(id)) {
            log.info("Return password question for token '" + id + "'");
            TfaQuestion question = new TfaQuestion();
            question.setQuestion(TOKEN_PASSWORD_QUESTION);
            question.setQuestionId("1");
            question.setQuestionType(QuestionTypeEnum.PASSWORD);
            question.setAnswerFormat(AnswerFormatEnum.STRING);

            questions.addTfaQuestionItem(question);
        }
        else if (isCustomToken(id)) {
            log.info("Return custom questions for token '" + id + "'");
            TfaQuestion question = new TfaQuestion();
            question.setQuestion(TOKEN_CUSTOM_QUESTION_1);
            question.setQuestionId("1");
            question.setQuestionType(QuestionTypeEnum.STUDY_DATE);
            question.setAnswerFormat(AnswerFormatEnum.DATE);

            questions.addTfaQuestionItem(question);

            TfaQuestion question2 = new TfaQuestion();
            question2.setQuestion(TOKEN_CUSTOM_QUESTION_2);
            question2.setQuestionId("2");
            question2.setQuestionType(QuestionTypeEnum.CUSTOM);
            question2.setAnswerFormat(AnswerFormatEnum.STRING);

            questions.addTfaQuestionItem(question2);
        }
        else {
            log.info("Return DEFAULT birthdate question for token '" + id + "' (This will never get a correct answer!)");
            TfaQuestion question = new TfaQuestion();
            question.setQuestion(TOKEN_QUESTION_401);
            question.setQuestionId("1");
            question.setQuestionType(QuestionTypeEnum.PAT_BIRTH_DATE);
            question.setAnswerFormat(AnswerFormatEnum.DATE);

            questions.addTfaQuestionItem(question);
        }

        if (!questions.getTfaQuestion().isEmpty()) {
            return new ResponseEntity<TfaQuestions>(questions, HttpStatus.OK);
        }
        else {
            // should never happen
            log.error("Token type not implemented.");
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        }
    }

    private static boolean isBirthdayToken(String token) {
        return (token != null && token.equalsIgnoreCase(TOKEN_BIRTHDATE));
    }

    private static boolean isPasswordToken(String token) {
        return (token != null && token.equalsIgnoreCase(TOKEN_PASSWORD));
    }

    private static boolean isCustomToken(String token) {
        return (token != null && token.equalsIgnoreCase(TOKEN_CUSTOM));
    }

    public static boolean isValidToken(String token) {
        return (isBirthdayToken(token) || isPasswordToken(token) || isCustomToken(token));
    }

}
