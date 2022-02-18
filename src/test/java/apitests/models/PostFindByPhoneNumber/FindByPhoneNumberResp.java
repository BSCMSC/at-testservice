package apitests.models.PostFindByPhoneNumber;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "ns3:Envelope")
public class FindByPhoneNumberResp {
    @JacksonXmlProperty(isAttribute = true, localName = "xmlns:ns2")
    private String ns2;

    @JacksonXmlProperty(isAttribute = true, localName = "xmlns:ns3")
    private String ns3;

    @JsonProperty("ns2:Header")
    private HeaderDTO header;

    @JsonProperty("ns2:Body")
    private BodyDTOResp body;
}
