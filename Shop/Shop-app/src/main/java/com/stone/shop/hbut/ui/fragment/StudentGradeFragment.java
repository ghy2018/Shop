package com.stone.shop.hbut.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.stone.shop.R;
import com.stone.shop.base.config.BmobConfig;
import com.stone.shop.base.ui.BaseFragment;
import com.stone.shop.hbut.HBUTManager;
import com.stone.shop.hbut.model.SingleGrade;
import com.stone.shop.hbut.model.StudentGrade;
import com.stone.shop.base.util.ToastUtils;

import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * 学生成绩
 * <p/>
 * Created by stonekity.shi on 2015/4/9.
 */
public class StudentGradeFragment extends BaseFragment {

    private static final String TAG = "StudentGradeFragment";

    private ExpandableStickyListHeadersListView lvStuGrades;
    private GradeListAdapter mGradeListAdapter;
    private StudentGrade studentGrade;

    private AQuery aq;

    public StudentGradeFragment() {
        super();
    }


    @Override
    protected int provideLayoutResId() {
        return R.layout.fragment_stu_grade;
    }

    @Override
    protected void initView(View rootView) {
        aq = new AQuery(getActivity());
        lvStuGrades = (ExpandableStickyListHeadersListView) rootView.findViewById(R.id.lv_stu_grade);
    }

    @Override
    protected void initListener() {

    }

    public void refreshData() {
        if(null != mGradeListAdapter) {
            studentGrade = HBUTManager.getInstance().getStudentGrade();
            ToastUtils.showToast("同步成功");
            mGradeListAdapter.notifyDataSetChanged();
        }
    }


    @Override
    protected void initData(View rootView, Bundle savedInstanceState) {
        studentGrade = HBUTManager.getInstance().getStudentGrade();

        if (null != studentGrade && BmobConfig.DEBUG) {
            ToastUtils.showToast(studentGrade.getStuGradeList().size() + "");
        }

        aq.id(R.id.tv_stu_grade_title).text(studentGrade.getTitle());
        aq.id(R.id.tv_stu_grade_avg_point).text(studentGrade.getAverageGradePoint()+"");
        aq.id(R.id.tv_stu_grade_total_point).text(studentGrade.getTotalGradePoint()+"");

        mGradeListAdapter = new GradeListAdapter(getActivity());
        lvStuGrades.setAdapter(mGradeListAdapter);
        lvStuGrades.setOnHeaderClickListener(new StickyListHeadersListView.OnHeaderClickListener() {
            @Override
            public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
                if(lvStuGrades.isHeaderCollapsed(headerId)){
                    lvStuGrades.expand(headerId);
                }else {
                    lvStuGrades.collapse(headerId);
                }
            }
        });
    }


    public class GradeListAdapter extends BaseAdapter implements StickyListHeadersAdapter {


        private Context mContext;
        private LayoutInflater mInflater = null;

        public GradeListAdapter(Context context) {
            this.mContext = context;
            mInflater = LayoutInflater.from(context);
        }


        @Override
        public int getCount() {
            if (studentGrade != null)
                return studentGrade.getStuGradeList().size();
            return 0;
        }


        @Override
        public Object getItem(int position) {
            if (studentGrade != null)
                return studentGrade.getStuGradeList().get(position);

            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getHeaderView(int i, View convertView, ViewGroup parent) {
            SemesterHolder holder;
            if (convertView == null) {
                holder = new SemesterHolder();
                convertView = mInflater.inflate(R.layout.list_item_stu_grade_semester, parent, false);
                holder.tvSemester = (TextView) convertView.findViewById(R.id.tv_stu_grade_task_semester);
                convertView.setTag(holder);
            } else {
                holder = (SemesterHolder) convertView.getTag();
            }
            String semester = getHeaderId(i) + "";
            String headerStr = semester.substring(0, 4) + " 学年 第 " + semester.substring(4, 5)+" 学期";
            holder.tvSemester.setText(headerStr);
            return convertView;
        }


        @Override
        public long getHeaderId(int i) {
            Long semester = Long.parseLong(studentGrade.getStuGradeList().get(i).getTaskNO().substring(0, 5));
            return semester;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GradeHolder gradeHolder = new GradeHolder();
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item_stu_grade, null);
                gradeHolder.tvTaskName = (TextView) convertView.findViewById(R.id.tv_stu_grade_task_name);
                gradeHolder.tvGradeCredit = (TextView) convertView.findViewById(R.id.tv_stu_grade_credit);
                gradeHolder.tvGradePoint = (TextView) convertView.findViewById(R.id.tv_stu_grade_grade_point);
                gradeHolder.tvGrade = (TextView) convertView.findViewById(R.id.tv_stu_grade_grade);
                convertView.setTag(gradeHolder);
            } else {
                gradeHolder = (GradeHolder) convertView.getTag();
            }
            SingleGrade grade = studentGrade.getStuGradeList().get(position);
            gradeHolder.tvTaskName.setText(grade.getCourseName());
            gradeHolder.tvGradeCredit.setText(grade.getCourseCredit() + "");
            gradeHolder.tvGradePoint.setText(grade.getGradePoint() + "");
            gradeHolder.tvGrade.setText(grade.getGrade() + "");
            return convertView;
        }


    }


    public class GradeHolder {
        public TextView tvTaskName; //课程名称
        public TextView tvGradeCredit; //学分
        public TextView tvGradePoint; //学分绩点
        public TextView tvGrade;  //分数
    }

    public class SemesterHolder {
        public TextView tvSemester;  //学期
    }


}
