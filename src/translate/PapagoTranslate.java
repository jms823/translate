package translate;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class PapagoTranslate {
	public static final int KO_TO_EN = 1;
	public static final int EN_TO_KO = 2;
	
	private String clientId;
	private String clientSecret;
	
	public PapagoTranslate(String clientId,String clientSecret) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}
	
	private String translate(String text,String sourceLang,String targetLang) throws IOException {
		if(clientId == null || clientSecret == null) {
			System.out.println("클라이언트 아이디나 시크릿이 입력되지 않았습니다.");
			return null;
		}
		BufferedReader br = null;
		HttpURLConnection con = null;
		try {
			text = URLEncoder.encode(text, "utf-8");
			String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
			URL url = new URL(apiURL);
			con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("X-Naver-Client-Id", clientId);
			con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
			String postParams = "source=" + sourceLang + "&target=" + targetLang + "&text=" + text;
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(postParams);
			wr.flush();
			wr.close();
			int responseCode = con.getResponseCode();
			if(responseCode==200) {
				br = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"));
			}else {
				br = new BufferedReader(new InputStreamReader(con.getErrorStream(),"utf-8"));
			}
			String inputLine;
			String firstLine = new String("br.readLine() : " + br.readLine());
			System.out.println(firstLine);
			StringBuffer response = new StringBuffer();
			while((inputLine = br.readLine()) != null) {
				System.out.println(response.append(inputLine));
			}
			br.close();
			return firstLine;
		}catch(Exception e) {
			System.out.println(e);
		}finally {
			if(br != null){
				br.close();
			}
			if(con != null) {
				con.disconnect();
			}
		}
	return clientSecret;
	}
	
	private String getTranslatedText(String text) {
		String resultText = "translatedText";
		int startIndex = text.indexOf(resultText) + resultText.length() + 3;
		int endIndex = startIndex + text.substring(startIndex).indexOf('"');
		
		return text.substring(startIndex, endIndex);
	}
	
	public String getTranslate(String text,int TranslateType) throws IOException {
		if(TranslateType==KO_TO_EN) {
			text = this.translate(text,"ko","en");
		}else if(TranslateType==EN_TO_KO) {
			text = this.translate(text,"en","ko");
		}
		text = getTranslatedText(text);
		return text;
	}
	
	public static void main(String[] args) throws IOException {
		String clientId = "FuUYp9F62Nkh6zBLlMfm";
		String clientSecret = "OQstBxNrSj";
		
		PapagoTranslate translate = new PapagoTranslate(clientId,clientSecret);
		System.out.println(translate.getTranslate("i gotto go",EN_TO_KO));
		System.out.println(translate.getTranslate("i have no money",EN_TO_KO));
	}

}
