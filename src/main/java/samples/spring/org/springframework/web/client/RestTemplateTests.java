package samples.spring.org.springframework.web.client;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by izeye on 15. 1. 6..
 */
public class RestTemplateTests {

	RestTemplate restTemplate;

	@Before
	public void setUp() {
		this.restTemplate = new RestTemplate();
	}

	@Test
	public void test() throws NoSuchAlgorithmException, KeyManagementException {
		TrustManager[] trustManagers = new TrustManager[] {
			new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			}
		};
		SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(null, trustManagers, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

//		String url = "http://10.113.173.70/";
//		String url = "https://10.113.173.70/";
		String url = "https://www.izeye.com/";
		String string = restTemplate.getForObject(url, String.class);
		System.out.println(string);
	}

}
