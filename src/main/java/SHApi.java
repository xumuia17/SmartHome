import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;


import static spark.Spark.*;

public class SHApi {
    private static final String IN_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";
    private static final String OUT_DATE_FORMAT = "HH:mm";
    public static void main(String[] args){

        get("/sunrise", (req,res)->getSunrise());
        get("/synchronize", (req,res)->"0,4,1");
    }


    public static String getSunrise() throws IOException{
        String resultJson = getSunriseApi();
        JSONObject json = new JSONObject(resultJson);
        System.out.println(resultJson);
        String sunriseTime =json.getJSONObject("results").getString("sunrise");
        System.out.println(sunriseTime);
        return getSunriseInCurrentUTC(sunriseTime);
    }
    public static String getSunriseApi () throws IOException {
        String url ="https://api.sunrise-sunset.org/json?lat=51.76175102&lng=55.10937989&formatted=0";
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);
        HttpResponse response = client.execute(request);
        String responseJSON = EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8"));
        return responseJSON;
    }
    public static String getSunriseInCurrentUTC(String timeInDefaultUTC) {
        SimpleDateFormat parser = new SimpleDateFormat(IN_DATE_FORMAT);
        Date date = new Date();
        try{
            date = parser.parse(timeInDefaultUTC);
        }
        catch (Exception e){
            System.out.println("Can't parse date");
        }
        SimpleDateFormat formatter = new SimpleDateFormat(OUT_DATE_FORMAT);
        String formattedDate = formatter.format(date);
        return formattedDate;
    }

}
