package server;

public interface CacheManager<P, S> {
	boolean hasCache(P problem);
	void addCache(P problem, S solution);
	S getCache(P problem);
	void saveCache();
}
