package com.exam.repo;
import com.exam.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ExamRepo extends JpaRepository<Exam, Long> {}
