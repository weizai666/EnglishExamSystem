package com.example.activity.acticity_in_fragment1.words;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.activity.base.BaseAppCompatActivity;
import com.example.beans.Word;
import com.example.myapplication.R;
import com.example.utils.SqliteDBUtils;

import java.util.ArrayList;

/**
 * 这个类负责：单词练习
 *
 */
public class WordTestActivity extends BaseAppCompatActivity {

    // 单词本模块
    LinearLayout ll_words_book;
    // 显示单词总数
    TextView tv_words_all_number;
    // 显示 已学单词
    ImageView iv_study_words;

    //存储所有单词
    private ArrayList<Word> wordsList = new ArrayList<>();
    // 准备 传给 StudyWordsActivity 的单词数据
    private ArrayList<Word> preparingWords;

    boolean isInitData = false;//单词本是否初始化了

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            tv_words_all_number.setText(wordsList.size()+"");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_test);

        initView();
    }

    private void initView() {

        // 加载数据
        ll_words_book = findViewById(R.id.ll_words_book);
        ll_words_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isInitData){

                    //打开数据库输出流
                    SqliteDBUtils s = new SqliteDBUtils();
                    SQLiteDatabase db = s.openDatabase(getApplicationContext());
                    //查询数据库中rowid=1的数据
                    Cursor cursor = db.rawQuery("select * from cet4_words",null);
                    while(cursor.moveToNext()){
                        String word = cursor.getString(cursor.getColumnIndex("words"));
                        String phonetic = cursor.getString(cursor.getColumnIndex("phonetic"));
                        String chinese = cursor.getString(cursor.getColumnIndex("chinese"));
                        Word st = new Word(word,phonetic,chinese);
                        wordsList.add(st);
                    }

                    handler.sendEmptyMessage(0);
                    isInitData = true;
                }

              /*  new CustomDialog(getApplicationContext())
                        .builder()
                        .setTitle("清空消息列表后，聊天记录依然保留，确定要清空消息列表？")
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(false)
                        .addSheetItem("清空消息列表", CustomDialog.SheetItemColor.Red
                                , new CustomDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {

                                    }
                                }).show();*/

            }
        });

        tv_words_all_number = findViewById(R.id.tv_words_all_number);


        iv_study_words = findViewById(R.id.iv_study_words);
        iv_study_words.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (wordsList.size() == 0){
                    Toast.makeText(getBaseContext(),"请先选择词汇书", Toast.LENGTH_LONG).show();
                    return;
                }

                initPreparingWords(9);
                Intent intent = new Intent(WordTestActivity.this,StudyWordsActivity.class);
                intent.putExtra(StudyWordsActivity.WORDS,preparingWords);
                startActivity(intent);

                preparingWords.clear();
            }
        });

    }

    /**
     * 初始化数据：
     * preparingWords
     * 长度为length
     */
    private void initPreparingWords(int length){
   ///     System.arraycopy(wordsList,0,preparingWords,0,length);
        preparingWords = new ArrayList<>(length);
        preparingWords.addAll(wordsList.subList(0,length));
    }

}