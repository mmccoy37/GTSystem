package listViewAdapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hungdo.team44phase3.R;

import java.util.List;

import model.Apply;
import model.Course;
import model.Project;

/**
 * Created by hungdo on 12/2/16.
 */

public class CourseOrProjectAdapter extends BaseAdapter {
    public List<Object> list;
    Activity activity;

    public CourseOrProjectAdapter(Activity activity, List<Object> list) {
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

    private class ViewHolder {
        TextView txtFirst;
        TextView txtSecond;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater =  activity.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.list_pctype_adapter, null);
            holder = new ViewHolder();
            holder.txtFirst = (TextView) convertView.findViewById(R.id.name);
            holder.txtSecond = (TextView) convertView.findViewById(R.id.type);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        Object obj = list.get(position);
        if (obj.getClass() == Course.class) {
            holder.txtFirst.setText(((Course) obj).getName());
            holder.txtSecond.setText(((Course) obj).getType());
        } else {
            holder.txtFirst.setText(((Project) obj).getName());
            holder.txtSecond.setText(((Project) obj).getType());
        }

        return convertView;
    }
}
