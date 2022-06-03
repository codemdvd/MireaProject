package ru.mirea.apasov.mireaproject;

//import android.app.Fragment;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import ru.mirea.apasov.mireaproject.databinding.FragmentNewSettingsBinding;
import ru.mirea.apasov.mireaproject.databinding.FragmentWeatherAPIBinding;

public class WeatherAPI extends Fragment {
    private FragmentWeatherAPIBinding binding;
    private TextView resultTextViewWeather;
    private TextView resultTextViewCity;
    private TextView resultTextViewWind;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ru.mirea.apasov.mireaproject.databinding.FragmentWeatherAPIBinding binding = FragmentWeatherAPIBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        resultTextViewCity= binding.textView1;
        resultTextViewWeather= binding.textView2;
        resultTextViewWind= binding.textView3;
        binding.button13.setOnClickListener(this::onClick);

        return root;
    }

    public void onClick(View view) {

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = null;
        if (connectivityManager != null) {
            networkinfo = connectivityManager.getActiveNetworkInfo();
        }
        if (networkinfo != null && networkinfo.isConnected()) {
            AsyncTask<String, Void, String> getIpTask = new DownloadPageTask().execute("http://api.weatherapi.com/v1/current.json?key=102fb201d3ca461a8f7110349221705&q=Moscow&aqi=no");
            try {
                getIpTask.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), "Нет интернета", Toast.LENGTH_SHORT).show();
        }
    }
    private class DownloadPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            resultTextViewWeather.setText("Загружаем...");
            resultTextViewCity.setText("Загружаем...");
            resultTextViewWind.setText("Загружаем...");
        }
        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadIpInfo(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }
        @Override
        protected void onPostExecute(String result) {
            Log.d(MainActivity.class.getSimpleName(), result);
            try {
                JSONObject responseJson = new JSONObject(result);
                JSONObject object = responseJson.getJSONObject("location");
                JSONObject object2 = responseJson.getJSONObject("current");

                String city = object.getString("name");
                double weather = object2.getDouble("temp_c");
                double wind = object2.getDouble("wind_kph");

                resultTextViewCity.setText("City: " + city);
                resultTextViewWeather.setText("Weather in C: " + weather);
                resultTextViewWind.setText("Wind km/h: " + wind);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
    }
    private String downloadIpInfo(String address) throws IOException {
        InputStream inputStream = null;
        String data = "";
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setReadTimeout(100000);
            connection.setConnectTimeout(100000);
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
                inputStream = connection.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int read = 0;
                while ((read = inputStream.read()) != -1) {
                    bos.write(read);
                }
                bos.close();
                data = bos.toString();
            } else {
                data = connection.getResponseMessage() + " . Error Code : " + responseCode;
            }
            connection.disconnect();
            //return data;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return data;
    }
}