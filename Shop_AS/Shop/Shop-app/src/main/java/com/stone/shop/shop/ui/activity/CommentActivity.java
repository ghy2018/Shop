package com.stone.shop.shop.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.stone.shop.R;
import com.stone.shop.base.ui.BaseActivity;
import com.stone.shop.main.manager.ACLManager;
import com.stone.shop.base.util.ToastUtils;
import com.stone.shop.base.widget.TitleBarView;

/**
 * Created by stone on 15/4/12.
 */
public class CommentActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "CommentActivity";

    private TitleBarView titleBarView;

    private EditText etContent;
    private CheckBox cbShowName;
    private TextView tvCount;
    private Button btnSubmit;

    private String countFormat = "";

    public static final String EXTRA_KAY_CONTENT = "content";
    public static final String EXTRA_KEY_IS_SHOW_NAME = "is_show_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        initView();
    }

    private void initView() {
        //initTitleBar();

        etContent = (EditText) findViewById(R.id.et_comment_content);
        cbShowName = (CheckBox) findViewById(R.id.cb_comment_show_name);
        tvCount = (TextView) findViewById(R.id.tv_comment_input_count);
        btnSubmit = (Button) findViewById(R.id.btn_comment_submit);

        countFormat = getResources().getString(R.string.format_input_text_count);

        etContent.addTextChangedListener(watcher);
        tvCount.setText(String.format(countFormat, 30));
        btnSubmit.setOnClickListener(this);
    }

    private void initTitleBar() {

        titleBarView = (TitleBarView) findViewById(R.id.titlebar);
        titleBarView.getLeftBtn().setVisibility(View.VISIBLE);
        titleBarView.getLeftBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void submit() {
        String content = etContent.getText().toString();
        if (content.length() < 30) {
            ToastUtils.showToast("文字不能少于30字");
            return;
        }

        if (cbShowName.isChecked() && !ACLManager.checkACL(ACLManager.ACL_COMMENT_HIDDEN_NAME)) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_KAY_CONTENT, content);
        intent.putExtra(EXTRA_KEY_IS_SHOW_NAME, cbShowName.isChecked());
        setResult(RESULT_OK, intent);
        finish();
    }


    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().length() < 30) {
                String countStr = String.format(countFormat, 30 - s.toString().length());
                tvCount.setText(countStr);
                tvCount.setVisibility(View.VISIBLE);
            } else {
                tvCount.setVisibility(View.GONE);
            }

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_comment_submit:
                submit();
                break;
            default:
                break;
        }
    }
}
