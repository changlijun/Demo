package cn.changlijun.onlinegamedemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public abstract class NetBaseActivity extends Activity implements Runnable,
		Callback {
	private Thread thread;

	private String jsonStr;
	
	private void setJsonStr(String jsonStr) {
		
		this.jsonStr = jsonStr;
	}

	private Handler handler = new Handler(this);

	private boolean isShowTitle = false;

	private String url;

	private ProgressDialog progressDialog;

	private void showProgressDialog() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("请稍等...");
		progressDialog.setCancelable(true);
		progressDialog.setIndeterminate(true);
		progressDialog.show();
	}

	private void dismissProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	public void setShowTitle(boolean isShowTitle) {
		this.isShowTitle = isShowTitle;
	}

	protected abstract void handleResult(String jsonStr);

	protected void request(String jsonStr, String url) {
		setJsonStr(jsonStr);
		this.url = url;
		thread = new Thread(this);
		thread.start();
		showProgressDialog();
	}

	protected void release() {
		dismissProgressDialog();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public void run() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		//添加HTTP头信息
		httpPost.addHeader("Authorization", "your token"); //认证token 
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.addHeader("User-Agent", "changlijun"); 

		try {
			StringEntity se = new StringEntity(jsonStr, "utf-8");
			httpPost.setEntity(se);

			httpClient.getParams().setIntParameter(
					HttpConnectionParams.CONNECTION_TIMEOUT, 10000);
			httpClient.getParams().setIntParameter(
					HttpConnectionParams.SO_TIMEOUT, 10000);

			HttpResponse response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(entity.getContent()));
					StringBuffer sb = new StringBuffer();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					reader.close();
					if (sb == null || sb.toString().trim() == "") {
						handler.sendEmptyMessage(-2);
					} else {
						Message msg = new Message();
						msg.what = 0;
						msg.obj = sb.toString();
						handler.sendMessage(msg);
					}
				}
			}else{
				handler.sendEmptyMessage(-1);
			}

		} catch (IOException e) {
			handler.sendEmptyMessage(-1);
			e.printStackTrace();
		}

	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case 0:
			handleResult((String) msg.obj);
			dismissProgressDialog();
			return true;
		case -1:
			showToast("网络异常出错！");
			release();
			return true;
		case -2:
			showToast("请到changlijun.cn注册，并替换App.Email！");
			release();
			return true;
		}
		return false;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isShowTitle) {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		}

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

}
