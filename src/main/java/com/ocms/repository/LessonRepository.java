package com.ocms.repository;
import com.ocms.entity.Lesson;
import com.ocms.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCourse(Course course);
}
