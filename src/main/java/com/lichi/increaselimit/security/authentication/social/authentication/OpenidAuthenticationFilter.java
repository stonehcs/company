package com.lichi.increaselimit.security.authentication.social.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import com.lichi.increaselimit.security.properties.SecurityConstants;
import com.lichi.increaselimit.user.entity.SocialUserInfo;

/**
 * 第三方登陆认证过滤器
 * @author majie
 *
 */
public class OpenidAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	// ~ Static fields/initializers
	// =====================================================================================

	private String openidParameter = SecurityConstants.DEFAULT_PARAMETER_NAME_OPENID;
	
	private String providerIdParameter = SecurityConstants.DEFAULT_PARAMETER_NAME_PROVIDERID;
	
	private boolean postOnly = true;

	// ~ Constructors
	// ===================================================================================================

	public OpenidAuthenticationFilter() {
		super(new AntPathRequestMatcher(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_SOCIAL, "POST"));
	}

	// ~ Methods
	// ========================================================================================================
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		if (postOnly && ! "POST".equals(request.getMethod())) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}

		String openid = obtainOpenid(request);
		String providerId = obtainProviderId(request);
		String nickname = obtainNickname(request);
		if (openid == null) {
			openid = "";
		}
		if (providerId == null) {
			providerId = "";
		}
		if (nickname == null) {
			nickname = "";
		}

		openid = openid.trim();
		providerId = providerId.trim();
		nickname = nickname.trim();

		//设置用户信息
		SocialUserInfo socialUserInfo = new SocialUserInfo();
		socialUserInfo.setProviderId(providerId);
		socialUserInfo.setProviderUserId(openid);
		socialUserInfo.setDisplayName(nickname);
		
		OpenidAuthenticationToken authRequest = new OpenidAuthenticationToken(socialUserInfo);

		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);

		return this.getAuthenticationManager().authenticate(authRequest);
	}


	/**
	 * 获取手机号
	 */
	protected String obtainOpenid(HttpServletRequest request) {
		return request.getParameter(openidParameter);
	}
	/**
	 * 获取第三方类型
	 */
	protected String obtainProviderId(HttpServletRequest request) {
		return request.getParameter(providerIdParameter);
	}
	protected String obtainNickname(HttpServletRequest request) {
		return request.getParameter("nickname");
	}

	/**
	 * Provided so that subclasses may configure what is put into the
	 * authentication request's details property.
	 *
	 * @param request
	 *            that an authentication request is being created for
	 * @param authRequest
	 *            the authentication request object that should have its details
	 *            set
	 */
	protected void setDetails(HttpServletRequest request, OpenidAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	}

	/**
	 * Sets the parameter name which will be used to obtain the username from
	 * the login request.
	 *
	 * @param openid
	 *            the parameter name. Defaults to "username".
	 */
	public void setOpenid(String openid) {
		Assert.hasText(openid, "openid parameter must not be empty or null");
		this.openidParameter = openid;
	}


	/**
	 * Defines whether only HTTP POST requests will be allowed by this filter.
	 * If set to true, and an authentication request is received which is not a
	 * POST request, an exception will be raised immediately and authentication
	 * will not be attempted. The <tt>unsuccessfulAuthentication()</tt> method
	 * will be called as if handling a failed authentication.
	 * <p>
	 * Defaults to <tt>true</tt> but may be overridden by subclasses.
	 */
	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}

	public final String getOpenid() {
		return openidParameter;
	}

}
