package open.weather.api.test;

import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.DisableJmx;
import org.apache.commons.httpclient.HttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.test.context.BootstrapWith;

import open.weather.api.Application;


@RunWith(CamelSpringBootRunner.class)
@BootstrapWith(SpringBootTestContextBootstrapper.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@DisableJmx(true)
public class RestEndpointTest {

	@LocalServerPort
	int port;
	
	@Test
	public void testEndpoint() throws Exception {
		String url = "http://localhost:"+port+"/test/getWeather/72712";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet getRequest = new HttpGet(url);
		HttpResponse response = httpClient.execute(getRequest);
		System.out.println(response.getStatusLine());
		//System.out.println(port);
		//Thread.sleep(30000);
	}
	
}
