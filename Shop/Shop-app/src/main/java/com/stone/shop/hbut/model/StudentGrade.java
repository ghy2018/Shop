package com.stone.shop.hbut.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 学生所有成绩信息
 *
 * Created by stonekity.shi on 2015/4/6.
 */
public class StudentGrade {

    private Double AverageGradePoint;
    private Double TotalGradePoint;
    private String Title;
    private String Name;
    private Boolean IsViewGradeAfterEvaluating;
    private List<SingleGrade> StuGradeList;

    private transient Map<String, List<SingleGrade>> stuGradeListBySemester;


    public Double getAverageGradePoint() {
        return AverageGradePoint;
    }

    public void setAverageGradePoint(Double averageGradePoint) {
        this.AverageGradePoint = averageGradePoint;
    }

    public Double getTotalGradePoint() {
        return TotalGradePoint;
    }

    public void setTotalGradePoint(Double totalGradePoint) {
        this.TotalGradePoint = totalGradePoint;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public Boolean getIsViewGradeAfterEvaluating() {
        return IsViewGradeAfterEvaluating;
    }

    public void setIsViewGradeAfterEvaluating(Boolean isViewGradeAfterEvaluating) {
        this.IsViewGradeAfterEvaluating = isViewGradeAfterEvaluating;
    }

    public List<SingleGrade> getStuGradeList() {
        if(StuGradeList == null) {
            StuGradeList = new ArrayList<SingleGrade>();
        }
        return StuGradeList;
    }

    public void setStuGradeList(List<SingleGrade> stuGradeList) {
        this.StuGradeList = stuGradeList;
    }

    public Map<String, List<SingleGrade>> getStuGradeListBySemester() {
        return stuGradeListBySemester;
    }

    public void setStuGradeListBySemester(Map<String, List<SingleGrade>> stuGradeListBySemester) {
        this.stuGradeListBySemester = stuGradeListBySemester;
    }
}
