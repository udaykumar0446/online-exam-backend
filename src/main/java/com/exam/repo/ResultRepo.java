package com.exam.repo;
import com.exam.model.Result;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ResultRepo extends JpaRepository<Result, Long> {
    List<Result> findByUserIdOrderBySubmittedAtDesc(Long userId);
    List<Result> findAllByOrderBySubmittedAtDesc();
    boolean existsByUserIdAndExamId(Long userId, Long examId);
}
