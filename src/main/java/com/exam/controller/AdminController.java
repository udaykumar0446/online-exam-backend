package com.exam.controller;

import com.exam.model.*;
import com.exam.repo.*;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController @RequestMapping("/api/admin")
public class AdminController {
    private final ExamRepo exams; private final ResultRepo results;
    public AdminController(ExamRepo e, ResultRepo r) { exams = e; results = r; }

    @GetMapping("/exams") public List<Exam> list() { return exams.findAll(); }

    @PostMapping("/exams")
    public Exam create(@RequestBody Exam e) {
        if (e.getTitle() == null || e.getTitle().isBlank() || e.getDurationMinutes() <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title and a positive duration are required");
        e.setId(null); e.getQuestions().clear();
        return exams.save(e);
    }

    @DeleteMapping("/exams/{id}")
    public void delete(@PathVariable Long id) { exams.deleteById(id); }

    @PostMapping("/exams/{id}/questions")
    public Exam addQuestion(@PathVariable Long id, @RequestBody Question q) {
        Exam e = exams.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exam not found"));
        if (q.getText() == null || q.getCorrectOption() == null || !"ABCD".contains(q.getCorrectOption().toUpperCase()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Question text and a correct option (A-D) are required");
        q.setId(null); q.setCorrectOption(q.getCorrectOption().toUpperCase()); q.setExam(e);
        e.getQuestions().add(q);
        return exams.save(e);
    }

    @DeleteMapping("/exams/{examId}/questions/{qid}")
    public void deleteQuestion(@PathVariable Long examId, @PathVariable Long qid) {
        Exam e = exams.findById(examId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exam not found"));
        e.getQuestions().removeIf(q -> q.getId().equals(qid));
        exams.save(e);
    }

    @GetMapping("/results") public List<Result> allResults() { return results.findAllByOrderBySubmittedAtDesc(); }
}
