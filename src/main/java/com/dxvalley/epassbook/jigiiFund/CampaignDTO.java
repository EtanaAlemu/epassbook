package com.dxvalley.epassbook.jigiiFund;

import lombok.Data;

@Data
public class CampaignDTO {
    private String title;
    private String city;
    private Long goalAmount;
    private Long campaignDuration;
    private Long totalAmountCollected;
    private Long numberOfBackers;
    private Long numberOfLikes;
}
