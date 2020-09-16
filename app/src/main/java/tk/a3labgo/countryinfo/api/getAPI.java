package tk.a3labgo.countryinfo.api;
import java.util.List;

import retrofit2.http.Path;
import tk.a3labgo.countryinfo.models.Country;
import retrofit2.Call;
import retrofit2.http.GET;
public interface getAPI {
    @GET("all/")
    Call<List<Country>> getModel();

    @GET("callingcode/{code}")
    Call<List<Country>> getSpecificModel(@Path("code") String code);
}