package com.intellitor.dao.services;

import com.intellitor.common.dtos.QuizDTO;
import com.intellitor.common.utils.ErrorMessages;
import com.intellitor.common.utils.ObjectNames;
import com.intellitor.common.utils.Response;
import com.intellitor.dao.entities.Enrollment;
import com.intellitor.dao.entities.Quiz;
import com.intellitor.dao.mappers.QuizMapper;
import com.intellitor.dao.repositories.EnrollmentRepository;
import com.intellitor.dao.repositories.QuizRepository;
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
        Quiz quiz = quizRepository.findById(id).orElse(null);
        if (quiz == null) {
            return new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.QUIZ, id));
        }
        return new Response(200, quizMapper.toModel(quiz));
    }

    public Response createQuiz(QuizDTO quizDTO) {
        Quiz entity = quizMapper.toEntity(quizDTO);
        Enrollment enrollment = enrollmentRepository.findById(quizDTO.getEnrollmentId()).orElse(null);
        if (enrollment == null) {
            return new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.ENROLLMENT, quizDTO.getEnrollmentId()));
        }
        if (quizRepository.findByEnrollment(enrollment).isPresent()) {
            return new Response(400, String.format(ErrorMessages.OBJECT_FIELD_ALREADY_EXISTS, ObjectNames.QUIZ, "enrollment", enrollment.toString()));
        }
        Quiz saved = quizRepository.save(entity);
        return new Response(200, quizMapper.toModel(saved));
    }

    public Response updateQuiz(QuizDTO quizDTO) {
        Quiz entity = quizMapper.toEntity(quizDTO);
        if (quizRepository.findById(quizDTO.getId()).isEmpty()) {
            return new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.QUIZ, quizDTO.getId()));
        }
        Quiz saved = quizRepository.save(entity);
        return new Response(200, quizMapper.toModel(saved));
    }

    public Response deleteQuiz(Long id) {
        Optional<Quiz> found = quizRepository.findById(id);
        if (found.isEmpty()) {
            return new Response(400, String.format(ErrorMessages.NO_OBJECT_FOUND_BY_ID, ObjectNames.QUIZ, id));
        }
        quizRepository.deleteById(id);
        return new Response(200, quizMapper.toModel(found.get()));
    }
}
