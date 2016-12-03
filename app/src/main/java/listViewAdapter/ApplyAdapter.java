package listViewAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hungdo.team44phase3.R;

import model.Apply;

/**
 * Created by hungdo on 12/2/16.
 */

public class ApplyAdapter extends BaseAdapter{
    public List<Apply> list;
    Activity activity;

    public ApplyAdapter(Activity activity, List<Apply> list) {
        super();
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder {
        TextView txtFirst;
        TextView txtSecond;
        TextView txtThird;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater =  activity.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.list_apply_adapter, null);
            holder = new ViewHolder();
            holder.txtFirst = (TextView) convertView.findViewById(R.id.date);
            holder.txtSecond = (TextView) convertView.findViewById(R.id.projectName);
            holder.txtThird = (TextView) convertView.findViewById(R.id.status);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        Apply apply = list.get(position);
        holder.txtFirst.setText(apply.getDate());
        holder.txtSecond.setText(apply.getProjectName());
        holder.txtThird.setText(apply.getStatus());
        return convertView;
    }
}
