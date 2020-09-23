package com.hcl.appscan.maven.plugin.auth;

import java.io.IOException;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.apache.wink.json4j.JSONException;

import com.hcl.appscan.sdk.auth.AuthenticationHandler;
import com.hcl.appscan.sdk.auth.IAuthenticationProvider;
import com.hcl.appscan.sdk.auth.LoginType;
import com.hcl.appscan.sdk.utils.SystemUtil;

public class MavenAuthenticationProvider implements IAuthenticationProvider {

    private String m_token = null;
    private String m_key;
    private String m_secret;
	
    public MavenAuthenticationProvider(String key, String secret) {
    	m_key = key;
    	m_secret = secret;
    }
    
	@Override
	public boolean isTokenExpired() {
        boolean isExpired = false;
        AuthenticationHandler handler = new AuthenticationHandler(this);

        try {
            isExpired = handler.isTokenExpired() && !handler.login(m_key, m_secret, true, LoginType.ASoC_Federated);
        } catch (IOException | JSONException e) {
            isExpired = false;
        }
        return isExpired;
	}

	@Override
	public Map<String, String> getAuthorizationHeader(boolean persist) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer "+ getToken().trim()); //$NON-NLS-1$ //$NON-NLS-2$
        if(persist)
            headers.put("Connection", "Keep-Alive"); //$NON-NLS-1$ //$NON-NLS-2$
        return headers;
	}

	@Override
	public String getServer() {
		return SystemUtil.getServer(m_key);
	}

	@Override
	public void saveConnection(String token) {
		m_token = token;
	}

	@Override
	public Proxy getProxy() {
		return Proxy.NO_PROXY;
	}
	
	private String getToken() {
		return m_token == null ? "" : m_token; //$NON-NLS-1$
	}
}
