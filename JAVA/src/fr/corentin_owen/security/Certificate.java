package fr.corentin_owen.security;

import org.json.JSONObject;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


/**
 * Class {@link Certificate}
 *
 * @author Corentin - Owen
 * @version 12/2021
 */
public class Certificate {

    /**
     * Certificate
     */
    private final String machine;
    private final String IP;
    private final String publicKey;
    private final String ACName;
    private final String ACIp;
    private byte[] signature;

    /**
     * Constructor by initialisation
     *
     * @param machine   the machine name
     * @param IP        the machine ip
     * @param publicKey the public key
     * @param ACName    the authority name
     * @param ACIp      the authority ip
     */
    public Certificate(String machine, String IP, byte[] publicKey, String ACName, String ACIp) {
        this.machine = machine;
        this.IP = IP;
        this.publicKey = Base64.getEncoder().encodeToString(publicKey);
        this.ACName = ACName;
        this.ACIp = ACIp;
        this.signature = null;
    }

    /**
     * Constructor by initialisation
     *
     * @param machine   the machine name
     * @param IP        the machine ip
     * @param publicKey the public key
     * @param ACName    the authority name
     * @param ACIp      the authority ip
     * @param signature the signature
     */
    public Certificate(String machine, String IP, byte[] publicKey, String ACName, String ACIp, String signature) {
        this.machine = machine;
        this.IP = IP;
        this.publicKey = Base64.getEncoder().encodeToString(publicKey);
        this.ACName = ACName;
        this.ACIp = ACIp;
        this.signature = Base64.getDecoder().decode(signature);
    }

    /**
     * Get the machine name
     *
     * @return the machine name
     */
    public String getMachine() {
        return machine;
    }


    /**
     * Get the machine ip
     *
     * @return the machine ip
     */
    public String getIP() {
        return IP;
    }


    /**
     * Get the public key
     *
     * @return the public key
     */
    public String getPublicKey() {
        return publicKey;
    }


    /**
     * Get the authority name
     *
     * @return the authority name
     */
    public String getACName() {
        return ACName;
    }


    /**
     * Get the authority ip
     *
     * @return the authority ip
     */
    public String getACIp() {
        return ACIp;
    }


    /**
     * Get the signature
     *
     * @return the signature
     */
    public byte[] getSignature() {
        return signature;
    }

    /**
     * Transform the Certificate to a JSON object
     *
     * @return the json object
     */
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("machine", this.machine);
        jsonObject.put("ip", this.IP);
        jsonObject.put("publicKey", this.publicKey);
        jsonObject.put("acName", this.ACName);
        jsonObject.put("acIp", this.ACIp);
        jsonObject.put("signature", Base64.getEncoder().encodeToString(this.signature));
        return jsonObject;
    }

    /**
     * Create a Certificate from json
     *
     * @param jsonObject the json
     * @return the new certificate
     */
    public static Certificate fromJson(JSONObject jsonObject) {
        String machine = jsonObject.getString("machine");
        String ip = jsonObject.getString("ip");
        byte[] publicKey = Base64.getDecoder().decode(jsonObject.getString("publicKey"));
        String acName = jsonObject.getString("acName");
        String acIp = jsonObject.getString("acIp");
        String signature = jsonObject.getString("signature");

        return new Certificate(machine, ip, publicKey, acName, acIp, signature);
    }

    /**
     * Signe the certificate
     *
     * @param authorityPrivateKey the private key of authority
     */
    public void sign(byte[] authorityPrivateKey) {
        Signature signatureV = null;
        KeyFactory kf = null;
        try {
            signatureV = Signature.getInstance("SHA256withRSA");
            kf = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error when initialising the signature: " + e);
            System.exit(0);
        }

        try {
            signatureV.initSign(kf.generatePrivate(new PKCS8EncodedKeySpec(authorityPrivateKey)));
        } catch (InvalidKeyException | InvalidKeySpecException e) {
            System.err.println("Invalid private key: " + e);
            System.exit(0);
        }

        String[] data = {
                "machine", this.getMachine(), "IP", this.getIP(),
                "publicKey", this.publicKey,
                "ACName", this.getACName(), "ACIp", this.getACIp()
        };

        try {
            for (String datum : data) {
                byte[] tampon = datum.getBytes();
                signatureV.update(tampon, 0, tampon.length);
            }
            this.signature = signatureV.sign();
        } catch (SignatureException e) {
            System.err.println("Error when updating the signature: " + e);
            System.exit(0);
        }
    }

    /**
     * Verify the signature
     *
     * @param authorityPublicKey the public key of authority
     * @return true or false
     */
    public boolean checkSignature(byte[] authorityPublicKey) {
        Signature signatureV = null;
        KeyFactory kf = null;
        boolean isValid = true;

        try {
            signatureV = Signature.getInstance("SHA256withRSA");
            kf = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error when initialising the signature: " + e);
            System.exit(0);
        }

        try {
            signatureV.initVerify(kf.generatePublic(new X509EncodedKeySpec(authorityPublicKey)));
        } catch (InvalidKeyException | InvalidKeySpecException e) {
            System.err.println("Invalid public key: " + e);
            System.exit(0);
        }

        String[] data = {
                "machine", this.getMachine(), "IP", this.getIP(),
                "publicKey", this.publicKey,
                "ACName", this.getACName(), "ACIp", this.getACIp()
        };

        try {
            for (String datum : data) {
                byte[] tampon = datum.getBytes();
                signatureV.update(tampon, 0, tampon.length);
            }

            isValid = signatureV.verify(this.signature);

        } catch (SignatureException e) {
            System.err.println("Error when updating the signature: " + e);
            System.exit(0);
        }

        return isValid;
    }
}