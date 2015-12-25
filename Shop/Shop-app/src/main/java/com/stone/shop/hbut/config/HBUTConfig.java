package com.stone.shop.hbut.config;

/**
 * Created by stonekity.shi on 2015/4/6.
 */
public class HBUTConfig {


    //*********************************************JSON

    //学校配置
    public static final String URL_HBUT_DOMAIN = "http://202.114.176.103";

    //登录[http://202.114.176.103/Account/LogOnForJson?Mobile=1&UserName=1110321110&Password=scj201214&Role=Student]
    public static final String URL_HBUT_LOGIN = URL_HBUT_DOMAIN + "/Account/LogOnForJson";

    //退出
    public static final String URL_HBUT_LOGOUT = URL_HBUT_DOMAIN + "/Account/LogOffForJson/?Mobile=1";

    //获取当前学期[http://202.114.176.103/common/CurrentSemesterForJson/?Mobile=1]
    public static final String URL_HBUT_CUR_SEMESTER = URL_HBUT_DOMAIN + "/common/CurrentSemesterForJson/?Mobile=1";

    //获取最近学期的成绩[http://202.114.176.103/StuGrade/IndexRecentSemesterForJson/?id=1110321110&Mobile=1]
    public static final String URL_HBUT_CUR_SEMESTER_GRADE = URL_HBUT_DOMAIN + "/StuGrade/IndexRecentSemesterForJson";

    //获取当前学期课表
    public static final String URL_HBUT_CUR_SEMESTER_SCHDULE = URL_HBUT_DOMAIN + "/ArrangeTask/MyselfScheduleForJson";

    //获取所有成绩[http://202.114.176.103/StuGrade/IndexAllSemesterForJson/?id=1110321110&Mobile=1]
    public static final String URL_HBUT_ALL_SEMESTER_GRADE = URL_HBUT_DOMAIN + "/StuGrade/IndexAllSemesterForJson";


    public static String urlLogin(String username, String password) {
        StringBuilder url = new StringBuilder();
        url.append(URL_HBUT_LOGIN);
        url.append("?");
        url.append("Username=" + username);
        url.append("&Password=" + password);
        url.append("&Role=Student");
        return url.toString();
    }

    public static String urlLogout() {
        return URL_HBUT_LOGOUT;
    }

    public static String urlCurSemester() {
        return URL_HBUT_CUR_SEMESTER;
    }

    public static String urlStuAllGrade(String stuID) {
        StringBuilder url = new StringBuilder();
        url.append(URL_HBUT_ALL_SEMESTER_GRADE);
        url.append("?");
        url.append("id=" + stuID);
        url.append("&Mobile=1");
        return url.toString();
    }

    public static String urlStuCurSchdule(String stuID) {
        StringBuilder url = new StringBuilder();
        url.append(URL_HBUT_CUR_SEMESTER_SCHDULE);
        url.append("?");
        url.append("id=" + stuID);
        url.append("Mobile=1");
        return url.toString();
    }

    //*********************************************JSON






    //*********************************************HTML

    public static final String URL_HBUT_HTML_GRADE = URL_HBUT_DOMAIN + "/StuGrade/Index";

    //*********************************************HTML

}
