package com.exam.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
public class Question {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(length = 1000) private String text;
    private String optionA, optionB, optionC, optionD;
    /** A, B, C or D. Write-only so it is never sent to the browser. */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) private String correctOption;
    private int marks = 1;
    @ManyToOne @JoinColumn(name = "exam_id") @JsonIgnore private Exam exam;

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getText() { return text; } public void setText(String t) { this.text = t; }
    public String getOptionA() { return optionA; } public void setOptionA(String o) { this.optionA = o; }
    public String getOptionB() { return optionB; } public void setOptionB(String o) { this.optionB = o; }
    public String getOptionC() { return optionC; } public void setOptionC(String o) { this.optionC = o; }
    public String getOptionD() { return optionD; } public void setOptionD(String o) { this.optionD = o; }
    public String getCorrectOption() { return correctOption; } public void setCorrectOption(String c) { this.correctOption = c; }
    public int getMarks() { return marks; } public void setMarks(int m) { this.marks = m; }
    public Exam getExam() { return exam; } public void setExam(Exam e) { this.exam = e; }
}
