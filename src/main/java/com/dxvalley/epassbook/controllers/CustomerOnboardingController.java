package com.dxvalley.epassbook.controllers;

import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.json.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customeronboarding")
public class CustomerOnboardingController {

    @PostMapping("/createcustomer")
    public ResponseEntity<?> createCustomer(@RequestBody CreateCustomerReq req) {
        try {
            String url = "http://10.1.245.150:7081/v1/cbo/";
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String messageId = Integer.toString((int) (Math.random() * (10000000 - 1000000 + 1) + 1000000));
            String reqBody = "{\n" +
                    "    \"CreateCustomerRequest\": {\n" +
                    "        \"ESBHeader\": {\n" +
                    "            \"serviceCode\": \"180000\",\n" +
                    "            \"channel\": \"USSD\",\n" +
                    "            \"Service_name\": \"CreateCustomer\",\n" +
                    "            \"Message_Id\": \"EPAS" + messageId + "\"\n" +
                    "        },\n" +
                    "        \"OfsFunction\": {\n" +
                    "            \"messageId\": \"EPAS" + messageId + "\"\n" +
                    "        },\n" +
                    "        \"CUSTOMERETMBINPUTKYCOBType\": {\n" +
                    "            \"Mnemonic\": \"" + req.getMnemonic() + "\",\n" +
                    "            \"gSHORTNAME\": {\n" +
                    "                \"ShortName\": \"" + req.getShortName() + "\"\n" +
                    "            },\n" +
                    "            \"gNAME1\": {\n" +
                    "                \"FullName\": \"" + req.getFullName() + "\"\n" +
                    "            },\n" +
                    "            \"gNAME2\": {\n" +
                    "                \"FullName2\": \"" + req.getFullName2() + "\"\n" +
                    "            },\n" +
                    "            \"gSTREET\": {\n" +
                    "                \"ZoneSubcity\": \"" + req.getZoneSubcity() + "\"\n" +
                    "            },\n" +
                    "            \"gLLADDRESS\": {\n" +
                    "                \"mLLADDRESS\": {\n" +
                    "                    \"sgLLADDRESS\": {\n" +
                    "                        \"WoredaKebeleHouseNO\": {\n" +
                    "                            \"WoredaKebeleHouseNO\": \"" + req.getWoredaKebeleHouseNO() + "\"\n" +
                    "                        }\n" +
                    "                    }\n" +
                    "                }\n" +
                    "            },\n" +
                    "            \"gTOWNCOUNTRY\": {\n" +
                    "                \"TownCity\": \"" + req.getTownCity() + "\"\n" +
                    "            },\n" +
                    "            \"gPOSTCODE\": {\n" +
                    "                \"PostCode\": " + req.getPostCode() + "\n" +
                    "            },\n" +
                    "            \"gCOUNTRY\": {\n" +
                    "                \"Country\": \"" + req.getCountry() + "\"\n" +
                    "            },\n" +
                    "            \"Sector\": " + req.getSector() + ",\n" +
                    "            \"AccountOfficer\": \"\",\n" +
                    "            \"gOTHEROFFICER\": {\n" +
                    "                \"SecondOfficer\": \"" + req.getSecondOfficer() + "\"\n" +
                    "            },\n" +
                    "            \"Industry\": " + req.getIndustry() + ",\n" +
                    "            \"Target\": " + req.getTarget() + ",\n" +
                    "            \"Nationality\": \"" + req.getNationality() + "\",\n" +
                    "            \"CustomerStatus\": " + req.getCustomerStatus() + ",\n" +
                    "            \"Residence\": \"" + req.getResidence() + "\",\n" +
                    "            \"CONTACTDATE\": \"\",\n" +
                    "            \"INTRODUCER\": \"\",\n" +
                    "            \"gLEGALID\": {\n" +
                    "                \"mLEGALID\": {\n" +
                    "                    \"LegalIDNumber\": " + req.getLegalIDNumber() + ",\n" +
                    "                    \"DocumentName\": \"" + req.getDocumentName() + "\",\n" +
                    "                    \"NameonID\": \"" + req.getNameonID() + "\",\n" +
                    "                    \"IssueAuthority\": \"" + req.getIssueAuthority() + "\",\n" +
                    "                    \"IssueDate\": " + req.getIssueDate() + ",\n" +
                    "                    \"ExpirationDate\": " + req.getExpirationDate() + "\n" +
                    "                }\n" +
                    "            },\n" +
                    "            \"gOFFPHONE\": {\n" +
                    "                \"PhoneNumbersOff\": " + req.getPhoneNumbersOff() + "\n" +
                    "            },\n" +
                    "            \"CustomerLiability\": \"" + req.getCustomerLiability() + "\",\n" +
                    "            \"Language\": \"" + req.getLanguage() + "\",\n" +
                    "            \"COMPANYBOOK\": \"" + req.getCOMPANYBOOK() + "\",\n" +
                    "            \"CONFIDTXT\": \"" + req.getCONFIDTXT() + "\",\n" +
                    "            \"ISSUECHEQUES\": \"" + req.getISSUECHEQUES() + "\",\n" +
                    "            \"NOUPDATECRM\": \"" + req.getNOUPDATECRM() + "\",\n" +
                    "            \"Title\": \"" + req.getTitle() + "\",\n" +
                    "            \"GivenName\": \"" + req.getGivenNames() + "\",\n" +
                    "            \"FamilyName\": \"" + req.getFamilyName() + "\",\n" +
                    "            \"Gender\": \"" + req.getGender() + "\",\n" +
                    "            \"DateofBirth\": " + req.getDateofBirth() + ",\n" +
                    "            \"MaritalStatus\": \"" + req.getMaritalStatus() + "\",\n" +
                    "            \"NoofDependents\": \"" + req.getNoofDependents() + "\",\n" +
                    "            \"gPHONE1\": {\n" +
                    "                \"mPHONE1\": {\n" +
                    "                    \"PhoneNumbersRes\": \"" + req.getPhoneNumbersRes() + "\",\n" +
                    "                    \"MobilePhoneNumbers\": " + req.getMobilePhoneNumbers() + ",\n" +
                    "                    \"EmailAddress\": \"" + req.getEmailAddress() + "\"\n" +
                    "                }\n" +
                    "            },\n" +
                    "            \"gEMPLOYMENTSTATUS\": {\n" +
                    "                \"mEMPLOYMENTSTATUS\": {\n" +
                    "                    \"EmploymentStatus\": \"" + req.getEmploymentStatus() + "\",\n" +
                    "                    \"OccupationPosition\": \"" + req.getOccupationPosition() + "\",\n" +
                    "                    \"JobTitle\": \"" + req.getJobTitle() + "\",\n" +
                    "                    \"EmployersName\": \"" + req.getEmployersName() + "\",\n" +
                    "                    \"sgEMPLOYERSADD\": {\n" +
                    "                        \"EmployersAddress\": {\n" +
                    "                            \"EmployersAddress\": \"" + req.getEmployersAddress() + "\"\n" +
                    "                        }\n" +
                    "                    },\n" +
                    "                    \"EmployersBusiness\": \"" + req.getEmployersBusiness() + "\",\n" +
                    "                    \"EmploymentStartDt\": \"" + req.getEmploymentStartDt() + "\",\n" +
                    "                    \"CustomerCurrency\": \"" + req.getCustomerCurrency() + "\",\n" +
                    "                    \"CustomerSalary\": \"" + req.getCustomerSalary() + "\",\n" +
                    "                    \"AnnualBonus\": \"" + req.getAnnualBonus() + "\",\n" +
                    "                    \"SalaryDateFreq\": \"" + req.getSalaryDateFreq() + "\"\n" +
                    "                }\n" +
                    "            },\n" +
                    "            \"AverageMonthlyIncome\": " + req.getAverageMonthlyIncome() + ",\n" +
                    "            \"NetMonthlyOut\": \"" + req.getNetMonthlyOut() + "\",\n" +
                    "            \"ALLOWBULKPROCESS\": \"" + req.getALLOWBULKPROCESS() + "\",\n" +
                    "            \"CustomerSince\": \"" + req.getCustomerSince() + "\",\n" +
                    "            \"CustomerType\": \"" + req.getCustomerType() + "\",\n" +
                    "            \"CALCRISKCLASS\": \"" + req.getCALCRISKCLASS() + "\",\n" +
                    "            \"MANUALRISKCLASS\": \"" + req.getMANUALRISKCLASS() + "\",\n" +
                    "            \"LASTKYCREVIEWDATE\": \"" + req.getLASTKYCREVIEWDATE() + "\",\n" +
                    "            \"AUTONEXTKYCREVIEWDATE\": \"" + req.getAUTONEXTKYCREVIEWDATE() + "\",\n" +
                    "            \"MANUALNEXTKYCREVIEWDATE\": \"" + req.getMANUALNEXTKYCREVIEWDATE() + "\",\n" +
                    "            \"LASTSUITREVIEWDATE\": \"" + req.getLASTSUITREVIEWDATE() + "\",\n" +
                    "            \"AUTONEXTSUITREVIEWDATE\": \"" + req.getAUTONEXTSUITREVIEWDATE() + "\",\n" +
                    "            \"MANUALNEXTSUITREVIEWDATE\": \"" + req.getMANUALNEXTSUITREVIEWDATE() + "\",\n" +
                    "            \"KYCRELATIONSHIP\": \"" + req.getKYCRELATIONSHIP() + "\",\n" +
                    "            \"SecureMessage\": \"" + req.getSecureMessage() + "\",\n" +
                    "            \"AMLCHECK\": \"" + req.getAMLCHECK() + "\",\n" +
                    "            \"AMLRESULT\": \"" + req.getAMLRESULT() + "\",\n" +
                    "            \"LASTAMLRESULTDATE\": \"" + req.getLASTAMLRESULTDATE() + "\",\n" +
                    "            \"KYCCOMPLETE\": \"" + req.getKYCCOMPLETE() + "\",\n" +
                    "            \"INTERNETBANKINGSERVICE\": \"" + req.getINTERNETBANKINGSERVICE() + "\",\n" +
                    "            \"MOBILEBANKINGSERVICE\": \"" + req.getMOBILEBANKINGSERVICE() + "\",\n" +
                    "            \"REPORTTEMPLATE\": \"" + req.getREPORTTEMPLATE() + "\",\n" +
                    "            \"DateinEC\": \"" + req.getDateinEC() + "\",\n" +
                    "            \"TinNumber\": " + req.getTinNumber() + ",\n" +
                    "            \"DelinqStatus\": \"" + req.getDelinqStatus() + "\",\n" +
                    "            \"MOTHERFULLNAME\": \"" + req.getMOTHERFULLNAME() + "\"\n" +
                    "        }\n" +
                    "    }\n" +
                    "}  ";

            HttpEntity<String> request = new HttpEntity<String>(reqBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request,
                    String.class);

            HttpHeaders resHeaders = new HttpHeaders();
            resHeaders.setContentType(MediaType.APPLICATION_JSON);

            System.out.println(response.getBody());

            JSONObject ESBStatus = new JSONObject(response.getBody())
                    .getJSONObject("CreateCustomerResponse")
                    .getJSONObject("ESBStatus");
            Map<String, String> resBody = new HashMap();



            if (ESBStatus.getString("Status").equals("Success")){
                JSONObject CUSTOMERType = new JSONObject(response.getBody())
                        .getJSONObject("CreateCustomerResponse")
                        .getJSONObject("CUSTOMERType");

                resBody.put("status","success");
                resBody.put("id",CUSTOMERType.getString("id"));
                ResponseEntity<?> res = new ResponseEntity<>(
                        resBody
                        , resHeaders,
                        HttpStatus.OK);

                return res;
            }
            else{

                JSONArray errorDescription = new JSONArray(ESBStatus.getJSONArray("errorDescription"));

                resBody.put("status","failure");
                resBody.put("message",errorDescription.get(0).toString());
                return new ResponseEntity<>(
                        resBody
                        , resHeaders,
                        HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/createaccount")
    public ResponseEntity<?> createAccount(@RequestBody CreateAccount req) {
        try {
            String url = "http://10.1.245.150:7081/v1/cbo/";
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String messageId = Integer.toString((int) (Math.random() * (10000000 - 1000000 + 1) + 1000000));

            String reqBody = "{\n" +
                    "    \"AccountOpeningRequest\": {\n" +
                    "        \"ESBHeader\": {\n" +
                    "            \"serviceCode\": \"210000\",\n" +
                    "            \"channel\": \"USSD\",\n" +
                    "            \"Service_name\": \"AccountOpen\",\n" +
                    "            \"Message_Id\": \"EPAS" + messageId + "\"\n" +
                    "        },\n" +
                    "        \"OfsFunction\": {\n" +
                    "            \"messageId\": \"EPAS" + messageId + "\"\n" +
                    "        },\n" +
                    "    \"ACCOUNTSBOPENType\": {\n" +
                    "        \"CustomerID\": " + req.getCustomerID() + ",\n" +
                    "        \"ProductCode\": " + req.getProductCode() + ",\n" +
                    "        \"gACCOUNTTITLE1\": {\n" +
                    "            \"AccountName1\": \"" + req.getAccountName1() + "\"\n" +
                    "        },\n" +
                    "        \"gACCOUNTTITLE2\": {\n" +
                    "            \"AccountName2\": \"" + req.getAccountName2() + "\"\n" +
                    "        },\n" +
                    "        \"gSHORTTITLE\": {\n" +
                    "            \"ShortName\": \"" + req.getShortName() + "\"\n" +
                    "        },\n" +
                    "        \"Mnemonic\": \"" + req.getMnemonic() + "\",\n" +
                    "        \"Currency\": \"" + req.getCurrency() + "\",\n" +
                    "        \"AccountOfficer\": \"" + req.getAccountOfficer() + "\",\n" +
                    "        \"PassbookYN\": \"" + req.getPassbookYN() + "\",\n" +
                    "        \"LinktoLimitYN\": \"" + req.getLinktoLimitYN() + "\",\n" +
                    "        \"SingleLimitYN\": \"" + req.getSingleLimitYN() + "\"\n" +
                    "    }\n" +
                    "}\n" +
                    "}";
            HttpEntity<String> request = new HttpEntity<String>(reqBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request,
                    String.class);

            HttpHeaders resHeaders = new HttpHeaders();
            resHeaders.setContentType(MediaType.APPLICATION_JSON);


            System.out.println(response.getBody());

            JSONObject ESBStatus = new JSONObject(response.getBody())
                    .getJSONObject("AccountOpeningResponse")
                    .getJSONObject("ESBStatus");
            Map<String, String> resBody = new HashMap();


            if (ESBStatus.getString("Status").equals("Success")) {
                JSONObject CUSTOMERType = new JSONObject(response.getBody())
                        .getJSONObject("AccountOpeningResponse")
                        .getJSONObject("ACCOUNTType");

                resBody.put("status", "success");
                resBody.put("id", CUSTOMERType.getString("id"));
                ResponseEntity<?> res = new ResponseEntity<>(
                        resBody
                        , resHeaders,
                        HttpStatus.OK);

                return res;
            } else {

                JSONArray errorDescription = new JSONArray(ESBStatus.getJSONArray("errorDescription"));

                resBody.put("status", "failure");
                resBody.put("message", errorDescription.get(0).toString());
                return new ResponseEntity<>(
                        resBody
                        , resHeaders,
                        HttpStatus.BAD_REQUEST);
            }
        }
          catch (Exception e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/imagecapture")
    public ResponseEntity<?> imageCapture(@RequestBody ImageCapture req) {
        try {
            String url = "http://10.1.245.150:7081/v1/cbo/";
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String messageId = Integer.toString((int) (Math.random() * (10000000 - 1000000 + 1) + 1000000));

            String reqBody = "{ \n" +
                    "      \"ImageCaptureRequest\": { \n" +
                    "        \"ESBHeader\": { \n" +
                    "          \"serviceCode\": \"230000\", \n" +
                    "          \"channel\": \"USSD\", \n" +
                    "          \"Service_name\": \"ImageCapture\", \n" +
                    "          \"Message_Id\": \"EPAS" + messageId + "\" \n" +
                    "        },\n" +
                    "        \"OfsFunction\": {\n" +
                    "            \"messageId\": \"EPAS" + messageId + "\"\n" +
                    "            }, \n" +
                    "        \"IMDOCUMENTIMAGECAPTUREAPIType\": {\n" +
                    "          \"ImageType\": \"" + req.getImageType() + "\",\n" +
                    "          \"Application\": \"" + req.getApplication() + "\",\n" +
                    "          \"AccountNo\": " + req.getAccountNo() + ",\n" +
                    "          \"ShortDescription\": \"" + req.getShortDescription() + "\",\n" +
                    "          \"gDESCRIPTION\": {\n" +
                    "            \"Description\": " + req.getDescription() + "\n" +
                    "          },\n" +
                    "          \"IMAGE\": \"" + req.getIMAGE() + "\",\n" +
                    "          \"MultiMediaType\": \"" + req.getMultiMediaType() + "\"\n" +
                    "        }\n" +
                    "      } \n" +
                    "    }";
            HttpEntity<String> request = new HttpEntity<String>(reqBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request,
                    String.class);

            HttpHeaders resHeaders = new HttpHeaders();
            resHeaders.setContentType(MediaType.APPLICATION_JSON);

            System.out.println(response.getBody());

            JSONObject ESBStatus = new JSONObject(response.getBody())
                    .getJSONObject("ImageCaptureResponse")
                    .getJSONObject("ESBStatus");
            Map<String, String> resBody = new HashMap();


            if (ESBStatus.getString("Status").equals("Success")) {
                JSONObject CUSTOMERType = new JSONObject(response.getBody())
                        .getJSONObject("ImageCaptureResponse")
                        .getJSONObject("IMDOCUMENTIMAGEType");

                resBody.put("status", "success");
                resBody.put("id", CUSTOMERType.getString("id"));
                ResponseEntity<?> res = new ResponseEntity<>(
                        resBody
                        , resHeaders,
                        HttpStatus.OK);

                return res;
            } else {

                JSONArray errorDescription = new JSONArray(ESBStatus.getJSONArray("errorDescription"));

                resBody.put("status", "failure");
                resBody.put("message", errorDescription.get(0).toString());
                return new ResponseEntity<>(
                        resBody
                        , resHeaders,
                        HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/imageuploadcapture")
    public ResponseEntity<?> imageUploadCapture(@RequestBody ImageUploadCapture req) {
        try {
            String url = "http://10.1.245.150:7081/v1/cbo/";
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String messageId = Integer.toString((int) (Math.random() * (10000000 - 1000000 + 1) + 1000000));

            String reqBody = "{\n" +
                    "    \"ImageUploadRequest\": {\n" +
                    "        \"ESBHeader\": {\n" +
                    "            \"serviceCode\": \"240000\",\n" +
                    "            \"channel\": \"USSD\",\n" +
                    "            \"Service_name\": \"ImageUpload\",\n" +
                    "            \"Message_Id\": \"EPAS" + messageId + "\"\n" +
                    "        },\n" +
                    "        \"OfsFunction\": {\n" +
                    "            \"messageId\": \"EPAS" + messageId + "\"\n" +
                    "        },\n" +
                    "        \"IMDOCUMENTUPLOADCAPTUREType\": {\n" +
                    "                \"id\": \"" + req.getId() + "\",\n" +
                    "                \"ImageId\": \"" + req.getImageId() + "\",\n" +
                    "                \"UploadImage\": \"" + req.getUploadImage() + "\"\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n";
            HttpEntity<String> request = new HttpEntity<String>(reqBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request,
                    String.class);

            HttpHeaders resHeaders = new HttpHeaders();
            resHeaders.setContentType(MediaType.APPLICATION_JSON);

            System.out.println(response.getBody());

            JSONObject ESBStatus = new JSONObject(response.getBody())
                    .getJSONObject("ImageUploadResponse")
                    .getJSONObject("ESBStatus");
            Map<String, String> resBody = new HashMap();


            if (ESBStatus.getString("Status").equals("Success")) {
                JSONObject CUSTOMERType = new JSONObject(response.getBody())
                        .getJSONObject("ImageUploadResponse")
                        .getJSONObject("IMDOCUMENTUPLOADType");

                resBody.put("status", "success");
                resBody.put("id", CUSTOMERType.getString("id"));
                ResponseEntity<?> res = new ResponseEntity<>(
                        resBody
                        , resHeaders,
                        HttpStatus.OK);

                return res;
            } else {

                JSONArray errorDescription = new JSONArray(ESBStatus.getJSONArray("errorDescription"));

                resBody.put("status", "failure");
                resBody.put("message", errorDescription.get(0).toString());
                return new ResponseEntity<>(
                        resBody
                        , resHeaders,
                        HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/imagefileupload")
    public ResponseEntity<?> imageFileUpload(@RequestBody ImageFileUpload req) {
        try {
            String url = "http://10.1.245.150:7081/v1/cbo/";
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String messageId = Integer.toString((int) (Math.random() * (10000000 - 1000000 + 1) + 1000000));

            String reqBody = "{ \n" +
                    "      \"ImageFileUploadRequest\": { \n" +
                    "        \"ESBHeader\": { \n" +
                    "          \"serviceCode\": \"430000\", \n" +
                    "          \"channel\": \"USSD\", \n" +
                    "          \"Service_name\": \"ImageFileUpload\", \n" +
                    "          \"Message_Id\": \"EPAS" + messageId + "\" \n" +
                    "        }, \n" +
                    "        \"ImageFileUpload\": [ \n" +
                    "          { \n" +
                    "            \"data\": \"" + req.getData() + "\", \n" +
                    "            \"type\": \"" + req.getType() + "\", \n" +
                    "            \"fileName\":\"" + req.getFileName() + "\", \n" +
                    "            \"payload\": \"" + req.getPayload() + "\" \n" +
                    "          }\n" +
                    "         \n" +
                    "        ] \n" +
                    "      } \n" +
                    "    }";
            HttpEntity<String> request = new HttpEntity<String>(reqBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request,
                    String.class);

            HttpHeaders resHeaders = new HttpHeaders();
            resHeaders.setContentType(MediaType.APPLICATION_JSON);

            System.out.println(response.getBody());

            JSONObject ESBStatus = new JSONObject(response.getBody())
                    .getJSONObject("ImageFileUploadResponse")
                    .getJSONObject("ESBStatus");
            Map<String, String> resBody = new HashMap();


            if (ESBStatus.getString("Status").equals("Success")) {

                resBody.put("status", "success");
                ResponseEntity<?> res = new ResponseEntity<>(
                        resBody
                        , resHeaders,
                        HttpStatus.OK);

                return res;
            } else {

                resBody.put("status", "failure");
                return new ResponseEntity<>(
                        resBody
                        , resHeaders,
                        HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.NOT_FOUND);
        }
    }

}

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class CreateCustomerReq {
    String Mnemonic;
    String ShortName;
    String FullName;
    String FullName2;
    String ZoneSubcity;
    String WoredaKebeleHouseNO;
    String TownCity;
    long PostCode;
    String Country;
    long Sector;
    String AccountOfficer;
    String SecondOfficer;
    long Industry;
    long Target;
    String Nationality;
    long CustomerStatus;
    String Residence;
    String CONTACTDATE;
    String INTRODUCER;
    long LegalIDNumber;
    String DocumentName;
    String NameonID;
    String IssueAuthority;
    long IssueDate;
    long ExpirationDate;
    long PhoneNumbersOff;
    String CustomerLiability;
    String Language;
    String COMPANYBOOK;
    String CONFIDTXT;
    String ISSUECHEQUES;
    String NOUPDATECRM;
    String Title;
    String GivenNames;
    String FamilyName;
    String Gender;
    long DateofBirth;
    String MaritalStatus;
    String NoofDependents;
    String PhoneNumbersRes;
    long MobilePhoneNumbers;
    String EmailAddress;
    String EmploymentStatus;
    String OccupationPosition;
    String JobTitle;
    String EmployersName;
    String EmployersAddress;
    String EmployersBusiness;
    String EmploymentStartDt;
    String CustomerCurrency;
    String CustomerSalary;
    String AnnualBonus;
    String SalaryDateFreq;
    int AverageMonthlyIncome;
    String NetMonthlyOut;


    String ALLOWBULKPROCESS;
    String CustomerSince;
    String CustomerType;
    String CALCRISKCLASS;
    String MANUALRISKCLASS;
    String LASTKYCREVIEWDATE;
    String AUTONEXTKYCREVIEWDATE;
    String MANUALNEXTKYCREVIEWDATE;
    String LASTSUITREVIEWDATE;
    String AUTONEXTSUITREVIEWDATE;
    String MANUALNEXTSUITREVIEWDATE;
    String KYCRELATIONSHIP;
    String SecureMessage;
    String AMLCHECK;
    String AMLRESULT;
    String LASTAMLRESULTDATE;
    String KYCCOMPLETE;
    String INTERNETBANKINGSERVICE;
    String MOBILEBANKINGSERVICE;
    String REPORTTEMPLATE;
    String DateinEC;
    long TinNumber;
    String DelinqStatus;
    String MOTHERFULLNAME;
}

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class CreateAccount {
    long CustomerID;
    long ProductCode;
    String AccountName1;
    String AccountName2;
    String ShortName;
    String Mnemonic;
    String Currency;
    String AccountOfficer;
    String PassbookYN;
    String LinktoLimitYN;
    String SingleLimitYN;
}

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class ImageCapture {
    String ImageType;
    String Application;
    long AccountNo;
    String ShortDescription;
    String Description;
    String IMAGE;
    String MultiMediaType;
}

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class ImageUploadCapture {
    String id;
    String ImageId;
    String UploadImage;
}

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class ImageFileUpload {
    String data;
    String type;
    String fileName;
    String payload;
}