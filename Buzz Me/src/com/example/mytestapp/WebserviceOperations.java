package com.example.mytestapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.widget.Toast;

public class WebserviceOperations {

	public void postRequest() {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(
				"http://api.yummly.com/v1/api/recipe/Avocado-cream-pasta-sauce-recipe-306039");
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("_app_id", "0825fdeb"));
		parameters.add(new BasicNameValuePair("_app_key",
				"8b06defa3399b6c1c7644d6b28bd9090"));

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(parameters));
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = httpClient.execute(httpPost,
                    responseHandler);
			//HttpResponse response = httpClient.execute(httpPost);
			//getContentFromHttpResponse(response);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getContentFromResponseBody(String responseBody) {
		try {
			JSONObject json = new JSONObject(responseBody);
			JSONArray jArray = json.getJSONArray("ingredientLines");
            ArrayList<HashMap<String, String>> mylist = 
                   new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < jArray.length(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                JSONObject e = jArray.getJSONObject(i);
                String s = e.getString("post");
                JSONObject jObject = new JSONObject(s);

                map.put("idusers", jObject.getString("idusers"));
                map.put("UserName", jObject.getString("UserName"));
                map.put("FullName", jObject.getString("FullName"));

                mylist.add(map);
            }
            //Toast.makeText(WebserviceOperations.this, responseBody, Toast.LENGTH_LONG).show();
		} catch (JSONException e) {
			e.printStackTrace();
		}	
	}
	
	public void getContentFromHttpResponse(HttpResponse httpResponse) {
		HttpEntity httpEntity = httpResponse.getEntity();
		try {
			InputStream is = httpEntity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
			}
			is.close();
			String json = sb.toString();
			JSONObject jobj = new JSONObject(json);
			String ingredientLines = (String) jobj.get("ingredientLines");
			System.out.println("************** ingredientLines =============== ");
			System.out.println(ingredientLines);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void postRequest2() {
		String text = "";
		BufferedReader reader = null;
		try {
			String api_url = "http://api.yummly.com/v1/api/recipe/Avocado-cream-pasta-sauce-recipe-306039";
			URL url = new URL(api_url);
			String data = URLEncoder.encode("_app_id", "UTF-8") + "="
					+ URLEncoder.encode("0825fdeb", "UTF-8");

			data += "&"
					+ URLEncoder.encode("_app_key", "UTF-8")
					+ "="
					+ URLEncoder.encode("8b06defa3399b6c1c7644d6b28bd9090",
							"UTF-8");
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(
					conn.getOutputStream());
			wr.write(data);
			wr.flush();

			reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;

			// Read Server Response
			while ((line = reader.readLine()) != null) {
				// Append server response in string
				sb.append(line + "\n");
			}

			text = sb.toString();

			Log.d("Details", text);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
	
	public String readTwitterFeed() {
	    StringBuilder builder = new StringBuilder();
	    HttpClient client = new DefaultHttpClient();
	    HttpGet httpGet = new HttpGet("http://twitter.com/statuses/user_timeline/vogella.json");
	    try {
	      HttpResponse response = client.execute(httpGet);
	      StatusLine statusLine = response.getStatusLine();
	      int statusCode = statusLine.getStatusCode();
	      if (statusCode == 200) {
	        HttpEntity entity = response.getEntity();
	        InputStream content = entity.getContent();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
	        String line;
	        while ((line = reader.readLine()) != null) {
	          builder.append(line);
	        }
	      } else {
	        //Log.e(ParseJSON.class.toString(), "Failed to download file");
	      }
	    } catch (ClientProtocolException e) {
	      e.printStackTrace();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	    return builder.toString();
	  }
	
	
	public void parseStaticJson() {		
//		String strJson = "{\"Android\" : [{\"song_name\":\"Gimme Dat\",\"song_id\":\"1932\",\"artist_name\":\"Sidney Samson (Feat. Pitbull & Akon)\"},{\"song_name\":\"F-k The Money (Remix)\",\"song_id\":\"73\",\"artist_name\":\"B.o.B. (Feat. Wiz Khalifa)\"}]}";
		
//		final String yummly = "{"
//            "attributes": {
//                "course": [
//                    "Soups"
//                ],
//                "cuisine": [
//                    "Italian"
//                ]
//            },
//            "flavors": {
//                "salty": 0.6666666666666666,
//                "sour": 0.8333333333333334,
//                "sweet": 0.6666666666666666,
//                "bitter": 0.5,
//                "meaty": 0.16666666666666666,
//                "piquant": 0.5
//            },
//            "rating": 4.6,
//            "id": "Vegetarian-Cabbage-Soup-Recipezaar",
//            "smallImageUrls": [],
//            "sourceDisplayName": "Food.com",
//            "totalTimeInSeconds": 4500,
//            "ingredients": [
//                "garlic cloves",
//                "ground pepper",
//                "diced tomatoes",
//                "celery",
//                "tomato juice",
//                "salt",
//                "cabbage",
//                "bell peppers",
//                "oregano",
//                "carrots",
//                "basil",
//                "vegetable broth",
//                "chili pepper flakes",
//                "green beans",
//                "onions",
//                "onion soup mix"
//            ],
//            "recipeName": "Vegetarian Cabbage Soup"
//        }"
		
        final String strJson = "{ "
        							+ "\"Android\" :"
        								+ "["
        									+ "{"
        										+ "\"song_name\":\"Gimme Dat\","
        										+ "\"song_id\":\"1932\","
        										+ "\"artist_name\":\"Sidney Samson (Feat. Pitbull & Akon)\""
                                			+  "},"
                                			+  "{" 
                                				+ "\"song_name\":\"F-k The Money (Remix)\","
                                				+ "\"song_id\":\"73\","
                                				+ "\"artist_name\":\"B.o.B. (Feat. Wiz Khalifa)\""
                                			+  "}"
                                		+ "]"
                             + " }";
		
		String OutputData = "";
		JSONObject jsonResponse;		      
		try {		      
		     jsonResponse = new JSONObject(strJson);		      
		     JSONArray jsonMainNode = jsonResponse.optJSONArray("Android");
		     int lengthJsonArr = jsonMainNode.length(); 
		
		     for(int i=0; i < lengthJsonArr; i++)
		     {
		         JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
		         int song_id        = Integer.parseInt(jsonChildNode.optString("song_id").toString());
		         String song_name   = jsonChildNode.optString("song_name").toString();
		         String artist_name = jsonChildNode.optString("artist_name").toString();
		         OutputData += "Node : \n\n     "+ song_id +" | "
		                                         + song_name +" | "
		                                         + artist_name +" \n\n ";
		         //Log.i("JSON parse", song_name);
		    }		      		
		    System.out.println(OutputData);		      
		 } catch (JSONException e) {		
		     e.printStackTrace();
		 }
	}
}
