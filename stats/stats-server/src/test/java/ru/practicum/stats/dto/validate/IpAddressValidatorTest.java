package ru.practicum.stats.dto.validate;

import org.junit.jupiter.api.Test;
import ru.practicum.stats.validate.IpAddressValidator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IpAddressValidatorTest {
    private final IpAddressValidator validator = new IpAddressValidator();

    @Test
    void testNullInput() {
        assertTrue(validator.isValid(null, null));
    }

    @Test
    void testEmptyString() {
        assertFalse(validator.isValid("", null));
    }

    @Test
    void testWhitespaceOnly() {
        assertFalse(validator.isValid("   ", null));
    }

    @Test
    void testValidIPv4() {
        assertTrue(validator.isValid("192.168.1.1", null));
        assertTrue(validator.isValid("0.0.0.0", null));
        assertTrue(validator.isValid("255.255.255.255", null));
    }

    @Test
    void testInvalidIPv4() {
        assertFalse(validator.isValid("256.100.50.25", null));
        assertFalse(validator.isValid("192.168.1.1.1", null));
        assertFalse(validator.isValid("192.168.1.a", null));
    }

    @Test
    void testValidIPv6() {
        assertTrue(validator.isValid("2001:0db8:85a3:0000:0000:8a2e:0370:7334", null));
        assertTrue(validator.isValid("2001:db8:85a3::8a2e:370:7334", null));
        assertTrue(validator.isValid("::1", null));
    }

    @Test
    void testInvalidIPv6() {
        assertFalse(validator.isValid("2001:db8:85a3::8a2e:370:7334:1234:5678", null));
        assertFalse(validator.isValid("2001:db8:85a3::8a2e:370:733g", null));
    }
}