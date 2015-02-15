package encryption.org.watzlawek;

public interface EncryptionEngineConfig {

	public int getKeyLength();
	public int getSaltLength();
	public String getKeyExchangeCoreID();
	
}
