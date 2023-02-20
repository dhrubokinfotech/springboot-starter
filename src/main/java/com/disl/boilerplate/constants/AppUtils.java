package com.disl.boilerplate.constants;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.web.multipart.MultipartFile;

import java.beans.FeatureDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;
import java.util.stream.Stream;

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

	public static String getInvalidPasswordMessage(String password) {
		if(password == null || password.isEmpty()) {
			return "Password can't be empty";
		}

		if(password.length() <= 7) {
			return "Password must have 8 or more characters long";
		}

		if(password.contains(" ")) {
			return "Password shouldn't contains white spaces";
		}

		Pattern digitPattern = Pattern.compile("[0-9]");
		Pattern specialCharacterPattern = Pattern.compile("[0-9_\\/\\s,.-]+");

		boolean isMatched = digitPattern.matcher(password).matches() && specialCharacterPattern.matcher(password).matches();
		if(!isMatched) {
			return "Password must have minimum a digit and an special character";
		}

		return null;
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
