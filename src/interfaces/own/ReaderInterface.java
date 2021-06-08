package interfaces.own;

import java.util.List;

public interface ReaderInterface<E> {
    /**
     * Reads and saves data from the file whose location is stored inside the class.
     */
    public void read();

    /**
     * Stores the file path inside the class
     *
     * @param path - the entire file path where our target data file is at.
     */
    public void setFilePath(String path);

    /**
     * Gets the file data stored in a list
     *
     * @return The list containing data objects extracted from the file
     */
    public List<E> getList();

    /**
     * Gets the file path that is remembered inside the class
     *
     * @return A String containing the entire path to where the data file is located at
     */
    public String getFilePath();
}
