package com.hack.guidedog.email;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hack.guidedog.R;

public class MailActivity extends Activity implements OnClickListener {

	 Session session=null;
	 ProgressDialog pdialog=null;
	 Context context=null;
	 EditText reciept=null;
	 EditText sub=null;
	 EditText msg=null;
	 String recpient=null;
	 String subject=null;
	 String textmessage=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_mail);
		context = this;
		Button login = (Button) findViewById(R.id.mBtnSubmit);
		reciept = (EditText) findViewById(R.id.editText_to);
		sub = (EditText) findViewById(R.id.editText_sub);
		msg = (EditText) findViewById(R.id.editText_text);

		login.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mail, menu);		 
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		   recpient= reciept.getText().toString();
		      subject= sub.getText().toString();
		      textmessage= msg.getText().toString();
		    
		    Properties props = new Properties();
		     props.put("mail.smtp.host", "smtp.gmail.com");
		     props.put("mail.smtp.socketFactory.port", "465");
		     props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		     props.put("mail.smtp.auth", "true");
		     props.put("mail.smtp.port", "465");
		 
		     session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
		                       protected PasswordAuthentication getPasswordAuthentication() {
		     return new PasswordAuthentication("sahaj.horus@gmail.com", "domovoi0109");
		     }
		     });
		     pdialog = ProgressDialog.show(context, "", "Sending Mail...",true);
		     RetreiveFeedTask task= new RetreiveFeedTask();
		     task.execute();
		
	}
	
	
	class RetreiveFeedTask extends AsyncTask<Void,Void,Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			 try { 
			Message message = new MimeMessage(session);
             message.setFrom(new InternetAddress("sahaj.horus@gmail.com"));
             message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recpient));
             message.setSubject(subject);
             message.setContent(textmessage, "text/html; charset=utf-8");
 
             Transport.send(message);
              
            
          } 
          catch (MessagingException e) {
            e.printStackTrace();
             }
          catch (Exception e) {
             e.printStackTrace();
             
          }
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pdialog.dismiss();
			reciept.setText("");
			msg.setText("");
			sub.setText("");
			Toast.makeText(getApplicationContext(), "Message sent",
					Toast.LENGTH_LONG).show();
		}
	}
	 
	 

}
