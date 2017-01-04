package com.trusteer.interview.dsysme.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * https://www.mkyong.com/regular-expressions/how-to-validate-ip-address-with-regular-expression/
 */
 public enum IPAddressValidator {

    INSTANCE;

    private Pattern pattern;
    private Matcher matcher;

    private static final String IPADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    IPAddressValidator(){
        pattern = Pattern.compile(IPADDRESS_PATTERN);
    }

    /**
     * Validate ip address with regular expression
     * @param ip ip address for validation
     * @return true valid ip address, false invalid ip address
     */
    public String validate(final String ip) throws Exception{
        matcher = pattern.matcher(ip);
        if (matcher.matches())
            return ip;
        throw new Exception("Malformed ip address");
    }
}


