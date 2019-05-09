package com.a161328.demo3.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.a161328.demo3.Model.Record;
import com.a161328.demo3.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends Fragment implements OnChartValueSelectedListener {

//    GraphView graph;

    private ArrayList<Record> fragmentRecord;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private CollectionReference recordRef;
    private DocumentReference myRecordRef;
    private Record recordResult;
    private TextView percent1, percent2, percent3, percent4, result_totalScore, result_severity, result_advice;
    private ArrayList<TextView> txtPercent;
    private ProgressBar progressLoadResult;
    private LinearLayout result_linear_1, result_linear_2;
    ArrayList<PieEntry> entries = new ArrayList<>();

    public ResultFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_result, container, false);

//        pieChart2 = v.findViewById(R.id.pieChart2);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        PieChart pieChart = v.findViewById(R.id.pieChart1);

        percent1 = v.findViewById(R.id.txt_percent_1);
        percent2 = v.findViewById(R.id.txt_percent_2);
        percent3 = v.findViewById(R.id.txt_percent_3);
        percent4 = v.findViewById(R.id.txt_percent_4);
        result_totalScore = v.findViewById(R.id.txt_result_totalScore);
        result_severity = v.findViewById(R.id.txt_result_severity);
        result_advice = v.findViewById(R.id.txt_result_advice);
        progressLoadResult = v.findViewById(R.id.progress_resultLoad);

        result_linear_1 = v.findViewById(R.id.result_linear1);
        result_linear_2 = v.findViewById(R.id.result_linear2);

        progressLoadResult.setVisibility(View.VISIBLE);

        txtPercent = new ArrayList<>();
        txtPercent.add(percent1);
        txtPercent.add(percent2);
        txtPercent.add(percent3);
        txtPercent.add(percent4);

        if (mAuth.getCurrentUser() != null) {
            /*
            if user is signed in
             */
            fragmentRecord = new ArrayList<>(); // used if we load multiple record from one user
            recordResult = Record.getInstance();
            getSingleRecordFromFS();

            if (
                    !percent1.getText().equals("") &&
                            !percent2.getText().equals("") &&
                            !percent3.getText().equals("") &&
                            !percent4.getText().equals("") &&
                            !result_severity.getText().equals("")) {
                progressLoadResult.setVisibility(View.INVISIBLE);
            }
            setPieChart(pieChart);

        } else {
            /*
            if user is not signed in
            set visibility to GONE
             */
            progressLoadResult.setVisibility(View.INVISIBLE);
            pieChart.setVisibility(View.GONE);
            result_linear_1.setVisibility(View.GONE);
            result_linear_2.setVisibility(View.GONE);
            String s = "Null user has no record";
            result_totalScore.setText(s);
        }
        return v;
    }

    public void getSingleRecordFromFS() {

        percent1.setText(String.valueOf(recordResult.calculateSubScale("NM")));
        percent2.setText(String.valueOf(recordResult.calculateSubScale("NS")));
        percent3.setText(String.valueOf(recordResult.calculateSubScale("IE")));
        percent4.setText(String.valueOf(recordResult.calculateSubScale("IP")));

        int totalScore = recordResult.calculateTotalScore();
        String textTotalScore = "Your score is " + totalScore;
        result_totalScore.setText(textTotalScore);

        result_severity.setText(recordResult.getSevereness());
        shortToast("get record successful");

//        myRecordRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//
//                recordResult = documentSnapshot.toObject(Record.class);
//                Date resultDate = recordResult.getDate();
//                ArrayList<Integer> resultScoreList = recordResult.getRecord();
//                String resultUser = recordResult.getUser_id();
//
//                /*
//                calculate percentage of sub-scales
//                 */
//                /*
//                calculate total score
//                 */
//                percent1.setText(String.valueOf(recordResult.calculateSubScale("NM")));
//                percent2.setText(String.valueOf(recordResult.calculateSubScale("NS")));
//                percent3.setText(String.valueOf(recordResult.calculateSubScale("IE")));
//                percent4.setText(String.valueOf(recordResult.calculateSubScale("IP")));
//
//                int totalScore = recordResult.calculateTotalScore();
//                String textTotalScore = "Your score is " + totalScore;
//
//                result_totalScore.setText(textTotalScore);
//                result_severity.setText(recordResult.getSevereness());
//                shortToast("get record successful");
//                progressLoadResult.setVisibility(View.INVISIBLE);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                shortToast(e.getMessage());
//            }
//        });
    }

    public void setPieChart(PieChart pieChart) {

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setCenterText("moodswingsss");

        pieChart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(getResources().getColor(R.color.swatch1_4));

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        pieChart.setOnChartValueSelectedListener(this);

        pieChart.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        Legend l = pieChart.getLegend();

        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);

        setData(pieChart);
//        setData(pieChart2);
//        ArrayList<PieEntry> yValues = new ArrayList<>();
//        yValues.add(new PieEntry(3, "Emo"));
//        yValues.add(new PieEntry(2, "Neg mood"));
//        yValues.add(new PieEntry(1, "Neg self"));
//        yValues.add(new PieEntry(2, "Func"));
//        yValues.add(new PieEntry(3, "Ineff"));
//        yValues.add(new PieEntry(2, "Interp"));
//
//
//
//        PieDataSet dataSet = new PieDataSet(yValues, "score");
//        dataSet.setSliceSpace(3f);
//        dataSet.setSelectionShift(5f);
//        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
//
//        PieData data = new PieData((dataSet));
//        data.setValueTextSize(10f);
//        data.setValueTextColor(Color.WHITE);
//
//        pieChart.setData(data);
    }

    private void setData(PieChart pieChart) {
        entries = new ArrayList<>();

        // if exist, then add

        entries.add(new PieEntry(recordResult.calculateSubScale("NM"), "Negative Mood"));
        entries.add(new PieEntry(recordResult.calculateSubScale("NS"), "Negative Self-esteem"));
        entries.add(new PieEntry(recordResult.calculateSubScale("IE"), "Ineffectiveness"));
        entries.add(new PieEntry(recordResult.calculateSubScale("IP"),"Interpersonal Problem"));

        PieDataSet dataSet = new PieDataSet(entries, "Result");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
//        dataSet.setUsingSliceColorAsValueLineColor(true);

        //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
//        data.setValueTypeface(tf);
        pieChart.setData(data);

        pieChart.highlightValues(null);
        pieChart.invalidate();
    }


    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Moodswingss");
        s.setSpan(new RelativeSizeSpan(1.5f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);

        s.setSpan(new RelativeSizeSpan(.65f), 14, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        shortToast(e.toString());
    }

    @Override
    public void onNothingSelected() {

    }

    public void shortToast(String m) {
        Toast.makeText(getActivity(), m, Toast.LENGTH_SHORT).show();
    }
}
