package cn.changlijun.onlinegamedemo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class LoginActivity extends NetBaseActivity {
	private String its[] = { "炎黄争霸", "龙行天下" };
	private EditText etUsername;
	private EditText etPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		Spinner spLogin = (Spinner) findViewById(R.id.spLogin);
		spLogin.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, its));

		Button btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					JSONArray jsonArr = new JSONArray();
					etUsername = (EditText) findViewById(R.id.etUsername);
					etPassword = (EditText) findViewById(R.id.etPassword);
					jsonArr.put(0, etUsername.getText().toString().trim());
					jsonArr.put(1, etPassword.getText().toString().trim());

					JSONObject jsonObj = new JSONObject();
					jsonObj.put("actioncode", 1);
					jsonObj.put("data", jsonArr);
					
					request(jsonObj.toString(), "http://182.92.114.9/login/");
				} catch (JSONException e) {
					e.printStackTrace();
					showToast("解码JSON字符串错误！");
					release();
				}
			}
		});
		String s = "{\"data\":\"changlijun\",\"actioncode\":1,\"success\",true}";
		String key = "A3C1D4";
		System.out.println(s);
		String temp = jiaMi(s,key);
		System.out.println(temp);
		System.out.println(jieMi(temp, key));
	}
	 //加密
	  public String jiaMi(String s,String key){
	    String str = "";
	    int ch;
	    if(key.length() == 0){
	        return s;
	    }
	    else if(!s.equals(null)){
	        for(int i = 0,j = 0;i < s.length();i++,j++){
	          if(j > key.length() - 1){
	            j = j % key.length();
	          }
	          ch = s.codePointAt(i) + key.codePointAt(j);
	          if(ch > 65535){
	            ch = ch % 65535;//ch - 33 = (ch - 33) % 95 ;
	          }
	          str += (char)ch;
	        }
	    }
	    return str;

	  } 
	  //解密
	  public String jieMi(String s,String key){
	    String str = "";
	    int ch;
	    if(key.length() == 0){
	        return s;
	    }
	    else if(!s.equals(key)){
	        for(int i = 0,j = 0;i < s.length();i++,j++){
	          if(j > key.length() - 1){
	            j = j % key.length();
	          }
	          ch = (s.codePointAt(i) + 65535 - key.codePointAt(j));
	          if(ch > 65535){
	            ch = ch % 65535;//ch - 33 = (ch - 33) % 95 ;
	          }
	          str += (char)ch;
	        }
	    }
	    return str;
	  }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	protected void handleResult(String jsonStr) {
		try {
			JSONObject jsonObj = new JSONObject(jsonStr);
			if (jsonObj != null) {
				boolean isSuccess = jsonObj.getBoolean("success");
				if (isSuccess) {
					System.out.println(jsonObj.toString());
				} else {
					showToast("登录失败：用户名或密码不正确！\n" + jsonObj.toString());
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			release();
		}
	}

}
