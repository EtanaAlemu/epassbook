package com.dxvalley.epassbook.services;

import com.dxvalley.epassbook.dto.UserInfo;
import com.dxvalley.epassbook.models.Users;

public interface UserService {
    Users registerUser(Users tempUser);
}
