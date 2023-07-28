package jpmc.interview.assignment;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class WordOccurrenceMultithreading {

	public static void main(String[] args) {
		
		String fileName= WordOccurrenceMultithreading.class.getResource("gpl-3.0.txt").getFile();
		Map<String, Integer> wordOccurrences = getWordOccurrences(fileName);
		System.out.println(wordOccurrences);
	}

	public static Map<String, Integer> getWordOccurrences(String fileName) {
		Map<String, Integer> wordOccurrences = new HashMap<String, Integer>();
		String line = null;

		File file = new File(fileName);
		FileReader fileReader;
		ExecutorService service = null;
		BufferedReader bufferReader = null;
		List<Future<Map<String, Integer>>> futures = null;
		try {
			fileReader = new FileReader(file);
			bufferReader = new BufferedReader(fileReader);
			service = Executors.newFixedThreadPool(10);
			futures = new ArrayList<Future<Map<String, Integer>>>();

			while ((line = bufferReader.readLine()) != null) {
				String[] words = line.replaceAll("[^a-zA-Z0-9]", " ").split("\\s+");
				Callable<Map<String, Integer>> task = new Callable<Map<String, Integer>>() {

					@Override
					public Map<String, Integer> call() throws Exception {
						Map<String, Integer> wordsInLineMap = new HashMap<String, Integer>();
						for (String word : words) {
							word = word.toLowerCase();
							if (!word.isEmpty())
								wordsInLineMap.put(word, wordsInLineMap.getOrDefault(word, 0) + 1);
						}
						return wordsInLineMap;
					}
				};
				Future<Map<String, Integer>> future = service.submit(task);
				futures.add(future);
			}

			for (Future<Map<String, Integer>> future : futures) {
				Map<String, Integer> map = future.get();
				for (String word : map.keySet()) {
					if (!word.isEmpty())
						wordOccurrences.put(word, wordOccurrences.getOrDefault(word, 0) + map.get(word));
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			service.shutdown();
		}
		return wordOccurrences;
	}
}
