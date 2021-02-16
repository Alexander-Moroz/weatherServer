package com.amoroz.weather.validator;

import org.springframework.util.StringUtils;
import com.amoroz.weather.model.SearchRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SearchRequestCheckValidator implements ConstraintValidator<SearchRequestCheck, SearchRequest> {

    @Override
    public boolean isValid(SearchRequest value, ConstraintValidatorContext context) {
        //chk username
        if ("anonymous".equals(value.getUserName())) {
            var errorString = "anonymous users are not allowed";
            addValidationMsg(context, errorString, "userName");
            return false;
        }
        //chk all params
        if (value.getCityId() == null && !(StringUtils.hasLength(value.getCityName())) && value.getLatitude() == null && value.getLongitude() == null) {
            var errorString = "All params are empty";
            addValidationMsg(context, errorString, "cityId");
            return false;
        }
        return true;
    }

    private void addValidationMsg(ConstraintValidatorContext context, String error, String property) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(error)
                .addPropertyNode(property)
                .addConstraintViolation();
    }
}
