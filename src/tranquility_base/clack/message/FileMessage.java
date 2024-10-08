package tranquility_base.clack.message;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

/**
 * This class represents messages containing the name and
 * contents of a text file.
 *
 * @author D. Tuinstra, adapted from work by Soumyabrata Dey.
 */
public class FileMessage extends Message {

    private String filePath;
    private String fileSaveAsName;
    private String fileContents;

    /**
     * Constructs a FileMessage object with a given username
     * and file paths. It does not read in the file contents --
     * that must be done with readFile(). The fileSaveAsPath is
     * not used in its entirety: only the filename portion of
     * is kept and used when saving the message's file contents.
     *
     * @param username       name of user for this message.
     * @param filePath       where to find the file to read.
     * @param fileSaveAsPath the filename portion of this is used when saving the file.
     */
    public FileMessage(String username, String filePath, String fileSaveAsPath) {
        super(username, MSGTYPE_FILE);
        this.filePath = filePath;
        this.fileSaveAsName = new File(fileSaveAsPath).getName();
        this.fileContents = "";
    }

    /**
     * Constructs a FileMessage object with a given username,
     * and a given filePath to give both the reading and saving
     * location of the file. It does not read in the file contents --
     * that must be done with readFile().
     *
     * @param username name of user for this message.
     * @param filePath where to find the file to read, and where to write it.
     */
    public FileMessage(String username, String filePath) {
        this(username, filePath, filePath);
    }

    /**
     * Get the path, on the local file system, of the file to read.
     *
     * @return the path to the file to read.
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Get the current value of the fileContents field.
     * @return the fileContents field.
     */
    public String getFileContents() {
        return fileContents;
    }

    /**
     * Set the path where the file-to-read is located. This does not
     * cause the file to be read -- that must be done with readFile().
     *
     * @param filePath new file name to associate with this message.
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Get the path where the file is to be written.
     *
     * @return the path where the file is to be written.
     */
    public String getFileSaveAsName() {
        return this.fileSaveAsName;
    }

    /**
     * Set the name for the file to be written. This does not
     * cause the file to be written -- that must be done with
     * writeFile(). It is an IllegalArgument exception if the
     * fileSaveAsName contains path components.
     *
     * @throws IllegalArgumentException if fileSaveAsName
     *                                  contains path components
     */
    public void setFileSaveAsName(String newFileSaveAsName) {
        this.fileSaveAsName = (new File(newFileSaveAsName)).getName();
    }

    /**
     * Returns a three-element array of String. The first element is
     * the current filePath value, the second is the current
     * fileSaveAsName value, and the third is the current
     * fileContents value. The method does <b><em>not</em></b> read
     * the file named by filename -- that must be done with readFile().
     *
     * @return the current values of filePath, fileSaveAsName, and fileContents.
     */
    @Override
    public String[] getData() {
        return new String[]{this.filePath,
                this.fileSaveAsName,
                this.fileContents};
    }

    /**
     * Read contents of file given by 'filePath' into this message's
     * fileContents.
     *
     * @throws IOException if the file indicated by this.filePath does
     *                     not exist or cannot be opened for reading.
     */
    /* Since Java 11, there's an easy way to do this. It even handles
     * closing the files when done, whether normally or by Exception
     * (so we don't need to use try-with-resources). See
       https://howtodoinjava.com/java/io/java-read-file-to-string-examples/
     */
    public void readFile() throws IOException {
        Path filePath = Path.of(this.filePath);
        this.fileContents = Files.readString(filePath);
    }

    /**
     * Write this message's fileContents to the local Clack directory.
     *
     * @throws FileNotFoundException if file cannot be found or created,
     *                               or opened for writing.
     */
    public void writeFile() throws FileNotFoundException {
        // HOLD OFF FOR NOW ON IMPLEMENTING THIS. There is a design
        //   issue that we'll discuss in class.
        try (PrintStream output = new PrintStream(this.fileSaveAsName)) {
            output.print(this.fileContents);
        }
    }

    /**
     * Constructs a string representation of this object:
     * <pre>
     *   "{class=TextMessage"
     *   + "|timestamp=<i>timestamp</i>"
     *   + "|username=<i>username</i>"
     *   + "|filePath=<i>file path</i>"
     *   + "|fileSaveAsName=<i>file Save As name</i>"
     *   + "|fileContents=<i>file contents</i>}"
     *   </pre>
     * where the fileSaveAsName is just a file name (no path
     * components), and is the name that will be used when
     * saving a file. Such files will be saved into the user's
     * current working directory.
     *
     * @return this object's string representation.
     */
    @Override
    public String toString() {
        return "{class=FileMessage|"
                + super.toString()
                + "|filePath=" + this.filePath
                + "|fileSaveAsName=" + this.fileSaveAsName
                + "|fileContents=" + this.fileContents
                + "}";
    }

    @Override
    public boolean equals(Object o) {
        //TODO: Implement this.
        if (o == this) {
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        FileMessage that = (FileMessage) o;
        return Objects.equals(this.getTimestamp(), that.getTimestamp())
                && Objects.equals(this.getUsername(), that.getUsername())
                && Arrays.equals(this.getData(), that.getData());
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

}