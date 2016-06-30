package com.stone.shop.hbut.model;

/**
 * 单科成绩信息
 *
 * Created by stonekity.shi on 2015/4/6.
 */
public class SingleGrade {

    private String TaskNO;
    private String CourseName;
    private String CourseType;
    private Double CourseCredit;
    private Integer Grade;
    private Double GradePoint;
    private Integer CanModifyScore;
    private Boolean IsShowScore;
    private Boolean HasEvaludated;
    private Boolean IsCurrentSemester;


    public String getTaskNO() {
        return TaskNO;
    }

    public void setTaskNO(String taskNO) {
        this.TaskNO = taskNO;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String courseName) {
        this.CourseName = courseName;
    }

    public String getCourseType() {
        return CourseType;
    }

    public void setCourseType(String courseType) {
        this.CourseType = courseType;
    }

    public Double getCourseCredit() {
        return CourseCredit;
    }

    public void setCourseCredit(Double courseCredit) {
        this.CourseCredit = courseCredit;
    }

    public Integer getGrade() {
        return Grade;
    }

    public void setGrade(Integer grade) {
        this.Grade = grade;
    }

    public Double getGradePoint() {
        return GradePoint;
    }

    public void setGradePoint(Double gradePoint) {
        this.GradePoint = gradePoint;
    }

    public Integer getCanModifyScore() {
        return CanModifyScore;
    }

    public void setCanModifyScore(Integer canModifyScore) {
        this.CanModifyScore = canModifyScore;
    }

    public Boolean getIsShowScore() {
        return IsShowScore;
    }

    public void setIsShowScore(Boolean isShowScore) {
        this.IsShowScore = isShowScore;
    }

    public Boolean getHasEvaludated() {
        return HasEvaludated;
    }

    public void setHasEvaludated(Boolean hasEvaludated) {
        this.HasEvaludated = hasEvaludated;
    }

    public Boolean getIsCurrentSemester() {
        return IsCurrentSemester;
    }

    public void setIsCurrentSemester(Boolean isCurrentSemester) {
        this.IsCurrentSemester = isCurrentSemester;
    }
}
