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
 * This class holds information that is used in the list view for purity reports.
 */

public class PurityAdapter extends ArrayAdapter<PurityModel> {
    public PurityAdapter(ArrayList<PurityModel> data, Context context) {
        super(context, R.layout.single_purity_report_view, data);
        Context mContext = context;
    }
    // View lookup cache
    private static class ViewHolder {
        TextView txtDate;
        TextView txtReportId;
        TextView txtReporterName;
        TextView txtLocation;
        TextView txtOverallCondition;
        TextView txtVirusPPM;
        TextView txtContaminantPPM;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        PurityModel pureModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        PurityAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new PurityAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.single_purity_report_view, parent, false);
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.dateTextView);
            viewHolder.txtReportId = (TextView) convertView.findViewById(R.id.reportIdTextView);
            viewHolder.txtReporterName = (TextView) convertView.findViewById(R.id.reporterNameTextView);
            viewHolder.txtLocation = (TextView) convertView.findViewById(R.id.locationTextView);
            viewHolder.txtOverallCondition = (TextView) convertView.findViewById(R.id.overallConditionTextView);
            viewHolder.txtVirusPPM = (TextView) convertView.findViewById(R.id.virusPPMTextView);
            viewHolder.txtContaminantPPM = (TextView) convertView.findViewById(R.id.contaminantPPMTextView);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PurityAdapter.ViewHolder) convertView.getTag();
            result=convertView;
        }
        //using placeholder string because it is bad practice to concatenate strings inside of setText
        String placeholder = "Date: " + pureModel.getDate();
        viewHolder.txtDate.setText(placeholder);
        viewHolder.txtDate.setText(placeholder);
        placeholder = "Report ID: " + pureModel.getReportId();
        viewHolder.txtReportId.setText(placeholder);
        placeholder = "Reporter Name: " + pureModel.getWorkerName();
        viewHolder.txtReporterName.setText(placeholder);
        placeholder = "Location: " + pureModel.getLocation();
        viewHolder.txtLocation.setText(placeholder);
        placeholder = "Overall Condition: " + pureModel.getOverallCondition();
        viewHolder.txtOverallCondition.setText(placeholder);
        placeholder = "Virus PPM: " + pureModel.getVirusPPM();
        viewHolder.txtVirusPPM.setText(placeholder);
        placeholder = "Contaminant PPM: " + pureModel.getContaminantPPM();
        viewHolder.txtContaminantPPM.setText(placeholder);
        // Return the completed view to render on screen
        return convertView;
    }

}
