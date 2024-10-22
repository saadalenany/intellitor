package com.intellitor.enrollment.controllers;

import com.intellitor.common.dtos.QuizDTO;
import com.intellitor.common.utils.Response;
import com.intellitor.enrollment.services.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getQuiz(@PathVariable Long id) {
        return ResponseEntity.ok(quizService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Response> createQuiz(@RequestBody QuizDTO quiz) {
        return ResponseEntity.ok(quizService.createQuiz(quiz));
    }

    @PutMapping
    public ResponseEntity<Response> updateQuiz(@RequestBody QuizDTO quiz) {
        return ResponseEntity.ok(quizService.updateQuiz(quiz));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteQuiz(@PathVariable Long id) {
        return ResponseEntity.ok(quizService.deleteQuiz(id));
    }

}
