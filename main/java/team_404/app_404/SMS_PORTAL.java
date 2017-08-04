
package team_404.app_404;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import static team_404.app_404.Utility.prefix;

public class SMS_PORTAL extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    RadioGroup sms_type,via;
    EditText user_msg,old_no,new_no,token;
    TextView preview_msg,link;
    LinearLayout old_lo,new_lo,fast2sms_lo;
    CheckBox showtoken;
    Button preview_b,go_to_contacts;
    private final int MSG_LENGTH=145;

    @Override
    protected void onResume() {
        super.onResume();
        go_to_contacts.setText("Select Contacts..");
        go_to_contacts.setTextColor(Color.BLACK);
        go_to_contacts.setClickable(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms__portal);
        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        sms_type=(RadioGroup)findViewById(R.id.SMS_TYPE );
        sms_type.setOnCheckedChangeListener(this);
        via=(RadioGroup)findViewById(R.id.VIA );
        via.setOnCheckedChangeListener(this);

        user_msg=(EditText)findViewById(R.id.USER_MSG);
        old_no=(EditText)findViewById(R.id.old_phone_et);
        new_no=(EditText)findViewById(R.id.new_phone_et);
        token=(EditText)findViewById(R.id.TOKEN_ET );

        old_lo=(LinearLayout)findViewById(R.id.old_number_entry);
        new_lo=(LinearLayout)findViewById(R.id.new_number_entry);
        fast2sms_lo=(LinearLayout)findViewById(R.id.FAST2SMS_ENTRY);

        showtoken=(CheckBox)findViewById(R.id.SHOW_IT);
        showtoken.setOnCheckedChangeListener(this);

        preview_b=(Button)findViewById(R.id.PREVIEW_B);
        preview_b.setOnClickListener(this);
        go_to_contacts=(Button)findViewById(R.id.GO_TO_CONTACTS);
        go_to_contacts.setOnClickListener(this);

        preview_msg=(TextView)findViewById(R.id.PREVIEW_MSG_TV );
        link=(TextView)findViewById(R.id.LINK);
        link.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId)
        {
            case R.id.CUSTOM:
                old_lo.setVisibility(View.GONE);
                new_lo.setVisibility(View.GONE);
                user_msg.setHint("Enter your message here...");
                return;
            case R.id.SIM_CHANGE:
                old_lo.setVisibility(View.VISIBLE );
                new_lo.setVisibility(View.VISIBLE );
                user_msg.setHint("Enter your extra message here...");
                return;
            case R.id.ONLINE:
                fast2sms_lo.setVisibility(View.VISIBLE);
                link.setVisibility(View.VISIBLE);
                return;
            case R.id.OFFLINE :
                fast2sms_lo.setVisibility(View.GONE);
                link.setVisibility(View.GONE);
                return;
        }
    }

    @Override
    public void onClick(View v) {
        String msg=null;
        switch (v.getId())
        {
            case R.id.PREVIEW_B:
                msg=get_msg_format();
                if(msg.startsWith("!#"))preview_msg.setTextColor(Color.RED);
                else preview_msg.setTextColor(Color.BLUE);
                preview_msg.setText(get_msg_format());
                return;
            case R.id.LINK:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.fast2sms.com/"));
                startActivity(browserIntent);
                return;
            case R.id.GO_TO_CONTACTS:
                msg=get_msg_format();
                if(msg.startsWith("!#"))
                {
                    Toast.makeText(this,"PLEASE CHECK PREVIEW AND CONTINUE..",Toast.LENGTH_SHORT).show();
                    return;
                }
                go_to_contacts.setClickable(false);
                go_to_contacts.setTextColor(Color.RED);
                go_to_contacts.setText("Fetching Contacts...");
                Intent i=new Intent(this,SELECT_CONTACTS.class);
                i.putExtra("MSG",msg);
                if(via.getCheckedRadioButtonId()==R.id.ONLINE)
                {
                    i.putExtra("isOnline",true);
                    i.putExtra("token",token.getText().toString());
                }
                else
                    i.putExtra("isOnline",false);
                startActivity(i);
                return;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId())
        {
            case R.id.SHOW_IT:
                if(isChecked)
                    token.setInputType(InputType.TYPE_CLASS_TEXT);
                else
                    token.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                return;
        }
    }
    public String get_msg_format()
    {
        String result,msg = user_msg.getText().toString();;
        switch (sms_type.getCheckedRadioButtonId()) {
            case R.id.SIM_CHANGE:
                String old_number = old_no.getText().toString(),
                        new_number = new_no.getText().toString();

                if ((old_number == null) ||old_number.length()<10 || (new_number == null) ||(new_number.length() < 10))
                    return "!# ENTER VALID PHONE NUMBER";

                result = (prefix+" "+ old_number + " to_ " + new_number + " :" + msg);
                System.out.println(result);
                if (result.length() > MSG_LENGTH)
                    return "!# SMS LENGTH LIMIT EXCEED. LIMIT = 145";
                else return result;
            case R.id.CUSTOM:
                if (msg.length() > MSG_LENGTH)
                    return "!# SMS LENGTH LIMIT EXCEED. LIMIT = 145";
                else if(msg.length()<=0)
                    return "!# SMS LENGTH IS ZERO";
                else return msg;
        }

        return "!# ERROR!!";
    }
}
