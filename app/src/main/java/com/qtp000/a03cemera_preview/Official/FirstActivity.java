package com.qtp000.a03cemera_preview.Official;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.bkrc.camera.XcApplication;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.HexDump;
import com.hoho.android.usbserial.util.SerialInputOutputManager;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationItem;
import com.luseen.luseenbottomnavigation.BottomNavigation.BottomNavigationView;
import com.luseen.luseenbottomnavigation.BottomNavigation.OnBottomNavigationItemClickListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import car.bkrc.right.fragment.LeftFragment;
import car.bkrc.right.fragment.RightFragment1;
import car.bkrc.right.fragment.RightInfraredFragment;
import car.bkrc.right.fragment.RightOtherFragment;
import car.bkrc.right.fragment.RightZigbeeFragment;
import car2017_demo.connect_transport;
import ui.bkrc.car.TitleToolbar;

public class FirstActivity extends AppCompatActivity {
    private ViewPager viewPager;
//    private TitleToolbar mToolbar;  //工具栏
// //   private ActionBar mActionBar;  //ActionBar

    public static connect_transport Connect_Transport;
    // 小车ip
    public static String IPCar;
    // 摄像头IP
    public static String IPCamera = null;
    public static String purecameraip =null;
    public static boolean chief_status_flag = true;//主从状态
    public static boolean chief_control_flag = true; //主从控制
    public static boolean coded_control_flag = true; //角度、码盘控制

    private BottomNavigationView bottomNavigationView;
    public static Handler recvhandler =null;
     public static Handler but_handler;  //是按钮和menu同步

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_first1);
        but_handler =button_handler;  //用于leftfragment中隐藏的按钮和标题栏中的menu同步

        if(XcApplication.isserial == XcApplication.Mode.USB_SERIAL) {  //竞赛平台和a72通过usb转串口通信
            mHandler.sendEmptyMessageDelayed(MESSAGE_REFRESH, REFRESH_TIMEOUT_MILLIS); //启动usb的识别和获取
                Transparent.showLoadingMessage(this,"加载中",false);//启动旋转效果的对话框，实现usb的识别和获取
        }

        mToolbar = (TitleToolbar) findViewById(R.id.toolbar);//使用标题栏
        setSupportActionBar(mToolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);//使用viewPager实现页面滑动效果
        viewPager .setOffscreenPageLimit(3);

        //接下来与底部导航栏有关
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);

        int[] image = {R.drawable.ic_mic_black_24dp, R.drawable.ic_favorite_black_24dp,
                R.drawable.ic_book_black_24dp, R.drawable.github_circle};
        int[] color = {ContextCompat.getColor(this, R.color.firstColor), ContextCompat.getColor(this, R.color.secondColor),
                ContextCompat.getColor(this, R.color.thirdColor), ContextCompat.getColor(this, R.color.fourthColor)};
        if (bottomNavigationView != null) {
            bottomNavigationView.isWithText(false);
            // bottomNavigationView.activateTabletMode();
            bottomNavigationView.isColoredBackground(true);
            bottomNavigationView.setTextActiveSize(getResources().getDimension(R.dimen.text_active));
            bottomNavigationView.setTextInactiveSize(getResources().getDimension(R.dimen.text_inactive));
            bottomNavigationView.setItemActiveColorWithoutColoredBackground(ContextCompat.getColor(this, R.color.firstColor));
        }

        BottomNavigationItem bottomNavigationItem = new BottomNavigationItem
                ("主页", color[0], image[0]);
        BottomNavigationItem bottomNavigationItem1 = new BottomNavigationItem
                ("zigbee", color[1], image[1]);
        BottomNavigationItem bottomNavigationItem2 = new BottomNavigationItem
                ("红外", color[2], image[2]);
        BottomNavigationItem bottomNavigationItem3 = new BottomNavigationItem
                ("其他", color[3], image[3]);

        bottomNavigationView.addTab(bottomNavigationItem);
        bottomNavigationView.addTab(bottomNavigationItem1);
        bottomNavigationView.addTab(bottomNavigationItem2);
        bottomNavigationView.addTab(bottomNavigationItem3);

        bottomNavigationView.setOnBottomNavigationItemClickListener(new OnBottomNavigationItemClickListener() {
            @Override
            public void onNavigationItemClick(int index) {
                switch (index) {
                    case 0:
                        viewPager.setCurrentItem(0);
                        break;
                    case 1:
                        viewPager.setCurrentItem(1);
                        break;
                    case 2:
                        viewPager.setCurrentItem(2);
                        break;
                    case 3:
                        viewPager.setCurrentItem(3);
                        break;
                }
            }
        });
      Connect_Transport = new connect_transport();        //实例化连接类
       setupViewPager(viewPager);  //加载fragment
    }

    private void setupViewPager(ViewPager viewPager) {
       ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(RightFragment1.getInstance());
        adapter.addFragment(RightZigbeeFragment.getInstance());
        adapter.addFragment(RightInfraredFragment.getInstance());
        adapter.addFragment(RightOtherFragment.getInstance());
        viewPager.setAdapter(adapter);
    }


    private  Menu toolmenu ;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {   //activity创建时创建菜单Menu
        getMenuInflater().inflate(R.menu.tool_rightitem, menu);
        toolmenu =menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {  //菜单项监听
        int id = item.getItemId();
        Toast.makeText(FirstActivity.this,item.getTitle(),Toast.LENGTH_SHORT).show();
        switch (id)
        {
            case R.id.car_status:
                if(item.getTitle().equals("主车状态"))
                {
                    chief_status_flag =true;
                    item.setTitle(getResources().getText(R.string.follow_status));
                    Connect_Transport.vice(2);
                    LeftFragment.btchange_handler.obtainMessage(21).sendToTarget();
                }
                else if(item.getTitle().equals("从车状态"))
                {
                    chief_status_flag =false;
                    item.setTitle(getResources().getText(R.string.main_status));
                    Connect_Transport.vice(1);
                    LeftFragment.btchange_handler.obtainMessage(22).sendToTarget();
                }
                break;
            case R.id.car_control:
                if(item.getTitle().equals("主车控制"))
                {
                    chief_control_flag =true;
                    item.setTitle(getResources().getText(R.string.follow_control));
                    Connect_Transport.TYPE = 0xAA;
                    LeftFragment.btchange_handler.obtainMessage(23).sendToTarget();
                }
                else if(item.getTitle().equals("从车控制"))
                {
                    chief_control_flag =false;
                    item.setTitle(getResources().getText(R.string.main_control));
                    Connect_Transport.TYPE =  0x02;
                    LeftFragment.btchange_handler.obtainMessage(24).sendToTarget();
                }
                break;

            case R.id.angle_control:
                if(item.getTitle().equals("码盘控制"))
                {
                    coded_control_flag =true;
                    item.setTitle(getResources().getText(R.string.angle_control));
                    LeftFragment.btchange_handler.obtainMessage(25).sendToTarget();
                }
                else if(item.getTitle().equals("角度控制"))
                {
                    coded_control_flag =false;
                    item.setTitle(getResources().getText(R.string.coded_control));
                    LeftFragment.btchange_handler.obtainMessage(26).sendToTarget();
                }
                break;
            case R.id.clear_coded_disc:
                Connect_Transport.clear();
                break;
            case R.id.full_automatic:
                Toast.makeText(FirstActivity.this, "全自动", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

     private Handler button_handler =new Handler()  //实现menu和leftfragment中的三个按钮同步
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 11:
                    toolmenu.getItem(1).setTitle(getResources().getText(R.string.follow_status));
                    break;
                case 22:
                    toolmenu.getItem(1).setTitle(getResources().getText(R.string.main_status));
                    break;
                case 33:
                    toolmenu.getItem(2).setTitle(getResources().getText(R.string.follow_control));
                    break;
                case 44:
                    toolmenu.getItem(2).setTitle(getResources().getText(R.string.main_control));
                    break;
                case 55:
                    toolmenu.getItem(3).setTitle(getResources().getText(R.string.angle_control));
                    break;
                case 66:
                    toolmenu.getItem(3).setTitle(getResources().getText(R.string.coded_control));
                    break;
                default:
                    break;

            }
        }
    };



    //------------------------------------------------------------------------------------------
    //获取和实现usb转串口的通信，实现A72和竞赛平台的串口通信
    public static UsbSerialPort sPort = null;

    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    private SerialInputOutputManager mSerialIoManager;

    private final SerialInputOutputManager.Listener mListener =
            new SerialInputOutputManager.Listener() {

                @Override
                public void onRunError(Exception e) {
                    Log.e(TAG, "Runner stopped.");
                }

                @Override
                public void onNewData(final byte[] data) {   //新的数据
                    FirstActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg =recvhandler.obtainMessage(1,data);
                            msg.sendToTarget();
                            FirstActivity.this.updateReceivedData(data);
                        }
                    });
                }
            };

    protected void controlusb() {
        Log.e(TAG, "Resumed, port=" + sPort);
        if (sPort == null) {
            Toast.makeText(FirstActivity.this,"No serial device.",Toast.LENGTH_SHORT).show();
        } else {
            openUsbDevice();
            if (connection == null) {
                mHandler.sendEmptyMessageDelayed(MESSAGE_REFRESH, REFRESH_TIMEOUT_MILLIS);
                Toast.makeText(FirstActivity.this,"Opening device failed",Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                sPort.open(connection);
                sPort.setParameters(115200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            } catch (IOException e) {
                Toast.makeText(FirstActivity.this,"Error opening device: ",Toast.LENGTH_SHORT).show();
                try {
                    sPort.close();
                } catch (IOException e2) {
                }
                sPort = null;
                return;
            }
            Toast.makeText(FirstActivity.this,"Serial device: " + sPort.getClass().getSimpleName(),Toast.LENGTH_SHORT).show();
        }
        onDeviceStateChange();
        Transparent.dismiss();//关闭加载对话框
    }
    // 在打开usb设备前，弹出选择对话框，尝试获取usb权限
    private void openUsbDevice(){
        tryGetUsbPermission();
    }

    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private UsbDeviceConnection connection;

    private void tryGetUsbPermission(){

        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(mUsbPermissionActionReceiver, filter);
        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);

        //here do emulation to ask all connected usb device for permission
        for (final UsbDevice usbDevice : mUsbManager.getDeviceList().values()) {
            //add some conditional check if necessary
            if(mUsbManager.hasPermission(usbDevice)){
                //if has already got permission, just goto connect it
                //that means: user has choose yes for your previously popup window asking for grant perssion for this usb device
                //and also choose option: not ask again
                afterGetUsbPermission(usbDevice);
            }else{
                //this line will let android popup window, ask user whether to allow this app to have permission to operate this usb device
                mUsbManager.requestPermission(usbDevice, mPermissionIntent);
            }
        }
    }

    private void afterGetUsbPermission(UsbDevice usbDevice){

        Toast.makeText(FirstActivity.this, String.valueOf("Found USB device: VID=" + usbDevice.getVendorId() + " PID=" + usbDevice.getProductId()), Toast.LENGTH_LONG).show();
        doYourOpenUsbDevice(usbDevice);
    }

    private void doYourOpenUsbDevice(UsbDevice usbDevice){
        connection = mUsbManager.openDevice(usbDevice);
    }

    private final BroadcastReceiver mUsbPermissionActionReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice usbDevice = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        //user choose YES for your previously popup window asking for grant perssion for this usb device
                        if(null != usbDevice){
                            afterGetUsbPermission(usbDevice);
                        }
                    }
                    else {
                        //user choose NO for your previously popup window asking for grant perssion for this usb device
                        Toast.makeText(context, String.valueOf("Permission denied for device" + usbDevice), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    };

    private void stopIoManager() {
        if (mSerialIoManager != null) {
            Log.e(TAG, "Stopping io manager ..");
            mSerialIoManager.stop();
            mSerialIoManager = null;
        }
    }

    private void startIoManager() {
        if (sPort != null) {
            Log.e(TAG, "Starting io manager ..");
            mSerialIoManager = new SerialInputOutputManager(sPort, mListener); //添加监听
            mExecutor.submit(mSerialIoManager); //在新的线程中监听串口的数据变化
        }
    }

    private void onDeviceStateChange() {
        stopIoManager();
        startIoManager();
    }

    private void updateReceivedData(byte[] data) {
        final String message = "Read " + data.length + " bytes: \n"
                + HexDump.dumpHexString(data) + "\n\n";
      //  Log.e("read data is ：：","   "+message);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(XcApplication.isserial == XcApplication.Mode.USB_SERIAL) {
            unregisterReceiver(mUsbPermissionActionReceiver);
            try {
                sPort.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            sPort = null;
        }
        else if(XcApplication.isserial == XcApplication.Mode.SOCKET) {
            Connect_Transport.destory();
        }
    }

    private static final int MESSAGE_REFRESH= 101;
    private static final long REFRESH_TIMEOUT_MILLIS = 5000;
    private UsbManager mUsbManager;
    private List<UsbSerialPort> mEntries = new ArrayList<UsbSerialPort>();
    private final String TAG = FirstActivity.class.getSimpleName();

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_REFRESH:
                    refreshDeviceList();
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };

    private Handler usbHandler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what ==2) {
                useUsbtoserial();
            }
        }
    };

    private void useUsbtoserial()
    {
        final UsbSerialPort port = mEntries.get(0);  //A72上只有一个 usb转串口，用position =0即可
        final UsbSerialDriver driver = port.getDriver();
        final UsbDevice device = driver.getDevice();
        final String usbid = String.format("Vendor %s  ，Product %s",
                HexDump.toHexString((short) device.getVendorId()),
                HexDump.toHexString((short) device.getProductId()));
        Message msg =LeftFragment.showidHandler.obtainMessage(22,usbid);
        msg.sendToTarget();
        FirstActivity.sPort = port;
        if(sPort !=null) {
            controlusb();  //使用usb功能
        }
    }

    private void refreshDeviceList() {
        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        new AsyncTask<Void, Void, List<UsbSerialPort>>() {
            @Override
            protected List<UsbSerialPort> doInBackground(Void... params) {
                Log.e(TAG, "Refreshing device list ...");
                Log.e("mUsbManager is :","  "+mUsbManager);
                final List<UsbSerialDriver> drivers =
                        UsbSerialProber.getDefaultProber().findAllDrivers(mUsbManager);

                final List<UsbSerialPort> result = new ArrayList<UsbSerialPort>();
                for (final UsbSerialDriver driver : drivers) {
                    final List<UsbSerialPort> ports = driver.getPorts();
                    Log.e(TAG, String.format("+ %s: %s port%s",
                            driver, Integer.valueOf(ports.size()), ports.size() == 1 ? "" : "s"));
                    result.addAll(ports);
                }
                return result;
            }

            @Override
            protected void onPostExecute(List<UsbSerialPort> result) {
                mEntries.clear();
                mEntries.addAll(result);
                usbHandler.sendEmptyMessage(2);
                Log.e(TAG, "Done refreshing, " + mEntries.size() + " entries found.");
            }
        }.execute((Void) null);
    }

}
