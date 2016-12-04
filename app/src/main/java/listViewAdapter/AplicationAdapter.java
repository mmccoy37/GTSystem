package listViewAdapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hungdo.team44phase3.R;

import java.util.List;

import database.DatabaseAccess;
import model.Apply;
import model.User;

/**
 * Created by hungdo on 12/3/16.
 */

public class AplicationAdapter extends BaseAdapter {
    public List<Apply> list;
    Activity activity;
    private DatabaseAccess data;

    public AplicationAdapter(Activity activity, List<Apply> list) {
        super();
        this.activity = activity;
        this.list = list;
        data = DatabaseAccess.getDatabaseAccess();
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
        TextView txtFourth;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater =  activity.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.list_application_adapter, null);
            holder = new ViewHolder();
            holder.txtFirst = (TextView) convertView.findViewById(R.id.PName);
            holder.txtSecond = (TextView) convertView.findViewById(R.id.AMajor);
            holder.txtThird = (TextView) convertView.findViewById(R.id.AYear);
            holder.txtFourth = (TextView) convertView.findViewById(R.id.status);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        Apply apply = list.get(position);
        Log.i("SQL", apply.getEmail());
        User u = data.getUserByEmail(apply.getEmail());


        holder.txtFirst.setText(apply.getProjectName());
        holder.txtSecond.setText(u.getMajor());
        holder.txtThird.setText(u.getYearString());
        holder.txtFourth.setText(apply.getStatus());
        return convertView;
    }
}
