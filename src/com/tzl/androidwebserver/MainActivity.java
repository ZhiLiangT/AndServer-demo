package com.tzl.androidwebserver;

import com.tzl.androidwebserver.dialog.FoldersDialogFragment;
import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.Server;
import com.yanzhenjie.andserver.website.StorageWebsite;
import com.yanzhenjie.andserver.website.WebSite;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener{
	private Server server ;
	private String websiteDirectory;
	private WebSite wesite ;
	private AndServer andServer ;
	private Button btStart,btStop;
	private Button btChoice;
	private Button btState;
	private EditText etPort;
	private EditText etOutTime;
	private EditText etAddress;
	private int port;
	private int outTime;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}
	
	private void initView() {
		etAddress=(EditText) findViewById(R.id.main_et_set_address);
		etPort=(EditText) findViewById(R.id.main_et_set_port);
		etOutTime=(EditText) findViewById(R.id.main_et_set_outtime);
		btStart=(Button) findViewById(R.id.main_bt_start_server);
		btStop=(Button) findViewById(R.id.main_bt_stop_server);
		btChoice=(Button) findViewById(R.id.main_bt_set_address);
		btState=(Button) findViewById(R.id.main_bt_test_state);
		btState.setOnClickListener(this);
		btStart.setOnClickListener(this);
		btStop.setOnClickListener(this);
		btChoice.setOnClickListener(this);
	}
	
	private Server.Listener mListener = new Server.Listener() {
        @Override
        public void onStarted() {
        	//服务器启动成功
            Log.d("server", "server start");
        }
        @Override
        public void onStopped() {
        	//服务器停止了，一般是开发者调用stop()才会停止
            Log.d("server", "server stop");
        }
        @Override
        public void onError(Exception e) {
        	//服务器启动发生错误，一般是端口被占用
            Log.d("server", "server error");
        }
    };

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.main_bt_start_server:
			//开启服务器
			startServer();
			break;
		case R.id.main_bt_stop_server:
			//停止服务器
			server.stop();
			break;
		case R.id.main_bt_set_address:
			//选择文件夹地址
			getAddress();
		case R.id.main_bt_test_state:
			//测试服务器状态
			testState();
			break;
		default:
			break;
		}
	}
	public void testState(){
		if (server!=null) {
			if (server.isRunning()) {
				Toast.makeText(this, "正在运行中", Toast.LENGTH_SHORT).show();
			}else {
				Toast.makeText(this, "服务已停止", Toast.LENGTH_SHORT).show();
			}	
		}else {
			Toast.makeText(this, "服务未创建，无法获取状态", Toast.LENGTH_SHORT).show();
		}
	}
	//开启服务器
	public void startServer(){
		
		AndServer.Build build=new AndServer.Build();
		//设置端口号
		port=Integer.parseInt(etPort.getText().toString());
		if (port!=0) {
			build.port(port);
		}else {
			Toast.makeText(this, "端口未设置", Toast.LENGTH_SHORT).show();
			return;
		}
		//设置超时时间
		outTime=Integer.parseInt(etOutTime.getText().toString());
		if (outTime!=0) {
			build.timeout(outTime);
		}else {
			Toast.makeText(this, "超时时间未设置", Toast.LENGTH_SHORT).show();
		}
		websiteDirectory=etAddress.getText().toString();
		if (websiteDirectory!=null&&!websiteDirectory.equals("")) {
			wesite = new StorageWebsite(websiteDirectory);
			build.website(wesite);
		}else {
			Toast.makeText(this, "地址未设置", Toast.LENGTH_SHORT).show();
		}
		build.listener(mListener);
		andServer=build.build();
		//创建服务器
		server = andServer.createServer();
		//开启服务器
		server.start();
	}
	@SuppressLint("NewApi")
	public void getAddress(){
		FoldersDialogFragment dialog=new FoldersDialogFragment();
		dialog.setCallBack(new FoldersDialogFragment.PathCallBack(){
			@SuppressLint("NewApi")
			@Override
			public void callBack(String path) {
				if (path!=null) {
					websiteDirectory=path;
					etAddress.setText(path);
				}
			}
		});
		dialog.show(getFragmentManager(), "folder");
	}
}
