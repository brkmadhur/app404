package team_404.app_404;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GAJAM VENU GOPAL on 1/28/2017.
 */

public class Text_CheckBox_Status_Adapter extends ArrayAdapter<Text_CheckBox_Status_Class>  {
    class  ViewHolder{
        TextView name;
        TextView from_no;
        TextView to_no;
        CheckBox isChecked;
        ImageView imageView;
    }
    private Context context;

    private ArrayList<Text_CheckBox_Status_Class> arr;

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        this.notifyDataSetChanged();
        ViewHolder viewHolder=null;
        if(convertView==null||convertView.getTag()==null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.text_checkbox_status_layout,null);
            viewHolder=new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.CONTACT_NAME_TV);
            viewHolder.from_no = (TextView) convertView.findViewById(R.id.FROM_TV);
            viewHolder.to_no = (TextView) convertView.findViewById(R.id.TO_TV);

            viewHolder.isChecked = (CheckBox) convertView.findViewById(R.id.isChecked);

            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.STATUS_IV);
            convertView.setTag(viewHolder);
        }
        ViewHolder holder=(ViewHolder)convertView.getTag();
        holder.name.setText(arr.get(position).getName());
        holder.from_no.setText(arr.get(position).getFrom_no());
        holder.to_no.setText(arr.get(position).getTo_no());
        holder.imageView.setImageResource(arr.get(position).getStatus());
        holder.isChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //int check_box_position=(int)buttonView.getTag();
                arr.get(position).setChecked(isChecked);
                buttonView.setChecked(arr.get(position).getIsChecked());
            }
        });

        holder.isChecked.setChecked(arr.get(position).getIsChecked());
        return convertView;
    }

    public Text_CheckBox_Status_Adapter(Context context, int resource, ArrayList<Text_CheckBox_Status_Class> objects) {
        super(context, resource, objects);
        this.context=context;
        this.arr=objects;
    }




}
