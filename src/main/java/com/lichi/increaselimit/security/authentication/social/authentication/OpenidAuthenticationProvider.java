package com.lichi.increaselimit.security.authentication.social.authentication;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.security.SocialUserDetailsService;

import com.lichi.increaselimit.user.entity.SocialUserInfo;
import com.lichi.increaselimit.user.entity.User;
import com.lichi.increaselimit.user.service.UserService;

import lombok.Getter;
import lombok.Setter;

/**
 * 第三方app登陆provider
 * 
 * @author majie
 *
 */
@Getter
@Setter
public class OpenidAuthenticationProvider implements AuthenticationProvider {

	private SocialUserDetailsService socialUserDetailsService;

	private UsersConnectionRepository usersConnectionRepository;

	private UserService userService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.authentication.AuthenticationProvider#
	 * authenticate(org.springframework.security.core.Authentication)
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		OpenidAuthenticationToken authenticationToken = (OpenidAuthenticationToken) authentication;

		SocialUserInfo socialUserInfo = (SocialUserInfo) authenticationToken.getPrincipal();

		Set<String> providerUserIds = new HashSet<>();

		providerUserIds.add(socialUserInfo.getProviderUserId());

		Set<String> userIds = usersConnectionRepository.findUserIdsConnectedTo(socialUserInfo.getProviderId(),
				providerUserIds);

		/**
		 * 如果用户信息不存在，给用户注册一个
		 * 存在就获取用户信息
		 */
		User user = null;
		if (CollectionUtils.isEmpty(userIds) || userIds.size() != 1) {
			user = userService.insertSocialUser(socialUserInfo);
		} else {
			String userId = userIds.iterator().next();
			user = (User) socialUserDetailsService.loadUserByUserId(userId);
		}

		if (user == null) {
			throw new InternalAuthenticationServiceException("无法获取用户信息");
		}

		OpenidAuthenticationToken authenticationResult = new OpenidAuthenticationToken(user, user.getAuthorities());

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
		return OpenidAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
