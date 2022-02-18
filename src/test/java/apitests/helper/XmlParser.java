package apitests.helper;

import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import javax.xml.stream.XMLInputFactory;
import java.io.IOException;

public class XmlParser {

    public static XmlMapper parseXmlMessage() {
        XMLInputFactory f = new WstxInputFactory();
        f.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.FALSE);
        return new XmlMapper(new XmlFactory(f, new WstxOutputFactory()));
    }

    public static <T> T getXml(String xml, Class<T> typeReqObject) throws IOException {
        XmlMapper xmlMapper = XmlParser.parseXmlMessage();
        return xmlMapper.readValue(xml, typeReqObject);
    }
}
