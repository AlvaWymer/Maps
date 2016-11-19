package com.express.util;

import java.util.Date;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.express.model.MySecurity;

public class SendSimpleMail {
	
	//更改密码密码时发送邮件
	public static  String updatepasswordtomail(String name,String tomail,String newpassword)  {
		try {
			InternetAddress[] address = null;
			String mailserver = "yangnaihua.cn";
			String from = "admin@yangnaihua.cn";
			//String to = "yangnaihua.2008@163.com";
			String subject = "程序猿物流网提醒您的密码已更改";
			String messageText = "亲爱的" +name+ "用户,您刚刚在程序猿物流网更改了您的密码，修改之后的密码是：'"+newpassword+"',请您妥善保护好您的密码！";
			java.util.Properties props = System.getProperties();
			props.put("mail.smtp.host", mailserver);
			props.put("mail.smtp.auth", "true");
			MySecurity msec = new MySecurity("admin", "admin");
			Session mailSession = Session.getDefaultInstance(props, msec);
			mailSession.setDebug(false);
			Message msg = new MimeMessage(mailSession);// 创建文件信息
			msg.setFrom(new InternetAddress(from)); // 设置传送邮件的发信人
			address = InternetAddress.parse(tomail, false); // 指定收信人的信箱
			msg.setRecipients(Message.RecipientType.TO, address); // 向指定邮箱发送
			msg.setSubject(subject);
			msg.setSentDate(new Date()); // 立刻发送
			msg.setText(messageText); // 发送的内容
			Transport.send(msg, msg.getAllRecipients());
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return null;
	}
	/*public static void main(String[] args) throws Exception {
		InternetAddress[] address = null;
		String mailserver = "yangnaihua.cn";
		String from = "admin@yangnaihua.cn";
		String to = "yangnaihua.2008@163.com";
		String subject = "Hello！！！";
		String messageText = "杨乃华" + "杨乃华";
		java.util.Properties props = System.getProperties();
		props.put("mail.smtp.host", mailserver);
		props.put("mail.smtp.auth", "true");
		MySecurity msec = new MySecurity("admin", "admin");
		Session mailSession = Session.getDefaultInstance(props, msec);
		mailSession.setDebug(false);
		Message msg = new MimeMessage(mailSession);// 创建文件信息
		msg.setFrom(new InternetAddress(from)); // 设置传送邮件的发信人
		address = InternetAddress.parse(to, false); // 指定收信人的信箱
		msg.setRecipients(Message.RecipientType.TO, address); // 向指定邮箱发送
		msg.setSubject(subject);
		msg.setSentDate(new Date()); // 立刻发送
		msg.setText(messageText); // 发送的内容
		Transport.send(msg, msg.getAllRecipients());
	}
*/
}
