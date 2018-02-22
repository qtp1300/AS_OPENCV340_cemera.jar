package com.qtp000.a03cemera_preview;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by 祁天培 on 2018/2/9.
 */

public class FunctionActivity extends Activity {
    private Function_method fmd = new Function_method(FunctionActivity.this);
    private Socket_connect sc;
    private String qr;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.function_car);

        ExpandableListAdapter adapter = getAdapter();
        ExpandableListView expandListView = (ExpandableListView)findViewById(R.id.elv_list);
        expandListView.setAdapter(adapter);
        expandListView.setOnChildClickListener(new OnChildClickListenerImpl());
    }
//    public static int reqc;
//    public static int resc;
//    public static Intent data;
//	protected void onActivityResult(int requestCode, int resultCode, Intent data)
//	{
//		reqc = requestCode;
//		resc = resultCode;
//		this.data = data;
//
//		super.onActivityResult(requestCode, resultCode, data);
//	}

    private ExpandableListAdapter getAdapter()
    {
        ExpandableListAdapter adapter = new BaseExpandableListAdapter()
        {
            int[] logos = new int[]
                    {
                            R.drawable.ic_launcher,
                            R.drawable.ic_launcher,
                            R.drawable.ic_launcher,
                            R.drawable.ic_launcher,
                            R.drawable.ic_launcher,
                    };

            private String[] armTypes = new String[]
                    {"红外","识别","zigbee","预设位","任务板"};

            private String[][] arms = new String[][]
                    {
                            {"报警器","灯光档位器","隧道风扇","立体显示","图片器"},
                            {"图形图像","车牌","二维码","交通灯"},
                            {"闸门","数码管","语音播报","磁悬浮","TFT显示器"},
                            {"set1","set2","set3","call1","call2","call3"},
                            {"转向灯","蜂鸣器"}
                    };

            public Object getChild(int groupPosition, int childPosition)
            {
                return arms[groupPosition][childPosition];
            }

            public boolean isChildSelectable(int groupPosition, int childPosition)
            {
                return true;
            }

            public long getChildId(int groupPosition, int childPosition)
            {
                return childPosition;
            }

            public int getChildrenCount(int groupPosition)
            {
                return arms[groupPosition].length;
            }

            private TextView getTextView()
            {
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,70);
                TextView textView = new TextView(FunctionActivity.this);
                textView.setLayoutParams(lp);
                textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                textView.setPadding(100, 0, 0, 0);
                textView.setTextSize(25);
                textView.setTextColor(Color.rgb(255, 255, 255));
                return textView;
            }

            @Override
            public View getChildView(int groupPosition, int childPosition,
                                     boolean isLastChild, View convertView, ViewGroup parent)
            {
                TextView textView = getTextView();
                textView.setText(getChild(groupPosition, childPosition).toString());
                return textView;
            }

            public Object getGroup(int groupPosition)
            {
                return armTypes[groupPosition];
            }

            public int getGroupCount()
            {
                return armTypes.length;
            }

            public long getGroupId(int groupPosition)
            {
                return groupPosition;
            }

            public boolean hasStableIds()
            {
                return true;
            }

            public View getGroupView(int groupPosition, boolean isExpanded,
                                     View convertView, ViewGroup parent)
            {
                LinearLayout ll = new LinearLayout(FunctionActivity.this);
                ll.setOrientation(LinearLayout.HORIZONTAL);
                ImageView logo = new ImageView(FunctionActivity.this);
                logo.setImageResource(logos[groupPosition]);
                ll.addView(logo);
                TextView textView = getTextView();
                textView.setText(getGroup(groupPosition).toString());
                textView.setTextColor(Color.rgb(192, 192, 192));
                textView.setTextSize(28);
                ll.addView(textView);
                return ll;
            }
        };
        return adapter;
    }

    private class OnChildClickListenerImpl implements ExpandableListView.OnChildClickListener
    {
        public boolean onChildClick(ExpandableListView parent, View v,
                                    int groupPosition, int childPosition, long id)
        {
            switch(groupPosition)
            {
                case 0://红外
                    if(childPosition == 0)//报警器
                        fmd.policeController();
                    if(childPosition == 1)//灯光档位器
                        fmd.gearController();
//           		if((childPosition == 2) && (groupPosition == 0))
//           			//风扇未成功
//           			sc.fan();
                    if((childPosition == 3) && (groupPosition == 0))//立体显示
                    {
                        Log.e("已接受到消息", "立体显示");
                        fmd.threeDisplay();
                    }
                    if(childPosition == 4)//图片器
                        fmd.pictureController();
                    break;
                case 1://识别
                    if(childPosition == 0)//图形图像识别
                    {
                        //fmd.checkShecp();
                        //Toast.makeText(FunctionActivity.this, "图像识别完毕", 0).show();
                        int color = fmd.checkBack();
                        if(color == 0xffff00)
                        {
                            fmd.carLicence();
                            Toast.makeText(FunctionActivity.this, "车牌识别完成", Toast.LENGTH_SHORT).show();
                        }
                        else if(color == 0xffffff)
                        {
                            fmd.checkShecp();
                            Toast.makeText(FunctionActivity.this, "形状识别完成", Toast.LENGTH_SHORT).show();
                        }
                        else
                            fmd.checkShecp();
//            			Toast.makeText(FunctionActivity.this, "error", 0).show();
                        //Toast.makeText(FunctionActivity.this, "背景识别完毕", 0).show();*/
                    }
                    if(childPosition == 1)//车牌
                        fmd.carLicence();
                    if(childPosition == 2)//二维码
                    {
                        fmd.msg = 10;
                        fmd.QrCode();
                        if(fmd.result_qr == null)
                            Toast.makeText(FunctionActivity.this, "正在识别...请再次点击", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(FunctionActivity.this, fmd.result_qr, Toast.LENGTH_SHORT).show();
                        System.out.println("二维码已经触发");
                    }
                    if(childPosition == 3)//交通灯
                        fmd.checkTraffic();
                    break;
                case 2://zigbee
                    if((childPosition == 0) && (groupPosition == 2))//闸门
                        fmd.gateController();
                    if(childPosition == 1)//数码管
                        fmd.digital();
                    if(childPosition == 2)//语音播报
                        fmd.voiceController();
                    if(childPosition == 3)//磁悬浮
                        fmd.magnetic_suspension();
                    if(childPosition == 4)//TFT显示屏
                        fmd.TFT_LCD();
                    break;
                case 3://预设位
                    MainActivity.state_camera = childPosition+5;
                    break;
                case 4://任务板
                    if(childPosition == 0)//转向灯
                        fmd.lightController();
                    if(childPosition == 1)//蜂鸣器
                        fmd.buzzerController();
                    break;
            }
            return true;
        }
    }

}
