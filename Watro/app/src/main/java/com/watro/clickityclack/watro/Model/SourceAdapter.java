package com.watro.clickityclack.watro.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.watro.clickityclack.watro.R;

import java.util.ArrayList;

/**
 * Created by Uche Nkadi on 3/28/2017.
 * This class holds information that is used in the list view for water source reports.
 */

public class SourceAdapter extends ArrayAdapter<SourceModel> {

    public SourceAdapter(ArrayList<SourceModel> data, Context context) {
        super(context, R.layout.single_source_report_view, data);
        ArrayList<SourceModel> sourceList = data;
        Context mContext = context;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView txtDate;
        TextView txtReportId;
        TextView txtReporterName;
        TextView txtLocation;
        TextView txtWaterType;
        TextView txtWaterCondition;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        SourceModel sourceModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.single_source_report_view, parent, false);
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.dateTextView);
            viewHolder.txtReportId = (TextView) convertView.findViewById(R.id.reportIdTextView);
            viewHolder.txtReporterName = (TextView) convertView.findViewById(R.id.reporterNameTextView);
            viewHolder.txtLocation = (TextView) convertView.findViewById(R.id.locationTextView);
            viewHolder.txtWaterType = (TextView) convertView.findViewById(R.id.waterTypeTextView);
            viewHolder.txtWaterCondition = (TextView) convertView.findViewById(R.id.waterConditionTextView);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }
        //using placeholder string because it is bad practice to concatenate strings inside of setText
        String placeholder = "Date: " + sourceModel.getDate();
        viewHolder.txtDate.setText("Date: " + sourceModel.getDate());
        viewHolder.txtDate.setText(placeholder);
        placeholder = "Report ID: " + sourceModel.getReportId();
        viewHolder.txtReportId.setText(placeholder);
        placeholder = "Reporter Name: " + sourceModel.getReporterName();
        viewHolder.txtReporterName.setText(placeholder);
        placeholder = "Location: " + sourceModel.getLocation();
        viewHolder.txtLocation.setText(placeholder);
        placeholder = "Water Type: " + sourceModel.getWaterType();
        viewHolder.txtWaterType.setText(placeholder);
        placeholder = "Water Condition: " + sourceModel.getWaterCondition();
        viewHolder.txtWaterCondition.setText(placeholder);
        // Return the completed view to render on screen
        return convertView;
    }
}
