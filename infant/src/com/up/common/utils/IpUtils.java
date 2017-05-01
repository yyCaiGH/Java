package com.up.common.utils;

import java.net.*;
import java.util.Enumeration;
import java.util.List;

/**
 * TODO:
 * Created by 王剑洪
 * on 2016/12/11 0011. 22:39
 */
public class IpUtils {
    public static String localIp() {
        String ip = "";
        Enumeration allNetInterfaces;
        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                List<InterfaceAddress> InterfaceAddress = netInterface.getInterfaceAddresses();
                for (InterfaceAddress add : InterfaceAddress) {
                    InetAddress Ip = add.getAddress();
                    if (Ip != null && Ip instanceof Inet4Address) {
                        ip = Ip.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            System.out.println("获取本机Ip失败:异常信息:" + e.getMessage());
        }
        return ip;
    }
}
