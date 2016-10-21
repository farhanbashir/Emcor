package com.example.fbashir.emcor.interfaces;

import com.example.fbashir.emcor.models.CommonResponse;
import com.example.fbashir.emcor.models.EmcorPages;
import com.example.fbashir.emcor.models.appConfigurations.AppConfigurationClass;
import com.example.fbashir.emcor.models.businessService.BusinessServiceClass;
import com.example.fbashir.emcor.models.businessServiceDetails.BusinessServiceDetailsClass;
import com.example.fbashir.emcor.models.companies.CompaniesClass;
import com.example.fbashir.emcor.models.companyDetails.CompanyDetailsClass;
import com.example.fbashir.emcor.models.contactUs.ContactUsClass;
import com.example.fbashir.emcor.models.divisions.DivisionsClass;
import com.example.fbashir.emcor.models.startup.StartupClass;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by fbashir on 8/2/2016.
 */

public interface EmcorPagesAPI {
    //String END_POINT = "http://www.technyxsystems.com/demo/emcor_profile_book/api/";

    @GET("getPages")
    Call<CommonResponse> getPages();

//    @POST("getCompanies")
//    Call<EmcorPages> getCompanies();

    @POST("getBusinessServiceInfo")
    Call<BusinessServiceClass> getBusinessServiceInfo();

    @POST("getBusinessServiceDetails")
    Call<BusinessServiceDetailsClass> getBusinessServiceDetails();

    @POST("getDivisoins")
    Call<DivisionsClass> getDivisoins();

    @POST("startup")
    Call<StartupClass> startup();

    @FormUrlEncoded
    @POST("getCompanies")
    Call<CompaniesClass> getCompanies(@Field("division_id") String division_id,
                                      @Field("location_latitide") String location_latitide,
                                      @Field("location_longitude") String location_longitude);

    @FormUrlEncoded
    @POST("getCompanyDetails")
    Call<CompanyDetailsClass> getCompanyDetails(@Field("company_id") String company_id);

    @FormUrlEncoded
    @POST("contactUs")
    Call<ContactUsClass> contactUs(@Field("first_name") String first_name,
                                   @Field("last_name") String last_name,
                                   @Field("email") String email,
                                   @Field("contact_number") String contact_number,
                                   @Field("message") String message);

    @POST("getAppConfigurations")
    Call<AppConfigurationClass> getAppConfigurations();

    @Streaming
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);

}
