package com.springAi.LernerManagementSystem.dto;

import java.util.List;

public record CourseDto(
        Long courseId,
        String courseName,
        String courseDescription,
        List<Long> cohortIds
) {
}
