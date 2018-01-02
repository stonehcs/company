package com.lichi.increaselimit.security.authentication.mobile;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.lichi.increaselimit.user.entity.User;
import com.lichi.increaselimit.user.service.UserService;

import lombok.Getter;
import lombok.Setter;

/**
 * 短信认证provider
 * @author majie
 *
 */
@Getter
@Setter
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

	private UserService userService;
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.authentication.AuthenticationProvider#
	 * authenticate(org.springframework.security.core.Authentication)
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;
		
		String mobile = (String) authenticationToken.getPrincipal();
		
		User user = userService.loadUserInfoByMobile(mobile);

		if (user == null) {
			user = userService.insertMobileUser(mobile);
		}
		
		SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(user, user.getAuthorities());
		
		authenticationResult.setDetails(authenticationToken.getDetails());

		return authenticationResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.authentication.AuthenticationProvider#
	 * supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
