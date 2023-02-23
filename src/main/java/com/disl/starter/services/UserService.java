package com.disl.starter.services;

import com.disl.starter.config.AppProperties;
import com.disl.starter.constants.AppConstants;
import com.disl.starter.constants.AppUtils;
import com.disl.starter.entities.Role;
import com.disl.starter.entities.Secret;
import com.disl.starter.entities.User;
import com.disl.starter.enums.RoleType;
import com.disl.starter.exceptions.NotFoundException;
import com.disl.starter.exceptions.ResponseException;
import com.disl.starter.models.OAuth2UserInfo;
import com.disl.starter.models.PaginationArgs;
import com.disl.starter.models.requests.*;
import com.disl.starter.models.responses.TokenResponse;
import com.disl.starter.repository.UserRepository;
import com.disl.starter.security.JwtTokenProvider;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static com.disl.starter.enums.UserTokenPurpose.EMAIL_VERIFICATION;

@Service
public class UserService {

	@Autowired
	private RoleService roleService;

	@Autowired
	private MailService mailService;

	@Autowired
	private SecretService secretService;

	@Autowired
	private AppProperties appProperties;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RefreshTokenService refreshTokenService;

	@Autowired
	private AuthenticationManager authenticationManager;

	public User findByEmail(String email) {
		return userRepository.findTopByEmail(email).orElse(null);
	}

	public User findByEmailWithException(String email) {
		return userRepository.findTopByEmail(email).orElseThrow(() ->
				new ResponseException(HttpStatus.FORBIDDEN, "No user found with this email")
		);
	}

	public User findByIdWithException(long id) {
		return userRepository.findById(id).orElseThrow(() -> new NotFoundException(User.class));
	}

	public User findByPasswordResetToken(String token) {
		return userRepository.findByPasswordResetToken(token).orElse(null);
	}

	public User findById(long id) {
		return userRepository.findById(id).orElse(null);
	}

	public User saveUser(User user) {
		return this.userRepository.save(user);
	}

	public List<User> getAllUsersList() {
		return userRepository.findAll();
	}

	public List<User> getAllUsersByRole (Role role) {
		return userRepository.findByRoles(role);
	} 
	
	public List<User> getAllUsersByRolesIn (Role[] roles) {
		return userRepository.findByRolesIn(roles);
	}

	public TokenResponse signIn(SignInRequest request) {
		User user = findByEmailWithException(request.getEmail());

		UsernamePasswordAuthenticationToken usernamePassAuthToken = new UsernamePasswordAuthenticationToken(
				request.getEmail(), request.getPassword()
		);

		Authentication authentication = authenticationManager.authenticate(usernamePassAuthToken);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = tokenProvider.generateToken(authentication);
		if (jwt == null) {
			throw new ResponseException(HttpStatus.FORBIDDEN, "Unknown error. Please try again");
		}

		String refreshToken = refreshTokenService.createRefreshToken(user.getId()).getToken();
		return new TokenResponse(jwt, refreshToken, user);
	}

	public User getUserInfo(UserService userService) {
		User user = AppUtils.getLoggedInUser(userService);
		if (!user.getVerified()) {
			throw new ResponseException("This user is not verified yet");
		}

		return userService.findById(user.getId());
	}

	public User updateUserFromAdmin(UpdateUserFromAdmin request) {
		User user = userRepository.findById(request.getUserId()).orElse(null);
		if(user == null){
			throw new NotFoundException(User.class);
		}

		for(Role role: user.getRoles()) {
			if(role.getRoleType().equals(RoleType.ADMIN)) {
				throw new ResponseException("You can't update other admin type user information");
			}
		}

		user.setName(request.getName());
		return userRepository.save(user);
	}

	public boolean changePassword(ChangePasswordRequest request) {
		Optional<User> userOptional = userRepository.findTopByEmail(request.getEmail());
		if (userOptional.isEmpty()) {
			throw new ResponseException("User not found for the id.");
		}

		User user = userOptional.get();
		String password = user.getPassword();
		String newPassword = request.getNewPassword();

		if (password == null) {
			throw new ResponseException("Password Not available for google/facebook user.");
		}

		if(!passwordEncoder.matches(request.getPreviousPassword(), password)) {
			throw new ResponseException("Incorrect old password.");
		}

		String invalidPasswordMessage = AppUtils.getInvalidPasswordMessage(newPassword);
		if (invalidPasswordMessage != null) {
			throw new ResponseException(HttpStatus.BAD_REQUEST, invalidPasswordMessage);
		}

		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
		return true;
	}

	public User createSocialUser(OAuth2UserInfo oAuth2UserInfo, RoleType roleType) {
		Role role = roleService.findByRoleType(roleType);
		if (role == null) {
			throw new ResponseException("Role not found with this type " + roleType.name());
		}

		User user = new User();
		user.setEmail(oAuth2UserInfo.getEmail());
		String lastName = oAuth2UserInfo.getLastName();
		if(lastName!= null) {
			user.setName(oAuth2UserInfo.getFirstName() + " " + oAuth2UserInfo.getLastName());
		}
		else{
			user.setName(oAuth2UserInfo.getFirstName());
		}
		user.setVerified(true);
		user.setBanned(false);
		user.setRoles(Set.of(role));
		return userRepository.save(user);
	}

	public User createAdminUser(AdminCreationRequest request) {
		Role role = roleService.findRoleById(request.getRoleId());

		if(role == null) {
			throw new NotFoundException(Role.class);
		} else if (role.getRoleType() != RoleType.ADMIN) {
			throw new ResponseException("Invalid role type");
		}

		String email = request.getEmail();
		if (!AppUtils.isValidMail(email)){
			throw new ResponseException("please provide a valid mail");
		}

		String password = request.getPassword();
		String invalidPasswordMessage = AppUtils.getInvalidPasswordMessage(password);
		if (invalidPasswordMessage != null) {
			throw new ResponseException(HttpStatus.BAD_REQUEST, invalidPasswordMessage);
		}

		User adminUser = new User();
		adminUser.setEmail(email);
		adminUser.setRoles(Set.of(role));
		adminUser.setName(request.getName());
		adminUser.setPassword(passwordEncoder.encode(password));
		return userRepository.save(adminUser);
	}

	public void createNewUser(SignUpRequest request) {
		User userExists = userRepository.findTopByEmail(request.getEmail()).orElse(null);
		if (userExists != null) {
			throw new ResponseException("User with this email exists already. Please signin or try with different email");
		}

		String email = request.getEmail();
		String password = request.getPassword();

		if (!AppUtils.isValidMail(email)) {
			throw new ResponseException(HttpStatus.EXPECTATION_FAILED, "Your email format is not correct.");
		}

		String invalidPasswordMessage = AppUtils.getInvalidPasswordMessage(password);
		if (invalidPasswordMessage != null) {
			throw new ResponseException(HttpStatus.BAD_REQUEST, invalidPasswordMessage);
		}

		User signedUser = new User();
		signedUser.setEmail(request.getEmail());
		signedUser.setPassword(passwordEncoder.encode(password));
		signedUser.setName(request.getName());

		Role role = roleService.findRoleByNameWithException(AppConstants.userRole);
		signedUser.setRoles(Set.of(role));
		signedUser = userRepository.save(signedUser);

		Secret secret = new Secret();
		Long userId = signedUser.getId();
		secret.setUserId(userId);

		String token = UUID.randomUUID().toString();
		secret.setUserToken(token);

		mailService.sendMail(email, appProperties.getName() + " User Verification", "Please follow this link to verify your email for " + appProperties.getName() +". \n \t" + appProperties.getBackEndUrl() + AppConstants.VERIFICATION_SUBURL + token);
		secret.setUserTokenPurpose(EMAIL_VERIFICATION);
		secretService.saveSecret(secret);
	}

	public Page<User> getAllPaginatedUser(PaginationArgs paginationArgs) {
		Pageable pageable = AppUtils.getPageable(paginationArgs);
		Map<String, Object> specParameters = AppUtils.getParameters(paginationArgs.getParameters());

		if(!specParameters.isEmpty()) {
			Specification<User> userSpecification = Specification.where((root, query, criteriaBuilder) -> {
				List<Predicate> predicates = new ArrayList<>();

				for (Map.Entry<String, Object> entry : specParameters.entrySet()) {
					String filterBy = entry.getKey();
					String filterWith = entry.getValue().toString();

					if(filterWith != null && !filterWith.isEmpty()) {
						if (filterBy.endsWith("roleType")) {
							Join<User, List<Role>> roleJoin = root.join("roles", JoinType.INNER);
							return criteriaBuilder.equal(roleJoin.get("roleType"), RoleType.valueOf(filterWith));

						} else {
							Class<?> type = root.get(filterBy).getJavaType();
							if (type.equals(Long.class)) {
								return criteriaBuilder.equal(root.get(filterBy), Long.valueOf(filterWith));
							} else if (type.equals(Boolean.class)) {
								predicates.add(criteriaBuilder.equal(root.get(filterBy), Boolean.valueOf(filterWith)));
							} else if (type.equals(String.class)) {
								predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get(filterBy)), "%" + filterWith.toUpperCase() + "%"));
							} else if (type.equals(LocalDateTime.class)) {
								LocalDate localDate = LocalDate.parse(filterWith);
								LocalDateTime endDateTime = LocalDateTime.of(localDate, LocalTime.MAX);
								LocalDateTime startDateTime = LocalDateTime.of(localDate, LocalTime.MIN);
								predicates.add(criteriaBuilder.between(root.get(filterBy), startDateTime, endDateTime));
							} else {
								predicates.add(criteriaBuilder.equal(root.get(filterBy), filterWith));
							}
						}
					}
				}

				return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
			});

			return userRepository.findAll(userSpecification, pageable);
		}

		return userRepository.findAll(pageable);
	}
}
