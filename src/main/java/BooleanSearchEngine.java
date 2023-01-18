import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {

    private Map<String, List<PageEntry>> index = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {

        List<File> nameFile = List.of(Objects.requireNonNull(pdfsDir.listFiles()));
        for (File pdf : nameFile) {
            var doc = new PdfDocument(new PdfReader(pdf));
            for (int i = 0; i < doc.getNumberOfPages(); i++) {

                var text = PdfTextExtractor.getTextFromPage(doc.getPage(i + 1));
                var words = text.split("\\P{IsAlphabetic}+");
                Map<String, Integer> freqs = new HashMap<>();
                for (var word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    word = word.toLowerCase();
                    freqs.put(word, freqs.getOrDefault(word, 0) + 1);
                }
                for (var word : freqs.keySet()) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    List<PageEntry> pageEntryList = new ArrayList<>();
                    if (index.containsKey(word)) {
                        index.get(word).add(new PageEntry(pdf.getName(), i + 1, freqs.get(word)));
                    } else {
                        pageEntryList.add(new PageEntry(pdf.getName(), i + 1, freqs.get(word)));
                        index.put(word, pageEntryList);
                    }
                    index.get(word).sort(Collections.reverseOrder());
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        return index.get(word.toLowerCase());
    }
}
