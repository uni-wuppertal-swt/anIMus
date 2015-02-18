package encryption.org.watzlawek;

import java.security.Key;

import android.util.Log;

public class SaltedAndPepperedKey implements Key {

	private String algorithm;
	private String format;
	private String pepper;
	private byte[] saltedKey;
	private int keylength;
	
	SaltedAndPepperedKey (byte[] saltedKey, int keylength, String algorithm, String format, String pepper){
		this.algorithm = algorithm;
		this.format = format;
		this.pepper = pepper;
		this.saltedKey = saltedKey;
		this.keylength = keylength;
	}

	SaltedAndPepperedKey (Key key, byte[] moreSalt, String morePepper){
		this.algorithm = key.getAlgorithm();
		this.format = key.getFormat();
		this.pepper = morePepper;
		this.saltedKey = new byte[key.getEncoded().length + moreSalt.length];
		this.keylength = key.getEncoded().length;
		
		byte[] b = key.getEncoded();
		
		for(int i = 0;i < this.saltedKey.length ;i++)
			if(i < b.length)this.saltedKey[i] = b[i];
			else this.saltedKey[i] = moreSalt[i - b.length];
	}
	
	
	/**
	 * 
	 * @see java.security.Key#getEncoded()
	 */
	public byte[] getEncoded() {
		byte[] key = new byte[this.keylength];
		for(int i = 0; i < this.keylength; i++)key[i] = saltedKey[i];
		return key;
	}
	/*
	 * (non-Javadoc)
	 * @see java.security.Key#getFormat()
	 */
	public String getFormat() {return format;}
	/*
	 * (non-Javadoc)
	 * @see java.security.Key#getAlgorithm()
	 */
	public String getAlgorithm() {return algorithm;}

	/**
	 * 
	 */
	byte[] getSaltedEncoded(){ return saltedKey; }
	int getKeyLength(){ return keylength;}
	String getPepper(){ return pepper; }
	
}
