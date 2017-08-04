package team_404.app_404;

import java.util.ArrayList;

/**
 * Created by GAJAM VENU GOPAL on 1/27/2017.
 */

public class Utility {

    static String prefix="Change_my_number_from_";
    static String isIndianNumber(String mob)
    {
        mob=mob.replace(" ","")
                .replace("-","")
                .replace("+","")
                .replace("*","")
                .replace("(","")
                .replace(")","")
                .replace(".","")
                .replace("/","")
                .replace(",","")
                .replace(":","")
                .replace(";","")
                .replace("#","");
        if(mob.length()==12&&mob.startsWith("91"))return mob.substring(2);
        else if(mob.length()==10)return mob;
        else return "!# ERROR IN MOBILE NUMBER";
    }
    static String phoneNumberTrim(String mob)
    {
        mob=mob.replace(" ","")
                .replace("-","")
                .replace("+","")
                .replace("*","")
                .replace("(","")
                .replace(")","")
                .replace(".","")
                .replace("/","")
                .replace(",","")
                .replace(":","")
                .replace(";","")
                .replace("#","");
        return mob;
    }
}
