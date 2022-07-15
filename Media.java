import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.*;
import static javax.swing.JOptionPane.*;

/**
 * @author Michel T.
 * Date: 07/03/2022
 * Purpose: Media Rental System for renting out media
 */


abstract class Media {
    protected static final double RENTAL_FEE = 5.50;
    private int id;
    private String title;
    private int year, chapter;
    private boolean available;

    public Media() {
        this.id = 0;
        this.title = "";
        this.year = 0;
        this.chapter = 0;
        this.available = true;
    }

    public Media(int id, String title, int year, int chapter, boolean available) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.chapter = chapter;
        this.available = available;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public abstract double calculateRentalFee();


}

class EBook extends Media {

    public EBook(int id, String title, int year, int chapter, boolean available) {
        super(id, title, year, chapter, available);
    }

    @Override
    public double calculateRentalFee() {
        return RENTAL_FEE;
    }

    @Override
    public String toString() {
        return "EBook [id:" + this.getId() + " title:" + this.getTitle() + " chapter:" + this.getChapter() + " year:" + this.getYear()
                + " available:" + this.isAvailable() + "]\n";
    }

}

class MovieDVD extends Media {

    public MovieDVD(int id, String title, int year, int chapter, boolean available) {
        super(id, title, year, chapter, available);
    }

    @Override
    public double calculateRentalFee() {
        return RENTAL_FEE;
    }

    @Override
    public String toString() {
        return "MovieDVD [id:" + this.getId() + " title:" + this.getTitle() + " chapter:" + this.getChapter() + " year:" + this.getYear()
                + " available:" + this.isAvailable() + "]\n";
    }

}

class MusicCD extends Media {

    public MusicCD(int id, String title, int year, int chapter, boolean available) {
        super(id, title, year, chapter, available);
    }

    @Override
    public double calculateRentalFee() {
        return RENTAL_FEE;
    }

    @Override
    public String toString() {
        return "MusicCD [id:" + this.getId() + " title:" + this.getTitle() + " chapter:" + this.getChapter() + " year:" + this.getYear()
                + " available:" + this.isAvailable() + "]\n";
    }

}

class Manager
{
    private List<Media> media;
    private int idCounter; //Keeps track for assigning new id to next media object

    public Manager(){
        media = new ArrayList<Media>();
        idCounter = 0;
        loadData();
    }

    public void loadData(){
        loadData("media.txt");
    }

    public List<Media> findItems(String searchTitle){
        List<Media> selectedMedia = new ArrayList<Media>();
        for (Media item : media) {
            if (item.getTitle().equals(searchTitle))
                selectedMedia.add(item);
        }
        return selectedMedia;
    }

    public Media rentItem(int id){
        for (Media item : media) {
            if (item.getId() == id && item.isAvailable()) {
                item.setAvailable(false);
                return item;
            }
        }
        return null;
    }


    public void loadData(String path){
        File dataFile = new File(path);
        try {
            Scanner fileScanner = new Scanner(dataFile);

            fileScanner.nextLine(); //Discard the header line from file
            while(fileScanner.hasNextLine()) {
                String[] fields = fileScanner.nextLine().trim().split("\\|");
                switch(fields[0]){
                    case "E-Book":
                        media.add(new EBook(idCounter++, fields[1], Integer.parseInt(fields[2]), Integer.parseInt(fields[3]),Boolean.parseBoolean(fields[4])));
                        break;
                    case "CD":
                        media.add(new MusicCD(idCounter++, fields[1], Integer.parseInt(fields[2]), Integer.parseInt(fields[3]),Boolean.parseBoolean(fields[4])));
                        break;
                    case "DVD":
                        media.add(new MovieDVD(idCounter++, fields[1], Integer.parseInt(fields[2]), Integer.parseInt(fields[3]),Boolean.parseBoolean(fields[4])));
                        break;
                    default:
                        System.out.printf("Invalid media type: %s, %s, %s, %s\n", fields[0], fields[1], fields[2], fields[3]);
                }
            }
            fileScanner.close();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}

    class MediaRentalSystem {
    public static final String WELCOME = "Welcome to Media Rental System";
    private Manager manager;

    private void run() {
        manager = new Manager();
        init();
    }

    private void init() {
        //Create Frame
        JFrame frame = new JFrame(WELCOME);//Frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create Menubar and Menu
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");//new menu

        //Add Menu Items
        JMenuItem loadMenuItem = new JMenuItem("Load Menu objects...");
        JMenuItem findMenuItem = new JMenuItem("Find Menu objects...");
        JMenuItem rentMenuItem = new JMenuItem("Rent Menu objects...");
        JSeparator menuSeparator = new JSeparator();
        JMenuItem quitMenuItem = new JMenuItem("Quit");

        //Add Event Listeners to the Menu Items
        loadMenuItem.addActionListener(e -> loadData(frame));
        findMenuItem.addActionListener(e -> findMedia(frame));
        rentMenuItem.addActionListener(e -> rentMedia(frame));
        quitMenuItem.addActionListener(e -> exitMediaProgram(frame));

        //Add the Menu Items in the Menu, and menu in the menu bar
        menu.add(loadMenuItem);
        menu.add(findMenuItem);
        menu.add(rentMenuItem);
        menu.add(menuSeparator);
        menu.add(quitMenuItem);
        menuBar.add(menu);

        //Set the menubar in the frame
        frame.setJMenuBar(menuBar);

        //Set frame properties and make it visible
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null); //opens the frame in center of the screen
        frame.setVisible(true);
    }

    private void findMedia(JFrame frame) {
        List<Media> selectedItem;

        do {

            //Prompt Dialog to get the media title
            String input = showInputDialog(frame, "Enter the title");//prompt title
            if (input == null || input.length() == 0)
                break;

            //Find media with the given title
            selectedItem = manager.findItems(input);//checking for title
            if (selectedItem.size() > 0) {

                //If media exists then display the media in an information pane
                String output = "";
                for (Media m : selectedItem)
                    output += m + "\n";
                output = output.substring(0, output.length() - 1);
                showMessageDialog(frame, output);
                break;
            } else {//error message
                showMessageDialog(frame, String.format("There is no media with this title: %s", input), "Error", ERROR_MESSAGE);
            }
        } while (selectedItem.isEmpty());
    }

    private void rentMedia(JFrame frame) {

        //Search media with the given id
        String input = showInputDialog(frame, "Enter the id");//prompts id
        if (input == null)
            return;
        int id;
        try {//checking for errors
            id = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            showMessageDialog(frame, String.format("%s is not numeric.", input), "Error", ERROR_MESSAGE);
            return;
        }

        //Rent the media item selected and show success/error message accordingly
        Media item = manager.rentItem(id);
        if(item != null){//message i rented or not available
            showMessageDialog(frame, String.format("Media was successfully rented. Rental fee = $%.2f", item.calculateRentalFee()));
        }
        else{
            showMessageDialog(frame, String.format("%d was not found or is not available.", id), "Error", ERROR_MESSAGE);
        }
    }

    private void loadData(JFrame frame)
    {
        //Load file using the filechooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home"))); //Set default opening position in user home
        int result = fileChooser.showOpenDialog(frame);

        //If user selected a file
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            manager.loadData(selectedFile.getAbsolutePath());
            showMessageDialog(frame, "Media Loaded Successfully.", "Files Loaded", INFORMATION_MESSAGE);
        }
    }

    // used for exiting the program
    private void exitMediaProgram(JFrame frame) {
        showMessageDialog(frame, "Good bye.", "exit", INFORMATION_MESSAGE);
        System.exit(0);
    }

    public static void main(String[] args) {
        new MediaRentalSystem().run();
    }

}