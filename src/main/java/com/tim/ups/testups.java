package com.tim.ups;

import com.tim.ups.dto.AccountDTO;
import com.tim.ups.dto.AddressDTO;
import com.tim.ups.dto.UpsPackageDTO;

public class testups {

    public static void main(String[] args) throws Exception {

        UpsConnector ups = new UpsConnector();
        AccountDTO account = new AccountDTO();
        account.setKey("AD8F1CEFE623BA15");
        account.setUserId("xxxxxx");
        account.setUserPwd("xxxxxx");
        account.setUserAccountId("xxxxxx");
        ups.init(account, true);

        UpsPackageDTO upsPackageDTO = new UpsPackageDTO();
        upsPackageDTO.setDimensionsHeight("9");
        upsPackageDTO.setDimensionsLength("1");
        upsPackageDTO.setDimensionsWidth("4");
        upsPackageDTO.setPackageWeight("40");

        AddressDTO shipFrom = new AddressDTO();
        shipFrom.setAddress1("425 S PINE ST");
        shipFrom.setCity("San Gabriel");
        shipFrom.setCountry("US");
        shipFrom.setZip("91776");
        shipFrom.setState("CA");
        shipFrom.setName("xxxxxx");
        shipFrom.setPhone("xxxxxx");
        ups.setShipper(shipFrom);

        AddressDTO shipTo = new AddressDTO();
        shipTo.setAddress1("1600 Pennsylvania Ave");
        shipTo.setCity("Washington");
        shipTo.setCountry("US");
        shipTo.setZip("20500");
        shipTo.setState("DC");
        shipTo.setName("Google");
        shipTo.setPhone("2024561111");





        System.out.println(ups.getlable(upsPackageDTO,shipTo,"label.jpg"));
        //System.out.println(ups.trackingInfo("1Z8339V50332234089"));
        //System.out.println(ups.trackingInfo("aaaaaa"));
    }
}
