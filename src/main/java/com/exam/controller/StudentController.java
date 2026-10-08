package com.exam.controller;

import com.exam.model.*;
import com.exam.repo.*;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController @RequestMapping("/api/student")
public class StudentController {
    record SubmitReq(Map<Long, String> answers) {}

    private final ExamRepo exams; private final ResultRepo results; private final UserRepo users;
    public StudentController(ExamRepo e, ResultRepo r, UserRepo u) { exams = e; results = r; users = u; }

    /** Exams that have at least one question. Correct answers are never serialised. */
    @GetMapping("/exams")
    public List<Exam> available() { return exams.findAll().stream().filter(e -> !e.getQuestions().isEmpty()).toList(); }

    @GetMapping("/exams/{id}")
    public Exam one(@PathVariable Long id) {
        return exams.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exam not found"));
    }

    /** Automatic evaluation: compare each answer with the stored correct option. */
    @PostMapping("/exams/{id}/submit")
    public Result submit(@PathVariable Long id, @RequestBody SubmitReq req, Authentication auth) {
        User u = users.findByEmail(auth.getName()).orElseThrow();
        Exam e = one(id);
        if (results.existsByUserIdAndExamId(u.getId(), id))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "You have already attempted this exam");
        Map<Long, String> ans = req.answers() == null ? Map.of() : req.answers();
        int score = 0;
        for (Question q : e.getQuestions())
            if (q.getCorrectOption().equalsIgnoreCase(ans.getOrDefault(q.getId(), ""))) score += q.getMarks();
        int total = e.getTotalMarks();
        double pct = total == 0 ? 0 : Math.round(score * 10000.0 / total) / 100.0;

        Result r = new Result();
        r.setUserId(u.getId()); r.setStudentName(u.getName());
        r.setExamId(e.getId()); r.setExamTitle(e.getTitle());
        r.setScore(score); r.setTotalMarks(total); r.setPercentage(pct);
        r.setPassed(pct >= e.getPassPercentage());
        return results.save(r);
    }

    @GetMapping("/results")
    public List<Result> mine(Authentication auth) {
        return results.findByUserIdOrderBySubmittedAtDesc(users.findByEmail(auth.getName()).orElseThrow().getId());
    }
}
