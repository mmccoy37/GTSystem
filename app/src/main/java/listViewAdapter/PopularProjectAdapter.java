package listViewAdapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hungdo.team44phase3.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.Course;
import model.Project;

/**
 * Created by hungdo on 12/3/16.
 */

public class PopularProjectAdapter extends BaseAdapter {
    public List<String> listName;
    public List<Integer> listNumApplicant;
    Activity activity;

    public PopularProjectAdapter(Activity activity, Map<String, Integer> map) {
        super();
        this.activity = activity;
        listName = new ArrayList<>();
        listNumApplicant = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, Integer> e: map.entrySet()) {
            if (i < 10) {
                listName.add(e.getKey());
                listNumApplicant.add(e.getValue());
                i++;
            }
            if (i == 10) {
                break;
            }
        }
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return listName.get(position);
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
            convertView = inflater.inflate(R.layout.list_popularapp_adapter, null);
            holder = new ViewHolder();
            holder.txtFirst = (TextView) convertView.findViewById(R.id.PName);
            holder.txtSecond = (TextView) convertView.findViewById(R.id.numApplicant);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        String name = listName.get(position);
        Integer num = listNumApplicant.get(position);
        holder.txtFirst.setText(name);
        holder.txtSecond.setText("" + num);


        return convertView;
    }
}
