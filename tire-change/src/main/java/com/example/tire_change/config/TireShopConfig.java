package com.example.tire_change.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "tire-shops")
public class TireShopConfig {

    private List<TireShop> shops;

    public List<TireShop> getShops() {
        return shops;
    }

    public void setShops(List<TireShop> shops) {
        this.shops = shops;
    }

    public static class TireShop {
        private String name;
        private String baseUrl;
        private String timeEndpoint;
        private String bookingEndpoint;
        private String format;
        private List<String> vehicleTypes;


        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }


        public String getBaseUrl() {
            return baseUrl;
        }
        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        public String getTimeEndpoint() {
            return timeEndpoint;
        }
        public void setTimeEndpoint(String timeEndpoint) {
            this.timeEndpoint = timeEndpoint;
        }

        public String getBookingEndpoint() {
            return bookingEndpoint;
        }
        public void setBookingEndpoint(String bookingEndpoint) {
            this.bookingEndpoint = bookingEndpoint;
        }

        public String getFormat() {
            return format;
        }
        public void setFormat(String format) {
            this.format = format;
        }

        public List<String> getVehicleTypes() {
            return vehicleTypes;
        }
        public void setVehicleTypes(List<String> vehicleTypes) {
            this.vehicleTypes = vehicleTypes;
        }
    }
}
