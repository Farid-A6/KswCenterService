package android.net.wifi.hotspot2;

import android.net.wifi.hotspot2.omadm.PpsMoParser;
import android.security.KeyChain;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ConfigParser {
    private static final String BOUNDARY = "boundary=";
    private static final String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String ENCODING_BASE64 = "base64";
    private static final String TAG = "ConfigParser";
    private static final String TYPE_CA_CERT = "application/x-x509-ca-cert";
    private static final String TYPE_MULTIPART_MIXED = "multipart/mixed";
    private static final String TYPE_PASSPOINT_PROFILE = "application/x-passpoint-profile";
    private static final String TYPE_PKCS12 = "application/x-pkcs12";
    private static final String TYPE_WIFI_CONFIG = "application/x-wifi-config";

    private static class MimePart {
        public byte[] data;
        public boolean isLast;
        public String type;

        private MimePart() {
            this.type = null;
            this.data = null;
            this.isLast = false;
        }
    }

    private static class MimeHeader {
        public String boundary;
        public String contentType;
        public String encodingType;

        private MimeHeader() {
            this.contentType = null;
            this.boundary = null;
            this.encodingType = null;
        }
    }

    public static PasspointConfiguration parsePasspointConfig(String mimeType, byte[] data) {
        if (!TextUtils.equals(mimeType, TYPE_WIFI_CONFIG)) {
            Log.e(TAG, "Unexpected MIME type: " + mimeType);
            return null;
        }
        try {
            return createPasspointConfig(parseMimeMultipartMessage(new LineNumberReader(new InputStreamReader(new ByteArrayInputStream(Base64.decode(new String(data, StandardCharsets.ISO_8859_1), 0)), StandardCharsets.ISO_8859_1))));
        } catch (IOException | IllegalArgumentException e) {
            Log.e(TAG, "Failed to parse installation file: " + e.getMessage());
            return null;
        }
    }

    private static PasspointConfiguration createPasspointConfig(Map<String, byte[]> mimeParts) throws IOException {
        byte[] profileData = mimeParts.get(TYPE_PASSPOINT_PROFILE);
        if (profileData != null) {
            PasspointConfiguration config = PpsMoParser.parseMoText(new String(profileData));
            if (config == null) {
                throw new IOException("Failed to parse Passpoint profile");
            } else if (config.getCredential() != null) {
                byte[] caCertData = mimeParts.get(TYPE_CA_CERT);
                if (caCertData != null) {
                    try {
                        config.getCredential().setCaCertificate(parseCACert(caCertData));
                    } catch (CertificateException e) {
                        throw new IOException("Failed to parse CA Certificate");
                    }
                }
                byte[] pkcs12Data = mimeParts.get(TYPE_PKCS12);
                if (pkcs12Data != null) {
                    try {
                        Pair<PrivateKey, List<X509Certificate>> clientKey = parsePkcs12(pkcs12Data);
                        config.getCredential().setClientPrivateKey((PrivateKey) clientKey.first);
                        config.getCredential().setClientCertificateChain((X509Certificate[]) ((List) clientKey.second).toArray(new X509Certificate[((List) clientKey.second).size()]));
                    } catch (IOException | GeneralSecurityException e2) {
                        throw new IOException("Failed to parse PCKS12 string");
                    }
                }
                return config;
            } else {
                throw new IOException("Passpoint profile missing credential");
            }
        } else {
            throw new IOException("Missing Passpoint Profile");
        }
    }

    private static Map<String, byte[]> parseMimeMultipartMessage(LineNumberReader in) throws IOException {
        String line;
        MimePart mimePart;
        MimeHeader header = parseHeaders(in);
        if (!TextUtils.equals(header.contentType, TYPE_MULTIPART_MIXED)) {
            throw new IOException("Invalid content type: " + header.contentType);
        } else if (TextUtils.isEmpty(header.boundary)) {
            throw new IOException("Missing boundary string");
        } else if (TextUtils.equals(header.encodingType, ENCODING_BASE64)) {
            do {
                line = in.readLine();
                if (line != null) {
                } else {
                    throw new IOException("Unexpected EOF before first boundary @ " + in.getLineNumber());
                }
            } while (!line.equals("--" + header.boundary));
            Map<String, byte[]> mimeParts = new HashMap<>();
            do {
                mimePart = parseMimePart(in, header.boundary);
                mimeParts.put(mimePart.type, mimePart.data);
            } while (!mimePart.isLast);
            return mimeParts;
        } else {
            throw new IOException("Unexpected encoding: " + header.encodingType);
        }
    }

    private static MimePart parseMimePart(LineNumberReader in, String boundary) throws IOException {
        MimeHeader header = parseHeaders(in);
        if (!TextUtils.equals(header.encodingType, ENCODING_BASE64)) {
            throw new IOException("Unexpected encoding type: " + header.encodingType);
        } else if (TextUtils.equals(header.contentType, TYPE_PASSPOINT_PROFILE) || TextUtils.equals(header.contentType, TYPE_CA_CERT) || TextUtils.equals(header.contentType, TYPE_PKCS12)) {
            StringBuilder text = new StringBuilder();
            boolean isLast = false;
            String partBoundary = "--" + boundary;
            String endBoundary = partBoundary + "--";
            while (true) {
                String line = in.readLine();
                if (line == null) {
                    throw new IOException("Unexpected EOF file in body @ " + in.getLineNumber());
                } else if (line.startsWith(partBoundary)) {
                    if (line.equals(endBoundary)) {
                        isLast = true;
                    }
                    MimePart part = new MimePart();
                    part.type = header.contentType;
                    part.data = Base64.decode(text.toString(), 0);
                    part.isLast = isLast;
                    return part;
                } else {
                    text.append(line);
                }
            }
        } else {
            throw new IOException("Unexpected content type: " + header.contentType);
        }
    }

    private static MimeHeader parseHeaders(LineNumberReader in) throws IOException {
        MimeHeader header = new MimeHeader();
        for (Map.Entry<String, String> entry : readHeaders(in).entrySet()) {
            String key = entry.getKey();
            char c = 65535;
            int hashCode = key.hashCode();
            if (hashCode != 747297921) {
                if (hashCode == 949037134 && key.equals(CONTENT_TYPE)) {
                    c = 0;
                }
            } else if (key.equals(CONTENT_TRANSFER_ENCODING)) {
                c = 1;
            }
            switch (c) {
                case 0:
                    Pair<String, String> value = parseContentType(entry.getValue());
                    header.contentType = (String) value.first;
                    header.boundary = (String) value.second;
                    break;
                case 1:
                    header.encodingType = entry.getValue();
                    break;
                default:
                    Log.d(TAG, "Ignore header: " + entry.getKey());
                    break;
            }
        }
        return header;
    }

    private static Pair<String, String> parseContentType(String contentType) throws IOException {
        String[] attributes = contentType.split(";");
        if (attributes.length >= 1) {
            String type = attributes[0].trim();
            String boundary = null;
            for (int i = 1; i < attributes.length; i++) {
                String attribute = attributes[i].trim();
                if (!attribute.startsWith(BOUNDARY)) {
                    Log.d(TAG, "Ignore Content-Type attribute: " + attributes[i]);
                } else {
                    boundary = attribute.substring(BOUNDARY.length());
                    if (boundary.length() > 1 && boundary.startsWith("\"") && boundary.endsWith("\"")) {
                        boundary = boundary.substring(1, boundary.length() - 1);
                    }
                }
            }
            return new Pair<>(type, boundary);
        }
        throw new IOException("Invalid Content-Type: " + contentType);
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x00af  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.util.Map<java.lang.String, java.lang.String> readHeaders(java.io.LineNumberReader r8) throws java.io.IOException {
        /*
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            r1 = 0
            r2 = 0
        L_0x0007:
            java.lang.String r3 = r8.readLine()
            if (r3 == 0) goto L_0x00b7
            int r4 = r3.length()
            if (r4 == 0) goto L_0x00ad
            java.lang.String r4 = r3.trim()
            int r4 = r4.length()
            if (r4 != 0) goto L_0x001f
            goto L_0x00ad
        L_0x001f:
            r4 = 58
            int r4 = r3.indexOf(r4)
            if (r4 >= 0) goto L_0x0059
            if (r2 == 0) goto L_0x0036
            r5 = 32
            r2.append(r5)
            java.lang.String r5 = r3.trim()
            r2.append(r5)
            goto L_0x0088
        L_0x0036:
            java.io.IOException r5 = new java.io.IOException
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "Bad header line: '"
            r6.append(r7)
            r6.append(r3)
            java.lang.String r7 = "' @ "
            r6.append(r7)
            int r7 = r8.getLineNumber()
            r6.append(r7)
            java.lang.String r6 = r6.toString()
            r5.<init>(r6)
            throw r5
        L_0x0059:
            r5 = 0
            char r6 = r3.charAt(r5)
            boolean r6 = java.lang.Character.isWhitespace(r6)
            if (r6 != 0) goto L_0x008a
            if (r1 == 0) goto L_0x006d
            java.lang.String r6 = r2.toString()
            r0.put(r1, r6)
        L_0x006d:
            java.lang.String r5 = r3.substring(r5, r4)
            java.lang.String r1 = r5.trim()
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            r2 = r5
            int r5 = r4 + 1
            java.lang.String r5 = r3.substring(r5)
            java.lang.String r5 = r5.trim()
            r2.append(r5)
        L_0x0088:
            goto L_0x0007
        L_0x008a:
            java.io.IOException r5 = new java.io.IOException
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "Illegal blank prefix in header line '"
            r6.append(r7)
            r6.append(r3)
            java.lang.String r7 = "' @ "
            r6.append(r7)
            int r7 = r8.getLineNumber()
            r6.append(r7)
            java.lang.String r6 = r6.toString()
            r5.<init>(r6)
            throw r5
        L_0x00ad:
            if (r1 == 0) goto L_0x00b6
            java.lang.String r4 = r2.toString()
            r0.put(r1, r4)
        L_0x00b6:
            return r0
        L_0x00b7:
            java.io.IOException r4 = new java.io.IOException
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = "Missing line @ "
            r5.append(r6)
            int r6 = r8.getLineNumber()
            r5.append(r6)
            java.lang.String r5 = r5.toString()
            r4.<init>(r5)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.hotspot2.ConfigParser.readHeaders(java.io.LineNumberReader):java.util.Map");
    }

    private static X509Certificate parseCACert(byte[] octets) throws CertificateException {
        return (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(octets));
    }

    private static Pair<PrivateKey, List<X509Certificate>> parsePkcs12(byte[] octets) throws GeneralSecurityException, IOException {
        KeyStore ks = KeyStore.getInstance(KeyChain.EXTRA_PKCS12);
        ByteArrayInputStream in = new ByteArrayInputStream(octets);
        int i = 0;
        ks.load(in, new char[0]);
        in.close();
        if (ks.size() == 1) {
            String alias = ks.aliases().nextElement();
            if (alias != null) {
                PrivateKey clientKey = (PrivateKey) ks.getKey(alias, (char[]) null);
                List<X509Certificate> clientCertificateChain = null;
                Certificate[] chain = ks.getCertificateChain(alias);
                if (chain != null) {
                    clientCertificateChain = new ArrayList<>();
                    int length = chain.length;
                    while (i < length) {
                        Certificate certificate = chain[i];
                        if (certificate instanceof X509Certificate) {
                            clientCertificateChain.add((X509Certificate) certificate);
                            i++;
                        } else {
                            throw new IOException("Unexpceted certificate type: " + certificate.getClass());
                        }
                    }
                }
                return new Pair<>(clientKey, clientCertificateChain);
            }
            throw new IOException("No alias found");
        }
        throw new IOException("Unexpected key size: " + ks.size());
    }
}
