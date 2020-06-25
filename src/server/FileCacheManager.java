package server;

import java.io.*;
import java.util.HashMap;

public class FileCacheManager<P, S> implements CacheManager<P, S> {
	private File file;
	private HashMap<P, S> solved;

	@SuppressWarnings("unchecked")
	public FileCacheManager(String fileName) {
		this.solved = new HashMap<>();
		this.file = new File(fileName);

		ObjectInputStream in = null;
		try {
			this.file.createNewFile();
			in = new ObjectInputStream(new FileInputStream(this.file));
			this.solved = (HashMap<P, S>) in.readObject();

		} catch (FileNotFoundException | EOFException ignore) {
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();

		} finally {
			if(in != null)
				 try {
					 in.close();
				 } catch (Exception ignore) {}
		}
	}

	@Override
	public boolean hasCache(P problem) {
		return this.solved.containsKey(problem);
	}

	@Override
	public void addCache(P problem, S solution) {
		if(this.hasCache(problem))
			return;

		solved.put(problem, solution);
	}

	@Override
	public S getCache(P problem) {
		return this.solved.get(problem);
	}

	public void saveCache() {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(this.file))) {
			out.writeObject(this.solved);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
