package team_404.app_404;


import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.util.StringTokenizer;

/**
 * Created by GAJAM VENU GOPAL on 1/28/2017.
 */

public class Text_CheckBox_Status_Class  {
    private String info;
    private String name;
    private String from_no;
    private String to_no;
    private boolean isChecked;
    private int status;

    public Text_CheckBox_Status_Class(String info) {
        this.info = info;
        StringTokenizer st=new StringTokenizer(info,"|");
        System.out.println(st.countTokens());
        name=st.nextToken();
        from_no=st.nextToken();
        to_no=st.nextToken();
        this.isChecked=false;
        this.status= R.mipmap.unknown;
    }


    public String getName() {
        return name;
    }

    public String getFrom_no() {
        return from_no;
    }

    public String getTo_no() {
        return to_no;
    }

    public boolean getIsChecked() {
        return isChecked;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    @Override
    public String toString() {
        return info+" "+isChecked+" "+status;
    }



}
