package com.exam.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Result {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    private Long userId, examId;
    private String studentName, examTitle;
    private int score, totalMarks;
    private double percentage;
    private boolean passed;
    private LocalDateTime submittedAt = LocalDateTime.now();

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; } public void setUserId(Long u) { this.userId = u; }
    public Long getExamId() { return examId; } public void setExamId(Long e) { this.examId = e; }
    public String getStudentName() { return studentName; } public void setStudentName(String s) { this.studentName = s; }
    public String getExamTitle() { return examTitle; } public void setExamTitle(String s) { this.examTitle = s; }
    public int getScore() { return score; } public void setScore(int s) { this.score = s; }
    public int getTotalMarks() { return totalMarks; } public void setTotalMarks(int t) { this.totalMarks = t; }
    public double getPercentage() { return percentage; } public void setPercentage(double p) { this.percentage = p; }
    public boolean isPassed() { return passed; } public void setPassed(boolean p) { this.passed = p; }
    public LocalDateTime getSubmittedAt() { return submittedAt; } public void setSubmittedAt(LocalDateTime t) { this.submittedAt = t; }
}
