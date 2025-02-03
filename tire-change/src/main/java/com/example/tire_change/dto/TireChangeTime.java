package com.example.tire_change.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TireChangeTime {

    @JsonProperty("id")
    private String id;

    @JsonProperty("time")
    private String time;

    @JsonProperty("vehicleType")
    private String vehicleType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    @XmlRootElement(name = "tireChangeTimesResponse")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class XmlResponse {

        @XmlElement(name = "availableTime")
        private List<TireChangeTime> tireChangeTimes;

        public List<TireChangeTime> getTireChangeTimes() {
            return tireChangeTimes;
        }

        public void setTireChangeTimes(List<TireChangeTime> tireChangeTimes) {
            this.tireChangeTimes = tireChangeTimes;
        }

        public List<TireChangeTime> toList() {
            return tireChangeTimes;
        }
    }


    public static class JsonResponse {
        private List<TireChangeTime> availableTimes;


        public List<TireChangeTime> getAvailableTimes() {
            return availableTimes;
        }

        public void setAvailableTimes(List<TireChangeTime> availableTimes) {
            this.availableTimes = availableTimes;
        }

        public List<TireChangeTime> toList() {
            return availableTimes;
        }
    }

    public static class BookingRequest {
        private String contactInformation;

        public BookingRequest(String contactInformation) {
            this.contactInformation = contactInformation;
        }

        public String getContactInformation() {
            return contactInformation;
        }

        public void setContactInformation(String contactInformation) {
            this.contactInformation = contactInformation;
        }
    }
}
