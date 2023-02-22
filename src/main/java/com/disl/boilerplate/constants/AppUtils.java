package com.disl.boilerplate.constants;

import com.disl.boilerplate.enums.AscOrDescType;
import com.disl.boilerplate.enums.RoleType;
import com.disl.boilerplate.entities.Role;
import com.disl.boilerplate.entities.User;
import com.disl.boilerplate.models.PaginationArgs;
import com.disl.boilerplate.security.CustomUserDetails;
import com.disl.boilerplate.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.disl.boilerplate.constants.AppConstants.*;

public final class AppUtils {
	public static boolean isTrue (Boolean value) {
		return value != null && value;
	}

	public static boolean isNotNullOrEmpty (String str) {
		return str != null && !str.trim().isEmpty();
	}

	public static boolean isNullOrEmpty (String str) {
		return str == null || str.trim().isEmpty();
	}

	public static boolean isNullOrEmptyList (List<?> values) {
		return values == null || values.isEmpty();
	}

	public static boolean isNotNullOrEmptyList (List<?> values) {
		return values != null && !values.isEmpty();
	}

	public static boolean isNotNullAndGreaterZero (Long id) {
		return id != null && id > 0;
	}

	public static boolean isValidMail(String email) {
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		return email.matches(regex);
	}

	public static Map<String, Object> getParameters(Map<String, Object> parameters) {
		parameters.remove(PAGE_NO);
		parameters.remove(SORT_BY);
		parameters.remove(PAGE_SIZE);
		parameters.remove(ASC_OR_DESC);
		parameters.remove(ASC_OR_DESC);
		parameters.remove(PARAMERTES);
		return parameters;
	}

	public static Pageable getPageable(int pageNo, int pageSize, String sortBy, AscOrDescType ascOrDesc) {
		Pageable pageable;

		if(sortBy != null && sortBy.trim().length() > 0) {
			if (ascOrDesc.equals(AscOrDescType.asc)) {
				pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
			} else {
				pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
			}
		} else {
			pageable = PageRequest.of(pageNo, pageSize);
		}

		return pageable;
	}

	public static Pageable getPageable(PaginationArgs paginationArgs) {
		Pageable pageable;
		String sortBy = paginationArgs.getSortBy();
		int pageNo = paginationArgs.getPageNo();
		int pageSize = paginationArgs.getPageSize();

		if(sortBy != null && sortBy.length() > 0) {
			if (paginationArgs.getAscOrDesc().equals(AscOrDescType.asc)) {
				pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
			} else {
				pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
			}
		} else {
			pageable = PageRequest.of(pageNo, pageSize);
		}

		return pageable;
	}

	public static User getLoggedInUser(UserService userService, RoleType roleType) {
		User user = getLoggedInUser(userService);
		Optional<Role> optionalRole = user.getRoles()
				.stream()
				.filter(role -> role.getRoleType().equals(roleType))
				.findAny();

		return optionalRole.isEmpty() ? null : user;
	}

	public static User getLoggedInUser(UserService userService) {
		User user;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		try {
			Object principal = authentication.getPrincipal();

			if (principal instanceof CustomUserDetails customUserDetails) {
				user = new User();
				user.setId(customUserDetails.getId());
				user.setName(customUserDetails.getName());
				user.setEmail(customUserDetails.getEmail());
				user.setRoles(customUserDetails.getRoles());
				user.setVerified(customUserDetails.isVerified());
				user.setPassword(customUserDetails.getPassword());
			} else {
				user = userService.findByEmail(authentication.getName());
			}
		} catch(Exception e) {
			user = userService.findByEmail(authentication.getName());
		}

		return user;
	}

	public static <D, E> D mapEntityToDto(ModelMapper modelMapper, E entity, Class<D> dtoClass) {
		return modelMapper.map(entity, dtoClass);
	}

	public static <E, D> Page<D> mapEntityPageToDtoPage(ModelMapper modelMapper, Page<E> entities, Class<D> dtoClass) {
		return entities.map(objectEntity -> modelMapper.map(objectEntity, dtoClass));
	}

	public static String makeSlug(String slug){
		char ch = '-';
		slug = slug.replace(' ',ch);
		return slug;
	}

	public static boolean isValidPhoneNumber(String phoneNumber) {
		String regex = "^[0][1-9]\\d{9}$|^[1-9]\\d{9}$";
		return phoneNumber.matches(regex);
	}

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
}
