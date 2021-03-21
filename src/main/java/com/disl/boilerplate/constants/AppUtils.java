package com.disl.boilerplate.constants;

import java.beans.FeatureDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.web.multipart.MultipartFile;
import edu.vt.middleware.password.CharacterCharacteristicsRule;
import edu.vt.middleware.password.DigitCharacterRule;
import edu.vt.middleware.password.LengthRule;
import edu.vt.middleware.password.NonAlphanumericCharacterRule;
import edu.vt.middleware.password.Password;
import edu.vt.middleware.password.PasswordData;
import edu.vt.middleware.password.PasswordValidator;
import edu.vt.middleware.password.Rule;
import edu.vt.middleware.password.RuleResult;

public class AppUtils {

	public static boolean isThisDateValid(String dateToValidate){
		if(dateToValidate == null){
			return false;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(AppConstants.DEFAULTDATEFORMAT);
		sdf.setLenient(false);
		
		try {
			sdf.parse(dateToValidate);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}
	
	public static boolean isValidMail(String email) {
	   String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
	   return email.matches(regex);
	}
	
	public static List<Rule> getPasswordRules () {
		LengthRule lengthRule = new LengthRule(8, 15);
		CharacterCharacteristicsRule charRule = new CharacterCharacteristicsRule();
		charRule.getRules().add(new DigitCharacterRule(1));
		charRule.getRules().add(new NonAlphanumericCharacterRule(1));
		charRule.setNumberOfCharacteristics(2);
		List<Rule> ruleList = new ArrayList<Rule>();
		ruleList.add(charRule);
		ruleList.add(lengthRule);
		return ruleList;
	}
	
	public static RuleResult checkIfPasswordValid(String passwordToCheck) {
		PasswordValidator validator = AppUtils.getValidator(AppUtils.getPasswordRules());
		return validator.validate(new PasswordData(new Password(passwordToCheck)));
	}
	
	public static PasswordValidator getValidator(List<Rule> ruleList) {
		return new PasswordValidator(ruleList);
	}
	
	public static boolean isNullorEmpty (String str) {
		if (str == null || str.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
	
	public static File convertMultipartToFile(MultipartFile file) throws IOException {
		File convertedFile = new File(file.getOriginalFilename());
    	FileOutputStream fos = new FileOutputStream(convertedFile);
    	fos.write(file.getBytes());
    	fos.close();
		
    	return convertedFile;
    }
	
	public static String[] getNullPropertyNames(Object source) {
	    final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
	    return Stream.of(wrappedSource.getPropertyDescriptors())
	            .map(FeatureDescriptor::getName)
	            .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
	            .toArray(String[]::new);
	}
	
}
