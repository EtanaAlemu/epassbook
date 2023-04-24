package com.dxvalley.epassbook.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class APIStatusServiceImpl implements APIStatusService {
    @Autowired
    APIStatusRepository apiStatusRepository;
    @Override
    public APIStatus getApiStatus() {
        return apiStatusRepository.findById((short) 1)
                .orElseThrow(() ->new RuntimeException("Internal server error while fetching API Status"));
    }

    @Override
    public APIStatus updateApiStatus(APIStatus apiStatus) {
        APIStatus result = getApiStatus();
        result.setAtmCardEnabled(Optional.ofNullable(apiStatus.getAtmCardEnabled()).orElse(result.getAtmCardEnabled()));
        result.setEarlyPayDayEnabled(Optional.ofNullable(apiStatus.getEarlyPayDayEnabled()).orElse(result.getEarlyPayDayEnabled()));
        result.setAtmPinChangeEnabled(Optional.ofNullable(apiStatus.getAtmPinChangeEnabled()).orElse(result.getAtmPinChangeEnabled()));
        result.setAccountStatementEnabled(Optional.ofNullable(apiStatus.getAccountStatementEnabled()).orElse(result.getAccountStatementEnabled()));
        result.setCardLessCashEnabled(Optional.ofNullable(apiStatus.getCardLessCashEnabled()).orElse(result.getCardLessCashEnabled()));
        result.setNewProductEnabled(Optional.ofNullable(apiStatus.getNewProductEnabled()).orElse(result.getNewProductEnabled()));

        return apiStatusRepository.save(result);
    }
}
