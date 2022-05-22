package com.qa.util;

import com.qa.base.TestBase;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class emailUtils extends TestBase {

    public emailUtils() {
        super();
    }

    public static String startMail(String hostname, String smtpServer, String mailTO, String mailCC, String suiteName, String environment) {

        String mailResponse = sendMail(smtpServer, mailTO, mailCC, suiteName + " Execution Started",
                GetStartMailBody(hostname, suiteName, environment)
                , "");

        return mailResponse;
    }


    public static String endMail(String hostname, String smtpServer, String mailTO, String mailCC, String suiteName, String environment, int executeCount, int passCount, int failCount, int inconclusiveCount, int executionTime, String attachmentPath) {

        String mailResponse = sendMail(smtpServer, mailTO, mailCC, suiteName + " Execution Finished",
                GetEndMailBody(hostname, suiteName, environment, executeCount, passCount, failCount, inconclusiveCount, executionTime)
                , attachmentPath);

        return mailResponse;
    }


    private static String GetStartMailBody(String hostname, String suiteName, String environment) {
        String bodyStartMail =
                "<table class=MsoNormalTable border=1 cellpadding=0 width=585 style='width:438.75pt;background:#F5FFEB;border:solid lightslategray 1.0pt'>"
                        + "<tr>"
                        + "<td style='border:none;padding:3.75pt 3.75pt 3.75pt 3.75pt'>"
                        + "<p class=MsoNormal>"
                        + "<span style='font-size:9.5pt;font-family:\"Arial\",\"sans-serif\";color:#444444'>"
                        + "<img id=\"_x0000_i1025\" src=\"https://pub.rebit.org.in/inline-images/banner-logo.png\"><o:p></o:p></span></p></td></tr>"
                        //+"<tr>"
                        //+"<td style=\'border:none;padding:3.75pt 3.75pt 3.75pt 3.75pt'"
                        //+"<p class=MsoNormal>Test Suite Execution Started </p></td></tr>"
                        + "<tr>"
                        + "<td style='border:none;padding:3.75pt 3.75pt 3.75pt 3.75pt'>"
                        + "<p>"
                        + "<b><span style='font-size:9.5pt;font-family:\"Arial\",\"sans-serif\";color:#444444'>Suite Name: </span></b>"
                        + "<span style='font-size:9.5pt;font-family:\"Arial\",\"sans-serif\";color:#444444'>" + suiteName + "</span>"
                        + "</p>"
                        + "<p>"
                        + "<b><span style='font-size:9.5pt;font-family:\"Arial\",\"sans-serif\";color:#444444'>Machine Name: </span></b>"
                        + "<span style='font-size:9.5pt;font-family:\"Arial\",\"sans-serif\";color:#444444'>" + hostname + "</span>"
                        + "</p>"
                        + "<p>"
                        + "<b><span style='font-size:9.5pt;font-family:\"Arial\",\"sans-serif\";color:#444444'>Environment: </span></b>"
                        + "<em><span style='font-size:9.5pt;font-family:\"Arial\",\"sans-serif\";color:#444444'>" + environment + "</span></em>"
                        + "</p>"
                        + "<span style='font-size:9.5pt;font-family:\"Arial\",\"sans-serif\";color:#444444'> <o:p></o:p></span>"
                        + "</td></tr>"
                        + "</table>";

        return bodyStartMail;
    }


    private static String GetEndMailBody(String hostname, String suiteName,String environment, int executeCount, int passCount, int failCount, int inconclusiveCount, int executionTime)
    {

        String bodyEndMail = "<table class=MsoNormalTable border=1 cellpadding=0 width=585 style='width:438.75pt;background:#F5FFEB;border:solid lightslategray 1.0pt'>"
                + "<tr>"
                + "<td style='border:none;padding:3.75pt 3.75pt 3.75pt 3.75pt'>"
                + "<p class=MsoNormal>"
                + "<span style='font-size:9.5pt;font-family:\"Arial\",\"sans-serif\";color:#444444'>"
                + "<img id=\"_x0000_i1025\" src=\"https://pub.rebit.org.in/inline-images/banner-logo.png\"><o:p></o:p></span></p></td></tr>"
                + "<tr>"
                + "<tr>"
                + "<td style='border:none;padding:3.75pt 3.75pt 3.75pt 3.75pt'>"
                + "<p>"
                + "<b><span style='font-size:9.5pt;font-family:\"Arial\",\"sans-serif\";color:#444444'>Suite Name: </span></b>"
                + "<span style='font-size:9.5pt;font-family:\"Arial\",\"sans-serif\";color:#444444'>" + suiteName + "</span>"
                + "<span style='font-size:9.5pt;font-family:\"Arial\",\"sans-serif\";color:#444444'> <o:p></o:p></span>"
                + "</p>"
                + "</td></tr>"

                + "<tr>"
                + "<td style='border:none;padding:3.75pt 3.75pt 3.75pt 3.75pt'>"
                + "<p>"
                + "<b><span style='font-size:9.5pt;font-family:\"Arial\",\"sans-serif\";color:#444444'>Environment: </span></b>"
                + "<span style='font-size:9.5pt;font-family:\"Arial\",\"sans-serif\";color:#444444'>" + environment + "</span>"
                + "<span style='font-size:9.5pt;font-family:\"Arial\",\"sans-serif\";color:#444444'> <o:p></o:p></span>"
                + "</p>"
                + "</td></tr>"

                + "<tr>"
                + "<td style='border:none;padding:3.75pt 3.75pt 3.75pt 3.75pt'>"
                + "<p class=MsoNormal><b><span style='font-size:9.5pt;font-family:\"Arial\",\"sans-serif\";color:#444444'>Ran on: </span></b>"
                + "<span style='font-size:9.5pt;font-family:\"Arial\",\"sans-serif\";color:#444444'>" + hostname + "<o:p></o:p></span>"
                + "</p>"
                + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style='border:none;padding:3.75pt 3.75pt 3.75pt 3.75pt'>"
                + "<p class=MsoNormal><b><span style='font-size:9.5pt;font-family:\"Arial\",\"sans-serif\";color:#444444'>Test Case Results Summary:</b>         Executed = <b>" + executeCount + "</b> | Passed = <b>" + passCount + "</b> | Failed = <b>" + failCount + "</b> | Inconclusive = <b>" + inconclusiveCount + "</b></span>"
                //+ "<span style='font-size:9.5pt;font-family:\"Arial\",\"sans-serif\";color:#444444'><o:p></o:p></span>"
                //+ "<p class=MsoNormal style='margin-left:.5in'><span style='font-size:9.5pt;font-family:\"Arial\",\"sans-serif\";color:#444444'>Passed = <b>" + passCount + "</b> | Failed = <b>" + failCount + "</b> | Inconclusive = <b>" + inconclusiveCount + "</b><o:p></o:p></span></p>"
                + "</p>"
                + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style='border:none;padding:3.75pt 3.75pt 3.75pt 3.75pt'>"
                + "<p class=MsoNormal><b><span style='font-size:9.5pt;font-family:\"Arial\",\"sans-serif\";color:#444444'>Execution Time: </span></b>"
                + "<span style='font-size:9.5pt;font-family:\"Arial\",\"sans-serif\";color:#444444'>" + Math.round(((double)executionTime) / 60) + " Minutes <o:p></o:p></span>"
                + "</p>"
                + "</td>"
                + "</tr>"
                + "<tr>"
                + "<td style='border:none;padding:3.75pt 3.75pt 3.75pt 3.75pt'>"
                + "</td>"
                + "</tr>"
                + "</table>";


        return bodyEndMail;
    }


    private static String sendMail(String smtpServer, String to, String cc, String subject, String strBody, String attachmentPath) {

        try {

            String from = globalVariable.get("emailFrom");
            String password = globalVariable.get("emailPassword");

            if (to == null || to == "")
            {
                to = from;
                strBody = "<p style='color:RED;'>This email has been sent to vijaycreative@gmail.com because no address was specified in the properties file.</p><br/><br/>" + strBody;
            }

//            Properties properties = System.getProperties();
//            properties.setProperty("mail.smtp.host", smtpServer);
//            properties.setProperty("mail.smtp.starttls.enable", "true");
//            properties.setProperty("mail.user", from);
//            properties.setProperty("mail.password", password);

            Properties properties = new Properties();
            properties.put("mail.smtp.host", smtpServer);
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.user", from);
            properties.put("mail.password", password);

            Session session = Session.getDefaultInstance(properties,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(from,password);
                        }
                    });

            MimeMessage mail = new MimeMessage(session);
            mail.setFrom(new InternetAddress(from));
            Address toAddress;

            if (to.indexOf(',') >= 0)
            {
                for (String toMail : to.split(","))
                {
                    toAddress = new InternetAddress(toMail);
                    mail.addRecipient(Message.RecipientType.CC,toAddress);
                }
            }
            else
            {
                toAddress=new InternetAddress(to);
                mail.addRecipient(Message.RecipientType.TO,toAddress);
            }

            if (cc != null && cc != "" && !cc.isEmpty())
            {
                Address copyTo;

                //Check for Multiple Addresses
                if (cc.indexOf(',') >= 0)
                {
                    for (String ccMail : cc.split(","))
                    {
                        copyTo = new InternetAddress(ccMail);
                        mail.addRecipient(Message.RecipientType.CC,copyTo);
                    }
                }
                else
                {
                    copyTo = new InternetAddress(cc);
                    mail.addRecipient(Message.RecipientType.CC,copyTo);
                }
            }

            mail.setSubject(subject);

            MimeBodyPart messagePart = new MimeBodyPart();
            //messagePart.setText(strBody);
            messagePart.setContent(strBody,"text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messagePart);

            if(attachmentPath != "" && !(attachmentPath.isEmpty())) {
                MimeBodyPart imagePart = new MimeBodyPart();
                imagePart.setHeader("Content-ID", "pVk82x");
                imagePart.setDisposition(MimeBodyPart.INLINE);
                imagePart.attachFile(attachmentPath);
                multipart.addBodyPart(imagePart);
            }

            mail.setContent(multipart);
            Transport.send(mail);
            return "SUCCESS";
        }
        catch (Exception e) {
            e.printStackTrace();
            return ("ERROR: " + e.getMessage()+"\n"+e.getCause().toString());
        }
    }
}

