package team_404.app_404;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

/**
 * Created by GAJAM VENU GOPAL on 1/26/2017.
 */

public class fast2sms_API {
    String token;
    String req;
    fast2sms_API(String token)
    {
        this.token=token;
        req="http://api.fast2sms.com/sms.php?token=TOKEN&mob=PHONE&mess=MSG&sender=FSTSMS&route=0";
    }
    String sendSMS(String mob,String msg)
    {
        URL url = null;
        String response="";
        try {
            url = new URL(makeURL(mob,msg));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(url);
        Scanner cin;
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();

        }
        try {
            //InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            cin=new Scanner(urlConnection.getInputStream() );
            while(cin.hasNext())response=response+cin.next();
        } catch (IOException e) {
            e.printStackTrace();
            response="!# CONNECTION ERROR";
        } finally {
            urlConnection.disconnect();
        }
        return response;
    }

    private String makeURL(String mob, String msg) throws UnsupportedEncodingException {
        String result=String.valueOf(req);
        result=result.replace("TOKEN",token).replace("PHONE", URLEncoder.encode(mob,"UTF-8")).replace("MSG",URLEncoder.encode(msg,"UTF-8"));
        System.out.println(result);
        return  result;
    }
}
