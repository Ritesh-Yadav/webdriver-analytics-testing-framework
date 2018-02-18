package io.github.riteshyadav.utils;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.mitm.PemFileCertificateSource;
import net.lightbody.bmp.mitm.RootCertificateGenerator;
import net.lightbody.bmp.mitm.keys.RSAKeyGenerator;
import net.lightbody.bmp.mitm.manager.ImpersonatingMitmManager;
import net.lightbody.bmp.proxy.CaptureType;

import java.io.File;

public class ProxyServer {

    private static Boolean PROXY_ENABLED;
    private final String SECRET_PASSWORD = "secretPassword";

    public static Boolean enableProxy() {
        if (PROXY_ENABLED == null) {
            PROXY_ENABLED = false;
            if ("true".equalsIgnoreCase(System.getProperty("enableProxy"))) {
                PROXY_ENABLED = true;
                return true;
            } else if ("false".equalsIgnoreCase(System.getProperty("enableProxy"))) {
                PROXY_ENABLED = false;
                return false;
            } else if ("PROD".equalsIgnoreCase(System.getProperty("env"))) {
                PROXY_ENABLED = true;
                return true;
            }
        }
        return PROXY_ENABLED;
    }

    public BrowserMobProxy startProxyServer() {

        // Loading previously generated certificate
        File privateKeyFile = getPrivateKeyFile();
        File certificateFile = getCertificateFile();
        PemFileCertificateSource fileCertificateSource = new PemFileCertificateSource(
                certificateFile,
                privateKeyFile,
                SECRET_PASSWORD);

        ImpersonatingMitmManager mitmManager = ImpersonatingMitmManager.builder()
                .rootCertificateSource(fileCertificateSource)
                .build();

        // when using BrowserMob Proxy:
        BrowserMobProxy proxy = new BrowserMobProxyServer();
        proxy.setMitmManager(mitmManager);
        proxy.setTrustAllServers(true);

        proxy.start();

        // enable more detailed HAR capture, if desired (see CaptureType for the complete list)
        proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.REQUEST_COOKIES, CaptureType.REQUEST_HEADERS);

        return proxy;
    }

    private File getPrivateKeyFile() {
        return new File(getClass().getClassLoader().getResource("certs/proxy.pem").getFile());
    }

    private File getCertificateFile() {
        return new File(getClass().getClassLoader().getResource("certs/proxy.cer").getFile());
    }

    private File getPKCS12File() {
        return new File(getClass().getClassLoader().getResource("certs/proxy.p12").getFile());
    }

    private void generateRSACertificate() {

        RootCertificateGenerator ecRootCertificateGenerator = RootCertificateGenerator.builder()
                .keyGenerator(new RSAKeyGenerator())
                .build();

        ecRootCertificateGenerator.saveRootCertificateAsPemFile(getPrivateKeyFile());

        ecRootCertificateGenerator.savePrivateKeyAsPemFile(getPrivateKeyFile(), SECRET_PASSWORD);

        ecRootCertificateGenerator.saveRootCertificateAndKey("PKCS12", getPKCS12File(),
                "BrowserMob-Proxy", "secretPassword");

    }
}
