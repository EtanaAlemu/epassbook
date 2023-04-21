package com.dxvalley.epassbook.services;

import com.dxvalley.epassbook.models.APIStatus;

import java.util.List;

public interface APIStatusService {
    APIStatus getApiStatus();
    APIStatus updateApiStatus(APIStatus apiStatus);
 }
