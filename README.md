# Upsconnector
Ups Lable print,Tracking API Sample



#UPS面单打印、跟踪封装、地址验证
采用http+xml拼接方式替代官方SOAP代码
更简单轻量级。

```Java
        UpsConnector ups = new UpsConnector();
        AccountDTO account = new AccountDTO();
        account.setKey("XXXXXXXXX");  //KEY
        account.setUserId("xxxxxx");  //LOGIN USERID
        account.setUserPwd("xxxxxx"); //LOGIN USER PWD
        account.setUserAccountId("xxxxxx");  //ACCOUNT ID OPTIONAL ,ONLY FOR PRINT LABEL
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


        //addressValidation
        //System.out.println(ups.isAddressValidation(shipTo)); 
        //print Label
        System.out.println(ups.getlable(upsPackageDTO,shipTo,"label.jpg"));
        //tacking
        //System.out.println(ups.trackingInfo("1Z8339V50332234089"));
 ```
