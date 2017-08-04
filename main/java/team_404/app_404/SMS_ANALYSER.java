package team_404.app_404;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;


import java.util.ArrayList;

import static team_404.app_404.Utility.phoneNumberTrim;
import static team_404.app_404.Utility.prefix;


public class SMS_ANALYSER extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private ListView sms_lv;
    private Cursor c;
    private ArrayList<Text_CheckBox_Status_Class> info;
    private ArrayList<Text_CheckBox_Status_Class> selected_info;
    private Uri sms_db;
    private CheckBox select_all;
    private Button Update;
    Text_CheckBox_Status_Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms__analyser);






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
        init();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        init();
    }

    String nameof(String number)
    {
        ContentResolver cr = getContentResolver();

        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        String contactName = null;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
            System.out.println(contactName);
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return contactName;
    }
    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.UPDATE_B:
                selected_info=new ArrayList<Text_CheckBox_Status_Class>();
                for (int i = 0; i < info.size(); i++){
                    //System.out.println(info.get(i));
                    if(info.get(i).getIsChecked())selected_info.add(info.get(i));
                }

                adapter=new Text_CheckBox_Status_Adapter(this,R.layout.text_checkbox_status_layout,selected_info);
                sms_lv.setAdapter(adapter);
                for(int i=0;i<selected_info.size();i++) {
                    String old_number =selected_info.get(i).getFrom_no() ;
                    String new_number=selected_info.get(i).getTo_no();
                    String name=selected_info.get(i).getName();

                    if (selected_info.get(i).getName() == null) {
                        /*try {
                            createContact(new_number, new_number);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        } catch (OperationApplicationException e) {
                            e.printStackTrace();
                        }*/
                        selected_info.get(i).setStatus(R.mipmap.reject);
                    }
                    else {
                        try {
                            updateContact(name, new_number, old_number);
                            selected_info.get(i).setStatus(R.mipmap.accept);
                        } catch (Exception e) {
                            e.printStackTrace();
                            selected_info.get(i).setStatus(R.mipmap.reject);
                        }
                    }
                }
                return;
        }
    }

    void init()
    {
        sms_lv = (ListView) findViewById(R.id.SMS_LV);
        select_all=(CheckBox)findViewById(R.id.SELECT_ALL);
        select_all.setOnCheckedChangeListener(this);

        Update=(Button)findViewById(R.id.UPDATE_B);
        Update.setOnClickListener(this);

        info = new ArrayList<>();
        sms_db = Uri.parse("content://sms/inbox");
        c = getContentResolver().query(sms_db, null, null, null, null);
        startManagingCursor(c);
        if(c.moveToFirst()) {
            for(int i=0; i < c.getCount(); i++) {
                String body = analyse_sms(c.getString(c.getColumnIndexOrThrow("body")).toString());
                System.out.print(body);
                if(body.compareTo("!#")!=0)info.add(new Text_CheckBox_Status_Class(body));
                //info.add(new Text_CheckBox_Status_Class("NAME|7032394104|8331948680"));
                c.moveToNext();
            }
        }
        c.close();
        adapter=new Text_CheckBox_Status_Adapter(this,R.layout.text_checkbox_status_layout,info);
        sms_lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }



    private void updateContact(String name, String new_number, String old_number) throws RemoteException, OperationApplicationException {
        ContentResolver cr = getContentResolver();
        System.out.println(name+ " "+new_number+" "+old_number);
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(old_number));
        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.PhoneLookup._ID};
        Cursor cursor =
                cr.query(
                        uri,
                        projection,
                        null,
                        null,
                        null);
        int type = 0;
        while (cursor.moveToNext()) {
            String name1 = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME));
            System.out.println(name1);
            if (name1.equals(name)) {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
                Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                while (pCur.moveToNext()) {
                    String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    System.out.println(phoneNumberTrim(phoneNo) + " " + old_number);
                    if (phoneNumberTrim(phoneNo).equals(old_number)) {
                        System.out.println((phoneNo) + " " + old_number);
                        break;
                    }
                }
                type = pCur.getInt(pCur.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.TYPE));
                System.out.println(type);
                cursor.close();
                pCur.close();
                break;
            }
        }
            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            String where = ContactsContract.Data.DISPLAY_NAME + " = ? AND " +
                    ContactsContract.Data.MIMETYPE + " = ? AND " +
                    String.valueOf(ContactsContract.CommonDataKinds.Phone.TYPE) + " = ? ";
            System.out.println(where);
            String[] params = new String[]{name,
                    ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                    String.valueOf(type)};
            System.out.println(params[0]);
           // Cursor phoneCur = managedQuery(ContactsContract.Data.CONTENT_URI, null, where, params, null);
            ops.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                    .withSelection(where, params)
                    .withValue(ContactsContract.CommonDataKinds.Phone.DATA,new_number)
                    .build());
                cr.applyBatch(ContactsContract.AUTHORITY, ops);

    }
    private String analyse_sms(String msg)
    {
        String result;
        String[] msg_arr=msg.split(" ");
        for(int i=0;i<msg_arr.length;i++)
            System.out.print("<"+msg_arr[i]+","+phoneNumberTrim(msg_arr[i])+">");
        System.out.println(msg_arr.length);

        if(msg_arr.length<=1)result="!#";
        else if(msg_arr[0].contains(prefix))
            result=nameof(phoneNumberTrim(msg_arr[1]))+"|"+phoneNumberTrim(msg_arr[1])+"|"+phoneNumberTrim(msg_arr[3]);
        else if(msg_arr[1].contains(prefix))
            result=nameof(phoneNumberTrim(msg_arr[2]))+"|"+phoneNumberTrim(msg_arr[2])+"|"+phoneNumberTrim(msg_arr[4]);
        else result="!#";
        System.out.println(result);
        return result;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("Here:"+parent+" "+view+" "+position+" "+id);
    }



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch ((buttonView.getId()))
        {
            case R.id.SELECT_ALL:
                for(int i=0;i<info.size();i++)
                    info.get(i).setChecked(isChecked);
                adapter.notifyDataSetChanged();
                return;
        }
    }
}
