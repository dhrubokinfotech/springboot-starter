package com.disl.starter.controllers;

import com.disl.starter.config.AppProperties;
import com.disl.starter.config.CommonApiResponses;
import com.disl.starter.constants.AppConstants;
import com.disl.starter.constants.AppUtils;
import com.disl.starter.entities.RefreshToken;
import com.disl.starter.entities.User;
import com.disl.starter.enums.AscOrDescType;
import com.disl.starter.enums.RoleType;
import com.disl.starter.enums.SocialAuthType;
import com.disl.starter.exceptions.OAuth2AuthenticationProcessingException;
import com.disl.starter.exceptions.ResponseException;
import com.disl.starter.models.OAuth2UserInfo;
import com.disl.starter.models.PaginationArgs;
import com.disl.starter.models.Response;
import com.disl.starter.models.requests.*;
import com.disl.starter.models.responses.TokenResponse;
import com.disl.starter.security.AuthUserInfoProviderURLFactory;
import com.disl.starter.security.CustomUserDetailsService;
import com.disl.starter.security.JwtTokenProvider;
import com.disl.starter.security.OAuth2UserInfoFactory;
import com.disl.starter.services.MailService;
import com.disl.starter.services.RefreshTokenService;
import com.disl.starter.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

import static com.disl.starter.constants.AppConstants.*;

@RestController
@CommonApiResponses
@RequestMapping("/api")
public class EntryController {

    @Autowired
    private UserService userService;

    @Autowired
	private MailService mailService;

    @Autowired
	private RestTemplate restTemplate;

	@Autowired
	private AppProperties appProperties;

	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private RefreshTokenService refreshTokenService;

    @Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private AuthUserInfoProviderURLFactory authUserInfoProviderURLFactory;

	@Operation(summary = "Sign-in")
	@ApiResponse(content = @Content(schema = @Schema(implementation = TokenResponse.class)), responseCode = "200")
	@PostMapping(value = "/signin")
    public ResponseEntity<Response> authenticateUser(@RequestBody SignInRequest loginRequest) {
		return Response.getResponseEntity(
				true, "You are now logged in.",
				userService.signIn(loginRequest)
		);
    }

	@Operation(summary = "Sign Up")
	@ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)), responseCode = "200")
	@PostMapping(value = "/signup")
    public ResponseEntity<Response> createUser (@RequestBody SignUpRequest signUpRequest) {
		userService.createUser(signUpRequest);
		return Response.getResponseEntity(true, "User Created. Please check your email address and verify your account");
    }

	@Operation(summary = "Update banned status by admin manually")
	@ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)), responseCode = "200")
	@PreAuthorize("hasAuthority('USER_UPDATE')")
	@PutMapping(value = "/user/update-banned-status")
	public ResponseEntity<Response> updateBannedStatusManually(@RequestBody UpdateBannedStatusRequest request) {
		boolean banned = request.isBanned();
		User user = userService.findByIdWithException(request.getUserId());

		if (banned && user.getEmail().equals(AppConstants.INITIAL_USERNAME)) {
			return Response.getResponseEntity(false, "You can't ban super admin user");
		}

		user.setBanned(banned);
		User savedUser = userService.saveUser(user);
		if (savedUser != null) {
			return Response.getResponseEntity(true, "Banned status updated");
		} else {
			return Response.getResponseEntity(false, "Failed due to unknown reason. Please try again");
		}
	}

	@Operation(summary = "Update verify status by admin manually", security = @SecurityRequirement(name = "jwtToken"))
	@ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)), responseCode = "200")
	@PreAuthorize("hasAuthority('USER_UPDATE')")
	@PutMapping(value = "/user/update-verify-status")
	public ResponseEntity<Response> updateVerifyStatusManually(@RequestBody UpdateVerifyStatusRequest request) {
		boolean verify = request.isVerify();
		User user = userService.findByIdWithException(request.getUserId());

		if (!verify && user.getEmail().equals(AppConstants.INITIAL_USERNAME)) {
			return Response.getResponseEntity(false, "You can't unverify super admin user");
		}

		user.setVerified(verify);
		User savedUser = userService.saveUser(user);
		if (savedUser != null) {
			return Response.getResponseEntity(true, "Verify status updated");
		} else {
			return Response.getResponseEntity(false, "Failed due to unknown reason. Please try again");
		}
	}

	@Operation(summary = "Get all users - paginated", security = @SecurityRequirement(name = "jwtToken"))
	@ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class))), responseCode = "200")
	@PreAuthorize("hasAuthority('USER_READ')")
	@GetMapping(value = "/user/all")
	public ResponseEntity<Response> getAllPaginatedUsers(
			@RequestParam(name = PAGE_NO, defaultValue = "0") int pageNo,
			@RequestParam(name = PAGE_SIZE, defaultValue = "20") int pageSize,
			@RequestParam(name = SORT_BY, defaultValue = "") String sortBy,
			@RequestParam(name = ASC_OR_DESC, defaultValue = "") AscOrDescType ascOrDesc,
			@RequestParam(required = false) Map<String, Object> parameters
	) {
		PaginationArgs paginationArgs = new PaginationArgs(
				pageNo, pageSize, sortBy, ascOrDesc, parameters
		);

		return Response.getResponseEntity(
				true, "All paginated users loaded",
				userService.getAllPaginatedUser(paginationArgs)
		);
	}

	@Operation(summary = "Get user info", security = @SecurityRequirement(name = "jwtToken"))
	@ApiResponse(content = @Content(schema = @Schema(implementation = User.class)), responseCode = "200")
	@GetMapping("/me")
	public ResponseEntity<Response> getUserInfo() {
		return Response.getResponseEntity(
				true,"User info loaded",
				userService.getUserInfo(userService)
		);
	}

	@Operation(summary = "Get user info by id", security = @SecurityRequirement(name = "jwtToken"))
	@ApiResponse(content = @Content(schema = @Schema(implementation = User.class)), responseCode = "200")
	@GetMapping("/user/id/{userId}")
	public ResponseEntity<Response> findUserById(@PathVariable("userId") long userId) {
		return Response.getResponseEntity(
				true,"User info loaded",
				userService.findByIdWithException(userId)
		);
	}

	@Operation(summary = "Update user profile information", security = @SecurityRequirement(name = "jwtToken"))
	@ApiResponse(content = @Content(schema = @Schema(implementation = User.class)), responseCode = "200")
	@PutMapping("/user/info/update")
	public ResponseEntity<Response> updateUserInfo(@Valid @RequestBody UpdateUserInfoRequest updateUserInfoRequest) {
		User user = userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		user.setName(updateUserInfoRequest.getName());
		return Response.getResponseEntity(true, "User updated successfully!", userService.saveUser(user));
	}

	@Operation(summary = "Admin user Creation", security = @SecurityRequirement(name = "jwtToken"))
	@ApiResponse(content = @Content(schema = @Schema(implementation = User.class)), responseCode = "200")
	@PreAuthorize("hasAuthority('USER_CREATE')")
	@PostMapping(value = "/admin-user-create")
	public ResponseEntity<Response> createAdminUser (@RequestBody AdminCreationRequest request) {
		return Response.getResponseEntity(
				true,"Admin user created successfully",
				userService.createAdminUser(request)
		);
	}

	@Operation(summary = "Update user from admin", security = @SecurityRequirement(name = "jwtToken"))
	@ApiResponse(content = @Content(schema = @Schema(implementation = User.class)), responseCode = "200")
	@PreAuthorize("hasAuthority('USER_UPDATE_FROM_ADMIN')")
	@PutMapping("/update-user-from-admin")
	public ResponseEntity<Response> updateUserInfo(@Valid @RequestBody UpdateUserFromAdmin request) {
		return Response.getResponseEntity(
				true,"User updated successfully",
				userService.updateUserFromAdmin(request)
		);
	}

	@Operation(summary = "Change password request", security = @SecurityRequirement(name = "jwtToken"))
	@ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)), responseCode = "200")
	@PreAuthorize("#request.email == authentication.name")
	@PostMapping(value = "/changepassword")
	public ResponseEntity<Response> changePassword(@RequestBody ChangePasswordRequest request) {
		return Response.getResponseEntity(
				true, "Password Changed successfully",
				userService.changePassword(request)
		);
	}

	@Operation(summary = "Forget password Request")
	@ApiResponse(content = @Content(schema = @Schema(implementation = Response.class)), responseCode = "200")
	@PostMapping(value = "/forgetpassword")
	public ResponseEntity<Response> requestForgetPassword(@RequestBody InitialForgetPasswordRequest initialForgetPasswordRequest) {
		User user = userService.findByEmail(initialForgetPasswordRequest.getUserEmail());
		if (user == null) {
			return Response.getResponseEntity(HttpStatus.BAD_REQUEST, "user not found with this email");
		}

		if (AppUtils.isValidMail(user.getEmail())) {
			String token = UUID.randomUUID().toString();
			user.setPasswordResetToken(token);
			userService.saveUser(user);

			if(appProperties.getActiveProfile() != AppConstants.environment.development) {
				mailService.sendMail(user.getEmail(),
						AppConstants.forgetPasswordSubject, AppConstants.forgetPasswordText + appProperties.getBackEndUrl()+AppConstants.RESET_PASSWORD_SUBURL + token
				);
			}

			return Response.getResponseEntity(true, "Reset password link sent to your registered email address.");
		} else {
			return Response.getResponseEntity(HttpStatus.EXPECTATION_FAILED, "Your email format is not correct.");
		}
	}

	@Operation(summary = "Refresh Token Request")
	@ApiResponse(content = @Content(schema = @Schema(implementation = TokenResponse.class)), responseCode = "200")
	@PostMapping(value = "/refreshtoken")
	public ResponseEntity<Response> refreshtoken(@RequestBody RefreshTokenRequest request) {
		String requestRefreshToken = request.getRefreshTokenId();
		return refreshTokenService.findByToken(requestRefreshToken)
				.map(refreshTokenService::verifyExpiration)
				.map(RefreshToken::getUserId)
				.map(userId -> {
					UserDetails userDetails = customUserDetailsService.loadUserById(userId);
					refreshTokenService.deleteByCredentialId(userId);
					User user = userService.findById(userId);

					String newToken = tokenProvider.generateToken(userDetails);
					String refreshToken = refreshTokenService.createRefreshToken(userId).getToken();
					TokenResponse newTokenResponse = new TokenResponse(newToken,  refreshToken, user);
					return Response.getResponseEntity(true,"Token Successfully Generated.", newTokenResponse);
				})
				.orElseThrow(() -> new ResponseException(requestRefreshToken + " Refresh token is not in database!"));
	}

	@Operation(summary = "OAuth2 SignIn/SignUp")
	@ApiResponse(content = @Content(schema = @Schema(implementation = TokenResponse.class)), responseCode = "200")
	@GetMapping("/oauth2/login")
	public ResponseEntity<Response> socialLogin(
			@RequestParam("provider") SocialAuthType provider,
			@RequestHeader("Authorization") String authHeader
	) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String activeToken = authHeader;
		UriComponentsBuilder builder;

		if (provider == SocialAuthType.GOOGLE) {
			activeToken = "Bearer " + authHeader;
			headers.add("Authorization", activeToken);
			builder = UriComponentsBuilder.fromHttpUrl(authUserInfoProviderURLFactory.getUserInfoUri(provider));

		} else if (provider == SocialAuthType.FACEBOOK) {
			builder = UriComponentsBuilder.fromHttpUrl(authUserInfoProviderURLFactory.getUserInfoUri(provider))
					.queryParam("fields", "id,first_name,middle_name,last_name,name,email,verified,picture.width(250).height(250)")
					.queryParam("access_token", activeToken);
		}

		/*else if (provider == AuthType.APPLE) {
			builder = UriComponentsBuilder.fromHttpUrl(authUserInfoProviderURLFactory.getUserInfoUri(provider));
			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			Map<String, String> map = new HashMap<>();
			map.put("client_id", "com.disl.atlas"); // app_id like com.app.id//
//			String token = generateJWT();   // generated jwt
//			map.put("client_secret", token);
			map.put("grant_type", "authorization_code");
			map.put("code", authHeader);  // JWT code we got from iOS
			HttpEntity<Map<String, String>> request = new HttpEntity<>(map, headers);

//			final String appleAuthURL = "https://appleid.apple.com/auth/token";
//			String response = restTemplate.postForObject(appleAuthURL, request, String.class);
		}*/

		else {
			return Response.getResponseEntity(HttpStatus.NOT_ACCEPTABLE, "Provider not supported.");
		}

		HttpEntity<String> entity = new HttpEntity<>("", headers);
		ResponseEntity<Map> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, Map.class);

		if(response.getBody() == null) {
			return Response.getResponseEntity(HttpStatus.FORBIDDEN, "Something went wrong: " + response.getStatusCode());
		}

		OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, (Map) response.getBody());
		if (oAuth2UserInfo.getEmail() == null || oAuth2UserInfo.getEmail().isBlank()) {
			throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
		}

		User user = userService.findByEmail(oAuth2UserInfo.getEmail());
		if (user == null) {
			user = userService.createSocialUser(oAuth2UserInfo, RoleType.USER);
		}

		if(user.getBanned()) {
			return Response.getResponseEntity(HttpStatus.FORBIDDEN, "You are banned by admin. If you think it is a mistake then please contact with us.");
		}

		final UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());

		String accessToken = tokenProvider.generateToken(userDetails);
		String refreshToken = refreshTokenService.createRefreshToken(user.getId()).getToken();
		TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken, user);
		return Response.getResponseEntity(true, "User logged in.", tokenResponse);
	}
}
