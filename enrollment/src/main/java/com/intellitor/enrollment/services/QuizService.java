package com.intellitor.enrollment.services;

import com.intellitor.common.dtos.QuizDTO;
import com.intellitor.common.entities.Enrollment;
import com.intellitor.common.entities.Quiz;
import com.intellitor.common.mappers.QuizMapper;
import com.intellitor.enrollment.repos.EnrollmentRepository;
import com.intellitor.enrollment.repos.QuizRepository;
import com.intellitor.common.utils.ErrorMessages;
import com.intellitor.common.utils.ObjectNames;
import com.intellitor.common.utils.Response;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuizService {

    private final QuizRepository quizRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final QuizMapper quizMapper;

    public QuizService(QuizRepository quizRepository, EnrollmentRepository enrollmentRepository, QuizMapper quizMapper) {
        this.quizRepository = quizRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.quizMapper = quizMapper;
    }

    public Response findById(Long id) {
        Optional<Quiz> byId = quizRepository.findById(id);
        return byId.map(quiz ->
                new Response(200, quizMapper.toModel(quiz))).orElseGet(() ->
                new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.QUIZ, id)));
    }

    public Response createQuiz(QuizDTO quizDTO) {
        Quiz entity = quizMapper.toEntity(quizDTO);
        return saveQuiz(quizDTO, entity);
    }

    public Response updateQuiz(QuizDTO quizDTO) {
        Optional<Quiz> byId = quizRepository.findById(quizDTO.getId());
        if (byId.isEmpty()) {
            return new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.QUIZ, quizDTO.getId()));
        }
        Quiz entity = quizMapper.toEntity(quizDTO);
        entity.setId(byId.get().getId());
        return saveQuiz(quizDTO, entity);
    }

    public Response deleteQuiz(Long id) {
        Optional<Quiz> found = quizRepository.findById(id);
        if (found.isEmpty()) {
            return new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.QUIZ, id));
        }
        quizRepository.deleteById(id);
        return new Response(200, quizMapper.toModel(found.get()));
    }

    private Response saveQuiz(QuizDTO quizDTO, Quiz entity) {
        Optional<Enrollment> byId = enrollmentRepository.findById(quizDTO.getEnrollmentId());
        if (byId.isEmpty()) {
            return new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.ENROLLMENT, quizDTO.getEnrollmentId()));
        }
        if (quizRepository.findByEnrollment(byId.get()).isPresent()) {
            return new Response(400, String.format(ErrorMessages.OBJECT_FIELD_ALREADY_EXISTS, ObjectNames.QUIZ, "enrollment", byId.get().getId()));
        }
        Quiz saved = quizRepository.save(entity);
        return new Response(200, quizMapper.toModel(saved));
    }
}
