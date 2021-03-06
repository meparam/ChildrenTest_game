package com.myandroid.myapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.myandroid.dao.TurnResultDao;
import com.myandroid.dao.UserInfoDao;
import com.myandroid.model.TurnResult;
import com.myandroid.model.UserInfo;

public class ShuziyouxiActivity extends ActionBarActivity {

	private int score;

	private int tzxhfs;

	private int index;

	private TextView indextv;

	private Button tjbtn;

	private TextView num1;

	private TextView num2;

	private TextView num3;

	private TextView num4;

	private TextView daojishitv;

	private EditText shurujieguo;

	private ImageView img1;

	private ImageView img2;

	private ImageView img3;

	private int recLen = 9;

	private LinearLayout linerlayout;

	private Timer timer;
	private int stop;
	private int start;
	
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;

	private int[] icons = { R.drawable.a1, R.drawable.a2, R.drawable.a3,
			R.drawable.a4, R.drawable.a5, R.drawable.a6, R.drawable.a7,
			R.drawable.a8, R.drawable.a9 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_shuzhiyouxi);
		sp = getSharedPreferences("settime", 1);
		
		editor = sp.edit();
		if(sp != null){
			stop = sp.getInt("startTime", 9);
			start = sp.getInt("startResponse", 6);
		}
		index = 1;

		score = 0;

		tzxhfs = 0;

		num1 = (TextView) this.findViewById(R.id.num1);

		num2 = (TextView) this.findViewById(R.id.num2);

		num3 = (TextView) this.findViewById(R.id.num3);

		num4 = (TextView) this.findViewById(R.id.num4);

		daojishitv = (TextView) this.findViewById(R.id.daojishitv);
		daojishitv.setText("停止信号倒计时:" + String.valueOf(stop)
				+ "S");
		indextv = (TextView) this.findViewById(R.id.indextv);

		shurujieguo = (EditText) this.findViewById(R.id.shurujieguo);

		shurujieguo
				.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {

						if (hasFocus) {
							if (stop > 0) {
								tijiaolistener.onClick(tjbtn);
							}

						} else {

							// 此处为失去焦点时的处理内容

						}

					}

				});

		linerlayout = (LinearLayout) this.findViewById(R.id.LinearLayout11);

		img1 = (ImageView) this.findViewById(R.id.img1);

		img2 = (ImageView) this.findViewById(R.id.img2);

		img3 = (ImageView) this.findViewById(R.id.img3);

		tjbtn = (Button) this.findViewById(R.id.tjbtn);

		tjbtn.setOnClickListener(tijiaolistener);

		MakePic();

		timer = new Timer();

		timer.schedule(task, 1000, 1000);
	}

	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			runOnUiThread(new Runnable() { // UI thread
				@Override
				public void run() {
					if (stop == 0) {
						daojishitv.setText("停止信号倒计时结束");
						int daojishi = start ;

						String tijiao = "提  交(" + String.valueOf(daojishi)
								+ "S)";

						tjbtn.setText(tijiao);

						if (start-- == 0) {

							tjbtn.setText("提  交");

							tijiaolistener.onClick(tjbtn);
						}
						// timer.cancel();
					} else {
						daojishitv.setText("停止信号倒计时:" + String.valueOf(stop)
								+ "S");
						stop--;
					}
				}
			});
		}
	};

	// 设置监听
	// .setOnClickListener(listener);//设置监听
	Button.OnClickListener tijiaolistener = new Button.OnClickListener() {
		public void onClick(View v) {
      tjbtn.setText("提  交");
      
			linerlayout.requestFocus();

			String ceshijieguo = "";

			String shuru = shurujieguo.getText().toString();

			String num44 = num4.getText().toString();

			if (shuru.equals(num44)) {

				score = score + 1;
			}
			if (stop == 0 && shuru.length() > 0) {
				tzxhfs = tzxhfs + 1;
			}
			/*Toast.makeText(
					ShuziyouxiActivity.this,
					"答题正确率=" + String.valueOf(score) + ";正确使用停止信号="
							+ String.valueOf(tzxhfs), Toast.LENGTH_LONG).show();*/
			index = index + 1;

			if (index == 11) {
				timer.cancel();
				if (score < 7) {
					ceshijieguo = "答题正确率低于70%，训练结果无效，请重来!";
					addResult(ceshijieguo);
					new AlertDialog.Builder(ShuziyouxiActivity.this)
							.setTitle("训练结果")
							.setMessage("答题正确率低于70%，训练结果无效，请重来!")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog, int i) {
											ShuziyouxiActivity.this.finish();
										}
									}).show();
				} else if (score >= 7 && tzxhfs == 0) {
					ceshijieguo = "正确使用停止信号0%；非正常使用停止信号100%；评价：请指导儿童正确使用停止信号!";
					addResult(ceshijieguo);
					new AlertDialog.Builder(ShuziyouxiActivity.this)
							.setTitle("训练结果")
							.setMessage(
									"正确使用停止信号0%；非正常使用停止信号100%；评价：请指导儿童正确使用停止信号!")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog, int i) {
											ShuziyouxiActivity.this.finish();
										}
									}).show();
				} else if (score >= 7 && tzxhfs >= 1 && tzxhfs <= 4) {
					ceshijieguo = "正确使用停止信号" + String.valueOf(tzxhfs)
							+ "0%，非正常使用停止信号" + String.valueOf(10 - tzxhfs)
							+ "0%；评价：继续努力!";
					addResult(ceshijieguo);
					new AlertDialog.Builder(ShuziyouxiActivity.this)
							.setTitle("训练结果")
							.setMessage(
									"正确使用停止信号" + String.valueOf(tzxhfs)
											+ "0%，非正常使用停止信号"
											+ String.valueOf(10 - tzxhfs)
											+ "0%；评价：继续努力!")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog, int i) {
											ShuziyouxiActivity.this.finish();
										}
									}).show();
				} else if (score >= 7 && tzxhfs >= 5 && tzxhfs <= 7) {
					ceshijieguo = "正确使用停止信号" + String.valueOf(tzxhfs)
							+ "0%，非正常使用停止信号" + String.valueOf(10 - tzxhfs)
							+ "0%；评价：再接再厉!";
					addResult(ceshijieguo);
					new AlertDialog.Builder(ShuziyouxiActivity.this)
							.setTitle("训练结果")
							.setMessage(
									"正确使用停止信号" + String.valueOf(tzxhfs)
											+ "0%，非正常使用停止信号"
											+ String.valueOf(10 - tzxhfs)
											+ "0%；评价：再接再厉!")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog, int i) {
											ShuziyouxiActivity.this.finish();
										}
									}).show();
				} else if (score >= 7 && tzxhfs >= 8 && tzxhfs <= 9) {
					ceshijieguo = "正确使用停止信号" + String.valueOf(tzxhfs)
							+ "0%，非正常使用停止信号" + String.valueOf(10 - tzxhfs)
							+ "0%；评价：离完美只差一步!";
					addResult(ceshijieguo);
					new AlertDialog.Builder(ShuziyouxiActivity.this)
							.setTitle("训练结果")
							.setMessage(
									"正确使用停止信号" + String.valueOf(tzxhfs)
											+ "0%，非正常使用停止信号"
											+ String.valueOf(10 - tzxhfs)
											+ "0%；评价：离完美只差一步!")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog, int i) {
											ShuziyouxiActivity.this.finish();
										}
									}).show();
				} else if (score >= 7 && tzxhfs == 10) {
					ceshijieguo = "正确使用停止信号" + String.valueOf(tzxhfs)
							+ "0%，非正常使用停止信号" + String.valueOf(10 - tzxhfs)
							+ "0%；评价：完美!";
					addResult(ceshijieguo);
					new AlertDialog.Builder(ShuziyouxiActivity.this)
							.setTitle("训练结果")
							.setMessage(
									"正确使用停止信号" + String.valueOf(tzxhfs)
											+ "0%，非正常使用停止信号"
											+ String.valueOf(10 - tzxhfs)
											+ "0%；评价：完美!")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog, int i) {
											ShuziyouxiActivity.this.finish();
										}
									}).show();
				}
			}
			if (index < 11) {
				MakePic();
			}
			stop = sp.getInt("startTime", 8);
			start = sp.getInt("startResponse", 5);

		}

	};

	public void addResult(String result) {
		UserInfoDao userinfoDAO = new UserInfoDao(ShuziyouxiActivity.this);

		TurnResultDao turnDAO = new TurnResultDao(ShuziyouxiActivity.this);

		UserInfo userinfo = new UserInfo();

		TurnResult turnResult = new TurnResult();

		List<UserInfo> userinfoList = new ArrayList<UserInfo>();

		userinfoList = userinfoDAO.listUserInfo();

		if (userinfoList.size() > 0) {

			userinfo = (UserInfo) userinfoList.get(0);

			turnResult.setAge(userinfo.getAge());

			turnResult.setUsername(userinfo.getUsername());

			turnResult.setSex(userinfo.getSex());

			turnResult.setResult(result);

			turnResult.setTurntype("数字加减");

			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			Date date = new Date();

			turnResult.setTurntime(f.format(date));

			turnDAO.add(turnResult);

		}
	}

	// 更新图片
	public void MakePic() {
		int a = 0;

		int b = 0;

		int c = 0;

		while (true) {
			a = this.CreateRandom(1, 9);

			b = this.CreateRandom(1, 9);

			c = this.CreateRandom(1, 9);

			if ((a + b - c) > 0) {

				break;
			}
		}
		num1.setText(String.valueOf(a));

		img1.setImageResource(icons[a - 1]);

		num2.setText(String.valueOf(b));

		img2.setImageResource(icons[b - 1]);

		num3.setText(String.valueOf(c));

		img3.setImageResource(icons[c - 1]);

		num4.setText(String.valueOf(a + b - c));

		indextv.setText(String.valueOf(index));

		shurujieguo.setText("");
	}

	// 产生随机数
	public int CreateRandom(int min, int max) {
		Random random = new Random();

		int s = random.nextInt(max) % (max - min + 1) + min;

		return s;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
