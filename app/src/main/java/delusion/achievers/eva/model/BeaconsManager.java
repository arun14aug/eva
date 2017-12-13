package delusion.achievers.eva.model;

import android.app.Activity;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;
import delusion.achievers.eva.utils.EvaLog;
import delusion.achievers.eva.utils.Utils;

/*
 * Created by HP on 21-11-2017.
 */

public class BeaconsManager {

    private String TAG = BeaconsManager.class.getSimpleName();
    private ArrayList<BeaconValues> beaconValuesArrayList;
    private ArrayList<Category> categoryArrayList;
    private ArrayList<Spots> spotsArrayList;
    private ArrayList<SearchContent> searchContentArrayList = new ArrayList<>();

    public ArrayList<SearchContent> getSearchContentArrayList() {
        return searchContentArrayList;
    }

    public void setSearchContentArrayList(ArrayList<SearchContent> searchContentArrayList) {
        this.searchContentArrayList = searchContentArrayList;
    }

    public ArrayList<BeaconValues> getNames(Activity activity, boolean shouldRefresh, String url) {
        if (shouldRefresh)
            getNameList(activity, url);
        return beaconValuesArrayList;
    }

    private void getNameList(Activity activity, String url) {
        EvaLog.e("json data : ", url);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e(TAG, "onSuccess  --> " + response.toString());

                try {
                    int count = response.length();
                    beaconValuesArrayList = new ArrayList<>();
                    if (count > 0) {
                        for (int i = 0; i < count; i++) {
                            BeaconValues beaconValues = new BeaconValues();

                            beaconValues.setSpotId(response.getJSONObject(i).getString("SpotId"));
                            beaconValues.setUID(response.getJSONObject(i).getString("UID"));
                            beaconValues.setName(response.getJSONObject(i).getString("Name"));
                            beaconValues.setImagePath(response.getJSONObject(i).getString("ImagePath"));
                            beaconValues.setType(response.getJSONObject(i).getString("Type"));

                            if (response.getJSONObject(i).has("Sections")) {
                                Object item = response.getJSONObject(i).get("Sections");
                                if (item instanceof JSONArray) {
                                    JSONArray jsonArray = response.getJSONObject(i).getJSONArray("Sections");
                                    ArrayList<Sections> sectionsArrayList = new ArrayList<>();
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        Sections sections = new Sections();
                                        sections.setSectionID(jsonArray.getJSONObject(i).getString("SectionID"));
                                        sections.setSectionName(jsonArray.getJSONObject(i).getString("SectionName"));
                                        sections.setImagePath(jsonArray.getJSONObject(i).getString("ImagePath"));
                                        sectionsArrayList.add(sections);
                                    }
                                    beaconValues.setSectionsArrayList(sectionsArrayList);
                                }
                            }

                            beaconValuesArrayList.add(beaconValues);
                        }
                        EventBus.getDefault().post("GetNames True");
                    } else
                        EventBus.getDefault().post("GetNames False");


                } catch (JSONException e) {
                    e.printStackTrace();
                    EventBus.getDefault().post("GetNames False");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                EvaLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().post("GetNames False");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = "mqttuser:85wj3@321!";
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);
                return headers;
            }
        };
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonArrayRequest);
    }

    public ArrayList<Category> getCategories(Activity activity, boolean shouldRefresh, String url) {
        if (shouldRefresh)
            getCategoryList(activity, url);
        return categoryArrayList;
    }

    private void getCategoryList(Activity activity, String url) {
        EvaLog.e("json data : ", url);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e(TAG, "onSuccess  --> " + response.toString());

                try {
                    int count = response.length();
                    categoryArrayList = new ArrayList<>();
                    if (count > 0) {
                        for (int i = 0; i < count; i++) {
                            List<SubCategory> subCategoryList = new ArrayList<>();
                            if (response.getJSONObject(i).has("SubCategories")) {
                                JSONArray jsonArray = response.getJSONObject(i).getJSONArray("SubCategories");
                                if (jsonArray == null)
                                    jsonArray = new JSONArray();
                                if (jsonArray.length() > 0)
                                    for (int j = 0; j < jsonArray.length(); j++) {

                                        SearchContent searchContent = new SearchContent();
                                        searchContent.setSubCategoryID(jsonArray.getJSONObject(j).getString("SpotCategoryID"));
                                        searchContent.setCategoryID(response.getJSONObject(i).getString("SpotCategoryID"));
                                        searchContent.setSpotId(jsonArray.getJSONObject(j).getString("SpotId"));
                                        searchContent.setName(jsonArray.getJSONObject(j).getString("Name"));
                                        searchContent.setImagePath(jsonArray.getJSONObject(j).getString("ImagePath"));
                                        searchContentArrayList.add(searchContent);
                                        SubCategory subCategory = new SubCategory(jsonArray.getJSONObject(j).getString("SpotCategoryID"),
                                                jsonArray.getJSONObject(j).getString("Name"),
                                                jsonArray.getJSONObject(j).getString("SpotId"),
                                                jsonArray.getJSONObject(j).getString("ImagePath"),
                                                response.getJSONObject(i).getString("SpotCategoryID"));
                                        subCategoryList.add(subCategory);
                                    }
                            }

                            SearchContent searchContent = new SearchContent();
                            searchContent.setSpotId(response.getJSONObject(i).getString("SpotId"));
                            searchContent.setCategoryID(response.getJSONObject(i).getString("SpotCategoryID"));
                            searchContent.setName(response.getJSONObject(i).getString("Name"));
                            searchContent.setImagePath(response.getJSONObject(i).getString("ImagePath"));
                            searchContentArrayList.add(searchContent);

                            Category category = new Category(response.getJSONObject(i).getString("Name"), subCategoryList);

                            category.setSpotId(response.getJSONObject(i).getString("SpotId"));
                            category.setSpotCategoryID(response.getJSONObject(i).getString("SpotCategoryID"));
                            category.setName(response.getJSONObject(i).getString("Name"));
                            category.setImagePath(response.getJSONObject(i).getString("ImagePath"));

                            categoryArrayList.add(category);
                        }

                        setSearchContentArrayList(searchContentArrayList);

                        EventBus.getDefault().post("GetCategory True");
                    } else
                        EventBus.getDefault().post("GetCategory False");


                } catch (JSONException e) {
                    e.printStackTrace();
                    EventBus.getDefault().post("GetCategory False");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                EvaLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().post("GetCategory False");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = "mqttuser:85wj3@321!";
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);
                return headers;
            }
        };
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonArrayRequest);
    }

    public ArrayList<Spots> getSpots(Activity activity, boolean shouldRefresh, String url) {
        if (shouldRefresh)
            getSpotList(activity, url);
        return spotsArrayList;
    }

    private void getSpotList(Activity activity, String url) {
        EvaLog.e("json data : ", url);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e(TAG, "onSuccess  --> " + response.toString());

                try {
                    int count = response.length();
                    spotsArrayList = new ArrayList<>();
                    if (count > 0) {
                        for (int i = 0; i < count; i++) {
                            Spots spots = new Spots();

                            spots.setId(response.getJSONObject(i).getString("Id"));
                            spots.setShopName(response.getJSONObject(i).getString("ShopName"));
                            spots.setAddressLine1(response.getJSONObject(i).getString("AddressLine1"));
                            spots.setAddressLine2(response.getJSONObject(i).getString("AddressLine2"));
                            spots.setCity(response.getJSONObject(i).getString("City"));
                            spots.setState(response.getJSONObject(i).getString("State"));
                            spots.setPincode(response.getJSONObject(i).getString("Pincode"));
                            spots.setCountry(response.getJSONObject(i).getString("Country"));
                            spots.setMobileNo1(response.getJSONObject(i).getString("MobileNo1"));
                            spots.setMobileNo2(response.getJSONObject(i).getString("MobileNo2"));
                            spots.setMobileNo3(response.getJSONObject(i).getString("MobileNo3"));
                            spots.setEmail(response.getJSONObject(i).getString("Email"));
                            spots.setEmail1(response.getJSONObject(i).getString("Email1"));
                            spots.setSEO(response.getJSONObject(i).getString("SEO"));

                            spotsArrayList.add(spots);
                        }
                        EventBus.getDefault().post("GetSpots True");
                    } else
                        EventBus.getDefault().post("GetSpots False");


                } catch (JSONException e) {
                    e.printStackTrace();
                    EventBus.getDefault().post("GetSpots False");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                EvaLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().post("GetSpots False");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = "mqttuser:85wj3@321!";
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);
                return headers;
            }
        };
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonArrayRequest);
    }

}
