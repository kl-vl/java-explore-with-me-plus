package ru.practicum.statsdto.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RequiredArgsConstructor()
public class IpAddressValidator implements ConstraintValidator<ValidIpAddress, String> {

//    @Override
//    public void initialize(ValidIpAddress constraintAnnotation) {
//    }

    @Override
    public boolean isValid(String ip, ConstraintValidatorContext context) {
        if (ip == null) {
            return true; // Используйте @NotNull для обязательных полей
        }

        if (ip.trim().isEmpty()) {
            return false;
        }

        return isValidIpAddress(ip);
    }

    private boolean isValidIpAddress(String ip) {
        try {
            InetAddress.getByName(ip);
            return true;
        } catch (UnknownHostException e) {
            return false;
        }
    }
}