package yeeaoo.frist2016_06_15;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AnotherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another);
        Person person = getIntent().getParcelableExtra("person");
        TextView tv = (TextView) findViewById(R.id.textperson);
        tv.setText("姓名 : "+person.getName()+"\n"+"年龄 ："+person.getAge() );

    }
}
