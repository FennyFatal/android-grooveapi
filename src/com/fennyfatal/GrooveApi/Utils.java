package com.fennyfatal.GrooveApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.CharBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import android.annotation.SuppressLint;
import android.util.Log;

public class Utils {
	
	
	
	public static String[] getRawUrlRequest(String hostname,String path, int port,boolean usessl) {
		String[] headers = {
		"GET "+path+" HTTP/1.1",
        "Host: " + hostname,
        "Accept: */*",
        "Mozilla/5.0 (Windows NT 6.1)"}; // LIE LIKE A WHORE.
		return getRawUrlRequest(hostname, headers, null , port, usessl);
	}
	@SuppressLint("NewApi")
	public static String[] getRawUrlRequest(String hostname, String[] headers, String body, int port, boolean usessl) {
        Socket socket = null;
        PrintWriter writer = null;
        BufferedReader reader = null;
        char[] buf = new char[8192];
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
            try {
	            for (String line; (line = reader.readLine()) != null;) {
	            	if (!chunked)
	            	{
	            		retval += line;
	            	}
	            	else
	            	{
	            		int size=Integer.parseInt(line, 16);
	            		if (size == 0)
	            			{
	            			 reader.readLine();
	            			 break;
	            			}
	            		if (size > buf.length)
	            			buf = new char[size];
	            		int readCount;
	            		while ((readCount = reader.read(buf,0,size)) > 0)
	            		{
	            			retval += String.copyValueOf(buf, 0, readCount);
	            			if ((size -= readCount) <= 0)
	            			{
	            				String wat = reader.readLine(); //clear any newline characters.
	            				if (wat.length() > 0)
	            				{
	            					Log.w("ERROR", wat);
	            				}
	            				break;
	            			}
	            		}
	            	}
	            }
            } 
            catch (IOException e) 
            {}
            catch (Exception ec)
            {
            	ec.printStackTrace();
            }
            
            theWholeThing[1] = retval;
        } catch (IOException e){  
        } finally {
            if (reader != null) try { reader.close(); } catch (Exception logOrIgnore) {} 
            if (writer != null) try { writer.close(); } catch (Exception logOrIgnore) {}
            if (socket != null) try { socket.close(); } catch (Exception logOrIgnore) {} 
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
