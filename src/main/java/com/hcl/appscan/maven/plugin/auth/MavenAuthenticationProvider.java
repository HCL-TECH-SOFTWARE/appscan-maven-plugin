/**
 * © Copyright HCL Technologies Ltd. 2020. 
 * LICENSE: Apache License, Version 2.0 https://www.apache.org/licenses/LICENSE-2.0
 */

package com.hcl.appscan.maven.plugin.auth;

import java.io.IOException;
import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.crypto.DefaultSettingsDecryptionRequest;
import org.apache.maven.settings.crypto.SettingsDecrypter;
import org.apache.maven.settings.crypto.SettingsDecryptionRequest;
import org.apache.maven.settings.crypto.SettingsDecryptionResult;
import org.apache.wink.json4j.JSONException;

import com.hcl.appscan.maven.plugin.IMavenConstants;
import com.hcl.appscan.sdk.auth.AuthenticationHandler;
import com.hcl.appscan.sdk.auth.IAuthenticationProvider;
import com.hcl.appscan.sdk.auth.LoginType;
import com.hcl.appscan.sdk.utils.SystemUtil;

public class MavenAuthenticationProvider implements IAuthenticationProvider {

    private String m_token = null;
    private String m_key;
    private String m_secret;
    private Server m_server;
    private SettingsDecrypter m_settingsDecrypter;
	
    public MavenAuthenticationProvider(String key, String secret, MavenSession session, SettingsDecrypter decrypter) {
    	m_key = key;
    	m_secret = secret;
    	m_server = session.getSettings().getServer(IMavenConstants.APPSCAN_SERVER);
    	m_settingsDecrypter = decrypter;
    }
    
	@Override
	public boolean isTokenExpired() {
        boolean isExpired = false;
        AuthenticationHandler handler = new AuthenticationHandler(this);

        try {
            isExpired = handler.isTokenExpired() && !handler.login(getKey(), getSecret(), true, LoginType.ASoC_Federated);
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
		return SystemUtil.getServer(getKey());
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
	
	private String getKey() {
		if(m_key == null) {
			Server server = getMavenServer();
			m_key = server == null ? "" : server.getUsername(); //$NON-NLS-1$
		}
		return m_key;
	}
	
	private String getSecret() {
		if(m_secret != null)
			return m_secret;
		
		Server server = getMavenServer();
		return server == null ? "" : server.getPassword(); //$NON-NLS-1$
	}
	
	private Server getMavenServer() {
		if(m_server == null)
			return null;
		
		SettingsDecryptionRequest request = new DefaultSettingsDecryptionRequest(m_server);
		SettingsDecryptionResult result = m_settingsDecrypter.decrypt(request);
		
		return result.getServer();
	}
}
