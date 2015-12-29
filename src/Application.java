import com.google.gson.Gson;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class Application {
    private JTextField fetchIDTextField;
    private JButton fetchButton;
    private JTextField userIDTextField;
    private JTextField titleTextField;
    private JTextArea bodyTextArea;
    private JPanel mainPanel;

    public Application()
    {
        //Try to set look and feel from platform
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception error) {
            error.printStackTrace();
        }

        //Make GUI
        JFrame frame = new JFrame("GCI - REST and JSON");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        fillControls();

        fetchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String json = "";
                try {
                    json = downloadJSON("http://jsonplaceholder.typicode.com/posts/" + Integer.parseInt(fetchIDTextField.getText()));
                }
                finally {
                    if (json.length() > 0) {
                        Post post = parseJSON(json);
                        if (post != null) {
                            fillControls(post);
                        } else {
                            fillControls();
                        }
                    } else {
                        fillControls();
                    }
                }
            }
        });
    }

    /** Fills controls with information from given post */
    public void fillControls(Post post)
    {
        userIDTextField.setText("User ID: " + Integer.toString(post.userId));
        titleTextField.setText("Title: " + post.title);
        bodyTextArea.setText(post.body);
    }

    /** Fills controls with blank space */
    public void fillControls()
    {
        userIDTextField.setText("User ID:");
        titleTextField.setText("Title:");
        bodyTextArea.setText("Body");
    }

    /** Uses GSON to parse JSON as string to Post object
     * r
     * @param json to parse
     * @return Post or null if couldn't parse json
     */
    public Post parseJSON(String json) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, Post.class);
        }
        catch (Exception error) {
            error.printStackTrace();
        }
        return null;
    }

    /** Downloads JSON
     *
     * @param link to json
     * @return json as string or empty string if error occurred
     */
    public String downloadJSON(String link)
    {
        try {
            URL url = new URL(link);

            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();

            int read;
            char[] load = new char[1024];

            //Load ...
            read = reader.read(load);
            while(read != -1)
            {
                buffer.append(load, 0, read);
                read = reader.read(load);
            }

            return buffer.toString();
        }
        catch (Exception error)
        {
            error.printStackTrace();
        }
        return "";
    }



    public static void main(String[] args) {
        Application application = new Application();
    }
}
