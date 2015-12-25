package com.stone.shop.hbut.model;

/**
 * Created by stonekity.shi on 2014/12/29.
 *
 * 课程信息
 */
public class SingleSchedule {

    private String Classes;
    private String CurName;         //课程名称
    private Integer Day;            //星期
    private String DayStr;          //文字描述
    private Integer DayTime;        //节数
    private String DayTimeStr;      //文字描述
    private String Place;           //上课地点
    private Integer TaskTimeType;   //类型(必修课、选修、实习)
    private String Teacher;         //任课教师
    private String Week;            //周次


    //   "Classes": null,
    //   "CurName": "嵌入式系统设计与开发",
    //   "Day": 1,
    //   "DayStr": null,
    //   "DayTime": 1,
    //   "DayTimeStr": null,
    //   "Place": "2-414",
    //   "TaskTimeType": 1,
    //   "Teacher": "徐承志-主讲",
    //   "Week": "第1-10周 "


    public String getClasses() {
        return Classes;
    }

    public void setClasses(String classes) {
        this.Classes = classes;
    }

    public String getCurName() {
        return CurName;
    }

    public void setCurName(String curName) {
        this.CurName = curName;
    }

    public Integer getDay() {
        return Day;
    }

    public void setDay(Integer day) {
        this.Day = day;
    }

    public String getDayStr() {
        return DayStr;
    }

    public void setDayStr(String dayStr) {
        this.DayStr = dayStr;
    }

    public Integer getDayTime() {
        return DayTime;
    }

    public void setDayTime(Integer dayTime) {
        this.DayTime = dayTime;
    }

    public String getDayTimeStr() {
        return DayTimeStr;
    }

    public void setDayTimeStr(String dayTimeStr) {
        this.DayTimeStr = dayTimeStr;
    }

    public String getWeek() {
        return Week;
    }

    public void setWeek(String week) {
        this.Week = week;
    }

    public String getTeacher() {
        return Teacher;
    }

    public void setTeacher(String teacher) {
        this.Teacher = teacher;
    }

    public Integer getTaskTimeType() {
        return TaskTimeType;
    }

    public void setTaskTimeType(Integer taskTimeType) {
        this.TaskTimeType = taskTimeType;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        this.Place = place;
    }

}
