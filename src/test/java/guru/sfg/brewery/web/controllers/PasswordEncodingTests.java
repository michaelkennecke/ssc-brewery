package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

public class PasswordEncodingTests {

    static final String PASSWORD = "password";

    @Test
    void testBcrypt15() {
        PasswordEncoder bcrypt = new BCryptPasswordEncoder(15);
        System.out.println(bcrypt.encode(PASSWORD));
        System.out.println(bcrypt.encode(PASSWORD));
        System.out.println(bcrypt.encode("tiger"));
    }

    @Test
    void testBcrypt() {
        PasswordEncoder bcrypt = new BCryptPasswordEncoder(16);
        System.out.println(bcrypt.encode(PASSWORD));
        System.out.println(bcrypt.encode(PASSWORD));

    }

    @Test
    void testSHA256() {
        PasswordEncoder sha256 = new StandardPasswordEncoder();
        System.out.println(sha256.encode(PASSWORD));
        System.out.println(sha256.encode(PASSWORD));
    }

    @Test
    void testLdap() {
        // This algorithm is using a random salt
        PasswordEncoder ldap = new LdapShaPasswordEncoder();
        System.out.println(ldap.encode(PASSWORD));
        System.out.println(ldap.encode(PASSWORD));
    }

    @Test
    void testNoOp() {
        PasswordEncoder noOp = NoOpPasswordEncoder.getInstance();
        System.out.println(noOp.encode(PASSWORD));
    }
    @Test
    void hashingExample(){
        System.out.println(DigestUtils.md5DigestAsHex((PASSWORD.getBytes(StandardCharsets.UTF_8))));

        String salted = PASSWORD + "ThisIsMySaltValue";
        System.out.println(DigestUtils.md5DigestAsHex((salted.getBytes(StandardCharsets.UTF_8))));

    }
}
