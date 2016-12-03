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
import model.Report;


/**
 * Created by hungdo on 12/3/16.
 */

public class ReportAdapter extends BaseAdapter {
    public List<Report> list;
    Activity activity;

    public ReportAdapter(Activity activity, List<Report> list) {
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
        TextView txtFourth;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater =  activity.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.list_reports_adapter, null);
            holder = new ViewHolder();
            holder.txtFirst = (TextView) convertView.findViewById(R.id.PName);
            holder.txtSecond = (TextView) convertView.findViewById(R.id.numApplication);
            holder.txtThird = (TextView) convertView.findViewById(R.id.rate);
            holder.txtFourth = (TextView) convertView.findViewById(R.id.topMajors);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        Report report = list.get(position);



        holder.txtFirst.setText(report.getProjectName());
        holder.txtSecond.setText("" + report.getNumApplicant());
        holder.txtThird.setText(String.format("%.2f ", report.getRate()));
        List<String> majors = report.getMajors();
        String topMajor = majors.get(0);
        for (int i = 1; i < majors.size(); i++) {
            topMajor += "\n" + majors.get(i);
        }
        holder.txtFourth.setText(topMajor);
        return convertView;
    }
}
