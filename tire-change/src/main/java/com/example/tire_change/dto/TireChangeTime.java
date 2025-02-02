package dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;

import java.util.List;

@Data
public class TireChangeTime {
    private String id;
    private String time;
    private String vehicleType;

    @Data
    @XmlRootElement(name = "TireChangeTimes")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class XmlResponse {
        @XmlElement(name = "TireChangeTime")
        private List<TireChangeTime> tireChangeTimes;

        public List<TireChangeTime> toList() {
            return tireChangeTimes;
        }
    }

    @Data
    public static class BookingRequest {
        private String contactInformation;

        public BookingRequest(String contactInformation) {
            this.contactInformation = contactInformation;
        }
    }
}