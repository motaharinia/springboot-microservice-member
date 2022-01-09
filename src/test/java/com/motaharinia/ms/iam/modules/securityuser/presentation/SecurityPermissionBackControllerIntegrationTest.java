package com.motaharinia.ms.iam.modules.securityuser.presentation;

import com.motaharinia.ms.iam.config.security.oauth2.dto.BackUserDto;
import com.motaharinia.ms.iam.config.security.oauth2.dto.BearerTokenDto;
import com.motaharinia.ms.iam.modules.backuser.business.BackUserService;
import com.motaharinia.ms.iam.modules.backuser.business.mapper.BackUserMapper;
import com.motaharinia.ms.iam.modules.securityuser.business.enumeration.PermissionTypeEnum;
import com.motaharinia.ms.iam.modules.securityuser.business.service.SecurityUserService;
import com.motaharinia.ms.iam.modules.securityuser.persistence.orm.SecurityUser;
import com.motaharinia.ms.iam.modules.securityuser.presentation.securitypermission.SecurityPermissionCreateRequestDto;
import com.motaharinia.ms.iam.modules.securityuser.presentation.securitypermission.SecurityPermissionReadResponseDto;
import com.motaharinia.ms.iam.modules.securityuser.presentation.securitypermission.SecurityPermissionResponseDto;
import com.motaharinia.ms.iam.modules.securityuser.presentation.securitypermission.SecurityPermissionUpdateRequestDto;
import com.motaharinia.msutility.custom.customdto.ClientResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
class SecurityPermissionBackControllerIntegrationTest {
    /**
     * پورت رندوم تست
     */
    @LocalServerPort
    private Integer PORT;
    /**
     * نشانی وب ماژول
     */
    private String MODULE_API;

    @Autowired
    private SecurityUserService securityUserService;
    @Autowired
    private BackUserService backUserService;
    @Autowired
    private BackUserMapper backUserMapper;

    /**
     * شیی فراخوان تست
     * در صورتی که پروژه ریسورس سرور است و باید توکن خود را با سرور احراز هویت چک کند
     */
    @Autowired
    private TestRestTemplate testRestTemplate;

    private Long id;
    private final String tokenUsername = "0083419004";

    /**
     * این متد مقادیر پیش فرض قبل از هر تست این کلاس تست را مقداردهی اولیه میکند
     */
    @BeforeEach
    void beforeEach() {

    }


    /**
     * این متد مقادیر پیش فرض را قبل از اجرای تمامی متدهای تست این کلاس مقداردهی اولیه میکند
     */
    @BeforeAll
    void beforeAll() {
        //تنظیم زبان لوکیل پروژه روی پارسی
        Locale.setDefault(new Locale("fa", "IR"));
        //مسیر پیش فرض ماژول
        this.MODULE_API = "http://localhost:" + PORT + "/api/v1.0/security-permission-back";
    }

    private HttpHeaders getHeaders(String tokenUsername) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        if (!ObjectUtils.isEmpty(tokenUsername)) {
            SecurityUser securityUser = securityUserService.serviceReadByUsernameForBack(tokenUsername);
            BackUserDto backUserDto = backUserService.serviceReadById(securityUser.getBackUserId());
            BearerTokenDto bearerTokenDto = securityUserService.createBearerToken(securityUser.getId(), false, backUserDto, new HashMap<>());
            headers.set("Authorization", "Bearer " + bearerTokenDto.getAccessToken());
        }
        return headers;
    }
    //-------------------------------------------------------
    //CRUD
    //-------------------------------------------------------

    //-------------------------------------------
    //متدهای فعال که در حال حاضر در پروژه استفاده میشوند
    //-------------------------------------------

    /**
     * تولید درخت دسترسی
     */
    @Test
    @Order(4)
    void readAllTest() {
        try {
            //ارسال درخواست
            ResponseEntity<ClientResponseDto<ArrayList<SecurityPermissionReadResponseDto>>> response = this.testRestTemplate.exchange(this.MODULE_API + "/read-all", HttpMethod.GET, new HttpEntity<>(getHeaders(tokenUsername)), new ParameterizedTypeReference<>() {
            });
            //بررسی پاسخ
            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            //بررسی های تست
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getData()).isNotNull();
            ArrayList<SecurityPermissionReadResponseDto> responseDto = response.getBody().getData();
            assertThat(responseDto).isNotNull();
            assertThat(responseDto.size()).isGreaterThan(0);
            System.out.println(responseDto.toString());
        } catch (Exception exception) {
            log.error("Exception: {}", exception);
            fail("Exception: {}", exception);
        }
    }

    //-------------------------------------------
    //متدهایی که در پروژه در حال حاضر استفاده نمیشوند
    //-------------------------------------------

    /**
     * ثبت دسترسی
     */
    @Test
    @Order(1)
    void createTest() {
        try {
            //ایجاد مدل درخواست
            //ثبت دسترسی
            SecurityPermissionCreateRequestDto dto = new SecurityPermissionCreateRequestDto("تستی","home_test", "", 1, PermissionTypeEnum.HIDDEN_AUTHORITY, null, true);
            //ارسال درخواست
            ResponseEntity<ClientResponseDto<SecurityPermissionResponseDto>> response = this.testRestTemplate.exchange(this.MODULE_API, HttpMethod.POST, new HttpEntity<>(dto, getHeaders(tokenUsername)), new ParameterizedTypeReference<>() {
            });


            //بررسی پاسخ
            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            //بررسی های تست
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getData()).isNotNull();
            SecurityPermissionResponseDto responseDto = response.getBody().getData();
            assertThat(responseDto).isNotNull();
            assertThat(responseDto.getId()).isNotNull();
            id = responseDto.getId();


        } catch (Exception exception) {
            log.error("Exception: {}", exception);
            fail("Exception: {}", exception);
        }
    }


    /**
     * ویرایش اطلاعات دسترسی
     */
    @Test
    @Order(2)
    void updateTest() {
        try {
            //ایجاد مدل درخواست
            //ویرایش دسترسی های دارا
            SecurityPermissionUpdateRequestDto dto = new SecurityPermissionUpdateRequestDto(id, "تستی","home_test1", "", 1, PermissionTypeEnum.HIDDEN_AUTHORITY, null, true);
            dto.setAuthority("home_test");
            dto.setMenuOrder(0);
            dto.setTypeEnum(PermissionTypeEnum.AUTHORITY);
            dto.setParentId(null);

            //ارسال درخواست
            ResponseEntity<ClientResponseDto<SecurityPermissionResponseDto>> response = this.testRestTemplate.exchange(this.MODULE_API, HttpMethod.PUT, new HttpEntity<>(dto, getHeaders(tokenUsername)), new ParameterizedTypeReference<>() {
            });
            //بررسی پاسخ
            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            //بررسی های تست
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getData()).isNotNull();
            SecurityPermissionResponseDto responseDto = response.getBody().getData();
            assertThat(responseDto).isNotNull();
            assertThat(responseDto.getId()).isNotNull();
        } catch (Exception exception) {
            log.error("Exception: {}", exception);
            fail("Exception: {}", exception);
        }
    }


    /**
     * جستجو درخت دسترسی
     */
    @Test
    @Order(3)
    void readByIdTest() {
        try {
            //ارسال درخواست
            ResponseEntity<ClientResponseDto<SecurityPermissionReadResponseDto>> response = this.testRestTemplate.exchange(this.MODULE_API + "/read-by-id-back/" + id, HttpMethod.GET, new HttpEntity<>(getHeaders(tokenUsername)), new ParameterizedTypeReference<>() {
            });
            //بررسی پاسخ
            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            //بررسی های تست
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getData()).isNotNull();
            SecurityPermissionReadResponseDto responseDto = response.getBody().getData();
            assertThat(responseDto).isNotNull();
            System.out.println(responseDto.toString());
        } catch (Exception exception) {
            log.error("Exception: {}", exception);
            fail("Exception: {}", exception);
        }
    }


    /**
     * حذف دسترسی
     * به دلیل اینکه بعد از ثبت نمیخواستم حذف شود این متد تست کامنت شده است
     */
    @Test
    @Order(5)
    void deleteTest() {
        try {
            //ارسال درخواست
            ResponseEntity<ClientResponseDto> response = this.testRestTemplate.exchange(this.MODULE_API + "/" + id, HttpMethod.DELETE, new HttpEntity<>(getHeaders(tokenUsername)), new ParameterizedTypeReference<>() {
            });
            //بررسی پاسخ
            assertThat(response).isNotNull();
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            //بررسی های تست
            assertThat(response.getBody()).isNotNull();
        } catch (Exception exception) {
            log.error("Exception: {}", exception);
            fail("Exception: {}", exception);
        }
    }

}
