package mainpackage;

import java.io.*;
import java.net.*;


public class MainClass {
	
	public static boolean sendRequest(String request) throws IOException
	{
		URL url;
		HttpURLConnection connection;
		
		
		url = new URL("https://dictionary.yandex.net/api/v1/dicservice/lookup?key=dict.1.1.20140408T160401Z.2ee6e857eb14cecd.e19602ba5827476884c6e2c7b1d9d95fc1f1fede&lang=en-en&text=" + URLEncoder.encode(request, "UTF-8"));
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		
		connection.setUseCaches(false);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		
		
		
		InputStream in = connection.getInputStream();
		BufferedReader read = new BufferedReader(new InputStreamReader(in));
		
		
		StringBuffer result = new StringBuffer();
		
		while ( read.ready())
		{
			result.append(read.readLine());
		}
		read.close();
		
		if (connection != null) connection.disconnect();
		String [] splitet = result.toString().split(">");
		
		if (splitet.length > 10) return true; else return false;
		
	}
}
