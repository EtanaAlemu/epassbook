package com.dxvalley.epassbook.services;

import com.dxvalley.epassbook.models.Passcode;
import com.dxvalley.epassbook.repositories.PasscodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasscodeService {
    PasscodeRepository passcodeRepository;

    public PasscodeService(PasscodeRepository passcodeRepository) {
        this.passcodeRepository = passcodeRepository;
    }

//    private Passcode findPasscodeByUserId(){
////        passcodeRepository.findByUserId()
//
//    }
}
