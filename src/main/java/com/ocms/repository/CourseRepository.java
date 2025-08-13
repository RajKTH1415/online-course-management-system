package com.ocms.repository;
import com.ocms.entity.Course;
import com.ocms.enums.CourseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByStatus(CourseStatus status);
}
