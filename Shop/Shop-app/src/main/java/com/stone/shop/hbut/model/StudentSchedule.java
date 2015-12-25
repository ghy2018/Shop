package com.stone.shop.hbut.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 学生课表
 *
 * Created by stonekity.shi on 2015/4/6.
 */
public class StudentSchedule {

    private String Title;
    private String Content;
    private Boolean IsTeacher;
    private List<SingleSchedule> TimeScheduleList;


    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        this.Content = content;
    }

    public Boolean getIsTeacher() {
        return IsTeacher;
    }

    public void setIsTeacher(Boolean isTeacher) {
        this.IsTeacher = isTeacher;
    }

    public List<SingleSchedule> getTimeScheduleList() {
        if(TimeScheduleList == null) {
            TimeScheduleList = new ArrayList<>();
        }
        return TimeScheduleList;
    }

    public void setTimeScheduleList(List<SingleSchedule> timeScheduleList) {
        this.TimeScheduleList = timeScheduleList;
    }
}
