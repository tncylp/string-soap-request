
import javax.xml.soap.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {

    public static void main(String[] args) throws SOAPException, IOException, TransformerConfigurationException {

        soapResponder("Tuncay", "Alp");

    }


    public static void soapResponder(String param1, String param2) throws SOAPException, IOException {

        String soapRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns1=\"http://www.SoapClient.com/xml/SoapResponder.xsd\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\" SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">\n" +
                "  <SOAP-ENV:Body>\n" +
                "    <ns1:Method1>\n" +
                "      <bstrParam1 xsi:type=\"xsd:string\">" + param1 + "</bstrParam1>\n" +
                "      <bstrParam2 xsi:type=\"xsd:string\">" + param2 + "</bstrParam2>\n" +
                "    </ns1:Method1>\n" +
                "  </SOAP-ENV:Body>\n" +
                "</SOAP-ENV:Envelope>";

        SOAPConnectionFactory sfc = SOAPConnectionFactory.newInstance();
        SOAPConnection connection = sfc.createConnection();
        InputStream is = new ByteArrayInputStream(soapRequest.getBytes());
        SOAPMessage request = MessageFactory.newInstance().createMessage(null, is);

        request.writeTo(System.out);

        URL endpoint = new URL("http://soapclient.com/xml/soapresponder.wsdl");
        SOAPMessage response = connection.call(request, endpoint);

        seperator();

        System.out.println(getSOAPMessageAsString(response));

        seperator();

        String strResponse = getSOAPMessageAsString(response);
        String expected = "Your input parameters are Tuncay and Alp";
        String matched = "";

        Pattern p = Pattern.compile("(?<=<bstrReturn xsi:type=\"xsd:string\">)(.*)(?=<\\/bstrReturn>)");
        Matcher m = p.matcher(strResponse);

        if (m.find()){
            matched = m.group();
        }


        if (expected.equalsIgnoreCase(matched))
            System.out.println("SUCCESS");
        else
            System.out.println("FAIL");



    }


    public static String getSOAPMessageAsString(SOAPMessage soapMessage) {
        try {

            TransformerFactory tff = TransformerFactory.newInstance();
            Transformer tf = tff.newTransformer();

            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","2");

            Source sc = soapMessage.getSOAPPart().getContent();

            ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(streamOut);
            tf.transform(sc, result);

            String strMessage = streamOut.toString();
            return strMessage;
        } catch (Exception e) {
            System.out.println("Exception in getSOAPMessageAsString "
                    + e.getMessage());
            return null;
        }

    }

    public static void seperator(){
        System.out.println();
        System.out.println();
        System.out.println("---------------------------------------------");
        System.out.println("---------------------------------------------");
        System.out.println();
        System.out.println();
    }

}
