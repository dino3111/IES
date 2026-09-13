package pt.ua.claudino;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import pt.ua.claudino.IpmaCityForecast;
import pt.ua.claudino.IpmaService;

/**
 * demonstrates the use of the IPMA API for weather forecast
 */
public class WeatherStarter {



    public static void  main(String[] args ) {

        // get a retrofit instance, loaded with the GSon lib to convert JSON into objects
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.ipma.pt/open-data/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // create a typed interface to use the remote API (a client)
        IpmaService service = retrofit.create(IpmaService.class);
        // prepare the call to remote endpoint
        int cityId = Integer.parseInt(args[0]);
        Call<IpmaCityForecast> callSync = service.getForecastForACity(cityId);

        try {
            Response<IpmaCityForecast> apiResponse = callSync.execute();
            IpmaCityForecast forecast = apiResponse.body();

            if (forecast != null) {
                System.out.printf("Forecast for city %d:%n%n", cityId);
                for (var day : forecast.getData()) {
                    System.out.printf("Date: %s I Min: %s°C I Max: %s°C I Rain: %s%%%n",
                            day.getForecastDate(),
                            day.getTMin(),
                            day.getTMax(),
                            day.getPrecipitaProb());
                }
            } else {
                System.out.println("No results for this request!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}