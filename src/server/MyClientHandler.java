package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import search.*;

public class MyClientHandler implements ClientHandler {
	BestFirstSearcher bfs;
	FileCacheManager<Integer, String> fcm;

	public MyClientHandler() {
		this.fcm = new FileCacheManager<>("cache.dat");
	}
	
	private String getMove(Tuple<Integer, Integer> first,Tuple<Integer, Integer> second) {
		if(first.getElement1() > second.getElement1())
			return "Up";
		else if(first.getElement1() < second.getElement1())
			return "Down";
		else if(first.getElement2() > second.getElement2())
			return "Left";
		return "Right";
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void handleClient(InputStream in, OutputStream out) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		PrintWriter writer = new PrintWriter(out);
		String line;
		
		double[][] matrix = null;
		Tuple<Integer, Integer> initial = null, goal = null;
		
		List<double[]> matList = new ArrayList<>();
		try {
			while(true) {
				line = reader.readLine();
				if(line == null)
					continue;
				if(line.equals("end"))
					break;

				double[] tokens = null;
				try {
					tokens = Arrays.stream(line.split(",")).mapToDouble(Double::parseDouble).toArray();
					matList.add(tokens);
				} catch(Exception e) {
					e.printStackTrace();
					return;
				}
			}

			matrix = new double[matList.size()][];
			int j = 0;
			for(double[] arr : matList) {
				matrix[j] = arr;
				j++;
			}

			int[] tokens = null;

			line = reader.readLine();
			try {
				tokens = Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray();
				initial = new Tuple<>(tokens[0], tokens[1]);
			} catch(Exception e) {
				e.printStackTrace();
				return;
			}

			line = reader.readLine();
			try {
				tokens = Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray();
				goal = new Tuple<>(tokens[0], tokens[1]);
			} catch(Exception e) {
				e.printStackTrace();
				return;
			}

			MatrixSearchable ms = new MatrixSearchable(matrix, initial, goal);
			int key = ms.hashCode();

			if(!this.fcm.hasCache(key)) {
				System.out.println("calculated and saved to memory");
				BreadthFirstSearcher bfs = new BreadthFirstSearcher();
				List<State> backtrace = bfs.search(ms);
				Tuple<Integer, Integer>[] coordinates  = (Tuple<Integer, Integer>[]) new Tuple[backtrace.size()];

				for(int i = 0; i < coordinates.length; i++)
					coordinates[i] = ms.getStateIndex(backtrace.get(i));

				StringBuilder resultBuilder = new StringBuilder();
				for(int i = coordinates.length - 2; i > -1; i--)
					resultBuilder.append(getMove(coordinates[i + 1], coordinates[i])).append(",");
				resultBuilder.setLength(resultBuilder.length() - 1);
				this.fcm.addCache(key, resultBuilder.toString());
			} else {
				System.out.println("found and read from memory");
			}

			writer.println(this.fcm.getCache(key));
			writer.flush();
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			this.fcm.saveCache();
		}
	}

}
