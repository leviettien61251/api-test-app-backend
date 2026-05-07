package com.example.apitestappbackend.ChangePassword;

import com.example.apitestappbackend.DTO.PasswordChangedTest.PasswordChangedRequest;
import com.example.apitestappbackend.DTO.PasswordChangedTest.PasswordChangedResponse;
import com.example.apitestappbackend.models.LoggedInUsers;
import com.example.apitestappbackend.models.PasswordChanged;
import com.example.apitestappbackend.repository.LoggedInUsersRepository;
import com.example.apitestappbackend.repository.PasswordChangedRepository;
import com.example.apitestappbackend.repository.SignupNotYetLoginRepository;
import com.example.apitestappbackend.repository.UserTestRepository;
import com.example.apitestappbackend.services.LoggedInUsersService;
import com.example.apitestappbackend.services.PasswordChangedService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:postgresql://localhost:5432/hospital_test_2",
        "spring.datasource.username=postgres",
        "spring.datasource.password=123456789",
        "spring.datasource.driver-class-name=org.postgresql.Driver",
        "spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=true"
})
@DisplayName("Password Changed Scenario Tests - PostgreSQL Real Database")
public class PasswordChangedScenarioDbTest {

    @Autowired
    private PasswordChangedRepository passwordChangedRepository;

    @Autowired
    private LoggedInUsersRepository loggedInUsersRepository;

    @Autowired
    private SignupNotYetLoginRepository signupNotYetLoginRepository;

    @Autowired
    private UserTestRepository userTestRepository;

    private PasswordChangedService passwordChangedService;
    private LoggedInUsersService loggedInUsersService;

    @BeforeEach
    void setUp() {
        passwordChangedRepository.deleteAll();
        loggedInUsersRepository.deleteAll();
        signupNotYetLoginRepository.deleteAll();

        loggedInUsersService = new LoggedInUsersService(loggedInUsersRepository, signupNotYetLoginRepository, userTestRepository);
        passwordChangedService = new PasswordChangedService(passwordChangedRepository, loggedInUsersRepository, loggedInUsersService);
    }

    @Nested
    @DisplayName("Scenario 1: Đủ dữ liệu, mật khẩu cũ đúng")
    class Scenario1_SuccessChange {
        @Test
        void shouldChangePasswordWhenOldPasswordCorrect() {
            String phone = "0912345678";
            String oldPass = "OldPass123";
            String newPass = "NewPass123";

            // prepare logged in user
            LoggedInUsers u = new LoggedInUsers();
            u.setPhoneNumber(phone);
            u.setPassword(oldPass);
            u.setLoginStatus("success");
            loggedInUsersRepository.save(u);

            PasswordChangedRequest req = new PasswordChangedRequest(phone, oldPass, newPass);
            PasswordChangedResponse res = passwordChangedService.changePassword(req);

            assertEquals("success", res.getStatus(), "Server should return success");

            List<PasswordChanged> saved = passwordChangedRepository.findAll();
            assertFalse(saved.isEmpty(), "PasswordChanged should be saved to DB");

            System.out.println("✅ PASS: Scenario 1 - password changed successfully and recorded");
        }
    }

    @Nested
    @DisplayName("Scenario 2: Đủ dữ liệu, mật khẩu cũ sai")
    class Scenario2_WrongOldPassword {
        @Test
        void shouldNotChangeWhenOldPasswordIncorrect() {
            String phone = "0912345679";
            String realPass = "RealPass123";
            String wrongOld = "BadPass";
            String newPass = "NewPass123";

            LoggedInUsers u = new LoggedInUsers();
            u.setPhoneNumber(phone);
            u.setPassword(realPass);
            u.setLoginStatus("success");
            loggedInUsersRepository.save(u);

            PasswordChangedRequest req = new PasswordChangedRequest(phone, wrongOld, newPass);
            PasswordChangedResponse res = passwordChangedService.changePassword(req);

            assertEquals("fail", res.getStatus(), "Should not change password when old password is incorrect");
            assertEquals(0, passwordChangedRepository.count(), "No record should be saved");

            System.out.println("✅ PASS: Scenario 2 - wrong old password rejected");
        }
    }

    @Nested
    @DisplayName("Scenario 3: Tài khoản chưa đăng ký")
    class Scenario3_AccountNotRegistered {
        @Test
        void shouldNotChangeWhenAccountNotExist() {
            String phone = "0999999999"; // not registered
            PasswordChangedRequest req = new PasswordChangedRequest(phone, "any", "anyNew");
            PasswordChangedResponse res = passwordChangedService.changePassword(req);

            assertEquals("fail", res.getStatus(), "Should not change when account not registered");
            assertEquals(0, passwordChangedRepository.count(), "No record should be saved");

            System.out.println("✅ PASS: Scenario 3 - non-registered account rejected");
        }
    }

    @Nested
    @DisplayName("Scenario 4: Không nhập mật khẩu cũ (null)")
    class Scenario4_MissingOldPassword {
        @Test
        void shouldNotChangeWhenOldPasswordNull() {
            String phone = "0912111222";
            LoggedInUsers u = new LoggedInUsers();
            u.setPhoneNumber(phone);
            u.setPassword("SomePass");
            u.setLoginStatus("success");
            loggedInUsersRepository.save(u);

            PasswordChangedRequest req = new PasswordChangedRequest(phone, null, "NewPass123");
            PasswordChangedResponse res = passwordChangedService.changePassword(req);

            assertEquals("fail", res.getStatus(), "Should not change when old password is null");
            assertEquals(0, passwordChangedRepository.count(), "No record should be saved");

            System.out.println("✅ PASS: Scenario 4 - missing old password rejected");
        }
    }

    @Nested
    @DisplayName("Scenario 5: Không nhập mật khẩu mới (null)")
    class Scenario5_MissingNewPassword {
        @Test
        void shouldNotChangeWhenNewPasswordNull() {
            String phone = "0912111333";
            String oldPass = "OldPass123";
            LoggedInUsers u = new LoggedInUsers();
            u.setPhoneNumber(phone);
            u.setPassword(oldPass);
            u.setLoginStatus("success");
            loggedInUsersRepository.save(u);

            PasswordChangedRequest req = new PasswordChangedRequest(phone, oldPass, null);
            PasswordChangedResponse res = passwordChangedService.changePassword(req);

            assertEquals("fail", res.getStatus(), "Should not change when new password is null");
            assertEquals(0, passwordChangedRepository.count(), "No record should be saved");

            System.out.println("✅ PASS: Scenario 5 - missing new password rejected");
        }
    }

    @Nested
    @DisplayName("Scenario 6: Mật khẩu mới không hợp lệ (quá ngắn)")
    class Scenario6_NewPasswordInvalid {
        @Test
        void shouldNotChangeWhenNewPasswordTooShort() {
            String phone = "0912111444";
            String oldPass = "OldValid123";
            String shortNew = "a"; // too short per business rule

            LoggedInUsers u = new LoggedInUsers();
            u.setPhoneNumber(phone);
            u.setPassword(oldPass);
            u.setLoginStatus("success");
            loggedInUsersRepository.save(u);

            PasswordChangedRequest req = new PasswordChangedRequest(phone, oldPass, shortNew);
            PasswordChangedResponse res = passwordChangedService.changePassword(req);

            // Business expects rejection for invalid new password
            assertEquals("fail", res.getStatus(), "Should not accept too short new password");
            assertEquals(0, passwordChangedRepository.count(), "No record should be saved");

            System.out.println("✅ PASS: Scenario 6 - invalid new password rejected");
        }
    }

    @Nested
    @DisplayName("Scenario 7: Mật khẩu mới trùng mật khẩu cũ")
    class Scenario7_NewSameAsOld {
        @Test
        void shouldNotChangeWhenNewSameAsOld() {
            String phone = "0912111555";
            String pass = "SamePass123";
            LoggedInUsers u = new LoggedInUsers();
            u.setPhoneNumber(phone);
            u.setPassword(pass);
            u.setLoginStatus("success");
            loggedInUsersRepository.save(u);

            PasswordChangedRequest req = new PasswordChangedRequest(phone, pass, pass);
            PasswordChangedResponse res = passwordChangedService.changePassword(req);

            // Most designs reject same-as-old; assert fail per spec
            assertEquals("fail", res.getStatus(), "Should not accept new password identical to old");
            assertEquals(0, passwordChangedRepository.count(), "No record should be saved");

            System.out.println("✅ PASS: Scenario 7 - new password same as old rejected");
        }
    }

    @Nested
    @DisplayName("Scenario 8: Không có phone")
    class Scenario8_NoPhone {
        @Test
        void shouldNotChangeWhenPhoneMissing() {
            PasswordChangedRequest req = new PasswordChangedRequest(null, "any", "any");
            PasswordChangedResponse res = passwordChangedService.changePassword(req);

            assertEquals("fail", res.getStatus(), "Should not change when phone is missing");
            assertEquals(0, passwordChangedRepository.count(), "No record should be saved");

            System.out.println("✅ PASS: Scenario 8 - missing phone rejected");
        }
    }
}
