package com.exam.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Exam {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private String title;
    private int durationMinutes;
    private int passPercentage = 40;
    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; } public void setTitle(String t) { this.title = t; }
    public int getDurationMinutes() { return durationMinutes; } public void setDurationMinutes(int d) { this.durationMinutes = d; }
    public int getPassPercentage() { return passPercentage; } public void setPassPercentage(int p) { this.passPercentage = p; }
    public List<Question> getQuestions() { return questions; } public void setQuestions(List<Question> q) { this.questions = q; }
    public int getTotalMarks() { return questions.stream().mapToInt(Question::getMarks).sum(); }
}
