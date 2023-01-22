package open.weather.api.route;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class CamelRoute extends RouteBuilder{

	@Override
	public void configure() throws Exception {
		//restConfiguration()//Bind the api servlet to the localhost port 8080
		  // .component("servlet").host("localhost").port(8520);
		
		rest("/getWeather/{zip}")
		.get()
		.route()
		.routeId("gets weather by zip code")
		.onException(Exception.class)
		.handled(true)
		.process(exchange->{
			Exception exception = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
			log.info("exception message: "+exception.getMessage());
			Map<Object,Object> response = new HashMap<>();
			response.put("message", exception.getMessage());
			exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 400);
			exchange.getIn().setBody(response);
			
		})
		.marshal().json(JsonLibrary.Jackson)
		.end()
		.process(exchange->{
			String[] weatherType = {"sunny", "cloudy", "rain", "snow"};
			String zipCode = exchange.getIn().getHeader("zip", String.class);
			String regex = "\\d{5}";
			if(zipCode.matches(regex)) {
				LocalDate date = LocalDate.now();
				List<Map<Object,Object>> responseList = new ArrayList<>();
				for(int i=0; i<10; i++) {
					Map<Object,Object> response = new HashMap<>();
					int random = new Random().nextInt(4);
					response.put("date", date.plusDays(i));
					response.put("day", date.plusDays(i).getDayOfWeek());
					response.put("weather", weatherType[random]);
					responseList.add(response);
				}
				exchange.getIn().setBody(responseList);
			}else {
				throw new Exception("invalid zip code");
			}
			
		})
		.marshal().json(JsonLibrary.Jackson);
		
	}

}
