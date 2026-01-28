import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Storage handles loading and saving tasks to a file.
 */
public class Storage {
    private final String filePath;

    /**
     * Creates a Storage object with the specified file path.
     *
     * @param filePath the path to the storage file
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the storage file.
     *
     * @return a list of task strings from the file
     * @throws BuddyException if there's an error reading the file
     */
    public List<String> load() throws BuddyException {
        List<String> tasks = new ArrayList<>();
        File file = new File(filePath);
        
        if (!file.exists()) {
            return tasks; // Return empty list if file doesn't exist yet
        }

        try {
            Path path = Paths.get(filePath);
            tasks = Files.readAllLines(path);
        } catch (IOException e) {
            throw new BuddyException("Error loading tasks from file: " + e.getMessage());
        }

        return tasks;
    }

    /**
     * Saves tasks to the storage file.
     *
     * @param tasks the list of task strings to save
     * @throws BuddyException if there's an error writing to the file
     */
    public void save(List<String> tasks) throws BuddyException {
        try {
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            
            // Create parent directory if it doesn't exist
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            FileWriter writer = new FileWriter(file);
            for (String task : tasks) {
                writer.write(task + System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            throw new BuddyException("Error saving tasks to file: " + e.getMessage());
        }
    }
}