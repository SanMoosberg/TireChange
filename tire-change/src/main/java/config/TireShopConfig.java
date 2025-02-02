package config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "tire-shops")
public class TireShopConfig {
    private List<TireShop> shops;

    @Data
    public static class TireShop {
        private String name;
        private String baseUrl;
        private String timeEndpoint;
        private String bookingEndpoint;
        private String format;
        private List<String> vehicleTypes;
    }
}
