package dj.sjn.djbaedal.DataClass;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface UnivFoodInterface {
    String TOKEN = "OR8OlZ5Gy5eaGL6t6VW5h4rFSEZKFHdw7IEZmcDYNKQtEVZsVt";
    String UNIVCODE = "DcniqMlreo";
    String RES1 = "MjE1Mzc0MDE2"; // 학생회관
    String RES2 = "MjE1MjgxMDAx"; // 남자기숙사
    String RES3 = "MjE1MzQzMDA5"; // 여자기숙사
    String RES4 = "MjE1MzEyMDA0"; // 교수회관

    @Headers({"accesstoken: "+TOKEN})
    @GET("openapi/v1/campuses/"+UNIVCODE+"/stores/"+RES1)
    Call<UnivFoodList> getFoodList1(@Query("date")String date);

    @Headers({"accesstoken: "+TOKEN})
    @GET("openapi/v1/campuses/"+UNIVCODE+"/stores/"+RES2)
    Call<UnivFoodList> getFoodList2(@Query("date")String date);

    @Headers({"accesstoken: "+TOKEN})
    @GET("openapi/v1/campuses/"+UNIVCODE+"/stores/"+RES3)
    Call<UnivFoodList> getFoodList3(@Query("date")String date);

    @Headers({"accesstoken: "+TOKEN})
    @GET("openapi/v1/campuses/"+UNIVCODE+"/stores/"+RES4)
    Call<UnivFoodList> getFoodList4(@Query("date")String date);

    @Headers({"accesstoken: "+TOKEN})
    @GET("openapi/v1/campuses/"+UNIVCODE+"/stores")
    Call<UnivFoodList> getUnivList(@Query("date")String date);
}
