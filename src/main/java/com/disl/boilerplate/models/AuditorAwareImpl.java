package com.disl.boilerplate.models;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			return Optional.of(SecurityContextHolder.getContext().getAuthentication().getName());
		} else {
			return Optional.of("SYSTEM");
		}
	}

}