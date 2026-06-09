package com.springAi.LernerManagementSystem.service;

import com.springAi.LernerManagementSystem.dto.CourseDto;
import com.springAi.LernerManagementSystem.entity.Cohort;
import com.springAi.LernerManagementSystem.entity.Course;
import com.springAi.LernerManagementSystem.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public CourseDto createCourse(CourseDto courseDto) {
        Course course = convertToEntity(courseDto);
        Course savedCourse = courseRepository.save(course);
        return convertToDto(savedCourse);
    }

    @Transactional(readOnly = true)
    public List<CourseDto> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    private Course convertToEntity(CourseDto courseDto) {
        return new Course(
                null,
                courseDto.courseName(),
                courseDto.courseDescription(),
                List.of()
        );
    }

    private CourseDto convertToDto(Course course) {
        List<Long> cohortIds = course.getCohorts() == null
                ? List.of()
                : course.getCohorts()
                        .stream()
                        .map(Cohort::getCohortId)
                        .toList();

        return new CourseDto(
                course.getCourseId(),
                course.getCourseName(),
                course.getCourseDescription(),
                cohortIds
        );
    }
}
