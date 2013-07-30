package com.fennyfatal.grooveapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

public class Utils {
	
	
	
	public static String[] getRawUrlRequest(String hostname,String path, int port,boolean usessl) throws IOException {
		String[] headers = {
		"GET "+path+" HTTP/1.1",
        "Host: " + hostname,
        "Accept: */*",
        "Mozilla/5.0 (Windows NT 6.1)"}; // LIE LIKE A WHORE.
		return getRawUrlRequest(hostname, headers, null , port, usessl);
	}
	public static String[] getRawUrlRequest(String hostname, String[] headers, String body, int port, boolean usessl) throws IOException {
        Socket socket = null;
        PrintWriter writer = null;
        BufferedReader reader = null;
        String retval = "";
        String[] theWholeThing = {null,null}; //TODO define an object to hold this.
        try {
        	if (usessl)
        	{
        		SocketFactory factory = SSLSocketFactory.getDefault();
        		socket = factory.createSocket(hostname, port);
        	}
            else
            	socket = new Socket(hostname, port);
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            for (String header : headers)
            	writer.println(header);
            writer.println("");
            if (body != null)
            	writer.println(body);
            writer.flush();

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            for (String line; (line = reader.readLine()) != null;) {
                if (line.isEmpty()) break; // Stop when headers are completed. We're not interested in all the HTML.
                retval = retval+line+'\n';
            }
            theWholeThing[0] = retval;
            boolean chunked = retval.contains("Transfer-Encoding: chunked");
            retval = "";
            int count = 0;
            for (String line; (line = reader.readLine()) != null;) {
            	if (!chunked || !(line.length() == 4 && count == 0))
            	{
            		if (chunked && line.equals("a000"))
            			retval = retval.substring(0, retval.length()-1);
            		else
            		{
            			retval += line + '\n';
            		}
            	}
            	count++;
            }
            theWholeThing[1] = retval;
            
        } finally {
            if (reader != null) try { reader.close(); } catch (IOException logOrIgnore) {} 
            if (writer != null) { writer.close(); }
            if (socket != null) try { socket.close(); } catch (IOException logOrIgnore) {} 
        }
        return theWholeThing;
    }
	public static final String md5(final String s) {
	    try {
	        // Create MD5 Hash
	        MessageDigest digest = java.security.MessageDigest
	                .getInstance("MD5");
	        digest.update(s.getBytes());
	        byte messageDigest[] = digest.digest();

	        // Create Hex String
	        StringBuffer hexString = new StringBuffer();
	        for (int i = 0; i < messageDigest.length; i++) {
	            String h = Integer.toHexString(0xFF & messageDigest[i]);
	            while (h.length() < 2)
	                h = "0" + h;
	            hexString.append(h);
	        }
	        return hexString.toString();

	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return "";
	}
    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public final static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }
}
