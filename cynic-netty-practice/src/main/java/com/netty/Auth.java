package com.netty;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

/**
 * @author cynic
 * @version 1.0
 * @describe todo
 * @createTime 2020/5/20 13:43
 */
public class Auth {
    public static final String AUTH_CODE = "auth-code";
    public static final String DES_KEY = "waiqin365_auth";

    private String tenantCode;
    private String userCode;
    private String password;

    public Auth(String tenantCode, String userCode, String password)
    {
        this.tenantCode = tenantCode;
        this.userCode = userCode;
        this.password = password;
    }

    public String getAuthCode()
    {
        DesUtils des = new DesUtils(DES_KEY);
        try
        {
            return des.encrypt(String.format("%s|%s|%s", tenantCode, userCode, password));
        }
        catch (Exception e)
        {
            return "";
        }
    }

    public String getEncryptPassword()
    {
        DesUtils des = new DesUtils(DES_KEY);
        try
        {
            return des.encrypt(password);
        }
        catch (Exception e)
        {
            return "";
        }
    }

    public Header getAuthHeader()
    {
        return new BasicHeader(AUTH_CODE, getAuthCode());
    }

    public String getTenantCode()
    {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode)
    {
        this.tenantCode = tenantCode;
    }

    public String getUserCode()
    {
        return userCode;
    }

    public void setUserCode(String userCode)
    {
        this.userCode = userCode;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
