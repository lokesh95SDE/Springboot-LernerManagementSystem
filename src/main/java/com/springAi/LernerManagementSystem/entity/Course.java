package com.springAi.LernerManagementSystem.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long courseId;
    private String courseName;
    private String courseDescription;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<Cohort> cohorts = new ArrayList<>();

    public Course() {

    }

    public Course(Long courseId, String courseName, String courseDescription, List<Cohort> cohorts) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.cohorts = cohorts;
    }

    public List<Cohort> getCohorts() {
        return cohorts;
    }

    public void setCohorts(List<Cohort> cohorts) {
        this.cohorts = cohorts;
    }


    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }
}
