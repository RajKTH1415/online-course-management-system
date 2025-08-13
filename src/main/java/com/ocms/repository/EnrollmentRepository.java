package com.ocms.repository;
import com.ocms.entity.Enrollment;
import com.ocms.entity.Course;
import com.ocms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByStudentAndCourse(User student, Course course);
    List<Enrollment> findByCourse(Course course);
}
