package jpmc.interview.assignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WordOccurrenceInFile {

	public static void main(String[] args) throws IOException {

		String fileName = WordOccurrenceInFile.class.getResource("gpl-3.0.txt").getFile();

		Map<String, Integer> wordOccurrences = getWordOccurrences(fileName);
		System.out.println(wordOccurrences);

	}

	public static Map<String, Integer> getWordOccurrences(String fileName) throws IOException {
		Map<String, Integer> wordOccurrences = new HashMap<String, Integer>();
		String line = null;

		File file = new File(fileName);
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferReader = new BufferedReader(fileReader);

		while ((line = bufferReader.readLine()) != null) {
			line = line.replaceAll("[^a-zA-Z0-9]", " "); // removing special characters
			String[] words = line.split("\\s+"); // spilt by white space
			for (String word : words) {
				word = word.toLowerCase();

				if (!word.isEmpty())
					wordOccurrences.put(word, wordOccurrences.getOrDefault(word, 0) + 1);
			}
		}
		return wordOccurrences;
	}
}
