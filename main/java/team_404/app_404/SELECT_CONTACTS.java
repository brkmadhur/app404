package team_404.app_404;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import static team_404.app_404.Utility.isIndianNumber;
import static team_404.app_404.Utility.phoneNumberTrim;

public class SELECT_CONTACTS extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    Bundle data;
    TextView msg,via,contacts_txt;
    Button send;
    ListView contacts_lv;
    Button refresh;
    ArrayList<Text_CheckBox_Status_Class>contact_list;
    HashSet<String> temp = new HashSet<String>();
    Text_CheckBox_Status_Adapter adapter;
    CheckBox select_all;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select__contacts);

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
    protected void onResume() {
        super.onResume();
        System.out.println("yo");
        msg=(TextView)findViewById(R.id.MSG_TV);
        via=(TextView)findViewById(R.id.VIA_TV);
        contacts_txt=(TextView)findViewById(R.id.Contacts_header) ;

        data=getIntent().getExtras();
        msg.setText(data.getString("MSG") );
        if(data.getBoolean("isOnline"))via.setText("Online");
        else via.setText("Offline");

        send=(Button)findViewById(R.id.SEND_B);
        send.setOnClickListener(this);
        refresh=(Button)findViewById(R.id.Refresh);
        refresh.setOnClickListener(this);

        select_all=(CheckBox)findViewById(R.id.SELECT_ALL_CONTACTS);
        select_all.setOnCheckedChangeListener(this);

        contacts_lv=(ListView)findViewById(R.id.CONTACTS_LV);
        contact_list=getAllContacts();
        contacts_txt.setText(contacts_txt.getText().toString()+"\n"+contact_list.size());

        adapter=new Text_CheckBox_Status_Adapter(this,R.layout.text_checkbox_status_layout,contact_list);
        contacts_lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    ArrayList<Text_CheckBox_Status_Class> getAllContacts()
    {
        ArrayList<Text_CheckBox_Status_Class> result=new ArrayList<Text_CheckBox_Status_Class>();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                System.out.print("+");
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));

                        String to = phoneNumberTrim(phoneNo);
                        if(temp.contains(to)) ;
                        else{
                            String s = name + "|" + to+"|HOME";
                            System.out.println(s);
                            temp.add(to);
                            result.add(new Text_CheckBox_Status_Class(s));
                        }
                    }
                    pCur.close();
                }
            }
        }
        cur.close();
        Collections.sort(result, new Comparator<Text_CheckBox_Status_Class>() {
            @Override
            public int compare(Text_CheckBox_Status_Class o1, Text_CheckBox_Status_Class o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return result;
    }
    public String  Remove_Spcae(String str)
    {

        if(str.startsWith("+91"))
        {
            str=str.substring(3);
        }
        StringTokenizer st = new StringTokenizer(str," ");
        String madh = new String();

        while (st.hasMoreTokens()) {
            //System.out.println(st.nextToken());
            madh+=st.nextToken();
        }
        StringTokenizer st2 = new StringTokenizer(madh,"-");
        String madh2 = new String();

        while (st2.hasMoreTokens()) {
            //System.out.println(st.nextToken());
            madh2+=st2.nextToken();
        }
        return  madh2;
    }
    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.Refresh:
                for(int i=0;i<contact_list.size();i++)
                    contact_list.get(i).setChecked(false);
                adapter.notifyDataSetChanged();
                select_all.setChecked(false);
                return;
            case R.id.SEND_B:
                /*for(int i=0;i<contact_list.size();i++)
                {
                    if(contact_list.get(i).getFrom_no().compareTo("7032394104")==0||
                            contact_list.get(i).getFrom_no().compareTo("8331948680")==0||
                            contact_list.get(i).getFrom_no().compareTo("9493397622")==0)
                        contact_list.get(i).setChecked(true);
                    adapter.notifyDataSetChanged();
                }*/

                ArrayList<Text_CheckBox_Status_Class> selected_list=new ArrayList<Text_CheckBox_Status_Class>();
                for(int i=0;i<contact_list.size();i++)
                    if(contact_list.get(i).getIsChecked())
                        selected_list.add(contact_list.get(i));
                if(selected_list.size()<=0) Toast.makeText(this,"Select atleast one contact..",Toast.LENGTH_LONG).show();
                adapter=new Text_CheckBox_Status_Adapter(this,R.layout.text_checkbox_status_layout,selected_list);
                contacts_lv.setAdapter(adapter);
                if(data.getBoolean("isOnline"))
                {
                    fast2sms_API gate=new fast2sms_API(data.getString("token"));
                    System.out.println(data.get("token"));
                    int cnt=2;
                    for(int i=0;i<selected_list.size()&&cnt>0;i++)
                    {
                        if(selected_list.get(i).getIsChecked())
                        {
                            String mob=isIndianNumber(selected_list.get(i).getFrom_no());
                            if(mob.startsWith("!#"))
                            {
                                selected_list.get(i).setStatus(R.mipmap.reject);
                                Toast.makeText(this,mob,Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else
                            {
                                String response=gate.sendSMS(mob,msg.getText().toString());
                                System.out.println(response);
                                if(response.contains("accept"))selected_list.get(i).setStatus(R.mipmap.accept);
                                else selected_list.get(i).setStatus(R.mipmap.reject);
                                Toast.makeText(this,response,Toast.LENGTH_SHORT).show();
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
                else
                {
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    smsIntent.setData(Uri.parse("smsto:"));
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    String addresses="";
                    for(int i=0;i<selected_list.size();i++)
                    {
                        if(selected_list.get(i).getIsChecked())
                        {
                            String mob=isIndianNumber(selected_list.get(i).getFrom_no());
                            if(mob.startsWith("!#"))
                            Toast.makeText(this, mob, Toast.LENGTH_SHORT).show();
                            addresses+=mob+";";
                        }
                    }
                    addresses=addresses.substring(0,addresses.length()-2);
                    System.out.println(addresses);
                    smsIntent.putExtra("address"  , addresses);
                    smsIntent.putExtra("sms_body"  , msg.getText().toString());
                    startActivity(smsIntent);
                }
                return;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId())
        {
            case R.id.SELECT_ALL_CONTACTS:
                for(int i=0;i<contact_list.size();i++)
                    contact_list.get(i).setChecked(isChecked);
                adapter.notifyDataSetChanged();
                return;
        }
    }
}
