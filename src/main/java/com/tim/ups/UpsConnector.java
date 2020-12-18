package com.tim.ups;

import com.alibaba.fastjson.JSON;
import com.tim.ups.utils.Base64Util;
import com.tim.ups.utils.XmlUtil;
import com.tim.ups.utils.SoapUtil;
import com.tim.ups.dto.AccountDTO;
import com.tim.ups.dto.AddressDTO;
import com.tim.ups.dto.UpsPackageDTO;


import java.util.logging.Logger;

/**
 * @author tim
 */
public class UpsConnector {


    private boolean debug = true;
    private AccountDTO accountDTO;
    private AddressDTO shipper;
    Logger log = Logger.getLogger(getClass().getName());


    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setShipper(AddressDTO shipperAddressDTO) {
        this.shipper = shipperAddressDTO;
    }

    public void init(AccountDTO upsAccountDTO, Boolean isTest) {
        if (upsAccountDTO.getKey() == null || upsAccountDTO.getUserId() == null || upsAccountDTO.getUserPwd() == null || upsAccountDTO.getUserAccountId() == null) {
            log.warning("upsAccount info error : " + upsAccountDTO.toString());
        } else {
            log.info("upsAccount info :" + upsAccountDTO.toString());
        }
        this.accountDTO = upsAccountDTO;
        setDebug(isTest);
    }


    public JSON trackingInfo(String no) {
        String url = isDebug() ? "https://wwwcie.ups.com/ups.app/xml/Track" : "https://onlinetools.ups.com/ups.app/xml/Track";
        String xml = "<?xml version=\"1.0\"?>\n" +
                "<AccessRequest>\n" +
                "    <AccessLicenseNumber>" + accountDTO.getKey() + "</AccessLicenseNumber>\n" +
                "    <Password>" + accountDTO.getUserPwd() + "</Password>\n" +
                "    <UserId>" + accountDTO.getUserId() + "</UserId>\n" +
                "</AccessRequest>\n" +
                "\n" +
                "        <?xml version=\"1.0\"?>\n" +
                "<TrackRequest>\n" +
                "<Request>\n" +
                "    <RequestAction>Track</RequestAction>\n" +
                "    <RequestOption>activity</RequestOption>\n" +
                "    <TransactionReference>\n" +
                "        <CustomerContext>Get tracking status</CustomerContext>\n" +
                "        <XpciVersion>1.0</XpciVersion>\n" +
                "    </TransactionReference>\n" +
                "</Request>\n" +
                "<TrackingNumber>" + no + "</TrackingNumber>\n" +
                "</TrackRequest>";

        return (JSON) JSON.parse(XmlUtil.xmlToJson(SoapUtil.postXml(xml, url)));
    }

    public JSON getlable(UpsPackageDTO upsPackageDTO, AddressDTO shipTo, String imgfile) throws Exception {
        if (shipper == null) {
            log.warning("Shipeer info error : " + shipper.toString());
            throw new Exception("err");
        }
        if (shipper.getCountry() != shipTo.getCountry()) {
            log.warning("ShipTo Country error : " + shipper.toString());
            throw new Exception("err");
        }
        String confirm = confirmRequest(upsPackageDTO, shipTo).toString();
        String ShipmentDigest = confirm.split("<ShipmentDigest>")[1].split("</ShipmentDigest>")[0];
        //System.out.println(ShipmentDigest);
        String accept = acceptRequest(ShipmentDigest);
        String GraphicImage = accept.split("<GraphicImage>")[1].split("</GraphicImage>")[0];
        Base64Util.decoder(GraphicImage, imgfile);
        return (JSON) JSON.parse(XmlUtil.xmlToJson(accept));
    }


    public String confirmRequest(UpsPackageDTO upsPackageDTO, AddressDTO shipTo) {
        String url = isDebug() ? "https://wwwcie.ups.com/ups.app/xml/ShipConfirm" : "https://onlinetools.ups.com/ups.app/xml/ShipConfirm";
        String xml = "<?xml version=\"1.0\"?>\n" +
                "<AccessRequest>\n" +
                "    <AccessLicenseNumber>" + accountDTO.getKey() + "</AccessLicenseNumber>\n" +
                "    <Password>" + accountDTO.getUserPwd() + "</Password>\n" +
                "    <UserId>" + accountDTO.getUserId() + "</UserId>\n" +
                "</AccessRequest>\n" +
                "\n" +
                "        <?xml version=\"1.0\"?>\n" +
                "        <ShipmentConfirmRequest>\n" +
                "  <LabelSpecification>\n" +
                "    <HTTPUserAgent>Mozilla/4.5</HTTPUserAgent>\n" +
                "    <LabelImageFormat>\n" +
                "      <Code>GIF</Code>\n" +
                "    </LabelImageFormat>\n" +
                "    <LabelPrintMethod>\n" +
                "      <Code>GIF</Code>\n" +
                "    </LabelPrintMethod>\n" +
                "    <LabelStockSize>\n" +
                "      <Height>4</Height>\n" +
                "      <Width>6</Width>\n" +
                "    </LabelStockSize>\n" +
                "  </LabelSpecification>\n" +
                "  <Request>\n" +
                "    <RequestAction>ShipConfirm</RequestAction>\n" +
                "    <RequestOption>nonvalidate</RequestOption>\n" +
                "    <TransactionReference>\n" +
                "      <CustomerContext>get new shipment</CustomerContext>\n" +
                "      <XpciVersion>1.0001</XpciVersion>\n" +
                "    </TransactionReference>\n" +
                "  </Request>\n" +
                "  <Shipment>\n" +
                "    <Package>\n" +
                "      <Dimensions>\n" +
                "        <Height>" + upsPackageDTO.getDimensionsHeight() + "</Height>\n" +
                "        <Length>" + upsPackageDTO.getDimensionsLength() + "</Length>\n" +
                "        <UnitOfMeasurement>\n" +
                "          <Code>IN</Code>\n" +
                "        </UnitOfMeasurement>\n" +
                "        <Width>" + upsPackageDTO.getDimensionsWidth() + "</Width>\n" +
                "      </Dimensions>\n" +
                "      <PackageServiceOptions></PackageServiceOptions>\n" +
                "      <PackageWeight>\n" +
                "        <UnitOfMeasurement>\n" +
                "          <Code>LBS</Code>\n" +
                "        </UnitOfMeasurement>\n" +
                "        <Weight>" + upsPackageDTO.getPackageWeight() + "</Weight>\n" +
                "      </PackageWeight>\n" +
                "      <PackagingType>\n" +
                "        <Code>02</Code>\n" +
                "      </PackagingType>\n" +
                "    </Package>\n" +
                "    <PaymentInformation>\n" +
                "      <Prepaid>\n" +
                "        <BillShipper>\n" +
                "          <AccountNumber>" + accountDTO.getUserAccountId() + "</AccountNumber>\n" +
                "        </BillShipper>\n" +
                "      </Prepaid>\n" +
                "    </PaymentInformation>\n" +
                "    <Service>\n" +
                "      <Code>03</Code>\n" +
                "      <Description>ground</Description>\n" +
                "    </Service>\n" +
                "    <ShipTo>\n" +
                "      <Address>\n" +
                "        <AddressLine1>" + shipTo.getAddress1() + "</AddressLine1>\n" +
                "        <City>" + shipTo.getCity() + "</City>\n" +
                "        <CountryCode>" + shipTo.getCountry() + "</CountryCode>\n" +
                "        <PostalCode>" + shipTo.getZip() + "</PostalCode>\n" +
                "        <StateProvinceCode>" + shipTo.getState() + "</StateProvinceCode>\n" +
                "      </Address>\n" +
                "      <AttentionName>" + shipTo.getName() + "</AttentionName>\n" +
                "      <CompanyName>" + shipTo.getName() + "</CompanyName>\n" +
                "      <PhoneNumber>" + shipTo.getPhone() + "</PhoneNumber>\n" +
                "    </ShipTo>\n" +
                "    <Shipper>\n" +
                "      <Address>\n" +
                "        <AddressLine1>" + shipper.getAddress1() + "</AddressLine1>\n" +
                "        <City>" + shipper.getCity() + "</City>\n" +
                "        <CountryCode>" + shipper.getCountry() + "</CountryCode>\n" +
                "        <PostalCode>" + shipper.getZip() + "</PostalCode>\n" +
                "        <StateProvinceCode>" + shipper.getState() + "</StateProvinceCode>\n" +
                "      </Address>\n" +
                "      <AttentionName>" + shipper.getName() + "</AttentionName>\n" +
                "      <Name>" + shipper.getName() + "</Name>\n" +
                "      <PhoneNumber>" + shipper.getPhone() + "</PhoneNumber>\n" +
                "      <ShipperNumber>" + accountDTO.getUserAccountId() + "</ShipperNumber>\n" +
                "    </Shipper>\n" +
                "  </Shipment>\n" +
                "</ShipmentConfirmRequest>";
        //System.out.println(xml);
        return SoapUtil.postXml(xml, url);
    }

    public String acceptRequest(String shipmentDigest) {
        String url = isDebug() ? "https://wwwcie.ups.com/ups.app/xml/ShipAccept" : "https://onlinetools.ups.com/ups.app/xml/ShipAccept";

        String xml = "<?xml version=\"1.0\"?>\n" +
                "<AccessRequest>\n" +
                "    <AccessLicenseNumber>" + accountDTO.getKey() + "</AccessLicenseNumber>\n" +
                "    <Password>" + accountDTO.getUserPwd() + "</Password>\n" +
                "    <UserId>" + accountDTO.getUserId() + "</UserId>\n" +
                "</AccessRequest>\n" +
                "\n" +
                "        <?xml version=\"1.0\"?>\n" +
                "        <ShipmentAcceptRequest>\n" +
                "  <Request>\n" +
                "    <RequestAction>ShipAccept</RequestAction>\n" +
                "    <TransactionReference>\n" +
                "      <CustomerContext>shipment accept reference</CustomerContext>\n" +
                "      <XpciVersion>1.0001</XpciVersion>\n" +
                "    </TransactionReference>\n" +
                "  </Request>\n" +
                "  <ShipmentDigest>" + shipmentDigest + "</ShipmentDigest>\n" +
                "</ShipmentAcceptRequest>\n";
        return SoapUtil.postXml(xml, url);
    }


}
