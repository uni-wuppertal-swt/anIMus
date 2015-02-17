package encryption.org.watzlawek;

public interface EncryptionEngineConfig {

	public int getKeyLength();
	public int getSaltLength();
	public String getKeyExchangeCoreID();
	public String getForcedCoreID();
	public int getMinimumKeys();
	public int getMaximumKeys();
	public int getRecommendedKeysinOneMessage();
	
}
