package com.ocms.repository;
import com.ocms.entity.Review;
import com.ocms.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByCourse(Course course);
}
