package com.example.smartlinklib;

import com.example.smartlinklib.ModuleInfo;
import com.example.smartlinklib.SmartLinkManipulator;
import com.example.smartlinklib.SmartLinkManipulator.ConnectCallBack;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	TextView ssid;
	Button m_startBtn;
	EditText pswd;
	SmartLinkManipulator sm;
	boolean isconncting = false;
	Handler hand = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				m_startBtn.setText("停止链接");
				break;
			case 2:
				m_startBtn.setText("开始链接");
				break;
			default:
				break;
			}
		};
	};
	
	ConnectCallBack callback = new ConnectCallBack() {
		
		@Override
		public void onConnectTimeOut() {
			// TODO Auto-generated method stub
			hand.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(MainActivity.this, "配置超时", Toast.LENGTH_SHORT).show();
					m_startBtn.setText("开始链接");
					isconncting = false;
				}
			});
		}
		
		@Override
		public void onConnect(final ModuleInfo mi) {
			// TODO Auto-generated method stub
			Log.e("NEWMdule", mi.getModuleIP());
			hand.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(MainActivity.this, "发现设备  "+mi.getMid()+"mac"+ mi.getMac()+"IP"+mi.getModuleIP(), Toast.LENGTH_SHORT).show();
				}
			});
		}

		@Override
		public void onConnectOk() {
			// TODO Auto-generated method stub
			hand.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(MainActivity.this, "配置完成", Toast.LENGTH_SHORT).show();
					m_startBtn.setText("开始链接");
					isconncting = false;
				}
			});
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		m_startBtn = (Button) findViewById(R.id.start);
		ssid = (TextView) findViewById(R.id.ssid);
		ssid.setText(getSSid());
		pswd = (EditText) findViewById(R.id.pswd);
		
		
		//获取实例
//		sm = SmartLinkManipulator.getInstence(MainActivity.this);
		
		
		m_startBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isconncting){
					isconncting = true;
					sm = SmartLinkManipulator.getInstence(MainActivity.this);
					//不管在不在发送 先停止发送  释放所有缓存
//					sm.StopConnection();
					
					//再次获取实例 加载需要的信息
//					sm = SmartLinkManipulator.getInstence(MainActivity.this);
					
					String ss = getSSid();
					String ps = pswd.getText().toString().trim();
					hand.sendEmptyMessage(1);
					
					//设置要配置的ssid 和pswd
					sm.setConnection(ss, ps);
					//开始 smartLink
					sm.Startconnection(callback);
				}else{
					sm.StopConnection();
					hand.sendEmptyMessage(2);
					isconncting = false;
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private String getSSid(){
		WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
		if(wm != null){
			WifiInfo wi = wm.getConnectionInfo();
			if(wi != null){
				String s = wi.getSSID();
				if(s.length()>2&&s.charAt(0) == '"'&&s.charAt(s.length() -1) == '"'){
					return s.substring(0,s.length()-1);
				}
			}
		}
		return "";
	}

}
