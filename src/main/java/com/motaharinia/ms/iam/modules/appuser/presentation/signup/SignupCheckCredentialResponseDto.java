package com.motaharinia.ms.iam.modules.appuser.presentation.signup;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author eng.motahari@gmail.com<br>
 * کلاس مدل پاسخ گام اول ثبت نام(بررسی کلمه کاربری و رمز عبور)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupCheckCredentialResponseDto implements Serializable {
    /**
     *  کد تایید داخلی (برای زمان تست که توسعه دهندگان نیاز به بررسی پیامک نداشته باشند)
     */
    private String testOtp = "";
}
